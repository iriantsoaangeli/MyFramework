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

## Sprint 4 :

    utiliser le ContextListner au demarrage (Deja fais en sprint 1 )


## Sprint 5 :

    On gerera le view donnes par les methodes dans les controllers  via une classe ModelAndView

## Sprint 6 : 

    On peut choisir le type de content de la page (json ou HTML)
    L'annotation Controller a maintenant un attribut ContentType

## Sprint 7 :
    Utiliser les elements de la requete comme argument d'un controller 


## Sprint 7 bis :
    Cette fois ci on envoie des objets personnalise dans la requetes 
        Exemple :
   
```java
//Avant
addEmploye(String nom , String mail , int numero )

//Apres 
addEmploye(Employe emp)
```

        
