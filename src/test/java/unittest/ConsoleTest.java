package unittest;

import de.nitschmann.tefdnn.application.NeuralNetwork;
import de.nitschmann.tefdnn.persistence.Database;
import de.nitschmann.tefdnn.presentation.Console;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Paths;

public class ConsoleTest {

    private Database db;
    private String pathTraining1;
    private String pathTraining2;
    private String pathTest;
    private String pathJson;

    @Before
    public void before() {
        initDatabase();
        initPaths();
    }

    private void initDatabase() {
        db = new Database("jdbc:hsqldb:file:db/database; shutdown=true", "SA", "" );
        db.dropTables();
        db.initDatabase();
    }

    private void initPaths() {
        pathTraining1 = Paths.get("data/mnist/Training/0").toAbsolutePath().toString();
        pathTraining2 = Paths.get("data/mnist/Training/1").toAbsolutePath().toString();
        pathTest = Paths.get("data/mnist/Test/0").toAbsolutePath().toString();
        pathJson = Paths.get("test/test.json").toAbsolutePath().toString();
    }

    @After
    public void closeDatabase() {
        db.close();
    }


    @Test
    public void testSaveNetwork() {
        Console c = new Console(db);

        /* First, we initialize a clean neural network */
        String i = "init -cIN:2 -cHN:2 -cON:2 -cHL:4";
        NeuralNetwork neuralNetwork = c.init(i.toLowerCase());

        /* After that, we save it */
        String s = "save -nFF: TestNewFF";
        Assert.assertTrue(c.save(s.toLowerCase(), neuralNetwork));
    }

    @Test
    public void testSaveNewNeuralNetworkAndLoadIt() {
        Console c = new Console(db);

        /* First, we initialize a clean neural network */
        String i = "init -cIN:2 -cHN:2 -cON:2 -cHL:4";
        NeuralNetwork neuralNetwork = c.init(i.toLowerCase());

        /* After that, we save it */
        String s = "save -nFF: TestNewFF";
        c.save(s.toLowerCase(), neuralNetwork);

        /* Now, we load it */
        String is = "init -nFF: TestNewFF";
        NeuralNetwork loaded = c.init(is.toLowerCase());

        /* If the neural network equals the loaded network, it was successful */
        Assert.assertTrue(neuralNetwork.equals(loaded));
    }

    @Test
    public void testSaveNewConfiguratedNeuralNetwork() {
        Console c = new Console(db);

        /* First, we initialize a clean neural network */
        String i = "init -cIN:2 -cHN:2 -cON:2 -cHL:4";
        NeuralNetwork neuralNetwork = c.init(i.toLowerCase());

        /* Then, we configure it */
        String cs = "conf -me:20 -tl: 0.05 -lr:0.001 -mom: 0.5 -af:1 -tt:1";
        c.conf(cs.toLowerCase(), neuralNetwork);

        /* After that, we save it */
        String s = "save -nFF: TestNewFF";
        c.save(s.toLowerCase(), neuralNetwork);

        /* Now, we load it */
        String is = "init -nFF: TestNewFF";
        NeuralNetwork loaded = c.init(is.toLowerCase());

        /* If the neural network equals the loaded network, it was successful */
        Assert.assertEquals(true, neuralNetwork.equals(loaded));
    }

    @Test
    public void testInitNeuralNetworkAndNotProperlyConfigItAndTryToTrainIt() {
        Console c = new Console(db);

        String i = "init -cIN:784 -cHN:30 -cON:2 -cHL:1";
        NeuralNetwork neuralNetwork = c.init(i.toLowerCase());

        String conf = "conf -me: 20  -af: 1 -lr: 0.001 -mom: 0.5 -tt: 1";
        c.conf(conf, neuralNetwork);

        String tr1 = "train -ptd: " + pathTraining1 + " -tn: 0 -n: zero";
        String tr2 = "train -ptd: " + pathTraining2 + " -tn: 1 -n: one";
        String trs = "train -s";
        c.train(tr1, neuralNetwork);
        c.train(tr2, neuralNetwork);
        NeuralNetwork trainedNeuralNetwork = c.train(trs, neuralNetwork);

        Assert.assertNull(trainedNeuralNetwork);
    }

    @Test
    public void testLoadNeuralNetworkAndTrainAndTestIt() {
        Console c = new Console(db);

        String i = "init -cIN:784 -cHN:30 -cON:2 -cHL:1";
        NeuralNetwork neuralNetwork = c.init(i.toLowerCase());

        String conf = "conf -me: 20 -tl:0.1 -af: 1 -lr: 0.001 -mom: 0.5 -tt: 1";
        c.conf(conf, neuralNetwork);

        String tr1 = "train -ptd: " + pathTraining1 + " -tn: 0 -n: zero";
        String tr2 = "train -ptd: " + pathTraining2 + " -tn: 1 -n: one";
        String trs = "train -s";
        c.train(tr1, neuralNetwork);
        c.train(tr2, neuralNetwork);
        NeuralNetwork trainedNeuralNetwork = c.train(trs, neuralNetwork);

        String test = "test -ptd: " + pathTest;
        Assert.assertTrue(c.test(test, trainedNeuralNetwork));
    }

    @Test
    public void testLoadNeuralNetworkWithJSON() {
        Console c = new Console(db);

        String iJson = "init -json: " + pathJson;
        String iConsole = "init -cIN:784 -cHN:40 -cON:3 -cHL:1";
        String cConsole = "conf -lr: 0.001 -tt: 1 -af: 2 -me: 40 -tl: 0.005 -mom: 0.95";

        NeuralNetwork neuralNetworkConsole = c.init(iConsole.toLowerCase());
        c.conf(cConsole, neuralNetworkConsole);
        neuralNetworkConsole.setName("Test");

        NeuralNetwork neuralNetworkJSON = c.init(iJson.toLowerCase());
        Assert.assertEquals(true, neuralNetworkConsole.equalsStructureAndConfiguration(neuralNetworkJSON));
    }
}
