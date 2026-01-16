package ec.edu.ups.icc.fundamentos01.products.mappers;

import ec.edu.ups.icc.fundamentos01.products.dtos.ProductResponseDto;
import ec.edu.ups.icc.fundamentos01.products.entities.ProductEntity;
import java.util.stream.Collectors;

public class ProductMapper {

    public static ProductResponseDto toResponse(ProductEntity entity) {
        if (entity == null) return null;

        ProductResponseDto dto = new ProductResponseDto();

        dto.id = entity.getId();
        dto.name = entity.getName();
        dto.description = entity.getDescription();
        dto.price = entity.getPrice();

        if (entity.getOwner() != null) {
            ProductResponseDto.UserSummaryDto userDto = new ProductResponseDto.UserSummaryDto();
            userDto.id = entity.getOwner().getId();
            userDto.name = entity.getOwner().getName();
            userDto.email = entity.getOwner().getEmail();
            dto.user = userDto;
        }

        if (entity.getCategories() != null) {
            dto.categories = entity.getCategories().stream().map(cat -> {
                ProductResponseDto.CategorySummaryDto catDto = new ProductResponseDto.CategorySummaryDto();
                catDto.id = cat.getId();
                catDto.name = cat.getName();
                return catDto;
            }).collect(Collectors.toList());
        }

        return dto;
    }
}