FROM openjdk:13.0.1
ARG JAR_FILE
COPY ${JAR_FILE} /usr/bot-demo/

WORKDIR /usr/bot-demo/

ENTRYPOINT ["sh", "-c"]

CMD ["java -cp '/usr/bot-demo/*' ai.tock.demo.webhook.StartWebhookKt"]
