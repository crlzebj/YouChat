package com.zjx.youchat.nioChat;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import cn.hutool.json.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class Robot {
    /**
     * 文心一言
     * @param msg
     * @return
     */
    public static String chatBaidu(String msg) {
        //去除httpclient debug日志
        ((LoggerContext) LoggerFactory.getILoggerFactory())
                .getLogger("org.apache.http")
                .setLevel(Level.ERROR);

        try (CloseableHttpClient client = HttpClients.createDefault()) {
            String url = "https://yiyan.baidu.com/eb/chat/conversation/v2";

            HttpPost post = new HttpPost(url);

            // 设置请求头
            post.setHeader("Accept", "text/event-stream,application/json, text/event-stream");
            post.setHeader("Accept-Encoding", "gzip, deflate, br, zstd");
            post.setHeader("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8,en-GB;q=0.7,en-US;q=0.6");
            post.setHeader("Acs-Token", "1733742167717_1733801088361_ysQK8Ax1/g25idig14W+T/T8u4IsBbxc/zd3ZS8TMAtJHx391SMmkr7RTUWiVziNGQdY8izjnSdq6I/7Jgj7x/bFRHllmSxWox/pHEamxUHuCJ4/EivHZJYNngsiWdoQG0Pw8FnrmWKeBRdhm69PwqqwYps+uxYYWmvbfqPEuPaw1GLsKAtJGTCd4k0wpO8MzPT/NJzDLLZJvPJwyY4rsSsjlBL8L8jkQWh1LdYC+pVSDUwOmP5imEhHcs92lRkJSqfOUhxILKtEo46e/kDgcB3G4h2SsL7Bu9BQn4ObeUmbiB43LGUDJk1egqxFOaywSJvlXK/YSA6v0pMoMUrcL370woqa4L0BtlzY4Rh9P/5xm/ai4DkAupR5a/QjRbghKbfVZP2HMyRzAr5cClgT8wZe2OjLud2zxO6pG2wtN6mYjkASJWqX4rnm0wnPgYx2Cn7CQP+3qD19cz+DXc+rlsjQHFjRlEf0rlNAjDLy91qoVRpV1zo+5zrFm/sE7MsB");
            post.setHeader("Connection", "keep-alive");
            post.setHeader("Content-type", "application/json");
            post.setHeader("Cookie", "BIDUPSID=BBF8377649DE37C19BD91D6171C2A52F; PSTM=1725264721; BDUSS=1JZd2JVVEQ4Yk1iWU9qc1ktOHRYTHljOWNnUlJib1lWTUtOazRXVnhvNGh-UDFtRVFBQUFBJCQAAAAAAAAAAAEAAAB4MriBvKu2yNau19MAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACFv1mYhb9ZmQ; BDUSS_BFESS=1JZd2JVVEQ4Yk1iWU9qc1ktOHRYTHljOWNnUlJib1lWTUtOazRXVnhvNGh-UDFtRVFBQUFBJCQAAAAAAAAAAAEAAAB4MriBvKu2yNau19MAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACFv1mYhb9ZmQ; BAIDUID=BBF8377649DE37C19BD91D6171C2A52F:SL=0:NR=10:FG=1; BAIDUID_BFESS=BBF8377649DE37C19BD91D6171C2A52F:SL=0:NR=10:FG=1; ZFY=Ivibfr1GBo9dX:Bfq4unhSEmUPC7ZRgxFoiVHrOM8YTc:C; H_PS_PSSID=60451_60851_60949_60975; Hm_lvt_01e907653ac089993ee83ed00ef9c2f3=1733798984; HMACCOUNT=DCF84DBC7555312B; __bid_n=193ae78fa207536f5630bf; XFI=1e827ff0-b6a6-11ef-a428-238fb8e32ac8; Hm_lpvt_01e907653ac089993ee83ed00ef9c2f3=1733801007; XFCS=459682E16D311258DD3A746CE86A652F7FF8509F218114B60EF422154FF6270B; XFT=e5Ver3SA8xzS6ujLrZoZx3G6nGNftwt53NZ681T4LSY=; ab_sr=1.0.1_YzQ3YzI4N2QxNmY4MjQ0ZGY4ZDNiYjkzMTAwZDZjNzg5MWFmZTEzMjQ5ZWRmNGZmZTczYTM5OTI4MGZkNWQzNGM3OGVlZjlmMzhkMzZiNmYxNmY3OWZlZTAwZGU0NDQ5OGRkY2Y3ZGY2ZTUwNGNlNWJiZWJhNGFlNTNkNmNmYmMxZTc3NjZkZTc3ZmNjYzBhMTA0NTNkOGUyZGE0YWJhMmZhZGU2OTdlZGJjNTdiODJiOWU1YzdiNGUxZDM4ZDFkYmUyZTlmMWVmNzBjOThjN2EyN2UzMmM3MjZiMjJmY2Q=; RT=\"z=1&dm=baidu.com&si=be285442-eafb-4b4a-8215-032cde4272df&ss=m4hv5dgi&sl=1e&tt=1qee&bcn=https%3A%2F%2Ffclog.baidu.com%2Flog%2Fweirwood%3Ftype%3Dperf&ld=18oqt\"");
            post.setHeader("Host", "yiyan.baidu.com");
            post.setHeader("Origin", "https://yiyan.baidu.com");
            post.setHeader("Referer", "https://yiyan.baidu.com/chat/MjE3NjMzMjQwODo0NzcyMTQ0NjUw");
            post.setHeader("Sec-Ch-Ua", "\"Microsoft Edge\";v=\"131\", \"Chromium\";v=\"131\", \"Not_A Brand\";v=\"24\"");
            post.setHeader("Sec-Ch-Ua-Mobile", "?0");
            post.setHeader("Sec-Ch-Ua-Platform", "\"Windows\"");
            post.setHeader("Sec-Fetch-Dest", "empty");
            post.setHeader("Sec-Fetch-Mode", "cors");
            post.setHeader("Sec-Fetch-Site", "same-origin");
            post.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/131.0.0.0 Safari/537.36 Edg/131.0.0.0");

            // 设置请求参数
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("code", 0);
            jsonObject.put("deviceType", "pc");
            jsonObject.put("file_ids", null);
            jsonObject.put("isNewYiyan", true);
            jsonObject.put("isSlowThought", false);
            jsonObject.put("jt", "31$eyJrIj4iNyI0Iix5IkciQEdERUtERkZIUUlOUCJJIkFqIjwiNTw5OkA5Ozs8RENDSSI+IjYzIlEiSlFOT1VOUDAxODs1PSIzIit5IkYiPiI8IjYiTiJJRU1HUSJIIjYiOiI7enk9P2FyODIvZnJHd2RlOVtjaS9sZi1sdVJuakdWMTJxOHpXVEFYXVRmdF9YRmZzZVhqLTguOkwwPjNGRWF1ZDR1SXZILV1vPz5gKWFceS5cZFtbaWM8KmpSaT5zdUdZXDstKXQ+cFlZYFJnZHRBLmFLYGJHS2k7UmQvaWVoYlE8WUZqLk13YjFlY2dGaWBDLHVbeSo6bGE8LT9BOVRuMXJxcm8qOU0ucHNZUy87aSt0LG9BeCw3KT45Yj04ZzFlM0dvWnNVbFxyMF9vYithOi5jMzJLYzcsOWctP3RYUlhlOzl6bFpQP0MsYUZTakpgMFh4KmRbamgzYWMvdkxXTno6PTVgOC9cVnRwLT8vL0s2MmdLM2poYGA4dUU5M2lkK1t0P2s4UE4yVyx5Zz9VVF14Z15lLUhJYWpwUjxBSHZWXWp0Ky5eMC9VV0VDWy86X0NaTUk9MmBzUE90U0U3ZV1NK24yOT4rZU1EczQpNl1tdWJkN3AsMzlsUD5iZWt8eXUwejxROFZcZi55KUVTa195XlstTV9AUm5SSWtCejNRSnZHd3heeD5CMkt8QUF8S3VFdypLKywtLy5XVWE1ODQ4Zjg/bkBsQj49QkNJd0YifQ==");
            jsonObject.put("model", "EB35");
            jsonObject.put("msg", "");
            jsonObject.put("newAppSessionId", "");
            jsonObject.put("parentChatId", "14261535838");
            jsonObject.put("pluginIds", "");
            jsonObject.put("sessionId", "MjE3NjMzMjQwODo0NzcyMTQ0NjUw");
            jsonObject.put("sessionName", msg);
            jsonObject.put("sign", "1733742167717_1733804081290_VVgr2XX6D7S3K1zGPfPFH9FzhB7gthnpho3sJzNjQPFDnA/KJZMuFLmRWL1XdwU4GybjwtbXJF+g3VGcByCt3VmqHTmRf7QbXPiaO7EqtzWB5s2zI8QP+5Kbz1lqGvO00WVxeJJms8QqpemjCjtyqafQYCCHUel9ymeAWPm7wxfMYZVpopsCl2NuGoBz06CatAxSVXQ8NEpZ6QqgTDlL1exBElWJS5yVCtjT9+FOD9q59++U4bQmm+7hWcWorvJANauVt9V83DPMbo02ZT9CkGHSeRf0RgKrQfyAfBqtlqsU53LXnTr2hojrqMtkfuGj26CdujBoJ0CKwEXAjiOCqPuMrfoJLdNU1MwdbVub7d+ZkPI96Ui86oYb6NObdkY4Y5M6xT7ElahDhJjOU8oOIuI60SS1sz35FghD7BQ97ljPdOAiKNNG3YYoW7q07/oz/QmYBBeXNQHP8IBbYsQCB5iVSGO6DY4bN+Yd4jqXDkebx47H+07b6ZMJj7CoKvdS\"");
            jsonObject.put("text", msg);
            jsonObject.put("timestamp", 1733804081323L);
            jsonObject.put("type", "10");
            post.setEntity(new StringEntity(jsonObject.toString(), "UTF-8"));

            try (CloseableHttpResponse response = client.execute(post)) {
                if (response.getStatusLine().getStatusCode() != 200) {
                    log.info("请求失败!请重新抓包检查");
                    log.info(response.toString());
                    return null;
                }
                HttpEntity entity = response.getEntity();
                String responseContent = EntityUtils.toString(entity, "UTF-8");
                Pattern pattern = Pattern.compile("\"text\":\"(.*?)\"");
                Matcher matcher = pattern.matcher(responseContent);
                StringBuilder responseMessage = new StringBuilder();
                while (matcher.find()) {
                    responseMessage.append(matcher.group(1).replace("\\n", "\n"));
                }
                return responseMessage.toString();
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    public static void main(String[] args) {
        String msg = chatBaidu("介绍一下SYCL");
        log.info(msg);
    }

    /**
     * 飓风AI
     * @param msg
     * @return
     */
    public static String chat(String msg) {
        //去除httpclient debug日志
        ((LoggerContext) LoggerFactory.getILoggerFactory())
                .getLogger("org.apache.http")
                .setLevel(Level.ERROR);

        try (CloseableHttpClient client = HttpClients.createDefault()) {
            String url = "https://jfchatai.cn/api/chat/completions";

            HttpPost post = new HttpPost(url);

            // 设置请求头
            post.setHeader("Accept", "text/event-stream");
            post.setHeader("Accept-Encoding", "gzip, deflate, br, zstd");
            post.setHeader("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8,en-GB;q=0.7,en-US;q=0.6");
            post.setHeader("Authorization", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOjE5MTgsInNpZ24iOiI4ZWMzMWEzMzAyMWZmZmJmYTg3YzM3MjFlODg2ZGRmOSIsImV4cCI6MTAzNjgzOTczMjIsIm5iZiI6MTcyODQ4MzcyMiwiaWF0IjoxNzI4NDgzNzIyfQ.QgK8v7gMjsJ9stU5_uBI-WP4EVHxq7h1Bk8CnjcQ3_0");
            post.setHeader("Connection", "keep-alive");
            post.setHeader("Content-Type", "application/json");
            post.setHeader("Host", "jfchatai.cn");
            post.setHeader("Origin", "https://jfchatai.cn");
            post.setHeader("Referer", "https://jfchatai.cn/chat");
            post.setHeader("Sec-Ch-Ua", "Microsoft Edge\";v=\"131\", \"Chromium\";v=\"131\", \"Not_A Brand\";v=\"24\"");
            post.setHeader("Sec-Ch-Ua-Mobile", "?0");
            post.setHeader("Sec-Ch-Ua-Platform", "\"Windows\"");
            post.setHeader("Sec-Fetch-Dest", "empty");
            post.setHeader("Sec-Fetch-Mode", "cors");
            post.setHeader("Sec-Fetch-Site", "same-origin");
            post.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/131.0.0.0 Safari/537.36 Edg/131.0.0.0");
            post.setHeader("X-App-Version", "2.11.3");

            // 设置请求体
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("files", null);
            jsonObject.put("sessionId", 22064);
            jsonObject.put("text", msg);
            post.setEntity(new StringEntity(jsonObject.toString(), "UTF-8"));

            try (CloseableHttpResponse response = client.execute(post)) {
                if (response.getStatusLine().getStatusCode() != 200) {
                    log.info("请求失败!请重新抓包检查");
                    log.info(response.toString());
                    return null;
                }
                HttpEntity responseEntity = response.getEntity();
                String responseContent = EntityUtils.toString(responseEntity, "UTF-8");
                Pattern pattern = Pattern.compile("\"data\":\"(.*?)\"");
                Matcher matcher = pattern.matcher(responseContent);
                StringBuilder responseMessage = new StringBuilder();
                while (matcher.find()) {
                    responseMessage.append(matcher.group(1).replace("\\n", "\n"));
                }
                return responseMessage.toString();
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }
}
