package com.sd.challenge.booth.data.repositories;

import com.sd.challenge.booth.data.entities.Poll;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

public interface PollRepository extends CrudRepository<Poll, Long> {

    Set<Poll> findAllByEndsAtAfter(LocalDateTime now);

    Set<Poll> findAllByEndsAtBefore(LocalDateTime now);

    @Query("select p from Poll p join fetch UserVote where p.id = (:id) and p.endsAt < CURRENT DATE ")
    Optional<Poll> findByIdAndEndsAtBeforeWithVotes(Long id, LocalDateTime now);
}
