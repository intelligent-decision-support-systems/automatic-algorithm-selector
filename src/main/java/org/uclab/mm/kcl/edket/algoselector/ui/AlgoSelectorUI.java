package org.uclab.mm.kcl.edket.algoselector.ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.UIManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.uclab.mm.kcl.edket.algoselector.AutomaticAlgorithmSelector;
import org.uclab.mm.kcl.edket.algoselector.QueryManager;
import org.uclab.mm.kcl.edket.algoselector.SimilarityManager;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;

import jcolibri.exception.ExecutionException;

/**
 * Automatic Algorithm Selector
 *
 */
public class AlgoSelectorUI {
    private static Logger LOG = LogManager.getLogger(AlgoSelectorUI.class);
    
    private static AlgoSelectorUI uiInstance = null;
    private AutomaticAlgorithmSelector autoAlgoSelector;
    
    private JFrame frmSmartalgorecommender;
    private StatusBar statusBar;
    
    private JTextField attrV;
    private JTextField nominalAttrV;
    private JTextField numericAttrV;
    private JTextField binaryAttrV;
    private JTextField classesAttrV;
    private JTextField instanceCountAttrV;
    private JTextField missingAttrV;
    
    private JComboBox<Class<?>> attrSim;
    private JComboBox<Class<?>> nominalSim;
    private JComboBox<Class<?>> numericSim;
    private JComboBox<Class<?>> binarySim;
    private JComboBox<Class<?>> classesSim;
    private JComboBox<Class<?>> instanceCountSim;
    private JComboBox<Class<?>> missingSim;

    /**
     * Launch the application.
     */
    public static void main(String[] args) throws Exception{
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    uiInstance = new AlgoSelectorUI();
                    uiInstance.frmSmartalgorecommender.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the application.
     */
    public AlgoSelectorUI() {
        initialize();
        autoAlgoSelector = new AutomaticAlgorithmSelector(new QueryManager(), new SimilarityManager());
        try {
            autoAlgoSelector.configure();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frmSmartalgorecommender = new JFrame();
        frmSmartalgorecommender.setTitle("SmartAlgoRecommender");
        frmSmartalgorecommender.setBounds(100, 100, 800, 600);
        frmSmartalgorecommender.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JMenuBar menuBar = new JMenuBar();
        frmSmartalgorecommender.setJMenuBar(menuBar);
        
        JMenu mnFile = new JMenu("File");
        menuBar.add(mnFile);
        
        JMenuItem mntmOpen = new JMenuItem("Open");
        mnFile.add(mntmOpen);
        
        JMenuItem mntmLoad = new JMenuItem("Load");
        mnFile.add(mntmLoad);
        
        JMenuItem mntmExit = new JMenuItem("Exit");
        mnFile.add(mntmExit);
        
        JMenu mnEdit = new JMenu("Edit");
        menuBar.add(mnEdit);
        
        JMenuItem mntmCopy = new JMenuItem("Copy");
        mnEdit.add(mntmCopy);
        
        JMenuItem mntmFind = new JMenuItem("Find");
        mnEdit.add(mntmFind);
        
        statusBar = new StatusBar();
        frmSmartalgorecommender.getContentPane().add(statusBar, BorderLayout.SOUTH);
        
        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        frmSmartalgorecommender.getContentPane().add(tabbedPane, BorderLayout.CENTER);
        
        JPanel inputPanel = new JPanel();
        tabbedPane.addTab("Input case", null, inputPanel, null);
        inputPanel.setLayout(new FormLayout(new ColumnSpec[] {
                FormSpecs.RELATED_GAP_COLSPEC,
                FormSpecs.DEFAULT_COLSPEC,
                FormSpecs.RELATED_GAP_COLSPEC,
                FormSpecs.DEFAULT_COLSPEC,
                FormSpecs.RELATED_GAP_COLSPEC,
                FormSpecs.DEFAULT_COLSPEC,
                FormSpecs.RELATED_GAP_COLSPEC,
                ColumnSpec.decode("default:grow"),},
            new RowSpec[] {
                FormSpecs.RELATED_GAP_ROWSPEC,
                FormSpecs.DEFAULT_ROWSPEC,
                FormSpecs.RELATED_GAP_ROWSPEC,
                FormSpecs.DEFAULT_ROWSPEC,
                FormSpecs.RELATED_GAP_ROWSPEC,
                FormSpecs.DEFAULT_ROWSPEC,
                FormSpecs.RELATED_GAP_ROWSPEC,
                FormSpecs.DEFAULT_ROWSPEC,
                FormSpecs.RELATED_GAP_ROWSPEC,
                FormSpecs.DEFAULT_ROWSPEC,
                FormSpecs.RELATED_GAP_ROWSPEC,
                FormSpecs.DEFAULT_ROWSPEC,
                FormSpecs.RELATED_GAP_ROWSPEC,
                FormSpecs.DEFAULT_ROWSPEC,
                FormSpecs.RELATED_GAP_ROWSPEC,
                FormSpecs.DEFAULT_ROWSPEC,
                FormSpecs.RELATED_GAP_ROWSPEC,
                FormSpecs.DEFAULT_ROWSPEC,
                FormSpecs.RELATED_GAP_ROWSPEC,
                FormSpecs.DEFAULT_ROWSPEC,
                FormSpecs.RELATED_GAP_ROWSPEC,
                FormSpecs.DEFAULT_ROWSPEC,
                FormSpecs.RELATED_GAP_ROWSPEC,
                FormSpecs.DEFAULT_ROWSPEC,
                FormSpecs.RELATED_GAP_ROWSPEC,
                FormSpecs.DEFAULT_ROWSPEC,
                FormSpecs.RELATED_GAP_ROWSPEC,
                FormSpecs.DEFAULT_ROWSPEC,
                FormSpecs.RELATED_GAP_ROWSPEC,
                FormSpecs.DEFAULT_ROWSPEC,
                FormSpecs.RELATED_GAP_ROWSPEC,
                FormSpecs.DEFAULT_ROWSPEC,
                FormSpecs.RELATED_GAP_ROWSPEC,
                FormSpecs.DEFAULT_ROWSPEC,
                FormSpecs.RELATED_GAP_ROWSPEC,
                FormSpecs.DEFAULT_ROWSPEC,}));
        
        JLabel lblAttributes_2 = new JLabel("Attributes");
        lblAttributes_2.setFont(new Font("Tahoma", Font.BOLD, 11));
        inputPanel.add(lblAttributes_2, "4, 4");
        
        JLabel lblValue = new JLabel("Value");
        lblValue.setFont(new Font("Tahoma", Font.BOLD, 11));
        inputPanel.add(lblValue, "8, 4");
        
        JLabel lblAttributes = new JLabel("Attributes");
        inputPanel.add(lblAttributes, "4, 8");
        
        attrV = new JTextField();
        inputPanel.add(attrV, "8, 8, left, default");
        attrV.setColumns(16);
        
        JLabel lblNominalAttributes = new JLabel("Nominal attributes");
        inputPanel.add(lblNominalAttributes, "4, 12");
        
        nominalAttrV = new JTextField();
        inputPanel.add(nominalAttrV, "8, 12, left, default");
        nominalAttrV.setColumns(16);
        
        JLabel lblNumericAttributes = new JLabel("Numeric attributes");
        inputPanel.add(lblNumericAttributes, "4, 16");
        
        numericAttrV = new JTextField();
        numericAttrV.setText("");
        inputPanel.add(numericAttrV, "8, 16, left, default");
        numericAttrV.setColumns(16);
        
        JLabel lblBinaryAttributes = new JLabel("Binary attributes");
        inputPanel.add(lblBinaryAttributes, "4, 20");
        
        binaryAttrV = new JTextField();
        inputPanel.add(binaryAttrV, "8, 20, left, default");
        binaryAttrV.setColumns(16);
        
        JLabel lblClasses = new JLabel("Classes");
        inputPanel.add(lblClasses, "4, 24");
        
        classesAttrV = new JTextField();
        inputPanel.add(classesAttrV, "8, 24, left, default");
        classesAttrV.setColumns(16);
        
        JLabel lblInstanceCount = new JLabel("Instance count");
        inputPanel.add(lblInstanceCount, "4, 28");
        
        instanceCountAttrV = new JTextField();
        inputPanel.add(instanceCountAttrV, "8, 28, left, default");
        instanceCountAttrV.setColumns(16);
        
        JLabel lblMissing = new JLabel("Missing");
        inputPanel.add(lblMissing, "4, 32");
        
        missingAttrV = new JTextField();
        inputPanel.add(missingAttrV, "8, 32, left, default");
        missingAttrV.setColumns(16);
        
        JButton recommendAlgoBtn = new JButton("Recommend Algorithm");
        recommendAlgoBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                try {
                    statusBar.setMessage("Building Recommendation please wait...");
                    autoAlgoSelector.buildRecommendation();
                    statusBar.setMessage("Done.");
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });
        inputPanel.add(recommendAlgoBtn, "8, 36, left, default");
        
        JPanel similarityPanel = new JPanel();
        tabbedPane.addTab("Similarity tuning", null, similarityPanel, null);
        similarityPanel.setLayout(new FormLayout(new ColumnSpec[] {
                FormSpecs.RELATED_GAP_COLSPEC,
                FormSpecs.DEFAULT_COLSPEC,
                FormSpecs.RELATED_GAP_COLSPEC,
                FormSpecs.DEFAULT_COLSPEC,
                FormSpecs.RELATED_GAP_COLSPEC,
                FormSpecs.DEFAULT_COLSPEC,
                FormSpecs.RELATED_GAP_COLSPEC,
                ColumnSpec.decode("default:grow"),},
            new RowSpec[] {
                FormSpecs.RELATED_GAP_ROWSPEC,
                FormSpecs.DEFAULT_ROWSPEC,
                FormSpecs.RELATED_GAP_ROWSPEC,
                FormSpecs.DEFAULT_ROWSPEC,
                FormSpecs.RELATED_GAP_ROWSPEC,
                FormSpecs.DEFAULT_ROWSPEC,
                FormSpecs.RELATED_GAP_ROWSPEC,
                FormSpecs.DEFAULT_ROWSPEC,
                FormSpecs.RELATED_GAP_ROWSPEC,
                FormSpecs.DEFAULT_ROWSPEC,
                FormSpecs.RELATED_GAP_ROWSPEC,
                FormSpecs.DEFAULT_ROWSPEC,
                FormSpecs.RELATED_GAP_ROWSPEC,
                FormSpecs.DEFAULT_ROWSPEC,
                FormSpecs.RELATED_GAP_ROWSPEC,
                FormSpecs.DEFAULT_ROWSPEC,
                FormSpecs.RELATED_GAP_ROWSPEC,
                FormSpecs.DEFAULT_ROWSPEC,
                FormSpecs.RELATED_GAP_ROWSPEC,
                FormSpecs.DEFAULT_ROWSPEC,
                FormSpecs.RELATED_GAP_ROWSPEC,
                FormSpecs.DEFAULT_ROWSPEC,
                FormSpecs.RELATED_GAP_ROWSPEC,
                FormSpecs.DEFAULT_ROWSPEC,
                FormSpecs.RELATED_GAP_ROWSPEC,
                FormSpecs.DEFAULT_ROWSPEC,
                FormSpecs.RELATED_GAP_ROWSPEC,
                FormSpecs.DEFAULT_ROWSPEC,
                FormSpecs.RELATED_GAP_ROWSPEC,
                FormSpecs.DEFAULT_ROWSPEC,
                FormSpecs.RELATED_GAP_ROWSPEC,
                FormSpecs.DEFAULT_ROWSPEC,
                FormSpecs.RELATED_GAP_ROWSPEC,
                FormSpecs.DEFAULT_ROWSPEC,
                FormSpecs.RELATED_GAP_ROWSPEC,
                FormSpecs.DEFAULT_ROWSPEC,}));
        
        JLabel similarityLabel1 = new JLabel("Attributes");
        similarityLabel1.setFont(new Font("Tahoma", Font.BOLD, 11));
        similarityPanel.add(similarityLabel1, "4, 4");
        
        JLabel lblSimilarityAlgorithm = new JLabel("Similarity Algorithm");
        lblSimilarityAlgorithm.setFont(new Font("Tahoma", Font.BOLD, 11));
        similarityPanel.add(lblSimilarityAlgorithm, "8, 4");
        
        JLabel lblAttributes_1 = new JLabel("Attributes");
        similarityPanel.add(lblAttributes_1, "4, 8");
        
        attrSim = new JComboBox<Class<?>>();
        attrSim.setModel(new DefaultComboBoxModel<Class<?>>(new Class<?>[] {jcolibri.method.retrieve.NNretrieval.similarity.local.Equal.class, jcolibri.method.retrieve.NNretrieval.similarity.local.Threshold.class, jcolibri.method.retrieve.NNretrieval.similarity.local.Interval.class}));
        similarityPanel.add(attrSim, "8, 8, left, default");
        
        JLabel lblNominalAttributes_1 = new JLabel("Nominal attributes");
        similarityPanel.add(lblNominalAttributes_1, "4, 12");
        
        nominalSim = new JComboBox<Class<?>>();
        nominalSim.setModel(new DefaultComboBoxModel<Class<?>>(new Class<?>[] {jcolibri.method.retrieve.NNretrieval.similarity.local.Equal.class, jcolibri.method.retrieve.NNretrieval.similarity.local.Threshold.class, jcolibri.method.retrieve.NNretrieval.similarity.local.Interval.class}));
        similarityPanel.add(nominalSim, "8, 12, left, default");
        
        JLabel lblNumericAttributes_1 = new JLabel("Numeric attributes");
        similarityPanel.add(lblNumericAttributes_1, "4, 16");
        
        numericSim = new JComboBox<Class<?>>();
        numericSim.setModel(new DefaultComboBoxModel<Class<?>>(new Class<?>[] {jcolibri.method.retrieve.NNretrieval.similarity.local.Equal.class, jcolibri.method.retrieve.NNretrieval.similarity.local.Threshold.class, jcolibri.method.retrieve.NNretrieval.similarity.local.Interval.class}));
        similarityPanel.add(numericSim, "8, 16, left, default");
        
        JLabel lblBinaryAttributes_1 = new JLabel("Binary attributes");
        similarityPanel.add(lblBinaryAttributes_1, "4, 20");
        
        binarySim = new JComboBox<Class<?>>();
        binarySim.setModel(new DefaultComboBoxModel<Class<?>>(new Class<?>[] {jcolibri.method.retrieve.NNretrieval.similarity.local.Equal.class, jcolibri.method.retrieve.NNretrieval.similarity.local.Threshold.class, jcolibri.method.retrieve.NNretrieval.similarity.local.Interval.class}));
        similarityPanel.add(binarySim, "8, 20, left, default");
        
        JLabel lblClasses_1 = new JLabel("Classes");
        similarityPanel.add(lblClasses_1, "4, 24");
        
        classesSim = new JComboBox<Class<?>>();
        classesSim.setModel(new DefaultComboBoxModel<Class<?>>(new Class<?>[] {jcolibri.method.retrieve.NNretrieval.similarity.local.Equal.class, jcolibri.method.retrieve.NNretrieval.similarity.local.Threshold.class, jcolibri.method.retrieve.NNretrieval.similarity.local.Interval.class}));
        similarityPanel.add(classesSim, "8, 24, left, default");
        
        JLabel lblInstanceCount_1 = new JLabel("Instance count");
        similarityPanel.add(lblInstanceCount_1, "4, 28");
        
        instanceCountSim = new JComboBox<Class<?>>();
        instanceCountSim.setModel(new DefaultComboBoxModel<Class<?>>(new Class<?>[] {jcolibri.method.retrieve.NNretrieval.similarity.local.Equal.class, jcolibri.method.retrieve.NNretrieval.similarity.local.Threshold.class, jcolibri.method.retrieve.NNretrieval.similarity.local.Interval.class}));
        similarityPanel.add(instanceCountSim, "8, 28, left, default");
        
        JLabel lblMissing_1 = new JLabel("Missing");
        similarityPanel.add(lblMissing_1, "4, 32");
        
        missingSim = new JComboBox<Class<?>>();
        missingSim.setModel(new DefaultComboBoxModel<Class<?>>(new Class<?>[] {jcolibri.method.retrieve.NNretrieval.similarity.local.Equal.class, jcolibri.method.retrieve.NNretrieval.similarity.local.Threshold.class, jcolibri.method.retrieve.NNretrieval.similarity.local.Interval.class}));
        similarityPanel.add(missingSim, "8, 32, left, default");
        
        JButton similarityConfigBtn = new JButton("Update similarity configuration");
        similarityConfigBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               statusBar.setMessage("SimilarityAlgorithms updated successfully!");
            }
        });
        similarityPanel.add(similarityConfigBtn, "8, 36, left, default");
    }
    
    
    
    public JTextField getAttrV() {
        return attrV;
    }

    public JTextField getNominalAttrV() {
        return nominalAttrV;
    }

    public JTextField getNumericAttrV() {
        return numericAttrV;
    }

    public JTextField getBinaryAttrV() {
        return binaryAttrV;
    }

    public JTextField getClassesAttrV() {
        return classesAttrV;
    }

    public JTextField getInstanceCountAttrV() {
        return instanceCountAttrV;
    }

    public JTextField getMissingAttrV() {
        return missingAttrV;
    }

    public JComboBox<Class<?>> getAttrSim() {
        return attrSim;
    }

    public JComboBox<Class<?>> getNominalSim() {
        return nominalSim;
    }

    public JComboBox<Class<?>> getNumericSim() {
        return numericSim;
    }

    public JComboBox<Class<?>> getBinarySim() {
        return binarySim;
    }

    public JComboBox<Class<?>> getClassesSim() {
        return classesSim;
    }

    public JComboBox<Class<?>> getInstanceCountSim() {
        return instanceCountSim;
    }

    public JComboBox<Class<?>> getMissingSim() {
        return missingSim;
    }
    
    public static AlgoSelectorUI getInstance(){
        if(uiInstance == null){
            LOG.error("Application not initialized properly!");
            System.exit(0);
        }
        return uiInstance;
    }
}
