package com.jy.revook_1111.Activity;


import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.jy.revook_1111.ApplicationController;
import com.jy.revook_1111.R;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {
    private static final int RC_SIGN_IN = 10; // 구글 로그인 코드
    private static final int SIGN_UP_ACTIVITY = 20; // 회원가입 액티비티 코드
    private static final int SIGN_UP_SUCCESS = 30; // 회원가입 성공 코드

    private GoogleApiClient mGoogleApiClient;

    private EditText editTextEmail;
    private EditText editTextPassword;

    private CallbackManager mCallbackManager;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private LoginButton btn_facebook_login;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mFirebaseAuth = FirebaseAuth.getInstance();

        // 원격 설정
        FirebaseRemoteConfig mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        String splash_background = mFirebaseRemoteConfig.getString(getString(R.string.rc_color));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.parseColor(splash_background));
        }

        // 구글 로그인
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        Button btn_google_login = (Button) findViewById(R.id.loginactivity_btn_google_login);
        btn_google_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });
        // 회원가입 이동
        Button btn_loginactivity_signup = (Button) findViewById(R.id.loginactivity_btn_email_signup);
        btn_loginactivity_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(LoginActivity.this, SignupActivity.class), SIGN_UP_ACTIVITY);
            }
        });
        // 이메일 로그인
        editTextEmail = (EditText) findViewById(R.id.loginactivity_edittext_email);
        editTextPassword = (EditText) findViewById(R.id.loginactivity_edittext_password);
        final Button btn_email_login = (Button) findViewById((R.id.loginactivity_btn_email_login));
        btn_email_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser(editTextEmail.getText().toString(), editTextPassword.getText().toString());
            }
        });
        // 페이스북 로그인
        ImageView fakeFacebook = (ImageView) findViewById(R.id.fake_facebook);
        fakeFacebook.setOnClickListener(LoginActivity.this);
        mCallbackManager = CallbackManager.Factory.create();
        btn_facebook_login = (LoginButton) findViewById(R.id.loginactivity_btn_facebook_login);
        btn_facebook_login.setReadPermissions("email", "public_profile");
        btn_facebook_login.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException error) {
            }
        });

        // 로그인 인터페이스 리스너
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("uid", user.getUid());
                    startActivity(intent);
                    finish();
                } else {

                }
            }
        };
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fake_facebook:  //fake_naver 내 버튼을 눌렀을 경우
                btn_facebook_login.performClick(); //performClick 클릭을 실행하게 만들어 자동으로 실행되도록 한다.
                break;
        }
    }

    private void handleFacebookAccessToken(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            final String uid = task.getResult().getUser().getUid();

                            FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("userName").setValue(mFirebaseAuth.getCurrentUser().getDisplayName());
                            FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("email").setValue(mFirebaseAuth.getCurrentUser().getEmail());

                            FirebaseDatabase.getInstance().getReference().child("users").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    ApplicationController.currentUser.userName = dataSnapshot.child("userName").getValue().toString();
                                    ApplicationController.currentUser.email = dataSnapshot.child("email").getValue().toString();
                                    if (dataSnapshot.child("profileImage").getValue() != null) {
                                        ApplicationController.currentUser.profileImageUrl = dataSnapshot.child("profileImage").getValue().toString();
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                }
                            });
                        }
                    }
                });
    }

    private void loginUser(String email, String password) {
        mFirebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "이메일 혹은 비밀번호를 확인하세요.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(LoginActivity.this, "이메일 로그인 완료", Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);

            } else {
                // Google Sign In failed, update UI appropriately
                // ...
            }
        } else if (requestCode == SIGN_UP_ACTIVITY && resultCode == SIGN_UP_SUCCESS) {
            editTextEmail.setText(data.getStringExtra("email"));
            editTextPassword.setText(data.getStringExtra("password"));
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mFirebaseAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                } else { // 로그인 성공 후
                    final String uid = task.getResult().getUser().getUid();

                    FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("userName").setValue(mFirebaseAuth.getCurrentUser().getDisplayName());
                    FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("email").setValue(mFirebaseAuth.getCurrentUser().getEmail());

                    FirebaseDatabase.getInstance().getReference().child("users").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            ApplicationController.currentUser.userName = dataSnapshot.child("userName").getValue().toString();
                            ApplicationController.currentUser.email = dataSnapshot.child("email").getValue().toString();
                            if (dataSnapshot.child("profileImage").getValue() != null) {
                                ApplicationController.currentUser.profileImageUrl = dataSnapshot.child("profileImage").getValue().toString();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    @Override
    protected void onStart() {
        super.onStart();
        mFirebaseAuth.addAuthStateListener(mAuthListener); // 귀 붙이기
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient != null) {
            mFirebaseAuth.removeAuthStateListener(mAuthListener); // 귀 떼기
        }
    }
}