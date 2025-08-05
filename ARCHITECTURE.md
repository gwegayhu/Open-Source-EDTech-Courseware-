# Architecture

RESPECT's network architecture is designed to:
* **Use existing open standards**: [OAuth](https://oauth.net/2/), [xAPI](https://www.xapi.com/), 
  [OneRoster](https://www.1edtech.org/standards/oneroster), and [Web Publication](https://github.com/readium/webpub-manifest).
* **Support digital sovereignty**: ensure users, organizations, and governments can decide where 
  they store their personal information.
* **Support offline usage and reducing data usage**: maximize support for offline usage (though
  some login mechanisms may require Internet access) and support a variety of techniques to reduce
  data usage (including peer-to-peer caching, ISP/mobile network content delivery networks, etc).
* **Be language and infrastructure agnostic**: APIs can be implemented using a federated monoliths,
  clustering, microservices, or any combination thereof.

RESPECT's network design includes:
* **Realms** (typically a school): in the context of network security, a realm is a logical grouping
  of networked resources each with its own users, security rules, and data. Each RESPECT realm has an 
  xAPI LRS endpoint and OneRoster endpoint. It may also implement some RESPECT extension endpoints
  (e.g. for user sign-up via invitations etc). A realm is represented by a json file e.g.

GET https://school.example.org/respect-realm.json
```
{
    name: "School Name",
    self: "https://school.example.org/respect-realm.json",
    xapi: "https://school.example.org/api/xapi/",
    oneRoster: "https://school.example.org/api/oneroster/",
    respectExt: "https://school.example.org/api/respect/" 
}
```
School-level realms group related data together enabling each to be its own database (e.g. 
[horizontal partitioning](https://en.wikipedia.org/wiki/Partition_(database)#Partitioning_methods)).
This dramatically reduces the work required to perform queries (e.g. searching a users table that
contains only a thousand or so users in a particular school instead of a table that contains
all users in a district/country).

* **Directories** (typically country or regional level): a directory server lists realms. It is used
  by the RESPECT app to find the realm for a given school name. Directory servers are also used when 
  handling sign-up invitations.

