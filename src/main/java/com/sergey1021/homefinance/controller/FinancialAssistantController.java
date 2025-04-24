package com.sergey1021.homefinance.controller;

import com.sergey1021.homefinance.ai.AiClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class FinancialAssistantController {

    @Autowired
    private AiClient aiClient;

    @PostMapping("/ask")
    public ResponseEntity<Map<String, String>> askAssistant(@RequestBody QuestionRequest question) {
        try {
            String aiResponse = aiClient.helper(question.getQuestion());

            Map<String, String> response = new HashMap<>();
            response.put("answer", aiResponse);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }
}

class QuestionRequest {
    private String question;

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }
}
