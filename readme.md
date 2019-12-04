Run `sbt test` and notice that the test fails because context isn't being propagated after the http client fetch

Uncomment the future's `.map` call in `IOContextPropagation` and somehow context propagation magically works again (test passing)

This issue exist for both Kamon 1.x and 2.x