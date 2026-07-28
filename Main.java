import java.io.*;
import java.util.*;

class Task {
    private int id;
    private String name;
    private String description;
    private String priority;
    private String dueDate;
    private boolean completed;

    public Task(int id, String name, String description, String priority, String dueDate, boolean completed) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.priority = priority;
        this.dueDate = dueDate;
        this.completed = completed;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPriority() {
        return priority;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public void display() {
        System.out.println("----------------------------------------");
        System.out.println("Task ID      : " + id);
        System.out.println("Task Name    : " + name);
        System.out.println("Description  : " + description);
        System.out.println("Priority     : " + priority);
        System.out.println("Due Date     : " + dueDate);
        System.out.println("Status       : " + (completed ? "Completed" : "Pending"));
    }

    @Override
    public String toString() {
        return id + "," + name + "," + description + "," + priority + "," + dueDate + "," + completed;
    }
}

public class Main {

    static ArrayList<Task> tasks = new ArrayList<>();
    static Scanner sc = new Scanner(System.in);
    static final String FILE_NAME = "tasks.txt";

    public static void main(String[] args) {

        loadTasks();

        while (true) {

            System.out.println("\n==============================");
            System.out.println(" SMART TO-DO LIST MANAGER");
            System.out.println("==============================");
            System.out.println("1. Add Task");
            System.out.println("2. View Tasks");
            System.out.println("3. Delete Task");
            System.out.println("4. Complete Task");
            System.out.println("5. Search Task");
            System.out.println("6. Exit");
            System.out.print("Enter Choice: ");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {

                case 1:
                    addTask();
                    break;

                case 2:
                    viewTasks();
                    break;

                case 3:
                    deleteTask();
                    break;

                case 4:
                    completeTask();
                    break;

                case 5:
                    searchTask();
                    break;

                case 6:
                    saveTasks();
                    System.out.println("Thank You!");
                    System.exit(0);

                default:
                    System.out.println("Invalid Choice!");
            }
        }
    }

    static void addTask() {

        System.out.print("Task ID: ");
        int id = sc.nextInt();
        sc.nextLine();

        System.out.print("Task Name: ");
        String name = sc.nextLine();

        System.out.print("Description: ");
        String desc = sc.nextLine();

        System.out.print("Priority (High/Medium/Low): ");
        String priority = sc.nextLine();

        System.out.print("Due Date: ");
        String date = sc.nextLine();

        Task task = new Task(id, name, desc, priority, date, false);

        tasks.add(task);

        saveTasks();

        System.out.println("Task Added Successfully!");
    }

    static void viewTasks() {

        if (tasks.isEmpty()) {
            System.out.println("No Tasks Available.");
            return;
        }

        for (Task t : tasks) {
            t.display();
        }
    }

    static void deleteTask() {

        System.out.print("Enter Task ID: ");
        int id = sc.nextInt();

        Iterator<Task> iterator = tasks.iterator();

        while (iterator.hasNext()) {

            Task t = iterator.next();

            if (t.getId() == id) {

                iterator.remove();
                saveTasks();

                System.out.println("Task Deleted Successfully!");
                return;
            }
        }

        System.out.println("Task Not Found!");
    }

    static void completeTask() {

        System.out.print("Enter Task ID: ");
        int id = sc.nextInt();

        for (Task t : tasks) {

            if (t.getId() == id) {

                t.setCompleted(true);
                saveTasks();

                System.out.println("Task Completed!");
                return;
            }
        }

        System.out.println("Task Not Found!");
    }

    static void searchTask() {

        System.out.print("Enter Task Name: ");
        String keyword = sc.nextLine();

        boolean found = false;

        for (Task t : tasks) {

            if (t.getName().toLowerCase().contains(keyword.toLowerCase())) {

                t.display();
                found = true;
            }
        }

        if (!found)
            System.out.println("Task Not Found.");
    }

    static void saveTasks() {

        try {

            PrintWriter pw = new PrintWriter(new FileWriter(FILE_NAME));

            for (Task t : tasks) {
                pw.println(t);
            }

            pw.close();

        } catch (IOException e) {

            System.out.println("Error Saving File.");
        }
    }

    static void loadTasks() {

        File file = new File(FILE_NAME);

        if (!file.exists())
            return;

        try {

            Scanner fileScanner = new Scanner(file);

            while (fileScanner.hasNextLine()) {

                String[] data = fileScanner.nextLine().split(",");

                Task task = new Task(
                        Integer.parseInt(data[0]),
                        data[1],
                        data[2],
                        data[3],
                        data[4],
                        Boolean.parseBoolean(data[5]));

                tasks.add(task);
            }

            fileScanner.close();

        } catch (Exception e) {

            System.out.println("Error Loading Tasks.");
        }
    }
}