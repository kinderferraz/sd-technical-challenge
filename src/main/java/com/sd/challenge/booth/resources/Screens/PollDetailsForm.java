package com.sd.challenge.booth.resources.Screens;

import com.sd.challenge.booth.data.entities.Poll;
import com.sd.challenge.booth.data.entities.UserVote;
import com.sd.challenge.booth.resources.exception.PollException;
import com.sd.challenge.booth.resources.widgets.Element;
import com.sd.challenge.booth.resources.widgets.Form;
import com.sd.challenge.booth.services.ui.UIType;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PollDetailsForm {

    public static Form yetToOpenForm(Long userId, Poll poll, String baseUrl, Map<String, String> data) {
        if (!poll.getOwner().getId().equals(userId))
            throw PollException.builder()
                    .message("M=yetToOpenForm poll accessed by another user")
                    .build();

        Map<String, String> openData = new HashMap<>(data);
        openData.put("openPoll", "true");

        Element buttonOpen = Element.builder()
                .texto("Abrir para votação")
                .url(baseUrl + "/polls/open")
                .body(openData)
                .build();

        Element description = Element.builder()
                .tipo(UIType.TEXT)
                .texto(poll.getProposal())
                .build();

        Element createdAt = Element.builder()
                .tipo(UIType.TEXT)
                .titulo("Criada em: ")
                .valor(poll.getCreatedAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                .build();

        Element status = Element.builder()
                .tipo(UIType.TEXT)
                .texto("Iniciativa ainda não aberta para votação.")
                .build();

        return Form.builder()
                .titulo(poll.getTitle())
                .itens(List.of(description, createdAt, status))
                .botaoCancelar(getBackButton(baseUrl, "user", data))
                .botaoOk(buttonOpen)
                .build();
    }

    private static Element getBackButton(String baseUrl, String origin, Map<String, String> backButtonData) {
        backButtonData.remove("pollId");
        return Element.builder()
                .texto("Voltar")
                .url(baseUrl + "/ui/poll/listing/" + origin)
                .body(backButtonData)
                .build();
    }

    public static Form getClosedPollDetails(Poll poll, String baseUrl, Map<String, String> data) {
        Element seeResultsButton = Element.builder()
                .texto("Ver Resultados")
                .url(baseUrl + "/polls/results")
                .body(data)
                .build();

        Element description = Element.builder()
                .tipo(UIType.TEXT)
                .texto(poll.getProposal())
                .build();

        Element createdAt = Element.builder()
                .tipo(UIType.TEXT)
                .titulo("Criada em: ")
                .valor(poll.getCreatedAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                .build();

        Element status = Element.builder()
                .tipo(UIType.TEXT)
                .texto("Iniciativa fechada. Confira os resultados.")
                .build();

        return Form.builder()
                .titulo(poll.getTitle())
                .botaoOk(seeResultsButton)
                .itens(List.of(description, createdAt, status))
                .botaoCancelar(getBackButton(baseUrl, "closed", data))
                .build();
    }

    public static Form getOpenPollDetails(
            Long userId, Poll poll, String baseUrl, Map<String, String> data, UserVote vote,
            Boolean userMayVote
    ) {
        Element voteButton = userMayVote && !poll.getOwner().getId().equals(userId)
                ? Element.builder().texto("Votar").url(baseUrl + "/ui/poll/vote").body(data).build()
                : null;

        Element description = Element.builder()
                .tipo(UIType.TEXT)
                .texto(poll.getProposal())
                .build();

        Element createdAt = Element.builder()
                .tipo(UIType.TEXT)
                .titulo("Criada em: ")
                .valor(poll.getCreatedAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                .build();

        String complement = vote != null
                ? "Você já votou e fez sua parte"
                : !userMayVote ? "Infelizmente você não pode votar."
                : poll.getOwner().getId().equals(userId) ? "Essa iniciativa é sua, você não pode votar." : "Vote.";

        Element status = Element.builder()
                .tipo(UIType.TEXT)
                .texto("Iniciativa aberta. " + complement)
                .build();

        return Form.builder()
                .titulo(poll.getTitle())
                .botaoOk(voteButton)
                .itens(List.of(description, createdAt, status))
                .botaoCancelar(getBackButton(baseUrl, "open", data))
                .build();
    }

}
