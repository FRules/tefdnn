package de.nitschmann.tefdnn.presentation.gui;

import de.nitschmann.tefdnn.persistence.Database;
import de.nitschmann.tefdnn.persistence.NeuralNetworkInformation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import static java.awt.Event.DELETE;

public class LoadView extends JFrame {

    private JTable table;

    public LoadView(Database database, INetworkLoadEvent networkLoadEvent, INetworkDeleteEvent networkDeleteEvent) {
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setTitle("Load training environment from database");

        List<NeuralNetworkInformation> list = database.getNeuralNetworks();
        if (list.size() == 0) {
            this.dispose();
            System.out.println("No neural networks stored in database");
            return;
        }

        Object[][] data = convertListOfNeuralNetworkInformationTo2DObject(list);
        String[] columnNames = {"Id", "Name", "Input neurons", "Hidden neurons", "Output neurons", "Hidden layers",
                "Learning rate", "Momentum", "Target loss", "Max epoch", "Activation function", "Training type"};
        this.table = new JTable(new LoadTableModel(columnNames, data));
        this.table.setPreferredScrollableViewportSize(new Dimension(1300, 300));
        this.table.setFont(new Font("Serif", Font.PLAIN, 14));
        this.setColumnWidths();
        JScrollPane scrollPaneTable = new JScrollPane(table);
        this.add(scrollPaneTable);

        this.table.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent mouseEvent) {
                JTable table = (JTable) mouseEvent.getSource();
                Point point = mouseEvent.getPoint();
                int row = table.rowAtPoint(point);
                if (mouseEvent.getClickCount() == 2 && table.getSelectedRow() != -1) {
                    networkLoadEvent.networkLoad((String)table.getValueAt(row, 1));
                }
            }
        });

        InputMap inputMap = this.table.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = this.table.getActionMap();

        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), DELETE);
        actionMap.put(DELETE, new AbstractAction() {
           public void actionPerformed(ActionEvent e) {
               JTable table = (JTable) e.getSource();
               String name = (String)table.getValueAt(table.getSelectedRow(), 1);
               networkDeleteEvent.networkDelete(name);
               ((LoadTableModel)table.getModel()).removeRow(table.getSelectedRow());
           }
        });

        this.pack();
        this.setVisible(true);
    }

    private Object[][] convertListOfNeuralNetworkInformationTo2DObject(List<NeuralNetworkInformation> list) {
        Object[][] data = new Object[list.size()][];
        for(int i = 0; i < list.size(); i++) {
            data[i] = convertNeuralNetworkInformationToObject(list.get(i));
        }
        return data;
    }

    private Object[] convertNeuralNetworkInformationToObject(NeuralNetworkInformation nni) {
        return new Object[]{
                nni.getNeuralNetworkId(),
                nni.getName(),
                nni.getNumberOfInputNeurons(),
                nni.getNumberOfHiddenNeurons(),
                nni.getNumberOfOutputNeurons(),
                nni.getNumberOfHiddenLayers(),
                nni.getLearningRate(),
                nni.getMomentum(),
                nni.getTargetLoss(),
                nni.getMaxEpoch(),
                nni.getActivationFunction(),
                nni.getTrainingType()
        };
    }

    private void setColumnWidths() {
        this.table.getColumnModel().getColumn(0).setPreferredWidth(50);
        this.table.getColumnModel().getColumn(1).setPreferredWidth(400);
        this.table.getColumnModel().getColumn(2).setPreferredWidth(150);
        this.table.getColumnModel().getColumn(3).setPreferredWidth(170);
        this.table.getColumnModel().getColumn(4).setPreferredWidth(170);
        this.table.getColumnModel().getColumn(5).setPreferredWidth(170);
        this.table.getColumnModel().getColumn(6).setPreferredWidth(160);
        this.table.getColumnModel().getColumn(7).setPreferredWidth(130);
        this.table.getColumnModel().getColumn(8).setPreferredWidth(150);
        this.table.getColumnModel().getColumn(9).setPreferredWidth(130);
        this.table.getColumnModel().getColumn(10).setPreferredWidth(250);
        this.table.getColumnModel().getColumn(11).setPreferredWidth(170);
        this.table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
    }
}
