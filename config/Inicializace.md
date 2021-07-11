# Inicializace

## Back-end 

### Databáze
Account service a Notification service vyžadují každá vlastní PostgreSQL databázi, kterou je potřeba v `application.properties` obou balíčků nakonfigurovat (username, password, url). Všechny úvodní data se dají bez problému vytvořit z UI, není tedy třeba spouštět inicializační skripty pro naplnění databáze.  
### Spuštění servis
Podle současného nastavení v `application.properties` je každá servisa spuštěna na samostatném portu, což na frontendu není problém, jelikož jsou `SecurityConfig.java` souborech nastaveny výjimky pro tyto porty z hlediska CORS.
Je potřeba upravit doménu z localhost na doménu, na které budou servisy hostovány (v souborech `SecurityConfig.java`)

Pro nasazení na heroku je definován skript pro spuštění v `./Procfile`: `web: java -jar target/cz.cvut.fel.nss.holidayPlanner-1.0.jar`

## Front-end

Ve složce `./frontend`:

```
cd frontend
yarn install // nainstalovat dependencies
yarn dev // spustit 
yarn build // build for deployment into /build directory -> should be deployed to frontend server
```

