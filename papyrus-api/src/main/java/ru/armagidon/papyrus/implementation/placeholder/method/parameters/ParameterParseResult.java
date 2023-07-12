package ru.armagidon.papyrus.implementation.placeholder.method.parameters;

import org.apache.commons.lang.Validate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ParameterParseResult<T>
{

    @Nullable T getResult();

    @Nullable ParameterParseException getError();

    static <T> ParameterParseResult<T> success(@NotNull T result) {
        Validate.notNull(result);
        return new Success<>(result);
    }
    static <T> ParameterParseResult<T> fail(@NotNull ParameterParseException exception) {
        Validate.notNull(exception);
        return (ParameterParseResult<T>) new Fail(exception);
    }

    final class Fail implements ParameterParseResult<Object> {

        private final ParameterParseException error;

        private Fail(ParameterParseException error) {
            this.error = error;
        }

        @Override
        public Object getResult() {
            return null;
        }

        @Override
        public @NotNull ParameterParseException getError() {
            return error;
        }
    }

    final class Success<T> implements ParameterParseResult<T> {

        private final T result;

        private Success(T result) {
            this.result = result;
        }

        @Override
        public @Nullable T getResult() {
            return result;
        }

        @Override
        public @Nullable ParameterParseException getError() {
            return null;
        }
    }

}
