package ec.edu.ups.icc.fundamentos01.products.dtos;

import java.util.List;

public class ProductResponseDto {
    public Long id;
    public String name;
    public Double price;
    public String description;

    public UserSummaryDto user;
    public List<CategorySummaryDto> categories;

    public static class UserSummaryDto {
        public Long id;
        public String name;
        public String email;
    }

    public static class CategorySummaryDto {
        public Long id;
        public String name;
    }
}