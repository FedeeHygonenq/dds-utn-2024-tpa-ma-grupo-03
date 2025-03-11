# Esperar a que SonarQube esté listo
./wait-for-it.sh localhost:9000 --timeout=60 --strict -- echo "SonarQube is up"

# Ejecutar el análisis con SonarQube
mvn verify sonar:sonar -Dmaven.test.skip=true