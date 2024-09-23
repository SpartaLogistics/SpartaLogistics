package com.sparta.logistics.client.hub.repository;

import com.sparta.logistics.client.hub.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID>, ProductRepositoryCustom {
    Optional<Product> findByProductIdAndIsDeletedFalse(UUID productId);

    default Optional<Product> findByProductId(UUID productId) {
        return findByProductIdAndIsDeletedFalse(productId);
    }

    Page<Product> findAllByIsDeletedFalse(Pageable pageable);


}
