package unittest;

import de.nitschmann.tefdnn.application.TrainingEnvironment;
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
    }

    @After
    public void closeDatabase() {
        db.close();
    }

    @Test
    public void testSaveNewTrainingEnvironment() {
        Console c = new Console(db);

        /* First, we initialize a clean training environment */
        String i = "init -cIN:2 -cHN:2 -cON:2 -cHL:4 -cHNAE: 2";
        TrainingEnvironment trainingEnvironment = c.init(i.toLowerCase());

        /* After that, we save it */
        String s = "save -nS: TestNewTE";
        Assert.assertTrue(c.save(s.toLowerCase(), trainingEnvironment));
    }

    @Test
    public void testSaveNewFeedForwardAndAutoEncoder() {
        Console c = new Console(db);

        /* First, we initialize a clean training environment */
        String i = "init -cIN:2 -cHN:2 -cON:2 -cHL:4 -cHNAE: 2";
        TrainingEnvironment trainingEnvironment = c.init(i.toLowerCase());

        /* After that, we save it */
        String s = "save -nFF: TestNewFF -nAE: TestNewAE";
        Assert.assertTrue( c.save(s.toLowerCase(), trainingEnvironment));
    }

    @Test
    public void testSaveNewFeedForward() {
        Console c = new Console(db);

        /* First, we initialize a clean training environment */
        String i = "init -cIN:2 -cHN:2 -cON:2 -cHL:4";
        TrainingEnvironment trainingEnvironment = c.init(i.toLowerCase());

        /* After that, we save it */
        String s = "save -nFF: TestNewFF";
        Assert.assertTrue(c.save(s.toLowerCase(), trainingEnvironment));
    }

    @Test
    public void testSaveNewFeedForwardAndLoadIt() {
        Console c = new Console(db);

        /* First, we initialize a clean training environment */
        String i = "init -cIN:2 -cHN:2 -cON:2 -cHL:4";
        TrainingEnvironment trainingEnvironment = c.init(i.toLowerCase());

        /* After that, we save it */
        String s = "save -nFF: TestNewFF";
        c.save(s.toLowerCase(), trainingEnvironment);

        /* Now, we load it */
        String is = "init -nFF: TestNewFF";
        TrainingEnvironment te = c.init(is.toLowerCase());

        /* If the feedforward and autoencoder both equals the loaded networks, it was successful */
        Assert.assertTrue(trainingEnvironment.getFeedForwardNetwork().equals(te.getFeedForwardNetwork()));
        Assert.assertNull(trainingEnvironment.getAutoEncoderNetwork());
    }


    @Test
    public void testSaveNewFeedForwardAndAutoEncoderAndLoadIt() {
        Console c = new Console(db);

        /* First, we initialize a clean training environment */
        String i = "init -cIN:2 -cHN:2 -cON:2 -cHL:4 -cHNAE: 2";
        TrainingEnvironment trainingEnvironment = c.init(i.toLowerCase());

        /* After that, we save it */
        String s = "save -nFF: TestNewFF -nAE: TestNewAE";
        c.save(s.toLowerCase(), trainingEnvironment);

        /* Now, we load it */
        String is = "init -nFF: TestNewFF -nAE: TestNewAE";
        TrainingEnvironment te = c.init(is.toLowerCase());

        /* If the feedforward and autoencoder both equals the loaded networks, it was successful */
        Assert.assertTrue(trainingEnvironment.getFeedForwardNetwork().equals(te.getFeedForwardNetwork()));
        Assert.assertTrue(trainingEnvironment.getAutoEncoderNetwork().equals(te.getAutoEncoderNetwork()));
    }


    @Test
    public void testSaveNewTrainingEnvironmentAndLoadIt() {
        Console c = new Console(db);

        /* First, we initialize a clean training environment */
        String i = "init -cIN:2 -cHN:2 -cON:2 -cHL:4 -cHNAE: 2";
        TrainingEnvironment trainingEnvironment = c.init(i.toLowerCase());

        /* After that, we save it */
        String s = "save -nS: TestNewTE";
        c.save(s.toLowerCase(), trainingEnvironment);

        /* Now, we load it */
        String is = "init -nS: TestNewTE";
        TrainingEnvironment te = c.init(is.toLowerCase());

        /* If the feedforward and autoencoder both equals the loaded networks, it was successful */
        Assert.assertTrue(trainingEnvironment.getFeedForwardNetwork().equals(te.getFeedForwardNetwork()));
        Assert.assertTrue(trainingEnvironment.getAutoEncoderNetwork().equals(te.getAutoEncoderNetwork()));
    }



    @Test
    public void testSaveNewConfiguratedFeedForwardAndAutoEncoder() {
        Console c = new Console(db);

        /* First, we initialize a clean training environment */
        String i = "init -cIN:2 -cHN:2 -cON:2 -cHL:4 -cHNAE: 2";
        TrainingEnvironment trainingEnvironment = c.init(i.toLowerCase());

        /* Then, we configure it */
        String cs = "conf -me:20 -tl: 0.05 -lr:0.001 -mom: 0.5 -af:1 -tt:1";
        c.conf(cs.toLowerCase(), trainingEnvironment);

        /* After that, we save it */
        String s = "save -nFF: TestNewFF -nAE: TestNewAE";
        c.save(s.toLowerCase(), trainingEnvironment);

        /* Now, we load it */
        String is = "init -nFF: TestNewFF -nAE: TestNewAE";
        TrainingEnvironment te = c.init(is.toLowerCase());

        /* If the feedforward and autoencoder both equals the loaded networks, it was successful */
        Assert.assertEquals(true, trainingEnvironment.getFeedForwardNetwork().equals(te.getFeedForwardNetwork()));
        Assert.assertEquals(true, trainingEnvironment.getAutoEncoderNetwork().equals(te.getAutoEncoderNetwork()));
    }

    @Test
    public void testInitFeedForwardAndNotProperlyConfigItAndTryToTrainIt() {
        Console c = new Console(db);

        String i = "init -cIN:784 -cHN:30 -cON:2 -cHL:1";
        TrainingEnvironment trainingEnvironment = c.init(i.toLowerCase());

        String conf = "conf -me: 20  -af: 1 -lr: 0.001 -mom: 0.5 -tt: 1 -nff";
        c.conf(conf, trainingEnvironment);

        String tr1 = "train -ptd: " + pathTraining1 + " -tn: 0";
        String tr2 = "train -ptd: " + pathTraining2 + " -tn: 1";
        String trs = "train -s";
        c.train(tr1, trainingEnvironment);
        c.train(tr2, trainingEnvironment);
        TrainingEnvironment trainedEnvironment = c.train(trs, trainingEnvironment);

        Assert.assertNull(trainedEnvironment);
    }

    @Test
    public void testLoadFeedForwardAndTrainAndTestIt() {
        Console c = new Console(db);

        String i = "init -cIN:784 -cHN:30 -cON:2 -cHL:1";
        TrainingEnvironment trainingEnvironment = c.init(i.toLowerCase());

        String conf = "conf -me: 20 -tl:0.1 -af: 1 -lr: 0.001 -mom: 0.5 -tt: 1 -nff";
        c.conf(conf, trainingEnvironment);

        String tr1 = "train -ptd: " + pathTraining1 + " -tn: 0";
        String tr2 = "train -ptd: " + pathTraining2 + " -tn: 1";
        String trs = "train -s";
        c.train(tr1, trainingEnvironment);
        c.train(tr2, trainingEnvironment);
        TrainingEnvironment trainedEnvironment = c.train(trs, trainingEnvironment);

        String test = "test -ptd: " + pathTest;
        Assert.assertTrue(c.test(test, trainedEnvironment));
    }

    @Test
    public void testInitTrainingEnvironmentAndTrainAndTestIt() {
        Console c = new Console(db);

        String i = "init -cIN:784 -cHN:30 -cON:2 -cHL:1 -cHNAE: 50";
        TrainingEnvironment trainingEnvironment = c.init(i.toLowerCase());

        String conf = "conf -me: 20 -tl:0.1 -af: 1 -lr: 0.001 -mom: 0.5 -tt: 1";
        c.conf(conf, trainingEnvironment);

        String tr1 = "train -ptd: " + pathTraining1 + " -tn: 0";
        String tr2 = "train -ptd: " + pathTraining2 + " -tn: 1";
        String trs = "train -s";
        c.train(tr1, trainingEnvironment);
        c.train(tr2, trainingEnvironment);
        TrainingEnvironment trainedEnvironment = c.train(trs, trainingEnvironment);

        String test = "test -ptd: " + pathTest;
        Assert.assertTrue(c.test(test, trainedEnvironment));
    }


    @Test
    public void testInitTrainingEnvironmentAndNotProperlyConfigItAndTryToTrainIt() {
        Console c = new Console(db);

        String i = "init -cIN:784 -cHN:30 -cON:2 -cHL:1 -cHNAE: 20";
        TrainingEnvironment trainingEnvironment = c.init(i.toLowerCase());

        String conf = "conf -me: 20  -af: 1 -lr: 0.001 -mom: 0.5 -tt: 1";
        c.conf(conf, trainingEnvironment);

        String tr1 = "train -ptd: " + pathTraining1 + " -tn: 0";
        String tr2 = "train -ptd: " + pathTraining2 + " -tn: 1";
        String trs = "train -s";
        c.train(tr1, trainingEnvironment);
        c.train(tr2, trainingEnvironment);
        TrainingEnvironment trainedEnvironment = c.train(trs, trainingEnvironment);

        Assert.assertNull(trainedEnvironment);
    }

}
