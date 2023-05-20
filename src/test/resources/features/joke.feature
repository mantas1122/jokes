Feature: Joke Service

  Scenario: Get a random joke
    Given a joke service is available
    When the client requests a random joke
    Then the service should return a joke

  Scenario: Joke service unavailable (rate limit exceeded)
    Given the joke service is not available
    When the client requests a random joke
    Then the service should return a 'rate limit exceeded' error

  Scenario: Joke service returns an unsuccessful response
    Given the joke service is returning an unsuccessful response
    When the client requests a random joke
    Then the service should return a 'Joke API returned unsuccessful response' error

#  Scenario: Joke service not valid subscription response
#    Given the joke service is returning not valid subscription response
#    When the client requests a random joke
#    Then the service should return a 'not valid subscription response' error

# TODO cover all endpoints