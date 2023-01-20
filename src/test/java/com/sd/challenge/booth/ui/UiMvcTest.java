package com.sd.challenge.booth.ui;

import com.google.gson.Gson;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UiMvcTest {

    @Autowired
    protected MockMvc mockMvc;

    @Value("${application.properties.base-url}")
    protected String baseUrl;

    protected Gson gson = new Gson();

    protected void performPostRequest(
            String endpoint,
            Map<String, String> postData,
            ResultMatcher expectedStatus,
            ResultMatcher expectedContent
    ) throws Exception {
        mockMvc.perform(post(endpoint)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(postData)))
                .andExpect(expectedStatus)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(expectedContent);
    }


}
