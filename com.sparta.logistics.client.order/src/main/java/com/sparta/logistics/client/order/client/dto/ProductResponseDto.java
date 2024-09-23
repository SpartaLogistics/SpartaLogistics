package com.sparta.logistics.client.order.client.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.UUID;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductResponseDto {

    private String name;        // 상품명
    private UUID companyId;     // 상품 제공 업체 ID
    private Integer quantity;   // 수량
    private UUID managingHubId; // 관리 허브 ID
    private UUID productId;     //상품 업체
    private Long price;
    private boolean isDeleted;
}
