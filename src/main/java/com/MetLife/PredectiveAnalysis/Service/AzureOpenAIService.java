package com.MetLife.PredectiveAnalysis.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class AzureOpenAIService {

    @Value("${azure.openai.endpoint}")
    private String endpoint;

    @Value("${azure.openai.key}")
    private String apiKey;

    @Value("${azure.openai.deploymentName}")
    private String deploymentName;

    @Value("${azure.openai.apiVersion}")
    private String apiVersion;

    private final RestTemplate restTemplate = new RestTemplate();

    public String getChatCompletion(String userMessage) {
        String url = String.format("%sopenai/deployments/%s/chat/completions?api-version=%s",
                endpoint, deploymentName, apiVersion);

        // Build request body
        Map<String, Object> requestBody = Map.of(
                "messages", List.of(
                        Map.of("role", "system", "content", "You are a helpful AI assistant."),
                        Map.of("role", "user", "content", userMessage)
                )
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("api-key", apiKey);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(url, requestEntity, Map.class);

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            // Extract the answer from the response JSON structure
            // response JSON structure: { choices: [ { message: { content: "answer here" } } ] }
            try {
                var choices = (List<Map<String, Object>>) response.getBody().get("choices");
                if (choices != null && !choices.isEmpty()) {
                    Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
                    if (message != null) {
                        return (String) message.get("content");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return "Failed to get response from Azure OpenAI";
    }
}
