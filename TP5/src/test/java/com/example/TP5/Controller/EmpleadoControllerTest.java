package com.example.TP5.Controller;
import com.example.TP5.Controladores.EmpleadoController;
import com.example.TP5.Entidades.Empleado;
import com.example.TP5.Servicios.EmpleadoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import static org.hamcrest.Matchers.*;


@WebMvcTest(controllers = EmpleadoController.class)
public class EmpleadoControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EmpleadoService empleadoService;


    @Test
    void cuandoCrearEmpleado_entoncesRetornaEmpleado() throws Exception {
        Empleado empleado = new Empleado();
        empleado.setId(1L);
        empleado.setNombre("Lucia");
        empleado.setApellido("Perez");
        empleado.setEmail("lucia.perez@example.com");

        when(empleadoService.guardar(any(Empleado.class))).thenReturn(empleado);

        mockMvc.perform(post("/api/empleados")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(empleado)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Lucia"))
                .andExpect(jsonPath("$.apellido").value("Perez"))
                .andExpect(jsonPath("$.email").value("lucia.perez@example.com"));
    }

    @Test
    void cuandoObtenerEmpleadoPorId_entoncesRetornaEmpleado() throws Exception {
        Long id = 1L;
        Empleado empleado = new Empleado();
        empleado.setId(id);
        empleado.setNombre("Carlos");
        empleado.setApellido("Gomez");
        empleado.setEmail("carlos.gomez@gmail.com");
        empleado.setFechaContratacion(LocalDate.now());
        empleado.setSalario(new BigDecimal("50000.00"));
        when(empleadoService.buscarPorId(id)).thenReturn(empleado);
        mockMvc.perform(get("/api/empleados/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.nombre").value("Carlos"))
                .andExpect(jsonPath("$.apellido").value("Gomez"))
                .andExpect(jsonPath("$.email").value("carlos.gomez@gmail.com"));
    }
    @Test
    void obtenerTodosDebeRetornarListaDeEmpleados() throws Exception {
        Empleado empleado1 = new Empleado();
        empleado1.setId(1L);
        empleado1.setNombre("pepe");

        Empleado empleado2 = new Empleado();
        empleado2.setId(2L);
        empleado2.setNombre("carla");

        List<Empleado> empleados = List.of(empleado1, empleado2);
        when(empleadoService.obtenerTodos()).thenReturn(empleados);
        mockMvc.perform(get("/api/empleados"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].nombre", is("pepe")))
                .andExpect(jsonPath("$[1].nombre", is("carla")));
    }
    @Test
    void eliminarEmpleado() throws Exception {
        Long id = 1L;
        mockMvc.perform(delete("/api/empleados/{id}", id))
                .andExpect(status().isNoContent());
        verify(empleadoService).eliminar(id);
    }
    @Test
    void obtenerPorDepartamento_deberiaRetornarUnEmpleado() throws Exception {
        String departamentoNombre = "Ventas";

        Empleado empleado = new Empleado();
        empleado.setId(1L);
        empleado.setNombre("Ana");
        empleado.setApellido("Lopez");

        List<Empleado> empleados = List.of(empleado);
        when(empleadoService.buscarPorDepartamento(departamentoNombre)).thenReturn(empleados);
        mockMvc.perform(get("/api/empleados/departamento/{nombre}", departamentoNombre))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].nombre", is("Ana")));
    }
    @Test
    void actualizarEmpleado_deberiaRetornarEmpleadoActualizado() throws Exception {
        Long id = 1L;
        Empleado empleadoActualizado = new Empleado();
        empleadoActualizado.setId(id);
        empleadoActualizado.setNombre("Juan");
        String empleadoJson = objectMapper.writeValueAsString(empleadoActualizado);
        when(empleadoService.actualizar(eq(id), any(Empleado.class))).thenReturn(empleadoActualizado);
        mockMvc.perform(put("/api/empleados/{id}", id)
                        .contentType("application/json")
                        .content(empleadoJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.nombre").value("Juan"));
    }
    @Test
    void obtenerPorRangoSalario_simple() throws Exception {
        BigDecimal min = new BigDecimal("30000");
        BigDecimal max = new BigDecimal("70000");
        Empleado empleado = new Empleado();
        empleado.setNombre("Ana");

        when(empleadoService.buscarPorRangoSalario(min, max)).thenReturn(List.of(empleado));
        mockMvc.perform(get("/api/empleados/salario")
                        .param("min", min.toString())
                        .param("max", max.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Ana"));
    }
}