package com.universidad.productosservice.service;

import com.universidad.productosservice.domain.Producto;
import com.universidad.productosservice.repository.ProductoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class ProductoServiceTest {

    @Autowired
    private ProductoService service;

    @MockBean
    private ProductoRepository repository;

    @Test
    void rechazaNombreNulo() {
        assertThatThrownBy(() -> service.procesarProducto(null, 10.0, 3, "A", true, "Proveedor"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void rechazaNombreEnBlanco() {
        assertThatThrownBy(() -> service.procesarProducto("   ", 10.0, 3, "A", true, "Proveedor"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("El nombre no puede estar vacio");
    }

    @Test
    void rechazaPrecioNulo() {
        assertThatThrownBy(() -> service.procesarProducto("Teclado", null, 3, "A", true, "Proveedor"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("El precio debe ser mayor a cero");
    }

    @Test
    void rechazaPrecioNegativo() {
        assertThatThrownBy(() -> service.procesarProducto("Teclado", -1.0, 3, "A", true, "Proveedor"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("El precio debe ser mayor a cero");
    }

    @Test
    void rechazaPrecioExcesivo() {
        assertThatThrownBy(() -> service.procesarProducto("Teclado", 1_000_000.0, 3, "A", true, "Proveedor"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("El precio excede el maximo permitido");
    }

    @Test
    void rechazaStockNegativo() {
        assertThatThrownBy(() -> service.procesarProducto("Teclado", 10.0, -1, "A", true, "Proveedor"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("El stock no puede ser negativo");
    }

    @Test
    void buscarLanzaExcepcionSiProductoNoExiste() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.buscar(99L))
                .isInstanceOf(java.util.NoSuchElementException.class)
                .hasMessage("Producto no encontrado: 99");
    }

    @Test
    void procesaProductoValido() {
        when(repository.save(any(Producto.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Producto producto = service.procesarProducto("  Teclado  ", 120.0, 10, "A", true, "Proveedor");

        assertThat(producto.getEstado()).isEqualTo("NORMAL");
    }

    @Test
    void clasificaEstadosDeStock() {
        Producto producto = new Producto();

        producto.setStock(null);
        assertThat(producto.getEstado()).isEqualTo("DESCONOCIDO");

        producto.setStock(0);
        assertThat(producto.getEstado()).isEqualTo("AGOTADO");

        producto.setStock(3);
        assertThat(producto.getEstado()).isEqualTo("BAJO");

        producto.setStock(25);
        assertThat(producto.getEstado()).isEqualTo("ALTO");

        producto.setStock(75);
        assertThat(producto.getEstado()).isEqualTo("MUY_ALTO");

        producto.setStock(150);
        assertThat(producto.getEstado()).isEqualTo("EXCEDENTE");
    }
}
