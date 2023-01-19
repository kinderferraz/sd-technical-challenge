package com.sd.challenge.booth.resources.Screens;

import com.sd.challenge.booth.data.entities.Poll;
import com.sd.challenge.booth.resources.exception.PollException;
import com.sd.challenge.booth.resources.widgets.Element;
import com.sd.challenge.booth.resources.widgets.Form;
import com.sd.challenge.booth.services.ui.UIType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class PollResultsForm {

    public static Form get(Poll poll, String baseUrl, Map<String, String> data) {
        if (poll.getEndsAt()==null || poll.getEndsAt().isAfter(LocalDateTime.now()))
            throw PollException.builder()
                    .message("M=get poll is not closed pollId="+poll.getId())
                    .data(data)
                    .build();

        AtomicReference<Integer> totalVotes = new AtomicReference<>(0);
        AtomicReference<Integer> yesVotes = new AtomicReference<>(0);

        poll.getPollVotes().forEach(uv -> {
            totalVotes.updateAndGet(v -> v + 1);
            yesVotes.updateAndGet(v -> v + 1);
        });

        int noVotes = totalVotes.get() - yesVotes.get();

        Element backButton = Element.builder()
                .texto("Voltar")
                .url(baseUrl + "/ui/polls/listing")
                .body(data)
                .build();

        Element totalVotesText = Element.builder()
                .tipo(UIType.TEXT)
                .texto("Total de votos: " + totalVotes.get())
                .build();

        Element yesVotesText = Element.builder()
                .tipo(UIType.TEXT)
                .texto("Votos a favor: " + yesVotes.get())
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
