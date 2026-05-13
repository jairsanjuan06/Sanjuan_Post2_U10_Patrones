package com.universidad.productosservice.service;

import com.universidad.productosservice.domain.Producto;
import com.universidad.productosservice.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository repo;

    public Producto procesarProducto(String n, Double p, Integer s,
            String cat, boolean activo, String proveedor) {
        Producto producto = new Producto();
        if (n == null || n.equals("")) {
            throw new IllegalArgumentException("nombre requerido");
        }
        if (p == null) {
            throw new IllegalArgumentException("precio requerido");
        } else if (p <= 0) {
            throw new IllegalArgumentException("precio invalido");
        } else if (p > 999999) {
            throw new IllegalArgumentException("precio excesivo");
        }
        if (s == null || s < 0) {
            throw new IllegalArgumentException("stock invalido");
        }
        producto.setNombre(n);
        producto.setPrecio(p);
        producto.setStock(s);
        // TODO: implementar logica de categoria y proveedor
        return repo.save(producto);
    }

    public List<Producto> listar() {
        return repo.findAll();
    }

    public Producto buscar(Long id) {
        return repo.findById(id).orElse(null);
    }
}
