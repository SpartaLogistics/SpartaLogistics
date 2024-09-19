package com.sparta.logistics.client.hub.model;

import com.sparta.logistics.client.hub.dto.CompanyRequestDto;
import com.sparta.logistics.client.hub.enums.CompanyType;
import com.sparta.logistics.common.model.Timestamped;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "p_companies")
public class Company extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "company_id")
    private UUID companyId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "managing_hub_id", nullable = false)
    private Hub managingHub;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    private Boolean isDeleted = false;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CompanyType companyType;

    @Column(name = "company_manager_username", nullable = false)
    private String companyManagerUsername;  // 업체 담당자의 사용자 이름


    // 회사 생성용 빌더 클래스
    @Builder(builderClassName = "CreateCompanyInfoBuilder", builderMethodName = "createCompanyInfoBuilder")
    public Company(CompanyRequestDto companyRequestDto, Hub managingHubId, String companyManagerUsername) {
        this.managingHub = managingHubId;
        this.name = companyRequestDto.getName();
        this.companyType = companyRequestDto.getCompanyType();
        this.address = companyRequestDto.getAddress();
        this.companyManagerUsername = companyManagerUsername;
    }

    // 소프트 삭제 메서드
    public void softDelete(String deletedByUserId) {
        this.delete(deletedByUserId);
    }

    // 회사 수정용 메서드
    public void update(CompanyRequestDto companyRequestDto, Hub managingHubId) {
        if (managingHubId != null) {
            this.managingHub = managingHubId;
        }
        if (companyRequestDto.getName() != null) {
            this.name = companyRequestDto.getName();
        }
        if (companyRequestDto.getCompanyType() != null) {
            this.companyType = companyRequestDto.getCompanyType();
        }
        if (companyRequestDto.getAddress() != null) {
            this.address = companyRequestDto.getAddress();
        }
    }

    // 업체 담당자 변경 메서드
    public void changeManager(String newManagerUsername) {
        this.companyManagerUsername = newManagerUsername;
    }

    // Hub 엔티티의 ID를 반환하는 메서드
    public UUID getManagingHubId() {
        return managingHub != null ? managingHub.getHubId() : null;
    }


}
