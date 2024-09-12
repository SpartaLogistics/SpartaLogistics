package com.sparta.logistics.client.user.controller;

import com.sparta.logistics.client.user.dto.DeliveryManagerResponse;
import com.sparta.logistics.client.user.dto.ManagerRequestDto;
import com.sparta.logistics.client.user.model.User;
import com.sparta.logistics.client.user.repository.UserRepository;
import com.sparta.logistics.client.user.service.DeliveryManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/delivery_managers")
@RequiredArgsConstructor
public class DeliveryManagerController {
    private final DeliveryManagerService deliveryManagerService;
    private final UserRepository userRepository;

//    @PostMapping
//    public ResponseEntity<DeliveryManagerResponse> createDeliveryManager(@RequestBody ManagerRequestDto requestDto, @AuthenticationPrincipal SecurityUserDetails userDetails) {
//        User user = userRepository.findById(userDetails.getId())
//                .orElseThrow(() -> new IllegalArgumentException("User not found"));
//        DeliveryManagerResponse response = deliveryManagerService.createDeliveryManager(requestDto, user.getId());
//        return ResponseEntity.ok(response);
//    }

    @GetMapping
    public ResponseEntity<List<DeliveryManagerResponse>> getAllDeliveryManagers() {
        List<DeliveryManagerResponse> deliveryManagers = deliveryManagerService.getAllDeliveryManagers();
        return ResponseEntity.ok(deliveryManagers);
    }

    @GetMapping("/{deliveryId}")
    public ResponseEntity<DeliveryManagerResponse> getDeliveryManagerById(@PathVariable("deliveryId") UUID deliveryId) {
        DeliveryManagerResponse deliveryManagerResponse = deliveryManagerService.getDeliveryManagerById(deliveryId);
        return ResponseEntity.ok(deliveryManagerResponse);
    }

    @PatchMapping("/{deliveryId}")
    public ResponseEntity<DeliveryManagerResponse> patchDeliveryManager(@PathVariable("deliveryId") UUID deliveryId, @RequestBody ManagerRequestDto requestDto) {
        DeliveryManagerResponse response = deliveryManagerService.patchDeliveryManager(deliveryId, requestDto);
        return ResponseEntity.ok(response);
    }

//    @DeleteMapping("/{deliveryId}")
//    public ResponseEntity<DeliveryManagerResponse> deleteDeliveryManager(@PathVariable("deliveryId") UUID deliveryId,, @AuthenticationPrincipal SecurityUserDetails userDetails) {
//        User user = userRepository.findById(userDetails.getId())
//                .orElseThrow(() -> new IllegalArgumentException("User not found"));
//        DeliveryManagerResponse response = deliveryManagerService.deleteDeliveryManager(deliveryId, user.getId());
//        return ResponseEntity.ok(response);
//    }
}
