package com.curso.ecommerce.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.curso.ecommerce.model.Producto;
import com.curso.ecommerce.service.IProductoService;

@Controller
@RequestMapping("/administrador")
public class AdministradorController {
    
    @Autowired
	private IProductoService productosService;
	
	
	
	@GetMapping("")
	public String home(Model model) {

		List<Producto> productos=productosService.findAll();
		model.addAttribute("productos", productos);
		
		
		return "administrador/home";
	}
}