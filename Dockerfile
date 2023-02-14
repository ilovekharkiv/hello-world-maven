FROM tomcat:8.0-alpine
LABEL maintainer="abolmasovp@gmail.com"

ADD /home/ubuntu/workspace/final_project_epam_master/target/epam.war /usr/local/tomcat/webapps/

EXPOSE 8080
CMD ["catalina.sh", "run"]
