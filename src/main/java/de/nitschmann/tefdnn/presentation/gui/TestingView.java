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
    private GridBagConstraints c = new GridBagConstraints();
    private ArrayList<JLabel> neuronNumberList = new ArrayList<>();
    private ArrayList<JLabel> neuronValueList = new ArrayList<>();

    private final int widthOfImage = 600;
    private final int heightOfImage = 200;

    public TestingView(TrainingEnvironment trainedEnvironment) {
        this.trainedEnvironment = trainedEnvironment;
        this.imageLoader = new ImageLoader(trainedEnvironment.getFeedForwardNetwork());

        this.inputField = new JTextField();
        this.inputField.setText("Enter path to image or drag and drop...");
        this.testButton = new JButton("Test image...");
        this.testButton.addActionListener(this);

        this.imagePreview = new JLabel();
        this.imagePreview.setText("Drag and drop image here");
        this.imagePreview.setMinimumSize(new Dimension(this.widthOfImage, this.heightOfImage));
        this.imagePreview.setPreferredSize(new Dimension(this.widthOfImage, this.heightOfImage));
        this.imagePreview.setMaximumSize(new Dimension(this.widthOfImage, this.heightOfImage));
        this.imagePreview.setHorizontalAlignment(SwingConstants.CENTER);
        this.imagePreview.setVerticalAlignment(SwingConstants.CENTER);

        this.panel = new JPanel(new GridBagLayout());

        this.c.fill = GridBagConstraints.HORIZONTAL;
        this.c.gridx = 0;
        this.c.gridy = 0;
        this.panel.add(inputField, c);

        this.c.fill = GridBagConstraints.HORIZONTAL;
        this.c.gridx = 1;
        this.c.gridy = 0;
        this.panel.add(testButton, c);

        this.c.fill = GridBagConstraints.HORIZONTAL;
        this.c.gridwidth = 2;
        this.c.gridx = 0;
        this.c.gridy = 1;
        this.panel.add(imagePreview, c);

        new DropTarget(inputField, new DropTargetListener() {
            @Override
            public void drop(DropTargetDropEvent dtde) {
                dropped(dtde);
            }

            @Override
            public void dragEnter(DropTargetDragEvent dtde) {
            }

            @Override
            public void dragOver(DropTargetDragEvent dtde) {
            }

            @Override
            public void dropActionChanged(DropTargetDragEvent dtde) {
            }

            @Override
            public void dragExit(DropTargetEvent dte) {
            }
        });

        new DropTarget(imagePreview, new DropTargetListener() {
            @Override
            public void drop(DropTargetDropEvent dtde) {
                dropped(dtde);
            }

            @Override
            public void dragEnter(DropTargetDragEvent dtde) {
            }

            @Override
            public void dragOver(DropTargetDragEvent dtde) {
            }

            @Override
            public void dropActionChanged(DropTargetDragEvent dtde) {
            }

            @Override
            public void dragExit(DropTargetEvent dte) {
            }
        });

        this.setTitle("Test image");
        this.add(panel);
        this.pack();
        this.setVisible(true);
    }

    private void dropped(DropTargetDropEvent dtde) {
        try {
            Transferable tr = dtde.getTransferable();
            DataFlavor[] flavors = tr.getTransferDataFlavors();

            for (int i = 0; i < flavors.length; i++) {
                if (flavors[i].isFlavorJavaFileListType()) {
                    dtde.acceptDrop(dtde.getDropAction());
                    @SuppressWarnings("unchecked")
                    java.util.List<File> files = (java.util.List<File>) tr.getTransferData(flavors[i]);
                    if (files.size() > 1) {
                        JOptionPane.showMessageDialog(panel, "Es kann nur eine einzelne Datei getestet werden.");
                        dtde.dropComplete(true);
                        return;
                    }

                    for (int k = 0; k < files.size(); k++) {
                        if (!files.get(k).toString().endsWith(".jpg") &&
                                !files.get(k).toString().endsWith(".jpeg") &&
                                !files.get(k).toString().endsWith(".bmp") &&
                                !files.get(k).toString().endsWith(".gif") &&
                                !files.get(k).toString().endsWith(".png")) {
                            JOptionPane.showMessageDialog(panel,
                                    "Fehler beim Lesen der Bilddatei. Vermutlich wurde ein falsches Dateiformat angegeben.\n" +
                                            "Unterstützte Formate: .jpg, .jpeg, .png, .bmp, .gif");
                            dtde.dropComplete(true);
                            return;
                        }
                        inputField.setText(files.get(k).toString());
                    }

                    dtde.dropComplete(true);
                }
            }
            BufferedImage img;
            try {
                img = ImageIO.read(new File(inputField.getText()));
            } catch (IOException e) {
                JOptionPane.showMessageDialog(panel, e.getStackTrace());
                return;
            } catch (Exception e) {
                JOptionPane.showMessageDialog(panel, e.getStackTrace());
                return;
            }

            if (img == null) {
                JOptionPane.showMessageDialog(panel, "Bilddatei ist vermutlich beschädigt und konnte nicht geladen werden");
                return;
            }

            imagePreview.setIcon(new ImageIcon(getScaledImage(img, this.widthOfImage, this.heightOfImage)));
            imagePreview.setText("");

            this.test();
            return;
        } catch (Throwable t) {
            t.printStackTrace();
        }
        dtde.rejectDrop();
    }

    private Image getScaledImage(Image srcImg, int w, int h) {
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
            test();
        }
    }

    private void test() {
        removeOldNeuronLabels();

        String path = inputField.getText();

        if (!imageLoader.setTestImage(path)) {
            return;
        }
        double[] testData = imageLoader.getTestImage(trainedEnvironment.getFeedForwardNetwork().getMeanImage());

        trainedEnvironment.getFeedForwardNetwork().setInput(trainedEnvironment.getFeedForwardNetwork(), testData);
        ArrayList<Double> result = trainedEnvironment.getFeedForwardNetwork().test(trainedEnvironment.getFeedForwardNetwork());
        ArrayList<Neuron> outputNeurons = trainedEnvironment.getFeedForwardNetwork().getOutputLayer().getNeurons();
        for (int i = 0; i < result.size(); i++) {
            JLabel neuronNumber = new JLabel(outputNeurons.get(i).getName());
            JLabel neuronValue = new JLabel(String.format("%.4f", result.get(i) * 100) + " %");
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

    private void removeOldNeuronLabels() {
        for (JLabel label : neuronNumberList) {
            panel.remove(label);
        }
        for (JLabel label : neuronValueList) {
            panel.remove(label);
        }
    }

    public @Override
    void toFront() {
        int sta = super.getExtendedState() & ~JFrame.ICONIFIED & JFrame.NORMAL;

        super.setExtendedState(sta);
        super.setAlwaysOnTop(true);
        super.toFront();
        super.requestFocus();
        super.setAlwaysOnTop(false);
    }
}
