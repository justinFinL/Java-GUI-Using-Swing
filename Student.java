/*
Student.java
Author: Justin Fennell
Date: 3/3/2017
Program includes a GUI that interacts with a student database
 */

package com.IDEAProjectExample;

//allows us to use decimals format
import java.text.DecimalFormat;

public class Student {

    //declare variables
    private int id;
    private String name;
    private String major;
    private int totalCredits;
    private int totalQP;
    private double GPA = 4.0; //set initial to 4.0

    //constructor
    public Student(int id, String name, String major) {
        this.id = id;
        this.name = name;
        this.major = major;
        totalCredits = 0;
        totalQP = 0;
    }

    //set credits
    public void setTotalCredits(int totalCredits) {
        this.totalCredits += totalCredits;
    }
    //completed course
    public void courseCompleted(int courseGrade, int creditNumbers){
        GPA = 0;
        totalCredits = creditNumbers;
        totalQP = courseGrade * creditNumbers;
        DecimalFormat df = new DecimalFormat("#.##");
        GPA = Double.valueOf(df.format((double)totalQP / totalCredits));


    }


    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public int getTotalCredits() {
        return totalCredits;
    }


    public int getTotalQP() {
        return totalQP;
    }

    public void setTotalQP(int totalQP) {
        this.totalQP = totalQP;
    }


    public double getGPA() {
        return GPA;
    }

    public void setGPA(double GPA) {
        this.GPA = GPA;
    }

    @Override
    public String toString() {
        return " Id: " + id + "\n Name: " + name +"\n Major: " + major + "\n GPA: " + GPA;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Student student = (Student) o;

        if (id != student.id) return false;
        if (totalCredits != student.totalCredits) return false;
        if (totalQP != student.totalQP) return false;
        if (Double.compare(student.GPA, GPA) != 0) return false;
        if (name != null ? !name.equals(student.name) : student.name != null) return false;
        return major != null ? major.equals(student.major) : student.major == null;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (major != null ? major.hashCode() : 0);
        result = 31 * result + totalCredits;
        result = 31 * result + totalQP;
        temp = Double.doubleToLongBits(GPA);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
