package Object;

import javafx.scene.control.DatePicker;

import java.sql.Date;
import java.time.LocalDate;

/**
 * This class stores every information about an appointment
 */
public class Appointment {
    private int id;
    private String name;
    private Date date;
    private int start;
    private int startminutes;
    private int end;
    private int endminutes;
    private String location;
    private String participants;
    private String priority;
    private String reminder;


    /**
     * Creates an Appointment
     * @param id primary key of the mysql databse
     * @param name name of the appointment
     * @param date date of the appointment
     * @param start starting time of the appointment in hours
     * @param startminutes starting time of the appointment in minutes
     * @param end ending time of the appointment in hours
     * @param endminutes ending time of the appointment in minutes
     * @param location location of the appointment
     * @param participants a string of participating people
     * @param priority priority of an appointment
     * @param reminder point in time in which the email reminder should be sent
     */
    public Appointment(int id, String name, Date date, int start, int startminutes, int end, int endminutes, String location, String participants, String priority, String reminder) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.start = startminutes;
        this.endminutes = endminutes;
        this.location = location;
        this.participants = participants;
        this.priority = priority;
        this.reminder = reminder;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = Date.valueOf(date);
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getStartminutes() {
        return startminutes;
    }

    public void setStartminutes(int startminutes) {
        this.startminutes = startminutes;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public int getEndminutes() {
        return endminutes;
    }

    public void setEndminutes(int endminutes) {
        this.endminutes = endminutes;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getParticipants() {
        return participants;
    }

    public void setParticipants(String participants) {
        this.participants = participants;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getReminder() {
        return reminder;
    }

    public void setReminder(String reminder) {
        this.reminder = reminder;
    }
}
