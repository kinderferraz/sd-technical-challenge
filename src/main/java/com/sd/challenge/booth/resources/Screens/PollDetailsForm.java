package com.sd.challenge.booth.resources.Screens;

import com.sd.challenge.booth.data.entities.Poll;
import com.sd.challenge.booth.resources.widgets.Element;
import com.sd.challenge.booth.resources.widgets.Form;
import com.sd.challenge.booth.services.ui.UIType;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PollDetailsForm {

    public static Form get(Poll poll, Map<String, String> data) {
        Map<String, String> backButtonData = new HashMap<>(Map.copyOf(data));
        backButtonData.remove("pollId");

        DateTimeFormatter sd = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        Element description = Element.builder()
                .tipo(UIType.TEXT)
                .texto(poll.getProposal())
                .build();

        Element endsAt = Element.builder()
                .tipo(UIType.TEXT)
                .titulo("Aberta em: ")
                .valor(poll.getCreatedAt().format(sd))
                .build();

        Element createdAt = Element.builder()
                .tipo(UIType.TEXT)
                .titulo("Finaliza em: ")
                .valor(poll.getEndsAt().format(sd))
                .build();

        Element buttonBack = Element.builder()
                .texto("Voltar")
                .url("/ui/poll/listing")
                .body(backButtonData)
                .build();

        Element buttonVote = Element.builder()
                .texto("Votar")
                .url("/ui/poll/vote")
                .body(data)
                .build();

        return Form.builder()
                .titulo(poll.getTitle())
                .itens(List.of(description, createdAt, endsAt))
                .botaoCancelar(buttonBack)
                .botaoOk(buttonVote)
                .build();
    }

}
