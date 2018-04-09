# Training Environment for Deep Neural Networks

This project is a simple training environment for deep neural networks. The supported neural network types are autoencoders and typical fully connected feedforward networks. The application contains an hsqldb database where training environments can be saved. A training environment can either contain a fully connected feedforward network or both an autoencoder and a fully connected feedforward network. 

**Right now, you should only use the fully connected feedforward network because results with attached autoencoder were poor when I tested it.**

## Usage

You can control the application via CLI. There are several commands available.

### Initialization
```
init [-n:] [-cIN:] [-cHN:] [-cON:] [-cHL:] -[cHNAE:]
init [-nS:]
init [-nFF:]
init [-nFF:] [-nAE:]
````

#### Clean Initialization
These switches should be used when you want to initialize a completely empty training environment. 

| Switch | Effect |
| ------ | -------|
| n      | Name of the training environment |
| cIN    | Number of input neurons, should be 784 since images are resized to 28x28 |
| cHN    | Number of neurons on hidden layers |
| cON    | Number of output neurons, should equal number of classes trained |
| cHL    | Number of hidden layers |
| cHNAE  | Number of hidden neurons on autoencoder network |

#### Loading a training environment
These switches should be used when you want to load a training environment from the database. Of course it is necessary you already created a training environment and saved it to the database (we discuss this later)

| Switch | Effect |
| ------ | -------|
| nS     | Name of the training environment you want to load |
| nFF    | Name of the fully connected feedforward network you want to load |
| nAE    | Name of the autoencoder network you want to load |

### Configuration
After a training environment is initialized, it is possible to configure it. You can adjust parameters like learning rate and momentum here.
```
conf [-nff] [-lr:] [-tt:] [-af:] [-me:] [-tl:] [-mom:]
```

| Switch | Effect |
| ------ | -------|
| nff    | Configures not the entire training environment but just the fully connected feed forward network |
| lr     | Learning rate, double |
| tt     | Training Type. Right now, only backpropagation is supported and its bound to integer 1 |
| af     | Activation function. Right now, three activation functions are implemented which are ReLU (1), Sigmoid (2) and Tanh (3). |
| me     | Maximum number of epochs, integer |
| tl     | Target loss, double |
| mom    | Momentum, double |

### Training
We can add paths to training data and train the environment now.

```
train [-pTD:] [-tN:]
train [-s]
```

| Switch | Effect |
| ------ | -------|
| pTD    | Path to directory which contains images of a specific class |
| tN     | Target neuron which should be trained for that class |
| s      | Starts the training |

### Testing
When the training is finished we can test how the neural network classifies specific images. It is either possible to do this via a small, minimalistic graphical user interface or via the cli. Via the cli, it's possible to test an entire path which contains test images whereas the gui supports only single images.

```
test [-pTD:]
test [-pS:]
test [-gui]
```

| Switch | Effect |
| ------ | -------|
| pTD    | Path to directory which contains test images |
| pS     | Path to single image which should be tested |
| gui    | Opens a graphical user interface where you can drag & drop a single image or enter a path to it  |

### Saving
Since training can sometimes take a while, it is smart to save a training environment. Afterwards, you can just load it and feed it with test images or finetune your hyperparameters.

```
save [-nFF:] [-nAE:] [-nS:]
save [-nFF:] [-nAE:]
save [-nFF:] 
save [-nS:]
```

| Switch | Effect |
| ------ | -------|
| nFF    | Name of the fully connected feedforward network |
| nAE    | Name of the autoencoder network |
| nS     | Name of the training environment |

## Example #1 

In the repository, there's a data folder which contains examples for classifying busses and cars. You can exactly use the following instruction for getting familiar with the commands. Notice that lines with hashtags as prefix are comments.

```
# Initialize a new neural network which has 784 input neurons, 40 hidden neurons, 2 output neurons and 1 hidden 
# layer. Name it "BusVsPkwNetwork"
init -n: "BusVsPkwNetwork" -cIN: 784 -cHN: 40 -cON: 2 -cHL: 1

# Add training path. Neuron 0 fires when busses are recognized, neuron 1 if cars are recognized. Start training.
train -pTD: "\path\to\repo\data\bus\Training" -tN: 0
train -pTD: "\path\to\repo\data\pkw\Training" -tN: 1
train -s

# After training is completed, we can test it. Lets test all busses in testing directory if they get classified 
# correctly.
test -pTD: "\path\to\repo\data\bus\Test"

# Now, we can also bring the gui to front to drag & drop images to it and test single images
test -gui

# If we're done and the results were good, we want to save it to the database. We only save the fully
# connected feedforward network, since autoencoder doesn't have good results yet
save -nFF: "BusVsPkwNetwork"
```

## Example 2
This example is based on example 1. This time, we want to load the network we saved in #1. Simple:

```
# Load the network with a simple command
init -nFF: "BusVsPkwNetwork"

# Now, we can do the exact same stuff as in #1, like testing images of busses 
test -pTD: "\path\to\repo\data\bus\Test\"
```
