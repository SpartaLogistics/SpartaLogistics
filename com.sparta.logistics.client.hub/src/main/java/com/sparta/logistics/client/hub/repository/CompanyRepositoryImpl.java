package com.sparta.logistics.client.hub.repository;

import com.querydsl.core.BooleanBuilder;
import com.sparta.logistics.client.hub.enums.CompanyType;
import com.sparta.logistics.client.hub.model.Company;
import com.sparta.logistics.client.hub.model.QCompany;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.UUID;

public class CompanyRepositoryImpl extends QuerydslRepositorySupport implements CompanyRepositoryCustom {

    public CompanyRepositoryImpl() {
        super(Company.class);
    }

    @Override
    public List<Company> searchCompanies(String name, CompanyType companyType, String address, UUID managingHubId) {
        QCompany company = QCompany.company;

        BooleanBuilder builder = new BooleanBuilder();

        if (StringUtils.hasText(name)) {
            builder.and(company.name.containsIgnoreCase(name));
        }

        if (companyType != null) {
            builder.and(company.companyType.eq(companyType));
        }

        if (StringUtils.hasText(address)) {
            builder.and(company.address.containsIgnoreCase(address));
        }

        if (managingHubId != null) {
            builder.and(company.managingHubId.hubId.eq(managingHubId));
        }

        return from(company)
                .where(builder)
                .fetch();
    }
}
