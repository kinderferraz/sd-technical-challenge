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
import java.util.Map;

@Service
public class PollService {

    PollRepository pollRepository;

    UserRepository userRepository;
    private final UserVoteRepository userVoteRepository;

    PollService(
            PollRepository pollRepository,
            UserRepository userRepository,
            UserVoteRepository userVoteRepository) {
        this.pollRepository = pollRepository;
        this.userRepository = userRepository;
        this.userVoteRepository = userVoteRepository;
    }

    public void savePoll(Map<String, String> data) {
        DateTimeFormatter sd = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        String userId = data.get("userId");
        User owner = userRepository.findById(Long.parseLong(userId))
                .orElseThrow(() -> PollException.builder()
                        .message("poll not found")
                        .data(Map.of("userId", userId))
                        .build());

        String title = data.get("idtTitle");
        String description = data.get("idtDescription");
        String closingDate = data.get("idtClosingDate");

        Poll poll = Poll.builder()
                .title(title)
                .proposal(description)
                .endsAt(LocalDate.parse(closingDate, sd).atStartOfDay())
                .createdAt(LocalDateTime.now())
                .owner(owner)
                .build();

        pollRepository.save(poll);

    }

    public void castVote(Map<String, String> data) {
        boolean voteValue = Boolean.parseBoolean(data.get("accept"));
        Long pollId = Long.valueOf(data.get("pollId"));
        Long userId = Long.valueOf(data.get("userId"));

        Poll poll = pollRepository.findById(pollId)
                .orElseThrow(() -> PollException.builder()
                        .message("poll not found")
                        .data(Map.of("userId", String.valueOf(userId)))
                        .build());

        User user = userRepository.findById(userId)
                .orElseThrow(() -> PollException.builder()
                        .message("user not found")
                        .data(Map.of("userId", String.valueOf(userId)))
                        .build());

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

        userVoteRepository.save(vote);
    }
}
