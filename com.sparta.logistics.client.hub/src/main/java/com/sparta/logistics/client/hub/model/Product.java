package com.sparta.logistics.client.hub.model;

import com.sparta.logistics.client.hub.common.Timestamped;
import com.sparta.logistics.client.hub.dto.ProductRequestDto;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "p_products")
public class Product extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID productId;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @Column
    @Setter
    private Integer quantity;

    @Column
    private Long price;


    @Column(nullable = false)
    private Boolean isDeleted = false;

    // 상품 생성용 빌더 클래스
    @Builder(builderClassName = "CreateProductInfoBuilder", builderMethodName = "CreateProductInfoBuilder")
    public Product(ProductRequestDto productRequestDto, Company company) {
        this.name = productRequestDto.getName();
        this.company = company;
        this.quantity = productRequestDto.getQuantity();
        this.price = productRequestDto.getPrice();
    }

    // 상품 수정용 메서드
    public void updateProduct(ProductRequestDto productRequestDto, Company company) {
        if (productRequestDto.getName() != null) {
            this.name = productRequestDto.getName();
        }
        if (company != null) {
            this.company = company;
        }
        this.quantity = productRequestDto.getQuantity();
        this.price = productRequestDto.getPrice();
    }

    // 소프트 삭제 메서드
    public void softDelete() {
        this.isDeleted = true;
    }

    // Company 엔티티의 ID를 반환하는 메서드
    public UUID getCompanyId() {
        return company != null ? company.getCompanyId() : null;
    }

    // Hub 엔티티의 ID를 반환하는 메서드 (Company를 통해 간접적으로 접근)
    public UUID getManagingHubId() {
        return company != null ? company.getManagingHubId() : null;
    }

}
