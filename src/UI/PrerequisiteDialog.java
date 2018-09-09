package UI;

import Model.PrerequisiteCbModel;
import Database.ConnectDatabase;
import Find.FindLessons;
import Model.LessonsSearchModel;
import SchoolProject.Lesson;
import java.awt.*;
import javax.swing.*;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;
import javax.swing.border.TitledBorder;

/**
 *
 * @author NMixalis, mpaterakis
 */
public class PrerequisiteDialog extends ToolBarContainer {

    public int getCurrentIndex() {
        return currentIndex;
    }

    public PrerequisiteDialog(ConnectDatabase dbConnect) {
        this.dbConnect = dbConnect;
        lesson = new Lesson(dbConnect);
        initComponents();
        loadDataToForm();
    }

    private void initComponents() {

        // JPanel
        panelCenter = new JPanel(new GridLayout(4, 2));
        panelCenter.setBorder(new TitledBorder("Prerequisite"));

        // Labels 
        lidLesson = new JLabel("    Lesson:"); // mipos combobox? 
        lName = new JLabel("    Name:");
        lDescription = new JLabel("    Description:");
        lPrereq = new JLabel("    Prerequisites:");
        curDbId = new JLabel();

        // Textfields
        tfIdLesson = new JTextField();
        tfName = new JTextField();
        tfDescription = new JTextField();
        cbPrerequisites = new JComboBox();

        tfIdLesson.setEditable(false);
        tfName.setEditable(false);
        tfDescription.setEditable(false);

        // Add on center panel
        panelCenter.add(lidLesson);
        panelCenter.add(tfIdLesson);
        panelCenter.add(lName);
        panelCenter.add(tfName);
        panelCenter.add(lDescription);
        panelCenter.add(tfDescription);
        panelCenter.add(lPrereq);
        panelCenter.add(cbPrerequisites);

        bReplicate.setVisible(false);
        bReplicate.setEnabled(false);
        bFirst.setEnabled(false);
        bPrevious.setEnabled(false);
        bApply.setEnabled(false);
        bCancel.setEnabled(false);

        if (lesson.getLessonLength() == 0) {

            currentIndex = 0;
//                        JOptionPane.showMessageDialog(null, "No lesson records are created!");
        } else {
            currentIndex = 1;
        }

        isAddOrModify = false;

        this.add(panelCenter, BorderLayout.CENTER);
        this.add(curDbId, BorderLayout.SOUTH);
    }

    @Override
    public void doFirst() {
        bPrevious.setEnabled(false);
        bFirst.setEnabled(false);
        bNext.setEnabled(true);
        bLast.setEnabled(true);

        lesson.first();
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
        lesson.previous();
        isAddOrModify = false;
        loadDataToForm();
    }

    @Override
    public void doNext() {
        currentIndex++;
        bPrevious.setEnabled(true);
        bFirst.setEnabled(true);

        if (currentIndex == lesson.getLessonLength()) {
            bFirst.setEnabled(true);
            bNext.setEnabled(false);
            bLast.setEnabled(false);
        }
        lesson.next();
        isAddOrModify = false;
        loadDataToForm();
    }

    @Override
    public void doLast() {
        bPrevious.setEnabled(true);
        bFirst.setEnabled(true);
        bNext.setEnabled(false);
        bLast.setEnabled(false);

        lesson.last();
        currentIndex = lesson.getLessonLength();
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
        bModify.setEnabled(false);
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
        bModify.setEnabled(true);
        bDelete.setEnabled(true);
        bSearch.setEnabled(true);
        bTable.setEnabled(true);
        bCancel.setEnabled(false);
        bApply.setEnabled(false);

        if (currentIndex == lesson.getLessonLength()) {
            bNext.setEnabled(false);
            bLast.setEnabled(false);
        } else if (currentIndex == 1) {
            bPrevious.setEnabled(false);
            bFirst.setEnabled(false);
        }
        isAddOrModify = false;
        loadDataToForm();
    }

    @Override
    public void doApply() {
        bAdd.setEnabled(true);
        bModify.setEnabled(true);
        bDelete.setEnabled(true);
        bSearch.setEnabled(true);
        bTable.setEnabled(true);
        bCancel.setEnabled(false);
        bApply.setEnabled(false);

        String prerequisiteId = cbPrerequisites.getSelectedItem().toString().split(",")[0];

        if (isAdd) {
            lesson.addPrerequisite(tfIdLesson.getText(), prerequisiteId);
        } else {
            lesson.modifyPrerequisite(tfIdLesson.getText(), prerequisiteId);
        }
        if (currentIndex == 1) {
            bNext.setEnabled(true);
            bLast.setEnabled(true);
            bPrevious.setEnabled(false);
            bFirst.setEnabled(false);
        } else if (currentIndex == lesson.getLessonLength()) {
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
        isAddOrModify = false;
        loadDataToForm();
    }

    @Override
    public void doModify() {
        bPrevious.setEnabled(false);
        bFirst.setEnabled(false);
        bNext.setEnabled(false);
        bLast.setEnabled(false);
        bAdd.setEnabled(false);
        bModify.setEnabled(false);
        bDelete.setEnabled(false);
        bSearch.setEnabled(false);
        bTable.setEnabled(false);
        bCancel.setEnabled(true);
        bApply.setEnabled(true);

        isAdd = false;
        isAddOrModify = true;
        loadDataToForm();
    }

    @Override
    public void doDelete() {
        String prerequisiteId = cbPrerequisites.getSelectedItem().toString().split(",")[0];
        lesson.deletePrerequisite(tfIdLesson.getText(), prerequisiteId);

        loadDataToForm();
    }

    @Override
    public void doSearch() {
        FindLessons find = new FindLessons(dbConnect.getConnection(), true, new LessonsSearchModel(dbConnect.getConnection(), null, true));
        find.setVisible(true);

        int row = find.getRowId();

        if (row != -1) {
            doFirst();
            while (lesson.getIdLesson() != row) {
                doNext();
            }
        }
    }

    @Override
    public void doTable() {
        JDialog tableDialog = new JDialog(); // to dialog

        JPanel prerequisiteTablePanel = new JPanel(new BorderLayout()); // panel
        prerequisiteTable = new JTable(lesson.prerequisiteTable());  // to jtable
        JScrollPane prerequisite = new JScrollPane(prerequisiteTable);  // scollpane

        prerequisiteTablePanel.add(prerequisite, BorderLayout.CENTER);
        prerequisiteTable.setFillsViewportHeight(true);
        prerequisiteTable.setDefaultEditor(Object.class, null); // what?      
        tableDialog.add(prerequisiteTablePanel);
        tableDialog.setResizable(false);
        tableDialog.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        tableDialog.setLocationRelativeTo(null);
        tableDialog.pack();
        tableDialog.setModal(true);
        tableDialog.setVisible(true);
    }

    private void loadDataToForm() {
        if (lesson.getLessonLength() == 0) {
            bDelete.setEnabled(false);
            bDelete.setEnabled(false);
            bModify.setEnabled(false);
            bTable.setEnabled(false);
            bSearch.setEnabled(false);
        }
        if (lesson.getLessonLength() <= 1) {
            bNext.setEnabled(false);
            bLast.setEnabled(false);
            bPrevious.setEnabled(false);
            bFirst.setEnabled(false);
        }
        tfIdLesson.setText(String.valueOf(lesson.getIdLesson()));
        tfName.setText(lesson.getName());
        tfDescription.setText(lesson.getDescription());

        PrerequisiteCbModel cbModel = new PrerequisiteCbModel(dbConnect, lesson.getIdLesson(), isAddOrModify);
        cbPrerequisites.setModel(cbModel);

        if (cbModel.getSize() > 0) {
            cbPrerequisites.setEnabled(true);
            cbPrerequisites.setSelectedIndex(0);
            if (!isAddOrModify) {
                bModify.setEnabled(true);
                bDelete.setEnabled(true);
            }
            bAdd.setEnabled(false);
        } else {
            cbPrerequisites.setEnabled(false);
            bModify.setEnabled(false);
            bDelete.setEnabled(false);
            if (!isAddOrModify) {
                bAdd.setEnabled(true);
            }
        }
        curDbId.setText(currentIndex + "/" + lesson.getLessonLength());
    }
    
    private JPanel panelCenter;
    private JLabel lidLesson, lName, lDescription, lPrereq, curDbId;
    private JTextField tfIdLesson, tfName, tfDescription;
    private JComboBox cbPrerequisites;
    private final Lesson lesson;
    private int currentIndex;
    private boolean isAddOrModify;
    private final ConnectDatabase dbConnect;
    private boolean isAdd;
    private JTable prerequisiteTable;
}
