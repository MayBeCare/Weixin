package com.imooc.trans;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;


class HttpGet {
    protected static final int SOCKET_TIMEOUT = 10000; // 10S
    protected static final String GET = "GET";

    public static String get(String host, Map<String, String> params) {
        try {
      
            String sendUrl = getUrlWithQueryString(host, params);

//             System.out.println("URL:" + sendUrl);

            URL uri = new URL(sendUrl); // ����URL����
            HttpURLConnection conn = (HttpURLConnection) uri.openConnection();

            conn.setConnectTimeout(SOCKET_TIMEOUT); // ������Ӧ��ʱ
            conn.setRequestMethod(GET);
            int statusCode = conn.getResponseCode();
            if (statusCode != HttpURLConnection.HTTP_OK) {
                System.out.println("Http�����룺" + statusCode);
            }

            // ��ȡ������������
            InputStream is = conn.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            StringBuilder builder = new StringBuilder();
            String line = null;
            while ((line = br.readLine()) != null) {
                builder.append(line);
            }

            String text = builder.toString();

            close(br); // �ر�������
            close(is); // �ر�������
            conn.disconnect(); // �Ͽ�����

            return text;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getUrlWithQueryString(String url, Map<String, String> params) {
        if (params == null) {
            return url;
        }

        StringBuilder builder = new StringBuilder(url);
        if (url.contains("?")) {
            builder.append("&");
        } else {
            builder.append("?");
        }

        int i = 0;
        for (String key : params.keySet()) {
            String value = params.get(key);
            if (value == null) { // ���˿յ�key
                continue;
            }
            if (i != 0) {
                builder.append('&');
            }

            builder.append(key);
            builder.append('=');
            builder.append(encode(value));

            i++;
        }

        return builder.toString();
    }

    protected static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * ��������ַ�������URL����, ��ת��Ϊ%20������ʽ
     * 
     * @param input ԭ��
     * @return URL����. �������ʧ��, �򷵻�ԭ��
     */
    public static String encode(String input) {
        if (input == null) {
            return "";
        }
        try {
            return URLEncoder.encode(input, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return input;
    }


}

