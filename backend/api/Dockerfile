FROM ghcr.io/graalvm/native-image-community:21

ENV DEBIAN_FRONTEND=noninteractive

RUN microdnf install findutils

ENV JAVA_HOME=/usr/lib64/graalvm/graalvm-community-java21
ENV PATH="$JAVA_HOME/bin:$PATH"

WORKDIR /usr/local/app

COPY . .
RUN ./gradlew nativeCompile


