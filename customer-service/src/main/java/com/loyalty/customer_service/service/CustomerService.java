package com.loyalty.customer_service.service;

import com.loyalty.customer_service.dto.CustomerRequest;
import com.loyalty.customer_service.dto.CustomerResponse;

public interface CustomerService {

    CustomerResponse createCustomer(CustomerRequest request);
    CustomerResponse getCustomer(String customerNumber);
    CustomerResponse updateCustomer(String customerNumber, CustomerRequest request);
    void deleteCustomer(String customerNumber);

}
