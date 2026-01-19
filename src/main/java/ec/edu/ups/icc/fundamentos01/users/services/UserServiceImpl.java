package ec.edu.ups.icc.fundamentos01.users.services;

import ec.edu.ups.icc.fundamentos01.users.repositories.UserRepository;
import ec.edu.ups.icc.fundamentos01.products.repositories.ProductRepository;
import ec.edu.ups.icc.fundamentos01.products.dtos.ProductResponseDto;
import ec.edu.ups.icc.fundamentos01.products.mappers.ProductMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepo;
    private final ProductRepository productRepo;

    public UserServiceImpl(UserRepository userRepo, ProductRepository productRepo) {
        this.userRepo = userRepo;
        this.productRepo = productRepo;
    }

    @Override
    public List<ProductResponseDto> getProductsByUserId(Long userId) {
        if (!userRepo.existsById(userId)) {
            throw new RuntimeException("Usuario no encontrado con ID: " + userId);
        }

        return productRepo.findByOwnerId(userId).stream()
                .map(ProductMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductResponseDto> getProductsByUserIdWithFilters(
            Long userId, String name, Double minPrice, Double maxPrice, Long categoryId) {
        
        if (!userRepo.existsById(userId)) {
            throw new RuntimeException("Usuario no encontrado con ID: " + userId);
        }

        return productRepo.findByUserWithFilters(userId, name, minPrice, maxPrice, categoryId)
                .stream()
                .map(ProductMapper::toResponse)
                .collect(Collectors.toList());
    }
}