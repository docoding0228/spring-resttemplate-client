package com.sparta.springresttemplateclient.naver.service;

import com.sparta.springresttemplateclient.naver.dto.ItemDto;
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

@Slf4j(topic = "NAVER API")
// @Slf4j: 롬복의 로깅 기능을 활성화하는 애노테이션
// 로그 메시지의 주제를 "NAVER API"로 지정하겠다는 의미
// 프로그램이 실행되는 동안 발생하는 이벤트 및 상태를 기록함으로써 오류를 찾고 디버깅할 수 있다.
@Service
public class NaverApiService {

    private final RestTemplate restTemplate;

    /**
     * NaverApiService 생성자.
     * RestTemplateBuilder를 통해 RestTemplate 객체를 생성합니다.
     *
     * @param builder RestTemplateBuilder 객체
     */
    public NaverApiService(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    /**
     * 검색 쿼리를 받아 Naver API를 통해 아이템 목록을 검색합니다.
     *
     * @param query 검색할 쿼리 문자열
     * @return 검색된 아이템 목록
     */

    public List<ItemDto> searchItems(String query) {
        // 요청 URL 만들기
        URI uri = UriComponentsBuilder
                .fromUriString("https://openapi.naver.com") // 기본 URL 설정
                .path("/v1/search/shop.json") // 경로 추가
                .queryParam("display", 15) // "display" 파라미터 추가
                .queryParam("query", query) // "query" 파라미터 추가
                .encode() // URL 인코딩
                .build() // URI 객체 생성
                .toUri();
        log.info("uri = " + uri); // URI 로그 출력

        // GET 요청 엔티티 생성
        RequestEntity<Void> requestEntity = RequestEntity
                .get(uri) // GET 메소드 설정
                .header("X-Naver-Client-Id", "m3QYhQwgrZlPARkFee7K") // 클라이언트 ID 헤더 추가
                .header("X-Naver-Client-Secret", "bFp1InZE_v") // 클라이언트 시크릿 헤더 추가
                .build(); // 요청 엔티티 생성

        // Naver API에 요청 보내고 응답 받기
        ResponseEntity<String> responseEntity = restTemplate.exchange(requestEntity, String.class);

        log.info("NAVER API Status Code : " + responseEntity.getStatusCode()); // 응답 상태 코드 로그 출력

        // 응답 본문을 JSON에서 ItemDto 목록으로 변환하여 반환
        return fromJSONtoItems(responseEntity.getBody());
    }

    /**
     * JSON 문자열을 ItemDto 객체 목록으로 변환합니다.
     *
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
