# PagoNxt One Trade Finance Flowable Work

```
________              ___________                  .___      
\_____  \   ____   ___\__    ___/___________     __| _/____  
 /   |   \ /    \_/ __ \|    |  \_  __ \__  \   / __ |/ __ \ 
/    |    \   |  \  ___/|    |   |  | \// __ \_/ /_/ \  ___/ 
\_______  /___|  /\___  >____|   |__|  (____  /\____ |\___  >
        \/     \/     \/                    \/      \/    \/ 
```

## Instructions

### Building the project artifacts

Make sure that you have maven and npm properly configured for your user. See the following pages for additional information
in how to setup your local environment:

- [Maven](https://wiki.flowable.com/display/ITKB/Maven+Repository)
- [NPM](https://wiki.flowable.com/display/ITKB/NPM+Repository)

Afterwards, compiling the project and building the frontend is done with

`mvn clean install`

### Setting up the needed Infrastructure

For an easier setup with help of Docker navigate to `/dockerlocal`, then execute `docker-compose up`. Needed services such as 
Elasticsearch and a database will be started.

## Starting the project
Afterwards you can start the Spring Boot application defined in `com.pagonxt.onetradefinance.work.PagonxtOneTradeFinanceWorkApplication`. In order to achieve this,
you can build the executable war file with maven and execute `java -jar` pointing to the built jar or create a new IDE Run Configuration. 
Then open `http://localhost:8090` in the browser and use one of the users specified below in this document.

You can start the Flowable Design and Flowable Control applications accordingly.

## Helpful links
After both docker-compose and the application are started, these are the links for the different applications:

- http://localhost:8090 - Flowable Work
- http://localhost:8091 - Flowable Design
- http://localhost:8092 - Flowable Control

## Infrastructure Containers
If you need to recreate the containers, perform the following actions:
- Make sure the application and the docker containers are stopped.
- Execute `docker-compose down` inside the `/docker` respective directory. This will remove the created containers

## Data
Data created by the database and Elasticsearch are stored in docker volumes `data_db` and `data_es`.
This allows you to purge and recreate the containers without loosing any data.

If you need a clean state for the database and elasticsearch just execute `docker-compose down -v`inside the `/docker` respective directory.
The volumes will be recreated as soon as you restart the containers.  
BE CAREFUL AS WITH THIS YOU WILL LOSE ALL YOUR DATA STORED IN THE DATABASE AND ELASTICSEARCH!

## Sample users
| User                            | User Definition Key   | Login                 | Password |
|---------------------------------|-----------------------|-----------------------|----------|
| Flowable Administrator          | admin-flowable        | admin                 | test     |
| One Trade Finance Administrator | admin-oneTradeFinance | onetradefinance.admin | test     |
| One Trade Finance User          | user-oneTradeFinance  | oneTradeFinance.user  | test     |

## Change Log

### 0.0.1
- Initial commit

