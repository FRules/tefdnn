package de.nitschmann.tefdnn.test.unittest;

import de.nitschmann.tefdnn.application.training.Relu;
import de.nitschmann.tefdnn.application.training.Sigmoid;
import org.junit.Assert;
import org.junit.Test;

public class TrainingTest {

    @Test
    public void testSigmoidFunctionWithPositiveInput() {
        Sigmoid training = new Sigmoid();
        Assert.assertEquals(0.622459331, training.activate(0.5), 0.0001);
    }

    @Test
    public void testSigmoidFunctionWithNegativeInput() {
        Sigmoid training = new Sigmoid();
        Assert.assertEquals(0.3775406688, training.activate(-0.5), 0.0001);
    }

    @Test
    public void testDerivativeOfSigmoidFunctionWithPositiveInput() {
        Sigmoid training = new Sigmoid();
        Assert.assertEquals(0.2350037122, training.derivative(0.5), 0.0001);
    }

    @Test
    public void testDerivativeOfSigmoidFunctionWithNegativeInput() {
        Sigmoid training = new Sigmoid();
        Assert.assertEquals(0.2350037122, training.derivative(-0.5), 0.0001);
    }

    @Test
    public void testReluFunctionWithPositiveInput() {
        Relu training = new Relu();
        Assert.assertEquals(0.622459331, training.activate(0.622459331), 0.0001);
    }

    @Test
    public void testReluFunctionWithNegativeInput() {
        Relu training = new Relu();
        Assert.assertEquals(0, training.activate(-0.5), 0.0001);
    }

    @Test
    public void testDerivativeOfReluFunctionWithPositiveInput() {
        Relu training = new Relu();
        Assert.assertEquals(1, training.derivative(0.622459331), 0.0001);
    }

    @Test
    public void testDerivativeOfReluFunctionWithNegativeInput() {
        Relu training = new Relu();
        Assert.assertEquals(0, training.derivative(-0.5), 0.0001);
    }


}
