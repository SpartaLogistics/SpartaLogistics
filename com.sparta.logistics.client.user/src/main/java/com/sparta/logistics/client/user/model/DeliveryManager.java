package com.sparta.logistics.client.user.model;

import com.sparta.logistics.client.user.common.BaseEntity;
import com.sparta.logistics.client.user.dto.ManagerRequestDto;
import com.sparta.logistics.client.user.enums.DeliveryManagerType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.UUID;

@Entity(name = "p_delivery_managers")
@Getter
@NoArgsConstructor
@ToString
public class DeliveryManager extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "delivery_manager_id")
    private UUID id;

    //hubid
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String slack_id;

    @Column(name = "delivery_manager_type")
    private DeliveryManagerType type;

    public DeliveryManager(User user, String slack_id, DeliveryManagerType type) {
        this.user = user;
        this.slack_id = slack_id;
        this.type = type;
    }
    public static DeliveryManager createDeliveryManager(User user, String slack_id, DeliveryManagerType type) {
        return new DeliveryManager(user, slack_id, type);
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
