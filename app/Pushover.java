import models.Tache;
import play.jobs.Every;
import play.jobs.Job;
import play.test.Fixtures;
import pushover.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;


@Every("55s")
public class Pushover extends Job {

    public void doJob() throws PushoverException {
        System.out.println("Check for reminders");
        PushoverClient client = new PushoverRestClient();
        String dateFormat = "yyyy-MM-dd HH:mm";

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(dateFormat);
        LocalDateTime currentdate = LocalDateTime.now();
        String cd = dtf.format(currentdate);

        List<Tache> taches = Tache.findAll();
        new SimpleDateFormat(dateFormat);
        DateFormat df = new SimpleDateFormat(dateFormat);
        for(Tache t:taches){
            Date reminderDate = t.getReminderDate();
            String rd = df.format(reminderDate);
            if(rd.equals(cd)){
                client.pushMessage(PushoverMessage.builderWithApiToken("a3q77jy23fyi9poimh3ptmb4obuy5o")
                        .setUserId("u5yhw6gvm9mjergefiyh4zk1cagf2z")
                        .setMessage(t.getTitle())
                        .setTitle("Alerte rappel todolist !")
                        .build());
            }
        }
    }

}

