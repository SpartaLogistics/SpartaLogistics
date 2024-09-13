package com.sparta.logistics.client.hub.repository;

import com.querydsl.core.BooleanBuilder;
import com.sparta.logistics.client.hub.model.Product;
import com.sparta.logistics.client.hub.model.QProduct;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.UUID;

public class ProductRepositoryImpl extends QuerydslRepositorySupport implements ProductRepositoryCustom {

    public ProductRepositoryImpl() {
        super(Product.class);
    }

    @Override
    public List<Product> searchProducts(String name, UUID companyId) {
        QProduct product = QProduct.product;

        BooleanBuilder builder = new BooleanBuilder();

        if (StringUtils.hasText(name)) {
            builder.and(product.name.containsIgnoreCase(name));
        }

        if (companyId != null) {
            builder.and(product.company.companyId.eq(companyId));
        }

        return from(product)
                .where(builder)
                .fetch();
    }
}
