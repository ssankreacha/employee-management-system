import java.io.*;
import java.util.*;

class Employee implements Serializable {
    private int id;
    private String name;
    private String department;
    private double salary;

    // Constructor
    public Employee(int id, String name, String department, double salary) {
        this.id = id;
        this.name = name;
        this.department = department;
        this.salary = salary;
    }

    // Getters & Setters
    public int getId() { return id; }
    public String getName() { return name; }
    public String getDepartment() { return department; }
    public double getSalary() { return salary; }

    public void setDepartment(String department) { this.department = department; }
    public void setSalary(double salary) { this.salary = salary; }

    // Print Employee Details
    @Override
    public String toString() {
        return "ID: " + id + ", Name: " + name + ", Department: " + department + ", Salary: $" + salary;
    }
}

class EmployeeManager {
    private List<Employee> employees;
    private static final String FILE_NAME = "employees.txt";

    public EmployeeManager() {
        this.employees = new ArrayList<>();
        loadFromFile();  // Load data from file if available
    }

    // Add Employee
    public void addEmployee(Employee employee) {
        employees.add(employee);
        saveToFile();
        System.out.println("✅ Employee added successfully!");
    }

    // Remove Employee by ID
    public void removeEmployee(int id) {
        employees.removeIf(emp -> emp.getId() == id);
        saveToFile();
        System.out.println("❌ Employee removed successfully!");
    }

    // View All Employees
    public void listEmployees() {
        if (employees.isEmpty()) {
            System.out.println("⚠ No employees found!");
            return;
        }
        employees.forEach(System.out::println);
    }

    // Search Employee by Name or ID
    public Employee searchEmployee(String query) {
        for (Employee emp : employees) {
            if (emp.getName().equalsIgnoreCase(query) || String.valueOf(emp.getId()).equals(query)) {
                return emp;
            }
        }
        return null;
    }

    // Update Employee Details
    public void updateEmployee(int id, String newDepartment, double newSalary) {
        for (Employee emp : employees) {
            if (emp.getId() == id) {
                emp.setDepartment(newDepartment);
                emp.setSalary(newSalary);
                saveToFile();
                System.out.println("✅ Employee updated successfully!");
                return;
            }
        }
        System.out.println("⚠ Employee not found!");
    }

    // Sort Employees by Name, Salary, or Department
    public void sortEmployees(String criteria) {
        switch (criteria.toLowerCase()) {
            case "name":
                employees.sort(Comparator.comparing(Employee::getName));
                break;
            case "salary":
                employees.sort(Comparator.comparingDouble(Employee::getSalary));
                break;
            case "department":
                employees.sort(Comparator.comparing(Employee::getDepartment));
                break;
            default:
                System.out.println("⚠ Invalid sorting criteria!");
                return;
        }
        saveToFile();
        System.out.println("✅ Employees sorted by " + criteria + "!");
    }

    // Save Employee Data to File
    private void saveToFile() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            out.writeObject(employees);
        } catch (IOException e) {
            System.out.println("❌ Error saving employee data!");
        }
    }

    // Load Employee Data from File
    private void loadFromFile() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            employees = (List<Employee>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            employees = new ArrayList<>(); // Start fresh if file doesn't exist
        }
    }
}

public class EmployeeManagementSystem {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        EmployeeManager manager = new EmployeeManager();

        while (true) {
            System.out.println("\n📌 Employee Management System");
            System.out.println("1️⃣ Add Employee");
            System.out.println("2️⃣ Remove Employee");
            System.out.println("3️⃣ View All Employees");
            System.out.println("4️⃣ Search Employee");
            System.out.println("5️⃣ Update Employee");
            System.out.println("6️⃣ Sort Employees");
            System.out.println("7️⃣ Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            switch (choice) {
                case 1:
                    System.out.print("Enter Employee ID: ");
                    int id = scanner.nextInt();
                    scanner.nextLine();  // Consume newline
                    System.out.print("Enter Name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter Department: ");
                    String department = scanner.nextLine();
                    System.out.print("Enter Salary: ");
                    double salary = scanner.nextDouble();
                    scanner.nextLine();  // Consume newline
                    manager.addEmployee(new Employee(id, name, department, salary));
                    break;
                case 2:
                    System.out.print("Enter Employee ID to Remove: ");
                    int removeId = scanner.nextInt();
                    scanner.nextLine();
                    manager.removeEmployee(removeId);
                    break;
                case 3:
                    manager.listEmployees();
                    break;
                case 4:
                    System.out.print("Enter Employee Name or ID: ");
                    String query = scanner.nextLine();
                    Employee found = manager.searchEmployee(query);
                    if (found != null) {
                        System.out.println("🔍 Employee Found: " + found);
                    } else {
                        System.out.println("⚠ Employee not found!");
                    }
                    break;
                case 5:
                    System.out.print("Enter Employee ID to Update: ");
                    int updateId = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Enter New Department: ");
                    String newDept = scanner.nextLine();
                    System.out.print("Enter New Salary: ");
                    double newSalary = scanner.nextDouble();
                    manager.updateEmployee(updateId, newDept, newSalary);
                    break;
                case 6:
                    System.out.print("Sort by (name/salary/department): ");
                    String criteria = scanner.nextLine();
                    manager.sortEmployees(criteria);
                    break;
                case 7:
                    System.out.println("🔴 Exiting... Goodbye!");
                    scanner.close();
                    return;
                default:
                    System.out.println("⚠ Invalid choice! Try again.");
            }
        }
    }
}
