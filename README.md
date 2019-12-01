# KTrack

KTrack is a proof of concept application intended to investigate the requirements of an online tracker for records of dogs that are subject to the Trap-Neuter-Release (TNR) program of the not-for-profit [Canine Control and Care](http://caninecontrolandcare.org/ "Canine Control and Care") organization. This NGO has a setup of 60 kennels where stray dogs are kept for 3-4 days for the TNR program


### Prerequisites

You need to install the following software

```
JDK
Maven
MongoDB
Eclipse (or any other editor)
```

### Building and testing
1. Create a database with the URI `mongodb://admin:crypt*2461@localhost:27017/KTrack ` in MongoDB. This URI can be changed by modifying the` spring.data.mongodb.uri` property in the `/src/main/resources/application.properties` file.
1. Connect to this database using the Mongo shell and load the data from the `/src/main/resources/populatedb.js` file. This will setup the default user and other canned data.
1. Build the project using the `mvn clean install `command and run the application using the `mvn spring-boot:run` command. Alternatively use Eclipse Build/Run/Debug commands.
1. Navigate to `http://localhost:8080` and use the credentials user/password to login.


## Features

1. The All menu item lists all the records in the system providing pagination as well as sort, search and download features.
1. The Search feature permits search of the records using criteria such as Date and Caregiver data.
1. The New feature is used to enter a record for a dog. It permits upload using drag-drop of upto 3 snapshots and as well as maps to pinpoint the pickup location of the dog - this is used during the release to ensure no relocation. Auto complete is provided for vet and caregiver data based on existing records. A comment box captures important information such as any issues during surgery. The application also runs a scheduler thread to delete orphaned snapshots - i.e. uploaded snapshots whose forms were not submitted.
Note: Google Maps no longer works due to the missing billing account for the test key.
1. The Book Kennels and Admin fetaures were intended to explore the requirements around online booking of kennels by members of the public in advance but these features did not have sufficient clarity and were not completed

## Built With

* [Spring-Boot](https://spring.io/projects/spring-boot) - The web framework used
* [Wicket](https://wicket.apache.org/) - The UI framework used
* [Wicket-Bootstrap](https://github.com/l0rdn1kk0n/wicket-bootstrap) - Integration of Bootstrap with Wicket to define the layout and structure of the UI
* [MongoDB](https://www.mongodb.com/) - The NoSQL datastore
* [Google Maps](https://developers.google.com/maps/documentation/javascript/tutorial) - Used to generate geocode data for each record.
* [Datatables](https://datatables.net/) - Used to display records
* [JQuery](https://jquery.com/) - The JS framework used
* [Maven](https://maven.apache.org/) - Dependency Management and build framework

## Authors

* **Dhananjay Sharma** 

## License

This project is licensed under the Apache License - see the [license](https://github.com/Medium/opensource/blob/master/apache-license-2.0.md "license") file for details


