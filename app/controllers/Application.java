package controllers;

import org.joda.time.DateTime;
import play.mvc.Controller;

import models.Tache;
import play.mvc.Controller;
import play.db.jpa.JPA;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.constraints.Null;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Application extends Controller {

    private static final String ACTION_FAILED_OUTPUT = "Erreur. L'action n'a pas pu être effectuée. Redirection.";
    private static final String INVALID_ID_OUTPUT = "Erreur. L'id n'est pas correct. Redirection.";

    // Affiche toutes les tâches (voir variable taches) dans le template views/listTache.html
    public static void listTache() {
        List<Tache> taches = Tache.findAll();
        render(taches);
    }

    // Affiche le template views/ajouterTacheForm.html (formulaire d'ajout d'une tâche)
    public static void ajouterTacheForm() {
        render();
    }

    // Ajoute une nouvelle tâche en base de données et affiche le template views/ajouterTache.html
    public static void ajouterTache(String title, String reminderDateString) throws ParseException {
        try {
            reminderDateString = reminderDateString.replace("T"," ");
            Date reminderDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(reminderDateString);
            Tache new_tache = new Tache(title,false, reminderDate);
            new_tache.save();
            render();
        } catch (PersistenceException err){
            System.out.println(ACTION_FAILED_OUTPUT);
            redirect("Application.ajouterTacheForm");
        }
    }

    // Change le statut d'une tâche en base de données
    public static void validerTache(Long id) {
        Tache tache = Tache.findById(id);
        boolean taskDoneOrNot = !tache.isTaskDone();
        tache.setTaskDone(taskDoneOrNot);
        tache.save();
    }

    // Affiche le template views/editTacheForm.html (formulaire d'ajout d'une tâche)
    public static void editTacheForm(Long id) {
        try {
            Tache tache = Tache.findById(id);
            String data = tache.getReminderDate().toString().replace(" ","T");
            render(tache, data);
        } catch(IllegalArgumentException | NullPointerException err){
            System.out.println(INVALID_ID_OUTPUT);
            redirect("/");
        }
    }

    // Supprime une tâche en base de données
    public static void supprimerTache(Long id) {
        Tache.findById(id)._delete();
    }

    // Modifie une tâche en base de données
    public static void editTache(Long id, String title, String date) throws ParseException {
        try {
            Tache tache = Tache.findById(id);
            tache.setTitle(title);
            String reminderDateString = date.replace("T"," ");
            Date reminderDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(reminderDateString);
            tache.setReminderDate(reminderDate);
            tache.save();
            redirect("/");
        } catch(IllegalArgumentException | NullPointerException | PersistenceException err){
            System.out.println(ACTION_FAILED_OUTPUT);
            redirect("/");
        }

    }

}