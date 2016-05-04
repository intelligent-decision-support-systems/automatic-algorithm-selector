package org.uclab.mm.kcl.edket.algoselector.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

public class AlgoSelectionResultsModel extends JDialog implements Runnable{

    private final JPanel contentPanel = new JPanel();
    private JPanel retrievePanel;
    private JPanel reusePanel;
    private JPanel revisePanel;
    
    JScrollPane retrieveScrollPane;
    JScrollPane reuseScrollPane;
    JScrollPane reviseScrollPane;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        try {
            AlgoSelectionResultsModel dialog = new AlgoSelectionResultsModel();
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Create JDialog
     */
    public AlgoSelectionResultsModel() {
        super();
    }
    
    /**
     * Initialize the dialog.
     */
    @Override
    public void run() {
        setTitle("Automatic Algorithm Selection Results");
        setModal(true);
        setAlwaysOnTop(true);
        setBounds(100, 100, 800, 600);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(null);
        
        retrieveScrollPane = new JScrollPane();
        retrieveScrollPane.setBounds(10, 30, 764, 180);
        contentPanel.add(retrieveScrollPane);
        
        retrievePanel = new JPanel();
        retrievePanel.setBackground(Color.WHITE);
        retrievePanel.setBorder(new TitledBorder(null, "Retrieved Similar Cases", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        retrievePanel.setLayout(new BoxLayout(retrievePanel, BoxLayout.Y_AXIS));
        retrieveScrollPane.setViewportView(retrievePanel);
        
        reuseScrollPane = new JScrollPane();
        reuseScrollPane.setBounds(10, 223, 764, 180);
        contentPanel.add(reuseScrollPane);
        
        reusePanel = new JPanel();
        reusePanel.setBackground(Color.WHITE);
        reusePanel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Reused Similar Cases", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
        reusePanel.setLayout(new BoxLayout(reusePanel, BoxLayout.Y_AXIS));
        reuseScrollPane.setViewportView(reusePanel);
        
        reviseScrollPane = new JScrollPane();
        reviseScrollPane.setBounds(10, 414, 764, 103);
        contentPanel.add(reviseScrollPane);
        
        revisePanel = new JPanel();
        revisePanel.setBackground(Color.WHITE);
        revisePanel.setBorder(new TitledBorder(null, "Revised Most Similar Case", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        revisePanel.setLayout(new BoxLayout(revisePanel, BoxLayout.Y_AXIS));
        reviseScrollPane.setViewportView(revisePanel);
        
        {
            JPanel buttonPane = new JPanel();
            buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
            getContentPane().add(buttonPane, BorderLayout.SOUTH);
            {
                JButton retainButton = new JButton("Retain Case");
                retainButton.setActionCommand("OK");
                buttonPane.add(retainButton);
                getRootPane().setDefaultButton(retainButton);
            }
            {
                JButton cancelButton = new JButton("Cancel");
                cancelButton.setActionCommand("Cancel");
                buttonPane.add(cancelButton);
            }
        }
        
        
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setVisible(true);
    }
    
    public JPanel getRetrievePanel(){
        return this.retrievePanel;
    }
    public JPanel getReusePanel(){
        return this.reusePanel;
    }
    public JPanel getRevisePanel(){
        return this.revisePanel;
    }
    
    public JScrollPane getRetriveScrollPane(){
        return this.retrieveScrollPane;
    }
    public JScrollPane getReuseScrollPane(){
        return this.reuseScrollPane;
    }
    public JScrollPane getReviseScrollPane(){
        return this.reviseScrollPane;
    }
}
