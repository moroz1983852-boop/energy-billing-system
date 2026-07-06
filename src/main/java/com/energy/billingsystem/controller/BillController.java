package com.energy.billingsystem.controller;

import com.energy.billingsystem.model.Bill;
import com.energy.billingsystem.service.BillService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bills")
public class BillController {

    private final BillService billService;

    public BillController(BillService billService) {
        this.billService = billService;
    }

    @PostMapping("/create")
    public String createNewBill(
            @RequestParam Long customerId,
            @RequestParam Long tariffId,
            @RequestParam double kwh
    ) {
        try {
            Bill createdBill = billService.createBill(customerId, tariffId, kwh);
            return "Rechnung mit ID " + createdBill.getId() + " über " + createdBill.getBetrag() +
                    " € wurde erfolgreich für den Kunden mit ID " + customerId + " erstellt! (Tariff: "
                    + createdBill.getTariff().getName() + ", Verbrauch: " + kwh + " Kwh";
        } catch (IllegalArgumentException e) {
            return "Fehler: " + e.getMessage();
        }
    }

    @GetMapping("/customer")
    public List<Bill> getBillsByCustomer(@RequestParam Long customerId) {
        return billService.getBillsByCustomer(customerId);
    }

    @PostMapping("/pay")
    public String payBill(@RequestParam Long billId) {
        try {
            Bill paidBill = billService.payBill(billId);
            return "Rechnung mit ID " + paidBill.getId() + " wurde erfolgreich bezahlt!";
        } catch (IllegalArgumentException e) {
            return "Fehler: " + e.getMessage();
        }
    }

    @GetMapping("/analytics/customer-debt")
    public String getCustomerDebt(@RequestParam Long customerId) {
        double debt = billService.getCustomerTotalDebt(customerId);

        return "Die Gesamtschulden für den Kunden mit ID " + customerId + " betragen: " + debt + " €.";
    }

    @GetMapping("/analytics/company-unpaid")
    public String getCompanyUnpaidAmount() {
        double totalUnpaid = billService.getTotalCompanyUnpaidAmount();

        return "Der gesamte offebe Betrag (unbezahlte Rechnungen) im System beträgt: " + totalUnpaid + " €.";
    }
}