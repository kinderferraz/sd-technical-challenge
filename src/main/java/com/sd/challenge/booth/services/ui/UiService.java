package com.sd.challenge.booth.services.ui;

import com.sd.challenge.booth.data.entities.User;
import com.sd.challenge.booth.data.repositories.PollRepository;
import com.sd.challenge.booth.data.repositories.UserRepository;
import com.sd.challenge.booth.mapper.UiMapper;
import com.sd.challenge.booth.resources.widgets.Selection;
import com.sd.challenge.booth.resources.widgets.Element;
import com.sd.challenge.booth.resources.widgets.Form;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UiService {

    @Value("${application.properties.base-url}")
    String baseUrl;

    PollRepository pollRepository;

    UserRepository userRepository;

    UiMapper uiMapper;

    @Autowired
    public UiService(
            UserRepository userRepository,
            PollRepository pollRepository,
            UiMapper uiMapper
    ) {
        this.uiMapper = uiMapper;
        this.userRepository = userRepository;
        this.pollRepository = pollRepository;
    }

    public Form makeNewPollForm(Map<String, String> data) {
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
                .titulo("Nova votação")
                .itens(elements)
                .botaoCancelar(cancelButton)
                .botaoOk(acceptButton)
                .build();
    }

    public Form makeLoginScreen() {
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

    public Selection makeGateway(Map<String, String> data) {
        User u = userRepository.findUserByCpf(data.get("idtCpfInput"));
        log.info("M=makeGateway user={}", u.getId());

        data.put("idt_user", u.getId().toString());
        data.remove("idtCpfInput");

        Element userPolls = Element.builder()
                .id("idt_user_polls")
                .texto("Suas iniciativas")
                .url("/ui/polls/listing")
                .body(copyAndAdd(data, Map.of("listingOf", "userPolls")))
                .build();
        Element openPolls = Element.builder()
                .texto("Iniciativas em aberto")
                .url("/ui/polls/listing")
                .id("idt_open_polls")
                .body(copyAndAdd(data, Map.of("listingOf", "openPolls")))
                .build();
        Element newPoll = Element.builder()
                .texto("Propor iniciativa")
                .id("idt_new_poll")
                .body(data)
                .url("/ui/new-poll-form")
                .build();

        return Selection.builder()
                .titulo("Iniciativas")
                .itens(List.of(userPolls, openPolls, newPoll))
                .build();
    }

    private Map<String, String> copyAndAdd(
            Map<String, String> data, Map<String, String> other
    ) {
        Map<String, String> copy = new HashMap<>(Map.copyOf(data));
        copy.putAll(other);
        return copy;
    }

    public Selection makeUserPollListing(Map<String, String> data) {
        Long userId = Long.parseLong(data.get("userId"));
        User user = userRepository.findUserByIdWithPolls(userId);

        List<Element> polls = user.getUserPolls().stream().parallel()
                .map(p -> uiMapper.mapPollToElement(p, data))
                .collect(Collectors.toList());

        return Selection.builder()
                .titulo("Suas iniciativas")
                .itens(polls)
                .build();
    }

    public Selection makeOpenPollListing(Map<String, String> data) {
        List<Element> polls = pollRepository.findAllByEndsAtAfter(LocalDateTime.now())
                .stream().parallel()
                .map(p -> uiMapper.mapPollToElement(p, data))
                .collect(Collectors.toList());

        return Selection.builder()
                .titulo("Iniciativas em aberto")
                .itens(polls)
                .build();
    }

    public Form getPollDetails(Map<String, String> data) {
        return Form.builder().build();
    }

    public Form getPollResults(Map<String, String> data) {
        return null;
    }

}
