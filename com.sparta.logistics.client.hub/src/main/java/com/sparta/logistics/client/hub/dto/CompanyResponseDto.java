package com.sparta.logistics.client.hub.dto;

import com.sparta.logistics.client.hub.enums.CompanyType;
import com.sparta.logistics.client.hub.model.Company;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class CompanyResponseDto {
    private final UUID companyId; // 업체 ID
    private final String name; // 업체명
    private final CompanyType companyType; // 업체 타입
    private final String address; // 업체 주소
    private final UUID managingHubId; // 관리 허브 ID

    // Entity를 ResponseDto로 변환하는 static method
    public static CompanyResponseDto of(Company company) {
        return CompanyResponseDto.builder()
                .companyId(company.getCompanyId())
                .name(company.getName())
                .companyType(company.getCompanyType())
                .address(company.getAddress())
                .managingHubId(company.getManagingHubId() != null ? company.getManagingHubId().getHubId() : null)
                .build();
    }
}
