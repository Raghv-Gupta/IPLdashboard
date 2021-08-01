package io.myservice.IPL_Dashboard.processor;

import io.myservice.IPL_Dashboard.models.Match;
import io.myservice.IPL_Dashboard.models.MatchInput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import java.time.LocalDate;

public class MatchProcessor implements ItemProcessor<MatchInput, Match> {

    private static final Logger log = LoggerFactory.getLogger(MatchProcessor.class);

    @Override
    public Match process(final MatchInput matchInput) throws Exception {

        Match match=new Match();
        match.setId(Long.parseLong(matchInput.getId()));
        match.setCity(match.getCity());
        match.setDate(LocalDate.parse(matchInput.getDate()));
        match.setPlayerOfMatch(match.getPlayerOfMatch());
        match.setSeason(matchInput.getDate().split("-")[0]);

        String team1,team2;
        if("bat".equalsIgnoreCase(matchInput.getTossDecision()))
        {
            team1=matchInput.getTossWinner();
            team2=matchInput.getTossWinner().equals(matchInput.getTeam1())? matchInput.getTeam2() : matchInput.getTeam1();
        }
        else
        {
            team2=matchInput.getTossWinner();
            team1=matchInput.getTossWinner().equals(matchInput.getTeam1())? matchInput.getTeam2() : matchInput.getTeam1();
        }
        match.setTeam1(team1);
        match.setTeam2(team2);
        match.setTossWinner(match.getTossWinner());
        match.setTossDecision(match.getTossDecision());
        match.setResult(matchInput.getResult());
        match.setResultMargin(matchInput.getResultMargin());
        match.setMatchWinner(matchInput.getWinner());
        match.setUmpire1(matchInput.getUmpire1());
        match.setUmpire2(matchInput.getUmpire2());
        match.setVenue(matchInput.getVenue());

//        final String firstName = person.getFirstName().toUpperCase();
//        final String lastName = person.getLastName().toUpperCase();
//        final Person transformedPerson = new Person(firstName, lastName);

        log.info("Converting (" + matchInput.toString() + ") into (" + match.toString() + ")");

        return match;
    }
}
