package com.sd.challenge.booth.resources.widgets;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Form {

    static String tipo = "FORMULARIO";
    String titulo;
    List<Element> itens;
    Element botaoOk;
    Element botaoCancelar;

}
