[![Ticket Service CI](https://github.com/sandor95/ticket-service/actions/workflows/master.yml/badge.svg)](https://github.com/sandor95/ticket-service/actions/workflows/master.yml)

## Futtatás

### Előkészületek

A projekt dokumentációjában ismertetett infrastruktúra lokális felépítéséhez a `tools` mappában ki kell adni a `docker-compose up` parancsot, mely egy MySQL adatbázist épít fel, illetve tölti be a default adatokat, e mellett pedig egy RabbitMQ broker-t is.

### API-k indítása

Az api-k elő vannak készítve a "dockeresítésre", azonban ez "nice to have" feature volt a fejlesztés alatt, mivel a lokális használathoz mellékes extra lett volna. Ebből kifolyólag az api-k "kézihajtényosak":
- IDEA-ból indítva: build, majd runt
- terminálból indítva: `mvn clean install -DskipTests`, majd `java -jar path/to/target/*.jar`

### Swagger UI

- core-api: http://localhost:8081/swagger-ui/index.html
- main-api: http://localhost:8082/swagger-ui/index.html
- ticket-api: http://localhost:8083/swagger-ui/index.html
- partner-api: http://localhost:8084/swagger-ui/index.html

## Build időben használt tool-ok

### Checkstyle

A formai ellenőrzés be van kötve a maven compile fázisába, így egyetlen komponens sem fordul le helytelenül formázott forráskóddal.

Futtatás: `mvn clean checkstyle:check`


### JaCoCo

A JaCoCo egy tesztminőség mérésre használt eszköz, mely képes törni a buildet, ha minőség egy adott szint alá esik. A tool erőssége az elágazások és az általános lefedettség mérése. A plugin a test fázisban fut meg, így a unit tesztek futtatása után készít riportot.  

Futtatása: `mvn clean test`

Riport: `target/reports/jacoco/index.html`


### Pit test

A pit test egy olyan tool, ami a forráskód mutálásával (módosításával) ellenőrzi a tesztek minőségét, pl.: eltávolít egyes, logikailag fontos sorokat, metódus paramétereket cserél null-ra, negálja a feltételeket. Működése a mutálás után egyszerű, ha nem törik el legalább egy teszt, akkor alacsonyabb értékelést kapnak a tesztek. Futtatása igen erőforrásigényes, így nem célszerű a unit tesztek futásával egy fázisba bekötni.

Futtatása: `mvn clean test-compile pitest:mutationCoverage` 

Riport: `target/reports/pitest/index.html`


### AsciiDoctor

A projekt dokumentáció AsciiDoc formátumban íródott, melyekből az AsciiDoctor generál html-t. 

Futtatása (a docs modulban!): `mvn clean generate-resources`

Kimenet: `target/docs/documentation/`


## Egyéb tool-ok

### draw.io

Az infrastruktúra diagramja [draw.io-ban](https://app.diagrams.net/) van ábrázolva.

Könyvtár: `docs/architecture`


### Postman

A fejlesztői tesztekhez használt Postman kérések exportálásra kerültek és megtalálhatóak a tools mappa latt.

Állomány: `tools/ticket_service_requests.postman_collection_v2_1.json`


### RabbitMQ

A felépített jegyfoglaló szimulációban egy nagyon fapados naplózás is helyet kapott. Az api-k és a napló consumer között egy message broker található, melynek számos előnye van.

Config: `tools/rabbit_mq`

### MySQL

A microservice-k sémái egy közös MySQL db-ben találhatók, ami local fejlesztésre megfelelő,
más környezetekben dedikált DB-re lenne szüksége minden microservice-nek.

Config: `tools/database`