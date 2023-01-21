package com.sd.challenge.booth.services;

import com.sd.challenge.booth.data.entities.Poll;
import com.sd.challenge.booth.data.entities.User;
import com.sd.challenge.booth.data.entities.UserVote;
import com.sd.challenge.booth.data.repositories.PollRepository;
import com.sd.challenge.booth.data.repositories.UserRepository;
import com.sd.challenge.booth.data.repositories.UserVoteRepository;
import com.sd.challenge.booth.resources.exception.PollException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Map;

@Slf4j
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

        if (data.get("newPoll") == null)
            throw makeGatewayException("M=savePoll", userId, null);

        String title = data.get("idtTitle");
        String description = data.get("idtDescription");

        Poll poll = Poll.builder()
                .title(title)
                .proposal(description)
                .createdAt(LocalDateTime.now())
                .owner(owner)
                .acceptedVotes(0)
                .totalVotes(0)
                .build();

        pollRepository.save(poll);

    }

    public void castVote(Map<String, String> data) {
        String voteValueStr = data.get("accept");
        Long pollId = Long.valueOf(data.get("pollId"));
        Long userId = Long.valueOf(data.get("userId"));

        if (voteValueStr == null)
            throw makeGatewayException("M=castVote", userId, pollId);

        boolean voteValue = Boolean.parseBoolean(voteValueStr);

        Poll poll = getPoll(pollId, "M=castVote", userId);
        if (poll.getOwner().getId().equals(userId))
            throw makeException("M=castVote user owns poll userId={0} pollId={1}", userId, pollId);

        User user = getUser(userId, "M=castVote");
        if (userVoteRepository.findUserVoteByPollAndVoter(pollId, userId) != null)
            throw makeException("M=castVote user already cast vote on poll userId={0} pollId={1}",
                    userId, pollId);

        UserVote vote = UserVote.builder()
                .vote(voteValue)
                .poll(poll)
                .voter(user)
                .build();

        poll.setTotalVotes(poll.getTotalVotes() + 1);
        if(voteValue) poll.setAcceptedVotes(poll.getAcceptedVotes() + 1);

        saveBoth(poll, vote);
    }

    @Transactional
    void saveBoth(Poll poll, UserVote vote) {
        try {
            pollRepository.save(poll);
            userVoteRepository.save(vote);
        } catch (Exception e) {
            log.error("M=saveBoth error saving both poll and vote e={}", e.getMessage());
            throw PollException.builder()
                    .data(Map.of("userId", String.valueOf(vote.getVoter().getId())))
                    .build();
        }
    }

    public void openPoll(Map<String, String> data) {
        Long pollId = Long.valueOf(data.get("pollId"));
        Long userId = Long.valueOf(data.get("userId"));

        if (data.get("openPoll") == null)
            throw makeGatewayException("M=openPoll", userId, pollId);

        Poll poll = getPoll(pollId, "M=openPoll", userId);

        if(! poll.getOwner().getId().equals(userId))
            throw makeException("M=openPoll user does not own poll userId={0} pollId={1}",
                    userId, pollId);

        if(poll.getOpenedAt() != null)
            throw makeException("M=openPoll poll already open userId={0} pollId={1}",
                    userId, pollId);

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
                .orElseThrow(
                        () -> PollException.builder()
                        .message(MessageFormat.format("{0} poll not found pollId={1}",
                                method, pollId))
                        .data(Map.of("userId", String.valueOf(userId)))
                        .build());
    }

    private User getUser(Long userId, String method) {
        return userRepository.findById(userId)
                .orElseThrow(() -> PollException.builder()
                        .message(MessageFormat.format("{0} user not found userId={1}",
                                method, userId))
                        .data(Map.of("userId", String.valueOf(userId)))
                        .build());
    }

    private PollException makeException(String message, Long userId, Long pollId){
        return PollException.builder()
                .message(MessageFormat.format(message, userId, pollId))
                .data(Map.of("userId", String.valueOf(userId)))
                .build();
    }

    private PollException makeGatewayException(String method, Long userId, Long pollId){
        return makeException(method + "gateway error userId={0} pollId={1}", userId, pollId);
    }

}
