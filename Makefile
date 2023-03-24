.EXPORT_ALL_VARIABLES:
.PHONY: dev dev-css build run clean version deploy

SHELL = bash

dev:
	bb run dev

run:
	bb run main

build:
	npx tailwindcss -i ./tailwind/style.css -o ./public/assets/style.css --minify

deploy:
	@echo "deploy"
	rsync -avz src public bb.edn run.sh ${PROD_HOST}:${PROD_PATH}

dev-css:
	npx tailwindcss -i ./tailwind/style.css -o ./public/assets/style.css --watch

#.
