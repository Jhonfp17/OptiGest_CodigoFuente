# El WAR se compila en NetBeans y se publica en la carpeta dist.
FROM payara/micro:6.2025.11-jdk17

COPY dist/OptiGest.war /opt/payara/deployments/ROOT.war

EXPOSE 8080

ENTRYPOINT ["/bin/sh", "-c"]
CMD ["exec java -jar /opt/payara/payara-micro.jar --port ${PORT:-8080} --deploy /opt/payara/deployments/ROOT.war:"]
