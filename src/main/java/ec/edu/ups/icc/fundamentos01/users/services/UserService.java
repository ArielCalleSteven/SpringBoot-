package ec.edu.ups.icc.fundamentos01.users.services;

import ec.edu.ups.icc.fundamentos01.products.dtos.ProductResponseDto;
import java.util.List;

public interface UserService {
    List<ProductResponseDto> getProductsByUserId(Long userId);
    List<ProductResponseDto> getProductsByUserIdWithFilters(
            Long userId, 
            String name, 
            Double minPrice, 
            Double maxPrice, 
            Long categoryId
    );
}