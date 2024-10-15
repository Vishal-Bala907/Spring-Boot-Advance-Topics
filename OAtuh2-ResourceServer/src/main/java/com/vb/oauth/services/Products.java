package com.vb.oauth.services;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
public class Products {
	private String id;
	private String name;
	private String description;
	private double price;
}
