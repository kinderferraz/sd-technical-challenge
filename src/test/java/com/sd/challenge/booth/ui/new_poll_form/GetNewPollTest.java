package com.sd.challenge.booth.ui.new_poll_form;

import com.sd.challenge.booth.resources.Screens.NewPollForm;
import com.sd.challenge.booth.resources.widgets.Form;
import com.sd.challenge.booth.ui.MvcTest;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class GetNewPollTest extends MvcTest {

    @Test
    void getNewPollForUserTest() throws Exception {
        Map<String, String> data = new HashMap<>(Map.of("userId", "1", "newPoll", "true"));

        Form newPollForm = NewPollForm.get(baseUrl, data);

        performPostRequest("/ui/new-poll-form", data, status().isOk(),
                content().json(gson.toJson(newPollForm)));
    }
}
