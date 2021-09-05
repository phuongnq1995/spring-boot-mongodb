package com.phuongnq.mongodb.service.impl;

import static org.springframework.data.mongodb.core.query.Criteria.where;

import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.BulkOperations.BulkMode;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.phuongnq.mongodb.entity.Customer;
import com.phuongnq.mongodb.service.MongoService;

@Service
public class MongoServiceImpl implements MongoService {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Autowired
	private ObjectMapper mapper;

	@Override
	public void createCustomer(Customer customer) {
		mongoTemplate.insert(customer);
	}

	@Override
	public void updateCustomer(String name, Customer customer) {
		mongoTemplate.upsert(getQueryByName(name), getUpdateFromCustomer(customer), Customer.class);
	}

	@Override
	public Customer getCustomer(String name) {
		return mongoTemplate.findOne(getQueryByName(name), Customer.class);
	}

	@Override
	public List<Customer> getCustomers() {
		return mongoTemplate.findAll(Customer.class);
	}

	@Override
	public void insertBulk(List<Customer> customers, int bulkSize) {

		// Initial bulk operation
		// By default bulk has max operation is 1000
		BulkOperations bulkOperations = mongoTemplate.bulkOps(BulkMode.UNORDERED, Customer.class);

		int bulkCount = 0;
		for (Customer cus : customers) {

			bulkOperations.insert(getDocumentFromCustomer(cus));
			bulkCount++;

			if (bulkCount == bulkSize) {
				// Execute and reset
				bulkOperations.execute();
				bulkOperations = mongoTemplate.bulkOps(BulkMode.UNORDERED, Customer.class);
				System.out.println(String.format("Execute bulk with %s elements.", bulkCount));
				bulkCount = 0;
			}
		}

		if (bulkCount != 0) {
			bulkOperations.execute();
			System.out.println(String.format("Execute bulk with %d elements.", bulkCount));
		}
	}

	private static Query getQueryByName(String name) {
		return Query.query(where("name").is(name));
	}

	private Update getUpdateFromCustomer(Customer customer) {
		return Update.fromDocument(getDocumentFromCustomer(customer));
	}

	private Document getDocumentFromCustomer(Customer customer) {
		Map<String, Object> map = mapper.convertValue(customer, new TypeReference<Map<String, Object>>() {
		});
		map.remove("id");
		return new Document(map);
	}
}
