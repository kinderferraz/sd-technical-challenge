package com.sd.challenge.booth.resources.widgets;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Screen {

    String titulo;

    String tipo;

    List<Element> itens;

    Button botaoOk;

    Button botaoCancelar;

}
