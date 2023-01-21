package com.sd.challenge.booth.resources.v2;


import com.sd.challenge.booth.resources.widgets.Form;
import com.sd.challenge.booth.services.PollService;
import com.sd.challenge.booth.services.ui.UiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping(value = "/polls", produces = "application/vnd.v2+json")
public class PollResourceV2 {


    PollService pollService;

    UiService uiService;

    @Autowired
    PollResourceV2(
            PollService pollService,
            UiService uiService
    ) {
        this.uiService = uiService;
        this.pollService = pollService;
    }


    @PostMapping()
    ResponseEntity<Form> postNewPoll(
            @RequestBody Map<String, String> data
    ) {
        log.info("M=postNewPoll data={}", data);
        pollService.savePoll(data);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(uiService.getAcceptedForm(data));
    }

}
