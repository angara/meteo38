# Meteo38.ru - Clojure version using babashka

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
