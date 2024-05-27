package com.sparta.springresttemplateclient.controller;

import com.sparta.springresttemplateclient.dto.ItemDto;
import com.sparta.springresttemplateclient.service.RestTemplateService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/client")
public class RestTemplateController {

    private final RestTemplateService restTemplateService;

    /**
     * RestTemplateController 생성자.
     * @param restTemplateService 주입될 RestTemplateService 객체
     */
    public RestTemplateController(RestTemplateService restTemplateService) {
        this.restTemplateService = restTemplateService;
    }

    /**
     * GET 요청을 통해 단일 객체를 반환합니다.
     * @param query 요청에 포함될 쿼리 문자열
     * @return ItemDto 반환된 객체
     */
    @GetMapping("/get-call-obj")
    public ItemDto getCallObject(String query) {
        return restTemplateService.getCallObject(query);
    }

    /**
     * GET 요청을 통해 객체 목록을 반환합니다.
     * @return List<ItemDto> 반환된 객체 목록
     */
    @GetMapping("/get-call-list")
    public List<ItemDto> getCallList() {
        return restTemplateService.getCallList();
    }

    /**
     * POST 요청을 통해 단일 객체를 반환합니다.
     * @param query 요청에 포함될 쿼리 문자열
     * @return ItemDto 반환된 객체
     */
    @GetMapping("/post-call")
    public ItemDto postCall(String query) {
        return restTemplateService.postCall(query);
    }

    /**
     * 인증 토큰을 사용하여 객체 목록을 반환합니다.
     * @param token 인증 토큰
     * @return List<ItemDto> 반환된 객체 목록
     */
    @GetMapping("/exchange-call")
    public List<ItemDto> exchangeCall(@RequestHeader("Authorization") String token) {
        return restTemplateService.exchangeCall(token);
    }
}
