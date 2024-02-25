[![Ticket Service CI](https://github.com/sandor95/ticket-service/actions/workflows/master.yml/badge.svg)](https://github.com/sandor95/ticket-service/actions/workflows/master.yml)

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

Kimenet: `target/docs/specification/`


## Egyéb tool-ok

### draw.io

A teljes infrastruktúra és az egyes microservice-k class diagramjai [draw.io-ban](https://app.diagrams.net/) vannak ábrázolva.

Könyvtár: `docs/architecture`


### RabbitMQ

A microservice-k közötti aszinkron kommunikációról message queue gondoskodik, mely csak a DEV környezet configjait tartalmazza.

Config: `tools/rabbit_mq`

### MySQL

A microservice-k sémái egy közös MySQL db-ben találhatók, ami local fejlesztésre megfelelő,
más környezetekben dedikált DB-re lenne szüksége minden microservice-nek.

Config: `tools/database`