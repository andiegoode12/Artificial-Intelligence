library(nnet)
library(caret)
library(e1071)

data <- read.csv(file='tit-train.csv', head=TRUE, sep=",")
finaltest <- read.csv(file='tit-test.csv', head=TRUE, sep=",")

mod <- nnet(Survived~Pclass+Sex+Age+SibSp+Parch+Fare+Embarked, data,size=10,skip=FALSE,linout=FALSE)
summary(mod)

data_test = subset(data, select =- Survived)
pred <- predict(mod, data_test)
pred

pred <- predict(mod, data_test, type="class")
table(pred,data$Survived)

grid <- expand.grid(size=c(6,7,8,9,10,11,12,13,14,15), decay=c(0,0.01,0.1,1))
nfit<-train(na.action=na.pass, Survived~Pclass+Sex+Age+SibSp+Parch+Fare+Embarked, data, method="nnet",tuneGrid=grid,skip=FALSE,linout=FALSE)
nfit

pred3 <- predict(nfit, finaltest, type="raw")
table(pred3)
