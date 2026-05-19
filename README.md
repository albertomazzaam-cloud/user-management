# User Management
Piccola applicazione Spring Boot per gestire utenti.

## Cosa fa
- CRUD utenti (`/api/users`)
- ricerca utenti con filtri opzionali su nome/cognome (`/api/users/search`)
- upload CSV per inserimento massivo (`/api/users/upload-csv`)

## Tecnologie utilizzate
- Java 17
- Spring Boot
- PostgreSQL
- Docker Compose

## Avvio rapido con Docker
Per avviare sia l'immagine dell'app che il database PostgreSQL:
`docker compose up --build` (solo la prima volta)
 `docker compose up` (successivamente)

Per fermare i container:
`docker compose stop`

Per fermare e rimuovere i container:
`docker compose down`
`docker compose down -v` (per eliminare anche il volume) 

API disponibile su `http://localhost:8090`.

## CSV di esempio
Per provare l'upload è disponibile il file:
- `sample-data/users-csv-example.csv`

## Collection Postman
Per provare tutte le chiamate API è disponibile anche la collection Postman:
- `postman/user-management-postman-collection.json`
