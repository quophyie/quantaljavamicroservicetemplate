FROM maven:3.5.0-jdk-8

ADD target/quantalmicroservicetemplate-0.0.1-SNAPSHOT.jar ~/
ADD docker/wait-for-it.sh /wait-for-it.sh
RUN chmod 777 /wait-for-it.sh
CMD ["java", "-jar", "~/quantalmicroservicetemplate-0.0.1-SNAPSHOT.jar"]