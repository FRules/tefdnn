package de.nitschmann.tefdnn.presentation.gui;

import de.nitschmann.tefdnn.application.TrainingEnvironment;
import de.nitschmann.tefdnn.application.io.ImageLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

public class TestingView extends JFrame implements ActionListener {

    private JPanel panel;
    private JButton testButton;
    private JTextField inputField;
    private TrainingEnvironment trainedEnvironment;
    private ImageLoader imageLoader;
    private ArrayList<JLabel> neuronNumberList = new ArrayList<>();
    private ArrayList<JLabel> neuronValueList = new ArrayList<>();

    public TestingView(TrainingEnvironment trainedEnvironment, ImageLoader imageLoader) {
        this.trainedEnvironment = trainedEnvironment;
        this.imageLoader = imageLoader;

        inputField = new JTextField();
        inputField.setText("Enter path to image or drag and drop...");
        testButton = new JButton("Test image...");
        testButton.addActionListener(this);

        panel = new JPanel(new GridLayout(0, 2));


        panel.add(inputField);
        panel.add(testButton);

        new DropTarget(inputField, new DropTargetListener() {
            @Override
            public void drop(DropTargetDropEvent dtde)
            {
                try
                {
                    Transferable tr = dtde.getTransferable();
                    DataFlavor[] flavors = tr.getTransferDataFlavors();

                    for (int i = 0; i < flavors.length; i++)
                    {
                        if (flavors[i].isFlavorJavaFileListType())
                        {
                            dtde.acceptDrop(dtde.getDropAction());
                            @SuppressWarnings("unchecked")
                            java.util.List<File> files = (java.util.List<File>) tr.getTransferData(flavors[i]);
                            if (files.size() > 1) {
                                JOptionPane.showMessageDialog(panel, "Es kann nur eine einzelne Datei getestet werden.");
                                dtde.dropComplete(true);
                                return;
                            }

                            for (int k = 0; k < files.size(); k++)
                            {
                                inputField.setText(files.get(k).toString());
                            }

                            dtde.dropComplete(true);
                        }
                    }
                    return;
                }
                catch (Throwable t)
                {
                    t.printStackTrace();
                }
                dtde.rejectDrop();

            }

            @Override
            public void dragEnter(DropTargetDragEvent dtde)
            {}

            @Override
            public void dragOver(DropTargetDragEvent dtde)
            {}

            @Override
            public void dropActionChanged(DropTargetDragEvent dtde)
            {}

            @Override
            public void dragExit(DropTargetEvent dte)
            {}
        });

        this.setTitle("Test image");
        this.add(panel);
        this.pack();
        this.setSize(1024, 80);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(testButton)) {
            removeOldNeuronLabels();

            String path = inputField.getText();

            if (!imageLoader.setTestImage(path)) {
                return;
            }
            double[] testData = imageLoader.getTestImage(imageLoader.getTrainingData().getMeanImage());

            trainedEnvironment.getFeedForwardNetwork().setInput(trainedEnvironment.getFeedForwardNetwork(), testData);
            ArrayList<Double> result = trainedEnvironment.getFeedForwardNetwork().test(trainedEnvironment.getFeedForwardNetwork());
            for(int i = 0; i < result.size(); i++) {
                JLabel neuronNumber = new JLabel("Neuron " + i);
                JLabel neuronValue = new JLabel(result.get(i).toString() + " %");
                panel.add(neuronNumber);
                panel.add(neuronValue);
                neuronNumberList.add(neuronNumber);
                neuronValueList.add(neuronValue);
            }
            this.pack();
            System.out.println(path);
            System.out.println(result);
        }
    }

    private void removeOldNeuronLabels() {
        for(JLabel label : neuronNumberList) {
            panel.remove(label);
        }
        for (JLabel label : neuronValueList) {
            panel.remove(label);
        }
    }
}
