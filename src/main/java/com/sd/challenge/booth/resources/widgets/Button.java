package com.sd.challenge.booth.resources.widgets;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class Button {

    String texto;
    String url;
    Map<String, String> body;

}
