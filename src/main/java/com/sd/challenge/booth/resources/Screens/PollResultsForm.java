package com.sd.challenge.booth.resources.Screens;

import com.sd.challenge.booth.data.entities.Poll;
import com.sd.challenge.booth.resources.exception.PollException;
import com.sd.challenge.booth.resources.widgets.Element;
import com.sd.challenge.booth.resources.widgets.Form;
import com.sd.challenge.booth.services.ui.UIType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class PollResultsForm {

    public static Form get(Poll poll, String baseUrl, Map<String, String> data) {
        if (poll.getEndsAt() == null || poll.getEndsAt().isAfter(LocalDateTime.now()))
            throw PollException.builder()
                    .message("M=get poll is not closed pollId="+poll.getId())
                    .data(data)
                    .build();

        int noVotes = poll.getTotalVotes() - poll.getAcceptedVotes();

        Element backButton = Element.builder()
                .texto("Voltar")
                .url(baseUrl + "/ui/polls/listing")
                .body(Map.of("userId", data.get("userId")))
                .build();

        Element totalVotesText = Element.builder()
                .tipo(UIType.TEXT)
                .texto("Total de votos: " + poll.getTotalVotes())
                .build();

        Element yesVotesText = Element.builder()
                .tipo(UIType.TEXT)
                .texto("Votos a favor: " + poll.getAcceptedVotes())
                .build();

        Element noVotesText = Element.builder()
                .tipo(UIType.TEXT)
                .texto("Votos contra: " + noVotes)
                .build();

        return Form.builder()
                .titulo("Resultados de \"" + poll.getTitle() + "\" ")
                .botaoOk(backButton)
                .itens(List.of(totalVotesText, yesVotesText, noVotesText))
                .build();
    }
}
