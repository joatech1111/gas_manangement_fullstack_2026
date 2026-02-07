# 1. Base Image 설정: Tomcat 9과 Java 8을 사용하는 이미지를 사용
FROM tomcat:9-jdk8-openjdk

# 2. WAR 파일을 Tomcat 웹앱 디렉터리에 복사
# 현재 디렉토리에서 kkkkk.war 파일을 Tomcat의 webapps/ROOT.war로 복사
COPY ./kkkkk.war /usr/local/tomcat/webapps/ROOT.war

# 3. 포트 8080을 개방 (Tomcat 기본 포트)
EXPOSE 8080

# 4. Tomcat 시작 명령어 (이미지 기본으로 포함되어 있음)
CMD ["catalina.sh", "run"]
