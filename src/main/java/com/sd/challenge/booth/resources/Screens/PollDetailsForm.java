package com.sd.challenge.booth.resources.Screens;

import com.sd.challenge.booth.data.entities.Poll;
import com.sd.challenge.booth.data.entities.UserVote;
import com.sd.challenge.booth.resources.widgets.Element;
import com.sd.challenge.booth.resources.widgets.Form;
import com.sd.challenge.booth.services.ui.UIType;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PollDetailsForm {

    public static Form get(
            Poll poll, UserVote vote, Map<String, String> data, Boolean cpfValidation,
            String baseUrl
    ) {
        String status = votingStatus(poll);

        Map<String, String> backButtonData = new HashMap<>(Map.copyOf(data));
        backButtonData.remove("pollId");

        Element description = Element.builder()
                .tipo(UIType.TEXT)
                .texto(poll.getProposal())
                .build();

        Element createdAt = Element.builder()
                .tipo(UIType.TEXT)
                .titulo("Aberta em: ")
                .valor(poll.getCreatedAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                .build();

        Element openStatus = getStatusElement(poll, status);

        Element voteStatus = Element.builder()
                .tipo(UIType.TEXT)
                .texto(voteStatus(vote, cpfValidation))
                .build();

        Element buttonBack = Element.builder()
                .texto("Voltar")
                .url(baseUrl + "/ui/poll/listing")
                .body(backButtonData)
                .build();

        return Form.builder()
                .titulo(poll.getTitle())
                .itens(List.of(voteStatus, description, createdAt, openStatus))
                .botaoCancelar(buttonBack)
                .botaoOk(chooseButton(poll, data, baseUrl, status))
                .build();
    }

    private static Element getStatusElement(Poll poll, String status) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String title;

        if (status.equalsIgnoreCase("open"))
            title = "Iniciativa aberta para votação  até " + poll.getEndsAt().format(dtf);
        else if (status.equalsIgnoreCase("notOpen"))
            title = "Iniciativa ainda não aberta para votação";
        else
            title = "Iniciativa fechada. Confira os resultados.";

        return Element.builder()
                .tipo(UIType.TEXT)
                .texto(title)
                .build();
    }

    private static Element chooseButton(
            Poll poll, Map<String, String> data, String baseUrl, String status
    ) {
        Element voteButton = Element.builder()
                .texto("Votar")
                .url(baseUrl + "/ui/poll/vote")
                .body(data)
                .build();

        Element openButton = Element.builder()
                .texto("Abrir para votação")
                .url(baseUrl + "/polls/open")
                .body(data)
                .build();

        long userId = Long.parseLong(data.get("userId"));

        if (status.equalsIgnoreCase("notOpen") && poll.getOwner().getId().equals(userId))
            return openButton;

        if (status.equalsIgnoreCase("open"))
            return voteButton;

        return null;
    }

    private static String voteStatus(UserVote vote, Boolean cpfValidation) {
        if (cpfValidation.equals(Boolean.FALSE) && vote == null)
            return "Você não pode votar nesta iniciativa";

        if(cpfValidation.equals(Boolean.TRUE) && vote == null)
            return "Você ainda não votou nesta iniciativa";

        return "Você já votou nesta iniciativa";
    }

    private static boolean userMayVote(Poll poll, UserVote vote, Boolean cpfValidation) {
        return poll.getOpenedAt() != null
                && poll.getEndsAt().isAfter(LocalDateTime.now())
                && vote == null
                && poll.getEndsAt().isAfter(LocalDateTime.now())
                && cpfValidation.equals(Boolean.TRUE);
    }

    private static String votingStatus(Poll poll) {
        if (poll.getOpenedAt()==null) return "notOpen";
        if (poll.getCreatedAt().isBefore(LocalDateTime.now()))
            return "closed";
        return "open";
    }


}
