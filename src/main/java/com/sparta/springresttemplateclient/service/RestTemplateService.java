package com.sparta.springresttemplateclient.service;

import com.sparta.springresttemplateclient.dto.ItemDto;
import com.sparta.springresttemplateclient.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.RequestEntity;
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
     * POST 요청을 통해 단일 객체를 반환합니다.
     * @param query 요청에 포함될 쿼리 문자열
     * @return ItemDto 반환된 객체
     */
    public ItemDto postCall(String query) {
        // 요청 URL 만들기
        URI uri = UriComponentsBuilder
                .fromUriString("http://localhost:7070")
                .path("/api/server/post-call/{query}")
                .encode()
                .build()
                .expand(query)
                .toUri();
        log.info("uri = " + uri);

        // 요청 본문에 포함될 사용자 정보 생성
        User user = new User("Robbie", "1234");

        // POST 요청 실행 및 응답 받기
        ResponseEntity<ItemDto> responseEntity = restTemplate.postForEntity(uri, user, ItemDto.class);
        log.info("statusCode = " + responseEntity.getStatusCode());

        return responseEntity.getBody();
    }

    /**
     * 인증 토큰을 사용하여 객체 목록을 반환합니다.
     * @param token 인증 토큰
     * @return List<ItemDto> 반환된 객체 목록
     */
    public List<ItemDto> exchangeCall(String token) {
        // 요청 URL 만들기
        URI uri = UriComponentsBuilder
                .fromUriString("http://localhost:7070")
                .path("/api/server/exchange-call")
                .encode()
                .build()
                .toUri();
        log.info("uri = " + uri);

        // 요청 본문에 포함될 사용자 정보 생성
        User user = new User("Robbie", "1234");

        // 요청 엔티티 생성 (헤더에 인증 토큰 포함)
        RequestEntity<User> requestEntity = RequestEntity
                .post(uri)
                .header("X-Authorization", token)
                .body(user);

        // 요청 실행 및 응답 받기
        ResponseEntity<String> responseEntity = restTemplate.exchange(requestEntity, String.class);

        // JSON 응답을 객체 목록으로 변환
        return fromJSONtoItems(responseEntity.getBody());
    }

    /**
     * JSON 문자열을 ItemDto 객체 목록으로 변환합니다.
     * @param responseEntity JSON 문자열
     * @return List<ItemDto> 변환된 객체 목록
     */
    public List<ItemDto> fromJSONtoItems(String responseEntity) {
        // JSON 문자열을 JSONObject로 변환
        JSONObject jsonObject = new JSONObject(responseEntity);

        // "items" 배열 추출
        JSONArray items = jsonObject.getJSONArray("items");
        List<ItemDto> itemDtoList = new ArrayList<>();

        // 각 배열 요소를 ItemDto 객체로 변환하여 리스트에 추가
        for (Object item : items) {
            ItemDto itemDto = new ItemDto((JSONObject) item);
            itemDtoList.add(itemDto);
        }
        return itemDtoList;
    }
}
