package com.example.TP5.Repository.Proyecto;
import com.example.TP5.Entidades.Proyecto;
import com.example.TP5.Repository.ProyectoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ActiveProfiles("mysql")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
public class ProyectoRepositoryMySqlTest {
    @Autowired
    private ProyectoRepository proyectoRepository;

    private Proyecto proyecto1;

    @BeforeEach
    public void setUp() {
        proyecto1 = new Proyecto();
        proyecto1.setNombre("Proyecto A");
        proyecto1.setFechaInicio(LocalDate.of(2025, 1, 1));
        proyecto1 = proyectoRepository.save(proyecto1);

        Proyecto proyecto2 = new Proyecto();
        proyecto2.setNombre("Proyecto B");
        proyecto2.setFechaInicio(LocalDate.of(2025, 6, 12));
        proyectoRepository.save(proyecto2);
    }

    @Test
    void testFindByNombre() {
        Optional<Proyecto> encontrado = proyectoRepository.findByNombre("Proyecto A");
        assertTrue(encontrado.isPresent());
        assertEquals("Proyecto A", encontrado.get().getNombre());
    }

    @Test
    void testFindByFechaInicio() {
        List<Proyecto> resultados = proyectoRepository.findByFechaInicio(LocalDate.of(2025, 6, 12));
        assertEquals(1, resultados.size());
        assertEquals("Proyecto B", resultados.get(0).getNombre());
    }

    @Test
    void testFindByFechaFinAfter() {
        proyecto1.setFechaFin(LocalDate.of(2025, 12, 31));
        proyectoRepository.save(proyecto1);

        Proyecto proyecto2 = proyectoRepository.findByNombre("Proyecto B").orElseThrow();
        proyecto2.setFechaFin(LocalDate.of(2024, 6, 30));
        proyectoRepository.save(proyecto2);

        List<Proyecto> resultados = proyectoRepository.findByFechaFinAfter(LocalDate.of(2024, 12, 31));

        assertEquals(1, resultados.size());
        assertEquals("Proyecto A", resultados.get(0).getNombre());
    }
}