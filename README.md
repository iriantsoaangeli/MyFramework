## Ceci est un document personnel de reference 

## Commandes mavens (JDK 17)
### Initialiser 
`
mvn archetype:generate -DgroupId=angeli.test.servlet -DartifactId=test -DarchetypeArtifactId=maven-archetype-quickstart -DinteractiveMode=false
`

### Package 
`
mvn clean package
`
## Le dossier test
```text
mon-app-web/
├── pom.xml
└── src/
    └── main/
        ├── java/         
        └── webapp/       
            ├── index.html
            └── WEB-INF/ 
                ├── lib/ 
                └── web.xml
```
## Sprint 0 : FrontControllerServlet :
    doGet(),doPost() -> ProcessRequest() : print url .

