package ec.edu.ups.icc.fundamentos01.users.entities;

import ec.edu.ups.icc.fundamentos01.core.entities.BaseModel;
import ec.edu.ups.icc.fundamentos01.products.entities.ProductEntity;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "users")
public class UserEntity extends BaseModel {

    @Column(nullable = false, length = 150)
    private String name;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @Column(nullable = false)
    private String password;

    @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY)
    private List<ProductEntity> products;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public List<ProductEntity> getProducts() {
        return products;
    }

    public void setProducts(List<ProductEntity> products) {
        this.products = products;
    }
}