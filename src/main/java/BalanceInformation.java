import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "accountType",
        "accountNumber",
        "balance"
})
public class BalanceInformation {
    @JsonProperty("accountType")
    private String accountType;
    @JsonProperty("accountNumber")
    private String accountNumber;
    @JsonProperty("balance")
    private String balance;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<>();

    @JsonProperty("accountType")
    public String getAccountType() {
        return accountType;
    }

    @JsonProperty("accountType")
    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    @JsonProperty("accountNumber")
    public String getAccountNumber() {
        return accountNumber;
    }

    @JsonProperty("accountNumber")
    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    @JsonProperty("balance")
    public String getBalance() {
        return balance;
    }

    @JsonProperty("balance")
    public void setBalance(String balance) {
        this.balance = balance;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        additionalProperties.put(name, value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BalanceInformation that = (BalanceInformation) o;
        return Objects.equals(accountType, that.accountType) &&
                Objects.equals(accountNumber, that.accountNumber) &&
                Objects.equals(balance, that.balance) &&
                Objects.equals(additionalProperties, that.additionalProperties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountType, accountNumber, balance, additionalProperties);
    }

    public double balance() {
        return Double.parseDouble(balance);
    }
}
