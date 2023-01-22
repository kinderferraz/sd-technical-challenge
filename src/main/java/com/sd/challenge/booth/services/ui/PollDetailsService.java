package com.sd.challenge.booth.services.ui;

import com.sd.challenge.booth.data.entities.Poll;
import com.sd.challenge.booth.data.entities.UserVote;
import com.sd.challenge.booth.data.repositories.PollRepository;
import com.sd.challenge.booth.data.repositories.UserVoteRepository;
import com.sd.challenge.booth.resources.Screens.PollDetailsForm;
import com.sd.challenge.booth.resources.exception.PollException;
import com.sd.challenge.booth.resources.widgets.Form;
import com.sd.challenge.booth.services.integration.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
@Service
public class PollDetailsService {

    @Value("${application.properties.base-url}")
    String baseUrl;

    PollRepository pollRepository;

    UserVoteRepository userVoteRepository;

    UserService userService;

    @Autowired
    PollDetailsService(
            UserService userService,
            PollRepository pollRepository,
            UserVoteRepository userVoteRepository
    ) {
        this.userService = userService;
        this.pollRepository = pollRepository;
        this.userVoteRepository = userVoteRepository;
    }

    public Form getPollDetails(Map<String, String> data) {
        Long userId = Long.valueOf(data.get("userId"));
        Long pollId = Long.valueOf(data.get("pollId"));

        Poll poll = pollRepository.findById(pollId)
                .orElseThrow(() -> makeException("M=getPollDetails error getting poll pollId=" + pollId, data));

        String origin = data.get("listingOf");

        if (pollIsYetToBeOpened(poll))
            return getYetToBeOpenedPoll(poll, userId, origin, data);

        if (!pollIsOpen(poll))
            return getClosedPollDetails(poll, userId, origin, data);

        return getOpenPollDetails(userId, pollId, poll, origin, data);
    }

    private Form getYetToBeOpenedPoll(Poll poll, Long userId, String origin, Map<String, String> data) {
        if (origin.equals("closed") || origin.equals("open"))
            throw makeException("M=getPollDetails user bypassed gateway=" + userId, data);

        if (!poll.getOwner().getId().equals(userId))
            throw makeException("M=getPollDetails user trying to view unopened " +
                    "poll from someone else userId=" + userId, data);

        return PollDetailsForm.yetToOpenForm(poll, baseUrl, data);
    }

    private Form getClosedPollDetails(Poll poll, Long userId, String origin, Map<String, String> data){
        if (origin.equals("open"))
            throw makeException("M=getPollDetails user bypassed gateway=" + userId, data);

        return PollDetailsForm.getClosedPollDetails(poll, baseUrl, data);
    }

    private Form getOpenPollDetails(Long userId, Long pollId, Poll poll, String origin, Map<String, String> data) {
        if (origin.equals("closed"))
            throw makeException("M=getPollDetails user bypassed gateway=" + userId, data);

        Boolean userMayVote = userService.userMayVote(userId);
        UserVote vote = userVoteRepository.findUserVoteByPollAndVoter(pollId, userId);
        return PollDetailsForm.getOpenPollDetails(userId, poll, baseUrl, data, vote, userMayVote);
    }

    private boolean pollIsOpen(Poll poll) {
        return poll.getEndsAt() == null || poll.getEndsAt().isAfter(LocalDateTime.now());
    }

    private boolean pollIsYetToBeOpened(Poll poll){
        return poll.getOpenedAt() == null;
    }

    private PollException makeException(String message, Map<String, String> data) {
        return PollException.builder()
                .message(message)
                .data(data)
                .build();
    }

}
