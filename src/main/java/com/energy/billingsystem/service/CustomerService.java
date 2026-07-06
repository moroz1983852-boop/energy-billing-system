package com.energy.billingsystem.service;

import com.energy.billingsystem.model.Customer;
import com.energy.billingsystem.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public void createKunde(
            String firstName,
            String lastName,
            String email,
            String address,
            String meterNumber
    ) {
        Customer newKunde = new Customer();
        newKunde.setFirstName(firstName);
        newKunde.setLastName(lastName);
        newKunde.setEmail(email);
        newKunde.setAddress(address);
        newKunde.setMeterNumber(meterNumber);

        customerRepository.save(newKunde);
    }

    public Customer findCustomerById(Long searchId) {
        return customerRepository.findById(searchId).orElse(null);
    }

    public boolean deleteCustomerById(Long idForDelete) {
        if (customerRepository.existsById(idForDelete)) {
            customerRepository.deleteById(idForDelete);
            return true;
        }
        return false;
    }
}