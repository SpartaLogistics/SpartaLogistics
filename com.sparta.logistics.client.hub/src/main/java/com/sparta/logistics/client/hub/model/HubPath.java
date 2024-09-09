package com.sparta.logistics.client.hub.model;

import com.sparta.logistics.client.hub.common.Timestamped;
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

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "hub_id")
//    private Hub hub;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "departure_id", nullable = false)
    private Hub departureId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "arrival_id", nullable = false)
    private Hub arrivalId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "next_hub_id", nullable = false)
    private Hub nextHubId;

    @Column(nullable = false)
    private Boolean is_deleted = false;
}
