package app.com.models;

public class Department implements Comparable<Department> {
    private int DepartmentID;
    private String DepartmentName;


    public Department(int DepartmentID, String DepartmentName) {
        this.DepartmentID = DepartmentID;
        this.DepartmentName = DepartmentName;
    }

    public int GetDepartmentID() {
        return DepartmentID;
    }

    public String GetDepartmentName() {
        return DepartmentName;
    }

    @Override
    public String toString() {
        return DepartmentName;
    }


    @Override
    public int compareTo(Department another) {
        return this.GetDepartmentID() - another.GetDepartmentID();//ascending order
//            return another.getCountryID()-this.getCountryID();//descending  order
    }

}
