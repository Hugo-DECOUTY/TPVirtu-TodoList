package controllers;

import play.mvc.Controller;
import models.Tache;
import javax.persistence.PersistenceException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Application extends Controller {

    //Messages d'erreurs retournés.
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
            //On remplace le T du format renvoyé par le tag datetime-local.
            reminderDateString = reminderDateString.replace("T"," ");
            //On le renvoie sous un format de date simplifié.
            Date reminderDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(reminderDateString);
            //On crée une tâche et on l'enregistre dans la BDD.
            Tache new_tache = new Tache(title,false, reminderDate);
            new_tache.save();
            render();
            //Gestion d'exception : Le titre a déjà été attribué à une autre tâche et on redirige.
        } catch (PersistenceException err){
            System.out.println(ACTION_FAILED_OUTPUT);
            redirect("Application.ajouterTacheForm");
        }
    }

    // Change le statut d'une tâche en base de données
    public static void validerTache(Long id) {
        //On cherche la tâche par le biais de son identifiant puis on
        //définit la tâche accomplie ou non en fonction de l'inverse de l'état de la tâche avant l'appel à la fonction
        Tache tache = Tache.findById(id);
        boolean taskDoneOrNot = !tache.isTaskDone();
        tache.setTaskDone(taskDoneOrNot);
        tache.save();
    }

    // Affiche le template views/editTacheForm.html (formulaire d'ajout d'une tâche)
    public static void editTacheForm(Long id) {
        try {
            //On cherche la tâche par le biais de son identifiant puis on reparamètre la date pour rajouter le T,
            //et avoir un bel affichage HTML du tag datetime-local.
            Tache tache = Tache.findById(id);
            String data = tache.getReminderDate().toString().replace(" ","T");
            render(tache, data);
            //Gestion d'exceptions : Si l'identifiant fourni dans l'URL est incorrect, on redirige.
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
            //On modifie toutes les valeurs de la tâche et on enregistre. Enfin, on redirige.
            Tache tache = Tache.findById(id);
            tache.setTitle(title);
            String reminderDateString = date.replace("T"," ");
            Date reminderDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(reminderDateString);
            tache.setReminderDate(reminderDate);
            tache.save();
            redirect("/");
            //Gestion d'exceptions : Si l'identifiant est incorrect ou si le titre est déjà attribué, on redirige.
        } catch(IllegalArgumentException | NullPointerException | PersistenceException err){
            System.out.println(ACTION_FAILED_OUTPUT);
            redirect("/");
        }

    }

}