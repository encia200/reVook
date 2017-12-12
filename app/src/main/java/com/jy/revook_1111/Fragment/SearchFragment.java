package com.jy.revook_1111.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.jy.revook_1111.APISearchNaverBook;
import com.jy.revook_1111.Data.BookInfo;
import com.jy.revook_1111.FontSetting;
import com.jy.revook_1111.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    private final String SEARCH_WITH_TITLE = "d_titl";
    private final String SEARCH_WITH_AUTHOR = "d_auth";
    private final String SEARCH_WITH_ISBN = "d_isbn";
    private final String SEARCH_WITH_PUBLISHER = "d_publ";

    private final static int SEARCH_FOR_NOBLE = 1;

    public static List<BookInfo> famousNobleList = new ArrayList<>();

    private TextView famousNoble;
    private TextView famousNoble_title1;
    private TextView famousNoble_title2;
    private TextView famousNoble_title3;

    private ImageView famousNoble_img1;
    private ImageView famousNoble_img2;
    private ImageView famousNoble_img3;

    private TextView search_bar_title;
    private EditText editText_search;
    private Button btn_search;

    private FontSetting fontSetting;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_search, container, false);

        idSetting(v);
        fontSetting(v);

        new Thread(){
            @Override
            public void run() {
                search(SEARCH_FOR_NOBLE);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setImage(container);
                        setTitle();
                    }
                });

            }
        }.start();




        /*검색버튼 */
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(editText_search.getText().toString().length() < 1)
                    return;

                /*검색어가 같으면 */
                if (BookSearchFragment.isSearching && editText_search.getText().toString().equals(APISearchNaverBook.searchWord)) {
                    Toast.makeText(getContext(), "이미 검색중인 도서입니다.", Toast.LENGTH_SHORT).show();
                    return;
                }

                    BookSearchFragment.isSearching = true;
                /*처음 검색 시*/
                    new Thread() {
                        public void run() {
                            APISearchNaverBook.bookInfoList.clear();
                            APISearchNaverBook.searchWord = editText_search.getText().toString();
                            APISearchNaverBook.searchMode = SEARCH_WITH_TITLE;
                            APISearchNaverBook.start = 1;
                            APISearchNaverBook.search();
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Fragment fragment = new BookSearchFragment();
                                    FragmentManager fragmentManager = getFragmentManager();
                                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                    fragmentTransaction.replace(R.id.searchFragment, fragment);
                                    fragmentTransaction.addToBackStack("YES");
                                    fragmentTransaction.commit();
                                }
                            });
                        }
                    }.start();

                }
        });
        return v;
    }

    private void setImage(ViewGroup container)
    {
        Glide.with(container.getContext()).load(famousNobleList.get(0).imageURL).into(famousNoble_img1);
        Glide.with(container.getContext()).load(famousNobleList.get(1).imageURL).into(famousNoble_img2);
        Glide.with(container.getContext()).load(famousNobleList.get(2).imageURL).into(famousNoble_img3);
    }

    private void setTitle()
    {
        famousNoble_title1.setText(famousNobleList.get(0).title);
        famousNoble_title2.setText(famousNobleList.get(1).title);
        famousNoble_title3.setText(famousNobleList.get(2).title);
    }

    private void idSetting(View v)
    {
        famousNoble = (TextView) v.findViewById(R.id.search_famous_noble);
        famousNoble_title1 = (TextView)v.findViewById(R.id.search_famous_noble_title1);
        famousNoble_title2 = (TextView)v.findViewById(R.id.search_famous_noble_title2);
        famousNoble_title3 = (TextView)v.findViewById(R.id.search_famous_noble_title3);
        search_bar_title = (TextView) v.findViewById(R.id.search_bar_title);
        btn_search = (Button) v.findViewById(R.id.btn_search);
        editText_search = (EditText) v.findViewById(R.id.edittext_search);
        famousNoble_img1 = (ImageView) v.findViewById(R.id.search_famous_noble_img1);
        famousNoble_img2 = (ImageView) v.findViewById(R.id.search_famous_noble_img2);
        famousNoble_img3 = (ImageView) v.findViewById(R.id.search_famous_noble_img3);
    }

    private void fontSetting(View v)
    {
        fontSetting = new FontSetting(getContext());

        famousNoble.setTypeface(fontSetting.getTypeface_Title());

        famousNoble_title1.setTypeface(fontSetting.getTypeface_Contents());
        famousNoble_title2.setTypeface(fontSetting.getTypeface_Contents());
        famousNoble_title3.setTypeface(fontSetting.getTypeface_Contents());

        search_bar_title.setTypeface(fontSetting.getTypeface_Title());
        editText_search.setTypeface(fontSetting.getTypeface_Contents());
        btn_search.setTypeface(fontSetting.getTypeface_Contents());
    }

    public static void search(int mode) {
        String clientId = "ErSA6Vu68HcRM4ggzSQm";//애플리케이션 클라이언트 아이디값";
        String clientSecret = "hvjcnMyWQi";//애플리케이션 클라이언트 시크릿값";
        try {
            String apiURL = null;
            switch (mode)
            {
                case SEARCH_FOR_NOBLE:
                    apiURL = "https://openapi.naver.com/v1/search/book_adv.json?d_titl=소설&d_catg=100&sort=count&start=1&display=3";
                    break;
            }
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

            switch (mode)
            {
                case SEARCH_FOR_NOBLE:
                    parseBufferedReaderToJson(br, SEARCH_FOR_NOBLE);
                    break;
            }

            con.disconnect();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private static void parseBufferedReaderToJson(BufferedReader br, int mode) {
        BookInfo newBook;
        String jsonText;

        try {
            jsonText = readAll(br);
            JSONObject json = new JSONObject(jsonText);
            JSONArray jsonArray = new JSONArray(json.getString("items"));

            for (int i = 0; i < jsonArray.length(); i++) {
                json = new JSONObject(jsonArray.getString(i));
                newBook = createBookInfo(json);
                switch (mode)
                {
                    case SEARCH_FOR_NOBLE:
                        famousNobleList.add(newBook);
                        break;
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private static BookInfo createBookInfo(JSONObject json) {
        String title, link, publisher, price, imageURL, author, description, isbn;
        BookInfo tempBook;
        int j = 0;
        try {
            title = json.getString("title");
            while (j < title.length()) {
                if (title.charAt(j) == '(')
                    break;
                j++;
            }
            if (j != 0)
                title = title.substring(0, j);
            link = json.getString("link");
            publisher = json.getString("publisher");
            int tmp_price = Integer.parseInt(json.getString("price"));
            price = String.format("%,d", tmp_price);
            price = price + "원";
            imageURL = json.getString("image");
            author = json.getString("author");
            description = json.getString("description");
            isbn = json.getString("isbn");
            tempBook = new BookInfo(title, link, publisher, price, imageURL, author, description, isbn);
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
}
