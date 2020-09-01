package app.com.models;

import java.util.ArrayList;
import java.util.Comparator;

public class Notice {

    private String id;
    private String name;
    private String validity;
    private String title;
    private String priority;
    private String description;
    private String timestamp;
    private String emailID;
    private String contact;
    private String isCoordinator;
    private ArrayList<String> attachments;
    private String Banner;
    private String department;
    private String course;
    private String scope;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValidity() {
        return validity;
    }

    public void setValidity(String validity) {
        this.validity = validity;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getEmailID() {
        return emailID;
    }

    public void setEmailID(String emailID) {
        this.emailID = emailID;
    }

    public String getIsCoordinator() {
        return isCoordinator;
    }

    public void setIsCoordinator(String isCoordinator) {
        this.isCoordinator = isCoordinator;
    }

    public ArrayList<String> getAttachments() {
        return attachments;
    }

    public void setAttachments(ArrayList<String> attachments) {
        this.attachments = attachments;
    }

    public String getImages() {
        return Banner;
    }

    public void setImages(String bannerImage) {
        this.Banner = bannerImage;
    }

    public static Comparator<Notice> priorityComparator = new Comparator<Notice>() {

        public int compare(Notice s1, Notice s2) {
            String priorityOne = s1.getPriority().toUpperCase();
            String prioritySecond = s2.getPriority().toUpperCase();

            //ascending order
            return priorityOne.compareTo(prioritySecond);

            //descending order
            //return StudentName2.compareTo(StudentName1);
        }};

    public static Comparator<Notice> dateCompartor = new Comparator<Notice>() {

        public int compare(Notice s1, Notice s2) {
            String dateOne = s1.getTimestamp().toUpperCase();
            String dateSecond = s2.getTimestamp().toUpperCase();

            //ascending order
            return dateOne.compareTo(dateSecond);

            //descending order
            //return StudentName2.compareTo(StudentName1);
        }};

}

