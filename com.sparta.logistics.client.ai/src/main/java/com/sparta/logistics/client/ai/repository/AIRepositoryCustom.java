package com.sparta.logistics.client.ai.repository;

import com.sparta.logistics.client.ai.dto.AIResponseDto;
import com.sparta.logistics.client.ai.dto.AISearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AIRepositoryCustom {

    Page<AIResponseDto> getAIList(AISearchCriteria aiSearchCriteria, Pageable pageable);

}
