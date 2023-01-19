package com.sd.challenge.booth.services.integration;

import com.sd.challenge.booth.data.repositories.UserRepository;
import com.sd.challenge.booth.integration.CpfClient;
import com.sd.challenge.booth.integration.CpfServiceResponse;
import com.sd.challenge.booth.resources.exception.PollException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
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
                .orElseThrow(()-> PollException.builder()
                        .message("user not found")
                        .data(Map.of("userId", String.valueOf(id)))
                        .build())
                .getCpf()
                .replaceAll("\\.", "")
                .replaceAll("-", "");

        Optional<CpfServiceResponse> cpfResponse = client.getUserMayVote(validCpf);
        if (cpfResponse.isPresent())
            return cpfResponse.get().getStatus().equalsIgnoreCase("able_to_vote");

        return false;

    }

}
