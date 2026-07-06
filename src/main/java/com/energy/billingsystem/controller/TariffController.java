package com.energy.billingsystem.controller;

import com.energy.billingsystem.model.Tariff;
import com.energy.billingsystem.repository.TariffRepository;
import com.energy.billingsystem.service.TariffService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tariffs")
public class TariffController {

    private final TariffService tariffService;

    public TariffController(TariffService tariffService) {
        this.tariffService = tariffService;
    }

    @GetMapping("/all")
    public List<Tariff> getAllTariffs() {
        return tariffService.getAllTariffs();
    }

    @PostMapping("/create")
    public String createTariff(@RequestParam String name, @RequestParam double preisProKwh) {
        Tariff createdTariff = tariffService.createTariff(name, preisProKwh);
        return "Tariff " + createdTariff.getName() + "mit dem Preis "
                + createdTariff.getPreisProKwh() + " €/kWh wurde erfolgreich erstellt!";
    }
}