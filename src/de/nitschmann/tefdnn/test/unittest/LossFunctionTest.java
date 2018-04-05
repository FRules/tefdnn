package de.nitschmann.tefdnn.test.unittest;

import de.nitschmann.tefdnn.application.NeuralNetwork;
import de.nitschmann.tefdnn.application.training.*;
import de.nitschmann.tefdnn.test.init.Initialization;
import de.nitschmann.tefdnn.test.init.Weights;
import org.junit.Assert;
import org.junit.Test;

public class LossFunctionTest {

    @Test
    public void testLossFunction() {
        NeuralNetwork nn = new NeuralNetwork();
        Weights weights = Initialization.initPreInitializedTwoInputTwoHiddenTwoOutputNeurons();

        nn.initNetwork(weights.getOutputsInInput(), weights.getWeightInHidden(), weights.getWeightInOutput());
        nn.setInput(nn, new double[] {0.7, 0.6});
        nn.getOutputLayer().getNeurons().get(0).setEstimatedValue(0);
        nn.getOutputLayer().getNeurons().get(1).setEstimatedValue(1);

        Backpropagation bp = new Backpropagation(new Relu());

        nn = bp.forward(nn);
        Assert.assertEquals(0.37279999, bp.calcLoss(nn), 0.01);
        nn.setInput(nn, new double[] {0.4, 0.3});
        nn.getOutputLayer().getNeurons().get(0).setEstimatedValue(1);
        nn.getOutputLayer().getNeurons().get(1).setEstimatedValue(0);
        nn = bp.forward(nn);
        Assert.assertEquals(0.17581254, bp.calcLoss(nn), 0.01);
    }

}
