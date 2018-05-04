getwd()

scenario01_data = as.numeric(read.csv(file="scenario_01.csv",header=FALSE,sep=","))
scenario02_data = as.numeric(read.csv(file="scenario_02.csv",header=FALSE,sep=","))
scenario03_data = as.numeric(read.csv(file="scenario_03.csv",header=FALSE,sep=","))
scenario04_data = as.numeric(read.csv(file="scenario_04.csv",header=FALSE,sep=","))
scenario05_data = as.numeric(read.csv(file="scenario_05.csv",header=FALSE,sep=","))
scenario06_data = as.numeric(read.csv(file="scenario_06.csv",header=FALSE,sep=","))
scenario07_data = as.numeric(read.csv(file="scenario_07.csv",header=FALSE,sep=","))
scenario08_data = as.numeric(read.csv(file="scenario_08.csv",header=FALSE,sep=","))

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

# --- most frequent values - scenario01_data
sort(table(scenario01_data),decreasing=TRUE)[1:3]

# --- most frequent values - scenario02_data
sort(table(scenario02_data),decreasing=TRUE)[1:3]

# --- most frequent values - scenario03_data
sort(table(scenario03_data),decreasing=TRUE)[1:3]

# --- most frequent values - scenario04_data
sort(table(scenario04_data),decreasing=TRUE)[1:3]

# --- most frequent values - scenario05_data
sort(table(scenario05_data),decreasing=TRUE)[1:3]

# --- most frequent values - scenario06_data
sort(table(scenario06_data),decreasing=TRUE)[1:3]

# --- most frequent values - scenario07_data
sort(table(scenario07_data),decreasing=TRUE)[1:3]

# --- most frequent values - scenario08_data
sort(table(scenario08_data),decreasing=TRUE)[1:3]

svg("boxplot.svg",height = 10,width = 10)
boxplot(scenario01_data,scenario02_data,scenario03_data,scenario04_data,scenario05_data,scenario06_data,scenario07_data,scenario08_data,names=c("Scenario 01","Scenario 02","Scenario 03","Scenario 04","Scenario 05","Scenario 06","Scenario 07","Scenario 08"))
dev.off()

# --- t.test // scenario01_data - scenario02_data
c(mean(scenario01_data),mean(scenario02_data))
t.test(scenario01_data,scenario02_data)
# ---

# --- t.test // scenario01_data - scenario03_data
c(mean(scenario01_data),mean(scenario03_data))
t.test(scenario01_data,scenario03_data)
# ---

# --- t.test // scenario01_data - scenario04_data
c(mean(scenario01_data),mean(scenario04_data))
t.test(scenario01_data,scenario04_data)
# ---

# --- t.test // scenario01_data - scenario05_data
c(mean(scenario01_data),mean(scenario05_data))
t.test(scenario01_data,scenario05_data)
# ---

# --- t.test // scenario01_data - scenario06_data
c(mean(scenario01_data),mean(scenario06_data))
t.test(scenario01_data,scenario06_data)
# ---

# --- t.test // scenario01_data - scenario07_data
c(mean(scenario01_data),mean(scenario07_data))
t.test(scenario01_data,scenario07_data)
# ---

# --- t.test // scenario01_data - scenario08_data
c(mean(scenario01_data),mean(scenario08_data))
t.test(scenario01_data,scenario08_data)
# ---

# --- t.test // scenario02_data - scenario03_data
c(mean(scenario02_data),mean(scenario03_data))
t.test(scenario02_data,scenario03_data)
# ---

# --- t.test // scenario02_data - scenario04_data
c(mean(scenario02_data),mean(scenario04_data))
t.test(scenario02_data,scenario04_data)
# ---

# --- t.test // scenario02_data - scenario05_data
c(mean(scenario02_data),mean(scenario05_data))
t.test(scenario02_data,scenario05_data)
# ---

# --- t.test // scenario02_data - scenario06_data
c(mean(scenario02_data),mean(scenario06_data))
t.test(scenario02_data,scenario06_data)
# ---

# --- t.test // scenario02_data - scenario07_data
c(mean(scenario02_data),mean(scenario07_data))
t.test(scenario02_data,scenario07_data)
# ---

# --- t.test // scenario02_data - scenario08_data
c(mean(scenario02_data),mean(scenario08_data))
t.test(scenario02_data,scenario08_data)
# ---

# --- t.test // scenario03_data - scenario04_data
c(mean(scenario03_data),mean(scenario04_data))
t.test(scenario03_data,scenario04_data)
# ---

# --- t.test // scenario03_data - scenario05_data
c(mean(scenario03_data),mean(scenario05_data))
t.test(scenario03_data,scenario05_data)
# ---

# --- t.test // scenario03_data - scenario06_data
c(mean(scenario03_data),mean(scenario06_data))
t.test(scenario03_data,scenario06_data)
# ---

# --- t.test // scenario03_data - scenario07_data
c(mean(scenario03_data),mean(scenario07_data))
t.test(scenario03_data,scenario07_data)
# ---

# --- t.test // scenario03_data - scenario08_data
c(mean(scenario03_data),mean(scenario08_data))
t.test(scenario03_data,scenario08_data)
# ---

# --- t.test // scenario04_data - scenario05_data
c(mean(scenario04_data),mean(scenario05_data))
t.test(scenario04_data,scenario05_data)
# ---

# --- t.test // scenario04_data - scenario06_data
c(mean(scenario04_data),mean(scenario06_data))
t.test(scenario04_data,scenario06_data)
# ---

# --- t.test // scenario04_data - scenario07_data
c(mean(scenario04_data),mean(scenario07_data))
t.test(scenario04_data,scenario07_data)
# ---

# --- t.test // scenario04_data - scenario08_data
c(mean(scenario04_data),mean(scenario08_data))
t.test(scenario04_data,scenario08_data)
# ---

# --- t.test // scenario05_data - scenario06_data
c(mean(scenario05_data),mean(scenario06_data))
t.test(scenario05_data,scenario06_data)
# ---

# --- t.test // scenario05_data - scenario07_data
c(mean(scenario05_data),mean(scenario07_data))
t.test(scenario05_data,scenario07_data)
# ---

# --- t.test // scenario05_data - scenario08_data
c(mean(scenario05_data),mean(scenario08_data))
t.test(scenario05_data,scenario08_data)
# ---

# --- t.test // scenario06_data - scenario07_data
c(mean(scenario06_data),mean(scenario07_data))
t.test(scenario06_data,scenario07_data)
# ---

# --- t.test // scenario06_data - scenario08_data
c(mean(scenario06_data),mean(scenario08_data))
t.test(scenario06_data,scenario08_data)
# ---

# --- t.test // scenario07_data - scenario08_data
c(mean(scenario07_data),mean(scenario08_data))
t.test(scenario07_data,scenario08_data)
# ---