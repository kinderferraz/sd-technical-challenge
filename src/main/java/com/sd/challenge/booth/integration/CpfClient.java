package com.sd.challenge.booth.integration;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@FeignClient(name = "CpfClient", url = "${application.properties.cpf-url}", decode404 = true)
public interface CpfClient {

    @GetMapping("/users/{cpf}")
    Optional<CpfServiceResponse> getUserMayVote(@PathVariable String cpf);

}
