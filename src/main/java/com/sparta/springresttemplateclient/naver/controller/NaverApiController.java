package com.sparta.springresttemplateclient.naver.controller;

import com.sparta.springresttemplateclient.naver.dto.ItemDto;
import com.sparta.springresttemplateclient.naver.service.NaverApiService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class NaverApiController {

    private final NaverApiService naverApiService;

    /**
     * NaverApiController 생성자.
     * NaverApiService를 주입받아 초기화합니다.
     *
     * @param naverApiService 주입될 NaverApiService 객체
     */
    public NaverApiController(NaverApiService naverApiService) {
        this.naverApiService = naverApiService;
    }

    /**
     * 검색 쿼리를 받아 Naver API를 통해 아이템 목록을 검색합니다.
     *
     * @param query 검색할 쿼리 문자열
     * @return 검색된 아이템 목록
     */
    @GetMapping("/search")
    public List<ItemDto> searchItems(@RequestParam String query)  {
        return naverApiService.searchItems(query);
    }
}
