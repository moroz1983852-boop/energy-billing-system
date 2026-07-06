package com.energy.billingsystem.repository;

import com.energy.billingsystem.model.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {

    List<Bill> findByCustomerId(Long customerId);

    List<Bill> findByCustomerIdAndBezahlt(Long customerId, boolean bezahlt);

    List<Bill> findByBezahlt(boolean bezahlt);
}