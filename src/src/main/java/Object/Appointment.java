package Object;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.concurrent.TimeUnit;

/**
 * This class stores every information about an appointment
 */
public class Appointment {
    private int id;
    private String name;
    private Date date;
    private int startHours;
    private int startMinutes;
    private int endHours;
    private int endMinutes;
    private String location;
    private String participants;
    private String priority;
    private String reminder;
    private boolean reminder_sent;


    /**
     * Creates an Appointment
     * @param id primary key of the mysql databse
     * @param name name of the appointment
     * @param date date of the appointment
     * @param startHours starting time of the appointment in hours
     * @param startMinutes starting time of the appointment in minutes
     * @param endHours ending time of the appointment in hours
     * @param endMinutes ending time of the appointment in minutes
     * @param location location of the appointment
     * @param participants a string of participating people
     * @param priority priority of an appointment
     * @param reminder point in time in which the email reminder should be sent
     */
    public Appointment(int id, String name, Date date, int startHours, int startMinutes, int endHours, int endMinutes, String location, String participants, String priority, String reminder, int reminder_sent) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.startHours = startHours;
        this.startMinutes = startMinutes;
        this.endHours = endHours;
        this.endMinutes = endMinutes;
        this.location = location;
        this.participants = participants;
        this.priority = priority;
        this.reminder = reminder;
        this.reminder_sent = reminder_sent > 0;
    }

    public String getStartTimeFormatted() {
        StringBuilder startTime = new StringBuilder();
        LocalTime localTime;

        if (this.startHours < 10) startTime.append("0"+startHours);
        else startTime.append(startHours);

        startTime.append(":");

        if (this.startMinutes < 10) startTime.append("0"+startMinutes);
        else startTime.append(startMinutes);
        startTime.append(":00");

        localTime = LocalTime.parse(startTime);
        return localTime.toString();
    }

    public String getStartTimeFormatted(int minusHours, int minusMinutes) {
        StringBuilder startTime = new StringBuilder();
        LocalTime localTime;

        if (this.startHours < 10) startTime.append("0"+startHours);
        else startTime.append(startHours);

        startTime.append(":");

        if (startMinutes < 10) startTime.append("0"+startMinutes);
        else startTime.append(startMinutes);

        startTime.append(":00");

        localTime = LocalTime.parse(startTime);
        localTime = localTime.minusHours(minusHours).minusMinutes(minusMinutes);

        return localTime.toString();
    }

    public String getEndTimeFormatted() {
        StringBuilder startTime = new StringBuilder();
        if (this.endHours < 10) startTime.append("0"+endHours);
        else startTime.append(endHours);

        startTime.append(":");

        if (this.endMinutes < 10) startTime.append("0"+endMinutes);
        else startTime.append(endMinutes);

        return startTime.toString();
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

    public int getStartHours() {
        return startHours;
    }

    public void setStartHours(int startHours) {
        this.startHours = startHours;
    }

    public int getStartMinutes() {
        return startMinutes;
    }

    public void setStartMinutes(int startMinutes) {
        this.startMinutes = startMinutes;
    }

    public int getEndHours() {
        return endHours;
    }

    public void setEndHours(int endHours) {
        this.endHours = endHours;
    }

    public int getEndMinutes() {
        return endMinutes;
    }

    public void setEndMinutes(int endMinutes) {
        this.endMinutes = endMinutes;
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

    public boolean isReminder_sent() {
        return reminder_sent;
    }

    public void setReminder_sent(boolean reminder_sent) {
        this.reminder_sent = reminder_sent;
    }
}
