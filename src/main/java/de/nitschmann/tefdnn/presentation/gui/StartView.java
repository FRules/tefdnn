package de.nitschmann.tefdnn.presentation.gui;

import de.nitschmann.tefdnn.application.NeuralNetwork;
import de.nitschmann.tefdnn.persistence.Database;
import de.nitschmann.tefdnn.presentation.gui.console.MessageConsole;
import de.nitschmann.tefdnn.presentation.gui.initialization.ConfigurationComponentInitializer;
import de.nitschmann.tefdnn.presentation.gui.initialization.IInitializer;
import de.nitschmann.tefdnn.presentation.gui.initialization.InitializationComponentInitializer;
import de.nitschmann.tefdnn.presentation.gui.initialization.TrainingComponentInitializer;
import de.nitschmann.tefdnn.presentation.gui.wrapper.CustomGridBagConstraints;
import de.nitschmann.tefdnn.presentation.gui.wrapper.DynamicTrainingComponent;
import de.nitschmann.tefdnn.presentation.gui.wrapper.TrainingComponentType;
import de.nitschmann.tefdnn.presentation.json.JsonParser;
import de.nitschmann.tefdnn.presentation.json.mapper.JsonConfig;
import de.nitschmann.tefdnn.presentation.json.mapper.TrainingData;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class StartView extends JFrame {

    public NeuralNetwork neuralNetwork;
    public Database database;

    public JTextArea consoleArea = new JTextArea();
    public MessageConsole mc = new MessageConsole(consoleArea);
    public JScrollPane scrollPaneConsole;

    public JPanel panelMain;
    public GridBagConstraints c;
    public String[] trainingTypes = { "Backpropagation" };
    public String[] activationFunctions = { "ReLU", "Sigmoid", "TanH" };
    public Integer[] classes = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
    public final int insetsTop = 5;
    public final int insetsLeft = 10;
    public final int insetsRight = 10;
    public final int insetsBottom = 5;

    // We dynamically add classes depending on how many classes the user wants to have in the neural network.
    // Therefore, we have to add them here in a list of list of components which has a structure like this:
    // [0]
    //    [0] DynamicTrainingComponent1
    //    [1] DynamicTrainingComponent2
    // [1] ...
    // We have to wrap the training components in two lists in order to know which components belong to each other.
    // A list of DynamicTrainingComponents usually contains 2 components:
    // TextField for className
    // TextField for directoryPath
    public ArrayList<ArrayList<DynamicTrainingComponent>> dynamicTrainingComponents = new ArrayList<>();

    /* Initialization elements */
    public JLabel labelName = new JLabel("Name");
    public JLabel labelInputNeurons = new JLabel("Input neurons");
    public JLabel labelHiddenNeurons = new JLabel("Hidden neurons");
    public JLabel labelOutputNeurons = new JLabel("Output neurons");
    public JLabel labelHiddenLayers = new JLabel("Hidden layers");

    public JTextField textName = new JTextField();
    public JTextField textInputNeurons = new JTextField();
    public JTextField textHiddenNeurons = new JTextField();
    public JTextField textOutputNeurons = new JTextField();
    public JTextField textHiddenLayers = new JTextField();


    /* Configuration elements */
    public JLabel labelTargetLoss = new JLabel("Target loss");
    public JLabel labelMaxEpoch = new JLabel("Maximum epochs");
    public JLabel labelTrainingType = new JLabel("Training type");
    public JLabel labelActivationFunction = new JLabel("Activation function");
    public JLabel labelLearningRate = new JLabel("Learning rate");
    public JLabel labelMomentum = new JLabel("Momentum");

    public JTextField textTargetLoss = new JTextField();
    public JTextField textMaxEpoch = new JTextField();
    public JComboBox<String> comboTrainingType = new JComboBox(trainingTypes);
    public JComboBox<String> comboActivationFunction = new JComboBox(activationFunctions);
    public JTextField textLearningRate = new JTextField();
    public JTextField textMomentum = new JTextField();

    /* Training elements */
    public JLabel labelTrainingClasses = new JLabel("Amount of classes");
    public JComboBox<Integer> comboTrainingClasses = new JComboBox(classes);

    /* JSON */
    public JButton buttonJson = new JButton("Load configuration from JSON");
    public JFileChooser fileChooserJson = new JFileChooser();

    /* Control Buttons */
    public JButton buttonTrain = new JButton("Train");
    public JButton buttonTest = new JButton("Test");

    // This is currently global but not really used. It might be necessary to implement a
    // kill switch on the thread. Then, the stop button has to have access to this thread
    // in future. Right now, it also could be defined in the train action event of the button
    public TrainThread trainThread;

    public StartView(Database database) {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.database = database;

        this.panelMain = new JPanel();
        this.panelMain.setLayout(new GridBagLayout());

        IInitializer initComponentInitializer = new InitializationComponentInitializer();
        IInitializer confComponentInitializer = new ConfigurationComponentInitializer();
        IInitializer trainingComponentInitializer = new TrainingComponentInitializer();

        this.prepareJsonComponents();

        initComponentInitializer.initialize(this);
        confComponentInitializer.initialize(this);
        trainingComponentInitializer.initialize(this);

        this.buttonTrain.addActionListener((ActionEvent e) -> {
            this.trainThread = new TrainThread(this, (TrainingComponentInitializer) trainingComponentInitializer
            );
            Thread thread = new Thread(this.trainThread);
            thread.start();
            this.buttonTrain.setEnabled(false);
        });

        this.buttonTest.addActionListener((ActionEvent e) -> {
            TestingView testingView = new TestingView(this.neuralNetwork);
            testingView.toFront();
        });


        this.mc.redirectOut(null, System.out);
        this.mc.redirectErr(Color.RED, null);
        this.mc.setMessageLines(5000);
        /* Final code */
        this.setTitle("Training Environment for Deep Neural Networks");
        this.add(this.panelMain);
        this.pack();
        this.setVisible(true);
    }

    private void prepareJsonComponents() {
        c = CustomGridBagConstraints.getCustomGridBagConstraints(GridBagConstraints.HORIZONTAL, 0, 0,
                new Insets(insetsTop, insetsLeft, insetsBottom, insetsRight), 150, 7);
        this.c.fill = GridBagConstraints.HORIZONTAL;
        this.buttonJson.addActionListener((ActionEvent e) -> {
            int result = fileChooserJson.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File file = fileChooserJson.getSelectedFile();
                try {
                    JsonParser parser = new JsonParser();
                    JsonConfig config = parser.getJsonConfig(file.getAbsolutePath());
                    fillFieldsWithInformationFromJson(config);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        this.panelMain.add(this.buttonJson, c);

        FileNameExtensionFilter jsonFilter = new FileNameExtensionFilter("json files (*.json)", "json");
        this.fileChooserJson.setDialogTitle("Open json configuration");
        this.fileChooserJson.setFileFilter(jsonFilter);
    }

    private void fillFieldsWithInformationFromJson(JsonConfig config) {
        textName.setText(config.getInitialization().getName());
        textInputNeurons.setText(String.valueOf(config.getInitialization().getCountOfInputNeurons()));
        textHiddenNeurons.setText(String.valueOf(config.getInitialization().getCountOfHiddenNeurons()));
        textOutputNeurons.setText(String.valueOf(config.getInitialization().getCountOfOutputNeurons()));
        textHiddenLayers.setText(String.valueOf(config.getInitialization().getCountOfHiddenLayers()));

        textLearningRate.setText(String.valueOf(config.getConfiguration().getLearningRate()));
        textTargetLoss.setText(String.valueOf(config.getConfiguration().getTargetLoss()));
        textMaxEpoch.setText(String.valueOf(config.getConfiguration().getMaximumNumberOfEpochs()));
        textMomentum.setText(String.valueOf(config.getConfiguration().getMomentum()));
        comboTrainingType.setSelectedIndex(config.getConfiguration().getTrainingType() - 1);
        comboActivationFunction.setSelectedIndex(config.getConfiguration().getActivationFunction() - 1);

        comboTrainingClasses.setSelectedIndex(config.getTrainingData().size());

        for(TrainingData trainingData : config.getTrainingData()) {
            ArrayList<DynamicTrainingComponent> list = dynamicTrainingComponents.get(trainingData.getTargetNeuron());
            for(DynamicTrainingComponent component : list) {
                if (component.getComponentType() == TrainingComponentType.CLASSNAME) {
                    ((JTextField)component.getComponent()).setText(trainingData.getName());
                } else if (component.getComponentType() == TrainingComponentType.DIRECTORYPATH) {
                    ((JTextField)component.getComponent()).setText(trainingData.getPathToDirectory());
                }
            }
        }
        this.pack();
    }
}
