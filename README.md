## Commandes mavens (JDK 17)
### Initialiser 
`
mvn archetype:generate -DgroupId=angeli.test.servlet -DartifactId=test -DarchetypeArtifactId=maven-archetype-quickstart -DinteractiveMode=false
`

### Package 
`
mvn clean package
`


├── mon-framework \
│   ├── pom.xml \
│   ├── src \
│   └── target  \
├── README.md   \
├── test    \
│   ├── pom.xml \
│   └── src         \
└── Tomcat      


## Sprint 0 : FrontControllerServlet :
    doGet(),doPost() -> ProcessRequest() : print url .

