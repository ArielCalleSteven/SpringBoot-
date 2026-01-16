package ec.edu.ups.icc.fundamentos01.products.controllers;

import ec.edu.ups.icc.fundamentos01.products.dtos.*;
import ec.edu.ups.icc.fundamentos01.products.services.ProductService;
import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public Object getAll() {
        return productService.findAll();
    }

    @GetMapping("/{id}")
    public Object getById(@PathVariable int id) {
        return productService.findOne(id);
    }

    @PostMapping
    public Object create(@Valid @RequestBody CreateProductDto product) {
        return productService.create(product);
    }

    @PutMapping("/{id}")
    public Object update(@PathVariable int id, @Valid @RequestBody UpdateProductDto product) {
        return productService.update(id, product);
    }

    @PatchMapping("/{id}")
    public Object partialUpdate(@PathVariable int id, @RequestBody PartialUpdateProductDto product) {
        return productService.partialUpdate(id, product);
    }

    @DeleteMapping("/{id}")
    public Object delete(@PathVariable int id) {
        return productService.delete(id);
    }

    @PostMapping("/validate-name")
    public ResponseEntity validateName(@RequestBody ValidateProductNameDto dto) {
        return ResponseEntity.ok(null);
    }
}