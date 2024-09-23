package com.sparta.logistics.client.ai.service;

import com.sparta.logistics.client.ai.dto.AIApiResponseDto;
import com.sparta.logistics.client.ai.dto.AISearchCriteria;
import com.sparta.logistics.client.ai.model.AI;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import com.sparta.logistics.client.ai.common.exception.AIException;
import com.sparta.logistics.client.ai.dto.AIRequestDto;
import com.sparta.logistics.client.ai.dto.AIResponseDto;
import com.sparta.logistics.client.ai.repository.AIRepository;
import com.sparta.logistics.common.type.ApiResultError;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AIService {

    private final AIRepository aiRepository;
    private final RestTemplate restTemplate;

    @Value("${spartaLogistics.ai.url}")
    private String apiUrl;

    @Value("${spartaLogistics.ai.api-key}")
    private String apiKey;

    public AIResponseDto createAI(AIRequestDto aiRequestDto) throws AIException {
        String url = UriComponentsBuilder.fromHttpUrl(apiUrl)
                .queryParam("key", apiKey)
                .toUriString();

        log.info("Creating AI request url: {}", url);

        String question = aiRequestDto.getQuestion();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        String requestJson = "{\"contents\":[{\"parts\":[{\"text\":";
        requestJson += "\"" + question + "\"";
        requestJson += "}]}]}";

        HttpEntity<String> requestEntity = new HttpEntity<>(requestJson, headers);

        ResponseEntity<AIApiResponseDto> aiResponse = restTemplate.exchange(url, HttpMethod.POST, requestEntity, AIApiResponseDto.class);

        AIApiResponseDto responseDto = aiResponse.getBody();

        if (responseDto != null && !responseDto.getCandidates().isEmpty()) {
            AIApiResponseDto.Candidate candidate = responseDto.getCandidates().get(0);
            String responseText = candidate.getContent().getParts().get(0).getText();
            aiRequestDto.setContent(responseText);

            AI ai = AI.AICreateBuilder()
                    //.userId() login User
                    .requestDto(aiRequestDto)
                    .build();
            log.info("AI Created AI : {}", ai);
            return AIResponseDto.of(aiRepository.save(ai));

        } else {
            throw new AIException(ApiResultError.ERROR_AI_API);
        }
    }

    public AIResponseDto getAI(UUID aiId) throws AIException {
        AI ai = aiRepository.findByAiIdAndIsDeletedFalse(aiId).orElseThrow(() ->
                new AIException(ApiResultError.ERROR_AI_NOT_EXIST));

        return AIResponseDto.of(ai);
    }

    public Page<AIResponseDto> getAIList(AISearchCriteria aiSearchCriteria, Pageable pageable) {
        return aiRepository.getAIList(aiSearchCriteria, pageable);
    }

    public void deleteAI(UUID aiId) throws AIException {
        AI ai = aiRepository.findByAiIdAndIsDeletedFalse(aiId).orElseThrow(() ->
                new AIException(ApiResultError.ERROR_AI_NOT_EXIST));

        ai.softDelete();

        aiRepository.save(ai);

    }
}
