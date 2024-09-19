package com.sparta.logistics.client.user.model;

import com.sparta.logistics.client.user.dto.ManagerRequestDto;
import com.sparta.logistics.client.user.enums.DeliveryManagerType;
import com.sparta.logistics.common.model.Timestamped;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.UUID;

@Entity(name = "p_delivery_managers")
@Getter
@NoArgsConstructor
@ToString
public class DeliveryManager extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "delivery_manager_id")
    private UUID id;

    @Column(nullable = false)
    private UUID hub_id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String slack_id;

    @Column(name = "delivery_manager_type")
    private DeliveryManagerType type;

    @Column(nullable = false)
    private Boolean isDeleted = false;

    public void softDelete() {
        this.isDeleted = true;
    }

    public DeliveryManager(UUID hub_id, User user, String slack_id, DeliveryManagerType type) {
        this.hub_id = hub_id;
        this.user = user;
        this.slack_id = slack_id;
        this.type = type;
    }
    public static DeliveryManager createDeliveryManager(UUID hub_id, User user, String slack_id, DeliveryManagerType type) {
        return new DeliveryManager(hub_id, user, slack_id, type);
    }
    public void update(ManagerRequestDto managerRequestDto) {
        if(managerRequestDto.getSlackId() != null) {
            this.slack_id = managerRequestDto.getSlackId();
        }
        if(managerRequestDto.getDeliveryManagerType() != null) {
            this.type = managerRequestDto.getDeliveryManagerType();
        }

    }
}
