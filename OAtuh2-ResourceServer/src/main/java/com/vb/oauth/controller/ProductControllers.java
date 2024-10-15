package com.vb.oauth.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.vb.oauth.services.ProductServices;
import com.vb.oauth.services.Products;

@RestController
@RequestMapping("api/v1/products")
public class ProductControllers {

	@Autowired
	private ProductServices services;

	// create
	@PostMapping("/add")
	public Products create(@RequestBody Products products) {
		return services.addProduct(products);
	}

	@GetMapping("/get-all")
	@CrossOrigin(value = "http://localhost:5173",methods = RequestMethod.GET)
	public List<Products> getAllProds() {
		return services.getAllProds();
	}

	@GetMapping("/del/{id}")
	public boolean delete(@PathVariable("id") String id) {
		return services.delete(id); // list.removeIf(p->p.getId().equals(id));
	}

}
