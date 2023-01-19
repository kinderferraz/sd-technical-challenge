package com.sd.challenge.booth.resources.Screens;

import com.sd.challenge.booth.resources.widgets.Element;
import com.sd.challenge.booth.resources.widgets.Form;
import com.sd.challenge.booth.services.ui.UIType;

import java.util.List;
import java.util.Map;

public class NewPollForm {

    public static Form get(String baseUrl, Map<String, String> data) {
        Element instructionsText = Element.builder()
                .id("idt_instructions")
                .tipo(UIType.TEXT)
                .texto("Descreva aqui o objetivo de sua iniciativa e a data de encerramento da votação." +
                        "Quando finalizar, pressione o botão enviar para abrir a votação")
                .build();

        Element titleInput = Element.builder()
                .tipo(UIType.TEXT_INPUT)
                .id("idt_title")
                .titulo("Nome da iniciativa")
                .build();

        Element descriptionInput = Element.builder()
                .tipo(UIType.TEXT_INPUT)
                .id("idt_description")
                .titulo("Descrição da iniciativa")
                .build();

        Element closingDateInput = Element.builder()
                .tipo(UIType.DATE_INPUT)
                .id("idt_closing_date")
                .titulo("Data de encerramento da votação")
                .build();

        List<Element> elements = List.of(
                instructionsText, titleInput,
                closingDateInput, descriptionInput
        );

        Element acceptButton = Element.builder()
                .texto("Enviar iniciativa")
                .url(baseUrl + "/")
                .body(data)
                .build();
        Element cancelButton = Element.builder()
                .texto("Cancelar")
                .url(baseUrl + "/ui/poll-gateway")
                .body(data)
                .build();

        return Form.builder()
                .titulo("Propor Iniciativa")
                .itens(elements)
                .botaoCancelar(cancelButton)
                .botaoOk(acceptButton)
                .build();

    }
}
