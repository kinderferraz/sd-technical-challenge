package com.sd.challenge.booth.mapper;

import com.sd.challenge.booth.data.entities.Poll;
import com.sd.challenge.booth.resources.widgets.Element;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class UiMapper {

    @Value("${application.properties.base-url}")
    static String baseUrl;

    public Element mapPollToElement(Poll poll, Map<String, String> data) {
        return Element.builder()
                .titulo(poll.getTitle())
                .url(baseUrl + "ui/polls/listing")
                .body(data)
                .build();
    }

}
