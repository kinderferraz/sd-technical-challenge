package com.sd.challenge.booth.ui.poll_gateway;

import com.sd.challenge.booth.resources.Screens.PollGatewaySelection;
import com.sd.challenge.booth.resources.widgets.Selection;
import com.sd.challenge.booth.ui.MvcTest;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class GetPollGatewayTest extends MvcTest {

    @Test
    void getPollGatewayTest() throws Exception {
        Map<String, String> data = Map.of("idtCpfInput", "19279699059");

        Selection gateway = PollGatewaySelection.get(baseUrl, Map.of("userId", "1"));
        performPostRequest("/ui/poll-gateway", data, status().isOk(),
                content().json(gson.toJson(gateway)));
    }

}
