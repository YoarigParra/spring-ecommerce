package com.curso.ecommerce.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.curso.ecommerce.model.Producto;
import com.curso.ecommerce.repository.IProductoRepository;

@Service
public class ProductoServiceImpl implements IProductoService {
	
	@Autowired
	private IProductoRepository productorepository;

	@Override
	public Producto save(Producto producto) {
		return productorepository.save(producto);
	}

	@Override
	public Optional<Producto> get(Integer id) {
		return productorepository.findById(id);
	}

	@Override
	public void update(Producto producto) {
		productorepository.save(producto);
	}

	@Override
	public void delate(Integer id) {
	   productorepository.deleteById(id);
	}

}
