package com.sparta.logistics.client.order.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.logistics.client.order.model.QOrderProduct;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class OrderProductRepositoryCustomImpl implements OrderProductRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    public OrderProductRepositoryCustomImpl(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public List<UUID> findDistinctOrderIdsByProductId(UUID productId) {
        QOrderProduct qOrderProduct = QOrderProduct.orderProduct;

        return jpaQueryFactory.select(qOrderProduct.orderId)
                .distinct()
                .from(qOrderProduct)
                .where(qOrderProduct.productId.eq(productId)
                        .and(qOrderProduct.isDeleted.isFalse()))
                .fetch();
    }
}
