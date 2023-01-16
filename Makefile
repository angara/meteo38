.EXPORT_ALL_VARIABLES:
.PHONY: dev build run clean version

SHELL = bash

dev:
	bb run dev

run:
	bb run main


dev-css:
#	rsync -r ./assets/* ./dev/public/webapp/assets/
	npx tailwindcss -i ./tailwind/main.css -o ./public/assets/main.css --watch
.PHONY: dev-css
