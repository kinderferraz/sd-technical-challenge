package com.sd.challenge.booth.services.ui;

import com.sd.challenge.booth.data.entities.User;
import com.sd.challenge.booth.data.repositories.PollRepository;
import com.sd.challenge.booth.data.repositories.UserRepository;
import com.sd.challenge.booth.mapper.UiMapper;
import com.sd.challenge.booth.resources.widgets.Element;
import com.sd.challenge.booth.resources.widgets.Screen;
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
public class ScreenService {

    @Value("${application.properties.base-url}")
    String baseUrl;

    PollRepository pollRepository;

    UserRepository userRepository;

    UiMapper uiMapper;

    @Autowired
    public ScreenService(
            UserRepository userRepository,
            PollRepository pollRepository,
            UiMapper uiMapper
    ) {
        this.uiMapper = uiMapper;
        this.userRepository = userRepository;
        this.pollRepository = pollRepository;
    }

    public Screen makeNewPollForm(Map<String, String> data) {
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

        return Screen.builder()
                .titulo("Nova votação")
                .tipo(UIType.FORM)
                .itens(elements)
                .botaoCancelar(cancelButton)
                .botaoOk(acceptButton)
                .build();
    }

    public Screen makeLoginScreen() {
        Element cpfInput = Element.builder()
                .tipo(UIType.TEXT_INPUT)
                .titulo("CPF")
                .id("idt_cpf_input")
                .build();

        Element loginButton = Element.builder()
                .texto("Login")
                .url(baseUrl + "/ui/poll-gateway")
                .build();

        return Screen.builder()
                .titulo("Bem vindo")
                .itens(List.of(cpfInput))
                .botaoOk(loginButton)
                .tipo(UIType.FORM)
                .build();
    }

    public Screen makeGateway(Map<String, String> data) {
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

        return Screen.builder()
                .titulo("Iniciativas")
                .tipo(UIType.SELECTION)
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

    public Screen makeUserPollListing(Map<String, String> data) {
        Long userId = Long.parseLong(data.get("userId"));
        User user = userRepository.findUserByIdWithPolls(userId);

        List<Element> polls = user.getUserPolls().stream().parallel()
                .map(p -> uiMapper.mapPollToElement(p, data))
                .collect(Collectors.toList());

        return Screen.builder()
                .tipo(UIType.SELECTION)
                .titulo("Suas iniciativas")
                .itens(polls)
                .build();
    }

    public Screen makeOpenPollListing(Map<String, String> data) {
        List<Element> polls = pollRepository.findAllByEndsAtAfter(LocalDateTime.now())
                .stream().parallel()
                .map(p -> uiMapper.mapPollToElement(p, data))
                .collect(Collectors.toList());

        return Screen.builder()
                .tipo(UIType.SELECTION)
                .titulo("Iniciativas em aberto")
                .itens(polls)
                .build();
    }

    public Screen getPollDetails(Map<String, String> data) {
        return Screen.builder().build();
    }

    public Screen getPollResults(Map<String, String> data) {
        return null;
    }

}
