package ec.edu.ups.icc.fundamentos01.products.repositories;

import ec.edu.ups.icc.fundamentos01.products.entities.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {


    List<ProductEntity> findByCategoriesId(Long categoryId);

    List<ProductEntity> findByOwnerName(String name);

    List<ProductEntity> findByCategoriesName(String name);


    List<ProductEntity> findByCategoriesIdAndPriceGreaterThan(Long categoryId, Double price);

    long countByCategories_Id(Long categoryId);
}