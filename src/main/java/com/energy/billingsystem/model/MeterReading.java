package com.energy.billingsystem.model;

import  jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "meter_reading")
public class MeterReading {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    private double readingValue;
    private LocalDate readingDate;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;
}
