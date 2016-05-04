package org.uclab.mm.kcl.edket.algoselector.ui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class DatasetChooser extends JPanel implements ActionListener {

    JButton go;

    JFileChooser chooser;
    String choosertitle;
    private boolean directoryOnly = false;
    private JTextField selection;
    
    public DatasetChooser() {
        go = new JButton("Browse");
        go.setPreferredSize(new Dimension(67, 23));
        go.addActionListener(this);
        add(go); 
    }
    
    public void setDirectoryOnly(boolean directoryOnly){
        this.directoryOnly = directoryOnly;
    }
    public void setSelectionField(JTextField browseValue){
        this.selection = browseValue;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        chooser = new JFileChooser();
        chooser.setCurrentDirectory(new java.io.File("."));
        chooser.setDialogTitle(choosertitle);
        if(directoryOnly){
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        }else{
            chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        }
        chooser.setAcceptAllFileFilterUsed(!directoryOnly);
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            selection.setText(chooser.getSelectedFile()+"");
        } else {
            System.out.println("No Selection ");
        }

    }

    public Dimension getPreferredSize() {
        return new Dimension(200, 200);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("");
        JTextField f = new JTextField();
        f.setText("Please browse File or directory");
        f.setEditable(false);
        DatasetChooser panel = new DatasetChooser();
       
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        frame.getContentPane().add(f, "South");
        frame.getContentPane().add(panel, "Center");
        frame.setSize(panel.getPreferredSize());
        frame.setVisible(true);

    }

}
