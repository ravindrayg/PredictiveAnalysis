package com.MetLife.PredectiveAnalysis.Controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.MetLife.PredectiveAnalysis.Service.AzureOpenAIService;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class ChatController {

    private final AzureOpenAIService openAIService;

    public ChatController(AzureOpenAIService openAIService) {
        this.openAIService = openAIService;
    }

    @GetMapping("/chat")
    public String getPreventiveCarePlans(@RequestParam String question) {
        return openAIService.getChatCompletion(question);
    }
}

