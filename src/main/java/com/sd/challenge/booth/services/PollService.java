package com.sd.challenge.booth.services;

import com.sd.challenge.booth.data.repositories.PollRepository;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PollService {

    PollRepository pollRepository;

    PollService(PollRepository pollRepository) {
        this.pollRepository = pollRepository;
    }

    public void savePoll(Map<String, String> data) {
    }

    public void castVote(Map<String, String> data) {
    }
}
