package com.vb.oauth.services;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

@Service
public class ProductServices {
	public List<Products> list = new ArrayList<>();

	public ProductServices() {
		super();
		list.add(Products.builder()
				.id(UUID.randomUUID().toString())
				.name("Mobile")
				.description("Apple 16")
				.price(12345)
				.build());
	}

	// @GetMapping
	public Products addProduct(Products products) {
		products.setId(UUID.randomUUID().toString());
		list.add(products);
		return products;
	}

	public List<Products> getAllProds() {
		return list;
	}

	public boolean delete(String id) {
		return list.removeIf(p -> p.getId().equals(id));
	}
}
