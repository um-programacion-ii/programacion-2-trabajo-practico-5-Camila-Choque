package Excepciones;

public class DepartamentoNoEncontradoException extends RuntimeException {
    public DepartamentoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
