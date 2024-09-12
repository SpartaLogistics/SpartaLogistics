package com.sparta.logistics.client.hub.dto;

import com.sparta.logistics.client.hub.model.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDetailResponseDto {
    private UUID productId;
    private String name;
    private UUID companyId;
    private Integer quantity;
    private UUID managingHubId;
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime deletedAt;
    private String deletedBy;
    private LocalDateTime updatedAt;
    private String updatedBy;

    public static ProductDetailResponseDto of(Product product) {
        return ProductDetailResponseDto.builder()
                .productId(product.getProductId())
                .name(product.getName())
                .companyId(product.getCompanyId())
                .quantity(product.getQuantity())
                .managingHubId(product.getManagingHubId())
                .createdAt(product.getCreatedAt())
                .createdBy(String.valueOf(product.getCreatedBy()))
                .deletedAt(product.getDeletedAt())
                .deletedBy(String.valueOf(product.getDeletedBy()))
                .updatedAt(product.getUpdatedAt())
                .updatedBy(String.valueOf(product.getUpdatedBy()))
                .build();
    }
}