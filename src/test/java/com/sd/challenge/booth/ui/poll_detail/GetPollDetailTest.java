package com.sd.challenge.booth.ui.poll_detail;

import com.sd.challenge.booth.data.entities.Poll;
import com.sd.challenge.booth.data.entities.UserVote;
import com.sd.challenge.booth.data.repositories.PollRepository;
import com.sd.challenge.booth.data.repositories.UserVoteRepository;
import com.sd.challenge.booth.integration.CpfClient;
import com.sd.challenge.booth.integration.CpfServiceResponse;
import com.sd.challenge.booth.resources.Screens.PollDetailsForm;
import com.sd.challenge.booth.resources.exception.handler.PollExceptionHandler;
import com.sd.challenge.booth.resources.widgets.Form;
import com.sd.challenge.booth.ui.MvcTest;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class GetPollDetailTest extends MvcTest {

    @Autowired
    PollRepository pollRepository;

    @Autowired
    UserVoteRepository userVoteRepository;

    @MockBean
    CpfClient cpfClient;

    final String endpoint = "/ui/poll/details";

    public static Stream<Arguments> getOpenPollDetailsTest() {
        return Stream.of(
                Arguments.of("User may vote on open poll", "1", "5", true),
                Arguments.of("User may not vote on open poll", "7", "5", false),
                Arguments.of("User already voted on open poll", "1", "4", true),
                Arguments.of("User owns on open poll", "1", "2", true),
                Arguments.of("User with no cpf", "11", "2", false)
        );
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("getOpenPollDetailsTest")
    void getOpenPollDetailsTest(String name, String userId, String pollId, Boolean userMayVote) throws Exception {
        Map<String, String> postData = new HashMap<>(Map.of(
                "userId", userId,
                "pollId", pollId,
                "listingOf", "open"
        ));

        Map<String, String> formData = new HashMap<>(Map.of(
                "userId", userId,
                "pollId", pollId,
                "listingOf", "open"
        ));

        CpfServiceResponse response = new CpfServiceResponse();
        if (userMayVote)
            response.setStatus("ABLE_TO_VOTE");
        else
            response.setStatus("UNABLE_TO_VOTE");

        doReturn(response).when(cpfClient).getUserMayVote(anyString());

        Poll poll = pollRepository.findById(Long.valueOf(pollId)).orElseThrow();
        UserVote vote = userVoteRepository.findUserVoteByPollAndVoter(Long.valueOf(pollId), Long.valueOf(userId));

        Form expectedForm = PollDetailsForm.getOpenPollDetails(Long.valueOf(userId), poll,
                baseUrl, formData, vote, userMayVote);

        performPostRequest(endpoint, postData, status().isOk(), content().json(gson.toJson(expectedForm)));
    }

    public static Stream<Arguments> getClosedPollDetailsTest() {
        return Stream.of(
                Arguments.of("View closed poll", "1", "3", true),
                Arguments.of("View open poll", "1", "2", false),
                Arguments.of("View not yet open poll", "1", "1", false),
                Arguments.of("User view poll that does not exist", "1", "10", false)
        );
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("getClosedPollDetailsTest")
    void getClosedPollDetailsTest(String name, String userId, String pollId, Boolean success) throws Exception {
        Map<String, String> postData = new HashMap<>(Map.of(
                "userId", userId,
                "pollId", pollId,
                "listingOf", "closed"
        ));

        Map<String, String> responseData = new HashMap<>(Map.copyOf(postData));

        ResultMatcher status = success ? status().isOk() : status().is5xxServerError();

        Poll poll = pollRepository.findById(Long.parseLong(pollId)).orElse(null);

        Form result = (success && poll != null)
                ? PollDetailsForm.getClosedPollDetails(poll, baseUrl, responseData)
                : PollExceptionHandler.errorForm(baseUrl, Map.of("userId", userId));

        performPostRequest(endpoint, postData, status, content().json(gson.toJson(result)));
    }

    public static Stream<Arguments> getYetToOpenPollDetailsTestArguments() {
        return Stream.of(
                Arguments.of("User views yet to open poll", "1", "1", true),
                Arguments.of("User views already open poll", "1", "2", false),
                Arguments.of("User views another user's poll", "1", "4", false)
        );
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("getYetToOpenPollDetailsTestArguments")
    void getYetToOpenPollDetailsTest(String name, String userId, String pollId, Boolean success) throws Exception {
        Map<String, String> postData = new HashMap<>(Map.of(
                "userId", userId,
                "pollId", pollId,
                "listingOf", "user"
        ));

        Map<String, String> responseData = new HashMap<>(Map.copyOf(postData));

        ResultMatcher status = success ? status().isOk() : status().is5xxServerError();

        Poll poll = pollRepository.findById(Long.parseLong(pollId)).orElseThrow();

        Form result = success
                ? PollDetailsForm.yetToOpenForm(Long.parseLong(userId), poll, baseUrl, responseData)
                : PollExceptionHandler.errorForm(baseUrl, Map.of("userId", userId));

        performPostRequest(endpoint, postData, status, content().json(gson.toJson(result)));
    }

}
