# 1. Base Image 설정: Tomcat 9과 Java 8을 사용하는 이미지를 사용
FROM tomcat:9-jdk8-openjdk

# 2. 메타데이터 설정
LABEL maintainer="gas_management"
LABEL description="Gas Management Server - Tomcat 9 with JDK 8"

# 3. 환경 변수 설정
ENV CATALINA_HOME=/usr/local/tomcat
ENV PATH=$CATALINA_HOME/bin:$PATH
ENV TZ=Asia/Seoul

# 4. 타임존 설정
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# 5. WAR 파일을 Tomcat 웹앱 디렉터리에 복사
# Maven으로 빌드된 kkkkk_m.war 파일을 kkkkk_m_war.war로 복사하여 컨텍스트 경로를 /kkkkk_m_war로 설정
COPY target/kkkkk_m.war $CATALINA_HOME/webapps/kkkkk_m_war.war

# 6. Tomcat 설정 파일 복사 (필요한 경우)
# COPY conf/server.xml $CATALINA_HOME/conf/server.xml

# 7. 포트 8080을 개방 (Tomcat 기본 포트)
EXPOSE 8080

# 8. 헬스체크 추가 (wget 사용, curl이 없을 수 있음)
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
  CMD wget --quiet --tries=1 --spider http://localhost:8080/kkkkk_m_war/gasapp/home.jsp || exit 1

# 9. Tomcat 시작 명령어
CMD ["catalina.sh", "run"]
