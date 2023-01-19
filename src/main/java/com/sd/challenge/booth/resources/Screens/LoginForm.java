package com.sd.challenge.booth.resources.Screens;

import com.sd.challenge.booth.resources.widgets.Element;
import com.sd.challenge.booth.resources.widgets.Form;
import com.sd.challenge.booth.services.ui.UIType;

import java.util.List;

public class LoginForm {

    public static Form get(String baseUrl) {
        Element cpfInput = Element.builder()
                .tipo(UIType.TEXT_INPUT)
                .titulo("CPF")
                .id("idt_cpf_input")
                .build();

        Element loginButton = Element.builder()
                .texto("Login")
                .url(baseUrl + "/ui/poll-gateway")
                .build();

        return Form.builder()
                .titulo("Bem vindo")
                .itens(List.of(cpfInput))
                .botaoOk(loginButton)
                .build();
    }

}
