package com.sd.challenge.booth.data.repositories;

import com.sd.challenge.booth.data.entities.UserVote;
import org.springframework.data.repository.CrudRepository;

public interface UserVoteRepostory extends CrudRepository<UserVote, Long> { }
