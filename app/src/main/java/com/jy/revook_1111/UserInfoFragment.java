package com.jy.revook_1111;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserInfoFragment extends Fragment {

    /* 사용자 정보 */
    private TextView nameTextView;
    private TextView emailTextView;
    private FirebaseAuth auth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_user_info, container, false);

        Button btn_userInfo_logout = (Button) v.findViewById(R.id.btn_userInfo_logout);
        btn_userInfo_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                LoginManager.getInstance().logOut();
                getActivity().finish();
                Intent intent = new Intent(getActivity().getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

        auth = FirebaseAuth.getInstance();

        nameTextView = (TextView) v.findViewById(R.id.user_text_name);
        emailTextView = (TextView) v.findViewById(R.id.user_text_email);

        nameTextView.setText(auth.getCurrentUser().getDisplayName());
        emailTextView.setText(auth.getCurrentUser().getEmail());

        return v;
    }
}
