package com.energy.billingsystem.controller;

import com.energy.billingsystem.service.CustomerService;
import com.energy.billingsystem.model.Customer;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class HelloController {

    private final CustomerService customerService;

    public HelloController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/kunden-all")
    public List<Customer> showAllCustomers() {
        return customerService.getAllCustomers();
    }

    @PostMapping("/add-kunde")
    public String addKunde(
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String email,
            @RequestParam String address,
            @RequestParam String meterNumber
    ) {
        customerService.createKunde(firstName, lastName, email, address, meterNumber);

        return "Kunde " + firstName + " " + lastName + " wurde erfolgreich in der XAMPP-Datenbank gespeichert!";
    }

    @GetMapping("/kunde-find")
    public Customer findKunde(@RequestParam Long id) {
        return customerService.findCustomerById(id);
    }

    @DeleteMapping("/kunde-delete")
    public String deleteKunde(@RequestParam Long id) {
        boolean isDeleted = customerService.deleteCustomerById(id);

        if (isDeleted) {
            return "Kunde mit ID " + id + " wurde erfolgreich aus der Datenbank gelöscht!";
        } else {
            return "Kunde mit ID " + id + " wurde in der Datenbank nicht gefunden.";
        }
    }

}