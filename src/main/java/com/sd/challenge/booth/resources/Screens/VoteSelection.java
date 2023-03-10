package com.sd.challenge.booth.resources.Screens;

import com.sd.challenge.booth.data.entities.Poll;
import com.sd.challenge.booth.resources.widgets.Element;
import com.sd.challenge.booth.resources.widgets.Selection;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VoteSelection {

    public static Selection get(String baseUrl, Map<String, String> data, Poll poll) {

        Map<String, String> acceptData = new HashMap<>(Map.copyOf(data));
        acceptData.put("accept", "true");
        Element acceptVote = Element.builder()
                .texto("Sim")
                .body(acceptData)
                .url(baseUrl + "/poll/vote")
                .build();

        Map<String, String> denyData = new HashMap<>(Map.copyOf(data));
        denyData.put("accept", "false");
        Element denyVote = Element.builder()
                .texto("Não")
                .body(denyData)
                .url(baseUrl + "/poll/vote")
                .build();

        return Selection.builder()
                .titulo("Vote iniciativa \"" + poll.getTitle() + "\"")
                .itens(List.of(acceptVote, denyVote))
                .build();
    }

}
