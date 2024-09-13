package com.sparta.logistics.client.hub.model;

import com.sparta.logistics.client.hub.common.Timestamped;
import com.sparta.logistics.client.hub.dto.HubPathRequestDto;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "p_hub_paths")
public class HubPath extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID hubPathId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "departure_id", nullable = false)
    private Hub departureHub;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "arrival_id", nullable = false)
    private Hub arrivalHub;

    @Column(name = "duration", nullable = false)
    private Long duration;

    @Column(nullable = false)
    private Boolean isDeleted = false;

    // 허브 이동 경로 생성용 빌더 클래스
    @Builder(builderClassName = "CreateHubPathInfoBuilder", builderMethodName = "createHubPathInfoBuilder")
    public HubPath(HubPathRequestDto hubPathRequestDto, Hub departureHub, Hub arrivalHub) {
        this.departureHub = departureHub;
        this.arrivalHub = arrivalHub;
        this.duration = hubPathRequestDto.getDuration();
        this.isDeleted = false;
    }

    // 허브 이동 경로 수정용 메서드
    public void update(HubPathRequestDto hubPathRequestDto, Hub newDepartureHub, Hub newArrivalHub) {
        if (newDepartureHub != null) {
            this.departureHub = newDepartureHub;
        }
        if (newArrivalHub != null) {
            this.arrivalHub = newArrivalHub;
        }
        if (hubPathRequestDto.getDuration() != null) {
            this.duration = hubPathRequestDto.getDuration();
        }
    }

    // 소프트 삭제 메서드
    public void softDelete() {
        this.isDeleted = true;
    }
}
