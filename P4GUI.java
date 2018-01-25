/*
P4GUI.java
Author: Justin Fennell
Date: 3/3/2017
Program includes a GUI that interacts with a student database
 */

package com.IDEAProjectExample;

        //import classes
        import javax.swing.*;
        import java.awt.*;
        import java.awt.event.ActionEvent;
        import java.awt.event.ActionListener;
        import java.awt.event.WindowEvent;
        import java.io.*;
        import java.util.HashMap;
        import java.util.Map;


public class P4GUI extends JFrame implements ActionListener {

    private JButton processButton = new JButton("Process Request");

    private JLabel id = new JLabel("Id: ");
    private JLabel name = new JLabel("Name: ");
    private JLabel major = new JLabel("Major: ");
    private JLabel selection = new JLabel("Choose Selection: ");

    private JTextField idText = new JTextField();
    private JTextField nameText = new JTextField();
    private JTextField majorText = new JTextField();

    //Array used for filling crudMenu comboBox
    private String[] choices = {"Insert","Delete","Find","Update"};

    //CRUD - Create Read Update Delete
    private JComboBox<String> crudMenu = new JComboBox<>(choices);

    //output file
    private String fileName = "outData.txt";

    //Actual DataBase
    private Map<Integer,Student> studentDB = readStudentDB();


    //window
    private P4GUI(){
        super("Project 4");
        this.setBounds(20,20,300,150);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setResizable(false);

        Container container = this.getContentPane();

        //Grid layout for setting elements
        container.setLayout(new GridLayout(5,2,5,5));
        container.add(id);
        container.add(idText);
        container.add(name);
        container.add(nameText);
        container.add(major);
        container.add(majorText);
        container.add(selection);
        container.add(crudMenu);
        container.add(processButton);

        processButton.addActionListener(this);
        crudMenu.addActionListener(e ->updateState());
    }

    //Method used for block inputs Name and Major
    private void updateState(){
        boolean enabled = crudMenu.getSelectedItem().equals("Insert");
        if(!enabled){
            majorText.setEnabled(false);
            nameText.setEnabled(false);
        }
        else {
            majorText.setEnabled(true);
            nameText.setEnabled(true);
        }
    }


    //Main method
    public static void main(String[] args) {
        P4GUI p4GUI = new P4GUI();
        p4GUI.setVisible(true);
    }


    /**
     * Button press handler
     * @param e - parameter to handle some events. We don't need it, but it's an
     *  Interface method, so it must be there.
     *  Method checks, if Id is not empty(most important field!) reads id(only integer allowed,
     *  or exception will be produced.
     *  Then, depending on user choice it takes some sort of action.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        String choice = crudMenu.getSelectedItem().toString();
        if(!idText.getText().isEmpty()) {
            try {
                Integer key = Integer.valueOf(idText.getText());
                switch (choice) {
                    case "Insert":
                        insertStudent(key);
                        break;
                    case "Delete":
                        deleteStudent(key);
                        break;
                    case "Find":
                        findStudentById(key);
                        break;
                    case "Update":
                        updateStudent(key);
                        break;
                }
            } catch (NumberFormatException error) {
                JOptionPane.showMessageDialog(this,"Please enter only numbers!",
                        "Error!",JOptionPane.ERROR_MESSAGE);
            }
        }
        else
            JOptionPane.showMessageDialog(null,"Id Field must not be empty!");
    }

    /**
     * On-Close handler. In this method we call method to write map to file
     * @param e
     */
    @Override
    protected void processWindowEvent(WindowEvent e) {
        if(e.getID() == WindowEvent.WINDOW_CLOSING){
            writeStudentDB();
        }
        super.processWindowEvent(e);
    }


    /**
     * key is the ID of student, and also key of map.
     * Method checks if inserted ID is not already in map,
     * then checks that Name doesn't contains numbers.
     * I think that major should contain numbers and all other symbols.
     * Also, checks if Name and Major not empty.
     * If everything is correct, create Student instance and add it to map.
     *
     */
    private void insertStudent(Integer key){
        if(studentDB.containsKey(key)){
            JOptionPane.showMessageDialog(null,"Student with " +
                    "this key is already in the Database!");
        }
        else {
            if(idText.getText().isEmpty()){
                idText.setText("1");
            }
            String name = nameText.getText();
            if(name.matches("^-?\\d+$")){
                JOptionPane.showMessageDialog(null,"Must not contain numbers","",JOptionPane.ERROR_MESSAGE);
                return;
            }
            String major = majorText.getText();
            if(name.isEmpty() || major.isEmpty()){
                JOptionPane.showMessageDialog(null,"Fields must not be empty!","",JOptionPane.ERROR_MESSAGE);
            }
            else {
                Student student = new Student(key, name, major);
                studentDB.put(key, student);
                JOptionPane.showMessageDialog(null, "Success!\n");
                idText.setText(String.valueOf(Integer.parseInt(idText.getText()) + 1));
                clearFields();

            }
        }
    }

    /**
     * @param key - Student ID
     *            Check if iD is in DB and delete value by key
     */
    private void deleteStudent(Integer key){
        if(studentDB.containsKey(key)){
            studentDB.remove(key);
            JOptionPane.showMessageDialog(null, "Success!");
        }
        else
            JOptionPane.showMessageDialog(null,
                    "Student not in database!\n");
        clearFields();
    }

    /**
     * @param key - Student ID
     *     Check if iD is in DB and show all info
     */
    private void findStudentById(Integer key){
        if(studentDB.containsKey(key)){
            Student student = studentDB.get(key);
            JOptionPane.showMessageDialog(null,
                    student);
        }
        else
            JOptionPane.showMessageDialog(null,
                    "Student not in database!\n");
        clearFields();
    }

    /**
     * @param key - Student ID
     *            Check if iD is in DB.
     *            Creates 2 arrays for comboboxes in JOptionPane
     *            shows them to user.
     *            if user presses cancel it won't do anything
     *            gradeInt - representation of mark in numeric format.
     *            creditInt - credits.
     *              Creates instance of Student by getting it from map by key
     *            Then calls courseCompleted method from Student class, to calculate GPA
     *            and replaces value in this key
     */
    private void updateStudent(Integer key){
        if(!studentDB.containsKey(key)) {
            JOptionPane.showMessageDialog(null,
                    "Student not in database!\n");
        }
        else {
            Student student = studentDB.get(key);
            String[] grades = {"A", "B", "C", "D", "F"};
            String[] credits = {"3", "6"};
            String grade = (String) JOptionPane.showInputDialog(this, "Choose grade: ",
                    "", JOptionPane.QUESTION_MESSAGE, null, grades, grades[0]);
            String credit = (String) JOptionPane.showInputDialog(this, "Choose credit: "
                    , "", JOptionPane.QUESTION_MESSAGE, null, credits, credits[0]);
            int gradeInt = 0;
            int creditInt = 0;
            if (grade != null || credit != null) {
                switch (grade) {
                    case "A":
                        gradeInt = 4;
                        break;
                    case "B":
                        gradeInt = 3;
                        break;
                    case "C":
                        gradeInt = 2;
                        break;
                    case "D":
                        gradeInt = 1;
                        break;
                    case "F":
                        gradeInt = 0;
                        break;
                }

                switch (credit) {
                    case "3":
                        creditInt = 3;
                        break;
                    case "6":
                        creditInt = 6;
                        break;
                }

                student.courseCompleted(gradeInt, creditInt);
                studentDB.put(key, student);
                JOptionPane.showMessageDialog(this,
                        "Course " + student.getMajor() + " has been completed!\n" +
                                "Grade: " + grade +"\nNumber of credits: " + credit );
            }
        }
    }




    private void clearFields(){
        nameText.setText("");
        majorText.setText("");
    }

    /**
     * iterates map if it is not empty and writes all data to a file
     * if map is empty, it doesnt create anything
     *
     */
    private void writeStudentDB(){
        if(!studentDB.isEmpty()) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
                int sum = 0;
                int total = studentDB.size();
                StringBuilder sb = new StringBuilder();
                for (Map.Entry<Integer, Student> en :
                        studentDB.entrySet()) {
                    Student student = en.getValue();
                    sb.append(student.getId()).append("\t")
                            .append(student.getName()).append("\t")
                            .append(student.getMajor()).append("\t")
                            .append(student.getTotalQP()).append("\t")
                            .append(student.getTotalCredits()).append("\t")
                            .append(student.getGPA()).append(System.lineSeparator());
                    sum += student.getGPA();

                }
                writer.write(sb.toString());
                double avg = (double) sum / total;
                writer.write("Total student records: " + total
                        + " Average GPA: " + avg);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @return filled map depending on text file,
     * or empty map if file didn't exists
     */
    private Map<Integer,Student> readStudentDB(){
        String previousLine = null;
        String line;
        File file = new File(fileName);
        if(file.exists()){
            try(BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
                Map<Integer,Student> studentMap = new HashMap<>();
                while ((line = reader.readLine()) != null){
                    if (previousLine != null) {
                        if(!previousLine.isEmpty()){
                            String[] lines = previousLine.split("\t");
                            int id = Integer.valueOf(lines[0]);
                            String name = lines[1];
                            String major = lines[2];
                            int totalQP = Integer.valueOf(lines[3]);
                            int totalCredits = Integer.valueOf(lines[4]);
                            double GPA = Double.valueOf(lines[5]);
                            Student student = new Student(id,name,major);
                            student.setTotalCredits(totalCredits);
                            student.setTotalQP(totalQP);
                            student.setGPA(GPA);
                            studentMap.put(id,student);
                        }
                    }
                    previousLine = line;
                }
                return studentMap;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new HashMap<>();
    }

}

