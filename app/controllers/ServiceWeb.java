package controllers;

import com.google.gson.Gson;
import models.Tache;
import play.mvc.Controller;

import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ServiceWeb extends Controller {

    // Ajoute une tâche en base de données (CREATE => POST)
    // Test (curl) : curl --data "title=task-from-curl&date=2022-03-24 14:18" localhost:9000/api/tache
    public static void ajouterTache(String title, String date) throws ParseException {
        Date reminderDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(date);
        if(title != null && reminderDate != null){
            Tache new_tache = new Tache(title,false, reminderDate);
            new_tache.save();
            renderJSON(new_tache);
        }
        renderJSON(null);
    }

    // Retourne au format JSON la liste des tâches (READ => GET)
    // Test (curl) : curl localhost:9000/api/taches.json
    public static void listTache() {
        List<Tache> taches = Tache.findAll();
        renderJSON(taches);
    }

    // Retourne au format JSON une tâche (READ => GET)
    // Test (curl) : curl localhost:9000/api/tache/1.json
    public static void getTache(Long id) {
        Tache tache = Tache.findById(id);
        renderJSON(tache);
    }

    // Modifie le titre d'une tâche (UPDATE => PUT)
    // Test (curl) : curl -X PUT --data "title=aaabbb&date=2022-03-24 14:18" localhost:9000/api/tache/1
    public static void editTitleTache(Long id, String title, String date) throws ParseException {
        Tache tache = Tache.findById(id);
        tache.setTitle(title);
        tache.setReminderDate(new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(date));
        tache.save();
        renderJSON(tache);
    }

    // Change le statut d'une tâche (UPDATE => PUT)
    // Test (curl) : curl -X PUT localhost:9000/api/tache/1/change-statut
    public static void changeStatutTache(Long id) {
        Tache tache = Tache.findById(id);
        tache.setTaskDone(!tache.isTaskDone());
        tache.save();
        renderJSON(tache);
    }

    // Supprime une tâche (DELETE => DELETE)
    // Test (curl) : curl -X DELETE localhost:9000/api/tache/1
    public static void supprimeTache(Long id) {
        Tache tache = Tache.findById(id);
        tache._delete();
        renderJSON(tache);
    }
    
}
