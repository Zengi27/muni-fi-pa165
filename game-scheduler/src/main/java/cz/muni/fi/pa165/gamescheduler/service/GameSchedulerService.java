package cz.muni.fi.pa165.gamescheduler.service;

import cz.muni.fi.pa165.model.dto.LeagueDto;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class GameSchedulerService {

    public GameSchedulerDto generate(String leagueName) {

        GameSchedulerDto gameScheduler = new GameSchedulerDto();
        return gameScheduler;
    }
}
