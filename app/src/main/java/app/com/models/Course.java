package app.com.models;

public class Course implements Comparable<Course> {
    private int CourseID;
    private String CourseName;


    public Course(int CourseID, String CourseName) {
        this.CourseID = CourseID;
        this.CourseName = CourseName;
    }

    public int GetCourseID() {
        return CourseID;
    }

    public String GetCourseName() {
        return CourseName;
    }

    @Override
    public String toString() {
        return CourseName;
    }


    @Override
    public int compareTo(Course another) {
        return this.GetCourseID() - another.GetCourseID();//ascending order
//            return another.getCountryID()-this.getCountryID();//descending  order
    }

}
