package com.soldesk.festival.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PageDTO {
    
    private int min; //최소페이지 
	private int max; //최대페이지
	private int prevPage; // 이전 버튼 페이지 번호
	private int nextPage; // 이전 버튼 페이지 번호
	private int pageCnt; //전체 페이지 개수 
	private int currentPage; // 현재 페이지 번호 
	private int contentPageCnt = 10; // 페이지당 출력 게시글 수 
	private int paginationCnt = 10; //페이지 버튼 개수
    
    
public PageDTO(int contentCnt, int currentPage) {
    this.currentPage = currentPage < 1 ? 1 : currentPage;

    if (contentCnt == 0) {
        pageCnt = 0;
        min = 0;
        max = 0;
        prevPage = 0;
        nextPage = 0;
        return;
    }

    pageCnt = contentCnt / contentPageCnt;
    if (contentCnt % contentPageCnt > 0) {
        pageCnt++;
    }

    min = ((currentPage - 1) / paginationCnt) * paginationCnt + 1;
    max = min + paginationCnt - 1;
    if (max > pageCnt) {
        max = pageCnt;
    }

    prevPage = min - 1;
    nextPage = max + 1;
    if (nextPage > pageCnt) {
        nextPage = 0; // 다음 페이지 없음
    }
}


    

    
}
