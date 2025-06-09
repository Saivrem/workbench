# Workbench

This is a generic utils project for daily usage, currently only capable of backup and restore functionality, however, supposed to be extensible and configurable for more functionality to be added

Receives a JSON file as a config passed to args[0]
```json
{
  "threadPoolCapacity": 1,
  "operationToParamsList": {
    "BACKUP": [
      {
        "backupOrigin": "/path/to/origin",
        "backupDestination": "/path/to/destination"
      }
    ],
    "ZIP": [
      {
        "zipOrigin": "path/to/origin",
        "zipDestination": "/path/to/destination",
        "restrictedFiles": [
          "string",
          "string2"
        ]
      }
    ]
  }
}
```
For each Operation enum there is supported list of Params objects, deserialization ignores absent or unknown fields
Each operation enum should be assigned to actual Executor that is a Runnable implementation. Threads pull capacity is configured via dedicated property.