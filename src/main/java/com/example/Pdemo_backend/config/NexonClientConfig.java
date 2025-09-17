package com.example.Pdemo_backend.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class NexonClientConfig {
  @Bean
  public WebClient nexonWebClient() {
    Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
    String apiKey = dotenv.get("NEXON_API_KEY");

    if (apiKey == null || apiKey.isEmpty()) {
      throw new IllegalStateException("NEXON_API_KEY가 .env에서 읽히지 않았습니다!");
    }

    return WebClient.builder()
        .baseUrl("https://open.api.nexon.com")
        .defaultHeader("x-nxopen-api-key", apiKey)
        .defaultHeader("accept", "application/json")
        .build();
  }

}

