package com.sparta.logistics.client.hub.service;

import com.sparta.logistics.client.hub.common.exception.HubException;
import com.sparta.logistics.client.hub.dto.CompanyRequestDto;
import com.sparta.logistics.client.hub.dto.CompanyResponseDto;
import com.sparta.logistics.client.hub.enums.CompanyType;
import com.sparta.logistics.client.hub.model.Company;
import com.sparta.logistics.client.hub.model.Hub;
import com.sparta.logistics.client.hub.repository.CompanyRepository;
import com.sparta.logistics.common.client.UserClient;
import com.sparta.logistics.common.model.RoleType;
import com.sparta.logistics.common.model.UserVO;
import com.sparta.logistics.common.type.ApiResultError;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompanyService {
    private final CompanyRepository companyRepository;
    private final HubService hubService;
    private final UserClient userClient;

    @Transactional
    public CompanyResponseDto createCompany(CompanyRequestDto requestDto, String username) throws HubException {
        UserVO currentUser = userClient.findByUsername(username);
        Hub managingHubId = validateManagingHubId(requestDto.getManagingHubId());

        // 허브 관리자: 자신의 허브에 소속된 업체만 관리 가능
        if (currentUser.getRole().equals(RoleType.OWNER)) {
            if (!currentUser.getUsername().equals(managingHubId.getManagerUsername())) {
                throw new HubException(ApiResultError.NO_AUTH_PERMISSION_DENIED);
            }

        }

        Company company = Company.createCompanyInfoBuilder()
                .companyRequestDto(requestDto)
                .managingHubId(managingHubId)
                .companyManagerUsername(requestDto.getCompanyManagerUsername())
                .build();

        companyRepository.save(company);
        return CompanyResponseDto.of(company);
    }

    @Transactional(readOnly = true)
    public CompanyResponseDto getCompany(UUID companyId) throws HubException {
        Company company = companyRepository.findByCompanyId(companyId)
                .orElseThrow(() -> new HubException(ApiResultError.COMPANY_NO_EXIST));
        return CompanyResponseDto.of(company);
    }

    @Transactional(readOnly = true)
    public List<CompanyResponseDto> getAllCompanies() throws HubException {
        return companyRepository.findAllByIsDeletedFalse().stream()
                .map(CompanyResponseDto::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public CompanyResponseDto updateCompany(UUID companyId, CompanyRequestDto requestDto, String username) throws HubException {
        UserVO currentUser = userClient.findByUsername(username);

        Company company = companyRepository.findByCompanyId(companyId)
                .orElseThrow(() -> new HubException(ApiResultError.COMPANY_NO_EXIST));

        if (currentUser.getRole() == RoleType.OWNER) {
            // 허브 관리자: 자신의 허브에 소속된 업체만 수정 가능
            if (!currentUser.getUsername().equals(company.getManagingHub().getManagerUsername())) {
                throw new HubException(ApiResultError.NOT_YOUR_HUB_COMPANY);
            }
        } else if (currentUser.getRole() == RoleType.CUSTOMER) {
            // 허브 업체: 자신의 업체만 수정 가능
            if (!currentUser.getUsername().equals(company.getCompanyManagerUsername())) {
                throw new HubException(ApiResultError.NOT_YOUR_COMPANY);
            }
        }


        Hub managingHubId = validateManagingHubId(requestDto.getManagingHubId());

        company.update(requestDto, managingHubId);

        // 업체 담당자 변경 처리
        if (!company.getCompanyManagerUsername().equals(requestDto.getCompanyManagerUsername())) {
            UserVO newManager = userClient.findByUsername(requestDto.getCompanyManagerUsername());
            if (newManager == null) {
                throw new HubException(ApiResultError.USER_NO_EXIST);
            }
            company.changeManager(requestDto.getCompanyManagerUsername());
        }

        return CompanyResponseDto.of(company);
    }

    @Transactional
    public void deleteCompany(UUID companyId, String username, String userId) throws HubException {
        Company company = companyRepository.findByCompanyId(companyId)
                .orElseThrow(() -> new HubException(ApiResultError.COMPANY_NO_EXIST));

        UserVO currentUser = userClient.findByUsername(username);

        // MASTER : 모든 권한 , OWNER : 자신의 허브에 소속된 업체만 삭제 가능
        if (currentUser.getRole() == RoleType.OWNER && !currentUser.getUsername().equals(company.getManagingHub().getManagerUsername())) {
            throw new HubException(ApiResultError.NO_AUTH_PERMISSION_DENIED);
        }

        // 논리적 삭제(soft Delete)
        company.softDelete(userId);
        companyRepository.save(company);
    }

    public List<CompanyResponseDto> searchCompanies(String name, CompanyType companyType, String address, UUID managingHubId) throws HubException {
        // 관리 허브 ID 유효성 검사
        validateManagingHubId(managingHubId);

        List<Company> companies = companyRepository.searchCompanies(name, companyType, address, managingHubId);

        // 검색 결과가 없을 경우
        if (companies.isEmpty()) {
            throw new HubException(ApiResultError.SEARCH_NO_RESULT);
        }

        return companies.stream().map(CompanyResponseDto::of).collect(Collectors.toList());
    }

    private Hub validateManagingHubId(UUID managingHubId) throws HubException {
        if (managingHubId != null) {
            if (hubService.existsHubById(managingHubId)) {
                log.info("Hub with id {} already exists", managingHubId);
                return hubService.findHubById(managingHubId);
            }
            throw new HubException(ApiResultError.MANAGING_HUB_NO_EXIST);
        }
        return null;
    }

    // Company 존재 확인
    public Company findCompanyById(UUID companyId) throws HubException {
        return companyRepository.findByCompanyId(companyId)
                .orElseThrow(() -> new HubException(ApiResultError.COMPANY_NO_EXIST));
    }


}
