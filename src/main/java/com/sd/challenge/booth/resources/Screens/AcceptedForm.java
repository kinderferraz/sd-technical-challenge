package com.sd.challenge.booth.resources.Screens;

import com.sd.challenge.booth.resources.exception.PollException;
import com.sd.challenge.booth.resources.widgets.Element;
import com.sd.challenge.booth.resources.widgets.Form;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AcceptedForm {

    public static Form get(String baseUrl, Map<String, String> data) {
        String voteTitle = data.get("accept") != null ? "Obrigado pelo seu voto" : null;
        String newPollTitle = data.get("newPoll") != null ? "Iniciativa recebida" : null;
        String openPollTitle = data.get("pollOpen") != null ? "Votação aberta" : null;

        String userId = data.get("userId");
        String finalTitle = chooseTitle(voteTitle, newPollTitle, openPollTitle, userId);

        Map<String, String> replacementData = Map.of("userId", userId);

        Element buttonOk = Element.builder()
                .texto("Voltar ao início")
                .url(baseUrl + "/ui/poll-gateway")
                .body(replacementData)
                .build();

        return Form.builder()
                .titulo(finalTitle)
                .botaoOk(buttonOk)
                .build();

    }

    private static String chooseTitle(
            String voteTitle, String newPollTitle,
            String openPollTitle, String userId
    ) {
        List<String> list = Stream.of(voteTitle, newPollTitle, openPollTitle)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        if (list.size() != 1)
            throw PollException.builder()
                    .message("too few or too many options passed to accept form")
                    .data(Map.of("userId", userId))
                    .build();

        return list.get(0);

    }

}
