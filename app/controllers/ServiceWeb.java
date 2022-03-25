package controllers;

import models.Tache;
import net.bytebuddy.pool.TypePool;
import play.mvc.Controller;

import javax.persistence.PersistenceException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ServiceWeb extends Controller {

    private static final String ID_NOT_FOUND_OUTPUT = "L'identifiant fourni n'est pas correct.\n";
    private static final String INVALID_DATE_FORMAT_OUTPUT = "La date doit être renseignée selon le format yyyy-MM-dd HH:mm\n";
    private static final String DATE_AND_TITLE_NOT_SPECIFIED_OUTPUT = "Un titre et une date doit être spécifiée\n";
    private static final String TITLE_ALREADY_EXISTS_OUTPUT = "Il existe déjà un titre du nom de : ";

    // Ajoute une tâche en base de données (CREATE => POST)
    // Test (curl) : curl --data "title=task-from-curl&date=2022-03-24 14:18" localhost:9000/api/tache
    // Test (curl) : curl --data "title=task-from-curl&date=2022-03-24 14:18" http://app-5ab97e8e-e6b5-4f08-9645-fff5f353d754.cleverapps.io/api/tache
    public static void ajouterTache(String title, String date) throws ParseException {
        try {
            if(title != null && date != null){
                Date reminderDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(date);
                Tache new_tache = new Tache(title,false, reminderDate);
                new_tache.save();
                renderJSON("New data uploaded with id : " + new_tache.id + "\n");
            } else {
                renderJSON(DATE_AND_TITLE_NOT_SPECIFIED_OUTPUT);
            }
        } catch(ParseException err) {
            renderJSON(INVALID_DATE_FORMAT_OUTPUT);
        } catch(PersistenceException err){
            renderJSON(TITLE_ALREADY_EXISTS_OUTPUT + title + "\n");
        }

    }

    // Retourne au format JSON la liste des tâches (READ => GET)
    // Test (curl) : curl localhost:9000/api/taches.json
    // Test (curl) : curl http://app-5ab97e8e-e6b5-4f08-9645-fff5f353d754.cleverapps.io/api/taches.json
    public static void listTache() {
        List<Tache> taches = Tache.findAll();
        renderJSON(taches);
    }

    // Retourne au format JSON une tâche (READ => GET)
    // Test (curl) : curl localhost:9000/api/tache/1.json
    // Test (curl) : curl http://app-5ab97e8e-e6b5-4f08-9645-fff5f353d754.cleverapps.io/api/tache/1.json
    public static void getTache(Long id) {
        try {
            Tache tache = Tache.findById(id);
            renderJSON(tache + "\n");
        } catch(NullPointerException | IllegalArgumentException err){
            renderJSON(ID_NOT_FOUND_OUTPUT);
        }
    }

    // Modifie le titre d'une tâche (UPDATE => PUT)
    // Test (curl) : curl -X PUT --data "title=aaabbb&date=2022-03-24 14:18" localhost:9000/api/tache/1
    // Test (curl) : curl -X PUT --data "title=aaabbb&date=2022-03-24 14:18" http://app-5ab97e8e-e6b5-4f08-9645-fff5f353d754.cleverapps.io/api/tache/1
    public static void editTitleTache(Long id, String title, String date) throws ParseException {
        try {
            if(title != null && date != null){
                Tache tache = Tache.findById(id);
                tache.setTitle(title);
                tache.setReminderDate(new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(date));
                tache.save();
                renderJSON(tache + "\n");
            } else {
                renderJSON(DATE_AND_TITLE_NOT_SPECIFIED_OUTPUT);
            }
        } catch(NullPointerException | IllegalArgumentException err){
            renderJSON(ID_NOT_FOUND_OUTPUT);
        } catch(PersistenceException pe){
            renderJSON(TITLE_ALREADY_EXISTS_OUTPUT + title + "\n");
        } catch(ParseException pe){
            renderJSON(INVALID_DATE_FORMAT_OUTPUT);
        }


    }

    // Change le statut d'une tâche (UPDATE => PUT)
    // Test (curl) : curl -X PUT localhost:9000/api/tache/1/change-statut
    // Test (curl) : curl -X PUT http://app-5ab97e8e-e6b5-4f08-9645-fff5f353d754.cleverapps.io/api/tache/1/change-statut
    public static void changeStatutTache(Long id) {
        try {
            Tache tache = Tache.findById(id);
            tache.setTaskDone(!tache.isTaskDone());
            tache.save();
            renderJSON(tache + "\n");
        } catch(NullPointerException | IllegalArgumentException err){
            renderJSON(ID_NOT_FOUND_OUTPUT);
        }
    }

    // Supprime une tâche (DELETE => DELETE)
    // Test (curl) : curl -X DELETE localhost:9000/api/tache/1
    // Test (curl) : curl -X DELETE http://app-5ab97e8e-e6b5-4f08-9645-fff5f353d754.cleverapps.io/api/tache/1
    public static void supprimeTache(Long id) {
        try {
        Tache tache = Tache.findById(id);
        tache._delete();
        renderJSON(tache + "\n");
        } catch(NullPointerException | IllegalArgumentException err){
            renderJSON(ID_NOT_FOUND_OUTPUT);
        }
    }
    
}
