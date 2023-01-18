package com.sd.challenge.booth.resources;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
@Slf4j
public class PoolResource {

    @GetMapping(value = "/")
    public CompletableFuture<ResponseEntity<Void>> getTest() {
        return CompletableFuture.completedFuture(ResponseEntity.ok().build());
    }

}
