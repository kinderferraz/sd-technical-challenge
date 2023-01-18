package com.sd.challenge.booth.resources.widgets;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Selection {

    static String tipo = "SELECTION";
    String titulo;
    List<Element> itens;

}
