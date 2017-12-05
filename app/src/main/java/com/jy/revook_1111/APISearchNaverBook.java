package com.jy.revook_1111;

import com.jy.revook_1111.Data.BookInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class APISearchNaverBook {

    public static List<BookInfo> bookInfoList = new ArrayList<>();
    public static int start = 1;
    public static int display = 4;
    public static String searchWord = null;
    public static String searchMode = null;


    public static void search() {
        String clientId = "ErSA6Vu68HcRM4ggzSQm";//애플리케이션 클라이언트 아이디값";
        String clientSecret = "hvjcnMyWQi";//애플리케이션 클라이언트 시크릿값";
        try {
            String text = URLEncoder.encode(searchWord, "UTF-8");
            String apiURL = "https://openapi.naver.com/v1/search/book_adv.json?" + searchMode + "=" + text+"&start=" + Integer.toString(start) + "&display=" + Integer.toString(display); // json 결과
            // String apiURL = "https://openapi.naver.com/v1/search/blog.xml?query=java"; // xml 결과
            URL url = new URL(apiURL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("X-Naver-Client-Id", clientId);
            con.setRequestProperty("X-Naver-Client-Secret", clientSecret);

            int responseCode = con.getResponseCode();
            BufferedReader br;
            if (responseCode == 200) { // 정상 호출
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {  // 에러 발생
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }
            parseBufferedReaderToJson(br);
            for (int i = 0; i < bookInfoList.size(); i++) {
                System.out.println(bookInfoList.get(i).title);
                System.out.println(bookInfoList.get(i).imageURL);
            }
            con.disconnect();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private static void parseBufferedReaderToJson(BufferedReader br) {
        BookInfo newBook;
        String jsonText;

        try {
            jsonText = readAll(br);
//            Log.d("JSON",jsonText);
            JSONObject json = new JSONObject(jsonText);
            JSONArray jsonArray = new JSONArray(json.getString("items"));

            for (int i = 0; i < jsonArray.length(); i++) {
                json = new JSONObject(jsonArray.getString(i));
                newBook = createBookInfo(json);
                bookInfoList.add(newBook);
            }
            System.out.println("###################### arraySize" + bookInfoList.size());

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private static BookInfo createBookInfo(JSONObject json) {
        String title, link, publisher, price, imageURL, author, description, isbn;
        BookInfo tempBook;
        try {
            title = json.getString("title");
            link = json.getString("link");
            publisher = json.getString("publisher");
            price = json.getString("price");
            imageURL = json.getString("image");
            author = json.getString("author");
            description = json.getString("description");
            isbn = json.getString("isbn");
            tempBook = new BookInfo(title, link, publisher, price, imageURL, author, description, isbn);
            //System.out.println(tempBook);
            return tempBook;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String readAll(Reader br) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = br.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public static void moreList()
    {
        start += display;
        search();
    }
}