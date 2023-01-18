package com.sd.challenge.booth.config;

import feign.codec.Decoder;
import feign.gson.GsonDecoder;
import feign.optionals.OptionalDecoder;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableFeignClients
@EnableJpaRepositories(basePackages = {"com.sd.challenge.booth.data"})
public class ApplicationConfiguration {

    @Bean
    public Decoder feignDecoder() {
        return new OptionalDecoder(new GsonDecoder());
    }

}
