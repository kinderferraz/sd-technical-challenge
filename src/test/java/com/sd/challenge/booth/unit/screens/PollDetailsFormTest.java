package com.sd.challenge.booth.unit.screens;

import com.sd.challenge.booth.data.entities.Poll;
import com.sd.challenge.booth.data.entities.User;
import com.sd.challenge.booth.resources.Screens.PollDetailsForm;
import com.sd.challenge.booth.resources.exception.PollException;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class PollDetailsFormTest {

    @Test
    void PollDetailsFormExceptionTest() {
        assertThrows(PollException.class, () -> {
            User owner = User.builder().id(1L).build();
            PollDetailsForm.yetToOpenForm(2L, Poll.builder().owner(owner).build(), "baseUrl", Map.of());
        });
    }

}
