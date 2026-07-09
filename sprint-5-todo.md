# fonctionnalites a implementer dans le sprint 5 
- [ ] sprint 5 
  - [ ] creer la classe angeli.sprint.model.ModelAndView
    - [ ] attribut contexte 
    - [ ] Fonction setAttribute(String,Object)
    - [ ] Fonction getView()
    - [ ] Attribut view String

  - [ ] Modifier  angeli.sprint.servlet.FrontControllerServlet
    - [ ] Creer la fonction viewUrlNotFound()
      - [ ] Affiche les urls disponible si on a tapper un truc qui existe pas
      - [ ] Affiche erreur de page si on dis que le fichier jsp est pas la
    - [ ] Creer la fonction loadView()
      - [ ] Affiche la vue si elle existe
      - [ ] Si la vue n'existe pas dire de verifier le path
    
  

# problemes futurs a eviter 
- [ ] sprint 5 - fix 
  - [ ] envoyer toutes les erreurs dans catalina.out au lieu de catalina.log
    - [ ] Modifier angeli.sprint.util.reflect.Mapper 
- [ ] **Ajouter des commentaires sur les fonction et les classes de sprint 3**