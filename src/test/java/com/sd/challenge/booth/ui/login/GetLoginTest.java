package com.sd.challenge.booth.ui.login;


import com.google.gson.Gson;
import com.sd.challenge.booth.resources.Screens.LoginForm;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class GetLoginTest {

    @Autowired
    MockMvc mockMvc;

    @Value("${application.properties.base-url}")
    String baseUrl;

    @Test
    void getLoginFormTest() throws Exception {
        Gson gson = new Gson();

        Form loginForm = LoginForm.get(baseUrl);

        mockMvc.perform(get("/ui/login")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(gson.toJson(loginForm)));
    }
}
