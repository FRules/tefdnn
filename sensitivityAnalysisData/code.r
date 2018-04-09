getwd()

scenario01_data = as.numeric(read.csv(file="scenario01_data.csv",header=FALSE,sep=","))
scenario02_data = as.numeric(read.csv(file="scenario02_data.csv",header=FALSE,sep=","))
scenario03_data = as.numeric(read.csv(file="scenario03_data.csv",header=FALSE,sep=","))
scenario04_data = as.numeric(read.csv(file="scenario04_data.csv",header=FALSE,sep=","))
scenario05_data = as.numeric(read.csv(file="scenario05_data.csv",header=FALSE,sep=","))
scenario06_data = as.numeric(read.csv(file="scenario06_data.csv",header=FALSE,sep=","))
scenario07_data = as.numeric(read.csv(file="scenario07_data.csv",header=FALSE,sep=","))
scenario08_data = as.numeric(read.csv(file="scenario08_data.csv",header=FALSE,sep=","))

# --- length
c(length(scenario01_data),length(scenario02_data),length(scenario03_data),length(scenario04_data),length(scenario05_data),length(scenario06_data),length(scenario07_data),length(scenario08_data))
# ---

# --- mean
c(mean(scenario01_data),mean(scenario02_data),mean(scenario03_data),mean(scenario04_data),mean(scenario05_data),mean(scenario06_data),mean(scenario07_data),mean(scenario08_data))
# ---

# --- median
c(median(scenario01_data),median(scenario02_data),median(scenario03_data),median(scenario04_data),median(scenario05_data),median(scenario06_data),median(scenario07_data),median(scenario08_data))
# ---

# --- sd
c(sd(scenario01_data),sd(scenario02_data),sd(scenario03_data),sd(scenario04_data),sd(scenario05_data),sd(scenario06_data),sd(scenario07_data),sd(scenario08_data))
# ---
