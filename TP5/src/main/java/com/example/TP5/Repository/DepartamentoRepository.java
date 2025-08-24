package com.example.TP5.Repository;
import com.example.TP5.Entidades.Departamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface DepartamentoRepository extends JpaRepository<Departamento, Long> {
    Optional<Departamento> findByNombre(String nombre);
}
