package com.sparta.logistics.client.order.domain.model;

import com.sparta.logistics.client.order.common.type.DeliveryStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.BindParam;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "p_delivery")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Delivery {

    @Id
    @Column(name = "delivery_id", columnDefinition = "UUID")
    private UUID deliveryId;

    @Column(name = "order_id", columnDefinition = "UUID", nullable = false)
    private UUID orderId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private DeliveryStatus status;

    @Column(name = "departure_id", columnDefinition = "UUID", nullable = false)
    private UUID departureId;

    @Column(name = "arrival_id", columnDefinition = "UUID", nullable = false)
    private UUID arrivalId;

    @Column(name = "address", length = 200, nullable = false)
    private String address;

    @Column(name = "receiver", length = 100, nullable = false)
    private String receiver;

    @Column(name = "slack_id", length = 255)
    private String slackId;

    private Boolean isDeleted = false;

    @OneToMany(mappedBy = "delivery")
    private List<DeliveryPaths> deliveryPathsList;
}

