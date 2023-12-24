package com.tong.datamaker;

import com.github.javafaker.Faker;
import java.util.Locale;

public class HttpDataMaker {

    public static String generateFakeDataRow() {
        Faker faker = new Faker(new Locale("zh_CN")); // You can specify the locale as needed

        return "1225" + "|" +
                "371" + "|" +
                UtilsDataMaker.getRandomString("0371", "0379") + "|" +
                UtilsDataMaker.getRandomString("371", "898", "200", "250", "351", "531", "889", "") + "|" +
                UtilsDataMaker.getRandomString("0371", "0898", "0200", "0250", "0351", "0531", "0889", "") + "|" +
                faker.number().numberBetween(2, 8) + "|" +
                "11" + "|" +
                faker.bothify("?#???#" + 6701 + "###??#??##" + "9f17" + "###?#??#") + "|" +
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
                "103" + "|" +
                UtilsDataMaker.generateTimestampMillisecond("20231219103455234","20231219173455611") + "|" +
                UtilsDataMaker.generateTimestampMillisecond("20231219103455234","20231219173455611") + "|" +
                "|" +
                "|" +
                "|" +
                "|" +
                "1" + "|" +
                String.format("%02d", faker.number().numberBetween(1, 9)) + "|" +
                String.format("%05d", faker.number().numberBetween(1, 99)) + "|" +
                String.format("%03d", faker.number().numberBetween(0, 10)) + "|" +
                faker.number().numberBetween(1, 9) + "|" +
                faker.number().numberBetween(1, 9) + "|" +
                faker.internet().ipV4Address() + "|" +
                faker.internet().ipV6Address() + "|" +
                faker.regexify("[0-9]{5}") + "|" +
                "0" + "|" +
                faker.internet().ipV4Address() + "|" +
                faker.internet().ipV6Address() + "|" +
                UtilsDataMaker.getRandomString("80", "5432", "2181") + "|" +
                faker.number().numberBetween(200, 800) + "|" +
                faker.number().numberBetween(200, 800) + "|" +
                faker.number().numberBetween(10, 80) + "|" +
                faker.number().numberBetween(10, 80) + "|" +
                faker.number().numberBetween(3000, 40000000) + "|" +
                "0" + "|" +
                "0" + "|" +
                "0" + "|" +
                "0" + "|" +
                "0" + "|" +
                faker.number().numberBetween(20000, 40000) + "|" +
                faker.number().numberBetween(20000, 40000) + "|" +
                "0" + "|" +
                "0" + "|" +
                faker.number().numberBetween(2000, 4000) + "|" +
                "0" + "|" +
                faker.number().numberBetween(50000, 70000) + "|" +
                faker.number().numberBetween(2000, 4000) + "|" +
                faker.number().numberBetween(1, 9) + "|" +
                "0" + "|" +
                faker.number().numberBetween(1, 9) + "|" +
                faker.number().numberBetween(1, 9) + "|" +
                faker.number().numberBetween(1, 9) + "|" +
                "0" + "|" +
                "0" + "|" +
                "42" + "|" +
                "0" + "|" +
                "42" + "|" +
                "0" + "|" +
                "1" + "|" +
                "0" + "|" +
                "0" + "|" +
                "0" + "|" +
                "|" +
                "|" +
                "0" + "|" +
                faker.number().numberBetween(1, 9) + "|" +
                faker.number().numberBetween(1, 9) + "|" +
                "-1" + "|" +
                faker.number().numberBetween(100, 999) + "|" +
                faker.number().numberBetween(100, 999) + "|" +
                faker.number().numberBetween(100, 999) + "|" +
                "24" + "|" +
                "www." + faker.internet().domainName() + ".com" + "|" +
                "46" + "|" +
                "https://" + faker.internet().domainName() + ".com/" + faker.regexify("[a-f0-9]{8}") + "|" +
                "0" + "|" +
                "|" +
                "21" + "|" +
                UtilsDataMaker.getRandomString("okhttp/3.8.1", "MicroMessengerClient", "aegon-ios/3.42.0", "Dalvik/2.1.0(Linux;U;Android10;RMX2200Build/QP1A.190711.020)", "androidMozilla/5.0(Linux;Android8.0.0;LND-AL30Build/HONORLND-AL30;wv)AppleWebKit/537.36(KHTML,likeGecko)Version/4.0Chrome/70.0.3538.110Mobile", "xnet/1.0.0", "Apache-HttpClient/UNAVAILABLE(java1.4)", "fun-p2psdk-0.1") + "|" +
                UtilsDataMaker.getRandomString("video/mp4", "application/octet-stream", "text/html", "application/json;charset=utf-8", "video/x-flv", "") + "|" +
                "0" + "|" +
                "|" +
                "0" + "|" +
                "|" +
                faker.number().numberBetween(100, 99999) + "|" +
                "|" +
                faker.number().numberBetween(0, 5) + "|" +
                faker.number().numberBetween(0, 5) + "|" +
                faker.number().numberBetween(100, 99999) + "|" +
                "0" + "|" +
                UtilsDataMaker.getRandomString("0", "1") + "|" +
                "0" + "|" +
                "|" +
                UtilsDataMaker.getRandomString("0", "1") + "|" +
                "|" +
                "|" +
                "|" +
                faker.number().numberBetween(0, 255) + "|" +
                faker.number().numberBetween(0, 255) + "|" +
                "|" +
                "\n";
    }

}
