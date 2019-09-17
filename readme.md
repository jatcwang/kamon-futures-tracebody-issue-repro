To reproduce the issue, switch between using ASYNCFILE and FILE in `src/main/resources/logback.xml` 
and observe that log/app.log has different output because with ASYNCFILE turned on, 
the span which called `logger.info(..)` isn't being propagated to the log event handler (`MyLayout`).
