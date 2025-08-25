package com.example.TP5.Repository.Departamento;
import com.example.TP5.Entidades.Departamento;
import com.example.TP5.Repository.DepartamentoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@ActiveProfiles("mysql")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
public class DepartamentoRepositoryMySqlTest {
    @Autowired
    private DepartamentoRepository departamentoRepository;

    private Departamento departamentoIT;

    @BeforeEach
    void setUp() {
        departamentoIT = new Departamento();
        departamentoIT.setNombre("IT");
        departamentoIT = departamentoRepository.save(departamentoIT);

        Departamento departamentoHR = new Departamento();
        departamentoHR.setNombre("HR");
        departamentoRepository.save(departamentoHR);
    }

    @Test
    void testSaveAndFindById() {
        Optional<Departamento> encontrado = departamentoRepository.findById(departamentoIT.getId());
        assertTrue(encontrado.isPresent());
        assertEquals("IT", encontrado.get().getNombre());
    }

    @Test
    void testFindAll() {
        List<Departamento> departamentos = departamentoRepository.findAll();
        assertFalse(departamentos.isEmpty());
        assertThat(departamentos).extracting(Departamento::getNombre)
                .containsExactlyInAnyOrder("IT", "HR");
    }

    @Test
    void testDelete() {
        departamentoRepository.delete(departamentoIT);
        Optional<Departamento> encontrado = departamentoRepository.findById(departamentoIT.getId());
        assertFalse(encontrado.isPresent());
    }
    @Test
    void testFindByNombre() {
        Optional<Departamento> encontrado = departamentoRepository.findByNombre("HR");
        assertTrue(encontrado.isPresent());
        assertEquals("HR", encontrado.get().getNombre());
    }
}