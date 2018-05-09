# AI for Triathlete
We use a bunch of professional articles (see References section  below) to create an adaptable training plan for Triathletes.

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
using a power/pace meter and to measure improvement.[4]
#### IF (Intensity Factor)
IF is a percentage of the FTP which represents the zones. Values are from 1 to 7.
#### TSB (Training Stress Balance)

#### CTL (chronic training load)
[1] Fitness, if you want to look at it that way

#### ATL (acute training load) 
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
* Zone 3 106% to 113% of FTP (**sweetspot**)
* Zone 4 99% to 105% of FTP
* Zone 5a 97% to 100% of FTP
* Zone 5b 90% to 96% of FTP
* Zone 5c Faster than 90% of FTP
#### Swim Pace Zones [3]:
* Zone 1 SW (slow)
* Zone 2 5 secs slower than T-Time
* Zone 3 T-Time (**sweetspot**)
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

#### Short Workouts
In the short one, time spent on the sweet spot [5] follows the following rule:

Starting point is (this must be weighted using athlete's level):
* 15 minutes @ sweet spot (sp) for Spring distance,
* 30 minutes @ sweet spot (sp) for Olympic distance, 
* 60 minutes @ sweet spot (sp) for 70.3 distance,
* 120 minutes @ sweet spot (sp) for Ironman distance

Then increase time at sweet spot is following a four weeks pattern:
* week1: 0.7
* week2: 0.1
* week3: 0.1
* week4: -0.41 (end of micro-cycle)
* week5: 0.7 (start new micro-cycle)

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
The long workout is a % increase of the target race distance. The curve for long
workouts should follow the same rule as short workouts but % effects distances.
Intensity must remain at z2 or below. The bot will set your race day distance 
and decrease in time. In some weeks (near the end) you will do distance a little 
above the targeted distance. 

Starting point is:
* 5k for Spring distance,
* 10k for Olympic distance, 
* 21k for 70.3 distance,
* 42k for Ironman distance

Then increase time at sweet spot is following a four weeks pattern:
* week1: 5%
* week2: 8%
* week3: -10%
* week4: 20% (end/start micro-cycle)

##### Special Weeks
* At week 15 (or 75% for all training plan done), we repeat week 14 (or 0% increase)
* At week 17 start bricks (intensity followed by distance), start new micro-cycle but using repeat week 13 distance

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
### Training Set
Starting from a real training ride, run and swim, a simulator will create normal, overtrained and undertrained workout sessions.
This will be fed into a linear regression algorithm. This supervised ml will converged and be able to classify .... 

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

## References
1. http://tailwind-coaching.com/2016/04/13/training-stress-score-fatigue/, Coach Rob
2. https://www.trainingpeaks.com/blog/joe-friel-s-quick-guide-to-setting-zones/, MAY 4, 2012  BY JOE FRIEL 
3. Swim Workouts for Triathletes, 2nd Ed. Practical Workouts to Build Speed, Strength, and Endurance, Gale Bernhardt and Nick Hansen
4. https://www.bikeradar.com/road/gear/article/ftp-for-cycling-what-functional-threshold-power-means-how-to-test-it-and-how-to-improve-it-48624/, By Ben Delaney, February 08, 2018
5. https://fascatcoaching.com/tips/how-to-sweet-spot/, BY FRANK OVERTON ON JULY 31, 2016
6. http://www.trinewbies.com/tno_trainingprograms/tno_18wSp.asp
7. https://www.active.com/running/articles/3-interval-training-plans-to-build-fitness-fast, By Jason R. Karp, Ph.D.