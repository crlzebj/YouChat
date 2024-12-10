package com.zjx.youchat.service.impl;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import cn.hutool.json.JSONObject;
import com.zjx.youchat.service.RobotService;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
public class YiyanRobotServiceImpl implements RobotService {
    @Override
    public String chat(String msg) {
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
            post.setHeader("Acs-Token", "1733742167717_1733812098516_7JfXLz6mXz5Y6f9wfibHqCb7uZ04GM5uGw5JpHfEj/zIl/DgiGGBccb5DvyUENPiDiQ37Oxm+EKlwz8SbPrvdyvIGsOAXod52mcLIhWc2v7g75EDEXGOdIO/nlOiB04lhFyIiL++kWxpeFORYRdFvSSp8ysVk+L4qwkwlE+5wSVoPCeHf/6JEnE26oOXE0Qwh4YOD6iekIeXAVlgFyb43iDTD7XwqGZ1ZvyNKlkPqsXZvjyHLxtj8C6ZrXvOWJo55ws5nESm9K3nAIn3bCa43IUgssJG46LeSw8oW9xoVEU8lPFC/RZvzRIiK/bLdos5y7E8wgHhCRL864QZNfcghHlq2bBitzfdNnp6xJn25ID8/hnX/svGOYeY6ILv4+cjqU9BYT8yd/2gUIn4SaPAym5wO+4fXPjc+u8W2IWUCeY5YgHO6slypheoNwNvF32PqTGj4BsdQqcJNaVyaTE4fGQkJne4Pn5QnojqR9PiuMzVQKDBoiAkHUZbFZa9Ji+w");
            post.setHeader("Connection", "keep-alive");
            post.setHeader("Content-type", "application/json");
            post.setHeader("Cookie", "BIDUPSID=BBF8377649DE37C19BD91D6171C2A52F; PSTM=1725264721; BDUSS=1JZd2JVVEQ4Yk1iWU9qc1ktOHRYTHljOWNnUlJib1lWTUtOazRXVnhvNGh-UDFtRVFBQUFBJCQAAAAAAAAAAAEAAAB4MriBvKu2yNau19MAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACFv1mYhb9ZmQ; BDUSS_BFESS=1JZd2JVVEQ4Yk1iWU9qc1ktOHRYTHljOWNnUlJib1lWTUtOazRXVnhvNGh-UDFtRVFBQUFBJCQAAAAAAAAAAAEAAAB4MriBvKu2yNau19MAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACFv1mYhb9ZmQ; BAIDUID=BBF8377649DE37C19BD91D6171C2A52F:SL=0:NR=10:FG=1; BAIDUID_BFESS=BBF8377649DE37C19BD91D6171C2A52F:SL=0:NR=10:FG=1; ZFY=Ivibfr1GBo9dX:Bfq4unhSEmUPC7ZRgxFoiVHrOM8YTc:C; H_PS_PSSID=60451_60851_60949_60975; Hm_lvt_01e907653ac089993ee83ed00ef9c2f3=1733798984; HMACCOUNT=DCF84DBC7555312B; __bid_n=193ae78fa207536f5630bf; XFI=197218c0-b6ad-11ef-8c4a-49ffb103a20b; XFCS=290EB71B7A942DBAA7DA1C79961572E083AC9D3BC3E868444125A35909C62BB2; XFT=F67kegp0rXCSVCsNLPZpf97bH28DnorynOsvOSGnFwk=; Hm_lpvt_01e907653ac089993ee83ed00ef9c2f3=1733812023; ab_sr=1.0.1_NGYzMzA0OTMzZDY4YTExZDdjNDk3MWM2MjZkYTU1ODdhMGQ4YTE0ZDU5NzVmOGI4ZTdjOGU3MDdiMDg5NGUxM2FjNDE4MGUwMTBlZmY3Yjc5N2E5OWEzZTlhZDg1MzNmMzY0ZDQ4MTk0ODNjMmVmNWM0YzMzOTA5YmYxYzAwMmM3ZWNkOWFiMmJkMmJlOGQyN2Q2NWM2MmUxZWY5MmU4Y2Y1MDUzZmRmMGRkZmIwZTZhMzgxY2UwODA2OWRhZGFhNDQxMDEyZWE3OTZlNGIyYjRkYjQ3YTk0MmE2NWQ3NWM=; RT=\"z=1&dm=baidu.com&si=be285442-eafb-4b4a-8215-032cde4272df&ss=m4i2xgrk&sl=8&tt=20n&bcn=https%3A%2F%2Ffclog.baidu.com%2Flog%2Fweirwood%3Ftype%3Dperf&ld=i8z\"");
            post.setHeader("Host", "yiyan.baidu.com");
            post.setHeader("Origin", "https://yiyan.baidu.com");
            post.setHeader("Referer", "https://yiyan.baidu.com/");
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
            jsonObject.put("newAppSessionId", null);
            jsonObject.put("parentChatId", "0");
            jsonObject.put("pluginIds", "");
            jsonObject.put("sessionId", "");
            jsonObject.put("sessionName", msg);
            jsonObject.put("sign", "1733742167717_1733812098516_7JfXLz6mXz5Y6f9wfibHqCb7uZ04GM5uGw5JpHfEj/zIl/DgiGGBccb5DvyUENPiDiQ37Oxm+EKlwz8SbPrvdyvIGsOAXod52mcLIhWc2v7g75EDEXGOdIO/nlOiB04lhFyIiL++kWxpeFORYRdFvSSp8ysVk+L4qwkwlE+5wSVoPCeHf/6JEnE26oOXE0Qwh4YOD6iekIeXAVlgFyb43iDTD7XwqGZ1ZvyNKlkPqsXZvjyHLxtj8C6ZrXvOWJo55ws5nESm9K3nAIn3bCa43IUgssJG46LeSw8oW9xoVEU8lPFC/RZvzRIiK/bLdos5y7E8wgHhCRL864QZNfcghHlq2bBitzfdNnp6xJn25ID8/hnX/svGOYeY6ILv4+cjqU9BYT8yd/2gUIn4SaPAym5wO+4fXPjc+u8W2IWUCeY5YgHO6slypheoNwNvF32PqTGj4BsdQqcJNaVyaTE4fGQkJne4Pn5QnojqR9PiuMzVQKDBoiAkHUZbFZa9Ji+w\"");
            jsonObject.put("text", msg);
            jsonObject.put("timestamp", new Timestamp(System.currentTimeMillis()).getTime());
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
}
