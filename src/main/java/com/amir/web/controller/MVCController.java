package com.amir.web.controller;

import com.amir.web.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
public class MVCController {

	@Autowired
	private final ProductService productService;

	public MVCController(ProductService productService) {
		this.productService = productService;
	}



	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String viewProducts(ModelMap model) {

		model.addAttribute("products", productService.getProducts());
		return "productsView";
	}

}