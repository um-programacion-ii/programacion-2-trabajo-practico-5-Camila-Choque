package com.example.TP5.Excepciones;

public class EmailDuplicadoException extends RuntimeException {
    public EmailDuplicadoException(String mensaje) {
        super(mensaje);
    }
}
