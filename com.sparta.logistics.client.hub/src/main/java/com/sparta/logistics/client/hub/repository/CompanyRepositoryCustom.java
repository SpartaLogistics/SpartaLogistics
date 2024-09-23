package com.sparta.logistics.client.hub.repository;

import com.sparta.logistics.client.hub.enums.CompanyType;
import com.sparta.logistics.client.hub.model.Company;

import java.util.List;
import java.util.UUID;

public interface CompanyRepositoryCustom {
    List<Company> searchCompanies(String name, CompanyType companyType, String address, UUID managingHubId);
}
