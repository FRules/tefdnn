package de.nitschmann.tefdnn.presentation.gui;

import de.nitschmann.tefdnn.application.Neuron;
import de.nitschmann.tefdnn.application.TrainingEnvironment;
import de.nitschmann.tefdnn.application.io.ImageLoader;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class TestingView extends JFrame implements ActionListener {

    private JPanel panel;
    private JButton testButton;
    private JTextField inputField;
    private TrainingEnvironment trainedEnvironment;
    private ImageLoader imageLoader;
    private JLabel imagePreview;
    GridBagConstraints c = new GridBagConstraints();
    private ArrayList<JLabel> neuronNumberList = new ArrayList<>();
    private ArrayList<JLabel> neuronValueList = new ArrayList<>();

    public TestingView(TrainingEnvironment trainedEnvironment) {
        this.trainedEnvironment = trainedEnvironment;
        this.imageLoader = new ImageLoader(trainedEnvironment.getFeedForwardNetwork());

        JFrame self = this;

        inputField = new JTextField();
        inputField.setText("Enter path to image or drag and drop...");
        testButton = new JButton("Test image...");
        testButton.addActionListener(this);

        this.imagePreview = new JLabel();

        panel = new JPanel(new GridBagLayout());

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        panel.add(inputField, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 0;
        panel.add(testButton, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 2;
        c.gridx = 0;
        c.gridy = 1;
        panel.add(imagePreview, c);

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
                    BufferedImage img = null;
                    try {
                        img = ImageIO.read(new File(inputField.getText()));
                    } catch (IOException e) {
                        System.out.println(e.getMessage());
                    }

                    imagePreview.setIcon(new ImageIcon(getScaledImage(img, 600, 200)));
                    removeOldNeuronLabels();
                    self.pack();
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
        this.setVisible(true);
    }

    private Image getScaledImage(Image srcImg, int w, int h){
        BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resizedImg.createGraphics();

        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(srcImg, 0, 0, w, h, null);
        g2.dispose();

        return resizedImg;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(testButton)) {
            removeOldNeuronLabels();

            String path = inputField.getText();

            if (!imageLoader.setTestImage(path)) {
                return;
            }
            double[] testData = imageLoader.getTestImage(trainedEnvironment.getFeedForwardNetwork().getMeanImage());

            trainedEnvironment.getFeedForwardNetwork().setInput(trainedEnvironment.getFeedForwardNetwork(), testData);
            ArrayList<Double> result = trainedEnvironment.getFeedForwardNetwork().test(trainedEnvironment.getFeedForwardNetwork());
            ArrayList<Neuron> outputNeurons = trainedEnvironment.getFeedForwardNetwork().getOutputLayer().getNeurons();
            for(int i = 0; i < result.size(); i++) {
                JLabel neuronNumber = new JLabel(outputNeurons.get(i).getName());
                JLabel neuronValue = new JLabel(String.format("%.4f",result.get(i) * 100) + " %");
                c.fill = GridBagConstraints.HORIZONTAL;
                c.gridx = 0;
                c.gridy = 2 + i;
                panel.add(neuronNumber, c);

                c.fill = GridBagConstraints.HORIZONTAL;
                c.gridx = 1;
                c.gridy = 2 + i;
                panel.add(neuronValue, c);
                neuronNumberList.add(neuronNumber);
                neuronValueList.add(neuronValue);
            }
            this.pack();
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

    public @Override void toFront() {
        int sta = super.getExtendedState() & ~JFrame.ICONIFIED & JFrame.NORMAL;

        super.setExtendedState(sta);
        super.setAlwaysOnTop(true);
        super.toFront();
        super.requestFocus();
        super.setAlwaysOnTop(false);
    }
}
