package UI;

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
public class LessonsDialog extends ToolBarContainer {
    
    public LessonsDialog(ConnectDatabase dbConnect) {  
        this.dbConnect = dbConnect;
        lesson = new Lesson(dbConnect);
        initComponents();
        loadDataToForm();
    }

    private void initComponents() {

        // JPanel
        panelCenter = new JPanel(new GridLayout(3, 2));
        panelCenter.setBorder(new TitledBorder("Lessons"));

        lidLesson = new JLabel("    Lesson:");
        lName = new JLabel("    Name:");
        lDescription = new JLabel("    Description:");
        curDbId = new JLabel();

        // TextFields
        tfIdLesson = new JTextField();
        tfName = new JTextField();
        tfDescription = new JTextField();
        tfIdLesson.setEditable(false);

        // Add on center panel
        panelCenter.add(lidLesson);
        panelCenter.add(tfIdLesson);
        panelCenter.add(lName);
        panelCenter.add(tfName);
        panelCenter.add(lDescription);
        panelCenter.add(tfDescription);

        bFirst.setEnabled(false);
        bPrevious.setEnabled(false);
        bApply.setEnabled(false);
        bCancel.setEnabled(false);
        
        if (lesson.getLessonLength() == 0) {
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
       
        lesson.first();
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
        lesson.previous();
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
        bReplicate.setEnabled(false);
        bTable.setEnabled(false);
        bCancel.setEnabled(true);
        bApply.setEnabled(true);
        
        isAdd = true;
        
        tfName.setEditable(true);
        tfName.setText("");
        tfDescription.setEditable(true);
        tfDescription.setText("");
        tfIdLesson.setText("");
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

        if (currentIndex == lesson.getLessonLength()) {
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
            lesson.add(tfName.getText(), tfDescription.getText());
            currentIndex++;
            doLast();
        } else {
            lesson.modify(tfIdLesson.getText(), tfName.getText(), tfDescription.getText());
            if (currentIndex == lesson.getLessonLength()) {
                doLast();
            } else if (currentIndex == 1) {
                doFirst();
            } else {
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
            }
        }
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
        bReplicate.setEnabled(false);
        bTable.setEnabled(false);
        bCancel.setEnabled(true);
        bApply.setEnabled(true);
        
        isAdd = false;
        
        tfName.setEditable(true);
        tfDescription.setEditable(true);

    }

    @Override
    public void doDelete() {
        lesson.delete();
        if (lesson.getLessonLength() == 0) {
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
    public void doReplicate() {
        lesson.add(tfName.getText(), tfDescription.getText());
        currentIndex++;
        doLast();
    }

    @Override
    public void doTable() {
        JDialog tableDialog = new JDialog(); // to dialog

        JPanel lessonTablePanel = new JPanel(new BorderLayout()); // panel
        lessonTable = new JTable(lesson.table());  // to jtable
        JScrollPane lessonScroll = new JScrollPane(lessonTable);  // scollpane
        lessonTablePanel.add(lessonScroll, BorderLayout.CENTER);
        lessonTable.setFillsViewportHeight(true);
        lessonTable.setDefaultEditor(Object.class, null); // what?      
        tableDialog.add(lessonTablePanel);
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
            bReplicate.setEnabled(false);
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
        
        if (lesson.getLessonLength() == 0) {
            tfIdLesson.setText("");
            tfName.setText("");
            tfDescription.setText("");
        }
        
        if (lesson.getLessonLength() >= 1) {
            tfIdLesson.setText(String.valueOf(lesson.getIdLesson()));
            tfName.setText(lesson.getName());
            tfDescription.setText(lesson.getDescription());
        }

        tfName.setEditable(false);
        tfDescription.setEditable(false);

        curDbId.setText(currentIndex + "/" + lesson.getLessonLength());
    }
    
    private JPanel panelCenter;
    private JLabel lidLesson, lName, lDescription, curDbId;
    private JTextField tfIdLesson, tfName, tfDescription;
    private Lesson lesson;
    private int currentIndex;
    private boolean isAdd;
    private JTable lessonTable;
    private final ConnectDatabase dbConnect;
}
