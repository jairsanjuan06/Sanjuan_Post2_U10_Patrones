package com.universidad.productosservice.service;

import com.universidad.productosservice.repository.ProductoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
}
