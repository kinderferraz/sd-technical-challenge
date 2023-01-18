package com.sd.challenge.booth.resources;

import com.sd.challenge.booth.data.entities.Poll;
import com.sd.challenge.booth.data.entities.User;
import com.sd.challenge.booth.data.repositories.PollRepository;
import com.sd.challenge.booth.data.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(value = "/polls", produces = MediaType.APPLICATION_JSON_VALUE)
public class PollResource {

    PollRepository pollRepository;

    UserRepository userRepository;

    @Autowired
    PollResource(
            PollRepository pollRepository,
            UserRepository userRepository
    ) {
        this.pollRepository = pollRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/{pollId}")
    public ResponseEntity<Poll> getUserPolls(
            @PathVariable("pollId") final Long pollId
    ) {
        return ResponseEntity.ok(new Poll());
    }

    @GetMapping("/{pollId}/owner")
    public ResponseEntity<User> getPollOwner(
            @PathVariable("pollId") Long pollId
    ) {
        return ResponseEntity.ok(new User());
    }

    // todo: write widget objects to replace Void
    @GetMapping("/form")
    public ResponseEntity<Void> getNewPollForm() {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{pollId}/vote-count")
    public ResponseEntity<Void> getPollResults(
            @PathVariable("pollId") Long pollId
    ) {
        return ResponseEntity.ok().build();
    }

    @PostMapping()
    public ResponseEntity<Void> postNewPoll(
            @RequestBody Object data
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    @PostMapping("/{pollId}/vote")
    public ResponseEntity<Void> vote(
            @PathVariable("pollId") Long pollId,
            @RequestBody Object data
    ) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }



}


