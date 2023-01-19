package com.sd.challenge.booth.mapper;

import com.sd.challenge.booth.data.entities.Poll;
import com.sd.challenge.booth.resources.widgets.Element;

import java.util.HashMap;
import java.util.Map;


public class UiMapper {

    public Element mapPollToElement(Poll poll, String baseUrl, Map<String, String> data) {
        return Element.builder()
                .titulo(poll.getTitle())
                .url(baseUrl + "ui/polls/details")
                .body(data)
                .build();
    }

    public static Map<String, String> copyAndAdd(
            Map<String, String> data, Map<String, String> other
    ) {
        Map<String, String> copy = new HashMap<>(Map.copyOf(data));
        copy.putAll(other);
        return copy;
    }

}
