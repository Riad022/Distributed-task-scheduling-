TIMEOUT 2
start cmd.exe /c "java Slave.GreenFilter 3030"
start cmd.exe /c "java Slave.GreenFilter 3040"
start cmd.exe /c "java Slave.GreenFilter 3050"
start cmd.exe /c "java Slave.GreenFilter 3060"

start cmd.exe /c "java Slave.ConvolutionFilter 4030"
start cmd.exe /c "java Slave.ConvolutionFilter 4040"
start cmd.exe /c "java Slave.ConvolutionFilter 4050"
start cmd.exe /c "java Slave.ConvolutionFilter 4060"


start cmd.exe /c "java Server.Main"
TIMEOUT 2
java Client.TaskSchedulerClient

