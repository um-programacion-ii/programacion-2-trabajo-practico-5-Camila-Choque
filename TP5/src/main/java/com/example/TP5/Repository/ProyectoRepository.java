package com.example.TP5.Repository;
import com.example.TP5.Entidades.Proyecto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProyectoRepository extends JpaRepository<Proyecto, Long> {
    List<Proyecto> findByFechaFinAfter(LocalDate fecha);
    Optional<Proyecto> findByNombre(String nombre);
    List<Proyecto> findByFechaInicio(LocalDate fechaInicio);
    ;
}