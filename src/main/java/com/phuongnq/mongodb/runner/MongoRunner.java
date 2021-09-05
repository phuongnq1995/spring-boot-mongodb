package com.phuongnq.mongodb.runner;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.phuongnq.mongodb.entity.Customer;
import com.phuongnq.mongodb.service.MongoService;

@Component
@Order(value = Ordered.LOWEST_PRECEDENCE)
public class MongoRunner implements CommandLineRunner {

	@Autowired
	private MongoService mongoService;

	@Override
	public void run(String... args) throws Exception {

		Customer customer1 = new Customer("Cus1", 25);
		Customer customer2 = new Customer("Cus2", 35);

		mongoService.createCustomer(customer1);
		mongoService.createCustomer(customer2);

		System.out.println("---- PRINT ALL -----");
		mongoService.getCustomers().stream().forEach(System.out::println);

		mongoService.updateCustomer("Cus1", new Customer("Cus1Updated", 20));

		System.out.println("---- PRINT ALL -----");
		mongoService.getCustomers().stream().forEach(System.out::println);

		List<Customer> customers = new ArrayList<>();

		IntStream.range(0, 100).forEach(i -> {
			customers.add(new Customer("Cusbulk"+i, i));
		});

		System.out.println("customers:"+customers.size());
		// bulk size -> 50 for list of 100 elements
		mongoService.insertBulk(customers, 50);

		System.out.println("---- PRINT ALL -----");
		mongoService.getCustomers().stream().forEach(System.out::println);
	}

}
