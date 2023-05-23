## Joke app


### Run tests:
````./gradlew test````


### Run App
Set your API key
````
dad-jokes-client:
  key: <your key>
````
Run

```./gradlew bootRun```


### Swagger documentation
http://localhost:8080/swagger-ui/index.html


## Build Docker image
```docker build -t joke-service .```

## Run Docker container
```docker run -p 8080:8080 joke-service```


## Run OWASP check on dependencies
```./gradlew dependencyCheckAnalyze```