package com.sparta.logistics.common.kafka;

import lombok.*;

import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProductDeleted {
    private String name; // 상품명
    private UUID companyId; // 상품 제공 업체 ID
    private Integer quantity; // 수량
    private UUID managingHubId; // 관리 허브 ID
    private UUID productId; // 상품 ID
    private Long price;
    private String userId;

    // ProductDeleted DTO를 생성하는 메서드
    public static ProductDeleted of(UUID productId, String name, UUID companyId, Integer quantity, UUID managingHubId, Long price, String userId) {
        return ProductDeleted.builder()
                .productId(productId)
                .name(name)
                .companyId(companyId)
                .quantity(quantity)
                .managingHubId(managingHubId)
                .price(price)
                .userId(userId)
                .build();
    }
}
