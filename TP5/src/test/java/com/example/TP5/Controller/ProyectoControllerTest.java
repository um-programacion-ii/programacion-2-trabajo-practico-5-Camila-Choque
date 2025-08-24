package com.example.TP5.Controller;
import com.example.TP5.Controladores.ProyectoController;
import com.example.TP5.Entidades.Proyecto;
import com.example.TP5.Servicios.ProyectoService;
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

@WebMvcTest(controllers = ProyectoController.class)
public class ProyectoControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProyectoService proyectoService;

    @Test
    void cuandoCrearProyecto_entoncesRetornaProyecto() throws Exception {
        Proyecto proyecto = new Proyecto();
        proyecto.setId(1L);
        proyecto.setNombre("Proyecto1");

        when(proyectoService.guardar(any(Proyecto.class))).thenReturn(proyecto);

        mockMvc.perform(post("/api/proyecto")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(proyecto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Proyecto1"));
    }
    @Test
    void cuandoObtenerProyectoPorId_entoncesRetornaProyecto() throws Exception {
        Long id = 1L;
        Proyecto proyecto = new Proyecto();
        proyecto.setId(id);
        proyecto.setNombre("Proyecto2");
        when(proyectoService.buscarPorId(id)).thenReturn(proyecto);
        mockMvc.perform(get("/api/proyecto/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.nombre").value("Proyecto2"));
    }
    @Test
    void obtenerTodosDebeRetornarListaDeProyectos() throws Exception {
        Proyecto proyecto1 = new Proyecto();
        proyecto1.setId(1L);
        proyecto1.setNombre("Proyecto1");

        Proyecto proyecto2 = new Proyecto();
        proyecto2.setId(2L);
        proyecto2.setNombre("Proyecto2");

        List<Proyecto> proyectos = List.of(proyecto1, proyecto2);
        when(proyectoService.obtenerTodos()).thenReturn(proyectos);
        mockMvc.perform(get("/api/proyecto"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].nombre", is("Proyecto1")))
                .andExpect(jsonPath("$[1].nombre", is("Proyecto2")));
    }
    @Test
    void eliminarProyecto() throws Exception {
        Long id = 1L;
        mockMvc.perform(delete("/api/proyecto/{id}", id))
                .andExpect(status().isNoContent());
        verify(proyectoService).eliminar(id);
    }
    @Test
    void actualizarProyecto_deberiaRetornarProyectoActualizado() throws Exception {
        Long id = 1L;
        Proyecto proyectoActualizado = new Proyecto();
        proyectoActualizado.setId(id);
        proyectoActualizado.setNombre("Proyecto.1.1");
        String proyectoJson = objectMapper.writeValueAsString(proyectoActualizado);
        when(proyectoService.actualizar(eq(id), any(Proyecto.class))).thenReturn(proyectoActualizado);
        mockMvc.perform(put("/api/proyecto/{id}", id)
                        .contentType("application/json")
                        .content(proyectoJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.nombre").value("Proyecto.1.1"));
    }
}