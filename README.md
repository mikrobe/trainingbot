# AI for Triathlete
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

### Rules
The bot will generate two workouts per sport. A short one (focus: intensity) and a long one (focus: km).
In the short one, time spent @ FTP follows the following rule:

Increase time at FTP (z3) rules for four weeks pattern:
beginner : {-0.46, 0.60, 0.05, 0.05};
Normal : {-0.41, 0.70, 0.10, 0.10};
Advance : {-0.35, 0.75, 0.15, 0.15};

The long workout is a % increase of the target race distance. The curve for long
workouts should follow the same rule of short workouts but % effects distance.
Intensity must remain at z2 or below. The bot will set you race day distance 
and decrease in time. In some weeks (near the end) you will do distance a little 
above the targeted distance. 
beginner : {-0.46, 0.60, 0.05, 0.05};
Normal : {-0.41, 0.70, 0.10, 0.10};
Advance : {-0.35, 0.75, 0.15, 0.15};

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

## References
[1] http://tailwind-coaching.com/2016/04/13/training-stress-score-fatigue/
[2] https://www.trainingpeaks.com/blog/joe-friel-s-quick-guide-to-setting-zones/