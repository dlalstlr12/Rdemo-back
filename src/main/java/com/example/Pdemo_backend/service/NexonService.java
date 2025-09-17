package com.example.Pdemo_backend.service;

import java.util.Map;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class NexonService {
  private final WebClient webClient;

  public NexonService(WebClient nexonWebClient) {
    this.webClient = nexonWebClient;
  }

  // 닉네임 → OCID
  public Mono<String> fetchOcidByName(String characterName) {
    return webClient.get()
        .uri(uriBuilder -> uriBuilder
            .path("/maplestory/v1/id")
            .queryParam("character_name", characterName) // WebClient가 자동 인코딩
            .build())
        .retrieve()
        .bodyToMono(new ParameterizedTypeReference<Map<String, String>>() {})
        .map(m -> m.get("ocid"));
  }

  // OCID → 캐릭터 기본 정보
  public Mono<Map<String, Object>> fetchCharacterBasic(String ocid) {
    return webClient.get()
        .uri(uriBuilder -> uriBuilder
            .path("/maplestory/v1/character/basic")
            .queryParam("ocid", ocid)
            .build())
        .retrieve()
        .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {});
  }

  // 닉네임 → 캐릭터 기본 정보 (조합)
  public Mono<Map<String, Object>> getCharacterByName(String characterName) {
    return fetchOcidByName(characterName)
        .flatMap(ocid -> {
          System.out.println("조회된 OCID: " + ocid); // 디버그 로그
          return fetchCharacterBasic(ocid);
        });
  }
}