package com.example.TP5.Excepciones;

public class DepartamentoNoEncontradoException extends RuntimeException {
    public DepartamentoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
