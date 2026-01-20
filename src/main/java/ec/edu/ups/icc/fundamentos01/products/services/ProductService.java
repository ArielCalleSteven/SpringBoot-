package ec.edu.ups.icc.fundamentos01.products.services;

import ec.edu.ups.icc.fundamentos01.products.dtos.*;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice; 

public interface ProductService {
    
    List<ProductResponseDto> findAll();
    Object findOne(int id);
    ProductResponseDto create(CreateProductDto dto);
    Object update(int id, UpdateProductDto dto);
    Object partialUpdate(int id, PartialUpdateProductDto dto);
    Object delete(int id);
    boolean validateName(Integer id, String name);


    Page<ProductResponseDto> findAll(int page, int size, String[] sort);

    Slice<ProductResponseDto> findAllSlice(int page, int size, String[] sort);

    Page<ProductResponseDto> findWithFilters(
        String name, 
        Double minPrice, 
        Double maxPrice, 
        Long categoryId,
        int page, 
        int size, 
        String[] sort
    );

    Page<ProductResponseDto> findByUserIdWithFilters(
        Long userId,
        String name,
        Double minPrice,
        Double maxPrice,
        Long categoryId,
        int page,
        int size,
        String[] sort
    );
}