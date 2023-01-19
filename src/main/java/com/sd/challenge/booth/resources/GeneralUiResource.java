package com.sd.challenge.booth.resources;

import com.sd.challenge.booth.resources.exception.PollException;
import com.sd.challenge.booth.resources.widgets.Form;
import com.sd.challenge.booth.resources.widgets.Selection;
import com.sd.challenge.booth.services.ui.UiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/ui")
public class GeneralUiResource {

    UiService uiService;

    @Autowired
    public GeneralUiResource(UiService uiService) {
        this.uiService = uiService;
    }

    @GetMapping("/login")
    public ResponseEntity<Form> getLoginScreen() {
        log.info("M=getLoginScreen");
        return ResponseEntity.ok(uiService.makeLoginScreen());
    }

    @PostMapping("/new-poll-form")
    public ResponseEntity<Form> getNewPollForm(
            @RequestBody Map<String, String> data
    ) {
        log.info("M=getNewPollForm data={}", data.toString());
        return ResponseEntity.ok(uiService.makeNewPollForm(data));
    }

    @PostMapping("/poll-gateway")
    public ResponseEntity<Selection> getPollGateway(
            @RequestBody Map<String, String> data
    ) {
        log.info("M=getPollGateway data={}", data.toString());
        return ResponseEntity.ok(uiService.makeGateway(data));
    }

    @PostMapping("/poll/listing")
    public ResponseEntity<Selection> getPolls(
            @RequestBody Map<String, String> data
    ) {
        log.info("M=getPolls data={}", data.toString());

        if (data.get("listingOf").equalsIgnoreCase("userPolls"))
            return ResponseEntity.ok(uiService.makeUserPollListing(data));

        if (data.get("listingOf").equalsIgnoreCase("openPolls"))
            return ResponseEntity.ok(uiService.makeOpenPollListing(data));


        if (data.get("listingOf").equalsIgnoreCase("openPolls"))
            return ResponseEntity.ok(uiService.makeClosedPollListing(data));

        throw PollException.builder()
                .message("gateway: route not found")
                .data(data).build();
    }

    @PostMapping("/poll/details")
    public ResponseEntity<Form> getPollDetails(
            @RequestBody Map<String, String> data
    ) {
        log.info("M=getPollDetails data={}", data.toString());
        return ResponseEntity.ok(uiService.getPollDetails(data));
    }

    @PostMapping("/poll/vote")
    public ResponseEntity<Selection> getVotingSelection(
            @RequestBody Map<String, String> data
    ) {
        log.info("M=getVotingSelection data={}", data.toString());
        return ResponseEntity.ok(uiService.getVotingForm(data));
    }

    @PostMapping("/poll/results")
    public ResponseEntity<Form> getPollResults(
            @RequestBody Map<String, String> data
    ) {
        log.info("M=getPollResults data={}", data.toString());
        return ResponseEntity.ok(uiService.getPollResults(data));
    }
}
