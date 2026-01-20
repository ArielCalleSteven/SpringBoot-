package ec.edu.ups.icc.fundamentos01.bootstrap;

import ec.edu.ups.icc.fundamentos01.categories.entities.CategoryEntity;
import ec.edu.ups.icc.fundamentos01.categories.repositories.CategoryRepository;
import ec.edu.ups.icc.fundamentos01.products.entities.ProductEntity;
import ec.edu.ups.icc.fundamentos01.products.repositories.ProductRepository;
import ec.edu.ups.icc.fundamentos01.users.entities.UserEntity;
import ec.edu.ups.icc.fundamentos01.users.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;

@Component
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepo;
    private final CategoryRepository categoryRepo;
    private final ProductRepository productRepo;

    public DataSeeder(UserRepository userRepo, CategoryRepository categoryRepo, ProductRepository productRepo) {
        this.userRepo = userRepo;
        this.categoryRepo = categoryRepo;
        this.productRepo = productRepo;
    }

    @Override
    public void run(String... args) throws Exception {
        // Solo corre si no hay productos (para no duplicar si reinicias)
        if (productRepo.count() < 1000) {
            System.out.println("⚡ INICIANDO CARGA MASIVA DE 1000 PRODUCTOS...");

            // 1. Crear 5 Usuarios (Requisito 9.1)
            List<UserEntity> users = new ArrayList<>();
            for (int i = 1; i <= 5; i++) {
                UserEntity u = new UserEntity();
                u.setName("Usuario " + i);
                u.setEmail("user" + i + "@test.com");
                u.setPassword("12345"); // En un caso real, encriptar
                users.add(userRepo.save(u));
            }

            // 2. Crear 5 Categorías
            List<CategoryEntity> categories = new ArrayList<>();
            String[] catNames = {"Tecnología", "Hogar", "Deportes", "Libros", "Ropa"};
            for (String name : catNames) {
                CategoryEntity c = new CategoryEntity();
                c.setName(name);
                c.setDescription("Descripcion de " + name);
                categories.add(categoryRepo.save(c));
            }

            // 3. Generar 1000 Productos (Requisito 9.1)
            List<ProductEntity> products = new ArrayList<>();
            Random random = new Random();

            for (int i = 1; i <= 1000; i++) {
                ProductEntity p = new ProductEntity();
                // Nombres buscables variados
                String tipo = List.of("Laptop", "Mouse", "Teclado", "Monitor", "Celular").get(random.nextInt(5));
                p.setName(tipo + " Modelo " + i); 
                p.setDescription("Descripción generada para el producto " + i);
                
                // Precio entre $10 y $5000
                double price = 10 + (5000 - 10) * random.nextDouble();
                p.setPrice(Math.round(price * 100.0) / 100.0); // Redondear 2 decimales
                
                // Fechas aleatorias (para probar ordenamiento)
                p.setCreatedAt(LocalDateTime.now().minusDays(random.nextInt(365))); 
                p.setUpdatedAt(LocalDateTime.now());

                // Asignar usuario aleatorio
                p.setOwner(users.get(random.nextInt(users.size())));

                // Asignar al menos 2 categorías (Requisito 9.1)
                Set<CategoryEntity> prodCats = new HashSet<>();
                prodCats.add(categories.get(random.nextInt(categories.size()))); // Cat 1
                prodCats.add(categories.get(random.nextInt(categories.size()))); // Cat 2
                p.setCategories(prodCats);

                products.add(p);
                
                // Guardar en lotes de 100 para no saturar memoria
                if (i % 100 == 0) {
                    productRepo.saveAll(products);
                    products.clear();
                    System.out.println("   -> Cargados " + i + " productos...");
                }
            }
            // Guardar los restantes
            if (!products.isEmpty()) {
                productRepo.saveAll(products);
            }

            System.out.println("✅ ¡CARGA MASIVA COMPLETA! 1000 Productos listos.");
        }
    }
}