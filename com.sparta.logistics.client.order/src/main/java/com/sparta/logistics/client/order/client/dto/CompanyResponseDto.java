package com.sparta.logistics.client.order.client.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
public class CompanyResponseDto {

    private UUID companyId;           // 업체 ID
    private String name;              // 업체명
    private String address;           // 업체 주소
    private UUID managingHub;         // 관리 허브 ID
    private CompanyType companyType; // 업체



    public enum CompanyType {
        MANUFACTURER,  // 생산업체
        RECEIVER       // 수령업체
    }
}
