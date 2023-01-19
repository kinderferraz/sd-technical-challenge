package com.sd.challenge.booth.resources.Screens;

import com.sd.challenge.booth.data.entities.Poll;
import com.sd.challenge.booth.resources.widgets.Form;

import java.util.Map;

public class PollResultsForm {

    public static Form get(Poll poll, Map<String, String> data) {
        return Form.builder().build();
    }
}
