package com.sd.challenge.booth.ui.poll_results;

import com.sd.challenge.booth.data.entities.Poll;
import com.sd.challenge.booth.data.repositories.PollRepository;
import com.sd.challenge.booth.resources.Screens.PollResultsForm;
import com.sd.challenge.booth.resources.exception.handler.PollExceptionHandler;
import com.sd.challenge.booth.resources.widgets.Form;
import com.sd.challenge.booth.ui.UiMvcTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class GetPollResultsTest extends UiMvcTest {

    @Autowired
    PollRepository pollRepository;

    @Test
    void getClosedPollResultsTest() throws Exception {

        Map<String, String> postData = Map.of(
                "userId", "1",
                "pollId", "3",
                "listingOf", "closed"
        );

        Poll poll = pollRepository.findById(3L).orElseThrow();
        Form pollResults = PollResultsForm.get(poll, baseUrl, postData);
        performPostRequest("/ui/poll/results", postData, status().isOk(),
                content().json(gson.toJson(pollResults)));

    }

    @Test
    void getOpenPollResultTest() throws Exception{
        Map<String, String> postData = Map.of(
                "userId", "1",
                "pollId", "2",
                "listingOf", "open"
        );

        Form errorForm = PollExceptionHandler.errorForm(baseUrl, Map.of("userId", "1"));
        performPostRequest("/ui/poll/results", postData, status().is5xxServerError(),
                content().json(gson.toJson(errorForm)));
    }

    @Test
    void getYetToOpenPollResultTest() throws Exception{
        Map<String, String> postData = Map.of(
                "userId", "1",
                "pollId", "1",
                "listingOf", "user"
        );

        Form errorForm = PollExceptionHandler.errorForm(baseUrl, Map.of("userId", "1"));
        performPostRequest("/ui/poll/results", postData, status().is5xxServerError(),
                content().json(gson.toJson(errorForm)));
    }

}
