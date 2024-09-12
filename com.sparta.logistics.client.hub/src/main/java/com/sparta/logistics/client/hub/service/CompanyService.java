package com.sparta.logistics.client.hub.service;

import com.sparta.logistics.client.hub.common.exception.HubException;
import com.sparta.logistics.client.hub.dto.CompanyRequestDto;
import com.sparta.logistics.client.hub.dto.CompanyResponseDto;
import com.sparta.logistics.client.hub.enums.CompanyType;
import com.sparta.logistics.client.hub.model.Company;
import com.sparta.logistics.client.hub.model.Hub;
import com.sparta.logistics.client.hub.repository.CompanyRepository;
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

    @Transactional
    public CompanyResponseDto createCompany(CompanyRequestDto requestDto) throws HubException {
        Hub managingHubId = validateManagingHubId(requestDto.getManagingHubId());

        Company company = Company.createCompanyInfoBuilder()
                .companyRequestDto(requestDto)
                .managingHubId(managingHubId)
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
    public List<CompanyResponseDto> getAllCompanies() {
        return companyRepository.findAllByIsDeletedFalse().stream()
                .map(CompanyResponseDto::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public CompanyResponseDto updateCompany(UUID companyId, CompanyRequestDto requestDto) throws HubException {
        // TODO : 자신의 업체만 수정 가능
        Company company = companyRepository.findByCompanyId(companyId)
                .orElseThrow(() -> new HubException(ApiResultError.COMPANY_NO_EXIST));

        Hub managingHubId = validateManagingHubId(requestDto.getManagingHubId());

        company.update(requestDto, managingHubId);
        return CompanyResponseDto.of(company);
    }

    @Transactional
    public void deleteCompany(UUID companyId) throws HubException {
        Company company = companyRepository.findByCompanyId(companyId)
                .orElseThrow(() -> new HubException(ApiResultError.COMPANY_NO_EXIST));

        // 논리적 삭제(soft Delete)
        company.softDelete();
        companyRepository.save(company);
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

    public List<CompanyResponseDto> searchCompanies(String name, CompanyType companyType, String address, UUID managingHubId) throws HubException {
        // 관리 허브 ID 유효성 검사
        validateManagingHubId(managingHubId);

        List<Company> companies = companyRepository.searchCompanies(name, companyType, address, managingHubId);

        // 검색 결과가 없을 경우
        if (companies.isEmpty()) {
            throw new HubException(ApiResultError.COMPANY_SEARCH_NO_RESULT);
        }

        return companies.stream().map(CompanyResponseDto::of).collect(Collectors.toList());
    }
}
