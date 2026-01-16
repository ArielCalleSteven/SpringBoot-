package ec.edu.ups.icc.fundamentos01.products.services;

import ec.edu.ups.icc.fundamentos01.products.dtos.CreateProductDto;
import ec.edu.ups.icc.fundamentos01.products.dtos.ProductResponseDto;
import ec.edu.ups.icc.fundamentos01.products.dtos.UpdateProductDto;
import ec.edu.ups.icc.fundamentos01.products.dtos.PartialUpdateProductDto; 
import ec.edu.ups.icc.fundamentos01.products.entities.ProductEntity;
import ec.edu.ups.icc.fundamentos01.products.repositories.ProductRepository;
import ec.edu.ups.icc.fundamentos01.users.repositories.UserRepository;
import ec.edu.ups.icc.fundamentos01.categories.repositories.CategoryRepository;
import ec.edu.ups.icc.fundamentos01.users.entities.UserEntity;
import ec.edu.ups.icc.fundamentos01.categories.entities.CategoryEntity;
import ec.edu.ups.icc.fundamentos01.products.mappers.ProductMapper;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepo;
    private final UserRepository userRepo;
    private final CategoryRepository categoryRepo;

    public ProductServiceImpl(ProductRepository productRepo, UserRepository userRepo, CategoryRepository categoryRepo) {
        this.productRepo = productRepo;
        this.userRepo = userRepo;
        this.categoryRepo = categoryRepo;
    }

    @Override
    public ProductResponseDto create(CreateProductDto dto) {
        UserEntity owner = userRepo.findById(dto.userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + dto.userId));

        Set<CategoryEntity> categories = new HashSet<>();
        if (dto.categoryIds != null) {
            for (Long catId : dto.categoryIds) {
                CategoryEntity cat = categoryRepo.findById(catId)
                        .orElseThrow(() -> new RuntimeException("Categoría no encontrada con ID: " + catId));
                categories.add(cat);
            }
        }

        ProductEntity entity = new ProductEntity();
        entity.setName(dto.name);
        entity.setPrice(dto.price);
        entity.setDescription(dto.description);
        entity.setOwner(owner);
        entity.setCategories(categories);

        ProductEntity saved = productRepo.save(entity);
        return ProductMapper.toResponse(saved);
    }

    @Override
    public List<ProductResponseDto> findAll() {
        return productRepo.findAll().stream()
                .map(ProductMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ProductResponseDto findOne(int id) {
        return productRepo.findById((long) id)
                .map(ProductMapper::toResponse)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + id));
    }

    @Override
    public ProductResponseDto update(int id, UpdateProductDto dto) {
        ProductEntity entity = productRepo.findById((long) id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + id));

        entity.setName(dto.name);
        entity.setPrice(dto.price);
        entity.setDescription(dto.description);


        if (dto.categoryIds != null) {
            Set<CategoryEntity> newCategories = new HashSet<>();
            for (Long catId : dto.categoryIds) {
                CategoryEntity cat = categoryRepo.findById(catId)
                        .orElseThrow(() -> new RuntimeException("Categoría no encontrada con ID: " + catId));
                newCategories.add(cat);
            }
            entity.setCategories(newCategories);
        }

        ProductEntity saved = productRepo.save(entity);
        return ProductMapper.toResponse(saved);
    }

    @Override
    public ProductResponseDto partialUpdate(int id, PartialUpdateProductDto dto) {
        ProductEntity entity = productRepo.findById((long) id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + id));

        if (dto.name != null) entity.setName(dto.name);
        if (dto.price != null) entity.setPrice(dto.price);
        if (dto.description != null) entity.setDescription(dto.description);

        ProductEntity saved = productRepo.save(entity);
        return ProductMapper.toResponse(saved);
    }

@Override
    public Object delete(int id) {
        if (!productRepo.existsById((long) id)) {
            throw new RuntimeException("No se puede eliminar. Producto no encontrado con ID: " + id);
        }
        
        productRepo.deleteById((long) id);
        return new Object() { 
            public String message = "Deleted successfully"; 
        };
    }

    @Override
    public boolean validateName(Integer id, String name) {

        return true; 
    }
}