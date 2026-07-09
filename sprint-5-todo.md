# fonctionnalites a implementer dans le sprint 5 
- [ ] sprint 5 
  
  - [x] Modifier le angeli.sprint.utils.reflect.Mapper pour separer les GET POST en 2 listes separee
    - [x] Modifier la fonction mapURLToMethod() ajouter un arg String method 
    - [x] Creer 2 fonction mapGET et mapPOST qui apppellent mapURLToMethod("GET" ou "POST")
  

  - [x] creer la classe angeli.sprint.model.ModelAndView
    - [x] attribut contexte 
    - [x] Fonction setAttribute(String,Object)
    - [x] Fonction getView() String avec suffix et prefix 
    - [x] Attribut view String

  - [ ] creation de la classe angeli.sprint.servlet.PageWriter
    - [x] Creer la fonction viewPageNotFound(errorType)
      - [ ] Affiche les urls disponible si on a tapper un truc qui existe pas
      - [ ] Affiche erreur de page si on dis que le fichier jsp est pas la
    - [ ] Creer la fonction loadView()
      - [ ] Affiche la vue si elle existe
      - [ ] Si la vue n'existe pas dire que ce n'est pas une vue
  
  - [ ] Modifier la class angeli.sprint.util.URLParser
    - [ ] Creer la fonction readURL() retourne null ou path de view
    
    - [x] Modifier angeli.sprint.listner.ContextListner
      - [x] Creer la fonction initPrefixAndSuffix
  

# problemes futurs a eviter 
- [ ] sprint 5 - fix 
  - [ ] envoyer toutes les erreurs dans catalina.out au lieu de catalina.log
    - [ ] Modifier angeli.sprint.util.reflect.Mapper 
  - [ ] **Ajouter des commentaires sur les fonction et les classes de sprint 3** 
    - [ ] angeli.sprint.utils.URLParser
    - [ ] angeli.sprint.url.URLMethod
