package com.sd.challenge.booth.api;

import com.sd.challenge.booth.data.repositories.PollRepository;
import com.sd.challenge.booth.resources.Screens.AcceptedForm;
import com.sd.challenge.booth.resources.exception.handler.PollExceptionHandler;
import com.sd.challenge.booth.resources.widgets.Form;
import com.sd.challenge.booth.ui.MvcTest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.Map;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PostOpenPollTest extends MvcTest {

    @Autowired
    PollRepository pollRepository;

    public static Stream<Arguments> getOpenPollTestArguments() {
        return Stream.of(
                Arguments.of("Gateway error", "1", "1", "accept", false),
                Arguments.of("User does not own poll", "1", "4", "openPoll", false),
                Arguments.of("Poll already open", "1", "2", "openPoll", false),
                Arguments.of("Success", "1", "1", "openPoll", true)
        );
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("getOpenPollTestArguments")
    void openPollTest(String name, String userId, String pollId, String gateway, Boolean success) throws Exception {
        Map<String, String> postData = Map.of(
                "userId", userId,
                "pollId", pollId,
                gateway, "true"
        );

        Form expected = success
                ? AcceptedForm.get(baseUrl, Map.of("userId", userId, gateway, "true"))
                : PollExceptionHandler.errorForm(baseUrl, Map.of("userId", userId));

        ResultMatcher status = success ? status().isOk() : status().is5xxServerError();

        performPostRequest("/polls/open", postData, status, content().json(gson.toJson(expected)));

    }

}
