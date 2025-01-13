# filestore
This is a simple service that wraps minio to provide a simple API for storing and retrieving files.

## How to run

Local
```
./gradlew quarkusDev
```

## Config
.env
```
PORT=8080
DB_USER=global
DB_PASSWORD=Xyz
DB_URL=jdbc:postgres://...
STORAGE_PATH=/
BUCKET_NAME=
MINIO_URL=
MINIO_PORT=
MINIO_ACCESS_KEY=
MINIO_SECRET_KEY=
```