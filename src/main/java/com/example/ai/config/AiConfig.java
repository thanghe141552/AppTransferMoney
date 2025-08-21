//package com.example.ai.config;
//
//import io.micrometer.observation.ObservationRegistry;
//import org.springframework.ai.chat.client.ChatClient;
//import org.springframework.ai.chat.model.ChatModel;
//import org.springframework.ai.model.ApiKey;
//import org.springframework.ai.model.SimpleApiKey;
//import org.springframework.ai.model.openai.autoconfigure.OpenAIAutoConfigurationUtil;
//import org.springframework.ai.model.openai.autoconfigure.OpenAiAudioSpeechProperties;
//import org.springframework.ai.model.openai.autoconfigure.OpenAiConnectionProperties;
//import org.springframework.ai.model.tool.ToolCallingManager;
//import org.springframework.ai.openai.OpenAiAudioSpeechModel;
//import org.springframework.ai.openai.OpenAiChatModel;
//import org.springframework.ai.openai.OpenAiChatOptions;
//import org.springframework.ai.openai.api.OpenAiApi;
//import org.springframework.ai.openai.api.OpenAiAudioApi;
//import org.springframework.beans.factory.ObjectProvider;
//import org.springframework.boot.autoconfigure.web.client.RestClientBuilderConfigurer;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.retry.support.RetryTemplate;
//import org.springframework.web.client.ResponseErrorHandler;
//import org.springframework.web.client.RestClient;
//import org.springframework.web.reactive.function.client.WebClient;
//
//@Configuration
//public class AiConfig {
//	@Bean(name = "myChatClient")
//    public ChatClient chatClient() {
//
//        String apiKeyValue = "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";
//
//        ApiKey apiKey = new SimpleApiKey(apiKeyValue);
//
//        OpenAiApi openAiApi = OpenAiApi.builder().apiKey(apiKey).build();
//
//        ChatModel chatModel = OpenAiChatModel.builder().openAiApi(openAiApi).build();
//
//        return ChatClient.builder(chatModel).build();
//    }
//
////    public OpenAiAudioSpeechModel openAiAudioSpeechModelInitialize(){
////
////        OpenAiAudioSpeechModel openAiAudioSpeechModel = new OpenAiAudioSpeechModel();
////
////        return
////    }
////
////    public OpenAiAudioSpeechModel openAiAudioSpeechModel(OpenAiConnectionProperties commonProperties, OpenAiAudioSpeechProperties speechProperties, RetryTemplate retryTemplate, ObjectProvider<RestClient.Builder> restClientBuilderProvider, ObjectProvider<WebClient.Builder> webClientBuilderProvider, ResponseErrorHandler responseErrorHandler) {
////        OpenAIAutoConfigurationUtil.ResolvedConnectionProperties resolved = OpenAIAutoConfigurationUtil.resolveConnectionProperties(commonProperties, speechProperties, "speech");
////        OpenAiAudioApi openAiAudioApi = OpenAiAudioApi.builder().baseUrl(resolved.baseUrl()).apiKey(new SimpleApiKey(resolved.apiKey())).headers(resolved.headers()).restClientBuilder((RestClient.Builder)restClientBuilderProvider.getIfAvailable(RestClient::builder)).webClientBuilder((WebClient.Builder)webClientBuilderProvider.getIfAvailable(WebClient::builder)).responseErrorHandler(responseErrorHandler).build();
////        return new OpenAiAudioSpeechModel(openAiAudioApi, speechProperties.getOptions());
////    }
//
//}
