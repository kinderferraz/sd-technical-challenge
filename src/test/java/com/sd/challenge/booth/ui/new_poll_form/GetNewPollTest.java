package com.sd.challenge.booth.ui.new_poll_form;


import com.google.gson.Gson;
import com.sd.challenge.booth.resources.Screens.NewPollForm;
import com.sd.challenge.booth.resources.widgets.Form;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class GetNewPollTest {

    @Autowired
    MockMvc mockMvc;

    @Value("${application.properties.base-url}")
    String baseUrl;

    @Test
    void getNewPollForUserTest() throws Exception {
        Gson gson = new Gson();

        Map<String, String> data = new HashMap<>(Map.of("userId", "1", "newPoll", "true"));

        Form newPollForm = NewPollForm.get(baseUrl, data);

        mockMvc.perform(post("/ui/new-poll-form")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(data)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(gson.toJson(newPollForm)))
        ;
    }
}
