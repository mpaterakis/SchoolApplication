package UI;

import java.awt.BorderLayout;
import java.awt.event.*;
import javax.swing.*;

/**
 *
 * @author NMixalis, mpaterakis
 */
public abstract class ToolBarContainer extends JPanel {

    private JToolBar toolBar;
    protected JButton bFirst, bPrevious, bNext, bLast, bAdd, bCancel, bApply,
            bModify, bDelete, bSearch, bReplicate, bTable;

    public ToolBarContainer() {
        initComponents();
    }

    public JToolBar getToolBar() {
        return toolBar;
    }

    private void initComponents() {
        
        // Create the toolbar
        toolBar = new JToolBar();
        toolBar.setFloatable(false);
        
        // Add buttons and ActionListeners
        bFirst = new JButton(new ImageIcon("icons\\first16.png"));
        bFirst.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doFirst();
            }
        });

        bPrevious = new JButton(new ImageIcon("icons\\previous16.png"));
        bPrevious.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doPrevious();
            }
        });

        bNext = new JButton(new ImageIcon("icons\\next16.png"));
        bNext.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doNext();
            }
        });

        bLast = new JButton(new ImageIcon("icons\\last16.png"));
        bLast.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doLast();
            }
        });

        bAdd = new JButton(new ImageIcon("icons\\add16.png"));
        bAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doAdd();
            }
        });

        bCancel = new JButton(new ImageIcon("icons\\cancel16.png"));
        bCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doCancel();
            }
        });

        bApply = new JButton(new ImageIcon("icons\\ok16.png"));
        bApply.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doApply();
            }
        });

        bModify = new JButton(new ImageIcon("icons\\modify16.png"));
        bModify.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doModify();
            }
        });

        bDelete = new JButton(new ImageIcon("icons\\delete16.png"));
        bDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doDelete();
            }
        });

        bSearch = new JButton(new ImageIcon("icons\\search16.png"));
        bSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doSearch();
            }
        });

        bReplicate = new JButton(new ImageIcon("icons\\replicate16.png"));
        bReplicate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doReplicate();
            }
        });

        bTable = new JButton(new ImageIcon("icons\\table16.png"));
        bTable.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doTable();
            }
        });

        // Adding buttons
        toolBar.add(bFirst);
        toolBar.add(bPrevious);
        toolBar.add(bNext);
        toolBar.add(bLast);
        toolBar.add(bAdd);
        toolBar.add(bApply);
        toolBar.add(bCancel);
        toolBar.add(bModify);
        toolBar.add(bDelete);
        toolBar.add(bSearch);
        toolBar.add(bReplicate);
        toolBar.add(bTable);

        this.setLayout(new BorderLayout());
        this.add(toolBar, BorderLayout.NORTH);
    }

    public void doFirst() {
    }

    public abstract void doNext();

    public void doPrevious() {
    }

    public void doLast() {
    }

    public void doAdd() {
    }

    public void doCancel() {
    }

    public void doApply() {
    }

    public void doModify() {
    }

    public void doDelete() {
    }

    public void doSearch() {
    }

    public void doReplicate() {
    }

    public void doTable() {
    }
}
