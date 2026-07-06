package com.energy.billingsystem.controller;


import com.energy.billingsystem.model.MeterReading;
import com.energy.billingsystem.service.MeterReadingService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/readings")
public class MeterReadingController {

    private final MeterReadingService meterReadingService;

    public MeterReadingController(MeterReadingService meterReadingService) {
        this.meterReadingService = meterReadingService;
    }

    @PostMapping("/register")
    public String registerReading(
            @RequestParam Long customerId,
            @RequestParam double newValue,
            @RequestParam Long tariffId
    ) {
        try {
            MeterReading reading = meterReadingService.registerReading(customerId, newValue, tariffId);

            return "Zählerstand " + reading.getReadingValue() + " kWh wurde erfolgreich registriert! "
                    + "Eine neue Rechnung wurde automatisch generiert.";
        } catch (IllegalArgumentException e) {
            return "Fehler: " + e.getMessage();
        }
    }
}
