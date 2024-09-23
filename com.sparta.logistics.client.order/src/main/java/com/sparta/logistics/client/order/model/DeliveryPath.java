package com.sparta.logistics.client.order.model;


import com.sparta.logistics.client.order.common.type.DeliveryStatus;
import com.sparta.logistics.client.order.dto.DeliveryPathRequestDto;
import com.sparta.logistics.common.model.Timestamped;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "p_delivery_paths")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DeliveryPath extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "delivery_path_id", updatable = false, nullable = false)
    private UUID deliveryPathId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_id", insertable = false, updatable = false)
    private Delivery delivery;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private DeliveryStatus status;

    @Column(name = "sequence", nullable = false)
    private Integer sequence;

    @Column(name = "departure_id", nullable = false)
    private UUID departureId;

    @Column(name = "arrival_id")
    private UUID arrivalId;

    private Long expectedDistance;

    private Long expectedTime;

    private Long actualDistance;

    private Long actualTime;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;

    public void softDelete(String deletedByUserId) {
        this.delete(deletedByUserId);
        this.isDeleted = true;
    }

    @Builder(builderMethodName = "DeliveryPathCreateBuilder", builderClassName = "DeliveryPathCreateBuilder")
    public DeliveryPath(DeliveryPathRequestDto deliveryPathRequestDto, Delivery delivery) {
        this.deliveryPathId = deliveryPathRequestDto.getDeliveryPathId();
        this.delivery = delivery;
        this.sequence = deliveryPathRequestDto.getSequence();
        this.departureId = deliveryPathRequestDto.getDepartureId();
        this.arrivalId = deliveryPathRequestDto.getArrivalId();
        this.expectedDistance = deliveryPathRequestDto.getExpectedDistance();
        this.expectedTime = deliveryPathRequestDto.getExpectedTime();
        this.isDeleted = false;
        this.status = DeliveryStatus.HUB_ARRIVAL;
    }

}
