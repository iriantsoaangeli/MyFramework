- [ ] creation de liste par etage 
    - [ ] Changer la fonction ParamParser.parseNames pour prendre le 1er etage
        - [ ] retourner un String[] des Object et de leurs name

- [ ] Creer la fonction ParamParser.nextEtage(Map<String,String[]>) qui retire tout ce qu'il y a devant le "."
- [ ] Creer la fonction ParamParser.next(Map<String,String[]>) pour verifier si il y a encore un etage : true , false
- [ ] Creer la fonction Descriptor.createObject(Map<String,String[]>) , appel recursif et utilisant  ParamParser.ParseEtage(Map<String,String[]>) , donnant l'objet de l'etage inferieur 
  - [ ] Boucle les field et pour chaque Field a creer, verifie si primitif sinon creer un descriptor a appeler recursivement 