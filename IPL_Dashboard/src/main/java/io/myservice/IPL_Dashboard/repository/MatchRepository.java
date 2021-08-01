package io.myservice.IPL_Dashboard.repository;

import io.myservice.IPL_Dashboard.models.Match;
import io.myservice.IPL_Dashboard.models.Team;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.time.LocalDate;
import java.util.List;

public interface MatchRepository extends JpaRepository<Match,Long> {

        public List<Match> findByMatchWinner(String teamName);
        public List<Match> findBySeason(String season);
        public List<Match> findBySeasonAndTeam1OrTeam2(String season,String team1,String team2);

        @Query("SELECT m FROM Match m WHERE m.team1=:teamName1 OR m.team2=:teamName1 ORDER BY date DESC ")
        public List<Match> getLatestMatchesFromTeam(@Param("teamName1") String teamName1, Pageable pageable);

        @Query("SELECT m FROM Match m WHERE (m.team1=:teamName OR m.team2=:teamName) AND m.date BETWEEN :startDate AND :endDate")
        public List<Match> getAllTeamMatchInYear(@Param("teamName")String teamName,@Param("startDate") LocalDate startDate,@Param("endDate") LocalDate endDate);
}
