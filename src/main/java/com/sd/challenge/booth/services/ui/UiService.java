package com.sd.challenge.booth.services.ui;

import com.sd.challenge.booth.data.entities.Poll;
import com.sd.challenge.booth.data.entities.User;
import com.sd.challenge.booth.data.repositories.PollRepository;
import com.sd.challenge.booth.data.repositories.UserRepository;
import com.sd.challenge.booth.data.repositories.UserVoteRepository;
import com.sd.challenge.booth.resources.Screens.*;
import com.sd.challenge.booth.resources.exception.PollException;
import com.sd.challenge.booth.resources.widgets.Form;
import com.sd.challenge.booth.resources.widgets.Selection;
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

    UserVoteRepository userVoteRepository;

    PollDetailsService pollDetailsService;

    @Autowired
    public UiService(
            UserRepository userRepository,
            PollRepository pollRepository,
            UserVoteRepository userVoteRepository,
            PollDetailsService pollDetailsService
    ) {
        this.userVoteRepository = userVoteRepository;
        this.userRepository = userRepository;
        this.pollRepository = pollRepository;
        this.pollDetailsService = pollDetailsService;
    }

    private final String pollErrorCause = " error getting poll";

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
        return pollDetailsService.getPollDetails(data);
    }

    public Form getPollResults(Map<String, String> data) {
        Long pollId = Long.parseLong(data.get("pollId"));
        Poll poll = pollRepository.findById(pollId)
                .orElseThrow(() -> makeException("M=getPollResults" + pollErrorCause, data));

        if (poll.getEndsAt() == null || poll.getEndsAt().isAfter(LocalDateTime.now()))
            throw makeException("M=getVotingForm user trying to view results of open poll pollId=" + pollId +
                    " userId=" + data.get("userId"), data);

        return PollResultsForm.get(poll, baseUrl, data);
    }

    public Selection getVotingForm(Map<String, String> data) {
        Long pollId = Long.parseLong(data.get("pollId"));

        Poll poll = pollRepository.findById(pollId)
                .orElseThrow(() -> makeException("M=getVotingForm" + pollErrorCause, data));

        if (poll.getOwner().getId().equals(Long.parseLong(data.get("userId"))))
            throw makeException("M=getVotingForm user trying to vote on own poll pollId=" + pollId +
                    " userId=" + data.get("userId"), data);

        if (poll.getEndsAt() == null || poll.getEndsAt().isBefore(LocalDateTime.now()))
            throw makeException("M=getVotingForm user trying to vote on closed poll pollId=" + pollId +
                    " userId=" + data.get("userId"), data);

        return VoteSelection.get(baseUrl, data, poll);
    }

    public Form getAcceptedForm(Map<String, String> data) {
        return AcceptedForm.get(baseUrl, data);
    }

    private PollException makeException(String message, Map<String, String> data) {
        return PollException.builder()
                .message(message)
                .data(data)
                .build();
    }

}
