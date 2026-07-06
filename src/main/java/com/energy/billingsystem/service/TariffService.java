package com.energy.billingsystem.service;

import com.energy.billingsystem.model.Tariff;
import com.energy.billingsystem.repository.TariffRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Service
public class TariffService {

    private TariffRepository tariffRepository;

    public TariffService(TariffRepository tariffRepository) {
        this.tariffRepository = tariffRepository;
    }

    public Tariff createTariff(String name, double preisProKwh) {
        Tariff tariff = new Tariff();

        tariff.setName(name);
        tariff.setPreisProKwh(preisProKwh);

        return tariffRepository.save(tariff);
    }


    public List<Tariff> getAllTariffs() {
        return tariffRepository.findAll();
    }

    @PostConstruct
    public void initDefaultTariffs() {
        if (tariffRepository.count() == 0) {
            createTariff("Basisstrom", 0.35);
            createTariff("Oekostrom", 0.42);
            System.out.println(">> [TariffService] Standard-Tarife wurden automatisch hinzugefügt!");
        }
    }
}