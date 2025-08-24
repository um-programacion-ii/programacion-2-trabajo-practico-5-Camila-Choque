package com.example.TP5.Servicios;
import com.example.TP5.Entidades.Departamento;
import com.example.TP5.Entidades.Empleado;
import com.example.TP5.Excepciones.EmpleadoNoEncontradoException;
import com.example.TP5.Repository.DepartamentoRepository;
import com.example.TP5.Repository.EmpleadoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class EmpleadoServiceIntegrationTest {

    @Autowired
    private  EmpleadoService empleadoService;

    @Autowired
    private  EmpleadoRepository empleadoRepository;

    @Autowired
    private  DepartamentoRepository departamentoRepository;

    Empleado crearEmpleadoDePrueba(){
        Empleado empleado = new Empleado();
        empleado.setNombre("Carlos");
        empleado.setApellido("Gomez");
        empleado.setEmail("carlos.gomez@gmail.com");
        empleado.setFechaContratacion(LocalDate.now());
        empleado.setSalario(new BigDecimal("500000.00"));
       return empleado;
    }
    Departamento crearDepartamentoDePrueba(){
        Departamento departamento = new Departamento();
        departamento.setNombre("IT");
        departamento.setDescripcion("Departamento de Tecnología");
        return departamento;

    }
    @Test
    void guardarEmpleado_entoncesSePersisteCorrectamente() {

        Departamento departamento = crearDepartamentoDePrueba();
        departamento = departamentoRepository.save(departamento);

        Empleado empleado = crearEmpleadoDePrueba();
        empleado.setDepartamento(departamento);
        Empleado empleadoGuardado = empleadoService.guardar(empleado);
        assertNotNull(empleadoGuardado.getId());
        assertEquals("carlos.gomez@gmail.com", empleadoGuardado.getEmail());
        assertTrue(empleadoRepository.existsById(empleadoGuardado.getId()));
    }

    @Test
    void buscarPorEmailExistente_entoncesRetornaEmpleado() {

        Empleado empleado = crearEmpleadoDePrueba();
        empleadoRepository.save(empleado);

        Optional<Empleado> resultado = empleadoRepository.findByEmail("carlos.gomez@gmail.com");

        assertTrue(resultado.isPresent());
        assertEquals("carlos.gomez@gmail.com", resultado.get().getEmail());
    }

    @Test
    void buscarPorIdExistente_entoncesDevuelveEmpleado() {

        Departamento departamento = crearDepartamentoDePrueba();
        departamento = departamentoRepository.save(departamento);

        Empleado empleado = crearEmpleadoDePrueba();
        empleado.setDepartamento(departamento);
        empleado = empleadoService.guardar(empleado);

        Empleado encontrado = empleadoService.buscarPorId(empleado.getId());

        assertNotNull(encontrado);
        assertEquals(empleado.getEmail(), encontrado.getEmail());
    }
    @Test
    void buscarPorIdInexistente_entoncesLanzaExcepcion() {
        Long idInexistente = 999L;

        EmpleadoNoEncontradoException excepcion = assertThrows(
                EmpleadoNoEncontradoException.class,
                () -> empleadoService.buscarPorId(idInexistente)
        );

        assertEquals("Empleado no encontrado con ID: " + idInexistente, excepcion.getMessage());
    }
    @Test
    void buscarPorDepartamento_entoncesDevuelveListaEmpleados() {

        Departamento departamento = crearDepartamentoDePrueba();
        departamento = departamentoRepository.save(departamento);

        Empleado empleado1 = crearEmpleadoDePrueba();
        empleado1.setEmail("empleado1@empresa.com");
        empleado1.setDepartamento(departamento);
        empleadoService.guardar(empleado1);

        Empleado empleado2 = crearEmpleadoDePrueba();
        empleado2.setEmail("empleado2@empresa.com");
        empleado2.setDepartamento(departamento);
        empleadoService.guardar(empleado2);


        List<Empleado> empleados = empleadoService.buscarPorDepartamento(departamento.getNombre());

        assertNotNull(empleados);
        assertFalse(empleados.isEmpty());
        assertEquals(2, empleados.size());
        assertTrue(empleados.stream().anyMatch(e -> e.getEmail().equals("empleado1@empresa.com")));
        assertTrue(empleados.stream().anyMatch(e -> e.getEmail().equals("empleado2@empresa.com")));
    }
    @Test
    void obtenerTodos_entoncesDevuelveListaCompleta() {

        Departamento departamento = crearDepartamentoDePrueba();
        departamento = departamentoRepository.save(departamento);

        Empleado empleado1 = crearEmpleadoDePrueba();
        empleado1.setEmail("empleado1@empresa.com");
        empleado1.setDepartamento(departamento);
        empleadoService.guardar(empleado1);

        Empleado empleado2 = crearEmpleadoDePrueba();
        empleado2.setEmail("empleado2@empresa.com");
        empleado2.setDepartamento(departamento);
        empleadoService.guardar(empleado2);

        List<Empleado> empleados = empleadoService.obtenerTodos();

        assertNotNull(empleados);
        assertTrue(empleados.size() >= 2); // Puede haber más si la BD no está limpia
        assertTrue(empleados.stream().anyMatch(e -> e.getEmail().equals("empleado1@empresa.com")));
        assertTrue(empleados.stream().anyMatch(e -> e.getEmail().equals("empleado2@empresa.com")));
    }
    @Test
    void eliminarEmpleadoExistente_entoncesSeEliminaCorrectamente() {

        Departamento departamento = crearDepartamentoDePrueba();
        departamento = departamentoRepository.save(departamento);
        Empleado empleado = crearEmpleadoDePrueba();
        empleado.setEmail("empleado.eliminar@empresa.com");
        empleado.setDepartamento(departamento);
        empleado = empleadoService.guardar(empleado);
        Long id = empleado.getId();
        assertTrue(empleadoRepository.existsById(id));

        empleadoService.eliminar(id);

        assertFalse(empleadoRepository.existsById(id));
    }
    @Test
    void eliminarEmpleadoInexistente_entoncesLanzaExcepcion() {

        Long idInexistente = 999L;

        EmpleadoNoEncontradoException excepcion = assertThrows(
                EmpleadoNoEncontradoException.class,
                () -> empleadoService.eliminar(idInexistente)
        );
        assertEquals("Empleado no encontrado con ID: " + idInexistente, excepcion.getMessage());
    }
    @Test
    void buscarPorRangoSalario_entoncesDevuelveEmpleadosDentroDelRango() {

        Departamento departamento = crearDepartamentoDePrueba();
        departamento = departamentoRepository.save(departamento);

        Empleado empleado1 = crearEmpleadoDePrueba();
        empleado1.setEmail("empleado1@empresa.com");
        empleado1.setSalario(new BigDecimal("40000"));
        empleado1.setDepartamento(departamento);
        empleadoService.guardar(empleado1);

        Empleado empleado2 = crearEmpleadoDePrueba();
        empleado2.setEmail("empleado2@empresa.com");
        empleado2.setSalario(new BigDecimal("60000"));
        empleado2.setDepartamento(departamento);
        empleadoService.guardar(empleado2);

        Empleado empleado3 = crearEmpleadoDePrueba();
        empleado3.setEmail("empleado3@empresa.com");
        empleado3.setSalario(new BigDecimal("90000"));
        empleado3.setDepartamento(departamento);
        empleadoService.guardar(empleado3);

        BigDecimal min = new BigDecimal("45000");
        BigDecimal max = new BigDecimal("95000");
        List<Empleado> empleadosFiltrados = empleadoService.buscarPorRangoSalario(min, max);

        assertNotNull(empleadosFiltrados);
        assertEquals(2, empleadosFiltrados.size());
        assertTrue(empleadosFiltrados.stream().anyMatch(e -> e.getEmail().equals("empleado2@empresa.com")));
        assertTrue(empleadosFiltrados.stream().anyMatch(e -> e.getEmail().equals("empleado3@empresa.com")));
    }
}