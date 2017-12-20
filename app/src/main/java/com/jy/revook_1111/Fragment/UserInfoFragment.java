package com.jy.revook_1111.Fragment;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.service.autofill.Dataset;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.login.LoginManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;
import com.jy.revook_1111.Activity.LoginActivity;
import com.jy.revook_1111.ApplicationController;
import com.jy.revook_1111.FontSetting;
import com.jy.revook_1111.R;
import com.jy.revook_1111.model.UserModel;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserInfoFragment extends Fragment {

    /* 사용자 정보 */
    private TextView nameTextView;
    private TextView emailTextView;
    private CircularImageView profileImageView;
    private Uri imageUri;
    private static final int PICK_FROM_ALBUM = 10;
    private FirebaseAuth auth;

    private String uid;
    private List<UserModel> userModels;
    private TextView textViewFollowers;
    private TextView textViewPost;
    private TextView textViewFollowings;
    private Button buttonFollowers;
    private Button buttonFollowings;
    private Button buttonPost;
    private Button buttonLogout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_user_info, container, false);

       buttonLogout = (Button) v.findViewById(R.id.userfragment_button_logout);
        buttonLogout.setOnClickListener(new View.OnClickListener() {
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
        FontSetting fontSetting = new FontSetting(getContext());
        nameTextView = (TextView) v.findViewById(R.id.userinfofragment_textview_username);
        nameTextView.setTypeface(fontSetting.typeface_Title);
        emailTextView = (TextView) v.findViewById(R.id.userinfofragment_textview_useremail);
        emailTextView.setTypeface(fontSetting.typeface_Title);
        profileImageView = (CircularImageView) v.findViewById(R.id.userinfofragment_image_profile);
        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent, PICK_FROM_ALBUM);
            }
        });
        nameTextView.setText(ApplicationController.currentUser.userName);
        emailTextView.setText(ApplicationController.currentUser.email);
        if (ApplicationController.currentUser.profileImageUrl != null)
            Glide.with(this).load(ApplicationController.currentUser.profileImageUrl).into(profileImageView);

        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        textViewFollowers = (TextView)v.findViewById(R.id.userfragment_textview_followers);
        textViewFollowers.setTypeface(fontSetting.typeface_Title);
        textViewFollowings = (TextView)v.findViewById(R.id.userfragment_textview_followings);
        textViewFollowings.setTypeface(fontSetting.typeface_Title);
        textViewPost = (TextView)v.findViewById(R.id.userfragment_textview_post);
        textViewPost.setTypeface(fontSetting.typeface_Title);
        buttonFollowers = (Button) v.findViewById(R.id.userfragment_buttton_followers);
        buttonFollowers.setTypeface(fontSetting.typeface_Contents);
        buttonFollowings = (Button) v.findViewById(R.id.userfragment_buttton_followings);
        buttonFollowings.setTypeface(fontSetting.typeface_Contents);
        buttonPost = (Button) v.findViewById(R.id.userfragment_buttton_post);
        buttonPost.setTypeface(fontSetting.typeface_Contents);
        buttonLogout.setTypeface(fontSetting.typeface_Contents);
        userModels = new ArrayList<>();
        setUserCount();

        return v;
    }

    public void setUserCount(){

        textViewFollowers.setText(String.valueOf(ApplicationController.currentUser.followerCount));
        textViewFollowings.setText(String.valueOf(ApplicationController.currentUser.followingCount));

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_FROM_ALBUM && resultCode == RESULT_OK) {
            profileImageView.setImageURI(data.getData()); // 뷰 변경
            imageUri = data.getData(); // 이미지 경로 원본
            final String uid = auth.getCurrentUser().getUid();
            if (imageUri != null) {
                FirebaseStorage.getInstance().getReference().child("userImages").child(uid).putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        String imageUrl = task.getResult().getDownloadUrl().toString();

                        UserModel userModel = new UserModel();
                        userModel.userName = auth.getCurrentUser().getDisplayName();
                        userModel.email = auth.getCurrentUser().getEmail();
                        userModel.profileImageUrl = imageUrl;
                        ApplicationController.currentUser = userModel;

                        FirebaseDatabase.getInstance().getReference().child("users").child(uid).setValue(userModel);
                    }
                });
            }
        }
    }
}
