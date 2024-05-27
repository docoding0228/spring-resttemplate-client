package com.sparta.springresttemplateclient.service;

import com.sparta.springresttemplateclient.dto.ItemDto;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class RestTemplateService {

    private final RestTemplate restTemplate;

    /**
     * RestTemplateService 생성자.
     * RestTemplateBuilder를 통해 RestTemplate 객체를 생성합니다.
     */
    public RestTemplateService(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    /**
     * GET 요청을 통해 단일 객체를 반환합니다.
     * @param query 요청에 포함될 쿼리 문자열
     * @return ItemDto 반환된 객체
     */
    public ItemDto getCallObject(String query) {
        // 요청 URL 만들기
        URI uri = UriComponentsBuilder
                .fromUriString("http://localhost:7070")
                .path("/api/server/get-call-obj")
                .queryParam("query", query)
                .encode()
                .build()
                .toUri();
        log.info("uri = " + uri);

        // GET 요청 실행 및 응답 받기
        ResponseEntity<ItemDto> responseEntity = restTemplate.getForEntity(uri, ItemDto.class);
        log.info("statusCode = " + responseEntity.getStatusCode());
        return responseEntity.getBody();
    }

    /**
     * GET 요청을 통해 객체 목록을 반환합니다.
     * @return List<ItemDto> 반환된 객체 목록
     */
    public List<ItemDto> getCallList() {
        // 요청 URL 만들기
        URI uri = UriComponentsBuilder
                .fromUriString("http://localhost:7070")
                .path("/api/server/get-call-list")
                .encode()
                .build()
                .toUri();
        log.info("uri = " + uri);

        // GET 요청 실행 및 응답 받기
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(uri, String.class);
        log.info("statusCode = " + responseEntity.getStatusCode());
        log.info("Body = " + responseEntity.getBody());

        // JSON 응답을 객체 목록으로 변환
        return fromJSONtoItems(responseEntity.getBody());
    }

    /**
     * POST 요청을 통해 객체를 반환합니다.
     * 현재는 null을 반환하도록 되어 있습니다.
     * @param query 요청에 포함될 쿼리 문자열
     * @return ItemDto 반환된 객체
     */
    public ItemDto postCall(String query) {
        return null;
    }

    /**
     * 특정 토큰을 사용하여 객체 목록을 반환합니다.
     * 현재는 null을 반환하도록 되어 있습니다.
     * @param token 인증 토큰
     * @return List<ItemDto> 반환된 객체 목록
     */
    public List<ItemDto> exchangeCall(String token) {
        return null;
    }

    /**
     * JSON 문자열을 ItemDto 객체 목록으로 변환합니다.
     * @param responseEntity JSON 문자열
     * @return List<ItemDto> 변환된 객체 목록
     */
    public List<ItemDto> fromJSONtoItems(String responseEntity) {
        JSONObject jsonObject = new JSONObject(responseEntity);
        JSONArray items  = jsonObject.getJSONArray("items");
        List<ItemDto> itemDtoList = new ArrayList<>();

        for (Object item : items) {
            ItemDto itemDto = new ItemDto((JSONObject) item);
            itemDtoList.add(itemDto);
        }

        return itemDtoList;
    }
}
