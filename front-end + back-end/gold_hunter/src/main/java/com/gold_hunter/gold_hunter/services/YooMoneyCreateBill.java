package com.gold_hunter.gold_hunter.services;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class YooMoneyCreateBill {

    public String createBill(String comment, String label, int orderId, float sum) {
        final CloseableHttpClient httpclient = HttpClients.createDefault();
        final HttpPost httpPost = new HttpPost("https://yoomoney.ru/quickpay/confirm.xml");

        List<NameValuePair> nvps = new ArrayList<>();
        nvps.add(new BasicNameValuePair("receiver", "4100116952998588"));
        nvps.add(new BasicNameValuePair("formcomment", comment));
        nvps.add(new BasicNameValuePair("short-dest", comment));
        nvps.add(new BasicNameValuePair("label", label));
        nvps.add(new BasicNameValuePair("quickpay-form", "shop"));
        nvps.add(new BasicNameValuePair("targets", "Gold Hunter #" + orderId));
        nvps.add(new BasicNameValuePair("sum", sum + ""));
        nvps.add(new BasicNameValuePair("comment", comment));
        nvps.add(new BasicNameValuePair("paymentType", "PC"));

        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
        } catch (UnsupportedEncodingException ignored) { }

        try (CloseableHttpResponse httpResponse = httpclient.execute(httpPost)) {
            final HttpEntity entity = httpResponse.getEntity();

            String[] response = EntityUtils.toString(entity).split(" ", 4);
            httpclient.close();

            return response[3];
        } catch (IOException ignored) { }

        return "";
    }
}
