package com.sd.challenge.booth.unit.screens;

import com.sd.challenge.booth.data.entities.Poll;
import com.sd.challenge.booth.data.entities.User;
import com.sd.challenge.booth.resources.Screens.PollResultsForm;
import com.sd.challenge.booth.resources.exception.PollException;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class PollResultsFormTest {

    @Test
    void PollResultsFormExceptionTest() {
        assertThrows(PollException.class, () -> {
            User owner = User.builder().id(1L).build();
            PollResultsForm.get(Poll.builder().owner(owner).build(), "baseUrl", Map.of());
        });
        assertThrows(PollException.class, () -> {
            User owner = User.builder().id(1L).build();
            PollResultsForm.get(Poll.builder()
                            .owner(owner)
                            .endsAt(LocalDateTime.MAX)
                            .build(),
                    "baseUrl", Map.of());
        });
    }

}
