package com.jy.revook_1111.Fragment;


import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.jy.revook_1111.APISearchNaverBook;
import com.jy.revook_1111.Activity.CustomDialog;
import com.jy.revook_1111.Data.BookInfo;
import com.jy.revook_1111.FontSetting;
import com.jy.revook_1111.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

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
    private final static int SEARCH_FOR_COMIC = 2;

    public static List<BookInfo> famousNobleList = new ArrayList<>();
    public static List<BookInfo> famousComicList = new ArrayList<>();

    public static Fragment fragment;
    public static FragmentManager fragmentManager;
    public static FragmentTransaction fragmentTransaction;

    private TextView famousNoble;
    private TextView famousNoble_title1;
    private TextView famousNoble_title2;
    private TextView famousNoble_title3;

    private TextView famousComic;
    private TextView famousComic_title1;
    private TextView famousComic_title2;
    private TextView famousComic_title3;

    private ImageView famousNoble_img1;
    private ImageView famousNoble_img2;
    private ImageView famousNoble_img3;

    private ImageView famousComic_img1;
    private ImageView famousComic_img2;
    private ImageView famousComic_img3;

    private TextView search_bar_title;
    private EditText editText_search;
    private Button btn_search;

    private RelativeLayout relativeLayout;
    private InputMethodManager imm;
    private FontSetting fontSetting;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_search, container, false);
        idSetting(v);
        fontSetting(v);
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(getContext()));
        new Thread(){
            @Override
            public void run() {
                search(SEARCH_FOR_NOBLE);
                search(SEARCH_FOR_COMIC);
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

                if(BookSearchFragment.isSearching)
                {
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
                                    fragmentTransaction = getFragmentManager().beginTransaction();
                                    fragmentTransaction.remove(fragment);
                                    fragment = new BookSearchFragment();
                                    fragmentManager = getFragmentManager();
                                    fragmentTransaction = fragmentManager.beginTransaction();
                                    fragmentTransaction.replace(R.id.searchFragment, fragment);
                                    fragmentTransaction.addToBackStack(null);
                                    fragmentTransaction.commit();
                                }
                            });
                        }
                    }.start();
                }
                else {
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
                                   fragment = new BookSearchFragment();
                                    fragmentManager = getFragmentManager();
                                    fragmentTransaction = fragmentManager.beginTransaction();
                                    fragmentTransaction.replace(R.id.searchFragment, fragment);
                                    fragmentTransaction.addToBackStack(null);
                                    fragmentTransaction.commit();
                                }
                            });
                        }
                    }.start();
                }

                }
        });
        return v;
    }

    private void setImage(ViewGroup container)
    {
        Glide.with(container.getContext()).load(famousNobleList.get(0).imageURL).into(famousNoble_img1);
        Glide.with(container.getContext()).load(famousNobleList.get(1).imageURL).into(famousNoble_img2);
        Glide.with(container.getContext()).load(famousNobleList.get(2).imageURL).into(famousNoble_img3);
        Glide.with(container.getContext()).load(famousComicList.get(0).imageURL).into(famousComic_img1);
        Glide.with(container.getContext()).load(famousComicList.get(1).imageURL).into(famousComic_img2);
        Glide.with(container.getContext()).load(famousComicList.get(2).imageURL).into(famousComic_img3);
    }

    private void setTitle()
    {
        famousNoble_title1.setText(famousNobleList.get(0).title);
        famousNoble_title2.setText(famousNobleList.get(1).title);
        famousNoble_title3.setText(famousNobleList.get(2).title);
        famousComic_title1.setText(famousComicList.get(0).title);
        famousComic_title2.setText(famousComicList.get(1).title);
        famousComic_title3.setText(famousComicList.get(2).title);
    }

    private void idSetting(View v)
    {
        imm = (InputMethodManager) getContext().getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);

        famousNoble = (TextView) v.findViewById(R.id.search_famous_noble);
        famousNoble_title1 = (TextView)v.findViewById(R.id.search_famous_noble_title1);
        famousNoble_title2 = (TextView)v.findViewById(R.id.search_famous_noble_title2);
        famousNoble_title3 = (TextView)v.findViewById(R.id.search_famous_noble_title3);
        famousComic = (TextView) v.findViewById(R.id.search_famous_comic);
        famousComic_title1 = (TextView)v.findViewById(R.id.search_famous_comic_title1);
        famousComic_title2 = (TextView)v.findViewById(R.id.search_famous_comic_title2);
        famousComic_title3 = (TextView)v.findViewById(R.id.search_famous_comic_title3);
        search_bar_title = (TextView) v.findViewById(R.id.search_bar_title);
        btn_search = (Button) v.findViewById(R.id.btn_search);
        editText_search = (EditText) v.findViewById(R.id.edittext_search);
        famousNoble_img1 = (ImageView) v.findViewById(R.id.search_famous_noble_img1);
        famousNoble_img2 = (ImageView) v.findViewById(R.id.search_famous_noble_img2);
        famousNoble_img3 = (ImageView) v.findViewById(R.id.search_famous_noble_img3);
        famousComic_img1 = (ImageView) v.findViewById(R.id.search_famous_comic_img1);
        famousComic_img2 = (ImageView) v.findViewById(R.id.search_famous_comic_img2);
        famousComic_img3 = (ImageView) v.findViewById(R.id.search_famous_comic_img3);
        relativeLayout = (RelativeLayout) v.findViewById(R.id.searchFragment);
        relativeLayout.setOnTouchListener(new View.OnTouchListener() {
                                                @Override
                                                public boolean onTouch(View view, MotionEvent motionEvent) {
                                                    editText_search.clearFocus();
                                                    imm.hideSoftInputFromWindow(editText_search.getWindowToken(), 0);
                                                    return false;
                                                }
                                            }
        );

        editText_search.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //Enter key Action
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    btn_search.performClick();
                    return true;
                }
                return false;
            }
        });

        famousNoble_img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                        onClick1(SEARCH_FOR_NOBLE);
            }
        });

        famousNoble_img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClick2(SEARCH_FOR_NOBLE);
            }
        });

        famousNoble_img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClick3(SEARCH_FOR_NOBLE);
            }
        });

        famousComic_img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClick1(SEARCH_FOR_COMIC);
            }
        });

        famousComic_img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClick2(SEARCH_FOR_COMIC);
            }
        });

        famousComic_img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClick3(SEARCH_FOR_COMIC);
            }
        });

    }

    public void onClick1(int mode)
    {
        CustomDialog cd = new CustomDialog(getActivity(), 0, CustomDialog.SEARCH_FRAGMENT + mode);
        cd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        cd.setCancelable(true);
        cd.show();
    }

    public void onClick2(int mode)
    {
        CustomDialog cd = new CustomDialog(getActivity(), 1, CustomDialog.SEARCH_FRAGMENT + mode);
        cd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        cd.setCancelable(true);
        cd.show();
    }

    public void onClick3(int mode)
    {
        CustomDialog cd = new CustomDialog(getActivity(), 2, CustomDialog.SEARCH_FRAGMENT + mode);
        cd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        cd.setCancelable(true);
        cd.show();
    }

    private void fontSetting(View v)
    {
        fontSetting = new FontSetting(getContext());

        famousNoble.setTypeface(fontSetting.getTypeface_Title());

        famousNoble_title1.setTypeface(fontSetting.getTypeface_Contents());
        famousNoble_title2.setTypeface(fontSetting.getTypeface_Contents());
        famousNoble_title3.setTypeface(fontSetting.getTypeface_Contents());

        famousComic.setTypeface(fontSetting.getTypeface_Title());

        famousComic_title1.setTypeface(fontSetting.getTypeface_Contents());
        famousComic_title2.setTypeface(fontSetting.getTypeface_Contents());
        famousComic_title3.setTypeface(fontSetting.getTypeface_Contents());

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
                    apiURL = "https://openapi.naver.com/v1/search/book.json?query=소설&d_catg=100&sort=count&start=1&display=3";
                    break;
                case SEARCH_FOR_COMIC:
                    apiURL = "https://openapi.naver.com/v1/search/book.json?query=만화&d_catg=330&sort=count&start=1&display=3";
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
                case SEARCH_FOR_COMIC:
                    parseBufferedReaderToJson(br, SEARCH_FOR_COMIC);
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
                    case SEARCH_FOR_COMIC:
                        famousComicList.add(newBook);
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
