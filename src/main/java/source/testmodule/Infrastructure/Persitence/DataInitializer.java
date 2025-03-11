package source.testmodule.Infrastructure.Persitence;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import source.testmodule.Infrastructure.Persitence.Entity.ProductJpaEntity;
import source.testmodule.Infrastructure.Persitence.RepositoryAdapters.ProductRepositoryAdapter;

import java.math.BigDecimal;
import java.util.List;

@Component
public class DataInitializer {

    private final ProductRepositoryAdapter productRepo;

    public DataInitializer(ProductRepositoryAdapter productRepo) {
        this.productRepo = productRepo;
    }

    @PostConstruct
    public void init() {
        List<ProductJpaEntity> products = List.of(
                new ProductJpaEntity("Apple iPhone 13", new BigDecimal("799.99"), 2),
                new ProductJpaEntity("Samsung Galaxy S21", new BigDecimal("699.99"), 3),
                new ProductJpaEntity("Google Pixel 6", new BigDecimal("599.99"), 20),
                new ProductJpaEntity("OnePlus 9 Pro", new BigDecimal("969.99"), 15),
                new ProductJpaEntity("Sony WH-1000XM4 Headphones", new BigDecimal("349.99"), 40),
                new ProductJpaEntity("Dell XPS 13 Laptop", new BigDecimal("999.99"), 10),
                new ProductJpaEntity("HP Envy 15 Laptop", new BigDecimal("1199.99"), 8),
                new ProductJpaEntity("Apple MacBook Pro 14\"", new BigDecimal("1999.99"), 5),
                new ProductJpaEntity("Microsoft Surface Pro 7", new BigDecimal("749.99"), 12),
                new ProductJpaEntity("Logitech MX Master 3 Mouse", new BigDecimal("99.99"), 50)
        );

        productRepo.saveAll(products);
    }
}