package com.sd.challenge.booth.services.ui;

import com.sd.challenge.booth.resources.widgets.Element;
import com.sd.challenge.booth.resources.widgets.Screen;
import com.sd.challenge.booth.resources.widgets.Button;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScreenService {

    @Value("${application.properties.base-url}")
    String baseUrl;

    public Screen makeNewPollForm() {
        Element instructionsText = Element.builder()
                .id("idt_instructions")
                .tipo(UIType.TEXTO)
                .texto("Descreva aqui o objetivo de sua iniciativa e a data de encerramento da votação." +
                        "Quando finalizar, pressione o botão enviar para abrir a votação")
                .build();

        Element titleInput = Element.builder()
                .tipo(UIType.INPUT_TEXTO)
                .id("idt_title")
                .titulo("Nome da iniciativa")
                .build();

        Element descriptionInput = Element.builder()
                .tipo(UIType.INPUT_TEXTO)
                .id("idt_description")
                .titulo("Descrição da iniciativa")
                .build();

        Element closingDateInput = Element.builder()
                .tipo(UIType.INPUT_DATA)
                .id("idt_closing_date")
                .titulo("Data de encerramento da votação")
                .build();

        List<Element> elements = List.of(
                instructionsText, titleInput,
                closingDateInput, descriptionInput
        );

        Button acceptButton = Button.builder()
                .texto("Enviar iniciativa")
                .url(baseUrl + "/")
                .build();
        Button cancelButton = Button.builder()
                .texto("Cancelar")
                .build();

        return Screen.builder()
                .titulo("Nova votação")
                .tipo("FORMULARIO")
                .itens(elements)
                .botaoCancelar(cancelButton)
                .botaoOk(acceptButton)
                .build();
    }

}
