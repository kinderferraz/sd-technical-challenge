package com.sd.challenge.booth.data.repositories;

import com.sd.challenge.booth.data.entities.Poll;
import com.sd.challenge.booth.data.entities.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface PollRepository extends CrudRepository<Poll, Long> {

    Set<Poll> findAllByOwner(User user);

}
