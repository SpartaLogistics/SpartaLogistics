package com.sparta.logistics.client.hub.dto;

import com.sparta.logistics.client.hub.enums.CompanyType;
import com.sparta.logistics.client.hub.model.validation.CompanyVaild0001;
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
public class CompanyRequestDto {

    @NotNull(groups = {CompanyVaild0001.class},
            message = "업체명 정보가 누락되었습니다."
    )
    private String name;
    //TODO : userID

    @NotNull(groups = {CompanyVaild0001.class},
            message = "업체타입 정보가 누락되었습니다."
    )
    private CompanyType companyType;

    @NotNull(groups = {CompanyVaild0001.class},
            message = "업체 주소 정보가 누락되었습니다."
    )
    private String address;

    @NotNull(groups = {CompanyVaild0001.class},
            message = "업체 관리 허브 ID 정보가 누락되었습니다."
    )
    private UUID managingHubId;

    @NotNull(groups = {CompanyVaild0001.class},
            message = "업체 담당자 정보가 누락되었습니다."
    )
    private String companyManagerUsername;
}
