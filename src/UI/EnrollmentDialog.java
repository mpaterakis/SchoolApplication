package UI;

import Model.*;
import Database.ConnectDatabase;
import Find.FindStudents;
import SchoolProject.Student;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;
import javax.swing.border.TitledBorder;

/**
 *
 * @author NMixalis, mpaterakis
 */
public class EnrollmentDialog extends ToolBarContainer {

    private JPanel panelCenter;
    private JLabel lidStudent, lLastName, lFirstName, lGrade, lStatus, lLessons, curDbId;
    private JTextField tfIdStudent, tfLastName, tfFirstName, tfGrade, tfStatus;
    private JComboBox cbLessons;
    private final Student student;
    private int currentIndex;
    private boolean isAddOrModify;
    private final ConnectDatabase dbConnect;
    private boolean isAdd;
    private JTable enrollmentTable;

    public EnrollmentDialog(ConnectDatabase dbConnect) {
        this.dbConnect = dbConnect;
        student = new Student(dbConnect);
        if (student.getStudentsLength() > 0) {
            initComponents();
            loadDataToForm();
        }
    }

    private void initComponents() {

        // JPanel
        panelCenter = new JPanel(new GridLayout(7, 2));
        panelCenter.setBorder(new TitledBorder("Enrollment"));

        // Labels 
        lidStudent = new JLabel("    Student id:"); // mipos combobox? 
        lLastName = new JLabel("    Last Name:");
        lFirstName = new JLabel("    First Name:");
        lLessons = new JLabel("    Lessons:");
        lGrade = new JLabel("    Grade:");
        lStatus = new JLabel("    Status:");
        curDbId = new JLabel();

        // TextFields
        tfIdStudent = new JTextField();
        tfLastName = new JTextField();
        tfFirstName = new JTextField();
        tfGrade = new JTextField();
        tfStatus = new JTextField();
        cbLessons = new JComboBox();

        tfIdStudent.setEditable(false);
        tfLastName.setEditable(false);
        tfFirstName.setEditable(false);
        tfGrade.setEditable(false);
        tfStatus.setEditable(false);

        // Add on center panel
        panelCenter.add(lidStudent);
        panelCenter.add(tfIdStudent);
        panelCenter.add(lLastName);
        panelCenter.add(tfLastName);
        panelCenter.add(lFirstName);
        panelCenter.add(tfFirstName);
        panelCenter.add(lGrade);
        panelCenter.add(tfGrade);
        panelCenter.add(lStatus);
        panelCenter.add(tfStatus);
        panelCenter.add(lLessons);
        panelCenter.add(cbLessons);

        bReplicate.setEnabled(false);
        bReplicate.setVisible(false);
        bFirst.setEnabled(false);
        bPrevious.setEnabled(false);
        bApply.setEnabled(false);
        bCancel.setEnabled(false);

        if (student.getStudentsLength() == 0) {
            currentIndex = 0;
        } else {
            currentIndex = 1;
        }

        cbLessons.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (!cbLessons.getSelectedItem().toString().isEmpty()) {
                    if (!isAdd) {
                        tfGrade.setText(String.valueOf(student.getGrade(cbLessons.getSelectedItem().toString().split(",")[0])));
                        tfStatus.setText(String.valueOf(student.getStatus(cbLessons.getSelectedItem().toString().split(",")[0])));
                    }
                } else {
                    tfGrade.setText("");
                    tfStatus.setText("");
                }
            }
        });

        isAddOrModify = false;

        this.add(panelCenter, BorderLayout.CENTER);
        this.add(curDbId, BorderLayout.SOUTH);
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    @Override
    public void doFirst() {
        bPrevious.setEnabled(false);
        bFirst.setEnabled(false);
        bNext.setEnabled(true);
        bLast.setEnabled(true);

        student.first();
        currentIndex = 1;
        isAddOrModify = false;
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
        isAddOrModify = false;
        loadDataToForm();
    }

    @Override
    public void doNext() {
        currentIndex++;
        bPrevious.setEnabled(true);
        bFirst.setEnabled(true);

        if (currentIndex == student.getStudentsLength()) {
            bFirst.setEnabled(true);
            bNext.setEnabled(false);
            bLast.setEnabled(false);
        }
        student.next();
        isAddOrModify = false;
        loadDataToForm();
    }

    @Override
    public void doLast() {
        bPrevious.setEnabled(true);
        bFirst.setEnabled(true);
        bNext.setEnabled(false);
        bLast.setEnabled(false);

        student.last();
        currentIndex = student.getStudentsLength();
        isAddOrModify = false;
        loadDataToForm();
    }

    @Override
    public void doAdd() {
        tfGrade.setEditable(true);
        tfStatus.setEditable(true);
        tfGrade.setText("");
        tfStatus.setText("");

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
        isAddOrModify = true;
        loadDataToForm();
    }

    @Override
    public void doCancel() {
        tfGrade.setEditable(false);
        tfStatus.setEditable(false);

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

        cbLessons.setEnabled(true);

        if (currentIndex == student.getStudentsLength()) {
            bNext.setEnabled(false);
            bLast.setEnabled(false);
        } else if (currentIndex == 1) {
            bPrevious.setEnabled(false);
            bFirst.setEnabled(false);
        }

        isAdd = false;
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

        tfGrade.setEditable(false);
        tfStatus.setEditable(false);
        cbLessons.setEnabled(true);

        String lessonId = cbLessons.getSelectedItem().toString().split(",")[0];

        if (isAdd) {
            student.addEnrollment(lessonId, tfIdStudent.getText(), tfGrade.getText(), tfStatus.getText());
        } else {
            student.modifyEnrollment(lessonId, tfIdStudent.getText(), tfGrade.getText(), tfStatus.getText());
        }
        if (currentIndex == 1) {
            bNext.setEnabled(true);
            bLast.setEnabled(true);
            bPrevious.setEnabled(false);
            bFirst.setEnabled(false);
        } else if (currentIndex == student.getStudentsLength()) {
            bNext.setEnabled(false);
            bLast.setEnabled(false);
            bPrevious.setEnabled(true);
            bFirst.setEnabled(true);
        } else {
            bNext.setEnabled(true);
            bLast.setEnabled(true);
            bPrevious.setEnabled(true);
            bFirst.setEnabled(true);
        }

        isAdd = false;
        loadDataToForm();
    }

    @Override
    public void doModify() {
        tfGrade.setEditable(true);
        tfStatus.setEditable(true);

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

        cbLessons.setEnabled(false);

        isAdd = false;
        isAddOrModify = true;
        loadDataToForm();
    }

    @Override
    public void doDelete() {
        student.deleteEnrollment(cbLessons.getSelectedItem().toString().split(",")[0], tfIdStudent.getText());
        loadDataToForm();
    }

    @Override
    public void doSearch() {
        FindStudents find = new FindStudents(dbConnect.getConnection(), true, new StudentsSearchModel(dbConnect.getConnection(), null, true));
        find.setVisible(true);

        int row = find.getRowId();

        if (row != -1) {
            doFirst();
            while (student.getIdStudent() != row) {
                doNext();
            }
        }
    }

    @Override
    public void doTable() {
        JDialog tableDialog = new JDialog(); // to dialog

        JPanel enrollmentTablePanel = new JPanel(new BorderLayout()); // panel
        enrollmentTable = new JTable(student.enrollmentTable());  // to jtable
        JScrollPane enrollmentSP = new JScrollPane(enrollmentTable);  // scollpane

        enrollmentTablePanel.add(enrollmentSP, BorderLayout.CENTER);
        enrollmentTable.setFillsViewportHeight(true);
        enrollmentTable.setDefaultEditor(Object.class, null); // what?      
        tableDialog.add(enrollmentTablePanel);
        tableDialog.setResizable(false);
        tableDialog.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        tableDialog.setLocationRelativeTo(null);
        tableDialog.pack();
        tableDialog.setModal(true);
        tableDialog.setVisible(true);
    }

    private void loadDataToForm() {
        if (student.getStudentsLength() == 0) {
            bDelete.setEnabled(false);
            bReplicate.setEnabled(false);
            bDelete.setEnabled(false);
            bModify.setEnabled(false);
            bTable.setEnabled(false);
            bSearch.setEnabled(false);
        }
        if (student.getStudentsLength() <= 1) {
            bNext.setEnabled(false);
            bLast.setEnabled(false);
            bPrevious.setEnabled(false);
            bFirst.setEnabled(false);
        }

        EnrollmentCbModel cbModel = new EnrollmentCbModel(dbConnect, student.getIdStudent(), isAdd);
        cbLessons.setModel(cbModel);

        tfIdStudent.setText(String.valueOf(student.getIdStudent()));
        tfFirstName.setText(student.getFirstName());
        tfLastName.setText(student.getLastName());

        if (cbModel.getSize() > 0) {
            cbLessons.setEnabled(true);
            cbLessons.setSelectedIndex(0);
            if (!isAddOrModify) {
                bModify.setEnabled(true);
                bDelete.setEnabled(true);
            }
            if (!isAdd) {
                tfGrade.setText(String.valueOf(student.getGrade(cbLessons.getSelectedItem().toString().split(",")[0])));
                tfStatus.setText(String.valueOf(student.getStatus(cbLessons.getSelectedItem().toString().split(",")[0])));
            }
        } else {
            tfGrade.setText("");
            tfStatus.setText("");
            cbLessons.setEnabled(false);
            bModify.setEnabled(false);
            bDelete.setEnabled(false);
        }
        curDbId.setText(currentIndex + "/" + student.getStudentsLength());
    }
}
