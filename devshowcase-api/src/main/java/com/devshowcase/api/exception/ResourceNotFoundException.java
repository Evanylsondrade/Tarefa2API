package com.devshowcase.api.exception;

/**
 * Exceção lançada quando um recurso não é encontrado no banco de dados.
 * Mapeada para HTTP 404.
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String resource, Long id) {
        super(resource + " com id " + id + " não encontrado(a)");
    }
}
