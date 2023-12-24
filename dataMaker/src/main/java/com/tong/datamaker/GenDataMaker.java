package com.tong.datamaker;

import com.github.javafaker.Faker;
import java.util.Locale;

public class GenDataMaker {
    public static String generateFakeDataRow() {
        Faker faker = new Faker(new Locale("zh_CN")); // You can specify the locale as needed

        return "671" + "|" +
                "371" + "|" +
                UtilsDataMaker.getRandomString("0371", "0379") + "|" +
                UtilsDataMaker.getRandomString("371", "898", "200", "250", "351", "531", "889", "") + "|" +
                UtilsDataMaker.getRandomString("0371", "0898", "0200", "0250", "0351", "0531", "0889", "") + "|" +
                faker.number().numberBetween(2, 8) + "|" +
                "11" + "|" +
                faker.bothify("25000a6400" + "#?##?##?#??#??##??" + "633a") + "|" +
                "6" + "|" +
                faker.regexify("[A-F0-9]{32}") + "|" +
                "86" + faker.regexify("[0-9]{6}") + "#" + faker.regexify("[A-F0-9]{64}") + "|" +
                faker.phoneNumber().cellPhone() + "|" +
                "1" + "|" +
                faker.internet().ipV4Address() + "|" +
                faker.internet().ipV4Address() + "|" +
                faker.internet().ipV4Address() + "|" +
                "2152" + "|" +
                "2152" + "|" +
                "2152" + "|" +
                faker.regexify("[0-9]{10}") + "|" +
                faker.regexify("[0-9]{10}") + "|" +
                faker.regexify("[0-9]{5}") + "|" +
                faker.regexify("[0-9]{8}") + "|" +
                faker.internet().domainName() + "|" +
                "100" + "|" +
                UtilsDataMaker.generateTimestampMillisecond("20231219103455234", "20231219173455611") + "|" +
                UtilsDataMaker.generateTimestampMillisecond("20231219103455234", "20231219173455611") + "|" +
                "|" +
                "|" +
                "|" +
                "|" +
                "43" + "|" +
                "68" + "|" +
                "1" + "|" +
                "0" + "|" +
                "0" + "|" +
                "0" + "|" +
                faker.internet().ipV4Address() + "|" +
                faker.internet().ipV6Address() + "|" +
                faker.regexify("[0-9]{5}") + "|" +
                "0" + "|" +
                faker.internet().ipV4Address() + "|" +
                "|" +
                UtilsDataMaker.getRandomString("80", "5432", "2181") + "|" +
                faker.number().numberBetween(200, 800) + "|" +
                faker.number().numberBetween(200, 800) + "|" +
                faker.number().numberBetween(10, 80) + "|" +
                faker.number().numberBetween(10, 80) + "|" +
                faker.number().numberBetween(3000, 40000000) + "|" +
                faker.number().numberBetween(3000, 40000000) + "|" +
                "0" + "|" +
                "0" + "|" +
                faker.number().numberBetween(1, 8) + "|" +
                faker.number().numberBetween(1, 8) + "|" +
                faker.number().numberBetween(3000, 40000000) + "|" +
                faker.number().numberBetween(3000, 40000000) + "|" +
                "0" + "|" +
                "0" + "|" +
                "0" + "|" +
                "0" + "|" +
                "0" + "|" +
                "65535" + "|" +
                faker.number().numberBetween(0, 4) + "|" +
                faker.number().numberBetween(0, 4) + "|" +
                faker.number().numberBetween(0, 4) + "|" +
                faker.number().numberBetween(0, 4) + "|" +
                faker.number().numberBetween(0, 4) + "|" +
                faker.number().numberBetween(0, 4) + "|" +
                faker.number().numberBetween(0, 4) + "|" +
                faker.number().numberBetween(30, 70) + "|" +
                "0" + "|" +
                faker.number().numberBetween(30, 40) + "|" +
                "0" + "|" +
                "0" + "|" +
                "0" + "|" +
                "0" + "|" +
                "0" + "|" +
                "|" +
                "|" +
                "255" +
                "\n";
    }
}