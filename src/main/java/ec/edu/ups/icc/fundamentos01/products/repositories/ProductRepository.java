package ec.edu.ups.icc.fundamentos01.products.repositories;

import ec.edu.ups.icc.fundamentos01.products.entities.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
    List<ProductEntity> findByCategoriesId(Long categoryId);
    List<ProductEntity> findByOwnerName(String name);
    List<ProductEntity> findByCategoriesName(String name);
    List<ProductEntity> findByCategoriesIdAndPriceGreaterThan(Long categoryId, Double price);
    long countByCategories_Id(Long categoryId);
    List<ProductEntity> findByOwnerId(Long userId);


@Query("SELECT DISTINCT p FROM ProductEntity p " +
       "LEFT JOIN p.categories c " +
       "WHERE p.owner.id = :userId " +
       "AND (:name IS NULL OR p.name ILIKE %:name%) " + 
       "AND (:minPrice IS NULL OR p.price >= :minPrice) " +
       "AND (:maxPrice IS NULL OR p.price <= :maxPrice) " +
       "AND (:categoryId IS NULL OR c.id = :categoryId)")
List<ProductEntity> findByUserWithFilters(
        @Param("userId") Long userId,
        @Param("name") String name,
        @Param("minPrice") Double minPrice,
        @Param("maxPrice") Double maxPrice,
        @Param("categoryId") Long categoryId
);

}