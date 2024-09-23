package com.sparta.logistics.client.user.common;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.hibernate.annotations.Comment;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {

    @CreatedDate
    @Column(updatable = false, columnDefinition = "TIMESTAMP", nullable = false)
    @Comment("생성일시")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(columnDefinition = "TIMESTAMP")
    @Comment("수정일시")
    private LocalDateTime updatedAt;

    @Column(columnDefinition = "TIMESTAMP")
    @Comment("삭제일시")
    private LocalDateTime deletedAt;



    @CreatedBy
    @Column(updatable = false)
    private Long createdBy;

    @LastModifiedBy
    private Long updatedBy;

    private Long deletedBy;

    private boolean isDeleted = false;

    public void delete(Long handlerId) {
        this.deletedBy = handlerId;
        this.isDeleted = true;
        this.deletedAt = LocalDateTime.now();
    }

    public void undoDelete(Long handlerId) {
        this.deletedBy = null;
        this.deletedAt = null;
        this.isDeleted = false;
    }






}