# AI for Triathlete
We use a bunch of professional articles (see References below) to create an adaptable training plan for Triathletes.
## Plan phase
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

## Definitions
### Functional Threshold Pace (FTP)
FTP stands for Functional Threshold Power/Pace, which is commonly defined as the highest average power/pace 
you can sustain for an hour, measured in watts. FTP is often used to determine training zones when 
using a power/pace meter and to measure improvement.[4]
**Notes**

[5] Technically, the Sweet Spot is located between high zone 3 and low zone 4: between 84% to 97% of your FTP (power at threshold). For riders who aren’t using a power meter, I’d call Sweet Spot “medium hard”. Sweet Spot is just below your 40k time trial race pace, but harder than a traditional tempo workout.

### Zones by sports
#### Bike Power Zones [3]:
* Zone 1 (Active recovery) Less than 55% of FTPw
* Zone 2 (Endurance) 55% to 74% of FTPw
* Zone 3 (Tempo) 75% to 89% of FTPw
* Zone 4 (Threshold) 90% to 104% of FTPw
* Zone 5 (VO2 Max) 105% to 120% of FTPw
* Zone 6 (Anaerobic capacity) More than 120% of FTPw
 
#### Run Pace Zones [2]:
* Zone 1 Slower than 129% of FTP
* Zone 2 114% to 129% of FTP
* Zone 3 106% to 113% of FTP
* Zone 4 99% to 105% of FTP
* Zone 5a 97% to 100% of FTP
* Zone 5b 90% to 96% of FTP
* Zone 5c Faster than 90% of FTP
#### Swim Pace Zones [3]:
* Zone 1 SW (slow)
* Zone 2 5 secs slower than T-Time
* Zone 3 T-Time
* Zone 4 99% to 105% of T-Time
* Zone 5a 97% to 100% of T-Time
* Zone 5b 90% to 96% of T-Time
* Zone 5c Faster than 90% of T-Time

### Rules
The bot will generate two workouts per sport each week [6]. A short one (focus: intensity) and a long one (focus: volume). The training plan is a balanced amount of intensity and volume that increases an athlete’s functional threshold power (FTP) [5].
In the short one, time spent on the sweet spot [5] follows the following rule:

Starting point is:
* 15 minutes @ sweet spot (sp)
* 30 minutes 
* 60 minutes 
* 120 minutes 
This is the lower limit and 3 hours is the upper UPPER limit
Then increase time at sweet spot is following a four weeks pattern:
* week1: 0.7
* week2: 0.1
* week3: 0.1
* week4: -0.41

The long workout is a % increase of the target race distance. The curve for long
workouts should follow the same rule of short workouts but % effects distance.
Intensity must remain at z2 or below. The bot will set you race day distance 
and decrease in time. In some weeks (near the end) you will do distance a little 
above the targeted distance. 
* week1: -0.41
* week2: 0.7
* week3: 0.1
* week4: 0.1

The bot also uses a weight factor to slightly adapt the weekly increase/decrease 
depending of you level. 
beginner : -2
Normal : 0
Advance : +2

The athlete follows the prescribed workout. The bot will adapt the workout plan by using a machine 
learning (ml) algorithm to detect over/under training. The ml is using examples of
over/under/normal training sessions. More in this in the ML section.  

### IF (Intensity Factor)

### TSS (Training Stress Score)
TSS = [(s x NP x IF) / (FTP x 3,600)] x 100 [1]
#### Example
IF is the intensity factor representing the time you spend at FTP during a session. 
The rule is to keep the time at FTP under 10% increase per week and follow my rules.  

### TSB (Training Stress Balance)

### CTL (chronic training load)
[1] Fitness, if you want to look at it that way

### ATL (acute training load) 
[1] fatigue

## Machine Learning
The athlete follows the prescribed workout. The bot will adapt the workout plan by using a machine  learning (ml) algorithm to detect over/under/normal training. The ml is using examples of over/under/normal training sessions.
The examples are generated using simulations. The simulations creates virtual athletes doing virtual workouts.
Here are the attributes ml will use to train:
### Single Workout Attributes:
* TSS
* TSB
* CTL
* ATL
* Kilojoules
* Wattage (bike)/pace (run and swim)
* Sleep
* Happiness
* Stress

## References
[1] http://tailwind-coaching.com/2016/04/13/training-stress-score-fatigue/
[2] https://www.trainingpeaks.com/blog/joe-friel-s-quick-guide-to-setting-zones/
[3] Swim Workouts for Triathletes, 2nd Ed. Practical Workouts to Build Speed, Strength, and Endurance
[4] https://www.bikeradar.com/road/gear/article/ftp-for-cycling-what-functional-threshold-power-means-how-to-test-it-and-how-to-improve-it-48624/
[5] https://fascatcoaching.com/tips/how-to-sweet-spot/, BY FRANK OVERTON ON JULY 31, 2016
[6] http://www.trinewbies.com/tno_trainingprograms/tno_18wSp.asp