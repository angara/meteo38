# Meteo38.ru - Clojure version using babashka

## Website temperature label

```text
https://meteo38.ru/ext/t.js?st=STATION_ID
```

## Development

```sh
make dev-css
make dev
```

```clj
connect nrepl

(ns user) 
srv/run

; http://localhost:8038/
```

## Deploy

Install Babashka on target host

- <https://github.com/babashka/babashka/releases/>

```sh
# stop dev-css !
make build deploy
```

## Links

- [meteo38 dev](http://localhost:8038/)

- [Babashka book](https://book.babashka.org/)
- <https://github.com/babashka/nrepl-client>
