package org.uclab.mm.kcl.edket.algoselector.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import jcolibri.exception.ExecutionException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.uclab.mm.kcl.edket.algoselector.AutomaticAlgorithmSelector;
import org.uclab.mm.kcl.edket.algoselector.QueryManager;
import org.uclab.mm.kcl.edket.algoselector.SimilarityManager;
import org.uclab.mm.kcl.edket.algoselector.mfe.InputCaseBuilder;
import org.uclab.mm.kcl.edket.algoselector.mfe.MetaFeature;
import org.uclab.mm.kcl.edket.algoselector.mfe.MetaFeatureExtractor;

public class AlgorithmSelectionUI {
    private static Logger LOG = LogManager.getLogger(AlgorithmSelectionUI.class);
    private JFrame frame;
    private static StatusBar statusBar;
    private static JTable table;
    private JTextField textField;
    private DatasetChooser datasetChooser;
    private JCheckBox chckbxDirectoryonly;
    
    private AutomaticAlgorithmSelector autoAlgoSelector;
    /**
     * Launch the application.
     */
    public static void initializeGUI(String[] args) throws Exception {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    (new InputCaseBuilder()).buildCasesIfNotExists();
                    AlgorithmSelectionUI window = new AlgorithmSelectionUI();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the application.
     */
    public AlgorithmSelectionUI() {
        initialize();
        ((DefaultTableModel) table.getModel()).setRowCount(0);
        String resolvedCasesFile = "AlgorithmsSelectionCaseBase.csv";
        try {
            Map<MetaFeature, Integer> simIntervals = detectSimIntervals(resolvedCasesFile);
            AutomaticAlgorithmSelector.setTopKResults(5);
            autoAlgoSelector = new AutomaticAlgorithmSelector(new QueryManager(), new SimilarityManager(simIntervals));
            autoAlgoSelector.configure();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
            System.exit(0);
        }
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frame = new JFrame("Automatic Algorithm Selection");
        frame.setBounds(100, 100, 1200, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        statusBar = new StatusBar();
        frame.getContentPane().add(statusBar, BorderLayout.SOUTH);
        
        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        frame.getContentPane().add(tabbedPane, BorderLayout.CENTER);
        
        JPanel inputPanel = new JPanel();
        tabbedPane.addTab("Algorithm Selection", null, inputPanel, null);
        inputPanel.setLayout(new BorderLayout(0, 0));
        
        JPanel inputBtnPanel = new JPanel();
        inputPanel.add(inputBtnPanel, BorderLayout.SOUTH);
        
        JButton btnSelectAlgorithm = new JButton("Select Algorithm");
        btnSelectAlgorithm.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                DefaultTableModel model = (DefaultTableModel) table.getModel();
                int rowCount = model.getRowCount();
                if(rowCount == 0){
                    statusBar.setText("Please extract features first and then try again");
                    return;
                }
                
                //. TODO do algorithm selection
                try {
                    statusBar.setMessage("Building Recommendation please wait...");
                    
                    Map<MetaFeature, Object> queryCase = new HashMap<MetaFeature, Object>();
                    int row = 0;
                    int columnCount = model.getColumnCount();
                    for(int column = 0; column < columnCount; column++){
                        MetaFeature columnName = MetaFeature.valueOf(model.getColumnName(column));
                        Object columnValue =  model.getValueAt(row, column);
                        queryCase.put(columnName, columnValue);
                    }
                    
                    autoAlgoSelector.buildRecommendation(queryCase);
                    statusBar.setMessage("Done.");
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });
        inputBtnPanel.add(btnSelectAlgorithm);
        
        JPanel chooserPanel = new JPanel();
        chooserPanel.setBorder(new TitledBorder(null, "Dataset Chooser", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        inputPanel.add(chooserPanel, BorderLayout.NORTH);
        chooserPanel.setPreferredSize(new Dimension(400, 100));
        chooserPanel.setLayout(null);
        chckbxDirectoryonly = new JCheckBox("Directory Only?");
        chckbxDirectoryonly.setBounds(130, 46, 138, 23);
        chckbxDirectoryonly.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                datasetChooser.setDirectoryOnly(chckbxDirectoryonly.isSelected());
              }
            });
        chooserPanel.add(chckbxDirectoryonly);
        
        datasetChooser = new DatasetChooser();
        datasetChooser.setBounds(359, 12, 67, 31);
        chooserPanel.add(datasetChooser);
        
        JButton btnExtractmeta = new JButton("Extract Meta Features");
        btnExtractmeta.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                //. single mode run
                DefaultTableModel tblModel = (DefaultTableModel)table.getModel();
                tblModel.setRowCount(0);
                String datasetFile = textField.getText();
                
                String singleOutputDirectory = "output/";
                if(datasetFile == null || datasetFile.isEmpty()){
                    statusBar.setText("Please Enter Dataset Path.");
                    return;
                }
                
                try{
                    statusBar.setText("Processing "+datasetFile+" please wait...");
                    Map<String, String> options = new HashMap<String, String>();
                    if(chckbxDirectoryonly.isSelected()){
                        options.put("dataset_dir", datasetFile);
                        options.put("dataset_file", "");
                        options.put("mode", "multi");
                    }else{
                        options.put("dataset_dir", "");
                        options.put("dataset_file", datasetFile);
                        options.put("mode", "single");
                    }
                    options.put("output_dir", singleOutputDirectory);
                    
                    MetaFeatureExtractor mfe = new MetaFeatureExtractor( options ); 
                    Thread mfeThread = new Thread(mfe);
                    mfeThread.setDaemon(true);
                    mfeThread.start();
                    
                    //. mfeThread.join();
                    //. populateTableModel(mfe.getMetaFeatures());
                    //. statusBar.setText("Done.");
                }catch(Exception ex){
                    LOG.error("error: {}", ex.getMessage());
                    statusBar.setText("Error: " + ex.getMessage());
                }
            }
        });
        btnExtractmeta.setBounds(274, 46, 152, 23);
        chooserPanel.add(btnExtractmeta);
        
        textField = new JTextField();
        textField.setBounds(130, 19, 219, 20);
        chooserPanel.add(textField);
        textField.setColumns(10);
        datasetChooser.setSelectionField(textField);
        
        JPanel inputTablePanel = new JPanel();
        inputTablePanel.setBorder(new TitledBorder(null, "Extracted Meta Features", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        inputPanel.add(inputTablePanel, BorderLayout.CENTER);
        inputTablePanel.setLayout(null);
        
        JScrollPane scrollPane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBounds(0, 22, 1179, 362);
        inputTablePanel.add(scrollPane);
        
        table = new JTable();
        table.setBorder(null);
        table.setModel(new DefaultTableModel(
            new Object[][] {
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
            },
            new String[] {
                    "MeanSkewnessOfNumericAtts", "MeanKurtosisOfNumericAtts", "MeanStdDevOfNumericAtts", "MeanMeansOfNumericAtts", "NumAttributes", "Dimensionality", "PercentageOfBinaryAtts", "PercentageOfNominalAtts", "NumNominalAtts", "PercentageOfNumericAtts", "NumNumericAtts", "NumBinaryAtts", "ClassCount", "NegativePercentage", "PositivePercentage", "DefaultAccuracy", "IncompleteInstanceCount", "InstanceCount", "NumMissingValues", "PercentageOfMissingValues", "MeanNominalAttDistinctValues", "StdvNominalAttDistinctValues", "MinNominalAttDistinctValues", "MaxNominalAttDistinctValues", "ClassEntropy", "MeanMutualInformation", "NoiseToSignalRatio", "MeanAttributeEntropy", "EquivalentNumberOfAtts"
            }
        ));
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); 
        scrollPane.setViewportView(table);
        
        JPanel similarityPanel = new JPanel();
        tabbedPane.addTab("Similarity tuning", null, similarityPanel, null);
    }
    
    public void clearTableModel(DefaultTableModel dtm){
        dtm.setRowCount(0);
    }
    
    public static void setStatusMessage(String msg, boolean singleMode){
        statusBar.setMessage(msg);
    }
    
    public void populateTableModel(List<Map<String, Double>> featureList){
        for(Map<String, Double> xFeatures : featureList){
            addTableRow(xFeatures);
        }
    }
    public static void addTableRow(Map<String, Double> xFeatures){
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        int columnCount = model.getColumnCount();
        Object[] row = new Object[columnCount];
        for(int i=0; i<columnCount; i++){
            row[i] = xFeatures.get(model.getColumnName(i));
        }
        model.addRow(row);
    }
    
    public static DefaultTableModel getTableModel(){
        return (DefaultTableModel) table.getModel();
    }
    
    public Map<MetaFeature, Integer> detectSimIntervals(String resolvedCasesFile) throws IOException{
        Map<MetaFeature, Integer> intervals = null;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(resolvedCasesFile));
            intervals = new HashMap<MetaFeature, Integer>();
            //. initialize intervals with minimum values
            for(MetaFeature mf : MetaFeature.values()){
                intervals.put(mf, Integer.MIN_VALUE);
            }
            
            String line = null;
            boolean heading = true;
            String[] columnNames = null;
            while((line = br.readLine()) != null){
                if(line.isEmpty()){continue;}
                line = line.substring(line.indexOf(",") + 1);
                line = line.replaceAll(",,+.*", "");
                line = line.substring(0, line.lastIndexOf(","));
                if(heading){
                    heading = false;
                    columnNames = line.split(",");
                    continue;
                }
                
                String[] columnValues = line.split(",");
                for(int i=0; i<columnNames.length; i++){
                    String column = columnNames[i].trim();
                    double v = Double.parseDouble(columnValues[i]);
                    int value = (int) v;
                    
                    MetaFeature mf = MetaFeature.valueOf(column);
                    if(intervals.get(mf) < value ){
                        intervals.put(mf, value);
                    }
                }
            }
            br.close();
        } catch (IOException e) {
            LOG.error("Error: {}", e.getMessage());
            throw e;
        }
        
        return intervals;
    }
}
