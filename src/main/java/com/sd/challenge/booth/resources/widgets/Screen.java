package com.sd.challenge.booth.resources.widgets;

import com.sd.challenge.booth.services.ui.UIType;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Screen {

    String titulo;

    UIType tipo;

    List<Element> itens;

    Element botaoOk;

    Element botaoCancelar;

}
