package com.sd.challenge.booth.data.repositories;

import com.sd.challenge.booth.data.entities.Poll;
import org.springframework.data.repository.CrudRepository;


public interface PollRepository extends CrudRepository<Poll, Long> { }
