ARG TAG
FROM org.geoserver.cloud/gs-cloud-docker-base-java:${TAG}

ARG JAR_FILE=dummy

ADD target/${JAR_FILE} /opt/app/discovery-service.jar

CMD exec java $JAVA_OPTS -jar /opt/app/discovery-service.jar
