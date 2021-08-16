package jike2;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
 
import java.io.IOException;
import java.util.HashMap;
import java.util.Map


public class httpClient {

    public static String doPostJson(String url, String json) {
        // ����Httpclient����
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        String resultString = "";
        try {
            // ����Http Post����
            HttpPost httpPost = new HttpPost(url);
            // ������������
            StringEntity entity = new StringEntity(json, ContentType.APPLICATION_JSON);
            httpPost.setEntity(entity);
            // ִ��http����
            response = httpClient.execute(httpPost);
            resultString = EntityUtils.toString(response.getEntity(), "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                response.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        System.out.println("���ص�����"+resultString);
        return resultString;
    }
 
 
    public static void main(String[] args) {
        JSONObject json1=new JSONObject();
        json1.put("requestId","6c84fb9012c411e1840d7b25c5ee775a");
        json1.put("reqType", "REQ");
        json1.put("apiName","dryRun");
        String url="http://localhost:8801";
        System.out.println(doPostJson(url,json1.toString()));
    }


}
