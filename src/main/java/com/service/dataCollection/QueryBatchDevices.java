package com.service.dataCollection;

import Entity.Node;
import Mapper.userMapper;
import com.utils.Constant;
import com.utils.HttpsUtil;
import com.utils.JsonUtil;
import com.utils.StreamClosedHttpResponse;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;

/**
 * Query Batch Devices:
 * This interface is used to query information about devices in batches.
 */
public class QueryBatchDevices {

    public static void main(String args[]) throws Exception {

        String resource = "mybatis-config.xml"; //榛樿閰嶇疆鏂囦欢浠巖esource涓嬭鐨�
        //String resource = "D:/Java/IDE_project/shenteng/sgj/sgj/src/main/resources/mybatis-config.xml"; //榛樿閰嶇疆鏂囦欢浠巖esource涓嬭鐨�

        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);



        // Two-Way Authentication
        HttpsUtil httpsUtil = new HttpsUtil();
        httpsUtil.initSSLConfigForTwoWay();

        // Authentication.get token
        String accessToken = login(httpsUtil);

        //Please make sure that the following parameter values have been modified in the Constant file.
        String appId = Constant.APPID;
        String urlQueryBatchDevices = Constant.QUERY_BATCH_DEVICES;

        //please replace the status (ONLINE|OFFLINE|ABNORMAL), when you call this interface.
        String status = "ONLINE"; //鍦ㄧ嚎鐨�
        Integer pageNo = 0;
        //FEFEFEFE6810437632510000008116901F0047700100295148010029202005282058240000A716 瑙ｆ瀽杩欎釜锛岀粰浣犱釜鍒楄〃锛屾瘡鏉℃槸涓�涓澶囩殑锛屾嬁杩欎釜瑙ｆ瀽鏁版嵁銆�

        Map<String, String> paramQueryBatchDevices = new HashMap<>();
        paramQueryBatchDevices.put("status", status);
        paramQueryBatchDevices.put("pageNo", pageNo.toString());

        Map<String, String> header = new HashMap<>();
        header.put(Constant.HEADER_APP_KEY, appId);
        header.put(Constant.HEADER_APP_AUTH, "Bearer" + " " + accessToken);
        TimerTask task = new TimerTask() {
            public void run()
            {
                SqlSession session = sqlSessionFactory.openSession();
                StreamClosedHttpResponse responseQueryBatchDevices = null;
                try {
                    responseQueryBatchDevices = httpsUtil.doGetWithParasGetStatusLine(urlQueryBatchDevices,
                            paramQueryBatchDevices, header);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println("QueryBatchDevices, response content:");
                System.out.println(responseQueryBatchDevices.getStatusLine());
                String totaldata=responseQueryBatchDevices.getContent();
                //System.out.println(responseQueryBatchDevices.getContent());
                System.out.println(totaldata);
                //杩欏啓閿欎簡
                Map<String, ArrayList> data = new HashMap<>();
                data = JsonUtil.jsonString2SimpleObj(totaldata, data.getClass());
                ArrayList devicelist = data.get("devices");
                for(int i=0;i<devicelist.size();i++) {
                    String deviceId = ((HashMap<String, HashMap<String, String>>) devicelist.get(i)).get("deviceInfo").get("nodeId");
                    ArrayList<HashMap<String, HashMap>> services = ((HashMap<String, ArrayList>) devicelist.get(i)).get("services");
                    HashMap<String, HashMap> message = services.get(0);
                    HashMap<String, HashMap> mainData = message.get("data");
                    HashMap<String, String> device = mainData.get("device");
                    String data1 = device.get("data1");
                    System.out.println(data1);
                    String accumu = data1.substring(36, 44);
                    accumu = accumu.substring(6, 8) + accumu.substring(4, 6) + accumu.substring(2, 4) + accumu.substring(0, 2);
                    int aa = Integer.parseInt(accumu);
                    //System.out.println(aa);
                    double f1 = new BigDecimal((float) aa / 1000).setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
                    DecimalFormat decimalFormat=new DecimalFormat(".00");
                    String p = decimalFormat.format(f1);
                    float f2 =Float.parseFloat(p);
                    //float f2  =  (float)(Math.round(f1*100))/100;


                    System.out.println("Cumulative usage:" + f2);
                    //System.out.println("Cumulative usage:"+accumu.substring(6,8)+accumu.substring(4,6)+accumu.substring(2,4)+accumu.substring(0,2));
                    String date = data1.substring(56, 70);
                    date = date.substring(0, 4) + "-" + date.substring(4, 6) + "-" + date.substring(6, 8) + " " + date.substring(8, 10) + ":" + date.substring(10, 12) + ":" + date.substring(12, 14);
                    //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    // Date date1=sdf.parse(date);

                    System.out.println("Date:" + date);

                    userMapper mapper = session.getMapper(userMapper.class);

                    Node n = new Node(deviceId,f2,null,"2020-06-03 16:00:00",date);
                    int nn = mapper.insertNode(n);
                    session.commit();

                    System.out.println(nn);

                }
                session.close();
            }
        };
        Timer timer = new Timer();
        long delay = 0;
        long intevalPeriod = 1200 * 1000;
        timer.scheduleAtFixedRate(task, delay,intevalPeriod);




    }


    /**
     * Authentication.get token
     */
    @SuppressWarnings("unchecked")
    public static String login(HttpsUtil httpsUtil) throws Exception {

        String appId = Constant.APPID;
        String secret = Constant.SECRET;
        String urlLogin = Constant.APP_AUTH;

        Map<String, String> paramLogin = new HashMap<>();
        paramLogin.put("appId", appId);
        paramLogin.put("secret", secret);

        StreamClosedHttpResponse responseLogin = httpsUtil.doPostFormUrlEncodedGetStatusLine(urlLogin, paramLogin);

        System.out.println("app auth success,return accessToken:");
        System.out.println(responseLogin.getStatusLine());
        System.out.println(responseLogin.getContent());
        System.out.println();

        Map<String, String> data = new HashMap<>();
        data = JsonUtil.jsonString2SimpleObj(responseLogin.getContent(), data.getClass());
        return data.get("accessToken");
    }

}
