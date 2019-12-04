Start a zipkin server in docker

`docker run --rm -d --name myzipkin -p 9411:9411 openzipkin/zipkin-slim:2.19.0`

run the app

`sbt run`

visit `http://localhost:9411/zipkin` and search for traces

Expected:
```
Span0
  - Span1
  - Span2
```

Actual:
![image](https://user-images.githubusercontent.com/4957161/68204360-b4488a00-ffbf-11e9-93bb-49b4e277ff4c.png)
