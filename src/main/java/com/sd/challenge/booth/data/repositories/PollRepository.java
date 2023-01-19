package com.sd.challenge.booth.data.repositories;

import com.sd.challenge.booth.data.entities.Poll;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

public interface PollRepository extends CrudRepository<Poll, Long> {

    Set<Poll> findAllByEndsAtAfter(LocalDateTime now);

    Optional<Poll> findByIdAndEndsAtBefore(Long id, LocalDateTime now);
}
