package com.sd.challenge.booth.data.repositories;

import com.sd.challenge.booth.data.entities.UserVote;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface UserVoteRepostory extends CrudRepository<UserVote, Long> {

    @Query("select uv from UserVote uv where uv.poll.id = (:pollId) and uv.voter.id = (:voterId) ")
    UserVote findUserVoteByPollAndVoter(Long pollId, Long voterId);


}
