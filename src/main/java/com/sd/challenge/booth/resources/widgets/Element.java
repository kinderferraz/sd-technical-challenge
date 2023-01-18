package com.sd.challenge.booth.resources.widgets;

import com.sd.challenge.booth.services.ui.UIType;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class Element {
    UIType tipo;
    String texto;
    String id;
    String titulo;
    String valor;
    String url;
    Map<String, String> body;
}
