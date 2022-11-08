package org.prgrms.vouchermanagement.customer.repository;

import org.prgrms.vouchermanagement.customer.domain.BlackCustomer;
import org.prgrms.vouchermanagement.customer.domain.Customer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class BlackListCustomerRepository implements CustomerRepository {

    private final String path;

    public BlackListCustomerRepository(@Value("${repository.file.blacklist.path}") String path) {
        this.path = path;
    }

    @Override
    public List<Customer> findAll() {
        List<Customer> customers = new ArrayList<>();
        try {
            List<String> blackList = Files.readAllLines(Paths.get(path));
            blackList.forEach(customerInfo -> customers.add(createCustomer(customerInfo.split(","))));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return customers;
    }

    private Customer createCustomer(String[] customerInfos) {
        UUID customerId = UUID.fromString(customerInfos[0]);
        String name = customerInfos[1];

        return new BlackCustomer(customerId, name);
    }
}