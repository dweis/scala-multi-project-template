FROM ghcr.io/graalvm/native-image-community:21 as binarybuilder

COPY target/scala-3.3.1/server.jar /build/
RUN native-image --static -H:+ReportExceptionStackTraces -H:+AddAllCharsets --allow-incomplete-classpath --no-fallback --initialize-at-build-time --enable-http --enable-https --enable-all-security-services --verbose -jar "/build/server.jar" /build/server

FROM scratch
COPY --from=binarybuilder /build/server /server
CMD ["/server"]
