package com.sparta.logistics.client.ai.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.logistics.client.ai.dto.AIResponseDto;
import com.sparta.logistics.client.ai.dto.AISearchCriteria;
import com.sparta.logistics.client.ai.model.QAI;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;


@Repository
public class AIRepositoryCustomImpl implements AIRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    public AIRepositoryCustomImpl(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public Page<AIResponseDto> getAIList(AISearchCriteria aiSearchCriteria, Pageable pageable) {
        QAI qAI = QAI.aI;

        var query = jpaQueryFactory.selectFrom(qAI)
                .where(qAI.isDeleted.isFalse()); // 삭제되지 않은 레코드만 선택

        // 동적 필터링
        Long userId = aiSearchCriteria.getUserId();
        if (userId != null) {
            query = query.where(qAI.userId.eq(userId));
        }
        String service = aiSearchCriteria.getService();
        if (service != null && !service.isEmpty()) {
            query = query.where(qAI.service.eq(service));
        }
        String question = aiSearchCriteria.getQuestion();
        if (question != null && !question.isEmpty()) {
            query = query.where(qAI.question.like("%" + question + "%"));
        }

        // 총 결과 수 계산
        long total = query.fetchCount();

        // 페이징 적용
        List<AIResponseDto> content = query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch()
                .stream()
                .map(entity -> AIResponseDto.builder()
                        .aiId(entity.getAiId())
                        .userId(entity.getUserId())
                        .service(entity.getService())
                        .question(entity.getQuestion())
                        .content(entity.getContent())
                        .isDeleted(entity.isDeleted())
                        .build())
                .collect(Collectors.toList());

        return new PageImpl<>(content, pageable, total);
    }
}
