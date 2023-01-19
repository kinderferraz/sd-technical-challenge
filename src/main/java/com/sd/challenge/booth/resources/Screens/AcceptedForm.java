package com.sd.challenge.booth.resources.Screens;

import com.sd.challenge.booth.resources.widgets.Element;
import com.sd.challenge.booth.resources.widgets.Form;

import java.util.Map;

public class AcceptedForm {

    public static Form get(String baseUrl, Map<String, String> data) {

        String voteTitle = data.get("accept") != null ? "Obrigado pelo seu voto" : null;
        String newPollTitle = data.get("newPoll") != null ? "Iniciativa recebida" : null;

        Map<String, String> replacementData = Map.of("userId", data.get("userId"));

        Element buttonOk = Element.builder()
                .texto("Voltar ao início")
                .url(baseUrl + "/ui/poll-gateway")
                .body(replacementData)
                .build();

        return Form.builder()
                .titulo(voteTitle == null ? newPollTitle : voteTitle)
                .botaoOk(buttonOk)
                .build();

    }

}
