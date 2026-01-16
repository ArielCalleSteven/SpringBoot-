package ec.edu.ups.icc.fundamentos01.categories.controllers;

import ec.edu.ups.icc.fundamentos01.categories.entities.CategoryEntity;
import ec.edu.ups.icc.fundamentos01.categories.repositories.CategoryRepository;
import ec.edu.ups.icc.fundamentos01.products.repositories.ProductRepository; 
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories") 
public class CategoryController {

    private final CategoryRepository categoryRepo;
    private final ProductRepository productRepo; 

    public CategoryController(CategoryRepository categoryRepo, ProductRepository productRepo) {
        this.categoryRepo = categoryRepo;
        this.productRepo = productRepo;
    }

    @PostMapping
    public ResponseEntity<CategoryEntity> create(@RequestBody CategoryEntity category) {
        CategoryEntity saved = categoryRepo.save(category);
        return ResponseEntity.ok(saved);
    }

    @GetMapping
    public ResponseEntity<List<CategoryEntity>> findAll() {
        return ResponseEntity.ok(categoryRepo.findAll());
    }

    @GetMapping("/{id}/products/count")
    public long countProductsByCategory(@PathVariable Long id) {
        return productRepo.countByCategories_Id(id);
    }
}