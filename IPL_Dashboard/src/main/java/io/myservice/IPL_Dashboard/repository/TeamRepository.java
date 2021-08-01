package io.myservice.IPL_Dashboard.repository;

import io.myservice.IPL_Dashboard.models.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeamRepository extends JpaRepository<Team,Long> {
    public Team getByTeamName(String teamName);
    public List<Team> getByTeamNameOrTeamName(String team1,String team2);
}
