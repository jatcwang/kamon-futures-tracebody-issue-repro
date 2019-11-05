Start a zipkin server in docker

`docker run --rm -d --name myzipkin -p 9411:9411 openzipkin/zipkin-slim:2.19.0`

run the app

`sbt run`

visit `http://localhost:9411/zipkin` and search for traces
