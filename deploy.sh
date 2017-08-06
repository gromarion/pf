git push origin master;
git push heroku master;
mvn clean heroku:deploy-war
