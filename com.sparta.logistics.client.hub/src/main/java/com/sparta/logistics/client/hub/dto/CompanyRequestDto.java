package com.sparta.logistics.client.hub.dto;

import com.sparta.logistics.client.hub.enums.CompanyType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanyRequestDto {
    private String name;
    //TODO : userID
    private CompanyType companyType;
    private String address;
    private UUID managingHubId;
}
