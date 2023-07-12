package ru.armagidon.papyrus.implementation.placeholder.method.parameters;

import org.jetbrains.annotations.Nullable;

public class ParameterParseException extends RuntimeException {


    private final String parameterInput;

    public ParameterParseException(String message, String parameterInput) {
        super(message.replace("{input}", String.valueOf(parameterInput)));
        this.parameterInput = parameterInput;
    }


    public @Nullable String getParameterInput() {
        return parameterInput;
    }
}
