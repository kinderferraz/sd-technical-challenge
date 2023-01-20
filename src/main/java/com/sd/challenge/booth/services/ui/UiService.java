package com.sd.challenge.booth.services.ui;

import com.sd.challenge.booth.data.entities.Poll;
import com.sd.challenge.booth.data.entities.User;
import com.sd.challenge.booth.data.entities.UserVote;
import com.sd.challenge.booth.data.repositories.PollRepository;
import com.sd.challenge.booth.data.repositories.UserRepository;
import com.sd.challenge.booth.data.repositories.UserVoteRepository;
import com.sd.challenge.booth.resources.Screens.*;
import com.sd.challenge.booth.resources.exception.PollException;
import com.sd.challenge.booth.resources.widgets.Form;
import com.sd.challenge.booth.resources.widgets.Selection;
import com.sd.challenge.booth.services.integration.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

@Slf4j
@Service
public class UiService {

    @Value("${application.properties.base-url}")
    String baseUrl;

    PollRepository pollRepository;

    UserRepository userRepository;

    UserVoteRepository userVoteRepostory;

    UserService userService;

    @Autowired
    public UiService(
            UserService userService,
            UserRepository userRepository,
            PollRepository pollRepository,
            UserVoteRepository userVoteRepository
    ) {
        this.userService = userService;
        this.userVoteRepostory = userVoteRepository;
        this.userRepository = userRepository;
        this.pollRepository = pollRepository;
    }

    public Form makeNewPollForm(Map<String, String> data) {
        return NewPollForm.get(baseUrl, data);
    }

    public Form makeLoginScreen() {
        return LoginForm.get(baseUrl);
    }

    public Selection makeGateway(Map<String, String> data) {
        User u = userRepository.findUserByCpf(data.get("idtCpfInput"));
        log.info("M=makeGateway user={}", u.getId());

        data.put("userId", u.getId().toString());
        data.remove("idtCpfInput");
        data.remove("listingOf");

        return PollGatewaySelection.get(baseUrl, data);
    }

    public Selection makeUserPollListing(Map<String, String> data) {
        Long userId = Long.parseLong(data.get("userId"));
        Set<Poll> polls = pollRepository.findAllByOwner(userId);


        return ListingSelection.get(baseUrl, "Suas iniciativas", polls, data);
    }

    public Selection makeOpenPollListing(Map<String, String> data) {
        Set<Poll> polls = pollRepository
                .findAllByOpenedAtBeforeAndEndsAtAfter(LocalDateTime.now(), LocalDateTime.now());
        return ListingSelection.get(baseUrl, "Iniciativas em aberto", polls, data);
    }

    public Selection makeClosedPollListing(Map<String, String> data) {
        Set<Poll> polls = pollRepository.findAllByEndsAtBefore(LocalDateTime.now());
        return ListingSelection.get(baseUrl, "Iniciativas finalizadas", polls, data);
    }

    public Form getPollDetails(Map<String, String> data) {
        Long userId = Long.valueOf(data.get("userId"));
        Long pollId = Long.valueOf(data.get("pollId"));

        Boolean userMayVote = userService.userMayVote(userId);
        UserVote vote = userVoteRepostory.findUserVoteByPollAndVoter(pollId, userId);

        Poll poll = pollRepository.findById(pollId)
                .orElseThrow(() -> makeException("M=getPollDetails", data));

        return PollDetailsForm.get(poll, vote, data, userMayVote, baseUrl);
    }

    public Form getPollResults(Map<String, String> data) {
        Long id = Long.parseLong(data.get("pollId"));
        Poll poll = pollRepository.findById(id)
                .orElseThrow(() -> makeException("M=getPollResults", data));

        return PollResultsForm.get(poll, baseUrl, data);
    }

    public Selection getVotingForm(Map<String, String> data) {
        Long pollId = Long.parseLong(data.get("pollId"));

        Poll poll = pollRepository.findById(pollId)
                .orElseThrow(() -> makeException("M=getVotingForm", data));

        return VoteSelection.get(baseUrl, data, poll);
    }

    public Form getAcceptedForm(Map<String, String> data) {
        return AcceptedForm.get(baseUrl, data);
    }

    private PollException makeException(String method, Map<String, String> data) {
        return PollException.builder()
                .message(method + " error getting poll")
                .data(data)
                .build();
    }

}
