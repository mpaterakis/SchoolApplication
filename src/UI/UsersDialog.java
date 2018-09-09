package UI;

import Find.FindUsers;
import Database.ConnectDatabase;
import SchoolProject.User;
import Model.UsersSearchModel;
import java.awt.*;
import javax.swing.*;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;
import javax.swing.border.TitledBorder;

/**
 *
 * @author NMixalis, mpaterakis, erald
 */
public class UsersDialog extends ToolBarContainer {

    public UsersDialog(ConnectDatabase dbConnect) {
        user = new User(dbConnect);
        this.dbConnect = dbConnect;
        initComponents();
        loadDataToForm();
    }

    private void initComponents() {

        // JPanel
        panelCenter = new JPanel(new GridLayout(6, 2));
        panelCenter.setBorder(new TitledBorder("Users"));

        // Labels
        lId = new JLabel("    Serial:");
        lLastName = new JLabel("    Lastname:");
        lFirstName = new JLabel("    Firstname:");
        lEmail = new JLabel("    Email:");
        lLogin = new JLabel("    Login:");
        lPassword = new JLabel("    Password:");
        curDbId = new JLabel("0/0");

        // Textfields
        tfIdUser = new JTextField();
        tfLastName = new JTextField();
        tfFirstName = new JTextField();
        tfEmail = new JTextField();
        tfLogin = new JTextField();
        pfPassword = new JPasswordField();

        tfIdUser.setEditable(false);

        // Add on center panel
        panelCenter.add(lId);
        panelCenter.add(tfIdUser);
        panelCenter.add(lLastName);
        panelCenter.add(tfLastName);
        panelCenter.add(lFirstName);
        panelCenter.add(tfFirstName);
        panelCenter.add(lEmail);
        panelCenter.add(tfEmail);
        panelCenter.add(lLogin);
        panelCenter.add(tfLogin);
        panelCenter.add(lPassword);
        panelCenter.add(pfPassword);

        bFirst.setEnabled(false);
        bPrevious.setEnabled(false);
        bApply.setEnabled(false);
        bCancel.setEnabled(false);

        if (user.getUsersLength() == 0) {
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

        user.first();
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
        user.previous();
        loadDataToForm();
    }

    @Override
    public void doNext() {
        currentIndex++;
        bPrevious.setEnabled(true);
        bFirst.setEnabled(true);

        if (currentIndex == user.getUsersLength()) {
            bFirst.setEnabled(true);
            bNext.setEnabled(false);
            bLast.setEnabled(false);
        }
        user.next();
        loadDataToForm();
    }

    @Override
    public void doLast() {
        bPrevious.setEnabled(true);
        bFirst.setEnabled(true);
        bNext.setEnabled(false);
        bLast.setEnabled(false);

        user.last();
        currentIndex = user.getUsersLength();
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

        tfIdUser.setText("");
        tfLastName.setEditable(true);
        tfLastName.setText("");
        tfFirstName.setEditable(true);
        tfFirstName.setText("");
        tfEmail.setEditable(true);
        tfEmail.setText("");
        tfLogin.setEditable(true);
        tfLogin.setText("");

        pfPassword.setEditable(true);
        pfPassword.setText("");
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

        if (currentIndex == user.getUsersLength()) {
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
            user.add(tfLastName.getText(), tfFirstName.getText(), tfEmail.getText(),
                    tfLogin.getText(), passToString());
            currentIndex++;
            doLast();
        } else {
            user.modify(tfIdUser.getText(), tfLastName.getText(), tfFirstName.getText(), tfEmail.getText(),
                    tfLogin.getText(), passToString());
            if (currentIndex == user.getUsersLength()) {
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

        tfLastName.setEditable(true);
        tfFirstName.setEditable(true);
        tfEmail.setEditable(true);
        tfLogin.setEditable(true);
        tfEmail.setEditable(true);
        tfLogin.setEditable(true);
        pfPassword.setEditable(true);
    }

    @Override
    public void doDelete() {
        user.delete();
        if (user.getUsersLength() == 0) {
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
        FindUsers find = new FindUsers(dbConnect.getConnection(), true, new UsersSearchModel(dbConnect.getConnection(), null, true));
        find.setVisible(true);

        int row = find.getRowId();

        if (row != -1) {
            doFirst();
            while (user.getId() != row) {
                doNext();
            }
        }
    }

    @Override
    public void doReplicate() {
        user.add(tfLastName.getText(), tfFirstName.getText(), tfEmail.getText(),
                tfLogin.getText(), passToString());
        currentIndex++;
        doLast();
    }

    @Override
    public void doTable() {
        JDialog tableDialog = new JDialog(); // to dialog

        JPanel usersPanel = new JPanel(new BorderLayout()); // panel
        usersTable = new JTable(user.table());  // to jtable
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
        if (user.getUsersLength() == 0) {
            bReplicate.setEnabled(false);
            bDelete.setEnabled(false);
            bModify.setEnabled(false);
            bTable.setEnabled(false);
            bSearch.setEnabled(false);
        }
        if (user.getUsersLength() <= 1) {
            bNext.setEnabled(false);
            bLast.setEnabled(false);
            bPrevious.setEnabled(false);
            bFirst.setEnabled(false);
        }

        if (user.getUsersLength() == 0) {
            tfIdUser.setText("");
            tfLastName.setText("");
            tfFirstName.setText("");
            tfEmail.setText("");
            tfLogin.setText("");
            pfPassword.setText("");
        }

        if (user.getUsersLength() >= 1) {
            tfIdUser.setText(String.valueOf(user.getId()));
            tfLastName.setText(String.valueOf(user.getLastName()));
            tfFirstName.setText(String.valueOf(user.getFirstName()));
            tfEmail.setText(String.valueOf(user.getEmail()));
            tfLogin.setText(String.valueOf(user.getLogin()));
            pfPassword.setText(String.valueOf(user.getPassword()));
        }

        tfIdUser.setEditable(false);
        tfLastName.setEditable(false);
        tfFirstName.setEditable(false);
        tfEmail.setEditable(false);
        tfLogin.setEditable(false);
        pfPassword.setEditable(false);

        curDbId.setText(currentIndex + "/" + user.getUsersLength());
    }

    private String passToString() {
        char[] pass = pfPassword.getPassword();
        String passShow = "";

        for (int i = 0; i < pass.length; i++) {
            passShow += pass[i];
        }
        return passShow;
    }

    private JPanel panelCenter;
    private JLabel lId, lLastName, lFirstName, lEmail, lLogin, lPassword, curDbId;
    private JTextField tfIdUser, tfLastName, tfFirstName, tfEmail, tfLogin;
    private final User user;
    private boolean isAdd;
    private JTable usersTable;
    private JPasswordField pfPassword;
    private int currentIndex;
    protected final ConnectDatabase dbConnect;

}
