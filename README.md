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