package com.example.TP5.Repository.Empleado;
import com.example.TP5.Entidades.Departamento;
import com.example.TP5.Entidades.Empleado;
import com.example.TP5.Repository.DepartamentoRepository;
import com.example.TP5.Repository.EmpleadoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@DataJpaTest
@ActiveProfiles("mysql")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
public class EmpleadoRepositoryMySqlTest {
    @Autowired
    private EmpleadoRepository empleadoRepository;

    @Autowired
    private DepartamentoRepository departamentoRepository;

    private Departamento departamentoIT;


    @BeforeEach
    void setUp() {

        departamentoIT = new Departamento();
        departamentoIT.setNombre("IT");
        departamentoIT = departamentoRepository.save(departamentoIT);


        Empleado empleado = new Empleado();
        empleado.setNombre("Juan");
        empleado.setApellido("Pérez");
        empleado.setEmail("juan.perez@example.com");
        empleado.setSalario(new BigDecimal("5000"));
        empleado.setFechaContratacion(java.time.LocalDate.now());
        empleado.setDepartamento(departamentoIT);
        empleadoRepository.save(empleado);

        Empleado empleado2 = new Empleado();
        empleado2.setNombre("Alice");
        empleado2.setApellido("Ben");
        empleado2.setEmail("bean@example.com");
        empleado2.setSalario(new BigDecimal("2000"));
        empleado2.setFechaContratacion(java.time.LocalDate.now());
        empleado2.setDepartamento(departamentoIT);
        empleadoRepository.save(empleado2);

        Empleado empleado3 = new Empleado();
        empleado3.setNombre("Bob");
        empleado3.setApellido("Perez");
        empleado3.setEmail("perez@example.com");
        empleado3.setSalario(new BigDecimal("3000"));
        empleado3.setFechaContratacion(java.time.LocalDate.now());
        empleado3.setDepartamento(departamentoIT);
        empleadoRepository.save(empleado3);
    }


    @Test
    void testFindByEmail() {

        Optional<Empleado> encontrado = empleadoRepository.findByEmail("juan.perez@example.com");

        assertTrue(encontrado.isPresent());
        assertEquals("juan.perez@example.com", encontrado.get().getEmail());
    }
    @Test
    void testBuscarPorDepartamento() {
        List<Empleado> encontrados = empleadoRepository.findByDepartamento(departamentoIT);

        assertFalse(encontrados.isEmpty());
        assertEquals("IT", encontrados.get(0).getDepartamento().getNombre());
    }
    @Test
    void testFindBySalarioBetween() {
        List<Empleado> resultado = empleadoRepository.findBySalarioBetween(new BigDecimal("1800"), new BigDecimal("3200"));

        assertThat(resultado).hasSize(2)
                .extracting(Empleado::getNombre)
                .containsExactlyInAnyOrder("Alice", "Bob");
    }
    @Test
    void testFindByFechaContratacionAfter() {
        LocalDate ayer = LocalDate.now().minusDays(1);
        List<Empleado> empleadosRecientes = empleadoRepository.findByFechaContratacionAfter(ayer);
        assertFalse(empleadosRecientes.isEmpty());
        assertThat(empleadosRecientes).hasSize(3)
                .extracting(Empleado::getNombre)
                .containsExactlyInAnyOrder("Juan", "Alice", "Bob");
    }
    @Test
    void testFindAverageSalarioByDepartamento() {
        Optional<BigDecimal> promedio = empleadoRepository.findAverageSalarioByDepartamento(departamentoIT.getId());
        assertTrue(promedio.isPresent());
        BigDecimal suma = new BigDecimal("5000").add(new BigDecimal("2000")).add(new BigDecimal("3000"));
        BigDecimal esperado = suma.divide(BigDecimal.valueOf(3), 2, RoundingMode.HALF_UP);
        BigDecimal valorPromedio = promedio.get().setScale(2, RoundingMode.HALF_UP);
        assertEquals(esperado, valorPromedio);
    }
}