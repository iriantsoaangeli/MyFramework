cd "/home/angeli/Asa/Sprint(Mr Naina)/mon-framework"
mvn clean package
cp ./target/mon-framework-1.0-SNAPSHOT.jar "/home/angeli/Asa/Sprint(Mr Naina)/test/src/main/webapp/WEB-INF/lib"
cd "/home/angeli/Asa/Sprint(Mr Naina)/test"
mvn clean package
cp ./target/test-test.war "/home/angeli/Asa/Sprint(Mr Naina)/Tomcat/webapps"
"/home/angeli/Asa/Sprint(Mr Naina)/Tomcat/bin/startup.sh"