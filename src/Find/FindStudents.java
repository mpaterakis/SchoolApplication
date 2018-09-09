package Find;

import Model.StudentsSearchModel;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.table.AbstractTableModel;

public class FindStudents extends JDialog {

    public FindStudents(Connection con, boolean modal, AbstractTableModel etb) {
        this.etb = etb;
        this.con = con;
        initComponents();
    }

    private void initComponents() {

        p = new JPanel(new BorderLayout());

        pf = new JPanel();
        lf = new JLabel("Find:");
        ff = new JTextField(20);
        bs = new JButton("search");

        bs.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doSearch();
            }
        });
        ba = new JButton("all");
        ba.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doShowAll();
            }
        });
        pf.add(lf);
        pf.add(ff);
        pf.add(bs);
        pf.add(ba);
        p.add(pf, BorderLayout.NORTH);

        pb = new JPanel();
        bok = new JButton("OK");
        bok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doOK();
            }
        });
        bc = new JButton("cancel");
        bc.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doCancel();
            }
        });
        pb.add(bok);
        pb.add(bc);
        p.add(pb, BorderLayout.SOUTH);

        t = new JTable(etb);
        sc = new JScrollPane(t);
        p.add(sc);

        add(p);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setModal(true);
        setAlwaysOnTop(true);

    }

    private void doSearch() {
        String search4 = ff.getText().trim();

        if (!search4.equals(null)) {
            etb = new StudentsSearchModel(con, search4, false);
            t.setModel(etb);
            t.repaint();
        }
    }

    private void doShowAll() {
        etb = new StudentsSearchModel(con, null, true);
        t.setModel(etb);
        t.repaint();
    }

    private void doOK() {
        rowId = t.getSelectedRow();
        setVisible(false);
    }

    private void doCancel() {
        rowId = -1;
        setVisible(false);
    }

    public int getRowId() {
        if (rowId == -1) {
            return -1;
        }
        if ((etb.getRowCount() > 0) && t.getSelectedRow() >= 0) {
            AbstractTableModel atm = (AbstractTableModel) t.getModel();
            rowId = Integer.valueOf(atm.getValueAt(t.getSelectedRow(), 0).toString());
            return rowId;
        } else {
            return -1;
        }
    }

    private JPanel p, pf, pb;
    private JLabel lf;
    private JTextField ff;
    private JButton bs, ba, bok, bc;
    private JScrollPane sc;
    private JTable t;

    private Connection con;

    private AbstractTableModel etb;
    private int rowId;
}
