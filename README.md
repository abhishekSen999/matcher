Matcher
=======

Matcher is a utility tool used to match List of objects of 2 different classes based on Specified set of Field accessors provided in form of MatcherSpecifications.<br/>
Currently it uses a ProxyBasedMatcher to achieve this, where the equals and hashCode functions are overridden dynamically based on provided specifications.

Example
-------
Lets say there are 2 banks BANK_A & BANK_B ,  they underwent a merger now their customer bases needs to be merged.</br>

Lets say BANK_A maintained it's customers in below mentioned format

````
@Builder
@ToString
static class CustomerOfBankA {
    private String idInA;
    private String name;
    private String govIssuedIdNo;

    public String getIdInA() {
        return idInA;
    }

    public String getName() {
        return name;
    }

    public String getGovIssuedIdNo() {
        return govIssuedIdNo;
    }
}
````
And BANK_B maintained it like this:

````
@Builder
@ToString
static class CustomerOfBankB {

    private String idInB;
    private String customerName;
    private String govID;

    public String getIdInB() {
        return idInB;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getGovID() {
        return govID;
    }

}
````
For matching Classes CustomerOfBankA with CustomerOfBankB in O(n) time we would need someway to hash them , but we cannot override the actual equals(..) and hashCode() function as it would mess with other processing.<br/>
So this is where this matcher library comes in.</br>
<p/>

### Steps to Match
1. First we will need a MatcherSpecification ,  specifying which fields to match with it's counterpart.

    We are trying to match

| CustomerOfBankA   |CustomerOfBankB |
| ------------------ | ---------------- |
| name | customerName  |
| govIssuedIdNo | govID  |

````
var specifications = new MatcherSpecifications<CustomerOfBankA, CustomerOfBankB>() {
            @Override
            public Class<CustomerOfBankA> getClassOfLeftType() {
                return CustomerOfBankA.class;
            }

            @Override
            public String[] getFieldAccessorsForLeft() {
                return new String[]{"getName", "getGovIssuedIdNo"};
            }

            @Override
            public Class<CustomerOfBankB> getClassOfRightType() {
                return CustomerOfBankB.class;
            }

            @Override
            public String[] getFieldAccessorsForRight() {
                return new String[]{"getCustomerName", "getGovID"};
            }
        };
````
Take note of the order in which the field accessors are provided for each type.</br>
Here LeftType is CustomerOfBankA  and RightType is CustomerOfBankB</br>

<p/>
2.  Then we would need a just Matcher

````
Matcher<CustomerOfBankA, CustomerOfBankB> matcher =  new ProxyBasedMatcher<CustomerOfBankA, CustomerOfBankB>();
````

3. The Final Step would be to match 

````
Collection<Matched<CustomerOfBankA, CustomerOfBankB>> matchedObjs = matcher.match(bankACustomers, bankBCustomers, specifications);
````

As we can see it returns a Collection of Matched objects.</br>
Matched holds a list of LeftType objects (here CustomerOfBankA) and a list RightType objects ( here CustomerOfBankB). This allows many to many matching .


























