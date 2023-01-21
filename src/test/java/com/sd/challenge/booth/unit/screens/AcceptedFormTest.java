package com.sd.challenge.booth.unit.screens;

import com.sd.challenge.booth.resources.Screens.AcceptedForm;
import com.sd.challenge.booth.resources.exception.PollException;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class AcceptedFormTest {

    @Test
    void AcceptedFormThrowsExceptionTest() {
        assertThrows(PollException.class, ()-> AcceptedForm.get("baseUrl", Map.of(
                "userId", "1",
                "pollId", "2",
                "accept", "true",
                "openPoll", "true"
        )));
        assertThrows(PollException.class, ()-> AcceptedForm.get("baseUrl", Map.of(
                "userId", "1",
                "pollId", "2"
        )));
    }

}
