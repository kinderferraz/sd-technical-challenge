package com.sd.challenge.booth.resources;

import com.sd.challenge.booth.resources.widgets.Form;
import com.sd.challenge.booth.services.PollService;
import com.sd.challenge.booth.services.ui.UiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping(value = "/polls", produces = MediaType.APPLICATION_JSON_VALUE)
public class PollResource {


    PollService pollService;

    UiService uiService;

    @Autowired
    PollResource(
            PollService pollService,
            UiService uiService
    ) {
        this.pollService = pollService;
        this.uiService = uiService;
    }

    @PostMapping()
    public ResponseEntity<Form> postNewPoll(
            @RequestBody Map<String, String> data
    ) {
        log.info("M=postNewPoll data={}", data);
        pollService.savePoll(data);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(uiService.getAcceptedForm(data));
    }

    @PostMapping("/open")
    public ResponseEntity<Form> openPoll(
            @RequestBody Map<String, String> data
    ) {
        log.info("M=postNewPoll data={}", data);
        pollService.openPoll(data);
        return ResponseEntity.ok(uiService.getAcceptedForm(data));
    }

    @PostMapping("/vote")
    public ResponseEntity<Form> vote(
            @RequestBody Map<String, String> data
    ) {
        log.info("M=postNewPoll data={}", data);
        pollService.castVote(data);
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(uiService.getAcceptedForm(data));
    }

}


