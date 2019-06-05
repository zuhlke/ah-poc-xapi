# ah-poc-xapi

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
`ReactiveRestClient`.

- Perhaps it should just be a unit test against the `ReactiveRestClient`.

When you run the pact test, a pact file is created in `target/pacts/` with the naming convention `<consumer_name>-<provider_name>.json`.
The names of the consumer and provider are given in the test as overriden functions against the pact testing framework's API. I have
added comments in the test class itself.

#### Next steps

1. Verify the contract in the consumer pipeline
2. Run a pact broker on PCF
3. Upload generated pacts to the pact broker
4. Run pacts from the pact broker within the provider's pipeline