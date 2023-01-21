package com.sd.challenge.booth.ui.poll_vote;

import com.google.gson.Gson;
import com.sd.challenge.booth.data.entities.Poll;
import com.sd.challenge.booth.data.repositories.PollRepository;
import com.sd.challenge.booth.resources.Screens.VoteSelection;
import com.sd.challenge.booth.resources.exception.handler.PollExceptionHandler;
import com.sd.challenge.booth.resources.widgets.Form;
import com.sd.challenge.booth.resources.widgets.Selection;
import com.sd.challenge.booth.ui.MvcTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class GetVoteTest extends MvcTest {

    @Autowired
    PollRepository pollRepository;

    public static Stream<Arguments> getErrorVoteFormArguments() {
        return Stream.of(
                Arguments.of("Vote on poll not yet open", "1"),
                Arguments.of("Vote on closed poll", "3"),
                Arguments.of("Vote on own poll", "2")
        );
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("getErrorVoteFormArguments")
    void getVoteFormWithErrorTest(String name, String pollId) throws Exception {
        Gson gson = new Gson();

        Map<String, String> postData = new HashMap<>(Map.of(
                "userId", "1",
                "listingOf","open",
                "pollId", pollId));

        Map<String, String> errorData = Map.of("userId", "1");
        Form errorForm = PollExceptionHandler.errorForm(baseUrl, errorData);

        performPostRequest("/ui/poll/vote", postData, status().is5xxServerError(),
                content().json(gson.toJson(errorForm)));

   }

    @Test
    void getVoteOpenPollTest() throws Exception {
        Gson gson = new Gson();

        Poll poll = pollRepository.findById(2L).orElseThrow();

        Map<String, String> postData = new HashMap<>(Map.of(
                "userId", "2",
                "listingOf","open",
                "pollId", "2"));

        Map<String, String> responseData = new HashMap<>(Map.of("userId", "2"));

        Selection votingSelection = VoteSelection.get(baseUrl, responseData, poll);

        performPostRequest("/ui/poll/vote", postData, status().isOk(),
                content().json(gson.toJson(votingSelection)));
    }

}
