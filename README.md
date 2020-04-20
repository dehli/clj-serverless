[![Clojars Project](https://img.shields.io/clojars/v/dehli/serverless.svg)](https://clojars.org/dehli/serverless)

# Serverless

AWS serverless helpers targeting Cljs.

## Commands

```
clj -A:build (--watch)   # Build code
clj -A:test              # Run tests
```

## Publishing version

```
mvn deploy
```

> Note: `settings.xml` must be set in ~/.m2/settings.xml

```
<settings>
  <servers>
    <server>
      <id>clojars</id>
      <username>username</username>
      <password>password</password>
    </server>
  </servers>
</settings>
```