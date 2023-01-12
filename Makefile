.EXPORT_ALL_VARIABLES:
.PHONY: dev build clean version

SHELL = bash

dev:
	bb run dev

run:
	bb run main
