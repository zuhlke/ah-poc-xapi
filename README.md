# ah-poc-xapi

[![Build Status](https://travis-ci.com/zuhlke/ah-poc-xapi.svg?branch=master)](https://travis-ci.com/zuhlke/ah-poc-xapi)

This is an API which consumes the ah-poc PAPI. It has a travis pipeline which deploys it into the ah PCF org. It's requests to the PAPI are
done using WebFlux.

## Example

If you hit this API with a request like

`GET localhost:8080/sum-balances?customer-id=10101010`

then you'll get a response body like

```
{"accountNumber": "10101010", "sumBalance": "1269.0"}
```

## Pact

There is a pact test, `ReactivePapiServicePact`. The pact guidelines suggest writing your pact test as a test against your interface for
interacting with the provider. Therefore this test is effectively a component test against the `ReactivePapiService` and
`WebfluxRestClient`.

- Perhaps it should just be a unit test against the `WebfluxRestClient`.

When you run the pact test, a pact file is created in `target/pacts/` with the naming convention `<consumer_name>-<provider_name>.json`.
The names of the consumer and provider are given in the test as overriden functions against the pact testing framework's API. I have
added comments in the test class itself.

#### Next steps

1. Verify the contract in the consumer pipeline
    - I've tried to use this on the PAPI https://github.com/DiUS/pact-jvm/tree/master/provider/pact-jvm-provider-maven but with no luck so far. 
2. Run a pact broker on PCF
    - Either on docker or ruby
3. Upload generated pacts to the pact broker
    - Iris' example uses the gradle plugin (https://github.com/zuhlke/pact-consumer/blob/master/build.gradle#L26)
4. Run pacts from the pact broker within the provider's pipeline

