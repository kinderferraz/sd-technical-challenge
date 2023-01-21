package com.sd.challenge.booth.api;

import com.sd.challenge.booth.data.entities.UserVote;
import com.sd.challenge.booth.data.repositories.PollRepository;
import com.sd.challenge.booth.data.repositories.UserVoteRepository;
import com.sd.challenge.booth.resources.Screens.AcceptedForm;
import com.sd.challenge.booth.resources.exception.handler.PollExceptionHandler;
import com.sd.challenge.booth.resources.widgets.Form;
import com.sd.challenge.booth.ui.MvcTest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PostNewVoteTest extends MvcTest {

    @Autowired
    PollRepository pollRepository;

    @Autowired
    UserVoteRepository userVoteRepository;

    public static Stream<Arguments> getPostNewVoteTestArguments() {
        return Stream.of(
                Arguments.of("Success Vote Test", "1", "5", "open", "accept", true),
                Arguments.of("Already Voted Test", "1", "4", "open", "accept", false),
                Arguments.of("Vote on Own Poll Test", "1", "2", "own", "accept", false),
                Arguments.of("Gateway Error Test", "1", "2", "own", "newPoll", false)

        );
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("getPostNewVoteTestArguments")
    void postNewVoteTest(
            String name, String userId, String pollId, String listing, String gateway, Boolean success
    ) throws Exception {
        Map<String, String> postData = Map.of(
                "userId", userId,
                "pollId", pollId,
                gateway, "true",
                "listingOf", listing
        );

        Map<String, String> responseData = Map.of(
                "userId", userId,
                "accept", "true"
        );

        Form acceptedForm = success
                ? AcceptedForm.get(baseUrl, responseData)
                : PollExceptionHandler.errorForm(baseUrl, Map.of("userId", userId));

        ResultMatcher status = success ? status().isAccepted() : status().is5xxServerError();

        performPostRequest("/polls/vote", postData, status,
                content().json(gson.toJson(acceptedForm)));

        Set<UserVote> votes = new HashSet<>();
        userVoteRepository.findAll().forEach(votes::add);

        assertEquals(10, votes.size());
    }


}
