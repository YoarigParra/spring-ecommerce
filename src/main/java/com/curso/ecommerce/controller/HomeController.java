package com.curso.ecommerce.controller;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.curso.ecommerce.model.Producto;
import com.curso.ecommerce.service.IProductoService;

@Controller
@RequestMapping("/")
public class HomeController {
	
	private final Logger logger=LoggerFactory.getLogger(HomeController.class);

    @Autowired
	private IProductoService productoService; 
	
	@GetMapping("")
	public String home(Model model) {
		
		model.addAttribute("productos", productoService.findAll());
		
		return "usuario/home";
	}
	
	@GetMapping("productoHome/{id}")
	public String productoHome(@PathVariable Integer id, Model model) {
		
        Producto producto=new Producto();
        Optional<Producto> productoOptional= productoService.get(id);
        producto = productoOptional.get();
        
        model.addAttribute("producto",producto);
		
		logger.info("Id del producto enviado {}",id);
		
		return "usuario/productohome";
	}
	
	@PostMapping("cart")
	public String addCart() {
		
		
		return"usuario/carrito";
	}
}
