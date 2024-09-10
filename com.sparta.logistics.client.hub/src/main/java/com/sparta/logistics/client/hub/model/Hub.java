package com.sparta.logistics.client.hub.model;

import com.sparta.logistics.common.model.Timestamped;
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


}

