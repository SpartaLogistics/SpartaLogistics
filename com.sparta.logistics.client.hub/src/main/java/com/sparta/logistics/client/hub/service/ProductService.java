package com.sparta.logistics.client.hub.service;

import com.sparta.logistics.client.hub.common.exception.HubException;
import com.sparta.logistics.client.hub.dto.ProductDetailResponseDto;
import com.sparta.logistics.client.hub.dto.ProductRequestDto;
import com.sparta.logistics.client.hub.dto.ProductResponseDto;
import com.sparta.logistics.client.hub.model.Company;
import com.sparta.logistics.client.hub.model.Product;
import com.sparta.logistics.client.hub.repository.ProductRepository;
import com.sparta.logistics.common.type.ApiResultError;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final HubService hubService;
    private final CompanyService companyService;
    private final ProductRepository productRepository;

    @Transactional
    public ProductResponseDto createProduct(ProductRequestDto requestDto) throws HubException {
        //상품 업체가 존재하는지 확인
        Company company = companyService.findCompanyById(requestDto.getCompanyId());

        // 상품 관리 허브 ID를 확인하여 존재하는지 확인
        hubService.findHubById(company.getManagingHubId());

        Product product = Product.CreateProductInfoBuilder()
                .productRequestDto(requestDto)
                .company(company)
                .build();

        productRepository.save(product);
        return ProductResponseDto.of(product);
    }

    @Transactional
    public ProductResponseDto updateProduct(UUID productId, ProductRequestDto requestDto) throws HubException {
        Product product = productRepository.findByProductId(productId)
                .orElseThrow(() -> new HubException(ApiResultError.PRODUCT_NO_EXIST));

        Company company = null;
        if (requestDto.getCompanyId() != null) {
            company = companyService.findCompanyById(requestDto.getCompanyId());
            hubService.findHubById(company.getManagingHubId());
        }

        product.updateProduct(requestDto, company);

        return ProductResponseDto.of(product);
    }

    @Transactional(readOnly = true)
    public ProductDetailResponseDto getProduct(UUID productId) throws HubException {
        Product product = productRepository.findByProductId(productId)
                .orElseThrow(() -> new HubException(ApiResultError.PRODUCT_NO_EXIST));

        return ProductDetailResponseDto.of(product);
    }

    @Transactional(readOnly = true)
    public Page<ProductResponseDto> getAllProducts(Pageable pageable) throws HubException {
        Page<Product> productPage = productRepository.findAllByIsDeletedFalse(pageable);
        return productPage.map(ProductResponseDto::of);
    }

    @Transactional
    public void deleteProduct(UUID productId) throws HubException {
        Product product = productRepository.findByProductId(productId)
                .orElseThrow(() -> new HubException(ApiResultError.PRODUCT_NO_EXIST));

        product.softDelete();
        productRepository.save(product);
    }

    public List<ProductResponseDto> searchProducts(String name, UUID companyId) throws HubException {
        List<Product> products = productRepository.searchProducts(name, companyId);

        // 검색 결과가 없을 경우
        if (products.isEmpty()) {
            throw new HubException(ApiResultError.SEARCH_NO_RESULT);
        }
        return products.stream().map(ProductResponseDto::of).collect(Collectors.toList());
    }
}
