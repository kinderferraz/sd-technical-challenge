package com.sd.challenge.booth.services.ui;

public enum UIType {

    FORMULARIO("FORMULARIO"),
    TEXTO("TEXTO"),
    INPUT_TEXTO("INPUT_TEXTO"),
    INPUT_NUMERO("INPUT_NUMERO"),
    INPUT_DATA("INPUT_DATA");

    final String label;

    UIType(String label) {
        this.label = label;
    }

}
