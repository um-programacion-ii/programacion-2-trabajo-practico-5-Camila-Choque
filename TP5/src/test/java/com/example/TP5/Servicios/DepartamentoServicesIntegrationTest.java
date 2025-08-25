package com.example.TP5.Servicios;
import com.example.TP5.Entidades.Departamento;
import com.example.TP5.Excepciones.DepartamentoNoEncontradoException;
import com.example.TP5.Repository.DepartamentoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class DepartamentoServicesIntegrationTest {
    @Autowired
    private DepartamentoService departamentoService;
    @Autowired
    private DepartamentoRepository departamentoRepository;

    Departamento crearDepartamentoDePrueba(){
        Departamento departamento = new Departamento();
        departamento.setNombre("IT");
        departamento.setDescripcion("Departamento de Tecnología");
        return departamento;

    }

    @Test
    void guardarDepartamento_entoncesSePersisteCorrectamente() {

        Departamento departamento = crearDepartamentoDePrueba();
        Departamento guardado = departamentoService.guardar(departamento);
        assertNotNull(guardado.getId());
        assertEquals(departamento.getNombre(), guardado.getNombre());
        assertEquals(departamento.getDescripcion(), guardado.getDescripcion());
        assertTrue(departamentoRepository.existsById(guardado.getId()));
    }
    @Test
    void buscarPorIdExistente_entoncesDevuelveDepartamento() {

        Departamento departamento = crearDepartamentoDePrueba();
        departamento = departamentoRepository.save(departamento);
        Departamento encontrado = departamentoService.buscarPorId(departamento.getId());
        assertNotNull(encontrado);
        assertEquals(departamento.getNombre(), encontrado.getNombre());
        assertEquals(departamento.getDescripcion(), encontrado.getDescripcion());
    }
    @Test
    void buscarPorIdInexistente_entoncesLanzaExcepcion() {

        Long idInexistente = 999L;
        DepartamentoNoEncontradoException ex = assertThrows(
                DepartamentoNoEncontradoException.class,
                () -> departamentoService.buscarPorId(idInexistente)
        );
        assertEquals("Departamento no encontrado con ID: " + idInexistente, ex.getMessage());
    }
    @Test
    void obtenerTodosDepartamentos_entoncesDevuelveLista() {

        Departamento dep1 = crearDepartamentoDePrueba();
        dep1.setNombre("IT");
        departamentoRepository.save(dep1);
        Departamento dep2 = crearDepartamentoDePrueba();
        dep2.setNombre("RRHH");
        departamentoRepository.save(dep2);
        List<Departamento> lista = departamentoService.obtenerTodos();
        assertNotNull(lista);
        assertTrue(lista.size() >= 2);
        assertTrue(lista.stream().anyMatch(d -> d.getNombre().equals("IT")));
        assertTrue(lista.stream().anyMatch(d -> d.getNombre().equals("RRHH")));
    }
    @Test
    void actualizarDepartamentoExistente_entoncesSeActualizaCorrectamente() {

        Departamento original = crearDepartamentoDePrueba();
        original.setNombre("Ventas");
        original = departamentoRepository.save(original);
        Departamento actualizado = new Departamento();
        actualizado.setNombre("Ventas Internacionales");
        actualizado.setDescripcion("Exportaciones y ventas globales");
        Departamento resultado = departamentoService.actualizar(original.getId(), actualizado);
        assertEquals("Ventas Internacionales", resultado.getNombre());
        assertEquals("Exportaciones y ventas globales", resultado.getDescripcion());
        assertEquals(original.getId(), resultado.getId());
    }
    @Test
    void actualizarDepartamentoInexistente_entoncesLanzaExcepcion() {

        Long idInexistente = 999L;
        Departamento nuevo = crearDepartamentoDePrueba();
        DepartamentoNoEncontradoException ex = assertThrows(
                DepartamentoNoEncontradoException.class,
                () -> departamentoService.actualizar(idInexistente, nuevo)
        );
        assertEquals("Departamento no encontrado con ID: " + idInexistente, ex.getMessage());
    }
    @Test
    void eliminarDepartamentoExistente_entoncesSeEliminaCorrectamente() {

        Departamento departamento = crearDepartamentoDePrueba();
        departamento = departamentoRepository.save(departamento);
        Long id = departamento.getId();
        assertTrue(departamentoRepository.existsById(id));
        departamentoService.eliminar(id);
        assertFalse(departamentoRepository.existsById(id));
    }
    @Test
    void eliminarDepartamentoInexistente_entoncesLanzaExcepcion() {

        Long idInexistente = 999L;
        DepartamentoNoEncontradoException ex = assertThrows(
                DepartamentoNoEncontradoException.class,
                () -> departamentoService.eliminar(idInexistente)
        );
        assertEquals("Departamento no encontrado con ID: " + idInexistente, ex.getMessage());
    }
}