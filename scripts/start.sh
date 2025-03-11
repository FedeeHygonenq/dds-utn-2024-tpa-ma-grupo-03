#!/bin/bash

# Esperar a que MySQL esté listo
./wait-for-it.sh ${MYSQLHOST}:${MYSQLPORT} --timeout=60 --strict -- echo "MySQL is up"


# Finalmente, ejecutar la aplicación Java
java -jar app.jar
