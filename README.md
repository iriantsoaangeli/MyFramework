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

## Sprint 1 :
    angeli.sprint.annotation.controller
    Chargement de classe  au choix , puis verifier si il contiennet l'annotation
        - package specifique 
        - Tous 
    Au demarrage de l'appli web (ContextListener sinon init anle servlet raha tsy vita)
    Tsara ataovy izy roa


## Sprint 2 :
    Mapper les methodes avec annotation l'annotation specifiee a un URL


## Sprint 3 :
    Support Meme URL avec differentes methodes(post/get)


## Sprint 3+1/2 : 
    Run la methode dans l'url ?