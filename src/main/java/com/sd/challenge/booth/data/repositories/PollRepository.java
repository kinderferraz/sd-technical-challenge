package com.sd.challenge.booth.data.repositories;

import com.sd.challenge.booth.data.entities.Poll;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.Set;

public interface PollRepository extends CrudRepository<Poll, Long> {

    @Query("select p from Poll p where p.owner.id = (:ownerId)")
    Set<Poll> findAllByOwner(Long ownerId);

    Set<Poll> findAllByEndsAtBefore(LocalDateTime now);

    Set<Poll> findAllByOpenedAtBeforeAndEndsAtAfter(LocalDateTime now, LocalDateTime now2);

}
