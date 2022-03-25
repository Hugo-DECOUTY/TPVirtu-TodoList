package models;

import play.db.jpa.Model;
import javax.persistence.*;
import java.util.Date;

@Entity
public class Tache extends Model {

    @Basic
    @Column(length=70, nullable=false, unique = true)
    public String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Basic
    @Column(nullable = false)
    public boolean taskDone;

    public boolean isTaskDone() {
        return taskDone;
    }

    public void setTaskDone(boolean taskDone) {
        this.taskDone = taskDone;
    }

    @Basic
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    public java.util.Date reminderDate;

    public Date getReminderDate() {
        return reminderDate;
    }

    public void setReminderDate(Date reminderDate) {
        this.reminderDate = reminderDate;
    }

    public Tache(String title, boolean taskDone, Date reminderDate){
        this.title = title;
        this.taskDone = taskDone;
        this.reminderDate = reminderDate;
    }

}