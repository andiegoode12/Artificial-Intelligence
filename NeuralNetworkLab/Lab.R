library(nnet)
library('caret')
library(e1071)
data <- read.csv(file='input-one-m.csv', head=TRUE, sep=",")
finaltest <- read.csv(file='test-one-m.csv', head=TRUE, sep=",")
train = sample(1:149,100)
test = setdiff(1:149, train)
data[train,]
data[test,]
#Now we are ready to start training our neural network
mod <- nnet(e~a+b+c+d, data[train,],size=10,skip=FALSE,linout=FALSE)
#To see the summary about the neural network that was just created
summary(mod)
#To evaluate our neural network we need to test it with data[test,]
data_test = subset(data[test,], select =- e)
pred <- predict(mod, data_test)
pred
#To see the predicted classes
pred <- predict(mod, data_test, type="class")
#To see the number of miss-classification (meaning the nnet was not on target)
pred <- predict(mod, data_test, type="class")
#confusionMatrix(pred,data[test,]$e)
table(pred,data[test,]$e)
#To test the neural network on the original data
pred2 <- predict(mod, finaltest,type="class" )
#confusionMatrix(pred2 , data$e)
table(pred2 , data$e)
grid <- expand.grid(size=c(6,7,8,9,10,11,12,13,14,15), decay=c(0,0.01,0.1,1))
nfit<-train(e~a+b+c+d, data=data[train,], method="nnet",tuneGrid=grid,skip=FALSE,linout=FALSE)
nfit
#We can use the result of the training from caret to perform the prediction.
pred3 <- predict(nfit, finaltest, type="raw")
table(pred3 , data$e)

