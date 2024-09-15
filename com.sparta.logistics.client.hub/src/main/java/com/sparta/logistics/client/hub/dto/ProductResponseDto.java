package com.sparta.logistics.client.hub.dto;

import com.sparta.logistics.client.hub.model.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponseDto {

    private String name; // 상품명

    private UUID companyId; //상품 제공 업체 ID

    private Integer quantity; // 수량

    private UUID managingHubId; // 관리 허브 ID

    private UUID productId; //상품 업체

    private Long price;

    // Product 엔티티를 ProductResponseDto로 변환하는 메서드
    public static ProductResponseDto of(Product product) {
        return ProductResponseDto.builder()
                .productId(product.getProductId())
                .companyId(product.getCompanyId())
                .name(product.getName())
                .price(product.getPrice())
                .quantity(product.getQuantity())
                .managingHubId(product.getManagingHubId())
                .build();
    }
}
