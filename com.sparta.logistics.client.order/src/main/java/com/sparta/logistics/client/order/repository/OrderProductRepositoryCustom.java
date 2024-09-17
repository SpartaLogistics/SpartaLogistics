package com.sparta.logistics.client.order.repository;

import java.util.List;
import java.util.UUID;

public interface OrderProductRepositoryCustom {

    List<UUID> findDistinctOrderIdsByProductId(UUID productId);
}
