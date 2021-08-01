package io.myservice.IPL_Dashboard.config;
import io.myservice.IPL_Dashboard.models.Team;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Transactional
public class JobCompletionNotificationListener extends JobExecutionListenerSupport {

    private static final Logger log = LoggerFactory.getLogger(JobCompletionNotificationListener.class);

    private final EntityManager entityManager;
//    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public JobCompletionNotificationListener(EntityManager entityManager) {
//        this.jdbcTemplate = jdbcTemplate;
          this.entityManager=entityManager;
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        if(jobExecution.getStatus() == BatchStatus.COMPLETED)
        {
            log.info("!!! JOB FINISHED! Time to verify the results");

//            jdbcTemplate.query("SELECT team1, team2,date,match_winner FROM match",
//                    (rs, row) ->"Team1: "+rs.getString(1)+",Team2: "+rs.getString(2)+",date: "+rs.getString(3)+",winner: "+rs.getString(4)
//            ).forEach(match -> log.info(match));
//           List<Object[]> list= entityManager.createQuery("SELECT team1, team2,date,matchWinner FROM Match",Object[].class).getResultList();
//            for(Object[] ob:list)
//            {
//                System.out.println("Team1: "+ob[0]+",Team2: "+ob[1]+",date: "+ob[2]+",winner: "+ob[3]);
//            }

            Map<String, Team> teamData=new HashMap<>();

            List<Object[]> team1withcount =entityManager.createQuery("SELECT team1,count(*) FROM Match group by team1",Object[].class).getResultList();
            for(Object[] obj: team1withcount)
            {
                String teamname=(String) obj[0];
                long matchcount=(long)obj[1];
                Team team=new Team(teamname,matchcount);
                teamData.put(teamname,team);
            }
            List<Object[]> team2withcount =entityManager.createQuery("SELECT team2,count(*) FROM Match group by team2",Object[].class).getResultList();
            for(Object[] obj: team2withcount)
            {
                String teamname=(String) obj[0];
                long matchcount=(long)obj[1];
                Team team=teamData.get(teamname);
                if(team!=null)
                team.setTotalMatches(team.getTotalMatches()+matchcount);
            }
            List<Object[]> Winnercount =entityManager.createQuery("SELECT matchWinner,count(*) FROM Match group by matchWinner",Object[].class).getResultList();
            for(Object[] obj: Winnercount)
            {
                String teamname=(String) obj[0];
                long wincount=(long)obj[1];
                Team team=teamData.get(teamname);
                if(team!=null)
                team.setTotalWins(wincount);
            }

            for(Team team: teamData.values())
            {
                System.out.println(team.toString());
                entityManager.persist(team);
            }


        }



    }
}