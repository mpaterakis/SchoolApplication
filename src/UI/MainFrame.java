package UI;

import Database.ConnectDatabase;
import SchoolProject.*;
import java.awt.Image;
import javax.swing.*;
import java.awt.event.*;

/**
 *
 * @author NMixalis, mpaterakis
 */
public class MainFrame extends JFrame {

    public MainFrame() {
        initComponents();
    }

    private void initComponents() {
        // Connect to DB
        dbConnect = new ConnectDatabase("localhost:3307/schooldb", "root", "root");

        // Background image
        backgroundImage = new ImageIcon("icons\\teicrete.jpg");
        bgLabel = new JLabel(backgroundImage, JLabel.CENTER);

        // Menu bar
        menuBar = new JMenuBar();

        // Menu bar options
        pMenu = new JMenu("Persons");
        lMenu = new JMenu("Lessons");
        aMenu = new JMenu("Assigments");
        hMenu = new JMenu("Help");

        // User-related options
        usersMenuItem = new JMenuItem("Users");
        teacherMenuItem = new JMenuItem("Teachers");
        studentMenuItem = new JMenuItem("Students");
        exitMenuItem = new JMenuItem("Exit");

        // Lesson-related options     
        lessonMenuItem = new JMenuItem("Lessons");
        prerequisitesMenuItem = new JMenuItem("Prerequisites");

        // Assigment options
        teachingMenuItem = new JMenuItem("Teaching");
        enrollMenuItem = new JMenuItem("Enrollment");

        // Add background
        add(bgLabel);

        // Add user-related menu items
        pMenu.add(usersMenuItem);
        pMenu.add(teacherMenuItem);
        pMenu.add(studentMenuItem);
        pMenu.add(exitMenuItem);

        // Add lesson-related menu items
        lMenu.add(lessonMenuItem);
        lMenu.add(prerequisitesMenuItem);

        // Add assignment menu items
        aMenu.add(teachingMenuItem);
        aMenu.add(enrollMenuItem);

        // Add all sub-menus to menuBar
        menuBar.add(pMenu);
        menuBar.add(lMenu);
        menuBar.add(aMenu);
        menuBar.add(hMenu);

        setJMenuBar(menuBar);

        // Adding component listener for resizing        
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                Image image = backgroundImage.getImage();
                Image newimg = image.getScaledInstance(bgLabel.getWidth(), bgLabel.getHeight(), Image.SCALE_FAST);
                ImageIcon backgroundImage = new ImageIcon(newimg);
                bgLabel.setIcon(backgroundImage);
            }
        });

        // Adding Button ActionListeners        
        exitMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        JFrame mainFrame = this;

        usersMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JDialog openedDialog = new JDialog();
                openedDialog.add(new UsersDialog(dbConnect));
                openedDialog.setLocationRelativeTo(mainFrame);
                openedDialog.pack();
                openedDialog.setModal(true);
                openedDialog.setVisible(true);
            }
        });

        teacherMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JDialog openedDialog = new JDialog();
                openedDialog.add(new TeachersDialog(dbConnect));
                User user = new User(dbConnect);
                if (user.getUsersLength() != 0) {
                    openedDialog.setLocationRelativeTo(mainFrame);
                    openedDialog.pack();
                    openedDialog.setModal(true);
                    openedDialog.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(mainFrame, "No user records are created!");
                    openedDialog.dispose();
                }

            }
        });

        studentMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JDialog openedDialog = new JDialog();
                openedDialog.add(new StudentsDialog(dbConnect));
                User user = new User(dbConnect);
                if (user.getUsersLength() != 0) {
                    openedDialog.setLocationRelativeTo(mainFrame);
                    openedDialog.pack();
                    openedDialog.setModal(true);
                    openedDialog.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(mainFrame, "No user records are created!");
                    openedDialog.dispose();
                }
            }
        });

        lessonMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JDialog openedDialog = new JDialog();
                openedDialog.add(new LessonsDialog(dbConnect));
                openedDialog.setLocationRelativeTo(mainFrame);
                openedDialog.pack();
                openedDialog.setModal(true);
                openedDialog.setVisible(true);
            }
        });

        prerequisitesMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JDialog openedDialog = new JDialog();
                PrerequisiteDialog prereqDialog = new PrerequisiteDialog(dbConnect);
                openedDialog.add(prereqDialog);
                if (prereqDialog.getCurrentIndex() != 0) {
                    openedDialog.setLocationRelativeTo(mainFrame);
                    openedDialog.pack();
                    openedDialog.setModal(true);
                    openedDialog.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(null, "No lesson records are created!");
                    openedDialog.dispose();
                }
            }
        });

        teachingMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JDialog openedDialog = new JDialog();
                openedDialog.add(new TeachingDialog(dbConnect));
                Teacher teacher = new Teacher(dbConnect);
                if (teacher.getTeachersLength() != 0) {
                    openedDialog.setLocationRelativeTo(mainFrame);
                    openedDialog.pack();
                    openedDialog.setModal(true);
                    openedDialog.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(null, "No teacher records are created!");
                    openedDialog.dispose();
                }
            }
        });

        enrollMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JDialog openedDialog = new JDialog();
                openedDialog.add(new EnrollmentDialog(dbConnect));
                Student student = new Student(dbConnect);
                if (student.getStudentsLength() != 0) {
                    openedDialog.setLocationRelativeTo(mainFrame);
                    openedDialog.pack();
                    openedDialog.setModal(true);
                    openedDialog.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(mainFrame, "No student records are created!");
                    openedDialog.dispose();
                }
            }
        });        

        hMenu.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JOptionPane.showMessageDialog(mainFrame,
                        "Please contact an administrator to resolve your problem", "Help", JOptionPane.INFORMATION_MESSAGE);
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });

        // Close db connection on exit
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dbConnect.closeConnection();
                System.out.println("SQL Connection Closed");
            }
        });

        pack();
        setTitle("School Application");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    private JMenuBar menuBar;
    private JMenu pMenu, lMenu, aMenu, hMenu;
    private JMenuItem usersMenuItem, teacherMenuItem, studentMenuItem, exitMenuItem, lessonMenuItem,
            prerequisitesMenuItem, teachingMenuItem, enrollMenuItem;
    private ImageIcon backgroundImage;
    private JLabel bgLabel;
    private ConnectDatabase dbConnect;
}
