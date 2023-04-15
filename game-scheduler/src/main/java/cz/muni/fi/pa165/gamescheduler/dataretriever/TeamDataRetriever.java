package cz.muni.fi.pa165.gamescheduler.dataretriever;

import cz.muni.fi.pa165.model.dto.TeamDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Component
public class TeamDataRetriever {

    public final WebClient coreClient;

    @Autowired
    public TeamDataRetriever(WebClient coreClient) {
        this.coreClient = coreClient;
    }

    public List<TeamDto> getTeams(String leagueName) {

        return coreClient.get()
                .uri(uriBuilder -> uriBuilder.pathSegment("api", "teams", "find-by-league", leagueName)
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<TeamDto>>() {})
                .block();
    }
}
