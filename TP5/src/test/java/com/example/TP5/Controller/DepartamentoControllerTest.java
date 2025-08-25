package com.example.TP5.Controller;
import com.example.TP5.Controladores.DepartamentoController;
import com.example.TP5.Entidades.Departamento;
import com.example.TP5.Servicios.DepartamentoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = DepartamentoController.class)
public class DepartamentoControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private DepartamentoService departamentoService;

    @Test
    void cuandoCrearDepartamento_entoncesRetornaDepartamento() throws Exception {
        Departamento departamento = new Departamento();
        departamento.setId(1L);
        departamento.setNombre("Tecnologia");
        departamento.setDescripcion("lala");

        when(departamentoService.guardar(any(Departamento.class))).thenReturn(departamento);

        mockMvc.perform(post("/api/departamento")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(departamento)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Tecnologia"))
                .andExpect(jsonPath("$.descripcion").value("lala"));
    }
    @Test
    void cuandoObtenerDepartamentoPorId_entoncesRetornaDepartamento() throws Exception {
        Long id = 1L;
        Departamento departamento = new Departamento();
        departamento.setId(id);
        departamento.setNombre("Tecnologia");
        when(departamentoService.buscarPorId(id)).thenReturn(departamento);
        mockMvc.perform(get("/api/departamento/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.nombre").value("Tecnologia"));
    }

    @Test
    void obtenerTodosDebeRetornarListaDeDepartamentos() throws Exception {
        Departamento departamento1 = new Departamento();
        departamento1.setId(1L);
        departamento1.setNombre("Servicios");

        Departamento departamento2 = new Departamento();
        departamento2.setId(2L);
        departamento2.setNombre("Comunicacion");

        List<Departamento> departamentos = List.of(departamento1, departamento2);
        when(departamentoService.obtenerTodos()).thenReturn(departamentos);
        mockMvc.perform(get("/api/departamento"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].nombre", is("Servicios")))
                .andExpect(jsonPath("$[1].nombre", is("Comunicacion")));
    }
    @Test
    void eliminarDepartamento() throws Exception {
        Long id = 1L;
        mockMvc.perform(delete("/api/departamento/{id}", id))
                .andExpect(status().isNoContent());
        verify(departamentoService).eliminar(id);
    }
    @Test
    void actualizarDepartamento_deberiaRetornarDepartamentoActualizado() throws Exception {
        Long id = 1L;
        Departamento departamentoActualizado = new Departamento();
        departamentoActualizado.setId(id);
        departamentoActualizado.setNombre("Tecnologia");
        String departamentoJson = objectMapper.writeValueAsString(departamentoActualizado);
        when(departamentoService.actualizar(eq(id), any(Departamento.class))).thenReturn(departamentoActualizado);
        mockMvc.perform(put("/api/departamento/{id}", id)
                        .contentType("application/json")
                        .content(departamentoJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.nombre").value("Tecnologia"));
    }
}