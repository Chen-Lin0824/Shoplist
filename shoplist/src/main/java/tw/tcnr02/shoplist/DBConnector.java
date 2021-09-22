package tw.tcnr02.shoplist;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DBConnector {

    public static int httpstate = 0;
    private static String postUrl;
    private static String myResponse;
    static String result = null;
    private static OkHttpClient client = new OkHttpClient();
//---------------------------------------------------------

    //    static String connect_ip = "https://tcnr2021a02.000webhostapp.com/android_mysql_connect/android_connect_db_shoplist.php"; //000web
    static String connect_ip_shoplist01 = "https://tcnrcloud110a.com/110A1/fridge/shoplist-conn.php";
    static String connect_ip_shoplist02 = "https://tcnrcloud110a.com/110A1/fridge/shoplist-item-conn.php";
//hosting

    //===========現在開始為Shoplist01的區塊，勿動===========
    //----------------------------------------------------------------------------------------
    //===========查詢資料===========
    public static String executeQuery_Shoplist01(ArrayList<String> query_string) {//Mysql查詢 標準語法
//        OkHttpClient client = new OkHttpClient();
        postUrl = connect_ip_shoplist01;
        //--------------建立一個封包
        String query_0 = query_string.get(0);//外面帶參數
        //可寫  String query_0 = "SELECT * FROM......."
        FormBody body = new FormBody.Builder()
                .add("selefunc_string", "query")
                .add("query_string", query_0)
                .build();
//--------------丟出封包
        Request request = new Request.Builder()
                .url(postUrl)
                .post(body)
                .build();
// ===========================================
        // 使用httpResponse的方法取得http 狀態碼設定給httpstate變數
        httpstate = 0;   //設 httpcode初始值
        // ===========================================
        try (Response response = client.newCall(request).execute()) {
            httpstate = response.code();
            // ===========================================
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    //===========新增資料===========
    public static String executeInsert_Shoplist01(ArrayList<String> query_string) {
        //        OkHttpClient client = new OkHttpClient();
        postUrl = connect_ip_shoplist01;
        //--------------
        String query_0 = query_string.get(0);
        String query_1 = query_string.get(1);


        FormBody body = new FormBody.Builder()
                .add("selefunc_string", "insert")
                .add("shop_title", query_0)
                .add("shoplist_contain", query_1)

                .build();
//--------------
        Request request = new Request.Builder()
                .url(postUrl)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    //===========刪除資料===========
    public static String executeDelet_Shoplist01(ArrayList<String> query_string) {
        //OkHttpClient client = new OkHttpClient();
        postUrl = connect_ip_shoplist01;
        //--------------
        String query_0 = query_string.get(0);

        FormBody body = new FormBody.Builder()
                .add("selefunc_string", "delete")
                .add("id", query_0)
                .build();
//--------------
        Request request = new Request.Builder()
                .url(postUrl)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    //===========更新資料===========
    public static String executeUpdate_Shoplist01(ArrayList<String> query_string) {
//        OkHttpClient client = new OkHttpClient();
        postUrl = connect_ip_shoplist01;
        //--------------
        String query_0 = query_string.get(0);
        String query_1 = query_string.get(1);
        String query_2 = query_string.get(2);

        FormBody body = new FormBody.Builder()
                .add("selefunc_string", "update")
                .add("id", query_0)
                .add("shop_title", query_1)
                .add("shoplist_contain", query_2)

                .build();
//--------------
        Request request = new Request.Builder()
                .url(postUrl)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
    //===========shoplist01的區塊結束，勿動===========

    //===========現在開始為Shoplist02的區塊，勿動===========
    //===========查詢資料===========
    public static String executeQuery_Shoplist02(ArrayList<String> query_string) {//Mysql查詢 標準語法
//        OkHttpClient client = new OkHttpClient();
        postUrl = connect_ip_shoplist02;
        //--------------建立一個封包
        String query_0 = query_string.get(0);//外面帶參數
        //可寫  String query_0 = "SELECT * FROM......."
        FormBody body = new FormBody.Builder()
                .add("selefunc_string", "query")
                .add("query_string", query_0)
                .build();
//--------------丟出封包
        Request request = new Request.Builder()
                .url(postUrl)
                .post(body)
                .build();
// ===========================================
        // 使用httpResponse的方法取得http 狀態碼設定給httpstate變數
        httpstate = 0;   //設 httpcode初始值
        // ===========================================
        try (Response response = client.newCall(request).execute()) {
            httpstate = response.code();
            // ===========================================
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    //===========新增資料===========
    public static String executeInsert_Shoplist02(ArrayList<String> query_string) {
        //        OkHttpClient client = new OkHttpClient();
        postUrl = connect_ip_shoplist02;
        //--------------
        String query_0 = query_string.get(0);
        String query_1 = query_string.get(1);

        FormBody body = new FormBody.Builder()
                .add("selefunc_string", "insert")
                .add("titleID", query_0)
                .add("shoplist_item", query_1)


                .build();
//--------------
        Request request = new Request.Builder()
                .url(postUrl)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    //===========刪除資料===========
    public static String executeDelet_Shoplist02(ArrayList<String> query_string) {
        //OkHttpClient client = new OkHttpClient();
        postUrl = connect_ip_shoplist02;
        //--------------
        String query_0 = query_string.get(0);


        FormBody body = new FormBody.Builder()
                .add("selefunc_string", "delete")
                .add("id", query_0)
                .build();
//--------------
        Request request = new Request.Builder()
                .url(postUrl)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    //===========更新資料===========
    public static String executeUpdate_Shoplist02(ArrayList<String> query_string) {
//        OkHttpClient client = new OkHttpClient();
        postUrl = connect_ip_shoplist02;
        //--------------
        String query_0 = query_string.get(0);
        String query_1 = query_string.get(1);
        String query_2 = query_string.get(2);

        FormBody body = new FormBody.Builder()
                .add("selefunc_string", "update")
                .add("id", query_0)
                .add("titleID", query_1)
                .add("shoplist_item", query_2)


                .build();
//--------------
        Request request = new Request.Builder()
                .url(postUrl)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
//===========Shoplist02的區塊結束，勿動===========
}