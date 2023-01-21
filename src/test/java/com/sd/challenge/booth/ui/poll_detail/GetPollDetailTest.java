package com.sd.challenge.booth.ui.poll_detail;

import com.sd.challenge.booth.data.entities.Poll;
import com.sd.challenge.booth.data.entities.UserVote;
import com.sd.challenge.booth.data.repositories.PollRepository;
import com.sd.challenge.booth.data.repositories.UserVoteRepository;
import com.sd.challenge.booth.integration.CpfClient;
import com.sd.challenge.booth.integration.CpfServiceResponse;
import com.sd.challenge.booth.resources.Screens.PollDetailsForm;
import com.sd.challenge.booth.resources.widgets.Form;
import com.sd.challenge.booth.ui.MvcTest;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
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

    public static Stream<Arguments> getOpenPollDetailsTest() {
        return Stream.of(
                Arguments.of("User may vote on open poll", "1", "5", true),
                Arguments.of("User may not vote on open poll", "7", "5", false),
                Arguments.of("User already voted on open poll", "1", "4", true),
                Arguments.of("User owns on open poll", "1", "2", true)
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

        doReturn(Optional.of(response)).when(cpfClient).getUserMayVote(anyString());

        Poll poll = pollRepository.findById(Long.valueOf(pollId)).orElseThrow();
        UserVote vote = userVoteRepository.findUserVoteByPollAndVoter(Long.valueOf(pollId), Long.valueOf(userId));

        Form expectedForm = PollDetailsForm.getOpenPollDetails(Long.valueOf(userId), poll,
                baseUrl, formData, vote, userMayVote);

        performPostRequest("/ui/poll/details", postData, status().isOk(), content().json(gson.toJson(expectedForm)));
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("getClosedPollDetailsTest")
    void getClosedPollDetailsTest(String name, String userId, String pollId) {
        Map<String, String> postData = Map.of(
                "userId", userId,
                "pollId", pollId,
                "listingOf", "closed"
        );

    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("getYetToOpenPollDetailsTest")
    void getYetToOpenPollDetailsTest() {

    }

}

