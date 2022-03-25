import models.Tache;
import play.jobs.Every;
import play.jobs.Job;
import play.libs.WS;
import play.test.Fixtures;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


@Every("55s")
public class Pushover extends Job {

    public void doJob(){
        System.out.println("Check for reminders");

        //Partie date.
        String dateFormat = "yyyy-MM-dd HH:mm";
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(dateFormat);
        LocalDateTime currentdate = LocalDateTime.now();
        String cd = dtf.format(currentdate);
        List<Tache> taches = Tache.findAll();
        DateFormat df = new SimpleDateFormat(dateFormat);

        for(Tache t:taches){
            Date reminderDate = t.getReminderDate();
            String rd = df.format(reminderDate);
            if(rd.equals(cd)){
                Map<String,String> parameters_res = new HashMap<>();
                parameters_res.put("token", "a3q77jy23fyi9poimh3ptmb4obuy5o");
                parameters_res.put("user", "u5yhw6gvm9mjergefiyh4zk1cagf2z");
                parameters_res.put("message", t.getTitle());
                parameters_res.put("title", "Alerte Todolist !");
                WS.HttpResponse res = WS.url("https://api.pushover.net/1/messages.json").setParameters(parameters_res).post();

                int status = res.getStatus();
                if(status == 200){
                    System.out.println("Envoi d'un message effectué !");
                } else {
                    System.out.println("Erreur lors de l'envoi du message. Statut " + status);
                }

            }
        }
    }

}

