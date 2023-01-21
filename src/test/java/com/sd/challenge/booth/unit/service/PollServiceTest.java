package com.sd.challenge.booth.unit.service;

import com.sd.challenge.booth.resources.exception.PollException;
import com.sd.challenge.booth.services.PollService;
import com.sd.challenge.booth.ui.MvcTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class PollServiceTest extends MvcTest {

    @Autowired
    PollService pollService;

    @Test
    void pollServiceExceptionsTest() {
        assertThrows(PollException.class, () -> pollService
                .castVote(Map.of("userId", "11", "pollId", "1")));
        assertThrows(PollException.class, () -> pollService
                .castVote(Map.of("userId", "1", "pollId", "11")));
    }
}
