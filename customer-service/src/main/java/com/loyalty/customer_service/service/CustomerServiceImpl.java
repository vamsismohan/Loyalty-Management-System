package com.loyalty.customer_service.service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.loyalty.customer_service.dto.AddressRequest;
import com.loyalty.customer_service.dto.CustomerDto;
import com.loyalty.customer_service.dto.CustomerRequest;
import com.loyalty.customer_service.dto.CustomerResponse;
import com.loyalty.customer_service.entity.Customer;
import com.loyalty.customer_service.entity.CustomerAddress;
import com.loyalty.customer_service.exception.CustomerAlreadyExistsException;
import com.loyalty.customer_service.exception.CustomerBlockedException;
import com.loyalty.customer_service.exception.CustomerNotFoundException;
import com.loyalty.customer_service.exception.ResourceNotFoundException;
import com.loyalty.customer_service.repository.CustomerRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CustomerServiceImpl implements CustomerService {

    private CustomerRepository repository;

    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.repository = customerRepository;
    }

    private static final String ACTIVE = "ACTIVE";
    private static final String BLOCKED = "BLOCKED";
    private static final String INACTIVE = "INACTIVE";

    @Override
    public CustomerResponse createCustomer(CustomerRequest request) {

        Optional<Customer> existing = repository.findByEmail(request.getEmail());
        if (existing.isPresent()) {
            if (existing.get().getStatus().equals(ACTIVE)) {
                throw new CustomerAlreadyExistsException(request.getEmail());
            } else if (existing.get().getStatus().equals(INACTIVE)) {
                updateCustomer(existing.get().getCustomerNumber(), request);
            } else if (existing.get().getStatus().equals(BLOCKED)) {
                throw new CustomerBlockedException("Customer account is blocked. Please contact support.");
            }
        }

        Customer customer = new Customer();
        customer.setCustomerNumber(generateCustomerNumber());
        customer.setFirstName(request.getFirstName());
        customer.setLastName(request.getLastName());
        customer.setEmail(request.getEmail());
        customer.setPhone(request.getPhone());
        customer.setDob(request.getDob());
        customer.setCountry(request.getCountry());
        customer.setStatus(ACTIVE);
        customer.setCreatedAt(LocalDateTime.now());

        repository.save(customer);

        return mapToResponse(customer);
    }

    @Override
    public CustomerResponse getCustomer(String customerNumber) {
        Customer customer = repository.findById(customerNumber)
                .orElseThrow(() -> new CustomerNotFoundException(customerNumber));

        return mapToResponse(customer);
    }

    @Override
    public CustomerResponse updateCustomer(String customerNumber,
            CustomerRequest request) {

        Optional<Customer> optional = repository.findById(customerNumber);

        if (optional.isEmpty()) {
            throw new CustomerNotFoundException(customerNumber);
        }

        Customer customer = optional.get();

        if (customer.getStatus().equals(BLOCKED)) {
            throw new CustomerBlockedException("Customer account is blocked. Please contact support.");
        }

        customer.setFirstName(request.getFirstName());
        customer.setLastName(request.getLastName());
        customer.setPhone(request.getPhone());
        customer.setCountry(request.getCountry());
        customer.setStatus(ACTIVE);

        // Update addresses
        customer.getAddresses().clear();

        if (request.getAddresses() != null) {
            for (AddressRequest addrReq : request.getAddresses()) {
                CustomerAddress address = new CustomerAddress();
                address.setAddressLine(addrReq.getAddressLine());
                address.setCity(addrReq.getCity());
                address.setCountry(addrReq.getCountry());
                address.setPostalCode(addrReq.getPostalCode());
                address.setCreatedAt(LocalDateTime.now());
                address.setCustomer(customer);
                customer.getAddresses().add(address);
            }
        }

        repository.save(customer);

        return mapToResponse(customer);
    }

    private CustomerResponse mapToResponse(Customer customer) {
        CustomerResponse response = new CustomerResponse();
        response.setCustomerNumber(customer.getCustomerNumber());
        response.setFirstName(customer.getFirstName());
        response.setLastName(customer.getLastName());
        response.setEmail(customer.getEmail());
        response.setStatus(customer.getStatus());
        return response;
    }

    private String generateCustomerNumber() {
        return "CUST-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    public void deleteCustomer(String customerNumber) {

        Customer customer = repository.findById(customerNumber)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Customer not found"));

        customer.setStatus(INACTIVE);
        repository.save(customer);
    }

    @Override
    public CustomerDto getByEmail(String email) {

        log.info("Member Enrollment check using email: {}", email);
        Customer customer = repository.findByEmail(email)
                .orElseThrow(() -> new CustomerNotFoundException("customerNotFound"));

        if (BLOCKED.equals(customer.getStatus())) {
            throw new CustomerBlockedException("Customer account is blocked. Please contact support.");
        }
        CustomerDto cusDto = new CustomerDto();
        cusDto.setCustomerNumber(customer.getCustomerNumber());
        cusDto.setEmail(customer.getEmail());
        cusDto.setStatus(customer.getStatus());

        return cusDto;
    }

}
