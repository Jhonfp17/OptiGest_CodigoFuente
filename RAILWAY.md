# Despliegue de OptiGest en Railway

El repositorio ya incluye un `Dockerfile` que genera `OptiGest.war` en cada
despliegue y lo ejecuta con Payara Micro (Jakarta EE 10).

## En Railway

1. Cree un servicio desde el repositorio de GitHub y deje el directorio raíz
   apuntando a la carpeta que contiene este archivo.
2. En **Variables** del servicio web, cree estas referencias a su servicio
   MySQL ya creado:

   - `MYSQLHOST=${{MySQL.MYSQLHOST}}`
   - `MYSQLPORT=${{MySQL.MYSQLPORT}}`
   - `MYSQLDATABASE=${{MySQL.MYSQLDATABASE}}`
   - `MYSQLUSER=${{MySQL.MYSQLUSER}}`
   - `MYSQLPASSWORD=${{MySQL.MYSQLPASSWORD}}`

   Reemplace `MySQL` por el nombre real de su servicio de base de datos si es
   distinto. No agregue estas claves a GitHub.
3. Genere un dominio público para el servicio web y despliegue.

Railway detectará el `Dockerfile`, asignará `PORT` y realizará la comprobación
de salud en `/`. Cada `push` a la rama conectada de GitHub construirá un WAR
nuevo y publicará los cambios automáticamente.
