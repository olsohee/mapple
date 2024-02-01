#!/usr/bin/env bash

REPOSITORY=/home/ubuntu
PROJECT_NAME=mapple
CURRENT_PID=$(pgrep -f /home/ubuntu/mapple-0.0.1-SNAPSHOT.jar)

# S3에서 받아온 jar 파일을 /home/ubuntu 경로에 복사
echo "> Build 파일 복사"
cd $REPOSITORY
cp $REPOSITORY/$PROJECT_NAME/build/libs/*.jar $REPOSITORY/

# 현재 구동 중인 애플리케이션 실행 종료
echo "> 현재 구동 중인 애플리케이션 pid: $CURRENT_PID"
if [ -z $CURRENT_PID ]
then
  echo "> 현재 구동 중인 애플리케이션 없음"
else
  echo "> kill -15 $CURRENT_PID"
  kill -15 $CURRENT_PID
  sleep 5
fi

JAR_NAME=$(ls -tr $REPOSITORY/ | grep jar | tail -n 1)
echo "> Jar Name: $JAR_NAME"

echo "> 새 애플리케이션 배포"
chmod +x $JAR_NAME
nohup java -jar $REPOSITORY/$JAR_NAME 1>/home/ubuntu/log/output.log 2>/home/ubuntu/log/error.log &
