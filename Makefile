.EXPORT_ALL_VARIABLES:
.PHONY: dev dev-css build run clean deploy image

SHELL = bash

include .env
export


# npm config set registry https://registry.npmmirror.com
# npm config set registry https://registry.npmjs.org.

# npm install

dev:
	(set -a && source .env && bb run dev)

dev-css:
#	npm i
	npx tailwindcss -i ./tailwind/style.css -o ./public/assets/style.css --watch

# run:
# 	bb run main

build-map:
	npm run build

build-tailwind:
	npx tailwindcss -i ./tailwind/style.css -o ./public/assets/style.css --minify

build: build-map build-tailwind

image: build
	docker build -t meteo/meteo38 .

run-image:
	docker run -p 8038:8038 \
	  -e BASIC_AUTH=${BASIC_AUTH} \
	  -e HTTP_SERVER_HOST=0.0.0.0 \
	  -e HTTP_SERVER_PORT=8038 \
	  --rm meteo/meteo38

deploy:
	@echo "deploy"
	rsync -avz src public bb.edn run.sh ${PROD_HOST}:${PROD_PATH}

outdated:
	@(clojure -Sdeps '{:deps {antq/antq {:mvn/version "RELEASE"}}}' -T antq.core/-main || exit 0)

#.
