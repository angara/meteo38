# Meteo38.ru - Clojure/babashka version

## Website urls

- <https://meteo38.ru/> - main page
- <https://meteo38.ru/?st=uiii,npsd> - specify list of stations to show
- <https://meteo38.ru/ext/t.js?st=STATION_ID> - javascript snippet to embed into other webpages

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
; https://tailwind-elements.com/
```

## Deploy

Install Babashka on target host

- <https://github.com/babashka/babashka/releases/>

```sh
# stop dev-css !
make build deploy
```

## Links

Babashka:

- [Babashka book](https://book.babashka.org/)
- [Toolbox](https://babashka.org/toolbox/)

Icons:

- <https://www.iconfinder.com/search>
- <https://www.htmlsymbols.xyz/>
- <https://icons.getbootstrap.com/>
