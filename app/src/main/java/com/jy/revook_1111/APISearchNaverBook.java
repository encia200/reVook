/*
package com.jy.revook_1111;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.net.URL;

*/
/**
 * Created by encia on 2017-11-26.
 *//*


public class APISearchNaverBook {
    String clientId = "ErSA6Vu68HcRM4ggzSQm";//애플리케이션 클라이언트 아이디값";
    String clientSecret = "hvjcnMyWQi";//애플리케이션 클라이언트 시크릿값";

    Button b1;
    EditText et1;
    TextView tv1;
    NaverSearchTask task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        b1 = (Button) findViewById(R.id.button1);
        et1 = (EditText) findViewById(R.id.editText1);
        tv1 = (TextView) findViewById(R.id.textView1);

        b1.setOnClickListener(this);
    }

    class NaverSearchTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String ... search) {
            String url = "http://openapi.naver.com/search?key=네이버오픈API키&query=" + search[0] + "&target=news&start=1&display=10";
            XmlPullParserFactory factory;
            XmlPullParser parser;
            URL xmlUrl;
            String returnResult = "";

            try {
                boolean flag1 = false;

                xmlUrl = new URL(url);
                xmlUrl.openConnection().getInputStream();
                factory = XmlPullParserFactory.newInstance();
                parser = factory.newPullParser();
                parser.setInput(xmlUrl.openStream(), "utf-8");
                int eventType = parser.getEventType();
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    switch (eventType) {
                        case XmlPullParser.START_DOCUMENT:
                            break;
                        case XmlPullParser.END_DOCUMENT:
                            break;
                        case XmlPullParser.START_TAG:
                            if (parser.getName().equals("title")) {
                                flag1 = true;
                            }
                            break;
                        case XmlPullParser.END_TAG:
                            break;
                        case XmlPullParser.TEXT:
                            if (flag1 == true) {
                                returnResult += parser.getText()+"\n";
                                flag1 = false;
                            }
                            break;
                    }
                    eventType = parser.next();
                }
            } catch (Exception e) {

            }

            return returnResult;
        }

        @Override
        protected void onPostExecute(String result) {
            tv1.setText(result);
        }

    }

    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub
        task = new NaverSearchTask();
        task.execute(et1.getText().toString());
        et1.setText("");
    }}
*/
