package com.energy.billingsystem.service;

import com.energy.billingsystem.model.Bill;
import com.energy.billingsystem.model.Customer;
import com.energy.billingsystem.model.Tariff;
import com.energy.billingsystem.repository.BillRepository;
import com.energy.billingsystem.repository.CustomerRepository;
import com.energy.billingsystem.repository.TariffRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class BillService {

    public final BillRepository billRepository;
    public  final CustomerRepository customerRepository;
    public final TariffRepository tariffRepository;

    public BillService(BillRepository billRepository, CustomerRepository customerRepository, TariffRepository tariffRepository) {
        this.billRepository = billRepository;
        this.customerRepository = customerRepository;
        this.tariffRepository = tariffRepository;
    }

    public Bill createBill(Long customerId, Long tariffId, double kwh) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Kunde mit ID " + customerId + " nicht gefunden!"));

        Tariff tariff = tariffRepository.findById(tariffId)
                .orElseThrow(() -> new IllegalArgumentException("Tarif mit ID " + tariffId + " nicht gefunden!"));

        double berechneterBetrag = kwh * tariff.getPreisProKwh();

        Bill newBill = new Bill();
        newBill.setBetrag(berechneterBetrag);
        newBill.setRechnungsdatum(LocalDate.now());
        newBill.setBezahlt(false);
        newBill.setCustomer(customer);
        newBill.setTariff(tariff);

        return billRepository.save(newBill);
    }

    public List<Bill> getBillsByCustomer(Long customerId) {
        return billRepository.findByCustomerId(customerId);
    }

    public Bill payBill(Long billId) {
        Bill bill = billRepository.findById(billId).
                orElseThrow(() -> new IllegalArgumentException("Rehcnung mit ID " + billId + "nicht gefunden!"));

        bill.setBezahlt(true);

        return billRepository.save(bill);
    }

    public double getCustomerTotalDebt(Long customerId) {
        List<Bill> unpaiBills = billRepository.findByCustomerIdAndBezahlt(customerId, false);

        double totalDebt = 0.0;
        for (Bill bill : unpaiBills) {
            totalDebt += bill.getBetrag();
        }
        return totalDebt;
    }

    public double getTotalCompanyUnpaidAmount() {
        List<Bill> allUnpaidBills = billRepository.findByBezahlt(false);

        return allUnpaidBills.stream()
                .mapToDouble(Bill::getBetrag)
                .sum();
    }
}