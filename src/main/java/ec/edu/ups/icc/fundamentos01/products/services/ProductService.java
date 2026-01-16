package ec.edu.ups.icc.fundamentos01.products.services;

import ec.edu.ups.icc.fundamentos01.products.dtos.*;
import java.util.List;

public interface ProductService {
    List<ProductResponseDto> findAll();
    Object findOne(int id);
    ProductResponseDto create(CreateProductDto dto);
    Object update(int id, UpdateProductDto dto);
    Object partialUpdate(int id, PartialUpdateProductDto dto);
    Object delete(int id);
    boolean validateName(Integer id, String name);
}