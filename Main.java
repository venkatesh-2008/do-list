import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;

class Task {
    int id;
    String name;
    String description;
    String priority;
    String dueDate;
    boolean completed;

    public Task(int id, String name, String description,
                String priority, String dueDate, boolean completed) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.priority = priority;
        this.dueDate = dueDate;
        this.completed = completed;
    }

    @Override
    public String toString() {
        return id + "," + name + "," + description + "," +
                priority + "," + dueDate + "," + completed;
    }
}

public class Main extends JFrame {

    JTextField txtId = new JTextField();
    JTextField txtName = new JTextField();
    JTextField txtDesc = new JTextField();
    JTextField txtPriority = new JTextField();
    JTextField txtDate = new JTextField();
    JTextField txtSearch = new JTextField();

    JButton btnAdd = new JButton("Add Task");
    JButton btnDelete = new JButton("Delete");
    JButton btnComplete = new JButton("Complete");
    JButton btnSearch = new JButton("Search");
    JButton btnRefresh = new JButton("Refresh");

    DefaultTableModel model;

    ArrayList<Task> tasks = new ArrayList<>();

    final String FILE_NAME = "tasks.txt";

    public Main() {

        setTitle("Smart To-Do List Manager");
        setSize(900,550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel input = new JPanel(new GridLayout(6,2,5,5));

        input.add(new JLabel("Task ID"));
        input.add(txtId);

        input.add(new JLabel("Task Name"));
        input.add(txtName);

        input.add(new JLabel("Description"));
        input.add(txtDesc);

        input.add(new JLabel("Priority"));
        input.add(txtPriority);

        input.add(new JLabel("Due Date"));
        input.add(txtDate);

        input.add(btnAdd);

        input.add(btnRefresh);

        add(input,BorderLayout.NORTH);

        model = new DefaultTableModel();

        model.addColumn("ID");
        model.addColumn("Name");
        model.addColumn("Description");
        model.addColumn("Priority");
        model.addColumn("Due Date");
        model.addColumn("Status");

        JTable table = new JTable(model);

        JScrollPane pane = new JScrollPane(table);

        add(pane,BorderLayout.CENTER);

        JPanel bottom = new JPanel(new GridLayout(1,4,5,5));

        bottom.add(btnDelete);
        bottom.add(btnComplete);
        bottom.add(txtSearch);
        bottom.add(btnSearch);

        add(bottom,BorderLayout.SOUTH);

        loadTasks();
        refreshTable();

        btnAdd.addActionListener(e -> {

            try{

                int id=Integer.parseInt(txtId.getText());

                String name=txtName.getText();
                String desc=txtDesc.getText();
                String priority=txtPriority.getText();
                String date=txtDate.getText();

                tasks.add(new Task(id,name,desc,priority,date,false));

                saveTasks();
                refreshTable();

                clearFields();

                JOptionPane.showMessageDialog(this,"Task Added!");

            }catch(Exception ex){

                JOptionPane.showMessageDialog(this,"Invalid Input");
            }

        });

        btnDelete.addActionListener(e->{

            int row=table.getSelectedRow();

            if(row==-1){

                JOptionPane.showMessageDialog(this,"Select a Task");
                return;
            }

            tasks.remove(row);

            saveTasks();

            refreshTable();

        });

        btnComplete.addActionListener(e->{

            int row=table.getSelectedRow();

            if(row==-1){

                JOptionPane.showMessageDialog(this,"Select a Task");
                return;
            }

            tasks.get(row).completed=true;

            saveTasks();

            refreshTable();

        });

        btnSearch.addActionListener(e->{

            String key=txtSearch.getText().toLowerCase();

            model.setRowCount(0);

            for(Task t:tasks){

                if(t.name.toLowerCase().contains(key)){

                    model.addRow(new Object[]{
                            t.id,
                            t.name,
                            t.description,
                            t.priority,
                            t.dueDate,
                            t.completed?"Completed":"Pending"
                    });

                }

            }

        });

        btnRefresh.addActionListener(e->refreshTable());

    }

    void clearFields(){

        txtId.setText("");
        txtName.setText("");
        txtDesc.setText("");
        txtPriority.setText("");
        txtDate.setText("");

    }

    void refreshTable(){

        model.setRowCount(0);

        for(Task t:tasks){

            model.addRow(new Object[]{

                    t.id,
                    t.name,
                    t.description,
                    t.priority,
                    t.dueDate,
                    t.completed?"Completed":"Pending"

            });

        }

    }

    void saveTasks(){

        try{

            PrintWriter pw=new PrintWriter(new FileWriter(FILE_NAME));

            for(Task t:tasks){

                pw.println(t);

            }

            pw.close();

        }catch(Exception e){

            JOptionPane.showMessageDialog(this,"Error Saving");

        }

    }

    void loadTasks(){

        File file=new File(FILE_NAME);

        if(!file.exists())
            return;

        try{

            BufferedReader br=new BufferedReader(new FileReader(file));

            String line;

            while((line=br.readLine())!=null){

                String[] d=line.split(",");

                tasks.add(new Task(

                        Integer.parseInt(d[0]),
                        d[1],
                        d[2],
                        d[3],
                        d[4],
                        Boolean.parseBoolean(d[5])

                ));

            }

            br.close();

        }catch(Exception e){

            JOptionPane.showMessageDialog(this,"Error Loading");

        }

    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> new Main().setVisible(true));
    }
}
