# AI for Triathlete
We use a bunch of professional articles (see References section  below) to create an adaptable training plan for Triathletes. Our ML engine predicts in realtime if an athlete is overtraining and will adapt weekly sessions accordingly. Reducing intensity, volume or perhaps even skipping a week to make sure the athlete does no hurt him/her self.
On the business side of things, just create you profile with a personal id and ftp values. We will not ask you any registration only credentials to external training sources such as Strava in order to analyse your data for ou ML engine.

Interesting idea:
- use predictive model to create training plan instead of hard coded values for increasing weekly sessions...

## Terms Definitions
#### TSS (Training Stress Score)
TSS = [(s x NP/NGP x IF) / (FTP x 3,600)] x 100 [1]
Where “s” is duration of the workout in seconds
and “3600” is the number of seconds in an hour.
#### NP (Normalized Power - Bike)
For each gps coordinate (ca), bot will calculate the the grade between ca and the current gps coordinate (cc). f(ca,cc) = % grade (g). 
g will then be added to the current power (cp). NP = g + cp. This actually changes all power data to NP.
Bot will use the same formula for running. We will see how to use NGP in the future.
#### NGP (Normalized Graded Pace - Run)
To be completed.
#### Functional Threshold Pace (FTP)
FTP stands for Functional Threshold Power/Pace, which is commonly defined as the highest average power/pace 
you can sustain for an hour, measured in watts. FTP is often used to determine training zones when 
using a power/pace meter and to measure improvement [2],[4].
#### IF (Intensity Factor)
IF is a percentage of the FTP which represents the zones. Values are from 1 to 7.
#### TSB (Training Stress Balance)
Training Stress Balance (TSB) or Form represents the balance of training stress.

Form (TSB) = Yesterday's Fitness (CTL) - Yesterday's Fatigue (ATL)

A positive TSB number means that you would have a good chance of performing well during those 'positive' days, and would suggest that you are both fit and fresh.

#### CTL (chronic training load)
Fitness (CTL) is an exponentially weighted average of your last 42 days of training stress scores (TSS) and reflects the training you have done over the last 6 weeks. However, the workouts you did 15 days ago will impact your Fitness more than the workouts you did 30 days ago.
[1] Fitness, if you want to look at it that way

#### ATL (acute training load) 
An exponentially weighted average of your training stress scores from the past 7 days which provides an estimate of your fatigue accounting for the workouts you have done recently.
[1] fatigue

### Zones by sports
**Notes**

[5] Technically, the Sweet Spot is located between high zone 3 and low zone 4: between 84% to 97% of your FTP (power at threshold). For riders who aren’t using a power meter, I’d call Sweet Spot “medium hard”. Sweet Spot is just below your 40k time trial race pace, but harder than a traditional tempo workout.
#### Bike Power Zones:
* Zone 1 (Active recovery) Less than 55% of FTPw
* Zone 2 (Endurance) 55% to 74% of FTPw
* Zone 3 (Tempo) 75% to 89% of FTPw
* Sweetpot 84% to 97% of FTPw
* Zone 4 (Threshold) 90% to 104% of FTPw
* Zone 5 (VO2 Max) 105% to 120% of FTPw
* Zone 6 (Anaerobic capacity) More than 120% of FTPw
#### Run Pace Zones [2]:
* Zone 1 Slower than 129% of FTP
* Zone 2 114% to 129% of FTP
* Zone 3 106% to 113% of FTP
* Sweetspot 102% to 110% of FTP 
* Zone 4 99% to 105% of FTP
* Zone 5a 97% to 100% of FTP
* Zone 5b 90% to 96% of FTP
* Zone 5c Faster than 90% of FTP
#### Swim Pace Zones [3]:
* Zone 1 SW (slow)
* Zone 2 5 secs slower than T-Time
* Zone 3 T-Time
* Sweetspot 102% to 110% of FTP 
* Zone 4 99% to 105% of T-Time
* Zone 5a 97% to 100% of T-Time
* Zone 5b 90% to 96% of T-Time
* Zone 5c Faster than 90% of T-Time

### Rules
The bot will generate two workouts per sport each week [6]. A short one (focus: intensity) and a long one (focus: volume). The training plan is a balanced amount of intensity and volume that increases an athlete’s functional threshold power (FTP) [5].
The bot uses a weight factor to slightly adapt the weekly increase/decrease in workouts.
depending of you level. 
beginner : -2
Normal : 0
Advance : +2

The athlete follows the prescribed workout. The bot will adapt the workout plan by using a machine 
learning (ml) algorithm to detect over/under training. The ml is using examples of
over/under/normal training sessions. More in this in the ML section.  

#### Intensity (aka Short Workouts)
Intensity workout cannot be based on a target race pace. You cannot tell your
body to do a pace. You need to train it to be as fast as possible. The 
rule of thumb is to never increase more than 10% each week. You also need to 
train in micro-cylce. So for intensity workouts, the time spent on 
the sweet spot [5] follows the following rule:

Starting point is (this must be weighted using athlete's level):
* 15 minutes @ sweet spot (sp) for Spring distance,
* 30 minutes @ sweet spot (sp) for Olympic distance, 
* 60 minutes @ sweet spot (sp) for 70.3 distance,
* 120 minutes @ sweet spot (sp) for Ironman distance

Then increase time at sweet spot is following a four weeks pattern:
* week1: 10%
* week2: 10%
* week3: 10%
* week4: -10% (end of micro-cycle)

##### Special Weeks
* At week 15 (or 75% for all training plan done), we repeat week 14 (or 0%)
* At week 16 (end of micro-cycle), -0.41
* At week 17 start bricks (intensity followed by distance), 
start new micro-cycle but using repeat week 13 intensity

For bike and run workouts, we have a configurable set of workout templates used for short workouts [7].
Templates will be adapted by the bot.
 These will help change the monotonic nature. The bot also offers an API to create your own template :

**Pyramids**

* 30 seconds SP/30 seconds recover
* 1 minute SP/1 minute recover
* 2 minutes SP/2 minutes recover
* 4 minutes SP/4 minutes recover
* 2 minutes SP/2 minutes recover
* 1 minute SP/1 minute recover
* 30 seconds SP/30 seconds recover
* Finish with a 10-minute cooldown.

**Cardio Blaster**

Warm up for 15 minutes.
Then run or bike for 3 minutes at SP. 
Take three minutes active recovery (you're still moving, but at an easy pace) and 
repeat the 3 on/3 off pattern three to four more times.
Finish with a 10-minute cooldown.

**Speedplay**

Warm up for 15 minutes, adding a few 20-second bursts at the end to prepare for the workout.
Run or bike for 30 seconds at SP. Take three minutes active recovery and repeat the 30 on/3 off pattern five or six more times.
Finish with a 10-minute cooldown.

#### Long Workouts
The long workout will increase distances until you get close or a little above the target race distance. Sessions for long
workouts are based on the same rule as short workouts. The 10% rule applies in distances also to prevent injuries. But % effects distances.
Intensity must remain at z2 or below. The bot will set your race day distance 
and decrease distances each week. In some weeks (near the end) you will do distance a little 
above the targeted distance. 

Target:
* 5k for Spring distance,
* 10k for Olympic distance, 
* 21k for 70.3 distance,
* 42k for Ironman distance

Then increase distances following a four weeks pattern (weighted):
* week1: 10%
* week2: 10%
* week3: 10%
* week4: -10% (end/start micro-cycle)



##### Special Weeks
* At week 15 (or 75% for all training plan done), we repeat week 14 (or 0% increase)
* At week 17 start bricks (intensity followed by distance), start new micro-cycle but using repeat week 13 distance

##### Injury Notification
The Bot will adjust the training schedule based on types and intensity of the injury reported by the athlete. For intances, Erick was following week 14 running schedule and experienced sharp pain during the fifth interval. Week 14 should have been a low intensity (rest week) but a bug in the planning algo kept the intervals high and oups! injury. Anyways, the injury (achilles tendinitis) it is. The Bot will diminish load for at least two weeks and will keep (persist) that week 14 must be rest week and perhaps personalize this week for Erick for next year of 20 weeks planning. 
Injuries happens when [12]:
1. Training structure – too much speed work
1. Long runs are too fast
1. Rapid change in training type – addition of treadmill
1. ‘Failure to adapt’   

Training Bot will check and adjust planing according to training log to prevent injury. 

## Machine Learning
The athlete follows the prescribed workout. The bot will adapt the workout plan by using a machine learning (ml) algorithm to predict over/under/normal training. The ml is using a training set of over/under/normal training sessions.
What ML will give us is:
* How do I predict if an athlete will be over-trained/under-trained so we can adjust his training plan accordingly and notifies him?
* How training bot learn to distinguish between an over-trained/under-trained/normal athlete?

For supervised hypothesis, training set are carefully created by a specialized data scientist who knows the best metrics to use. Metrics (or features) are based in raw strava data that you can find in the above section and listed below. 
Here are the attributes ml will use to train:
### Single Athlete Features:
* TSS
* TSB
* CTL
* ATL
* Kilojoules
* Wattage (bike)/pace (run and swim)
* Sleep
* Happiness
* Stress

### Building Training Data Set
To create realistic features data sets, we need to understand the possible values each feature can take and how they
influence the classes (normal, undertrained, overtrained). 
The best would be to have access to existing athletes data sets. But for the sake of this project, we will craft our
own data set. 
The way we will achieve this is pretty simple. We are already creating training plan that meant
to be save and sound with phases for intensity and distance. If an athletes follows the plan, we
expect ATL, CTL and TSB to be in normal ranges and athlete will never under/over train. 
To create under/over trained athletes we will simple create variations to create plans that will 
mock training sessions leading to over/under training. We expect ATL, CTL and TSB values to go along 
with these variation.

#### Intensity Variations
Based on rules for intensity workouts for "normal athlete sessions". 
We will create random ranges for each classes:
Normal
Then increase time at sweet spot is following a four weeks pattern:
* week1: 0.7  undertrained(0.0, 0.49) normal(0.5, 0.75) , overtrained(0.76, 1) 
* week2: 0.1  undertrained(0.0, 0.01) normal(0.02, 0.15) , overtrained(0.16, 0.2)
* week3: 0.1  undertrained(0.0, 0.01) normal(0.02, 0.15) , overtrained(0.16, 0.2) 
* week4: -0.41 (end of micro-cycle) undertrained(-0.60, -0.51) normal(-0.50, -0.45), overtrained(-0.44, 1)  
* week5: 0.7 (start new micro-cycle) undertrained(0.0, 0.49) normal(0.5, 0.75) , overtrained(0.76, 1) 

### Algorithm
#### Hypothesis A - Predictive Analytics [8]
Using features described above (TSS, TSB, CTL, ATL, ect..) we will use Spark ML Dataframe API to predict if an athlete is overtraining or not.
1) create training dataset for overtrained
2) create training dataset for not overtrained athletes
3) separate each datasets 80% training and 20% testing (we need to test our model)
4) train using sparl ml RandomForestClassifier (other algos could be tried as well)
5) test
6) classify historical data from athletes (real life)

If an athlete is overtrained, we need to adjust % or perhaps even skip a week of training.
#### Hypothesis B - Linear regression of single or multivariate metrics (supervised)
How do I predict a continuous variable (regression)? 
TSS alone (single) or TSS, TSB, CTL (multivariate)
* Linear regression
* regularization
* ridge regression, and LASSO; 
* local linear regression
* conditional density estimation.
##### Training Set
A set of training rides, runs and swims, representing normal, over-trained and under-trained workout sessions.
This will be fed into an ML algorithm.
- linear regression 
#### Hypothesis B - Unsupervised....which one...

## API
### POST /trainbot/plan

```
{
  "plan": "phase",
  "athlete": "Erick Audet",
  "email": "eaudet@jarics.com",
  "weeksToTrain": "20",
  "sports": [
    {
      "sport": "running",
      "level": "beginner|intermediate|expert|professional",
      "ftp": "00:05:45",
      "target": "00:05:27"
     },
     {
      "sport": "swimming",
      "level": "beginner|intermediate|expert|professional",
      "ftp": "00:01:45",
      "target": "00:01:27"
     },
    {
      "sport": "cycling",
      "level": "beginner|intermediate|expert|professional",
      "ftp": "235",
      "target": "250"
     }
  ]
}
```

### returns 200 and ics (calendar) is sent to email athlete and 
```
{
  "athleteId": "000000001"
} 
```

## Adjust phase
### upload gpx file to /trainbot/adjust/{athleteId}
### returns 200 and ics (modified calendar) is sent to email athlete and 
```
{
  "athleteId": "000000001"
} 
```

## Deployment and start
Define properties in file called: application.properties an insert the following variable:
nitrite.db.file.path={path}/trainingbot.bd

The UI is in trainingui project....see readme in that project to visualize a basic training plan...
### Local
#### Build
1. cd trainingbot
1. mvn clean install
#### Run
java -jar trainingbot/service/target/service-1.0-SNAPSHOT.jar --spring.config.location=application.properties
#### Usage
You will find most queries in the postman project here: /Training Bot Postman.postman_collection.json
Simply import and setup the {{host}} variable. There is no security yet.
##### Create, Update and Get an athlete
See: Postman examples
##### Get a 20 days plan or weekly session
See: Postman examples
##### Attach you strava profile
//TODO

### Canarie Server
Canarie Server Admin: https://nova-ab.dair-atir.canarie.ca/project/

Canari SSH: ubuntu@208.75.75.136

1. scp /Users/erickaudet/Documents/application.properties ubuntu@208.75.75.136:apps
1. scp /Users/erickaudet/dev/trainingbot/service/target/service-1.0-SNAPSHOT.jar ubuntu@208.75.75.136:apps
2. ssh ubuntu@208.75.75.136
3. java -jar /Users/erickaudet/dev/trainingbot/service/target/service-1.0-SNAPSHOT.jar --spring.config.location=/Users/erickaudet/Documents/application.properties 

## References
1. http://tailwind-coaching.com/2016/04/13/training-stress-score-fatigue/, Coach Rob
2. https://www.trainingpeaks.com/blog/joe-friel-s-quick-guide-to-setting-zones/, MAY 4, 2012  BY JOE FRIEL 
3. Swim Workouts for Triathletes, 2nd Ed. Practical Workouts to Build Speed, Strength, and Endurance, Gale Bernhardt and Nick Hansen
4. https://www.bikeradar.com/road/gear/article/ftp-for-cycling-what-functional-threshold-power-means-how-to-test-it-and-how-to-improve-it-48624/, By Ben Delaney, February 08, 2018
5. https://fascatcoaching.com/tips/how-to-sweet-spot/, BY FRANK OVERTON ON JULY 31, 2016
6. http://www.trinewbies.com/tno_trainingprograms/tno_18wSp.asp
7. https://www.active.com/running/articles/3-interval-training-plans-to-build-fitness-fast, By Jason R. Karp, Ph.D.
8. https://dzone.com/articles/predictive-analytics-with-spark-ml, by David Moyers  ·  Sep. 19, 17 · AI Zone · Tutorial
9. Joe Friel - Part 3- Training Stress Balance—So What?, Joe Friel 07/12/2015
10. Suggested Weekly TSS And Target CTL, 
11. https://blog.trainerroad.com/why-tss-atl-ctl-and-tsb-matter/, August 16, 2016  |  Chelsea Hejny
12. https://blogs.bmj.com/bjsm/2016/07/29/training-error-and-achilles-pain/, Training error and achilles pain, Posted on July 29, 2016 by BJSM
13. /weka-3.8/wekadocs/WekaManual.pdf