import models.Tache;
import play.jobs.Job;
import play.jobs.OnApplicationStart;
import play.test.Fixtures;


@OnApplicationStart
public class Bootstrap extends Job {

    public void doJob() {
        // Load default data if the database is empty
        if(Tache.count() == 0) {
            Fixtures.loadModels("data.yml");
            System.out.println("load data.yml done !");
        }
    }

}
