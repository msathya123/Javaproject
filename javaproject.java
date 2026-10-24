import javax.swing.*;
import java.awt.*;
import java.io.*;

// Base Class
class Person {
    protected String name;
    protected int age;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }
}

// Derived Class
class Patient extends Person {
    private String disease;
    private double weight;

    public Patient(String name, int age, String disease, double weight) {
        super(name, age);
        this.disease = disease;
        this.weight = weight;
    }

    public String toFileString() {
        return name + "," + age + "," + disease + "," + weight;
    }
}

// File Handling Class
class FileManager {
    private static final String FILE_NAME = "patients.txt";

    public static void save(Patient p) throws IOException {
        FileWriter fw = new FileWriter(FILE_NAME, true);
        fw.write(p.toFileString() + "\n");
        fw.close();
    }

    public static String readAll() throws IOException {
        File file = new File(FILE_NAME);
        if (!file.exists()) return "No records found.";

        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        StringBuilder data = new StringBuilder();

        while ((line = br.readLine()) != null) {
            data.append(line).append("\n");
        }
        br.close();
        return data.toString();
    }
}

// GUI Class
public class HealthSystem extends JFrame {

    private JTextField nameField, ageField, diseaseField, weightField;
    private JTextArea outputArea;

    public HealthSystem() {
        setTitle("Personal Health Record Monitoring System");
        setSize(450, 500);
        setLayout(new FlowLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Input Fields
        add(new JLabel("Name:"));
        nameField = new JTextField(15);
        add(nameField);

        add(new JLabel("Age:"));
        ageField = new JTextField(15);
        add(ageField);

        add(new JLabel("Disease:"));
        diseaseField = new JTextField(15);
        add(diseaseField);

        add(new JLabel("Weight:"));
        weightField = new JTextField(15);
        add(weightField);

        // Buttons
        JButton saveBtn = new JButton("Save Record");
        JButton viewBtn = new JButton("View Records");

        add(saveBtn);
        add(viewBtn);

        // Output Area
        outputArea = new JTextArea(12, 35);
        outputArea.setEditable(false);
        add(new JScrollPane(outputArea));

        // Save Button Action
        saveBtn.addActionListener(e -> {
            try {
                String name = nameField.getText().trim();
                int age = Integer.parseInt(ageField.getText().trim());
                String disease = diseaseField.getText().trim();
                double weight = Double.parseDouble(weightField.getText().trim());

                if (name.isEmpty() || disease.isEmpty()) {
                    outputArea.setText("❌ Please fill all fields!");
                    return;
                }

                Patient p = new Patient(name, age, disease, weight);
                FileManager.save(p);

                outputArea.setText("✅ Record Saved Successfully!");

                // Clear fields
                nameField.setText("");
                ageField.setText("");
                diseaseField.setText("");
                weightField.setText("");

            } catch (NumberFormatException ex) {
                outputArea.setText("❌ Enter valid numbers for Age & Weight!");
            } catch (Exception ex) {
                outputArea.setText("❌ Error: " + ex.getMessage());
            }
        });

        // View Button Action
        viewBtn.addActionListener(e -> {
            try {
                outputArea.setText(FileManager.readAll());
            } catch (Exception ex) {
                outputArea.setText("❌ Error: " + ex.getMessage());
            }
        });
    }

    public static void main(String[] args) {
        new HealthSystem().setVisible(true);
    }
}
