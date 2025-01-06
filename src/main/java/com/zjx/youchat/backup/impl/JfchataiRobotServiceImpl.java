package com.zjx.youchat.backup.impl;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import cn.hutool.json.JSONObject;
import com.zjx.youchat.backup.RobotService;
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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
public class JfchataiRobotServiceImpl implements RobotService {
    @Override
    public String chat(String msg) {
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
