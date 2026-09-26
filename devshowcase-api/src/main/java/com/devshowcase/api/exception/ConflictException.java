package com.devshowcase.api.exception;

/**
 * Exceção lançada quando há conflito de dados, como e-mail ou nome já cadastrado.
 * Mapeada para HTTP 409.
 */
public class ConflictException extends RuntimeException {

    public ConflictException(String message) {
        super(message);
    }
}
