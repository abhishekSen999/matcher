package com.sen.matcher;

import codes.sen.matcher.AnnotationBasedMatcher;
import codes.sen.matcher.Matched;
import codes.sen.matcher.annotation.MatchingKey;
import lombok.Builder;
import lombok.ToString;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class TestProxyBasedMatcherWithAnnotationSupportOneToMany {

    public static final String PAIR_A_B = "A-B";
    public static final String PAIR_A_C = "A-C";

    private List<CustomerOfCompanyA> getCompanyACustomers(){
        return Arrays.asList(
                CustomerOfCompanyA.builder().idInA("COMPANY_A_1").name("Customer_ABC1").govIssuedIdNo("INDIA_01").build(),
                CustomerOfCompanyA.builder().idInA("COMPANY_A_2").name("Customer_ABC2").govIssuedIdNo("INDIA_02").build(),
                CustomerOfCompanyA.builder().idInA("COMPANY_A_3").name("Customer_ABC3").govIssuedIdNo("INDIA_03").build(),
                CustomerOfCompanyA.builder().idInA("COMPANY_A_4").name("Customer_ABC4").govIssuedIdNo("INDIA_04").build());
    }

    private List<CustomerOfCompanyB> getCompanyBCustomers(){
        return Arrays.asList(
                CustomerOfCompanyB.builder().idInB("COMPANY_B_1").customerName("Customer_ABC1").govID("INDIA_01").build(),
                CustomerOfCompanyB.builder().idInB("COMPANY_B_2").customerName("Customer_ABC2").govID("INDIA_02").build(),
                CustomerOfCompanyB.builder().idInB("COMPANY_B_3").customerName("Customer_ABC3").govID("INDIA_03").build(),
                CustomerOfCompanyB.builder().idInB("COMPANY_B_4").customerName("Customer_ABC4").govID("INDIA_04").build(),
                CustomerOfCompanyB.builder().idInB("COMPANY_B_5").customerName("Customer_ABC5").govID("INDIA_05").build());
    }

    private List<CustomerOfCompanyC> getCompanyCCustomers(){
        return Arrays.asList(
                CustomerOfCompanyC.builder().idInC("COMPANY_C_1").fullName("Customer_ABC1").govIssuedId("INDIA_01").build(),
                CustomerOfCompanyC.builder().idInC("COMPANY_C_2").fullName("Customer_ABC2").govIssuedId("INDIA_02").build(),
                CustomerOfCompanyC.builder().idInC("COMPANY_C_3").fullName("Customer_ABC3").govIssuedId("INDIA_03").build(),
                CustomerOfCompanyC.builder().idInC("COMPANY_C_4").fullName("Customer_ABC4").govIssuedId("INDIA_04").build(),
                CustomerOfCompanyC.builder().idInC("COMPANY_C_5").fullName("Customer_ABC5").govIssuedId("INDIA_05").build());

    }

    @Test
    public void testMatcherForA_B() {

        // checking matching of A
        AnnotationBasedMatcher<CustomerOfCompanyA, CustomerOfCompanyB> matcher =  AnnotationBasedMatcher.newInstance(CustomerOfCompanyA.class , CustomerOfCompanyB.class);

        Collection<Matched<CustomerOfCompanyA, CustomerOfCompanyB>> matchedObjs = matcher.match(getCompanyACustomers(), getCompanyBCustomers(), PAIR_A_B);

        for (Matched<CustomerOfCompanyA,CustomerOfCompanyB> matched: matchedObjs ) {
            List<CustomerOfCompanyA> customersOfCompanyA = matched.getLeftObjects();
            List<CustomerOfCompanyB> customersOfCompanyB = matched.getRightObjects();

            for (CustomerOfCompanyA customerOfCompanyA: customersOfCompanyA) {
                for (CustomerOfCompanyB customerOfCompanyB: customersOfCompanyB ) {

                    Assertions.assertEquals(customerOfCompanyA.getName() , customerOfCompanyB.getCustomerName());
                    Assertions.assertEquals(customerOfCompanyA.getGovIssuedIdNo() , customerOfCompanyB.getGovID());

                }
            }

        }

    }

    @Test
    public void testMatcherForA_C() {

        // checking matching of A
        AnnotationBasedMatcher<CustomerOfCompanyA, CustomerOfCompanyC> matcher =  AnnotationBasedMatcher.newInstance(CustomerOfCompanyA.class , CustomerOfCompanyC.class);

        Collection<Matched<CustomerOfCompanyA, CustomerOfCompanyC>> matchedObjs = matcher.match(getCompanyACustomers(), getCompanyCCustomers(), PAIR_A_C);

        for (Matched<CustomerOfCompanyA,CustomerOfCompanyC> matched: matchedObjs ) {
            List<CustomerOfCompanyA> customersOfCompanyA = matched.getLeftObjects();
            List<CustomerOfCompanyC> customersOfCompanyC = matched.getRightObjects();

            for (CustomerOfCompanyA customerOfCompanyA: customersOfCompanyA) {
                for (CustomerOfCompanyC customerOfCompanyC: customersOfCompanyC ) {

                    Assertions.assertEquals(customerOfCompanyA.getName() , customerOfCompanyC.getFullName());
                    Assertions.assertEquals(customerOfCompanyA.getGovIssuedIdNo() , customerOfCompanyC.getGovIssuedId());

                }
            }

        }

    }



    @Builder
    @ToString
    static class CustomerOfCompanyA {
        private String idInA;
        @MatchingKey(order = 0,pairId = PAIR_A_B)
        @MatchingKey(order = 1,pairId = PAIR_A_C)
        private String name;
        @MatchingKey(order = 1,pairId = PAIR_A_B)
        @MatchingKey(order = 0,pairId = PAIR_A_C)
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

    @Builder
    @ToString
    static class CustomerOfCompanyB {

        private String idInB;
        @MatchingKey(order = 0,pairId = PAIR_A_B)
        private String customerName;
        @MatchingKey(order = 1,pairId = PAIR_A_B)
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

    @Builder
    @ToString
    static class CustomerOfCompanyC {
        private String idInC;
        @MatchingKey(order = 0,pairId = PAIR_A_C)
        private String govIssuedId;
        @MatchingKey(order = 1,pairId = PAIR_A_C)
        private String fullName;

        public String getIdInC() {
            return idInC;
        }

        public String getFullName() {
            return fullName;
        }

        public String getGovIssuedId() {
            return govIssuedId;
        }
    }


}
