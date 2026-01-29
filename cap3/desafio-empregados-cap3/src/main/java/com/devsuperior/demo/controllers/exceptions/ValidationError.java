package com.devsuperior.demo.controllers.exceptions;

import java.util.ArrayList;
import java.util.List;

public class ValidationError extends StandardError {

    private List<FieldMessage> erros = new ArrayList<>();

    public List<FieldMessage> getErros() {
        return erros;
    }

    public void addError(String fieldMessage, String message) {
        erros.add(new FieldMessage(fieldMessage, message));
    }
}
