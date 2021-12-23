FROM mcr.microsoft.com/java/tomcat:11-zulu-ubuntu-tomcat-9
RUN rm -fr /usr/local/tomcat/webapps/ROOT
COPY target/scc-tp1-0.0.1-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war
EXPOSE 8080
