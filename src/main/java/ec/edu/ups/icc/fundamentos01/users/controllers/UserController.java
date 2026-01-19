package ec.edu.ups.icc.fundamentos01.users.controllers;

import ec.edu.ups.icc.fundamentos01.users.entities.UserEntity;
import ec.edu.ups.icc.fundamentos01.users.repositories.UserRepository;
import ec.edu.ups.icc.fundamentos01.users.services.UserService; 
import ec.edu.ups.icc.fundamentos01.products.dtos.ProductResponseDto; 
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users") 
public class UserController {

    private final UserRepository userRepo; 
    private final UserService userService; 

    public UserController(UserRepository userRepo, UserService userService) {
        this.userRepo = userRepo;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserEntity> create(@RequestBody UserEntity user) {
        UserEntity saved = userRepo.save(user);
        return ResponseEntity.ok(saved);
    }

    @GetMapping
    public ResponseEntity<List<UserEntity>> findAll() {
        return ResponseEntity.ok(userRepo.findAll());
    }

    @GetMapping("/{id}/products")
    public ResponseEntity<List<ProductResponseDto>> getProductsByUser(@PathVariable Long id) {
        List<ProductResponseDto> products = userService.getProductsByUserId(id);
        return ResponseEntity.ok(products);
    }


    @GetMapping("/{id}/products-v2")
    public ResponseEntity<List<ProductResponseDto>> getUserProductsV2(
            @PathVariable Long id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) Long categoryId
    ) {
        List<ProductResponseDto> products = userService.getProductsByUserIdWithFilters(
                id, name, minPrice, maxPrice, categoryId
        );
        return ResponseEntity.ok(products);
    }
}