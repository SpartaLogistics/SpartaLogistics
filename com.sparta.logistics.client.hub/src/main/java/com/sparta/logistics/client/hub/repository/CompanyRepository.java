package com.sparta.logistics.client.hub.repository;

import com.sparta.logistics.client.hub.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CompanyRepository extends JpaRepository<Company, UUID> {
    Optional<Company> findByCompanyIdAndIsDeletedFalse(UUID companyId);

    default Optional<Company> findByCompanyId(UUID companyId) {
        return findByCompanyIdAndIsDeletedFalse(companyId);
    }

    List<Company> findAllByIsDeletedFalse();
}
