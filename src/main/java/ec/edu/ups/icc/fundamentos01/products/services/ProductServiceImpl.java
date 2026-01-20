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
import org.springframework.data.domain.*; 
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
        return new Object() { public String message = "Deleted successfully"; };
    }

    @Override
    public boolean validateName(Integer id, String name) {
        return true; 
    }


    @Override
    public Page<ProductResponseDto> findAll(int page, int size, String[] sort) {
        Pageable pageable = createPageable(page, size, sort);
        Page<ProductEntity> productPage = productRepo.findAll(pageable);
        
        return productPage.map(this::toResponseDto);
    }

    @Override
    public Slice<ProductResponseDto> findAllSlice(int page, int size, String[] sort) {
        Pageable pageable = createPageable(page, size, sort);
        Slice<ProductEntity> productSlice = productRepo.findAll(pageable);
        
        return productSlice.map(this::toResponseDto);
    }

    @Override
    public Page<ProductResponseDto> findWithFilters(
            String name, Double minPrice, Double maxPrice, Long categoryId,
            int page, int size, String[] sort) {
        
        validateFilterParameters(minPrice, maxPrice);
        
        Pageable pageable = createPageable(page, size, sort);
        
        Page<ProductEntity> productPage = productRepo.findWithFilters(
            name, minPrice, maxPrice, categoryId, pageable);
        
        return productPage.map(this::toResponseDto);
    }

    @Override
    public Page<ProductResponseDto> findByUserIdWithFilters(
            Long userId, String name, Double minPrice, Double maxPrice, Long categoryId,
            int page, int size, String[] sort) {
        
        if (!userRepo.existsById(userId)) {
            throw new RuntimeException("Usuario no encontrado con ID: " + userId);
        }
        
        validateFilterParameters(minPrice, maxPrice);
        
        Pageable pageable = createPageable(page, size, sort);
        
        Page<ProductEntity> productPage = productRepo.findByUserIdWithFilters(
            userId, name, minPrice, maxPrice, categoryId, pageable);
        
        return productPage.map(this::toResponseDto);
    }



    private Pageable createPageable(int page, int size, String[] sort) {
        // Validar parámetros
        if (page < 0) {
            throw new RuntimeException("La página debe ser mayor o igual a 0");
        }
        if (size < 1 || size > 100) {
            throw new RuntimeException("El tamaño debe estar entre 1 y 100");
        }
        
        Sort sortObj = createSort(sort);
        
        return PageRequest.of(page, size, sortObj);
    }

    private Sort createSort(String[] sort) {
        if (sort == null || sort.length == 0) {
            return Sort.by("id");
        }

        List<Sort.Order> orders = new ArrayList<>();
        for (String sortParam : sort) {
            String[] parts = sortParam.split(",");
            String property = parts[0];
            String direction = parts.length > 1 ? parts[1] : "asc";
            
            if (!isValidSortProperty(property)) {
                throw new RuntimeException("Propiedad de ordenamiento no válida: " + property);
            }
            
            Sort.Order order = "desc".equalsIgnoreCase(direction) 
                ? Sort.Order.desc(property)
                : Sort.Order.asc(property);
            
            orders.add(order);
        }
        
        return Sort.by(orders);
    }

    private boolean isValidSortProperty(String property) {
        Set<String> allowedProperties = Set.of(
            "id", "name", "price", "createdAt", "updatedAt",
            "owner.name", "owner.email", "category.name"
        );
        return allowedProperties.contains(property);
    }

    private void validateFilterParameters(Double minPrice, Double maxPrice) {
        if (minPrice != null && minPrice < 0) {
            throw new RuntimeException("El precio mínimo no puede ser negativo");
        }
        if (maxPrice != null && maxPrice < 0) {
            throw new RuntimeException("El precio máximo no puede ser negativo");
        }
        if (minPrice != null && maxPrice != null && maxPrice < minPrice) {
            throw new RuntimeException("El precio máximo debe ser mayor o igual al precio mínimo");
        }
    }

    private ProductResponseDto toResponseDto(ProductEntity product) {

        ProductResponseDto dto = new ProductResponseDto();
        
        dto.id = product.getId();
        dto.name = product.getName();
        dto.price = product.getPrice();
        dto.description = product.getDescription();
        dto.createdAt = product.getCreatedAt();
        dto.updatedAt = product.getUpdatedAt();
        
        if (product.getOwner() != null) {
            dto.user = new ProductResponseDto.UserSummaryDto();
            dto.user.id = product.getOwner().getId();
            dto.user.name = product.getOwner().getName();
            dto.user.email = product.getOwner().getEmail();
        }
        return ProductMapper.toResponse(product); 
    }
}