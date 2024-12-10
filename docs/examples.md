## Create a session

**POST /sessions**

Creates a new session. You must specify a ueId, an asId and a qos. Notification configuration and duration are optional. Port information is optional as well and the session creation works without it. However, when you try to get or delete that session, the API returns a 500 error that requires restarting the API service. Therefore, it is recommended to always provide this field, specifying the whole port range (0-65535) if the session does not have to prioritize only a particular port, or a given range.

Available QoS levels:
* QOS_E
* QOS_S
* QOS_M
* QOS_L

The exact meaning of these QoS levels is defined in the [application.yml](../api/src/main/resources/application.yml), where the Media Type, Maximum Uplink Throughput and Maximum Downlink Throughput are defined.

```json
{
  "duration": 86400,
  "ueId": {
    "ipv4addr": "10.45.0.3"
  },
  "asId": {
    "ipv4addr": "10.45.0.1"
  },
  "uePorts": {
    "ranges": [
      {
        "from": 0,
        "to": 65535
      }
    ]
  },
  "asPorts": {
    "ranges": [
      {
        "from": 0,
        "to": 65535
      }
    ]
  },
  "qos": "QOS_S",
  "notificationUri": "http://192.168.40.170:9091/notifications",
  "notificationAuthToken": "c8974e592c2fa383d4a3960714"
}
```
Sample response:
```json
{
  "duration": 86400,
  "ueId": {
    "externalId": null,
    "msisdn": null,
    "ipv4addr": "10.45.0.3",
    "ipv6addr": null
  },
  "asId": {
    "ipv4addr": "10.45.0.1",
    "ipv6addr": null
  },
  "uePorts": {
    "ranges": [
      {
        "from": 0,
        "to": 65535
      }
    ],
    "ports": null
  },
  "asPorts": {
    "ranges": [
      {
        "from": 0,
        "to": 65535
      }
    ],
    "ports": null
  },
  "qos": "QOS_S",
  "notificationUri": "http://192.168.40.170:9091/notifications",
  "notificationAuthToken": "c8974e592c2fa383d4a3960714",
  "id": "1970cd96-e8d7-488e-beec-4bc86ed58d71",
  "startedAt": 1733758949,
  "expiresAt": 1733845349,
  "messages": [
    {
      "severity": "WARNING",
      "description": "AS address range is in private network (10.0.0.0/8). Some features may not work properly."
    }
  ]
}
```
Note that since we're prioritizing a connection to the 5G core itself, it shows a warning about the AS address range. It should not cause any issue, though.

## Get a session

**GET /sessions/{sessionId}**

Return all the information of a given session based on the id field returned when creating that session.

## Delete a session

**DELETE /sessions/{sessionId}**

Deletes a session given its id.