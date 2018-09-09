package UI;

import Model.TeachingCbModel;
import Database.ConnectDatabase;
import Find.FindTeachers;
import Model.TeachersSearchModel;
import SchoolProject.Teacher;
import java.awt.*;
import javax.swing.*;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;
import javax.swing.border.TitledBorder;

/**
 *
 * @author NMixalis, mpaterakis
 */
public class TeachingDialog extends ToolBarContainer {

    public TeachingDialog(ConnectDatabase dbConnect) {
        this.dbConnect = dbConnect;
        teacher = new Teacher(dbConnect);
        if (teacher.getTeachersLength() > 0) {
            initComponents();
            loadDataToForm();
        }
    }

    private void initComponents() {

        // JPanel
        panelCenter = new JPanel(new GridLayout(5, 2));
        panelCenter.setBorder(new TitledBorder("Teaching"));

        // Labels 
        lidTeacher = new JLabel("    Teacher id:"); // mipos combobox? 
        lLastName = new JLabel("    Last Name:");
        lFirstName = new JLabel("    First Name:");
        lLessons = new JLabel("    Lessons:");
        curDbId = new JLabel();

        // TextFields
        tfIdTeacher = new JTextField();
        tfLastName = new JTextField();
        tfFirstName = new JTextField();
        cbLessons = new JComboBox();

        tfIdTeacher.setEditable(false);
        tfLastName.setEditable(false);
        tfFirstName.setEditable(false);
        
        // Add on center panel
        panelCenter.add(lidTeacher);
        panelCenter.add(tfIdTeacher);
        panelCenter.add(lLastName);
        panelCenter.add(tfLastName);
        panelCenter.add(lFirstName);
        panelCenter.add(tfFirstName);
        panelCenter.add(lLessons);
        panelCenter.add(cbLessons);

        bReplicate.setVisible(false);
        bModify.setVisible(false);
        bFirst.setEnabled(false);
        bPrevious.setEnabled(false);
        bApply.setEnabled(false);
        bCancel.setEnabled(false);

        if (teacher.getTeachersLength() == 0) {
            currentIndex = 0;
        } else {
            currentIndex = 1;
        }

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

        teacher.first();
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
        teacher.previous();
        isAddOrModify = false;
        loadDataToForm();
    }

    @Override
    public void doNext() {
        currentIndex++;
        bPrevious.setEnabled(true);
        bFirst.setEnabled(true);

        if (currentIndex == teacher.getTeachersLength()) {
            bFirst.setEnabled(true);
            bNext.setEnabled(false);
            bLast.setEnabled(false);
        }
        teacher.next();
        isAddOrModify = false;
        loadDataToForm();
    }

    @Override
    public void doLast() {
        bPrevious.setEnabled(true);
        bFirst.setEnabled(true);
        bNext.setEnabled(false);
        bLast.setEnabled(false);

        teacher.last();
        currentIndex = teacher.getTeachersLength();
        isAddOrModify = false;
        loadDataToForm();
    }
    
    @Override
    public void doAdd() {
        bPrevious.setEnabled(false);
        bFirst.setEnabled(false);
        bNext.setEnabled(false);
        bLast.setEnabled(false);
        bAdd.setEnabled(false);
        bDelete.setEnabled(false);
        bSearch.setEnabled(false);
        bTable.setEnabled(false);
        bCancel.setEnabled(true);
        bApply.setEnabled(true);

        isAdd = true;
        isAddOrModify = true;
        loadDataToForm();
    }

    @Override
    public void doCancel() {
        bPrevious.setEnabled(true);
        bFirst.setEnabled(true);
        bNext.setEnabled(true);
        bLast.setEnabled(true);
        bAdd.setEnabled(true);
        bDelete.setEnabled(true);
        bSearch.setEnabled(true);
        bTable.setEnabled(true);
        bCancel.setEnabled(false);
        bApply.setEnabled(false);

        cbLessons.setEnabled(true);

        if (currentIndex == teacher.getTeachersLength()) {
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
        bDelete.setEnabled(true);
        bSearch.setEnabled(true);
        bTable.setEnabled(true);
        bCancel.setEnabled(false);
        bApply.setEnabled(false);

        cbLessons.setEnabled(true);

        String lessonId = cbLessons.getSelectedItem().toString().split(",")[0];

        if (isAdd) {
            teacher.addTeaching(lessonId, tfIdTeacher.getText());
        } 
        if (currentIndex == 1) {
            bNext.setEnabled(true);
            bLast.setEnabled(true);
            bPrevious.setEnabled(false);
            bFirst.setEnabled(false);
        } else if (currentIndex == teacher.getTeachersLength()) {
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
        isAddOrModify = true;

    }

    @Override
    public void doDelete() {
        teacher.deleteTeaching(cbLessons.getSelectedItem().toString().split(",")[0], tfIdTeacher.getText());
        loadDataToForm();
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
    public void doTable() {
        JDialog tableDialog = new JDialog(); // to dialog

        JPanel enrollmentTablePanel = new JPanel(new BorderLayout()); // panel
        enrollmentTable = new JTable(teacher.teachingTable());  // to jtable
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
        if (teacher.getTeachersLength() == 0) {
            bDelete.setEnabled(false);
            bDelete.setEnabled(false);
            bTable.setEnabled(false);
            bSearch.setEnabled(false);
        }
        if (teacher.getTeachersLength() <= 1) {
            bNext.setEnabled(false);
            bLast.setEnabled(false);
            bPrevious.setEnabled(false);
            bFirst.setEnabled(false);
        }

        TeachingCbModel cbModel = new TeachingCbModel(dbConnect, teacher.getIdTeacher(), isAdd);
        cbLessons.setModel(cbModel);

        tfIdTeacher.setText(String.valueOf(teacher.getIdTeacher()));
        tfFirstName.setText(teacher.getFirstName());
        tfLastName.setText(teacher.getLastName());

        if (cbModel.getSize() > 0) {
            cbLessons.setEnabled(true);
            cbLessons.setSelectedIndex(0);
            if (!isAddOrModify) {
                bDelete.setEnabled(true);
            }
        } else {
            cbLessons.setEnabled(false);
            bDelete.setEnabled(false);
        }
        curDbId.setText(currentIndex + "/" + teacher.getTeachersLength());
    }

    private JPanel panelCenter;
    private JLabel lidTeacher, lLastName, lFirstName, lLessons, curDbId;
    private JTextField tfIdTeacher, tfLastName, tfFirstName;
    private JComboBox cbLessons;
    private final Teacher teacher;
    private int currentIndex;
    private boolean isAddOrModify;
    private final ConnectDatabase dbConnect;
    private boolean isAdd;
    private JTable enrollmentTable;
}
