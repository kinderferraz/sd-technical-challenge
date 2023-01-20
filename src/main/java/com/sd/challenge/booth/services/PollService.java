package com.sd.challenge.booth.services;

import com.sd.challenge.booth.data.entities.Poll;
import com.sd.challenge.booth.data.entities.User;
import com.sd.challenge.booth.data.entities.UserVote;
import com.sd.challenge.booth.data.repositories.PollRepository;
import com.sd.challenge.booth.data.repositories.UserRepository;
import com.sd.challenge.booth.data.repositories.UserVoteRepository;
import com.sd.challenge.booth.resources.exception.PollException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Map;

@Service
public class PollService {

    PollRepository pollRepository;

    UserRepository userRepository;
    private final UserVoteRepository userVoteRepository;

    PollService(
            PollRepository pollRepository,
            UserRepository userRepository,
            UserVoteRepository userVoteRepository
    ) {
        this.pollRepository = pollRepository;
        this.userRepository = userRepository;
        this.userVoteRepository = userVoteRepository;
    }

    public void savePoll(Map<String, String> data) {
        Long userId = Long.valueOf(data.get("userId"));
        User owner = getUser(userId, "M=savePoll");

        String title = data.get("idtTitle");
        String description = data.get("idtDescription");

        Poll poll = Poll.builder()
                .title(title)
                .proposal(description)
                .createdAt(LocalDateTime.now())
                .owner(owner)
                .build();

        pollRepository.save(poll);

    }

    public void castVote(Map<String, String> data) {
        boolean voteValue = Boolean.parseBoolean(data.get("accept"));
        Long pollId = Long.valueOf(data.get("pollId"));
        Long userId = Long.valueOf(data.get("userId"));

        Poll poll = getPoll(pollId, "M=castVote", userId);
        User user = getUser(userId, "M=castVote");

        if (userVoteRepository.findUserVoteByPollAndVoter(pollId, userId) != null)
            throw PollException.builder()
                    .message("user already cast vote on poll" +
                            " userId=" + userId +
                            " pollId=" + pollId )
                    .data(Map.of("userId", userId.toString()))
                    .build();

        UserVote vote = UserVote.builder()
                .vote(voteValue)
                .poll(poll)
                .voter(user)
                .build();

        poll.setTotalVotes(poll.getTotalVotes() + 1);
        if(voteValue) poll.setAcceptedVotes(poll.getAcceptedVotes() + 1);

        userVoteRepository.save(vote);
        pollRepository.save(poll);
    }

    public void openPoll(Map<String, String> data) {
        Long pollId = Long.valueOf(data.get("pollId"));
        Long userId = Long.valueOf(data.get("userId"));

        Poll poll = getPoll(pollId, "M=openPoll", userId);

        if(! poll.getOwner().getId().equals(userId))
            throw PollException.builder()
                    .message("M=openPoll user does not own poll poll=" + pollId +
                            " userId=" + userId)
                    .data(Map.of("userId", String.valueOf(userId)))
                    .build();

        poll.setOpenedAt(LocalDateTime.now());
        poll.setEndsAt(getEndingTime(data));
        pollRepository.save(poll);
    }

    private LocalDateTime getEndingTime(Map<String, String> data) {
        String timestamp = data.getOrDefault("idtClosesAt", null);
        if (timestamp == null) return LocalDateTime.now().plus(1, ChronoUnit.MINUTES);

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return LocalDate.parse(timestamp, dtf).atStartOfDay();
    }

    private Poll getPoll(Long pollId, String method, Long userId) {
        return pollRepository.findById(pollId)
                .orElseThrow(() -> PollException.builder()
                        .message(method + " poll not found pollId=" + pollId)
                        .data(Map.of("userId", String.valueOf(userId)))
                        .build());
    }

    private User getUser(Long userId, String method) {
        return userRepository.findById(userId)
                .orElseThrow(() -> PollException.builder()
                        .message(method + " user not found userId="+userId)
                        .data(Map.of("userId", String.valueOf(userId)))
                        .build());
    }

}
