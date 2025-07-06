FROM alpine:3.22

RUN apk add --no-cache curl bash ca-certificates

WORKDIR /app

RUN curl -sL https://github.com/babashka/babashka/releases/download/v1.12.204/babashka-1.12.204-linux-amd64-static.tar.gz \
    | tar -xz && chmod +x ./bb

COPY src    /app/src
COPY public /app/public
COPY bb.edn .

CMD ["./bb", "run", "main"]
