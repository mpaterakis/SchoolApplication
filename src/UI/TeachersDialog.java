package UI;

import Find.FindTeachers;
import Database.ConnectDatabase;
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
public class TeachersDialog extends ToolBarContainer {

    public TeachersDialog(ConnectDatabase dbConnect) {
        this.dbConnect = dbConnect;
        teacher = new Teacher(dbConnect);
        initComponents();
        loadDataToForm();
    }

    private void initComponents() {

        // JPanel
        panelCenter = new JPanel(new GridLayout(5, 2));
        panelCenter.setBorder(new TitledBorder("Teachers"));

        // Labels
        lIdTeacher = new JLabel("    Serial:");
        lUser = new JLabel("    Users:");
        lEidikotita = new JLabel("    Specialization:");
        lEndiaferonta = new JLabel("    Interrests:");
        lTilefono = new JLabel("    Phone:");

        curDbId = new JLabel("0/0");

        // Textfields
        tfIdTeacher = new JTextField();
        cbUsers = new JComboBox();
        tfEidikotita = new JTextField();
        tfEndiaferonta = new JTextField();
        tfTilefono = new JTextField();

        tfIdTeacher.setEditable(false);

        // Add on center panel
        panelCenter.add(lIdTeacher);
        panelCenter.add(tfIdTeacher);
        panelCenter.add(lUser);
        panelCenter.add(cbUsers);
        panelCenter.add(lEidikotita);
        panelCenter.add(tfEidikotita);
        panelCenter.add(lEndiaferonta);
        panelCenter.add(tfEndiaferonta);
        panelCenter.add(lTilefono);
        panelCenter.add(tfTilefono);

        bFirst.setEnabled(false);
        bPrevious.setEnabled(false);
        bApply.setEnabled(false);
        bCancel.setEnabled(false);

        if (teacher.getUsersLength() == 0) {
            currentIndex = 0;
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

        teacher.first();
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
        teacher.previous();
        loadDataToForm();
    }

    @Override
    public void doNext() {
        currentIndex++;
        bPrevious.setEnabled(true);
        bFirst.setEnabled(true);

        if (currentIndex == teacher.getUsersLength()) {
            bFirst.setEnabled(true);
            bNext.setEnabled(false);
            bLast.setEnabled(false);
        }
        teacher.next();
        loadDataToForm();
    }

    @Override
    public void doLast() {
        bPrevious.setEnabled(true);
        bFirst.setEnabled(true);
        bNext.setEnabled(false);
        bLast.setEnabled(false);

        teacher.last();
        currentIndex = teacher.getUsersLength();
        loadDataToForm();
    }

    @Override
    public void doAdd() {
        TeacherCbModel cbModel = new TeacherCbModel(dbConnect, teacher.getIdTeacher(), true);

        if (cbModel.getSize() > 0) {
            cbUsers.setModel(cbModel);
            cbUsers.setEnabled(true);
            cbUsers.setSelectedIndex(0);
            tfIdTeacher.setEnabled(true);
            tfEidikotita.setEnabled(true);
            tfTilefono.setEnabled(true);
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

        tfIdTeacher.setText("");
        tfEidikotita.setText("");
        tfEidikotita.setEditable(true);
        tfTilefono.setText("");
        tfTilefono.setEditable(true);
        tfEndiaferonta.setText("");
        tfEndiaferonta.setEditable(true);
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

        if (currentIndex == teacher.getUsersLength()) {
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
            teacher.addTeacher(cbUsers.getSelectedItem().toString().split(",")[0], tfEidikotita.getText(), tfEndiaferonta.getText(), tfTilefono.getText());
            currentIndex++;
            doLast();
        } else {
            teacher.modifyTeacher(tfIdTeacher.getText(), tfEidikotita.getText(), tfEndiaferonta.getText(), tfTilefono.getText(), cbUsers.getSelectedItem().toString().split(",")[0]);

            if (currentIndex == teacher.getUsersLength()) {
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
        TeacherCbModel cbModel = new TeacherCbModel(dbConnect, teacher.getIdTeacher(), true);

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

        tfEidikotita.setEditable(true);
        tfEndiaferonta.setEditable(true);
        tfTilefono.setEditable(true);
    }

    @Override
    public void doDelete() {
        teacher.delete();
        if (teacher.getUsersLength() == 0) {
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
        FindTeachers find = new FindTeachers(dbConnect.getConnection(), true, new TeachersSearchModel(dbConnect.getConnection(), null, true));
        find.setVisible(true);

        int row = find.getRowId();

        if (row != -1) {
            doFirst();
            while (teacher.getIdTeacher() != row) {
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
        teacher.addTeacher(String.valueOf(user.getId()), tfEidikotita.getText(), tfEndiaferonta.getText(), tfTilefono.getText());
        currentIndex++;
        doLast();
    }

    @Override
    public void doTable() {
        JDialog tableDialog = new JDialog(); // to dialog

        JPanel usersPanel = new JPanel(new BorderLayout()); // panel
        usersTable = new JTable(teacher.table());  // to jtable
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
        if (teacher.getUsersLength() == 0) {
            bDelete.setEnabled(false);
            bReplicate.setEnabled(false);
            bDelete.setEnabled(false);
            bModify.setEnabled(false);
            bTable.setEnabled(false);
            bSearch.setEnabled(false);
        }
        if (teacher.getUsersLength() <= 1) {
            bNext.setEnabled(false);
            bLast.setEnabled(false);
            bPrevious.setEnabled(false);
            bFirst.setEnabled(false);
        }

        TeacherCbModel cbModel = new TeacherCbModel(dbConnect, teacher.getIdTeacher(), false);
        if (teacher.getUsersLength() == 0) {
            cbUsers.setModel(cbModel);
            cbUsers.setEnabled(false);
            tfIdTeacher.setText("");
            tfEidikotita.setText("");
            tfEndiaferonta.setText("");
            tfTilefono.setText("");
        }

        if (teacher.getUsersLength() >= 1) {
            cbUsers.setModel(cbModel);
            cbUsers.setSelectedIndex(0);
            cbUsers.setEnabled(false);
            tfIdTeacher.setText(String.valueOf(teacher.getIdTeacher()));
            tfEidikotita.setText(String.valueOf(teacher.getSpecialty()));
            tfEndiaferonta.setText(String.valueOf(teacher.getInterests()));
            tfTilefono.setText(String.valueOf(teacher.getPhone()));
        }

        tfIdTeacher.setEditable(false);
        tfEidikotita.setEditable(false);
        tfEndiaferonta.setEditable(false);
        tfTilefono.setEditable(false);

        curDbId.setText(currentIndex + "/" + teacher.getUsersLength());
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    private JPanel panelCenter;
    private JLabel lIdTeacher, lUser, lEidikotita, lEndiaferonta, lTilefono, curDbId;
    private JTextField tfIdTeacher, tfEidikotita, tfEndiaferonta, tfTilefono;
    private JComboBox cbUsers;
    private final Teacher teacher;
    private boolean isAdd;
    private JTable usersTable;
    private int currentIndex;
    private final ConnectDatabase dbConnect;
}
