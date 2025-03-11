# Imagen base para compilar
FROM maven:3.9.9-eclipse-temurin-17 AS builder

# Establecer directorio de trabajo
WORKDIR /app/

RUN mkdir -p /root/.m2
COPY settings.xml /root/.m2/settings.xml

COPY pom.xml /app/pom.xml

RUN mvn dependency:go-offline -B

COPY src ./src

# Construir el proyecto
RUN mvn package -Dmaven.test.skip=true

# Imagen base para ejecutar
FROM eclipse-temurin:17-jdk AS final

# Establecer directorio de trabajo
WORKDIR /app

# Copia el archivo resolv.conf personalizado
COPY custom-resolv.conf /etc/resolv.conf

RUN apt-get update && apt-get install -y iputils-ping curl && apt-get install -y nano \
    && apt-get install -y netcat-openbsd && apt-get install -y gettext && rm -rf /var/lib/apt/lists/*


COPY --from=builder /app/pom.xml /app/pom.xml

# Copiar el archivo JAR construido
COPY --from=builder /app/target/*jar-with-dependencies.jar app.jar

COPY --from=builder /app/src /app/src


# Copiar el script wait-for-it.sh (debes asegurarte de que esté en tu directorio de trabajo)
COPY scripts/wait-for-it.sh /app/wait-for-it.sh
COPY scripts/start.sh /app/start.sh
RUN chmod +x /app/wait-for-it.sh /app/start.sh

# Exponer el puerto de la aplicación
EXPOSE 9092

# Comando para ejecutar la aplicación
ENTRYPOINT ["/app/start.sh"]

