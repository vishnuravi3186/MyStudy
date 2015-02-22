# Practical Machine Learning Course Project
Baoshi Sun  

## Introduction
The purpose of this project is to predict the manner or classe of people in which they do exercise. Accelerometer data of 6 participants were collected during they perform barbell in 5 different ways (either correctly or incorrectly). This research is based on these example data.    
More background information can be found at http://groupware.les.inf.puc-rio.br/har (see the section on the Weight Lifting Exercise Dataset).

## Preparation
### Setting up working environment
* Woking Directory
A directory named 'PML Course Project' is built as the main working directory. All files related to this project will be stored in here.
There is a sub-directory named 'submission' under the main directory. It is used to store the 20 prediction result files corresponding to the 20 different test cases.
Please refer to the [README.md]() for detailed information about directories and files under this project.

* Raw Data
The example data include both traning data and testing data, which were downloaded on Feb 5rd from the following URLs respectively:
https://d396qusza40orc.cloudfront.net/predmachlearn/pml-training.csv
https://d396qusza40orc.cloudfront.net/predmachlearn/pml-testing.csv
They are in CVS format. The main working directory also hold a copy of the two data files.

* Software Packages
To finish the project, a couple of software needs to be installed.
** R Version 3.1.2 64bit
** RStudio Version 0.98.1102
** notepad++ Version 6.7.4
The following packages in R are expected to be used:
** knitr - markdown tools
** caret
** randomForest
** ggplot2

## Planning
### Raw Data Exploration
In order to have an overall idea about the given observations, raw data has to be imported and reviewed.


```r
# read original datasets, both training and testing examples
rawData_training <- read.csv('pml-training.csv', header=TRUE, sep=',', na.strings=c("NA","#DIV/0!",""))
rawData_testing <- read.csv('pml-testing.csv', header=TRUE, sep=',', na.strings=c("NA","#DIV/0!",""))

# Raw Data Exploration
dim(rawData_training); dim(rawData_testing)
```

```
## [1] 19622   160
```

```
## [1]  20 160
```

```r
names(rawData_training)
```

```
##   [1] "X"                        "user_name"               
##   [3] "raw_timestamp_part_1"     "raw_timestamp_part_2"    
##   [5] "cvtd_timestamp"           "new_window"              
##   [7] "num_window"               "roll_belt"               
##   [9] "pitch_belt"               "yaw_belt"                
##  [11] "total_accel_belt"         "kurtosis_roll_belt"      
##  [13] "kurtosis_picth_belt"      "kurtosis_yaw_belt"       
##  [15] "skewness_roll_belt"       "skewness_roll_belt.1"    
##  [17] "skewness_yaw_belt"        "max_roll_belt"           
##  [19] "max_picth_belt"           "max_yaw_belt"            
##  [21] "min_roll_belt"            "min_pitch_belt"          
##  [23] "min_yaw_belt"             "amplitude_roll_belt"     
##  [25] "amplitude_pitch_belt"     "amplitude_yaw_belt"      
##  [27] "var_total_accel_belt"     "avg_roll_belt"           
##  [29] "stddev_roll_belt"         "var_roll_belt"           
##  [31] "avg_pitch_belt"           "stddev_pitch_belt"       
##  [33] "var_pitch_belt"           "avg_yaw_belt"            
##  [35] "stddev_yaw_belt"          "var_yaw_belt"            
##  [37] "gyros_belt_x"             "gyros_belt_y"            
##  [39] "gyros_belt_z"             "accel_belt_x"            
##  [41] "accel_belt_y"             "accel_belt_z"            
##  [43] "magnet_belt_x"            "magnet_belt_y"           
##  [45] "magnet_belt_z"            "roll_arm"                
##  [47] "pitch_arm"                "yaw_arm"                 
##  [49] "total_accel_arm"          "var_accel_arm"           
##  [51] "avg_roll_arm"             "stddev_roll_arm"         
##  [53] "var_roll_arm"             "avg_pitch_arm"           
##  [55] "stddev_pitch_arm"         "var_pitch_arm"           
##  [57] "avg_yaw_arm"              "stddev_yaw_arm"          
##  [59] "var_yaw_arm"              "gyros_arm_x"             
##  [61] "gyros_arm_y"              "gyros_arm_z"             
##  [63] "accel_arm_x"              "accel_arm_y"             
##  [65] "accel_arm_z"              "magnet_arm_x"            
##  [67] "magnet_arm_y"             "magnet_arm_z"            
##  [69] "kurtosis_roll_arm"        "kurtosis_picth_arm"      
##  [71] "kurtosis_yaw_arm"         "skewness_roll_arm"       
##  [73] "skewness_pitch_arm"       "skewness_yaw_arm"        
##  [75] "max_roll_arm"             "max_picth_arm"           
##  [77] "max_yaw_arm"              "min_roll_arm"            
##  [79] "min_pitch_arm"            "min_yaw_arm"             
##  [81] "amplitude_roll_arm"       "amplitude_pitch_arm"     
##  [83] "amplitude_yaw_arm"        "roll_dumbbell"           
##  [85] "pitch_dumbbell"           "yaw_dumbbell"            
##  [87] "kurtosis_roll_dumbbell"   "kurtosis_picth_dumbbell" 
##  [89] "kurtosis_yaw_dumbbell"    "skewness_roll_dumbbell"  
##  [91] "skewness_pitch_dumbbell"  "skewness_yaw_dumbbell"   
##  [93] "max_roll_dumbbell"        "max_picth_dumbbell"      
##  [95] "max_yaw_dumbbell"         "min_roll_dumbbell"       
##  [97] "min_pitch_dumbbell"       "min_yaw_dumbbell"        
##  [99] "amplitude_roll_dumbbell"  "amplitude_pitch_dumbbell"
## [101] "amplitude_yaw_dumbbell"   "total_accel_dumbbell"    
## [103] "var_accel_dumbbell"       "avg_roll_dumbbell"       
## [105] "stddev_roll_dumbbell"     "var_roll_dumbbell"       
## [107] "avg_pitch_dumbbell"       "stddev_pitch_dumbbell"   
## [109] "var_pitch_dumbbell"       "avg_yaw_dumbbell"        
## [111] "stddev_yaw_dumbbell"      "var_yaw_dumbbell"        
## [113] "gyros_dumbbell_x"         "gyros_dumbbell_y"        
## [115] "gyros_dumbbell_z"         "accel_dumbbell_x"        
## [117] "accel_dumbbell_y"         "accel_dumbbell_z"        
## [119] "magnet_dumbbell_x"        "magnet_dumbbell_y"       
## [121] "magnet_dumbbell_z"        "roll_forearm"            
## [123] "pitch_forearm"            "yaw_forearm"             
## [125] "kurtosis_roll_forearm"    "kurtosis_picth_forearm"  
## [127] "kurtosis_yaw_forearm"     "skewness_roll_forearm"   
## [129] "skewness_pitch_forearm"   "skewness_yaw_forearm"    
## [131] "max_roll_forearm"         "max_picth_forearm"       
## [133] "max_yaw_forearm"          "min_roll_forearm"        
## [135] "min_pitch_forearm"        "min_yaw_forearm"         
## [137] "amplitude_roll_forearm"   "amplitude_pitch_forearm" 
## [139] "amplitude_yaw_forearm"    "total_accel_forearm"     
## [141] "var_accel_forearm"        "avg_roll_forearm"        
## [143] "stddev_roll_forearm"      "var_roll_forearm"        
## [145] "avg_pitch_forearm"        "stddev_pitch_forearm"    
## [147] "var_pitch_forearm"        "avg_yaw_forearm"         
## [149] "stddev_yaw_forearm"       "var_yaw_forearm"         
## [151] "gyros_forearm_x"          "gyros_forearm_y"         
## [153] "gyros_forearm_z"          "accel_forearm_x"         
## [155] "accel_forearm_y"          "accel_forearm_z"         
## [157] "magnet_forearm_x"         "magnet_forearm_y"        
## [159] "magnet_forearm_z"         "classe"
```

```r
table(rawData_training$user_name)
```

```
## 
##   adelmo carlitos  charles   eurico   jeremy    pedro 
##     3892     3112     3536     3070     3402     2610
```

```r
table(rawData_training$classe)
```

```
## 
##    A    B    C    D    E 
## 5580 3797 3422 3216 3607
```

```r
head(rawData_training, 3)
```

```
##   X user_name raw_timestamp_part_1 raw_timestamp_part_2   cvtd_timestamp
## 1 1  carlitos           1323084231               788290 05/12/2011 11:23
## 2 2  carlitos           1323084231               808298 05/12/2011 11:23
## 3 3  carlitos           1323084231               820366 05/12/2011 11:23
##   new_window num_window roll_belt pitch_belt yaw_belt total_accel_belt
## 1         no         11      1.41       8.07    -94.4                3
## 2         no         11      1.41       8.07    -94.4                3
## 3         no         11      1.42       8.07    -94.4                3
##   kurtosis_roll_belt kurtosis_picth_belt kurtosis_yaw_belt
## 1                 NA                  NA                NA
## 2                 NA                  NA                NA
## 3                 NA                  NA                NA
##   skewness_roll_belt skewness_roll_belt.1 skewness_yaw_belt max_roll_belt
## 1                 NA                   NA                NA            NA
## 2                 NA                   NA                NA            NA
## 3                 NA                   NA                NA            NA
##   max_picth_belt max_yaw_belt min_roll_belt min_pitch_belt min_yaw_belt
## 1             NA           NA            NA             NA           NA
## 2             NA           NA            NA             NA           NA
## 3             NA           NA            NA             NA           NA
##   amplitude_roll_belt amplitude_pitch_belt amplitude_yaw_belt
## 1                  NA                   NA                 NA
## 2                  NA                   NA                 NA
## 3                  NA                   NA                 NA
##   var_total_accel_belt avg_roll_belt stddev_roll_belt var_roll_belt
## 1                   NA            NA               NA            NA
## 2                   NA            NA               NA            NA
## 3                   NA            NA               NA            NA
##   avg_pitch_belt stddev_pitch_belt var_pitch_belt avg_yaw_belt
## 1             NA                NA             NA           NA
## 2             NA                NA             NA           NA
## 3             NA                NA             NA           NA
##   stddev_yaw_belt var_yaw_belt gyros_belt_x gyros_belt_y gyros_belt_z
## 1              NA           NA         0.00            0        -0.02
## 2              NA           NA         0.02            0        -0.02
## 3              NA           NA         0.00            0        -0.02
##   accel_belt_x accel_belt_y accel_belt_z magnet_belt_x magnet_belt_y
## 1          -21            4           22            -3           599
## 2          -22            4           22            -7           608
## 3          -20            5           23            -2           600
##   magnet_belt_z roll_arm pitch_arm yaw_arm total_accel_arm var_accel_arm
## 1          -313     -128      22.5    -161              34            NA
## 2          -311     -128      22.5    -161              34            NA
## 3          -305     -128      22.5    -161              34            NA
##   avg_roll_arm stddev_roll_arm var_roll_arm avg_pitch_arm stddev_pitch_arm
## 1           NA              NA           NA            NA               NA
## 2           NA              NA           NA            NA               NA
## 3           NA              NA           NA            NA               NA
##   var_pitch_arm avg_yaw_arm stddev_yaw_arm var_yaw_arm gyros_arm_x
## 1            NA          NA             NA          NA        0.00
## 2            NA          NA             NA          NA        0.02
## 3            NA          NA             NA          NA        0.02
##   gyros_arm_y gyros_arm_z accel_arm_x accel_arm_y accel_arm_z magnet_arm_x
## 1        0.00       -0.02        -288         109        -123         -368
## 2       -0.02       -0.02        -290         110        -125         -369
## 3       -0.02       -0.02        -289         110        -126         -368
##   magnet_arm_y magnet_arm_z kurtosis_roll_arm kurtosis_picth_arm
## 1          337          516                NA                 NA
## 2          337          513                NA                 NA
## 3          344          513                NA                 NA
##   kurtosis_yaw_arm skewness_roll_arm skewness_pitch_arm skewness_yaw_arm
## 1               NA                NA                 NA               NA
## 2               NA                NA                 NA               NA
## 3               NA                NA                 NA               NA
##   max_roll_arm max_picth_arm max_yaw_arm min_roll_arm min_pitch_arm
## 1           NA            NA          NA           NA            NA
## 2           NA            NA          NA           NA            NA
## 3           NA            NA          NA           NA            NA
##   min_yaw_arm amplitude_roll_arm amplitude_pitch_arm amplitude_yaw_arm
## 1          NA                 NA                  NA                NA
## 2          NA                 NA                  NA                NA
## 3          NA                 NA                  NA                NA
##   roll_dumbbell pitch_dumbbell yaw_dumbbell kurtosis_roll_dumbbell
## 1      13.05217      -70.49400    -84.87394                     NA
## 2      13.13074      -70.63751    -84.71065                     NA
## 3      12.85075      -70.27812    -85.14078                     NA
##   kurtosis_picth_dumbbell kurtosis_yaw_dumbbell skewness_roll_dumbbell
## 1                      NA                    NA                     NA
## 2                      NA                    NA                     NA
## 3                      NA                    NA                     NA
##   skewness_pitch_dumbbell skewness_yaw_dumbbell max_roll_dumbbell
## 1                      NA                    NA                NA
## 2                      NA                    NA                NA
## 3                      NA                    NA                NA
##   max_picth_dumbbell max_yaw_dumbbell min_roll_dumbbell min_pitch_dumbbell
## 1                 NA               NA                NA                 NA
## 2                 NA               NA                NA                 NA
## 3                 NA               NA                NA                 NA
##   min_yaw_dumbbell amplitude_roll_dumbbell amplitude_pitch_dumbbell
## 1               NA                      NA                       NA
## 2               NA                      NA                       NA
## 3               NA                      NA                       NA
##   amplitude_yaw_dumbbell total_accel_dumbbell var_accel_dumbbell
## 1                     NA                   37                 NA
## 2                     NA                   37                 NA
## 3                     NA                   37                 NA
##   avg_roll_dumbbell stddev_roll_dumbbell var_roll_dumbbell
## 1                NA                   NA                NA
## 2                NA                   NA                NA
## 3                NA                   NA                NA
##   avg_pitch_dumbbell stddev_pitch_dumbbell var_pitch_dumbbell
## 1                 NA                    NA                 NA
## 2                 NA                    NA                 NA
## 3                 NA                    NA                 NA
##   avg_yaw_dumbbell stddev_yaw_dumbbell var_yaw_dumbbell gyros_dumbbell_x
## 1               NA                  NA               NA                0
## 2               NA                  NA               NA                0
## 3               NA                  NA               NA                0
##   gyros_dumbbell_y gyros_dumbbell_z accel_dumbbell_x accel_dumbbell_y
## 1            -0.02                0             -234               47
## 2            -0.02                0             -233               47
## 3            -0.02                0             -232               46
##   accel_dumbbell_z magnet_dumbbell_x magnet_dumbbell_y magnet_dumbbell_z
## 1             -271              -559               293               -65
## 2             -269              -555               296               -64
## 3             -270              -561               298               -63
##   roll_forearm pitch_forearm yaw_forearm kurtosis_roll_forearm
## 1         28.4         -63.9        -153                    NA
## 2         28.3         -63.9        -153                    NA
## 3         28.3         -63.9        -152                    NA
##   kurtosis_picth_forearm kurtosis_yaw_forearm skewness_roll_forearm
## 1                     NA                   NA                    NA
## 2                     NA                   NA                    NA
## 3                     NA                   NA                    NA
##   skewness_pitch_forearm skewness_yaw_forearm max_roll_forearm
## 1                     NA                   NA               NA
## 2                     NA                   NA               NA
## 3                     NA                   NA               NA
##   max_picth_forearm max_yaw_forearm min_roll_forearm min_pitch_forearm
## 1                NA              NA               NA                NA
## 2                NA              NA               NA                NA
## 3                NA              NA               NA                NA
##   min_yaw_forearm amplitude_roll_forearm amplitude_pitch_forearm
## 1              NA                     NA                      NA
## 2              NA                     NA                      NA
## 3              NA                     NA                      NA
##   amplitude_yaw_forearm total_accel_forearm var_accel_forearm
## 1                    NA                  36                NA
## 2                    NA                  36                NA
## 3                    NA                  36                NA
##   avg_roll_forearm stddev_roll_forearm var_roll_forearm avg_pitch_forearm
## 1               NA                  NA               NA                NA
## 2               NA                  NA               NA                NA
## 3               NA                  NA               NA                NA
##   stddev_pitch_forearm var_pitch_forearm avg_yaw_forearm
## 1                   NA                NA              NA
## 2                   NA                NA              NA
## 3                   NA                NA              NA
##   stddev_yaw_forearm var_yaw_forearm gyros_forearm_x gyros_forearm_y
## 1                 NA              NA            0.03            0.00
## 2                 NA              NA            0.02            0.00
## 3                 NA              NA            0.03           -0.02
##   gyros_forearm_z accel_forearm_x accel_forearm_y accel_forearm_z
## 1           -0.02             192             203            -215
## 2           -0.02             192             203            -216
## 3            0.00             196             204            -213
##   magnet_forearm_x magnet_forearm_y magnet_forearm_z classe
## 1              -17              654              476      A
## 2              -18              661              473      A
## 3              -18              658              469      A
```

We can see there are 19622 and 20 examples in the training and testing datasets respectively, while the original data set contains 159 features (excluding the first column 'X', which is believed to be the row index). We can also tell the response variable is called "classe" and variable "user_name" stands for the name of participants.
> [1] 19622   160  
> [1]  20 160
>
> [1] "X"                        "user_name"                "raw_timestamp_part_1"  
> [4] "raw_timestamp_part_2"     "cvtd_timestamp"           "new_window"  
> [7] "num_window"               "roll_belt"                "pitch_belt"  
> ......  
> [157] "magnet_forearm_x"         "magnet_forearm_y"         "magnet_forearm_z"  
> [160] "classe"  

6 participants and 5 classes (from A to E) are identified, which are corresponding to the project description.
>  adelmo carlitos  charles   eurico   jeremy   pedro  
>  3892     3112     3536     3070     3402     2610  
>
>  A    B    C    D    E  
>  5580 3797 3422 3216 3607  

When We go a little deeper into some data samples, we noticed these facts:
* There are four types of sensors. They are "roll_belt", "pitch_belt", "yaw_belt" and "total_accel_belt".
* A high percentage of variables have "NA" values.
* Among the 159 variables, some of they may have no relevancy to this research.
* Some variables have values whose variance closes to zero. At this moment, we are not sure if they should be trimmed off.
Therefore, during implementation phase data preprocessing should be applied.


```r
# number of rows with no NA values
sum(complete.cases(rawData_training))
```

```
## [1] 0
```

```r
# number of columns with at least one NA value
sum(sapply(names(rawData_training), function(x) any(is.na(rawData_training[,x]))))
```

```
## [1] 100
```
>  [1] 0  
>  [1] 100  

Furthermore, as we can see above, every example (row) contains at lease one NA value and there are 100 features (columns) with at lease one NA value. Therefore, it seems to be important to deal with those NA values properly. A couple of options will be discussed during data cleaning of implementation phase.

### Model Selection
The project is a classification question. According to what I learned from the course, RPart, Bagging, Random Forests, Boosting, LDA and Naive Bayes are all suitable. Considering a sub 20 thousand dataset, we don't have to worried about the speed. Random Forests is a outstanding choice. With rf, we can take the advantage for its accuracy. In addition, "In random forests, there is no need for cross-validation or a separate test set to get an unbiased estimate of the test set error."[[Random Forests by Leo Breiman and Adele Cutler](http://www.stat.berkeley.edu/~breiman/RandomForests/cc_home.htm#ooberr)]    

As a comparison, Naive Bayes algorithm is also applied. Due to the time constrains, I've decided not to try others algorithms. However, sometime after the course I'd like to make a fully comparison among all of above algorithms regarding this project.

## Implementation
### Data cleaning
For data cleaning, We have 3 tasks to do:
1. To delete irrelevant columns (the first 7) and convert non-number fields except "classe" column to number

```r
## 1. Delete irrelevant columns (the first 7)
training <- rawData_training[, -c(1:7)]
testing <- rawData_testing[, -c(1:7)]
## Convert everything except "classe" (last column) to numbers
features = dim(training)[2]
suppressWarnings(training[,-c(features)] <- sapply(training[,-c(features)], as.numeric))
suppressWarnings(testing[,-c(features)] <- sapply(testing[,-c(features)], as.numeric))
```

2. To deal with columns with NA values
3. To trim off features with near zero variance [optional]
Features with near zero variance may slow down the processing speed without improving the accuracy of the final model significantly. It should be a good practice to perform this step properly (by using function nearZeroVar()). However, given the limited amount of training data, we don't think the speed would be a major consideration. We would like to mark this task as optional one and skip it in this turn.  

It is obvious that the focus for data cleaning is to deal with NA values (task 2). We have 3 options in this case:
* Option 1: to delete any columns containing NAs, which is the simplest method, but may lose information
* Option 2: just remove columns with all or an excessive ratio of NAs. The threshold can be defined. For example, we choose 20% here.
* Option 3: to convert NAs with numbers (e.g. 0), which is an easy way too, but will introduce additional assumptions
Please refer to the following code lists:


```r
# Option 1: to delete any columns containing NAs, simplest method but may lose information
training <- training[, colSums(is.na(training)) == 0]
testing <- testing[, colSums(is.na(testing)) == 0]
dim(training);dim(testing)
```

```
## [1] 19622    53
```

```
## [1] 20 53
```
> [1] 19622    53  
> [1] 20 53  

As we can see the 100 features are removed from both datasets.


```r
### Option 2: just remove columns with all or an excessive ratio of NAs. The threshold can be defined.
#### We choose 80% here
Threshold_NARatio = 0.8
ExcessiveNAsCol <- (colSums(is.na(training)) > (nrow(training) * Threshold_NARatio))
training <- training[!ExcessiveNAsCol]
testing <- testing[!ExcessiveNAsCol]
dim(training);dim(testing)
```

```
## [1] 19622    53
```

```
## [1] 20 53
```
> [1] 19622    53  
> [1] 20 53  

When the threshold is 80%, we still expel the same 100 features as option 1 does. As computed, as long as the threshold is under 98%, the result stays the same. Only if we set the threshold upto 98%, we can encase more features.
> # Threshold_NARatio = 0.98
> [1] 19622   134  
> [1]  20 134  


```r
### Option 3: to convert NAs with numbers (e.g. 0), easy way but will introduce additional assumptions
NAtoNum = 0.0
training <- replace(training, is.na(training), NAtoNum)
testing <- replace(testing, is.na(testing), NAtoNum)
### check number of columns with at least one NA value
sum(sapply(names(training), function(x) any(is.na(training[,x]))))
```

```
## [1] 0
```
> [1] 0  

After this operation, We have no NA value anymore.    

Based on above analysis, I decided to run two rounds of modeling for feature selection. The first round goes with option 1 (52 features). And by using option 2 for the second round, the model will have 133 features with Threshold_NARatio of 98%. Then I can make a comparison and decide how many features I should keep.

### Feature Selection
For feature selection, I tried to use rpart with 5-fold cross validation. As we talked above, I've run the test twice and compared the results of variable importance. Although the accuracy of rpart is unacceptable (less than 51%), I can still have the result of variable importance and decide the feature selection.


```r
library(caret)
```

```
## Loading required package: lattice
## Loading required package: ggplot2
```

```r
library(randomForest)
```

```
## randomForest 4.6-10
## Type rfNews() to see new features/changes/bug fixes.
```

```r
fitCtrl <- trainControl(method = "cv", number = 5)   
modFit <- train(classe ~ ., method="rpart", data=training, 
  na.action = na.pass, trControl = fitCtrl)   
```

```
## Loading required package: rpart
```

```r
modFit
```

```
## CART 
## 
## 19622 samples
##    52 predictor
##     5 classes: 'A', 'B', 'C', 'D', 'E' 
## 
## No pre-processing
## Resampling: Cross-Validated (5 fold) 
## 
## Summary of sample sizes: 15697, 15697, 15699, 15697, 15698 
## 
## Resampling results across tuning parameters:
## 
##   cp          Accuracy   Kappa       Accuracy SD  Kappa SD  
##   0.03567868  0.5231921  0.38204500  0.02466926   0.04144430
##   0.05998671  0.4178904  0.21099738  0.06991381   0.11701420
##   0.11515454  0.3162258  0.04851373  0.04363960   0.06647905
## 
## Accuracy was used to select the optimal model using  the largest value.
## The final value used for the model was cp = 0.03567868.
```

```r
vi = varImp(modFit)
vi
```

```
## rpart variable importance
## 
##   only 20 most important variables shown (out of 52)
## 
##                      Overall
## pitch_forearm         100.00
## roll_forearm           72.97
## roll_belt              70.36
## magnet_dumbbell_y      49.51
## accel_belt_z           43.02
## magnet_belt_y          40.80
## yaw_belt               40.41
## magnet_dumbbell_z      36.37
## total_accel_belt       35.34
## magnet_arm_x           27.09
## accel_arm_x            26.33
## roll_dumbbell          18.67
## accel_dumbbell_y       15.60
## roll_arm               15.28
## magnet_arm_z            0.00
## total_accel_arm         0.00
## yaw_forearm             0.00
## accel_dumbbell_x        0.00
## magnet_forearm_z        0.00
## total_accel_dumbbell    0.00
```

#### Round 1 - 52 predictors (removed all columns with NA value)
#### Round 2 - 133 predictors (removed columns with ratio of NA value >= 98%)
>                        Overall
> pitch_forearm           100.00
> roll_forearm             72.97
> roll_belt                70.36
> magnet_dumbbell_y        49.51
> accel_belt_z             43.02
> magnet_belt_y            40.80
> yaw_belt                 40.41
> magnet_dumbbell_z        36.37
> total_accel_belt         35.34
> magnet_arm_x             27.09
> accel_arm_x              26.33
> roll_dumbbell            18.67
> accel_dumbbell_y         15.60
> roll_arm                 15.28
> skewness_roll_dumbbell    0.00
> stddev_roll_arm           0.00
> gyros_forearm_y           0.00
> skewness_roll_belt        0.00
> avg_roll_forearm          0.00
> accel_belt_x              0.00   

As can be seen we had almost the same variable importance for the two rounds. And I am going to take the 14 predictors whose importance is greater than 0 to perform to real model training.  


```r
# Weed out 0 importance features
impfeatures <- rownames(vi$importance)[vi$importance > 0]
```

### Creation of Working Datasets
Notes: to reproduce the same result set.seed(201502) should be called at every model training circle.    
Training dataset was splitted into sub-training (70%) and self-testing (30%) for cross validation

```r
set.seed(201502)
modFeatures = c(impfeatures, "classe")
training <- training[modFeatures]
inTrain <- createDataPartition(y=training$classe, p=0.7, list=FALSE)
selftraining <- training[inTrain,]
selftesting <- training[-inTrain,]
```

### Model Training
#### rf & nb
Notes: In order to speed up compiling, I commented out the NB modeling code in Rmd file. Please feel free to open and run the code on your computer.

```r
# rf
modrf = randomForest(classe ~ ., data=selftraining, na.action=na.omit)
# nb
#modnb = train(classe ~ ., data=selftraining, method="nb")
```

By using randomForest package, rf model was generated quickly, whereas nb took a long time to output the model.
Then the two models were applied to self-testing dataset to see the accuracy of each algorithms.


```r
# Model Self-test   
predrf = predict(modrf, selftesting)   
#prednb = predict(modnb, selftesting)   
```

Please refer to the confusion matrix for the random forests model.  
As can be seen, the accuracy is prefect good, and reaches 99.24%.
However, the accuracy of NB is not acceptable, only got 70.57%. 


```r
##### rf: Sample Accuracy
confusionMatrix(predrf, selftesting$classe)
```

```
## Confusion Matrix and Statistics
## 
##           Reference
## Prediction    A    B    C    D    E
##          A 1670    1    0    0    0
##          B    4 1130   10    0    1
##          C    0    7 1013    9    1
##          D    0    1    3  952    4
##          E    0    0    0    3 1076
## 
## Overall Statistics
##                                         
##                Accuracy : 0.9925        
##                  95% CI : (0.99, 0.9946)
##     No Information Rate : 0.2845        
##     P-Value [Acc > NIR] : < 2.2e-16     
##                                         
##                   Kappa : 0.9905        
##  Mcnemar's Test P-Value : NA            
## 
## Statistics by Class:
## 
##                      Class: A Class: B Class: C Class: D Class: E
## Sensitivity            0.9976   0.9921   0.9873   0.9876   0.9945
## Specificity            0.9998   0.9968   0.9965   0.9984   0.9994
## Pos Pred Value         0.9994   0.9869   0.9835   0.9917   0.9972
## Neg Pred Value         0.9991   0.9981   0.9973   0.9976   0.9988
## Prevalence             0.2845   0.1935   0.1743   0.1638   0.1839
## Detection Rate         0.2838   0.1920   0.1721   0.1618   0.1828
## Detection Prevalence   0.2839   0.1946   0.1750   0.1631   0.1833
## Balanced Accuracy      0.9987   0.9945   0.9919   0.9930   0.9969
```

```r
##### nb: Sample Accuracy
#confusionMatrix(prednb, selftesting$classe)
```

>           Reference
> Prediction    A    B    C    D    E
>          A 1670    1    0    0    0
>          B    4 1130   10    0    1
>          C    0    7 1013   10    1
>          D    0    1    3  951    4
>          E    0    0    0    3 1076
> 
> Overall Statistics
>                                           
>                Accuracy : 0.9924          
>                  95% CI : (0.9898, 0.9944)
>     No Information Rate : 0.2845          
>     P-Value [Acc > NIR] : < 2.2e-16       
>                                           
>                   Kappa : 0.9903          
>  Mcnemar's Test P-Value : NA              

### Prediction on Testing Dataset
Finally, let's use the achieved random forests model to predict the real testing dataset. 
Results can be seen below. And they are also stored into a text file.


```r
finalpred = predict(modrf, testing)   
finalpred   
```

```
##  1  2  3  4  5  6  7  8  9 10 11 12 13 14 15 16 17 18 19 20 
##  B  A  B  A  A  E  D  B  A  A  B  C  B  A  E  E  A  B  B  B 
## Levels: A B C D E
```

```r
write.table(finalpred, file="results.txt", quote=TRUE, sep=",", col.names=FALSE, row.names=FALSE)
```

Here comes the predicted results.
>  1  2  3  4  5  6  7  8  9 10 11 12 13 14 15 16 17 18 19 20   
>  B  A  B  A  A  E  D  B  A  A  B  C  B  A  E  E  A  B  B  B    

## Results
### Accuracy
With the randomForest package, we generated a high accurate model. The final model has 99.24% accuracy, 95% CI (0.9898, 0.9944) and above 0.99 Kappa.

### Out of Sample Error
With a confusion matrix, we can see the number of Out of sample error is quite low.  
Two operations we conducted may help control the out of sample error and avoid over fitting.
* Early weed out less relevancy features: from 153 to 14
* Cross validation by splitting training dataset into final training partition and self-testing partition.

### Variable Importance in the final model
As we can see, the variable importance in the final model is notably different from the list that we worked out with rpart method earlier.  
In the final model, 'roll_belt', 'yaw_belt' and 'agnet_dumbbell_z' are the top three important features; 
while in the rpart model 'pitch_forearm', 'roll_forearm' and 'roll_belt' occupy the first three positions.


```r
finalvi = varImp(modrf)
finalvi
```

```
##                     Overall
## accel_arm_x        453.4735
## accel_belt_z       592.8715
## accel_dumbbell_y   678.5102
## magnet_arm_x       443.0251
## magnet_belt_y      601.6317
## magnet_dumbbell_y  974.7378
## magnet_dumbbell_z 1072.8141
## pitch_forearm     1004.6471
## roll_arm           611.1098
## roll_belt         1418.3076
## roll_dumbbell      663.9278
## roll_forearm       796.0871
## total_accel_belt   315.0171
## yaw_belt          1233.0589
```
  
>                     Overall
> accel_arm_x        453.4735
> accel_belt_z       592.8715
> accel_dumbbell_y   678.5102
> magnet_arm_x       443.0251
> magnet_belt_y      601.6317
> magnet_dumbbell_y  974.7378
> agnet_dumbbell_z 1072.8141
> pitch_forearm     1004.6471
> roll_arm           611.1098
> roll_belt         1418.3076
> roll_dumbbell      663.9278
> roll_forearm       796.0871
> total_accel_belt   315.0171
> yaw_belt          1233.0589

## Conclusions
This is a great hand-on assignment. Although the final model is not complicated at all, the data exploration phase is quite a job. 
In the real world, we are encountering the shift from information scarcity to surfeit. 
To identify 'information' from 'noise' is one of the most important work for a data scientist.    
Another trick I learned from the project is the right package will definitely speed up my work. 
Caret is a versatile tool box, but it seems not as efficient as other specialty package. 
For example, randomForest package is extremely faster than 'rf' in caret.
