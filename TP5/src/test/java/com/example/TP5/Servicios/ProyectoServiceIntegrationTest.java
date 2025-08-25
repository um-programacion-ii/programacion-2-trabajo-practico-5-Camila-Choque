package com.example.TP5.Servicios;
import com.example.TP5.Entidades.Departamento;
import com.example.TP5.Entidades.Empleado;
import com.example.TP5.Entidades.Proyecto;
import com.example.TP5.Excepciones.ProyectoNoEncontradoException;
import com.example.TP5.Repository.DepartamentoRepository;
import com.example.TP5.Repository.EmpleadoRepository;
import com.example.TP5.Repository.ProyectoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class ProyectoServiceIntegrationTest {
    @Autowired
    private ProyectoService proyectoService;
    @Autowired
    private ProyectoRepository proyectoRepository;
    @Autowired
    private EmpleadoRepository empleadoRepository;
    @Autowired
    private DepartamentoRepository departamentoRepository;

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

    public Proyecto crearProyectoDePrueba(Set<Empleado> empleados) {
        Proyecto proyecto = new Proyecto();
        proyecto.setNombre("Nuevo Sistema");
        proyecto.setDescripcion("Implementación de un nuevo sistema de gestión");
        proyecto.setFechaInicio(LocalDate.now());
        proyecto.setFechaFin(LocalDate.now().plusMonths(6));
        proyecto.setEmpleados(new HashSet<>(empleados));
        return proyecto;
    }
    @Test
    void guardarProyecto_entoncesSePersisteCorrectamente() {

        Departamento departamento = departamentoRepository.save(crearDepartamentoDePrueba());
        Empleado empleado = crearEmpleadoDePrueba();
        empleado.setDepartamento(departamento);
        empleado = empleadoRepository.save(empleado);
        Set<Empleado> empleados = new HashSet<>();
        empleados.add(empleado);
        Proyecto proyecto = crearProyectoDePrueba(empleados);
        Proyecto proyectoGuardado = proyectoService.guardar(proyecto);
        assertNotNull(proyectoGuardado.getId());
        assertEquals("Nuevo Sistema", proyectoGuardado.getNombre());
        assertEquals(1, proyectoGuardado.getEmpleados().size());
        assertTrue(proyectoGuardado.getEmpleados().stream()
                .anyMatch(e -> e.getEmail().equals("carlos.gomez@gmail.com")));
    }
    @Test
    void buscarProyectoPorIdExistente_entoncesRetornaProyecto() {

        Departamento departamento = departamentoRepository.save(crearDepartamentoDePrueba());
        Empleado empleado = crearEmpleadoDePrueba();
        empleado.setDepartamento(departamento);
        empleado = empleadoRepository.save(empleado);
        Set<Empleado> empleados = new HashSet<>();
        empleados.add(empleado);
        Proyecto proyecto = crearProyectoDePrueba(empleados);
        proyecto = proyectoRepository.save(proyecto);
        Proyecto encontrado = proyectoService.buscarPorId(proyecto.getId());
        assertNotNull(encontrado);
        assertEquals(proyecto.getNombre(), encontrado.getNombre());
    }
    @Test
    void buscarProyectoPorIdInexistente_entoncesLanzaExcepcion() {

        Long idInexistente = 999L;
        assertThrows(ProyectoNoEncontradoException.class, () -> {
            proyectoService.buscarPorId(idInexistente);
        });
    }
    @Test
    void obtenerTodosLosProyectos_entoncesRetornaLista() {

        Departamento departamento = departamentoRepository.save(crearDepartamentoDePrueba());
        Empleado empleado = crearEmpleadoDePrueba();
        empleado.setDepartamento(departamento);
        empleado = empleadoRepository.save(empleado);
        Set<Empleado> empleados = Set.of(empleado);
        Proyecto proyecto1 = proyectoRepository.save(crearProyectoDePrueba(empleados));
        Proyecto proyecto2 = proyectoRepository.save(crearProyectoDePrueba(empleados));
        List<Proyecto> proyectos = proyectoService.obtenerTodos();
        assertEquals(2, proyectos.size());
    }
    @Test
    void eliminarProyectoExistente_entoncesSeEliminaCorrectamente() {

        Departamento departamento = departamentoRepository.save(crearDepartamentoDePrueba());
        Empleado empleado = crearEmpleadoDePrueba();
        empleado.setDepartamento(departamento);
        empleado = empleadoRepository.save(empleado);
        Set<Empleado> empleados = Set.of(empleado);
        Proyecto proyecto = proyectoRepository.save(crearProyectoDePrueba(empleados));
        Long id = proyecto.getId();
        proyectoService.eliminar(id);
        assertFalse(proyectoRepository.existsById(id));
    }
    @Test
    void eliminarProyectoInexistente_entoncesLanzaExcepcion() {
        Long idInexistente = 999L;
        assertThrows(ProyectoNoEncontradoException.class, () -> {
            proyectoService.eliminar(idInexistente);
        });
    }
}