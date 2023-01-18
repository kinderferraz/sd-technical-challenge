package com.sd.challenge.booth.services.ui;

public enum UIType {

    FORM("FORMULARIO"),
    SELECTION("SELECAO"),
    TEXT("TEXTO"),
    TEXT_INPUT("INPUT_TEXTO"),
    NUMBER_INPUT("INPUT_NUMERO"),
    DATE_INPUT("INPUT_DATA");

    final String label;

    UIType(String label) {
        this.label = label;
    }

}
