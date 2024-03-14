import java.awt.*;
import java.awt.event.*;
import java.util.Calendar;
import java.util.HashMap;
import javax.swing.*;

public class SummerProgramAttendance extends JFrame {
    private JTextField nameField;
    private JButton updateButton;
    private JButton logoutButton;
    private JComboBox<String> monthComboBox;
    private JComboBox<String> studentComboBox;
    private JLabel statusLabel;
    private JPanel monthPanel;
    private HashMap<String, HashMap<String, String>> attendanceRecords;

    public SummerProgramAttendance() {
        attendanceRecords = new HashMap<>();
        // For demonstration purposes, initializing attendance records for John Doe
        HashMap<String, String> johnAttendance = new HashMap<>();
        johnAttendance.put("June", "Present");
        johnAttendance.put("July", "Absent");
        attendanceRecords.put("John Doe", johnAttendance);

        String username = login();
        if (username != null) {
            if (username.equals("admin")) {
                initializeMainFrame(username);
            } else {
                initializeStudentFrame(username);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Login failed. Exiting application.", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
    }

    private String login() {
        JPanel loginPanel = new JPanel(new BorderLayout());
        JPanel inputPanel = new JPanel(new GridLayout(2, 2, 5, 5));

        JTextField usernameField = new JTextField(10);
        JPasswordField passwordField = new JPasswordField(10);

        inputPanel.add(new JLabel("Username:"));
        inputPanel.add(usernameField);
        inputPanel.add(new JLabel("Password:"));
        inputPanel.add(passwordField);

        loginPanel.add(inputPanel, BorderLayout.CENTER);

        int result = JOptionPane.showConfirmDialog(null, loginPanel, "Login", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String username = usernameField.getText();
            char[] password = passwordField.getPassword();

            if (authenticate(username, password)) {
                return username;
            }
        }
        return null;
    }

    private boolean authenticate(String username, char[] password) {
        String adminUsername = "admin";
        String adminPassword = "admin";
        String studentUsername = "john";
        String studentPassword = "john";

        return (username.equals(adminUsername) && new String(password).equals(adminPassword)) ||
                (username.equals(studentUsername) && new String(password).equals(studentPassword));
    }

    private void initializeMainFrame(String username) {
        setTitle("Summer at City Hall Attendance Manager");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel selectionPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        selectionPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        studentComboBox = new JComboBox<>(new String[]{"John Doe", "Jane Smith", "Michael Johnson", "Emily Brown", "Daniel Wilson",
                "Sophia Miller", "William Davis", "Olivia Garcia", "Matthew Rodriguez", "Ava Martinez",
                "Jacob Hernandez", "Isabella Lopez", "Ethan Gonzalez", "Mia Perez", "Alexander Moore",
                "Emma Thompson", "James Hall", "Charlotte Lee", "Benjamin Scott", "Amelia White",
                "William Harris", "Victoria Clark", "Michael Lewis", "Grace Turner", "Daniel Walker",
                "Chloe King", "Owen Green", "Abigail Baker", "Joseph Adams", "Harper Carter"
        });
        selectionPanel.add(new JLabel("Select Student:"));
        selectionPanel.add(studentComboBox);

        monthComboBox = new JComboBox<>(new String[]{"June", "July"});
        selectionPanel.add(new JLabel("Select Month:"));
        selectionPanel.add(monthComboBox);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        updateButton = new JButton("Confirm");
        buttonPanel.add(updateButton);

        logoutButton = new JButton("Logout");
        buttonPanel.add(logoutButton);

        mainPanel.add(selectionPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        statusLabel = new JLabel("Select a student and month to update attendance.");
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(statusLabel, BorderLayout.NORTH);

        add(mainPanel);

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedStudent = (String) studentComboBox.getSelectedItem();
                String selectedMonth = (String) monthComboBox.getSelectedItem();
                if (selectedStudent != null && selectedMonth != null) {
                    showCalendarDialog(selectedStudent, selectedMonth);
                } else {
                    JOptionPane.showMessageDialog(null, "Please select a student and a month.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new SummerProgramAttendance().setVisible(true);
            }
        });
    }

    private void initializeStudentFrame(String studentUsername) {
        setTitle("Summer at City Hall - Student Attendance Viewer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        JLabel nameLabel = new JLabel("Welcome, " + studentUsername + "!");
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(nameLabel, BorderLayout.NORTH);

        JTextArea attendanceTextArea = new JTextArea();
        attendanceTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(attendanceTextArea);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new SummerProgramAttendance().setVisible(true);
            }
        });
        mainPanel.add(closeButton, BorderLayout.SOUTH);

        add(mainPanel);

        displayAttendanceRecords(studentUsername, attendanceTextArea);
    }

    private void displayAttendanceRecords(String studentUsername, JTextArea attendanceTextArea) {
        HashMap<String, String> studentAttendance = attendanceRecords.get(studentUsername);
        if (studentAttendance != null) {
            StringBuilder records = new StringBuilder();
            for (String month : studentAttendance.keySet()) {
                String status = studentAttendance.get(month);
                records.append("Month: ").append(month).append(", Status: ").append(status).append("\n");
            }
            attendanceTextArea.setText(records.toString());
        } else {
            attendanceTextArea.setText("No attendance records found for " + studentUsername);
        }
    }

    private void showCalendarDialog(String studentName, String month) {
        JFrame calendarFrame = new JFrame("Attendance Calendar");
        calendarFrame.setSize(400, 300);
        calendarFrame.setLocationRelativeTo(null);

        JPanel calendarPanel = new JPanel(new BorderLayout());
        Calendar calendar = Calendar.getInstance();
        if (month.equals("June")) {
            calendar.set(Calendar.MONTH, Calendar.JUNE);
        } else if (month.equals("July")) {
            calendar.set(Calendar.MONTH, Calendar.JULY);
        }

        monthPanel = new JPanel(new GridLayout(0, 7));
        String[] daysOfWeek = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        for (String day : daysOfWeek) {
            monthPanel.add(new JLabel(day, SwingConstants.CENTER));
        }

        int firstDayOfMonth = calendar.get(Calendar.DAY_OF_WEEK);
        int numberOfDaysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        int currentDay = 1;

        for (int i = 1; i < firstDayOfMonth; i++) {
            monthPanel.add(new JLabel(""));
        }

        for (int i = 1; i <= numberOfDaysInMonth; i++) {
            JLabel dayLabel = new JLabel(String.valueOf(currentDay), SwingConstants.CENTER);
            dayLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    saveAttendance(studentName, month, dayLabel);
                }
            });
            monthPanel.add(dayLabel);
            currentDay++;
        }

        calendarPanel.add(monthPanel, BorderLayout.CENTER);

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calendarFrame.dispose();
            }
        });
        calendarPanel.add(saveButton, BorderLayout.SOUTH);

        calendarFrame.add(calendarPanel);
        calendarFrame.setVisible(true);
    }

    private void saveAttendance(String studentName, String month, JLabel dayLabel) {
        String[] options = {"Present", "Tardy", "Absent"};
        int choice = JOptionPane.showOptionDialog(null, "Select attendance status for day " + dayLabel.getText() + ":", "Select Attendance",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

        if (choice != JOptionPane.CLOSED_OPTION) {
            String attendanceStatus = options[choice];
            System.out.println("Attendance for day " + dayLabel.getText() + ": " + attendanceStatus);
            updateAttendance(studentName, month, attendanceStatus);
            dayLabel.setBackground(getColorForAttendanceStatus(attendanceStatus));
            dayLabel.setOpaque(true);
        }
    }

    private void updateAttendance(String studentName, String month, String attendanceStatus) {
        HashMap<String, String> studentAttendance = attendanceRecords.getOrDefault(studentName, new HashMap<>());
        studentAttendance.put(month, attendanceStatus);
        attendanceRecords.put(studentName, studentAttendance);
        System.out.println("Attendance for " + studentName + " in " + month + " saved.");
    }

    private Color getColorForAttendanceStatus(String attendanceStatus) {
        switch (attendanceStatus) {
            case "Present":
                return new Color(173, 255, 47);
            case "Tardy":
                return new Color(255, 255, 153);
            case "Absent":
                return new Color(255, 99, 71);
            default:
                return Color.WHITE;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new SummerProgramAttendance().setVisible(true);
            }
        });
    }
}
