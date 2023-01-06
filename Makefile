.EXPORT_ALL_VARIABLES:
.PHONY: dev build clean version

SHELL = bash

dev:
	bb nrepl

run:
	bb run main
