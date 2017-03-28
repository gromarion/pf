# Data Quality Assesment

Developers
  - [Alderete, Facundo](https://github.com/facualderete)
  - [De La Puerta Echeverría, María](https://github.com/mariadelapuerta)
  - [Romarión, Germán](https://github.com/gromarion)

**Data Quality Assesment** is a Java-Wicket powered web application for Semantic Web data quality analysis.

## Installation
**General Requirements**:
  - Download and install the latest version of [PostgreSQL](https://www.postgresql.org/download/)

### Run the application from Eclipse
**Requirements**:
  - Download and install the latest version of [Eclipse](https://eclipse.org/downloads/)
  - Dowload the Maven plugin for Eclipse: Help -> Install New Software... -> Paste the following URL in the "Work within" input field: http://download.eclipse.org/technology/m2e/releases -> Select all packages to install

**Setup**:
  - Clone this repository to your computer
  - Import the project in Eclipse: File -> Import... -> Maven -> Existing Maven Projects
  - In Eclipse, right click on the src/test/java/com/itba/Start.java -> Run As -> Java Application
  - By default, the application will be listening for requests at the `127.0.0.1:8443`. Open a web browser and head to `http://localhost:8443` to get started.

### Run the application from an Apache Tomcat server
***Requirements***:
  - Download the latest version of [Apache Tomcat](http://tomcat.apache.org/whichversion.html)

***Setup***:
  - Clone this repository to your computer
  - Step inside the cloned repository
  - run `mvn package`. This will generate the `dataqualityassesments-1.0-SNAPSHOT.war` file inside the `target` folder
  - copy the `.war` file in the `PATH_TO_APACHE_TOMCAT_FOLDER/webapps/` folder
  - Stepped in the `PATH_TO_APACHE_TOMCAT_FOLDER` folder, run `sh bin/startup.sh`
  - Open a web browser and head to `htp://localhost:8080/dataqualityassessmens-1.0-SNAPSHOT/`

## Usage
Coming soon...
