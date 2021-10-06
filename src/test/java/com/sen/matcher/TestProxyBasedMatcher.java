package com.sen.matcher;

import codes.sen.matcher.Matched;
import codes.sen.matcher.Matcher;
import codes.sen.matcher.MatcherSpecifications;
import codes.sen.matcher.ProxyBasedMatcher;
import lombok.Builder;
import lombok.ToString;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class TestProxyBasedMatcher {


    @Test
    public void testMatcher() {

        List<CustomerOfBankA> bankACustomers = Arrays.asList(
                CustomerOfBankA.builder().idInA("BANK_A_1").name("Customer_ABC1").govIssuedIdNo("INDIA_01").build(),
                CustomerOfBankA.builder().idInA("BANK_A_2").name("Customer_ABC2").govIssuedIdNo("INDIA_02").build(),
                CustomerOfBankA.builder().idInA("BANK_A_3").name("Customer_ABC3").govIssuedIdNo("INDIA_03").build(),
                CustomerOfBankA.builder().idInA("BANK_A_4").name("Customer_ABC4").govIssuedIdNo("INDIA_04").build());

        List<CustomerOfBankB> bankBCustomers = Arrays.asList(
                CustomerOfBankB.builder().idInB("BANK_B_1").customerName("Customer_ABC1").govID("INDIA_01").build(),
                CustomerOfBankB.builder().idInB("BANK_B_2").customerName("Customer_ABC2").govID("INDIA_02").build(),
                CustomerOfBankB.builder().idInB("BANK_B_3").customerName("Customer_ABC3").govID("INDIA_03").build(),
                CustomerOfBankB.builder().idInB("BANK_B_4").customerName("Customer_ABC4").govID("INDIA_04").build(),
                CustomerOfBankB.builder().idInB("BANK_B_5").customerName("Customer_ABC5").govID("INDIA_05").build());


        MatcherSpecifications<CustomerOfBankA, CustomerOfBankB> specifications = new MatcherSpecifications<CustomerOfBankA, CustomerOfBankB>() {
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


        Matcher<CustomerOfBankA, CustomerOfBankB> matcher =  new ProxyBasedMatcher<CustomerOfBankA, CustomerOfBankB>();

        Collection<Matched<CustomerOfBankA, CustomerOfBankB>> matchedObjs = matcher.match(bankACustomers, bankBCustomers, specifications);

        for (Matched matched: matchedObjs ) {
            List<CustomerOfBankA> customersOfBankA = matched.getLeftObjects();
            List<CustomerOfBankB> customersOfBankB = matched.getRightObjects();

            for (CustomerOfBankA customerOfBankA: customersOfBankA) {
                for (CustomerOfBankB customerOfBankB: customersOfBankB ) {

                    Assertions.assertEquals(customerOfBankA.getName() , customerOfBankB.getCustomerName());
                    Assertions.assertEquals(customerOfBankA.getGovIssuedIdNo() , customerOfBankB.getGovID());

                }
            }

        }

    }

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


}
