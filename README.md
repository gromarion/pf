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
- Open the PGAdmin application and execute the SQL code in the src/test/resources/postgresql-create-tables.sql file.
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
- Open the PGAdmin application and execute the SQL code in the src/test/resources/postgresql-create-tables.sql file.
- Stepped in the `PATH_TO_APACHE_TOMCAT_FOLDER` folder, run `sh bin/startup.sh`
- Open a web browser and head to `htp://localhost:8080/dataqualityassessmens-1.0-SNAPSHOT/`

## Usage
### User registration and login
If it's your first time using the application, then you must register first before getting started. In order to do so, click the **¿Aún no eres un usuario? ¡Regístrate ahora!** link. This will redirect you to a page that will require you to fill up some basic information: full name, username, password and password confirmation, as shown below in the animated .GIF:

![alt text](https://raw.githubusercontent.com/gromarion/pf/master/readme_images/register_user.gif)

Once you've been fully registered, you can get started by logging in.

### Manual checks
Once the user is logged in the application, he can start doing some manual checks on any SPARQL endpoint loaded in the application, by making a search of any resource:

![alt text](https://raw.githubusercontent.com/gromarion/pf/master/readme_images/manual_checks.gif)

If he doesn't know exactly what to look for, he can always click the **Obtener recurso aleatorio** button, to fetch any random resource in the chosen endpoint:

![alt text](https://raw.githubusercontent.com/gromarion/pf/master/readme_images/fetch_next_random.gif)

### Resource detail
When the user finds the resource that he was looking for, they can click it to see in detail all of its properties:

![alt text](https://raw.githubusercontent.com/gromarion/pf/master/readme_images/resource_detail.gif)

### Error selection page
If the user detects some errors in the selected resource, he can select the most accurate error, as shown in the following image:

![alt text](https://raw.githubusercontent.com/gromarion/pf/master/readme_images/error_selection_page.gif)