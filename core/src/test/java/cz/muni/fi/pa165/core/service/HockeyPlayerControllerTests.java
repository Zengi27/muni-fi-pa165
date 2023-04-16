package cz.muni.fi.pa165.core.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import cz.muni.fi.pa165.model.dto.HockeyPlayerDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class HockeyPlayerControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    private HockeyPlayerService hockeyPlayerService;

    private final HockeyPlayerDto mockHockeyPlayerDto = new HockeyPlayerDto(1L, "Miroslav", "Satan",
            Instant.parse("1974-10-22T13:13:13.715Z"), "winger", 99, 99, 99,
            99, 99, 99, null);
    private final HockeyPlayerDto mockHockeyPlayerDtoUpdated = new HockeyPlayerDto(1L, "Miroslav", "Satan",
            Instant.parse("1974-10-22T13:13:13.715Z"), "center", 66, 66, 66,
            66, 66, 66, null);

    @Test
    void getAll() throws Exception {
        when(hockeyPlayerService.findAll()).thenReturn(List.of(mockHockeyPlayerDto));

        String response = mockMvc.perform(get("/api/hockey-players")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        List<HockeyPlayerDto> hockeyPlayers = objectMapper.readValue(response, new TypeReference<>(){});

        assertThat(hockeyPlayers.size()).isNotEqualTo(0);
    }

    @Test
    void getAllWithoutTeam() throws Exception {
        when(hockeyPlayerService.getAllWithoutTeam()).thenReturn(List.of(mockHockeyPlayerDto));

        String response = mockMvc.perform(get("/api/hockey-players/get-all-without-team")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        List<HockeyPlayerDto> hockeyPlayers = objectMapper.readValue(response, new TypeReference<>(){});

        assertThat(hockeyPlayers.size()).isNotEqualTo(0);
        hockeyPlayers.stream().map(h -> assertThat(h.teamDto()).isEqualTo(null));
    }

    @Test
    void createHockeyPlayerValid() throws Exception {
        when(hockeyPlayerService.create(mockHockeyPlayerDto)).thenReturn(mockHockeyPlayerDto);

        String response = mockMvc.perform(post("/api/hockey-players").contentType("application/json")
                        .content(objectMapper.writeValueAsString(mockHockeyPlayerDto)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        HockeyPlayerDto hockeyPlayerDto = objectMapper.readValue(response, HockeyPlayerDto.class);

        assertThat(hockeyPlayerDto).isEqualTo(mockHockeyPlayerDto);
    }

    @Test
    void updateHockeyPlayerValid() throws Exception {
        when(hockeyPlayerService.update(mockHockeyPlayerDtoUpdated)).thenReturn(mockHockeyPlayerDtoUpdated);

        String response = mockMvc.perform(put("/api/hockey-players").contentType("application/json")
                        .content(objectMapper.writeValueAsString(mockHockeyPlayerDtoUpdated)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        HockeyPlayerDto hockeyPlayerDto = objectMapper.readValue(response, HockeyPlayerDto.class);

        assertThat(hockeyPlayerDto).isEqualTo(mockHockeyPlayerDtoUpdated);
    }

    @Test
    void updateHockeyPlayerInvalid() throws Exception {
        when(hockeyPlayerService.update(mockHockeyPlayerDto)).thenThrow(new IllegalArgumentException());

        mockMvc.perform(put("/api/hockey-players").contentType("application/json")
                        .content(objectMapper.writeValueAsString(mockHockeyPlayerDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void findByIdValid() throws Exception {
        when(hockeyPlayerService.findById(1L)).thenReturn(mockHockeyPlayerDto);

        String response = mockMvc.perform(get("/api/hockey-players/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        HockeyPlayerDto hockeyPlayer = objectMapper.readValue(response, HockeyPlayerDto.class);

        assertThat(hockeyPlayer).isEqualTo(mockHockeyPlayerDto);
    }

    @Test
    void findByIdInvalid() throws Exception {
        when(hockeyPlayerService.findById(-1L)).thenThrow(new IllegalArgumentException());

        mockMvc.perform(get("/api/hockey-players/-1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteByIdValid() throws Exception {
        mockMvc.perform(delete("/api/hockey-players/1"))
                .andExpect(status().isOk());
    }

    @Test
    void deleteByIdInvalid() throws Exception {
        doThrow(new IllegalArgumentException()).when(hockeyPlayerService).deleteById(-1L);

        mockMvc.perform(delete("/api/hockey-players/-1"))
                .andExpect(status().isNotFound());
    }
}
