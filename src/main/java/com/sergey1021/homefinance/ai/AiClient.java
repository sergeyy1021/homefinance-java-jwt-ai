package com.sergey1021.homefinance.ai;

import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AiClient {


    private final OpenAiService openAiService;
    private final String model;

    public AiClient(@Value("${openai.api.key}") String apiKey,
                    @Value("${openai.api.model}") String model) {
        this.openAiService = new OpenAiService(apiKey);
        this.model = model;
    }

    public String helper(String inputText) {
        System.out.println("⚙️ Вызов ИИ: " + inputText);
        ChatMessage systemMessage = new ChatMessage("system", "Ты помощник по финансам.");
        ChatMessage userMessage = new ChatMessage("user", inputText);

        ChatCompletionRequest request = ChatCompletionRequest.builder()
                .model(model)
                .messages(List.of(systemMessage, userMessage))
                .maxTokens(1000)
                .temperature(0.3)
                .build();

        return openAiService.createChatCompletion(request)
                .getChoices()
                .get(0)
                .getMessage()
                .getContent();

    }
}
