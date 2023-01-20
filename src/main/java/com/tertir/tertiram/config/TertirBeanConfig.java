package com.tertir.tertiram.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

@Configuration
public class TertirBeanConfig {

    @Value("${tertir.url}")
    private String tertirURL;

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder){
        return restTemplateBuilder
                .rootUri(tertirURL).messageConverters(new MappingJackson2HttpMessageConverter(),
                        new ByteArrayHttpMessageConverter())
                .build();
    }

}
