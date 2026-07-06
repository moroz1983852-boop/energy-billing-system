const customerForm = document.getElementById('customerForm');

if (customerForm) {

  function loadCustomersTable() {
    fetch('/api/kunden-all')
      .then(res => res.json())
      .then(customers => {

        const table = document.getElementById('customersTable');
        const tableBody = document.getElementById('customersTableBody');
        const noMessage = document.getElementById('noCustomersMessage');

        tableBody.innerHTML = '';

        if (customers.length === 0 ) {
          noMessage.innerText = 'Keine Kunden im System gefunden.';
          table.style.display = 'none';
          return;
        }

        customers.forEach(kunde => {
          const row = document.createElement('tr');

          row.innerHTML = `
            <td>${kunde.id}</td>
            <td>${kunde.firstName}</td>
            <td>${kunde.lastName}</td>
            <td>${kunde.email}</td>
            <td>${kunde.address}</td>
            <td>${kunde.meterNumber}</td>
            `;

          tableBody.appendChild(row);
        });

        noMessage.style.display = 'none';
        table.style.display = 'table';

      })
      .catch(error => {
        console.log('Fehler:', error);
        document.getElementById('noCustomersMessage').innerText = 'Fehler beim Laden der Kundenliste!';
      });
  }

  loadCustomersTable();

  customerForm.addEventListener('submit', function(event) {
    event.preventDefault();

    const firstName = document.getElementById('firstName').value;
    const lastName = document.getElementById('lastName').value;
    const email = document.getElementById('email').value;
    const address = document.getElementById('address').value;
    const meterNumber = document.getElementById('meterNumber').value;

    const url = `/api/add-kunde?firstName=${encodeURIComponent(firstName)}&lastName=${encodeURIComponent(lastName)}&email=${encodeURIComponent(email)}&address=${encodeURIComponent(address)}&meterNumber=${encodeURIComponent(meterNumber)}`;

    fetch(url, {method: 'POST'})
      .then(responce => responce.text())
      .then(textVonJava => {
        const resultDiv = document.getElementById('customerResult');
        resultDiv.innerText = textVonJava;
        resultDiv.className = 'result-message success';
        resultDiv.style.display = 'block';
        customerForm.reset();

        loadCustomersTable();
      })
      .catch(error => {
        const resultDiv = document.getElementById('customerRsult');
        resultDiv.innerText = 'Verbindungsfehler zum Java-Server!';
        resultDiv.className = 'result-message error';
        resultDiv.style.display = 'block';
      });
  });
}

const billForm = document.getElementById('billForm');

if (billForm) {
  billForm.addEventListener('submit', function(event) {
    event.preventDefault();

    const customerId = document.getElementById('billCustomerId').value;
    const tariffId = document.getElementById('billTariffId').value;
    const kwh = document.getElementById('billKwh').value;

    fetch(`/api/bills/create?customerId=${customerId}&tariffId=${tariffId}&kwh=${kwh}`, {method: 'POST'})
      .then(res => res.text())
      .then(data => {
        const resDiv = document.getElementById('billResult');
        resDiv.innerText = data;
        resDiv.className = 'result-message ' + (data.includes('Fehler') ? 'error' : 'success');
        resDiv.style.display = 'block';
        if (!data.includes('Fehler')) billForm.reset();
      });
  });
}

const payButton = document.getElementById('payButton');

if (payButton) {
  payButton.addEventListener('click', function () {
    const billId = document.getElementById('payBillId').value;
    if (!billId) return alert('Bitte Rechnungs-ID eingeben!');

    fetch(`/api/bills/pay?billId=${billId}`, {method: 'POST'})
      .then(res => res.text())
      .then(data => {
        const resDiv = document.getElementById('payResult');
        resDiv.innerText = data;
        resDiv.className = 'result-message ' + (data.includes('Fehler') ? 'error' : 'success');
        resDiv.style.display = 'block';
      });
  });
}

const unpaidButton = document.getElementById('unpaidButton');

if (unpaidButton) {
  unpaidButton.addEventListener('click', function () {
    fetch('/api/bills/analytics/company-unpaid')
      .then(res => res.text())
      .then(data => {
        const analyticsDiv = document.getElementById('analyticsResult');

        analyticsDiv.innerText = `Der gesamte offene Betrag (unbezahlte Rechnungen) im System bertägt: ${data} €`;

        analyticsDiv.style.display = 'block';

      })
      .catch(error => {
        console.error('Fehler:', error);
      });
  });
}