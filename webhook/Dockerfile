FROM openjdk:13.0.1
ARG JAR_FILE
COPY ${JAR_FILE} /usr/bot-demo/

WORKDIR /usr/bot-demo/

ENV JAVA_OPTS="$JAVA_OPTS tock_bot_api_demo=false"

ENTRYPOINT ["sh", "-c"]

CMD ["java -cp '/usr/bot-demo/*' $JAVA_OPTS $JAVA_ARGS ai.tock.demo.websocket.StartWebhookKt"]
