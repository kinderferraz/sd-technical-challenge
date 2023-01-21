package com.sd.challenge.booth.api;


import com.sd.challenge.booth.data.entities.Poll;
import com.sd.challenge.booth.data.repositories.PollRepository;
import com.sd.challenge.booth.resources.Screens.AcceptedForm;
import com.sd.challenge.booth.resources.exception.handler.PollExceptionHandler;
import com.sd.challenge.booth.resources.widgets.Form;
import com.sd.challenge.booth.ui.MvcTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PostNewPollTest extends MvcTest {

    @Autowired
    PollRepository pollRepository;

    @Test
    void postNewPollSuccessTest() throws Exception {
        Map<String, String> postData = Map.of(
                "userId", "1",
                "idtTitle", "Sexta proposta",
                "idtDescription", "Tomar refrigerante",
                "newPoll", "true"
        );

        Form acceptedForm = AcceptedForm.get(baseUrl, postData);

        performPostRequest("/polls", postData, status().isCreated(),
                content().json(gson.toJson(acceptedForm)));

        Optional<Poll> poll = pollRepository.findById(6L);
        assertTrue(poll.isPresent());
    }

    @Test
    void postNewPollFailTest() throws Exception {
        Map<String, String> postData = Map.of(
                "userId", "1",
                "idtTitle", "Sexta proposta",
                "idtDescription", "Tomar refrigerante",
                "accept", "true"
        );

        Form acceptedForm = PollExceptionHandler.errorForm(baseUrl, Map.of("userId", "1"));

        performPostRequest("/polls", postData, status().is5xxServerError(),
                content().json(gson.toJson(acceptedForm)));
    }

}
