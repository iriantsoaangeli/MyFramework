# fonctionnalites a implementer dans le sprint 5 
- [ ] sprint 5 
  
  - [x] Modifier le angeli.sprint.utils.reflect.Mapper pour separer les GET POST en 2 listes separee
    - [x] Modifier la fonction mapURLToMethod() ajouter un arg String method 
    - [x] Creer 2 fonction mapGET et mapPOST qui apppellent mapURLToMethod("GET" ou "POST")
  

  - [ ] creer la classe angeli.sprint.model.ModelAndView
    - [ ] attribut contexte 
    - [ ] Fonction setAttribute(String,Object)
    - [ ] Fonction getView()
    - [ ] Attribut view String

  - [ ] Modifier  angeli.sprint.servlet.FrontControllerServlet
    - [ ] Creer la fonction viewPageNotFound()
      - [ ] Affiche les urls disponible si on a tapper un truc qui existe pas
      - [ ] Affiche erreur de page si on dis que le fichier jsp est pas la
    - [ ] Creer la fonction loadView()
      - [ ] Affiche la vue si elle existe
      - [ ] Si la vue n'existe pas appeler PageNotFound

    - [ ] Creer une classe angeli.sprint.util.Config
      - [ ]  Creer une fonction getSuffixAndPrefix()
        - [ ]  Lire le xml 
    
    - [x] Modifier angeli.sprint.listner.ContextListner
      - [x] Creer la fonction initPrefixAndSuffix
  

# problemes futurs a eviter 
- [ ] sprint 5 - fix 
  - [ ] envoyer toutes les erreurs dans catalina.out au lieu de catalina.log
    - [ ] Modifier angeli.sprint.util.reflect.Mapper 
  - [ ] **Ajouter des commentaires sur les fonction et les classes de sprint 3** 
    - [ ] angeli.sprint.utils.URLParser
    - [ ] angeli.sprint.url.URLMethod
