package com.example.OzonHelper.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class OzonAgentService {
    private final ChatClient chatClient;
    private final OzonAgentTools tools;

    public OzonAgentService(ChatClient.Builder builder, OzonAgentTools tools){
        this.chatClient = builder.build();
        this.tools = tools;
    }

    public String ask(String message){
        return chatClient
                .prompt()
                .user(message)
                .tools(tools)
                .call()
                .content();
    }
}
