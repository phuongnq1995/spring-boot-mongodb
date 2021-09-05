package com.phuongnq.mongodb.runner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.stereotype.Component;

import com.phuongnq.mongodb.entity.Customer;

@Component
@Order(value = 1000)
public class InitMongoDb implements CommandLineRunner {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public void run(String... args) throws Exception {

		// This operation only for demo, should not drop collection programmatically
		mongoTemplate.dropCollection(Customer.class);

		if (!mongoTemplate.collectionExists(Customer.class)) {
			mongoTemplate.createCollection(Customer.class);
		}

		
		// Always index on the query fields
		mongoTemplate.indexOps(Customer.class).ensureIndex(new Index("name", Direction.ASC));
	}

}
