package com.energy.billingsystem.repository;


import com.energy.billingsystem.model.MeterReading;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MeterReadingRepository extends JpaRepository<MeterReading, Long> {

    Optional<MeterReading> findFirstByCustomerIdOrderByReadingDateDesc(Long customerId);
}
