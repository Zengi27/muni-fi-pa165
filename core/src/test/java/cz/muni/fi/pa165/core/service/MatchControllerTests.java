package cz.muni.fi.pa165.core.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import cz.muni.fi.pa165.model.dto.LeagueDto;
import cz.muni.fi.pa165.model.dto.MatchDto;
import cz.muni.fi.pa165.model.dto.TeamDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class MatchControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    private MatchService matchService;

    LeagueDto mockLeague = new LeagueDto(1L, "TIPOS Extraliga", null);
    TeamDto mockHomeTeam = new TeamDto(2L, "Kosice", null, mockLeague, null);
    TeamDto mockAwayTeam = new TeamDto(3L, "Banska Bystrica", null, mockLeague, null);
    private final MatchDto mockMatchDto = new MatchDto(1L, Instant.now().minus(1, ChronoUnit.DAYS),
            null, null, mockHomeTeam, mockAwayTeam);
    private final MatchDto mockMatchDtoUpdated = new MatchDto(1L, Instant.now().minus(1, ChronoUnit.DAYS),
            1, 1, mockHomeTeam, mockAwayTeam);

    @Test
    void getAll() throws Exception {
        when(matchService.findAll()).thenReturn(List.of(mockMatchDto));

        String response = mockMvc.perform(get("/api/matches")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        List<MatchDto> matches = objectMapper.readValue(response, new TypeReference<>(){});

        assertThat(matches.size()).isNotEqualTo(0);
    }

    @Test
    void findByLeagueNameValid() throws Exception {
        String leagueName = "TIPOS Extraliga";

        when(matchService.findByLeagueName(leagueName)).thenReturn(List.of(mockMatchDto));

        String response = mockMvc.perform(get("/api/matches/TIPOS Extraliga")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        List<MatchDto> matches = objectMapper.readValue(response, new TypeReference<>(){});

        assertThat(matches.size()).isNotEqualTo(0);
        matches.stream().map(m -> assertThat(m.homeTeam().league().name()).isEqualTo(leagueName));
        matches.stream().map(m -> assertThat(m.awayTeam().league().name()).isEqualTo(leagueName));
    }

    @Test
    void findByLeagueNameInvalid() throws Exception {
        mockMvc.perform(get("/api/matches/superliga"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createMatchValid() throws Exception {
        when(matchService.create(mockMatchDto)).thenReturn(mockMatchDto);

        String response = mockMvc.perform(post("/api/matches").contentType("application/json")
                .content(objectMapper.writeValueAsString(mockMatchDto)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        MatchDto matchDto = objectMapper.readValue(response, MatchDto.class);

        assertThat(matchDto).isEqualTo(mockMatchDto);
    }

    @Test
    void updateMatchValid() throws Exception {
        when(matchService.update(mockMatchDtoUpdated)).thenReturn(mockMatchDtoUpdated);

        String response = mockMvc.perform(put("/api/matches").contentType("application/json")
                        .content(objectMapper.writeValueAsString(mockMatchDtoUpdated)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        MatchDto matchDto = objectMapper.readValue(response, MatchDto.class);

        assertThat(matchDto).isEqualTo(mockMatchDtoUpdated);
    }

    @Test
    void updateMatchInvalid() throws Exception {
        when(matchService.update(mockMatchDto)).thenThrow(new IllegalArgumentException());

        mockMvc.perform(put("/api/matches").contentType("application/json")
                        .content(objectMapper.writeValueAsString(mockMatchDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void findUnplayedMatchesBeforeNowValid() throws Exception {
        when(matchService.findUnplayedMatchesBeforeNow()).thenReturn(List.of(mockMatchDto));

        String response = mockMvc.perform(get("/api/matches/find-unplayed-matches")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        List<MatchDto> matches = objectMapper.readValue(response, new TypeReference<>(){});

        assertThat(matches.size()).isNotEqualTo(0);
        matches.stream().map(m -> assertThat(m.dateOfMatch()).isBefore(Instant.now()));
        matches.stream().map(m -> assertThat(m.homeGoals()).isEqualTo(null));
    }

    @Test
    void playUnplayedMatchesValid() throws Exception {
        when(matchService.playUnplayedMatches()).thenReturn(List.of(mockMatchDtoUpdated));

        String response = mockMvc.perform(get("/api/matches/play-unplayed-matches")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        List<MatchDto> matches = objectMapper.readValue(response, new TypeReference<>(){});

        assertThat(matches.size()).isNotEqualTo(0);
        matches.stream().map(m -> assertThat(m.dateOfMatch()).isBefore(Instant.now()));
        matches.stream().map(m -> assertThat(m.homeGoals()).isNotEqualTo(null));
    }
}
