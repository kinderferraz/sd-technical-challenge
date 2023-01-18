package com.sd.challenge.booth.services;

import com.sd.challenge.booth.data.repositories.PollRepository;
import org.springframework.stereotype.Service;

@Service
public class PollService {

    PollRepository pollRepository;

    PollService(PollRepository pollRepository) {
        this.pollRepository = pollRepository;
    }


}
