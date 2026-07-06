package com.energy.billingsystem.service;

import com.energy.billingsystem.model.Customer;
import com.energy.billingsystem.model.MeterReading;
import com.energy.billingsystem.repository.CustomerRepository;
import com.energy.billingsystem.repository.MeterReadingRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class MeterReadingService {

    private final MeterReadingRepository meterReadingRepository;
    private final CustomerRepository customerRepository;
    private final BillService billService;

    public MeterReadingService(
            MeterReadingRepository meterReadingRepository,
            CustomerRepository customerRepository,
            BillService billService
    ) {
        this.meterReadingRepository = meterReadingRepository;
        this.customerRepository = customerRepository;
        this.billService = billService;
    }

    public MeterReading registerReading(Long customerId, double newValue, Long tariffId) {
        Customer customer = customerRepository.findById(customerId).orElseThrow(() -> new IllegalArgumentException(
                "Customer mit ID " + customerId + " nicht gefunden!"));

        Optional<MeterReading> lastReadingOpt = meterReadingRepository.findFirstByCustomerIdOrderByReadingDateDesc(customerId);

        double verbrauchKwh;

        if (lastReadingOpt.isPresent()) {
            double oldValue = lastReadingOpt.get().getReadingValue();

            if (newValue < oldValue) {
                throw new IllegalArgumentException("Der neue Zählerstand darf nicht kleríner sein als der alte (" + oldValue + " kWh)!");
            }
            verbrauchKwh = newValue - oldValue;
        } else {
            verbrauchKwh = newValue;
        }

        MeterReading newReading = new MeterReading();
        newReading.setReadingValue(newValue);
        newReading.setReadingDate(LocalDate.now());
        newReading.setCustomer(customer);

        MeterReading savedReading = meterReadingRepository.save(newReading);

        billService.createBill(customerId, tariffId, verbrauchKwh);

        return savedReading;
    }
}