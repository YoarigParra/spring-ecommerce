package com.curso.ecommerce.controller;

import java.util.ArrayList;
import java.util.List;
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
import org.springframework.web.bind.annotation.RequestParam;

import com.curso.ecommerce.model.DetalleOrden;
import com.curso.ecommerce.model.Orden;
import com.curso.ecommerce.model.Producto;
import com.curso.ecommerce.model.Usuario;
import com.curso.ecommerce.service.IProductoService;
import com.curso.ecommerce.service.IUsuarioService;


@Controller
@RequestMapping("/")
public class HomeController {
	
	private final Logger logger=LoggerFactory.getLogger(HomeController.class);
	
	//Para guardar los detalles de la orden
	List<DetalleOrden> detalle=new ArrayList<DetalleOrden>();
	
	//datos de la orden
	Orden orden=new Orden();

    @Autowired
	private IProductoService productoService; 
    
    @Autowired
    private IUsuarioService usuarioService;
	
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
	public String addCart(@RequestParam Integer id, @RequestParam Integer cantidad, Model model) {
		DetalleOrden detalleOrden=new DetalleOrden();
		Producto producto=new Producto();
		double sumaTotal=0;
		
		Optional<Producto> optionalProducto= productoService.get(id);
		
		logger.info("Producto añadido: {}", optionalProducto.get());
		logger.info("Cantidad: {}",cantidad);
		producto=optionalProducto.get();
		
		detalleOrden.setCantidad(cantidad);
		detalleOrden.setPrecio(producto.getPrecio());
		detalleOrden.setNombre(producto.getNombre());
        detalleOrden.setTotal(producto.getPrecio()*cantidad);
        detalleOrden.setProducto(producto);
        
        // validar que el producto no se añada dos veces
        Integer idProducto=producto.getId();
        boolean ingresado=detalle.stream().anyMatch(p -> p.getProducto().getId()==idProducto);
        
        if (!ingresado) {
        	  detalle.add(detalleOrden);
        	
        }
        
        sumaTotal=detalle.stream().mapToDouble(dt->dt.getTotal()).sum();
        
        orden.setTotal(sumaTotal);
        model.addAttribute("cart", detalle);
        model.addAttribute("orden", orden);
        
		return"usuario/carrito";
	}
	
	//Quitar un productos del carrito
	@GetMapping ("/delete/cart/{id}")
	public String deleteProductocart(@PathVariable Integer id,Model model) {
		
		//lista nueva de productos
		List<DetalleOrden> ordenesNuevas=new ArrayList<DetalleOrden>();
		
		for(DetalleOrden detalleOrden: detalle) {
			
			if(detalleOrden.getProducto().getId()!=id) {
				ordenesNuevas.add(detalleOrden);
			}
		}
		
		//poner la nueva lista de productos restantes
		detalle=ordenesNuevas;
		
		double sumaTotal=0;
		
        sumaTotal=detalle.stream().mapToDouble(dt->dt.getTotal()).sum();
        
        orden.setTotal(sumaTotal);
        model.addAttribute("cart", detalle);
        model.addAttribute("orden", orden);
		
		return "usuario/carrito";
	}
	
	@GetMapping("/getCart")
	public String getCart (Model model) {
		 model.addAttribute("cart", detalle);
	     model.addAttribute("orden", orden);
		return "/usuario/carrito";
	}
	
	@GetMapping ("/order")
	public String order(Model model) {
		
		Usuario usuario=usuarioService.findById(1).get();
		
		 model.addAttribute("cart", detalle);
	     model.addAttribute("orden", orden); 
		 model.addAttribute("usuario", usuario);
	     
		return "usuario/resumenorden";
	}
	
}
