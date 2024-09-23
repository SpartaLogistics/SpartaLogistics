package com.sparta.logistics.client.hub.repository;

import com.sparta.logistics.client.hub.model.Product;

import java.util.List;
import java.util.UUID;

public interface ProductRepositoryCustom {
    List<Product> searchProducts(String name, UUID companyId);
}
