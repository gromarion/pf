git push origin master;
git push heroku master;
git push --mirror https://bitbucket.org/itba/calidad-de-datos-en-linked-data;
mvn clean heroku:deploy-war
