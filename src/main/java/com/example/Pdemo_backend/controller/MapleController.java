package com.example.Pdemo_backend.controller;

import com.example.Pdemo_backend.service.NexonService;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/maple")
@CrossOrigin(origins = "http://localhost:3000")
public class MapleController {

  private final NexonService nexonService;

  public MapleController(NexonService nexonService) {
    this.nexonService = nexonService;
  }

  // 테스트용: 닉네임 → 캐릭터 기본 정보
  @GetMapping("/character")
  public Mono<ResponseEntity<Map<String, Object>>> getCharacter(@RequestParam String name) {
    System.out.println("요청 닉네임: " + name);
    return nexonService.getCharacterByName(name)
        .map(ResponseEntity::ok)
        .onErrorResume(ex -> {
          ex.printStackTrace();
          return Mono.just(ResponseEntity.status(HttpStatus.BAD_GATEWAY)
              .body(Map.of("error", ex.getMessage())));
        });
  }

  // 테스트용: 프론트에서 닉네임 출력
  @GetMapping("/nickname")
  public Mono<ResponseEntity<String>> getNickname(@RequestParam String name) {
    System.out.println("요청 닉네임: " + name);
    return nexonService.getCharacterByName(name)
        .map(characterMap -> (String) characterMap.get("character_name"))
        .map(ResponseEntity::ok)
        .onErrorResume(ex -> {
          ex.printStackTrace();
          return Mono.just(ResponseEntity.status(502)
              .body("API 요청 실패: " + ex.getMessage()));
        });
  }
}
