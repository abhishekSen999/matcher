Matcher
=======

Matcher is a utility tool used to match List of objects of 2 different classes based on Specified list of Field accessors provided in form of MatcherSpecifications.<br/>
Currently it uses a ProxyBasedMatcher to achieve this, where the equals and hashCode functions are overridden dynamically based on provided specifications.

Dependency
----------

````
<dependency>
   <groupId>codes.sen</groupId>
   <artifactId>matcher</artifactId>
</dependency>
````

Example
-------
Lets say there are 2 companies COMPANY_A & COMPANY_B ,  they underwent a merger now their customer bases needs to be merged.</br>

Lets say COMPANY_A maintained it's customers in below mentioned format

````
@Builder
@ToString
static class CustomerOfCompanyA {
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
And COMPANY_B maintained it like this:

````
@Builder
@ToString
static class CustomerOfCompanyB {

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
For matching Classes CustomerOfCompanyA with CustomerOfCompanyB in O(n) time we would need someway to hash them , but we cannot override the actual equals(..) and hashCode() function as it would mess with other processing.<br/>
So this is where this matcher library comes in.</br>
<p/>

### Steps to Match
1. First we will need a MatcherSpecification ,  specifying which fields to match with it's counterpart.

    We are trying to match

| CustomerOfCompanyA   |CustomerOfCompanyB |
| ------------------ | ---------------- |
| name | customerName  |
| govIssuedIdNo | govID  |

````
var specifications = new MatcherSpecifications<CustomerOfCompanyA, CustomerOfCompanyB>() {
            @Override
            public Class<CustomerOfCompanyA> getClassOfLeftType() {
                return CustomerOfCompanyA.class;
            }

            @Override
            public String[] getFieldAccessorsForLeft() {
                return new String[]{"getName", "getGovIssuedIdNo"};
            }

            @Override
            public Class<CustomerOfCompanyB> getClassOfRightType() {
                return CustomerOfCompanyB.class;
            }

            @Override
            public String[] getFieldAccessorsForRight() {
                return new String[]{"getCustomerName", "getGovID"};
            }
        };
````
Take note of the order in which the field accessors are provided for each type.</br>
Here LeftType is CustomerOfCompanyA  and RightType is CustomerOfCompanyB</br>

<p/>
2.  Then we would need a Matcher

````
Matcher<CustomerOfCompanyA, CustomerOfCompanyB> matcher =  Matcher.newInstance(CustomerOfCompanyA.class , CustomerOfCompanyB.class);
````

3. The Final Step would be to match 

````
Collection<Matched<CustomerOfCompanyA, CustomerOfCompanyB>> matchedObjs = matcher.match(companyACustomers, companyBCustomers, specifications);
````

As we can see it returns a Collection of Matched objects.</br>
Matched holds a list of LeftType objects (here CustomerOfCompanyA) and a list of RightType objects ( here CustomerOfCompanyB). This allows many to many matching .


























