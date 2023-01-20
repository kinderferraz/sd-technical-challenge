package com.sd.challenge.booth.resources.exception.handler;

import com.sd.challenge.booth.resources.exception.PollException;
import com.sd.challenge.booth.resources.widgets.Element;
import com.sd.challenge.booth.resources.widgets.Form;
import com.sd.challenge.booth.services.ui.UIType;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class PollExceptionHandler {

    @Value("${application.properties.base-url}")
    String baseUrl;

    @ExceptionHandler(value = {PollException.class, HibernateException.class})
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public Form handlePollException(PollException ex) {

        log.warn("M=handlePollException ex={}", ex.getMessage());

        Map<String, String> data = new HashMap<>();
        data.put("userId", ex.getData().get("userId"));
        return errorForm(baseUrl, data);
    }

    public static Form errorForm(String baseUrl, Map<String, String> data) {
        Element message = Element.builder()
                .tipo(UIType.TEXT)
                .texto("Pedimos desculpa, alguma coisa saiu errado")
                .build();

        Element buttonBack = Element.builder()
                .texto("Voltar ao inicio")
                .url(baseUrl + "/ui/poll-gateway")
                .body(data)
                .build();

        return Form.builder()
                .titulo("Eita! Algo saiu errado")
                .itens(List.of(message))
                .botaoOk(buttonBack)
                .build();
    }

}
