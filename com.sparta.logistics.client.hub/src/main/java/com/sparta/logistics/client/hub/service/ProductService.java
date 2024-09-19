package com.sparta.logistics.client.hub.service;

import com.sparta.logistics.client.hub.common.exception.HubException;
import com.sparta.logistics.client.hub.dto.ProductDetailResponseDto;
import com.sparta.logistics.client.hub.dto.ProductRequestDto;
import com.sparta.logistics.client.hub.dto.ProductResponseDto;
import com.sparta.logistics.client.hub.model.Company;
import com.sparta.logistics.client.hub.model.Product;
import com.sparta.logistics.client.hub.repository.ProductRepository;
import com.sparta.logistics.common.client.UserClient;
import com.sparta.logistics.common.kafka.ProductDeleted;
import com.sparta.logistics.common.model.EventSerializer;
import com.sparta.logistics.common.model.RoleType;
import com.sparta.logistics.common.model.UserVO;
import com.sparta.logistics.common.type.ApiResultError;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class
ProductService {

    private final HubService hubService;
    private final CompanyService companyService;
    private final ProductRepository productRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final UserClient userClient;

    @Transactional
    public ProductResponseDto createProduct(ProductRequestDto requestDto, String username) throws HubException {
        UserVO currentUser = userClient.findByUsername(username);
        //상품 업체가 존재하는지 확인
        Company company = companyService.findCompanyById(requestDto.getCompanyId());

        validateUserPermission(currentUser, company);

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
    public ProductResponseDto updateProduct(UUID productId, ProductRequestDto requestDto, String username) throws HubException {
        UserVO currentUser = userClient.findByUsername(username);
        Product product = productRepository.findByProductId(productId)
                .orElseThrow(() -> new HubException(ApiResultError.PRODUCT_NO_EXIST));

        validateUserPermission(currentUser, product.getCompany());


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
    public void deleteProduct(UUID productId, String username, String userId) throws HubException {
        UserVO currentUser = userClient.findByUsername(username);
        Product product = productRepository.findByProductId(productId)
                .orElseThrow(() -> new HubException(ApiResultError.PRODUCT_NO_EXIST));

        validateUserPermission(currentUser, product.getCompany());

        product.softDelete(userId);
        productRepository.save(product);

        // 상품 삭제 이벤트 발행
        ProductDeleted productDeleted = ProductDeleted.of(
                product.getProductId(),
                product.getName(),
                product.getCompanyId(),
                product.getQuantity(),
                product.getManagingHubId(),
                product.getPrice()
        );
        kafkaTemplate.send("product-deleted", EventSerializer.serialize(productDeleted));
    }

    public List<ProductResponseDto> searchProducts(String name, UUID companyId) throws HubException {
        List<Product> products = productRepository.searchProducts(name, companyId);

        // 검색 결과가 없을 경우
        if (products.isEmpty()) {
            throw new HubException(ApiResultError.SEARCH_NO_RESULT);
        }
        return products.stream().map(ProductResponseDto::of).collect(Collectors.toList());
    }

    @Transactional
    public ProductResponseDto increaseQuantity(UUID productId, int quantity) throws HubException {
        log.info("Increasing quantity for product: {} by {}", productId, quantity);
        Product product = productRepository.findByProductId(productId)
                .orElseThrow(() -> new HubException(ApiResultError.PRODUCT_NO_EXIST));

        product.setQuantity(product.getQuantity() + quantity);
        productRepository.save(product);

        log.info("{}의 수량을 증가시킵니다. {} -> {}", product.getName(), product.getQuantity(), product.getQuantity() + quantity);
        return ProductResponseDto.of(product);
    }

    @Transactional
    public ProductResponseDto decreaseQuantity(UUID productId, int quantity) throws HubException {
        log.info("Decreasing quantity for product: {} by {}", productId, quantity);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new HubException(ApiResultError.PRODUCT_NO_EXIST));

        int newQuantity = product.getQuantity() - quantity;
        if (newQuantity < 0) {
            log.warn("{}의 수량이 부족합니다 : {}", product.getName(), product.getQuantity());
            throw new HubException(ApiResultError.STOCK_NO_EXIST);
        }
        product.setQuantity(newQuantity);
        productRepository.save(product);
        log.info("Quantity decreased for product: {}. New quantity: {}", productId, product.getQuantity());
        return ProductResponseDto.of(product);
    }

    // 테스트용 kafka
    @Transactional
    @KafkaListener(topics = "product-deleted", groupId = "product-service")
    public void consumeFromProduct(String message) {
        // 메시지를 ProductDeleted 객체로 역직렬화
        ProductDeleted event = EventSerializer.deserialize(message, ProductDeleted.class);

        // 역직렬화된 객체 사용
        log.info("@@@ {}", event.getProductId());
    }


    private void validateUserPermission(UserVO currentUser, Company company) throws HubException {
        if (currentUser.getRole() == RoleType.OWNER) {
            // 허브 관리자: 자신의 허브에 소속된 업체의 상품만 관리 가능
            if (!currentUser.getUsername().equals(company.getManagingHub().getManagerUsername())) {
                throw new HubException(ApiResultError.NOT_YOUR_HUB_COMPANY);
            }
        } else if (currentUser.getRole() == RoleType.CUSTOMER) {
            // 허브 업체: 자신의 업체의 상품만 관리 가능
            if (!currentUser.getUsername().equals(company.getCompanyManagerUsername())) {
                throw new HubException(ApiResultError.NOT_YOUR_COMPANY);
            }
        }
    }
}
