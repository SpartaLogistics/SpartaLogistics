package com.sparta.logistics.client.hub.model;

import com.sparta.logistics.client.hub.common.Timestamped;
import com.sparta.logistics.client.hub.dto.HubRequestDto;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "p_hubs")
public class Hub extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "hub_id")
    private UUID hubId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false, precision = 11, scale = 8)
    private BigDecimal latitude;

    @Column(nullable = false, precision = 12, scale = 8)
    private BigDecimal longitude;

    @Column(nullable = false)
    private Boolean is_deleted = false;

    @OneToMany(mappedBy = "departureId")
    private List<HubPath> departurePaths = new ArrayList<>();

    @OneToMany(mappedBy = "arrivalId")
    private List<HubPath> arrivalPaths = new ArrayList<>();

    @OneToMany(mappedBy = "nextHubId")
    private List<HubPath> nextHubPaths = new ArrayList<>();

    // 소프트 삭제 메서드
    public void softDelete() {
        this.is_deleted = true;
    }


    // 허브 생성용 빌더 클래스
    @Builder(builderClassName = "CreateHubInfoBuilder", builderMethodName = "createHubInfoBuilder")
    public Hub(HubRequestDto hubRequestDto) {
        this.name = hubRequestDto.getName();
        this.address = hubRequestDto.getAddress();
        this.latitude = hubRequestDto.getLatitude();
        this.longitude = hubRequestDto.getLongitude();
    }

    // 허브 수정용 메서드
    public void update(HubRequestDto hubRequestDto) {
        if (hubRequestDto.getName() != null) {
            this.name = hubRequestDto.getName();
        }
        if (hubRequestDto.getAddress() != null) {
            this.address = hubRequestDto.getAddress();
        }
        if (hubRequestDto.getLatitude() != null) {
            this.latitude = hubRequestDto.getLatitude();
        }
        if (hubRequestDto.getLongitude() != null) {
            this.longitude = hubRequestDto.getLongitude();
        }
    }


}

