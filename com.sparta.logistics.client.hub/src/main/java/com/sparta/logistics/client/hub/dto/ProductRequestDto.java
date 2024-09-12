package com.sparta.logistics.client.hub.dto;


import com.sparta.logistics.client.hub.model.validation.ProductValid0001;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequestDto {

    @NotNull(groups = {ProductValid0001.class},
            message = "상품명 정보가 누락되었습니다."
    )
    private String name;

    @NotNull(groups = {ProductValid0001.class},
            message = "업체ID 정보가 누락되었습니다."
    )
    private UUID companyId;

    @NotNull(groups = {ProductValid0001.class},
            message = "상품 수량 정보가 누락되었습니다."
    )
    private int quantity;
}
