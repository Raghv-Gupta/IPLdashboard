package io.myservice.IPL_Dashboard.controller;

import io.myservice.IPL_Dashboard.models.Match;
import io.myservice.IPL_Dashboard.models.Team;
import io.myservice.IPL_Dashboard.repository.MatchRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import io.myservice.IPL_Dashboard.repository.TeamRepository;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/")
@CrossOrigin
public class TeamController {

    private TeamRepository teamRepository;
    private MatchRepository matchRepository;

    TeamController(TeamRepository teamRepository,MatchRepository matchRepository)
    {
        this.teamRepository=teamRepository;
        this.matchRepository=matchRepository;
    }
    @GetMapping("/team/{teamName}")
    public Team getTeam(@PathVariable("teamName") String teamName)
    {
        Team team=teamRepository.getByTeamName(teamName);
        Pageable p=PageRequest.of(0,4);
        List<Match> list=matchRepository.getLatestMatchesFromTeam(teamName,p);
        team.setLatestMatches(list);
        return team;
    }
    @GetMapping("/team/{teamName}/matches")
    public List<Match> getTeamMatchesInYear(@PathVariable String teamName,@RequestParam int year)
    {
        LocalDate startDate=LocalDate.of(year,1,1);
        LocalDate endDate=LocalDate.of(year+1,1,1);
        return matchRepository.getAllTeamMatchInYear(teamName,startDate,endDate);
    }
    @GetMapping("/team/allTeams")
    public List<Team> getAllteams()
    {
        return teamRepository.findAll();
    }
    @GetMapping("/matchwinner/{teamName}")
    public List<Match> getAllMatchwon(@PathVariable("teamName") String teamName)
    {
        return matchRepository.findByMatchWinner(teamName);
    }

    @GetMapping("season/{season}")
    public List<Match> getAllMatchesBySeason(@PathVariable("season") String season) {
        return matchRepository.findBySeason(season);
    }

    @GetMapping("seasonAndTeams/{season}")
    public List<Match> getAllMatchesBySeasonAndTeams(@PathVariable("season") String season) {

        String team1 = "Chennai Super Kings";
        String team2 = "Deccan Chargers";

        return matchRepository.findBySeasonAndTeam1OrTeam2(season, team1, team2);
    }
}
