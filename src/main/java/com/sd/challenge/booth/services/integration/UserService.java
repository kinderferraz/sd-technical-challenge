package com.sd.challenge.booth.services.integration;

import com.sd.challenge.booth.data.entities.User;
import com.sd.challenge.booth.data.repositories.UserRepository;
import com.sd.challenge.booth.integration.CpfClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        return userRepository.findById(id)
                .map(User::getCpf)
                .map(cpf-> client.getUserMayVote(cpf))
                .map(r-> r.getStatus().equalsIgnoreCase("able_to_vote"))
                .orElse(false);
    }

}
