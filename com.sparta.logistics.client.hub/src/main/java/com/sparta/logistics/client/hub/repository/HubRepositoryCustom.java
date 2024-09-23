package com.sparta.logistics.client.hub.repository;

import com.sparta.logistics.client.hub.model.Hub;

import java.util.List;

public interface HubRepositoryCustom {
    List<Hub> searchHubs(String name, String address);
}
