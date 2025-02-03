package com.gold_hunter.gold_hunter.services;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class IssuingChecks {

    private String token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ7XCJhdXRoVHlwZVwiOlwiRVNJQVwiLFwiZmlkXCI6MTAwMTUyOTY1MTc4LFwibG9naW5cIjpcIjExNTY0NDkwMDRcIixcImlkXCI6NTY4ODM4NixcImRldmljZUlkXCI6XCJtQlZsWUZYRHV5SlJ2M19EREVGdG9cIixcIm9wZXJhdG9ySWRcIjpudWxsLFwiY3N1ZFVzZXJuYW1lXCI6bnVsbH0iLCJleHAiOjE2MzQ5ODY0MDJ9.M5hfTB9D0zWFJ4bgknwlf7_W5_J2Xm29h64_2ewSPc9_55qSiOYQD90UDj8FIvl60t-2gUXOm3YUY9hvvAOodA";

    private String postRequest(String method, String jsonString) {
        final CloseableHttpClient httpClient = HttpClients.createDefault();
        final HttpPost httpPost = new HttpPost("https://lknpd.nalog.ru/api/v1/" + method);
        String response = "";

        httpPost.setHeader("Authorization", "Bearer " + token);

        HttpEntity stringEntity = new StringEntity(jsonString, ContentType.APPLICATION_JSON);
        httpPost.setEntity(stringEntity);

        try (CloseableHttpResponse httpResponse = httpClient.execute(httpPost)) {
            final HttpEntity entity = httpResponse.getEntity();

            response = EntityUtils.toString(entity);
            httpClient.close();
        } catch (IOException ignored) { }

        return response;
    }

    public void getToken() {
        String deviceId = "mBVlYFXDuyJRv3_DDEFto";
        String refreshToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ7XCJ0eXBlXCI6XCJSRUZSRVNIX1RPS0VOXCIsXCJyZWZyZXNoQ29udGV4dFwiOntcImF1dGhUeXBlXCI6XCJFU0lBXCIsXCJmaWRcIjoxMDAxNTI5NjUxNzgsXCJsb2dpblwiOlwiMTE1NjQ0OTAwNFwiLFwiaWRcIjo1Njg4Mzg2LFwiZGV2aWNlSWRcIjpcIm1CVmxZRlhEdXlKUnYzX0RERUZ0b1wiLFwib3BlcmF0b3JJZFwiOm51bGwsXCJjc3VkVXNlcm5hbWVcIjpudWxsfSxcImV4cGlyYXRpb25cIjpudWxsfSJ9.zVSH2N4fHoKishMfJUOJzVLJSEJpRc_qVouMTVT5wS7HL4JMCeeJdBJv0PeXxqk902aADzBj0UunTj5IRbooIw";
        String jsonString = "{\"deviceInfo\":{\"appVersion\":\"3.3.1\",\"metaDetails\":{\"browser\":\"\",\"browserVersion\":\"\",\"os\":\"android\"},\"sourceDeviceId\":\"" + deviceId + "\",\"sourceType\":\"android\"},\"refreshToken\":\"" + refreshToken + "\"}";

        String[] response = postRequest("auth/token", jsonString).split("\"", 11);

        token = response[9];
    }

    public String newTransaction(String service, float amount) {
        Date current = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SXXX");
        String dateTime = formatter.format(current);
        String jsonString = "{\"ignoreMaxTotalIncomeRestriction\":false,\"operationTime\":\"" + dateTime + "\",\"paymentType\":\"CASH\",\"requestTime\":\"" + dateTime + "\",\"services\":[{\"amount\":\"" + amount + "\",\"name\":\"" + service + "\",\"quantity\":1}],\"totalAmount\":\"" + amount + "\"}";

        String[] response = postRequest("income", jsonString).split("\"", 5);

        return response[3];
    }
}
