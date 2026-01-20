package ec.edu.ups.icc.fundamentos01.products.repositories;

import ec.edu.ups.icc.fundamentos01.products.entities.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    List<ProductEntity> findByCategoriesId(Long categoryId);
    List<ProductEntity> findByOwnerName(String name);
    List<ProductEntity> findByCategoriesName(String name);
    List<ProductEntity> findByCategoriesIdAndPriceGreaterThan(Long categoryId, Double price);
    long countByCategories_Id(Long categoryId);
    List<ProductEntity> findByOwnerId(Long userId);

    @Query("SELECT p FROM ProductEntity p " +
           "LEFT JOIN p.categories c " +
           "WHERE p.owner.id = :userId " +
           "AND (:name IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))) " +
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


    @Query("SELECT p FROM ProductEntity p " +
           "JOIN p.owner o " +
           "JOIN p.category c " +
           "WHERE (:name IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))) " +
           "AND (:minPrice IS NULL OR p.price >= :minPrice) " +
           "AND (:maxPrice IS NULL OR p.price <= :maxPrice) " +
           "AND (:categoryId IS NULL OR c.id = :categoryId)")
    Page<ProductEntity> findWithFilters(
        @Param("name") String name,
        @Param("minPrice") Double minPrice,
        @Param("maxPrice") Double maxPrice,
        @Param("categoryId") Long categoryId,
        Pageable pageable
    );

    @Query("SELECT p FROM ProductEntity p " +
           "JOIN p.owner o " +
           "JOIN p.category c " +
           "WHERE o.id = :userId " +
           "AND (:name IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))) " +
           "AND (:minPrice IS NULL OR p.price >= :minPrice) " +
           "AND (:maxPrice IS NULL OR p.price <= :maxPrice) " +
           "AND (:categoryId IS NULL OR c.id = :categoryId)")
    Page<ProductEntity> findByUserIdWithFilters(
        @Param("userId") Long userId,
        @Param("name") String name,
        @Param("minPrice") Double minPrice,
        @Param("maxPrice") Double maxPrice,
        @Param("categoryId") Long categoryId,
        Pageable pageable
    );

    Page<ProductEntity> findByOwnerNameContaining(@Param("ownerName") String ownerName, Pageable pageable);
    Page<ProductEntity> findByCategoryId(Long categoryId, Pageable pageable);
    Page<ProductEntity> findByPriceBetween(Double minPrice, Double maxPrice, Pageable pageable);

    Slice<ProductEntity> findByCategoryIdOrderByCreatedAtDesc(Long categoryId, Pageable pageable);

    @Query("SELECT p FROM ProductEntity p WHERE p.createdAt > :date ORDER BY p.createdAt DESC")
    Slice<ProductEntity> findCreatedAfter(@Param("date") LocalDateTime date, Pageable pageable);

    @Query("SELECT COUNT(p) FROM ProductEntity p " +
           "JOIN p.owner o " +
           "JOIN p.category c " +
           "WHERE (:name IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))) " +
           "AND (:minPrice IS NULL OR p.price >= :minPrice) " +
           "AND (:maxPrice IS NULL OR p.price <= :maxPrice) " +
           "AND (:categoryId IS NULL OR c.id = :categoryId)")
    long countWithFilters(
        @Param("name") String name,
        @Param("minPrice") Double minPrice,
        @Param("maxPrice") Double maxPrice,
        @Param("categoryId") Long categoryId
    );
}