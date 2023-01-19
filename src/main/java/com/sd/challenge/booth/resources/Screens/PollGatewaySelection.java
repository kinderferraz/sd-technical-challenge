package com.sd.challenge.booth.resources.Screens;

import com.sd.challenge.booth.resources.widgets.Element;
import com.sd.challenge.booth.resources.widgets.Selection;

import java.util.List;
import java.util.Map;

import static com.sd.challenge.booth.mapper.UiMapper.copyAndAdd;

public class PollGatewaySelection {

    public static Selection get(String baseUrl, Map<String, String> data) {
        Element userPolls = Element.builder()
                .id("idt_user_polls")
                .texto("Suas iniciativas")
                .url(baseUrl + "/ui/polls/listing")
                .body(copyAndAdd(data, Map.of("listingOf", "userPolls")))
                .build();

        Element openPolls = Element.builder()
                .texto("Iniciativas em aberto")
                .url(baseUrl + "/ui/polls/listing")
                .id("idt_open_polls")
                .body(copyAndAdd(data, Map.of("listingOf", "openPolls")))
                .build();

        Element closedPolls = Element.builder()
                .texto("Iniciativas finalizadas")
                .url(baseUrl + "/ui/polls/listing")
                .id("idt_open_polls")
                .body(copyAndAdd(data, Map.of("listingOf", "closedPolls")))
                .build();

        Element newPoll = Element.builder()
                .texto("Propor iniciativa")
                .id("idt_new_poll")
                .body(data)
                .url(baseUrl + "/ui/new-poll-form")
                .build();

        return Selection.builder()
                .titulo("Iniciativas")
                .itens(List.of(userPolls, openPolls, closedPolls, newPoll))
                .build();
    }

}
