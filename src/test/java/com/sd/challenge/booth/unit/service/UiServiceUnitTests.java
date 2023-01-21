package com.sd.challenge.booth.unit.service;

import com.sd.challenge.booth.resources.exception.PollException;
import com.sd.challenge.booth.services.ui.UiService;
import com.sd.challenge.booth.ui.MvcTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class UiServiceUnitTests extends MvcTest {

    @Autowired
    UiService uiService;

    @Test
    void getVotingFormThrowsException() {
        assertThrows(PollException.class ,() -> uiService.getVotingForm(Map.of(
                "userId", "1",
                "pollId", "3"
        )));
        assertThrows(PollException.class ,() -> uiService.getVotingForm(Map.of(
                "userId", "1",
                "pollId", "11"
        )));
    }



}
