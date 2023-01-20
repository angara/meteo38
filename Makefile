.EXPORT_ALL_VARIABLES:
.PHONY: dev build run clean version

SHELL = bash

dev:
	bb run dev

run:
	bb run main

build:
	npx tailwindcss -i ./tailwind/style.css -o ./public/assets/style.css --minify

deploy:
	echo "!not yet raedy!"

dev-css:
#	rsync -r ./assets/* ./dev/public/webapp/assets/
	npx tailwindcss -i ./tailwind/style.css -o ./public/assets/style.css --watch
.PHONY: dev-css
