# QA + DEVOPS

## INTRODUCCIÓN

El objetivo de esta formación es tener un modelo para la construcción de un entorno de Integración Continua con Jenkins y Docker en el que se implementarán estrategias de testing, de automatización de pruebas y se incorporará dentro de DevOps.

Se trata de poner en práctica una estrategia de calidad que englobe todo el ciclo de vida de software en el pipeline de Jenkins y teniendo como objetivo principal el mantener el entorno de la máquina limpio, no siendo necesario albergar en ella todas las instalaciones y/o versiones del software específico de cada proyecto que se administre.

Para conseguir este objetivo se utiliza la tecnología docker. Con ella se podrán repartir las distintas tareas en diferentes contenedores en lugar de máquinas físicas y/o virtuales consiguiendo un entorno mucho más ligero, más fácil de escalar y más sencillo de portar.

## CICLO DE CI - PIPELINE

Todos los pasos que a continuación se detallan, a excepción de la copia en local del proyecto en la que se utiliza Git, se ejecutarán en entornos aislados, en contenedores utilizando Docker.

- Checkout del proyecto.
- Compilación y archivado.
- Ejecución Tests unitarios.
- Análisis estático de código contra Sonarqube.
- Levantar aplicación y bases de datos necesarias.
- Ejecución de Tests Cucumber + REST-Assured.
- Ejecución de Tests de integración con Postman.
- Detener la aplicación.

Para poder dockerizar todos estos pasos se crea una composición de contenedores, que dependen unos de otros y están vinculados entre sí en una red privada virtual.

## INSTALACIÓN

La guía de instalación y configuración de máquina se define en los siguientes puntos.

## COMPONENTES

- Una aplicación sobre la que se aplicará el ciclo de integración continúa.
- El Jenkins instalado en la máquina.  
- Un servidor SonarQube con docker.

A continuación se define cada uno.

## APLICACIÓN

La aplicación es un ejemplo con Spring Boot Rest API que usa Spring Data MongoDB y Maven de las operaciones CRUD de un sistema de gestión de estudiantes.

Contiene los test unitarios así como dos formas de pruebas funcionales o de integración: cucumber + Rest-Assured y Postman.

Para la construcción de la imagen se utiliza el archivo Dockerfile que contiene los comandos necesarios para ello. Dentro de los pasos del pipeline esta construcción se encuentra en la fase de 'Compilación y archivado'.

Para levantar y detener esta imagen así como las demás que serán necesarias para su funcionamiento (BBDD) y definir la red común en la que alojaremos todos los contenedores, se crea un archivo docker-compose. Fase: 'Levantar aplicación y bases de datos' y 'Detener la aplicación'.

También se definirá otro archivo docker-compose con la definición de las pruebas de integración en la que se levantará una imagen de newman en la red común con la que de forma totalmente aislada se pasarán los test funcionales. Fase: 'Tests de integración con Postman'.

Para todas las fases en las que se utiliza Maven se lanzarán también mediante docker, reutilizando su caché para las diferentes compilaciones que se ejecuten y sin necesidad de tener instalado un JDK ni Maven en la máquina. Fases: 'Compilación y archivado', 'Análisis estático de código contra Sonarqube', 'Ejecución Tests unitarios' y 'Ejecución de Tests Cucumber + REST-Assured'.

La aplicación se encuentra en la carpeta: demo-project.

## JENKINS

La instalación se hará sobre una máquina local, en este caso será Windows 10. Tendrá que tener instalado: la jdk, git, docker y docker-compose.
Para ello:

1. Para instalarlo se pueden seguir por ejemplo los pasos de esta WEB: <http://webipedia.es/tecnologia/cursos/jenkins-primeros-pasos/#:~:text=entorno%20de%20trabajo-,Instalaci%C3%B3n%20de%20Jenkins,que%20contendr%C3%A1%20un%20instalable%20%E2%80%9Cjenkins>.
2. Se instalarán los plugins necesarios para la visualización de los test unitarios, del SonarQube Scanner y de los Cucumber reports.
3. Se configurará como variable global la herramienta Git.
4. Se creará una nueva tarea de tipo pipeline en la que se configurará la definición de “Pipeline script” que está contenida en el repo de la aplicación en el archivo Jenkinsfile o se podrá también configurar como un “Pipeline script from SCM” que ejecutará directamente en archivo Jenkinsfile en el repo de la aplicación.
La primera opción se usa durante la configuración por ser más rápida la realización de pruebas. En cuanto esa configuración es definitiva se recomienda usar la segunda.

## SONARQUBE

Para verificar la calidad interna de la aplicación se realizará un análisis de código con esta herramienta. Para ello se levantará en un contenedor docker con las siguientes instrucciones:

`docker pull sonarqube:lts`

`docker run --name sonarqube -d -p 9000:9000 -p 9092:9092 -v sonarqube_home:/opt/sonarqube/data sonarqube:lts`

`docker start sonaqube`

Se puede acceder entrando en <http://localhost:9000> con el login: admin/admin  

## PORTAINER

Opcionalmente pero muy recomendado es tener levantado también con docker esta herramienta. Con ella se podrá administrar de forma sencilla tanto los contenedores como redes levantadas en nuestros equipos así como acceder mediante la consola a ellos.
La instrucción para levantarlo es:

`docker run -d -p 9001:9000 -v /var/run/docker.sock:/var/run/docker.sock -v portainer_data:/data portainer/portainer`

Se puede acceder entrando en <http://localhost:9001>
