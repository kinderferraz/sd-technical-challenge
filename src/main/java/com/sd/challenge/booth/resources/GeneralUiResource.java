package com.sd.challenge.booth.resources;

import com.sd.challenge.booth.resources.widgets.Screen;
import com.sd.challenge.booth.services.ui.ScreenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/ui")
public class GeneralUiResource {

    ScreenService screenService;

    @Autowired
    public GeneralUiResource(ScreenService screenService) {
        this.screenService = screenService;
    }

    @GetMapping("/login")
    public ResponseEntity<Screen> getLoginScreen() {
        return ResponseEntity.ok(screenService.makeLoginScreen());
    }

    @PostMapping("/new-poll-form")
    public ResponseEntity<Screen> getNewPollForm(
            @RequestBody Map<String, String> data
    ) {
        return ResponseEntity.ok(screenService.makeNewPollForm(data));
    }

    @PostMapping("/poll-gateway")
    public ResponseEntity<Screen> getPollGateway(
            @RequestBody Map<String, String> data
    ) {
        data.forEach((key, value) -> log.info("M=getPollGateway k={} v={}", key, value));
        return ResponseEntity.ok(screenService.makeGateway(data));
    }

}
