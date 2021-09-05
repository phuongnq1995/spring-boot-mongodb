package com.phuongnq.mongodb.service;

import java.util.List;

import com.phuongnq.mongodb.entity.Customer;

public interface MongoService {

	void createCustomer(Customer customer);

	void updateCustomer(String name, Customer customer);

	void insertBulk(List<Customer> customers, int bulkSize);

	Customer getCustomer(String name);

	List<Customer> getCustomers();
}
