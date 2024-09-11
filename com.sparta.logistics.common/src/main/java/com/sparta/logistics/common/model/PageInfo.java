package com.sparta.logistics.common.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PageInfo {

    private long totalItem; //총 아이템수
    private int pageItemSize; //페이지당 아이템수
    private int groupSize; //페이지 그룹수

    private int currentPage; //현재 페이지 번호
    private int[] currentPageGroup; //현재 페이지 그룹
    private int totalPage; //총 페이지 수

    private boolean havePrev;
    private boolean haveNext;


    public PageInfo(Page<?> data) {
        if(data.getPageable() != null) {

        }

        this.totalItem = data.getTotalElements();
        this.pageItemSize = data.getSize();
        this.groupSize = data.getNumberOfElements();
        this.currentPage = data.getNumber();
        this.totalPage = data.getTotalPages();
        this.havePrev = data.hasPrevious();
        this.haveNext = data.hasNext();

    }
}
