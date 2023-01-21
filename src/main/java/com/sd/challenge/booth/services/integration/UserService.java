package com.sd.challenge.booth.services.integration;

import com.sd.challenge.booth.data.entities.User;
import com.sd.challenge.booth.data.repositories.UserRepository;
import com.sd.challenge.booth.integration.CpfClient;
import com.sd.challenge.booth.integration.CpfServiceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    CpfClient client;

    UserRepository userRepository;

    @Autowired
    UserService(
            CpfClient cpfClient,
            UserRepository userRepository
    ) {
        this.client = cpfClient;
        this.userRepository = userRepository;
    }

    public Boolean userMayVote(Long id) {
        String validCpf = userRepository.findById(id)
                .map(User::getCpf)
                .orElse("11111111111");

        Optional<CpfServiceResponse> cpfResponse = client.getUserMayVote(validCpf);
        if (cpfResponse.isPresent())
            return cpfResponse.get().getStatus().equalsIgnoreCase("able_to_vote");

        return false;

    }

}
