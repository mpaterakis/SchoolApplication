package UI;

import Database.ConnectDatabase;
import Find.FindStudents;
import Model.*;
import SchoolProject.*;
import java.awt.*;
import java.sql.*;
import javax.swing.*;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;
import javax.swing.border.TitledBorder;

/**
 *
 * @author NMixalis, mpaterakis
 */
public class StudentsDialog extends ToolBarContainer {

    public StudentsDialog(ConnectDatabase dbConnect) {
        this.dbConnect = dbConnect;
        student = new Student(dbConnect);
        initComponents();
        loadDataToForm();
    }

    private void initComponents() {

        // JPanel
        panelCenter = new JPanel(new GridLayout(4, 2));
        panelCenter.setBorder(new TitledBorder("Students"));

        // Labels
        lIdStudent = new JLabel("    Serial : ");
        lUser = new JLabel("    User :");
        lAm = new JLabel("    AM :");
        lEksamino = new JLabel("    Eksamino :");
        curDbId = new JLabel();

        // Textfields
        tfIdStudent = new JTextField();
        cbUsers = new JComboBox();
        tfAm = new JTextField();
        tfEksamino = new JTextField();

        tfIdStudent.setEditable(false);

        // Add on center panel
        panelCenter.add(lIdStudent);
        panelCenter.add(tfIdStudent);
        panelCenter.add(lUser);
        panelCenter.add(cbUsers);
        panelCenter.add(lAm);
        panelCenter.add(tfAm);
        panelCenter.add(lEksamino);
        panelCenter.add(tfEksamino);

        bFirst.setEnabled(false);
        bPrevious.setEnabled(false);
        bApply.setEnabled(false);
        bCancel.setEnabled(false);

        if (student.getStudentsLength() == 0) {
            currentIndex = 0;
            tfIdStudent.setText("");
            tfEksamino.setText("");

        } else {
            currentIndex = 1;
        }

        this.add(panelCenter, BorderLayout.CENTER);
        this.add(curDbId, BorderLayout.SOUTH);
    }

    @Override
    public void doFirst() {
        bPrevious.setEnabled(false);
        bFirst.setEnabled(false);
        bNext.setEnabled(true);
        bLast.setEnabled(true);

        student.first();
        currentIndex = 1;
        loadDataToForm();
    }

    @Override
    public void doPrevious() {
        currentIndex--;
        bLast.setEnabled(true);
        bNext.setEnabled(true);

        if (currentIndex == 1) {
            bPrevious.setEnabled(false);
            bFirst.setEnabled(false);
        }
        student.previous();
        loadDataToForm();
    }

    @Override
    public void doNext() {
        currentIndex++;
        bPrevious.setEnabled(true);
        bFirst.setEnabled(true);

        if (currentIndex == student.getUsersLength()) {
            bFirst.setEnabled(true);
            bNext.setEnabled(false);
            bLast.setEnabled(false);
        }
        student.next();
        loadDataToForm();
    }

    @Override
    public void doLast() {
        bPrevious.setEnabled(true);
        bFirst.setEnabled(true);
        bNext.setEnabled(false);
        bLast.setEnabled(false);

        student.last();
        currentIndex = student.getUsersLength();
        loadDataToForm();
    }

    @Override
    public void doAdd() {
        StudentCbModel cbModel = new StudentCbModel(dbConnect, student.getIdStudent(), true);

        if (cbModel.getSize() > 0) {
            cbUsers.setModel(cbModel);
            cbUsers.setEnabled(true);
            cbUsers.setSelectedIndex(0);
            tfIdStudent.setEnabled(true);
            tfAm.setEnabled(true);
            tfEksamino.setEnabled(true);
        } else {
            JOptionPane.showMessageDialog(null, "There are no users left that are not already assigned to a student or teacher.");
            return;
        }

        bPrevious.setEnabled(false);
        bFirst.setEnabled(false);
        bNext.setEnabled(false);
        bLast.setEnabled(false);
        bAdd.setEnabled(false);
        bModify.setEnabled(false);
        bDelete.setEnabled(false);
        bSearch.setEnabled(false);
        bReplicate.setEnabled(false);
        bTable.setEnabled(false);
        bCancel.setEnabled(true);
        bApply.setEnabled(true);

        isAdd = true;

        tfIdStudent.setText("");
        tfAm.setText("");
        tfAm.setEditable(true);
        tfEksamino.setText("");
        tfEksamino.setEditable(true);
    }

    @Override
    public void doCancel() {
        bPrevious.setEnabled(true);
        bFirst.setEnabled(true);
        bNext.setEnabled(true);
        bLast.setEnabled(true);
        bAdd.setEnabled(true);
        bModify.setEnabled(true);
        bDelete.setEnabled(true);
        bSearch.setEnabled(true);
        bReplicate.setEnabled(true);
        bTable.setEnabled(true);
        bCancel.setEnabled(false);
        bApply.setEnabled(false);

        if (currentIndex == student.getUsersLength()) {
            bNext.setEnabled(false);
            bLast.setEnabled(false);
        } else if (currentIndex == 1) {
            bPrevious.setEnabled(false);
            bFirst.setEnabled(false);
        }

        loadDataToForm();
    }

    @Override
    public void doApply() {
        bAdd.setEnabled(true);
        bModify.setEnabled(true);
        bDelete.setEnabled(true);
        bSearch.setEnabled(true);
        bReplicate.setEnabled(true);
        bTable.setEnabled(true);
        bCancel.setEnabled(false);
        bApply.setEnabled(false);

        if (isAdd) {
            student.addStudent(cbUsers.getSelectedItem().toString().split(",")[0], tfAm.getText(), tfEksamino.getText());
            currentIndex++;
            doLast();
        } else {
            student.modifyStudent(tfIdStudent.getText(), tfAm.getText(), tfEksamino.getText(), cbUsers.getSelectedItem().toString().split(",")[0]);

            if (currentIndex == student.getUsersLength()) {
                doLast();
            } else if (currentIndex == 1) {
                doFirst();
            } else {
                bPrevious.setEnabled(true);
                bFirst.setEnabled(true);
                bNext.setEnabled(true);
                bLast.setEnabled(true);
            }
        }
    }

    @Override
    public void doModify() {
        StudentCbModel cbModel = new StudentCbModel(dbConnect, student.getIdStudent(), true);

//        cbModel.addElement("hi");
//        cbUsers.setModel(cbModel);
        cbUsers.setEnabled(false);
//        cbUsers.setSelectedIndex(0);

        bPrevious.setEnabled(false);
        bFirst.setEnabled(false);
        bNext.setEnabled(false);
        bLast.setEnabled(false);
        bAdd.setEnabled(false);
        bModify.setEnabled(false);
        bDelete.setEnabled(false);
        bSearch.setEnabled(false);
        bReplicate.setEnabled(false);
        bTable.setEnabled(false);
        bCancel.setEnabled(true);
        bApply.setEnabled(true);

        isAdd = false;

        tfAm.setEditable(true);
        tfEksamino.setEditable(true);
    }

    @Override
    public void doDelete() {
        student.delete();
        if (student.getUsersLength() == 0) {
            currentIndex = 0;
            loadDataToForm();
        } else if (currentIndex > 1) {
            currentIndex--;
            loadDataToForm();
        } else {
            doFirst();
        }
    }

    @Override
    public void doSearch() {
        FindStudents find = new FindStudents(dbConnect.getConnection(), true, new StudentsSearchModel(dbConnect.getConnection(), null, true));
        find.setVisible(true);

        int row = find.getRowId();
        System.out.println(row);

        if (row != -1) {
            doFirst();
            while (student.getIdStudent() != row) {
                doNext();
            }
        }
    }

    @Override
    public void doReplicate() {
        User user = new User(dbConnect);
        try {
            Statement statement = dbConnect.getConnection().createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            String query = "select * from users where idusers = " + cbUsers.getSelectedItem().toString().split(",")[0];
            ResultSet usersRs = statement.executeQuery(query);
            usersRs.last();
            user.add(usersRs.getString("lastname"), usersRs.getString("firstname"),
                    usersRs.getString("email"), usersRs.getString("login"), usersRs.getString("password"));
            user.last();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
            System.exit(1);
        }
        student.addStudent(String.valueOf(user.getId()), tfAm.getText(), tfEksamino.getText());
        currentIndex++;
        doLast();
    }

    @Override
    public void doTable() {
        JDialog tableDialog = new JDialog(); // to dialog

        JPanel usersPanel = new JPanel(new BorderLayout()); // panel
        usersTable = new JTable(student.table());  // to jtable
        JScrollPane usersScroll = new JScrollPane(usersTable);  // scollpane
        usersPanel.add(usersScroll, BorderLayout.CENTER);
        usersTable.setFillsViewportHeight(true);
        usersTable.setDefaultEditor(Object.class, null); // what?      
        tableDialog.add(usersPanel);
        tableDialog.setResizable(false);
        tableDialog.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        tableDialog.setLocationRelativeTo(null);
        tableDialog.pack();
        tableDialog.setModal(true);
        tableDialog.setVisible(true);
    }

    private void loadDataToForm() {
        if (student.getUsersLength() == 0) {
            bReplicate.setEnabled(false);
            bDelete.setEnabled(false);
            bModify.setEnabled(false);
            bTable.setEnabled(false);
            bSearch.setEnabled(false);
            bCancel.setEnabled(false);
            bApply.setEnabled(false);
        }
        if (student.getUsersLength() <= 1) {
            bNext.setEnabled(false);
            bLast.setEnabled(false);
            bPrevious.setEnabled(false);
            bFirst.setEnabled(false);
        }

        StudentCbModel cbModel = new StudentCbModel(dbConnect, student.getIdStudent(), false);
        if (student.getUsersLength() == 0) {
            cbUsers.setModel(cbModel);
            cbUsers.setEnabled(false);
            tfIdStudent.setText("");
            tfAm.setText("");            
            tfEksamino.setText("");            
        }

        if (student.getUsersLength() >= 1) {
            cbUsers.setModel(cbModel);
            cbUsers.setSelectedIndex(0);
            cbUsers.setEnabled(false);
            tfIdStudent.setText(String.valueOf(student.getIdStudent()));
            tfAm.setText(String.valueOf(student.getAm()));
            tfEksamino.setText(String.valueOf(student.getExamino()));
        }

        tfIdStudent.setEditable(false);
        tfAm.setEditable(false);
        tfEksamino.setEditable(false);

        curDbId.setText(currentIndex + "/" + student.getUsersLength());
    }

    public int getCurrentIndex() {
        return currentIndex;
    }
    
    private JPanel panelCenter;
    private JLabel lIdStudent, lUser, lAm, lEksamino, curDbId;
    private JTextField tfIdStudent, tfAm, tfEksamino;
    private JComboBox cbUsers;
    private final Student student;
    private boolean isAdd;
    private JTable usersTable;
    private int currentIndex;
    private final ConnectDatabase dbConnect;
}
