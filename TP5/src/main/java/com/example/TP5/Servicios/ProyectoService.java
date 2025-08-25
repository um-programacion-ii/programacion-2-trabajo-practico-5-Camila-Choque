package com.example.TP5.Servicios;
import com.example.TP5.Entidades.Proyecto;
import java.util.List;

public interface ProyectoService {
    Proyecto guardar(Proyecto proyecto);
    Proyecto buscarPorId(Long id);
    List<Proyecto> obtenerTodos();
    Proyecto actualizar(Long id, Proyecto proyecto);
    void eliminar(Long id);
}
