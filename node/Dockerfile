FROM node:current-alpine3.13

WORKDIR /app
COPY ./app /app
RUN apk update && \
    npm install -g npm
EXPOSE 9000