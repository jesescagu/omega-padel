FROM fedora:36

ENV URL_DOWNLOAD_JDK 
ENV URL_DOWNLOAD_TOMCAT 

# Variables relacionadas con la aplicación
ENV URL_APP_ZIP https://github.com/jesescagu/omega-padel/archive/refs/heads/main.zip
ENV NAME_APP_ZIP omega-padel

# set JAVA_HOME
# ENV JAVA_HOME /usr/lib/jvm/java-openjdk

# Variables de entorno de MySQL, por defecto los valores propios de nuestra aplicación
ENV MYSQL_ROOT_PASSWORD secret
ENV MYSQL_USER user
ENV MYSQL_PASSWORD omegapadel
ENV MYSQL_DATABASE omegapadel

# Comandos
RUN dnf -y update
RUN dnf -y install vim
RUN dnf -y install java-1.8.0-openjdk-devel.x86_64
RUN dnf -y install tomcat
RUN dnf -y install wget
RUN dnf -y install unzip
RUN dnf clean all

# Puertos por defecto
EXPOSE 8080
EXPOSE 3306


ENTRYPOINT wget $URL_APP_ZIP -O "$NAME_APP_ZIP.zip" && unzip -o $NAME_APP_ZIP.zip -d $NAME_APP_ZIP && rm -f $NAME_APP_ZIP.zip && mv $NAME_APP_ZIP/* /var/lib/tomcat/webapps/ && /sbin/init







 #Añadir jdk y tomcat al contenedor
ADD jdk-8u181-linux-x64.tar.gz /usr/local
ADD apache-tomcat-9.0.19.tar.gz /usr/local
 #Instalar vim editor
RUN yum -y install vim
 # Establezca su punto de apoyo después de iniciar sesión
ENV MYPATH /usr/local
WORKDIR $MYPATH
 #Configurar variables de entorno jdk y tomcat
ENV JAVA_HOME /usr/local/jdk1.8.0_181
ENV CLASSPATH $JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar
ENV CATALINA_HOME /usr/local/apache-tomcat-9.0.19
ENV CATALINE_BASE /usr/local/apache-tomcat-9.0.19
ENV PATH $PATH:$JAVA_HOME/bin:$CATALINA_HOME/lib:$CATALINA_HOME/bin
 # El puerto en el que escucha el contenedor
EXPOSE 8080
 #Run Tomcat al inicio
#ENTRYPOINT ["/usr/local/apache-tomcat-9.0.19/bin/startup.sh"]
#CMD ["/usr/local/apache-tomcat-9.0.19/bin/startup.sh","run"]
CMD /usr/local/apache-tomcat-9.0.19/bin/startup.sh && tail -F /usr/local/apache-tomcat-9.0.19/bin/logs/catalina.o







# docker build -t jesescagu/omegapadel:v1 .