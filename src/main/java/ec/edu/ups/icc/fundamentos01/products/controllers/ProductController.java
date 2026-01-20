package ec.edu.ups.icc.fundamentos01.products.controllers;

import ec.edu.ups.icc.fundamentos01.products.dtos.*;
import ec.edu.ups.icc.fundamentos01.products.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page; 
import org.springframework.data.domain.Slice; 
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
    public ResponseEntity<Page<ProductResponseDto>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,asc") String[] sort) {
        return ResponseEntity.ok(productService.findAll(page, size, sort));
    }

    @GetMapping("/slice")
    public ResponseEntity<Slice<ProductResponseDto>> findAllSlice(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,asc") String[] sort) {
        return ResponseEntity.ok(productService.findAllSlice(page, size, sort));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<ProductResponseDto>> findWithFilters(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt,desc") String[] sort) {
        return ResponseEntity.ok(productService.findWithFilters(name, minPrice, maxPrice, categoryId, page, size, sort));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<ProductResponseDto>> findByUserId(
            @PathVariable Long userId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt,desc") String[] sort) {
        return ResponseEntity.ok(productService.findByUserIdWithFilters(userId, name, minPrice, maxPrice, categoryId, page, size, sort));
    }

    // ========================================================================
    // MÉTODOS ANTIGUOS QUE YA TENÍAS (SE MANTIENEN IGUAL)
    // ========================================================================

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