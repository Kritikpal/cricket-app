package org.fastX.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class GameException extends RuntimeException {
    private final int exceptionCode;

    public GameException(String message, int code) {
        super(message);
        this.exceptionCode = code;
    }
}
