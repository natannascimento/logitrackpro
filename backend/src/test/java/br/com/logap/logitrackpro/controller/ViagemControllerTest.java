package br.com.logap.logitrackpro.controller;

import static org.hamcrest.Matchers.greaterThan;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.logap.logitrackpro.AbstractIntegrationTest;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser
class ViagemControllerTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private static final String VIAGEM_VALIDA = """
            {
              "veiculoId": 1,
              "dataSaida": "2026-07-20T08:00:00",
              "dataChegada": "2026-07-20T12:00:00",
              "origem": "Natal",
              "destino": "João Pessoa",
              "kmPercorrida": 180.5
            }
            """;

    private int criarViagemValida() throws Exception {
        String resposta = mockMvc.perform(post("/api/viagens")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(VIAGEM_VALIDA))
                .andReturn().getResponse().getContentAsString();
        JsonNode json = new ObjectMapper().readTree(resposta);
        return json.get("id").asInt();
    }

    @Nested
    class Listar {

        @Test
        void deveListarViagensDaCargaInicialQuandoBaseJaPossuiRegistros() throws Exception {
            // Arrange: a carga inicial (Flyway V1) já popula viagens de exemplo

            // Act
            var resultado = mockMvc.perform(get("/api/viagens"));

            // Assert
            resultado.andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()", greaterThan(0)));
        }
    }

    @Nested
    class BuscarPorId {

        @Test
        void deveRetornar404QuandoViagemNaoExiste() throws Exception {
            // Arrange
            int idInexistente = 999_999;

            // Act
            var resultado = mockMvc.perform(get("/api/viagens/" + idInexistente));

            // Assert
            resultado.andExpect(status().isNotFound());
        }
    }

    @Nested
    class Criar {

        @Test
        void deveCriarViagemQuandoDadosValidos() throws Exception {
            // Arrange
            String payload = VIAGEM_VALIDA;

            // Act
            var resultado = mockMvc.perform(post("/api/viagens")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(payload));

            // Assert
            resultado.andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").exists())
                    .andExpect(jsonPath("$.veiculo.id").value(1))
                    .andExpect(jsonPath("$.kmPercorrida").value(180.5));
        }

        @Test
        void deveRejeitarQuandoDataChegadaAnteriorADataSaida() throws Exception {
            // Arrange
            String payload = """
                    {
                      "veiculoId": 1,
                      "dataSaida": "2026-07-20T08:00:00",
                      "dataChegada": "2026-07-19T12:00:00",
                      "origem": "Natal",
                      "destino": "João Pessoa",
                      "kmPercorrida": 180.5
                    }
                    """;

            // Act
            var resultado = mockMvc.perform(post("/api/viagens")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(payload));

            // Assert
            resultado.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value("dataChegada não pode ser anterior a dataSaida"));
        }

        @Test
        void deveRejeitarQuandoKmPercorridaMenorOuIgualAZero() throws Exception {
            // Arrange
            String payload = """
                    {
                      "veiculoId": 1,
                      "dataSaida": "2026-07-20T08:00:00",
                      "dataChegada": "2026-07-20T12:00:00",
                      "origem": "Natal",
                      "destino": "João Pessoa",
                      "kmPercorrida": 0
                    }
                    """;

            // Act
            var resultado = mockMvc.perform(post("/api/viagens")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(payload));

            // Assert
            resultado.andExpect(status().isBadRequest());
        }

        @Test
        void deveRejeitarQuandoVeiculoNaoExiste() throws Exception {
            // Arrange
            String payload = """
                    {
                      "veiculoId": 999999,
                      "dataSaida": "2026-07-20T08:00:00",
                      "dataChegada": "2026-07-20T12:00:00",
                      "origem": "Natal",
                      "destino": "João Pessoa",
                      "kmPercorrida": 180.5
                    }
                    """;

            // Act
            var resultado = mockMvc.perform(post("/api/viagens")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(payload));

            // Assert
            resultado.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value("Veículo não encontrado: 999999"));
        }
    }

    @Nested
    class Atualizar {

        @Test
        void deveAtualizarQuandoViagemExisteEDadosValidos() throws Exception {
            // Arrange
            int id = criarViagemValida();
            String atualizacao = """
                    {
                      "veiculoId": 2,
                      "dataSaida": "2026-07-21T08:00:00",
                      "dataChegada": "2026-07-21T12:00:00",
                      "origem": "Natal",
                      "destino": "Fortaleza",
                      "kmPercorrida": 540
                    }
                    """;

            // Act
            var resultado = mockMvc.perform(put("/api/viagens/" + id)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(atualizacao));

            // Assert
            resultado.andExpect(status().isOk())
                    .andExpect(jsonPath("$.veiculo.id").value(2))
                    .andExpect(jsonPath("$.destino").value("Fortaleza"));
        }
    }

    @Nested
    class Excluir {

        @Test
        void deveExcluirQuandoViagemExiste() throws Exception {
            // Arrange
            int id = criarViagemValida();

            // Act
            var resultado = mockMvc.perform(delete("/api/viagens/" + id));

            // Assert
            resultado.andExpect(status().isNoContent());
            mockMvc.perform(get("/api/viagens/" + id))
                    .andExpect(status().isNotFound());
        }

        @Test
        void deveRetornar404QuandoViagemNaoExiste() throws Exception {
            // Arrange
            int idInexistente = 999_999;

            // Act
            var resultado = mockMvc.perform(delete("/api/viagens/" + idInexistente));

            // Assert
            resultado.andExpect(status().isNotFound());
        }
    }
}
