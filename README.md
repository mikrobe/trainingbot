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

