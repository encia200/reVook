package com.jy.revook_1111.Fragment;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;
import com.jy.revook_1111.Activity.LoginActivity;
import com.jy.revook_1111.ApplicationController;
import com.jy.revook_1111.R;
import com.jy.revook_1111.model.UserModel;
import com.mikhaellopez.circularimageview.CircularImageView;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_user_info, container, false);

        Button btn_userInfo_logout = (Button) v.findViewById(R.id.userinfofragment_btn_logout);
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
        nameTextView = (TextView) v.findViewById(R.id.userinfofragment_textview_username);
        emailTextView = (TextView) v.findViewById(R.id.userinfofragment_textview_useremail);
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
        return v;
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
