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

public class TestProxyBasedMatcherWithAnnotationSupportOneToOne {


    @Test
    public void testMatcher() {

        List<CustomerOfCompanyA> companyACustomers = Arrays.asList(
                CustomerOfCompanyA.builder().idInA("COMPANY_A_1").name("Customer_ABC1").govIssuedIdNo("INDIA_01").build(),
                CustomerOfCompanyA.builder().idInA("COMPANY_A_2").name("Customer_ABC2").govIssuedIdNo("INDIA_02").build(),
                CustomerOfCompanyA.builder().idInA("COMPANY_A_3").name("Customer_ABC3").govIssuedIdNo("INDIA_03").build(),
                CustomerOfCompanyA.builder().idInA("COMPANY_A_4").name("Customer_ABC4").govIssuedIdNo("INDIA_04").build());

        List<CustomerOfCompanyB> companyBCustomers = Arrays.asList(
                CustomerOfCompanyB.builder().idInB("COMPANY_B_1").customerName("Customer_ABC1").govID("INDIA_01").build(),
                CustomerOfCompanyB.builder().idInB("COMPANY_B_2").customerName("Customer_ABC2").govID("INDIA_02").build(),
                CustomerOfCompanyB.builder().idInB("COMPANY_B_3").customerName("Customer_ABC3").govID("INDIA_03").build(),
                CustomerOfCompanyB.builder().idInB("COMPANY_B_4").customerName("Customer_ABC4").govID("INDIA_04").build(),
                CustomerOfCompanyB.builder().idInB("COMPANY_B_5").customerName("Customer_ABC5").govID("INDIA_05").build());


        AnnotationBasedMatcher<CustomerOfCompanyA, CustomerOfCompanyB> matcher =  AnnotationBasedMatcher.newInstance(CustomerOfCompanyA.class , CustomerOfCompanyB.class);

        Collection<Matched<CustomerOfCompanyA, CustomerOfCompanyB>> matchedObjs = matcher.match(companyACustomers, companyBCustomers);

        for (Matched matched: matchedObjs ) {
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

    @Builder
    @ToString
    static class CustomerOfCompanyA {
        private String idInA;
        @MatchingKey(order = 0)
        private String name;
        @MatchingKey(order = 1)
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
        @MatchingKey(order = 0)
        private String customerName;
        @MatchingKey(order = 1)
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


}
