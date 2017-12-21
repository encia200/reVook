package com.jy.revook_1111.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jy.revook_1111.ApplicationController;
import com.jy.revook_1111.Data.ReviewDTO;
import com.jy.revook_1111.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;

public class EditorActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {

    private static final int GALLERY_CODE = 10;
    private static final int UPLOADSUCESS_CODE = 101;
    private static final int UPLOADFAIL_CODE = 100;
    private FirebaseStorage storage;
    private FirebaseAuth auth;
    private FirebaseDatabase database;
    public final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    private ImageView editor_img;
    private EditText editor_content_date;
    private EditText editor_content_watermark;
    boolean isdrawable = false;
    byte[] data;
    private EditText editor_content_edittext;
    private Button btn_toolbar_img;
    private Button btn_editor_upload;
    public ImagePathVariable imagePathVariable;
    private RelativeLayout editor_content;
    private AssetManager assetManager;
    TextView fontsizeTextview;
    public int seekProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        fontsizeTextview = (TextView) findViewById(R.id.editoractivity_fontsize_textview);
        SeekBar editoractivity_seekbar = (SeekBar) findViewById(R.id.editoractivity_fontsize_seekbar);
        editoractivity_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStopTrackingTouch(SeekBar seekBar) {
                seekProgress = seekBar.getProgress();
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                seekProgress = seekBar.getProgress();
            }

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seekProgress = progress;
                fontsizeTextview.setText("" + seekProgress);
                editor_content_edittext.setTextSize(seekProgress);
            }
        });


        ImageView bg_color_yellow = (ImageView) findViewById(R.id.bg_dialog_color_yellow);
        bg_color_yellow.setOnClickListener(this);
        ImageView bg_color_brown = (ImageView) findViewById(R.id.bg_dialog_color_brown);
        bg_color_brown.setOnClickListener(this);
        ImageView bg_color_mint = (ImageView) findViewById(R.id.bg_dialog_color_mint);
        bg_color_mint.setOnClickListener(this);
        ImageView bg_color_lightnavy = (ImageView) findViewById(R.id.bg_dialog_color_lightnavy);
        bg_color_lightnavy.setOnClickListener(this);
        ImageView bg_color_navy = (ImageView) findViewById(R.id.bg_dialog_color_navy);
        bg_color_navy.setOnClickListener(this);
        ImageView bg_color_gray = (ImageView) findViewById(R.id.bg_dialog_color_gray);
        bg_color_gray.setOnClickListener(this);

        assetManager = getApplicationContext().getResources().getAssets();
        ImageView font_dxsaenal_bold = (ImageView) findViewById(R.id.editoractivity_font_dxsaenal_bold);
        font_dxsaenal_bold.setOnClickListener(this);
        ImageView font_typo_decosolidfill = (ImageView) findViewById(R.id.editoractivity_font_typo_decosolidfill);
        font_typo_decosolidfill.setOnClickListener(this);
        ImageView font_chungchunsidae_r = (ImageView) findViewById(R.id.editoractivity_font_chungchunsidae_r);
        font_chungchunsidae_r.setOnClickListener(this);
        ImageView font_nanumpen = (ImageView) findViewById(R.id.editoractivity_font_nanumpen);
        font_nanumpen.setOnClickListener(this);
        ImageView font_timemachine = (ImageView) findViewById(R.id.editoractivity_font_timemachine);
        font_timemachine.setOnClickListener(this);
        ImageView font_typo_ssangmundongb = (ImageView) findViewById(R.id.editoractivity_font_typo_ssangmundongb);
        font_typo_ssangmundongb.setOnClickListener(this);

        editor_content = (RelativeLayout) findViewById(R.id.editoractivity_editor_content);
        editor_img = (ImageView) findViewById(R.id.editoractivity_editor_img);


        /*imagePathVariable = new ImagePathVariable();
        imagePathVariable.setImagePath(Uri.parse("android.resource://" + "com.jy.revook_1111" + "/drawable/dialog_background_gray").toString());

        imagePathVariable.setListener(new ImagePathVariable.ChangeListener() {
            @Override
            public void onChange() {
                Glide.with(getApplicationContext()).load(imagePathVariable.imagePath).into(editor_img);
                editor_img.setImageURI(Uri.parse(imagePathVariable.imagePath));
                editor_img.setDrawingCacheEnabled(true);
                editor_img.buildDrawingCache();
                Bitmap bitmap = editor_img.getDrawingCache();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                data = baos.toByteArray();
                isdrawable = true;
            }
        });*/
        Intent intent = getIntent();
        imagePathVariable = new ImagePathVariable();
        imagePathVariable.setImagePath(intent.getStringExtra("image"));
        imagePathVariable.setListener(new ImagePathVariable.ChangeListener() {
            @Override
            public void onChange() {

                /*Glide.with(getApplicationContext()).load(imagePathVariable.imagePath).into(editor_img);
                editor_img.setImageURI(Uri.parse(imagePathVariable.imagePath));
                editor_img.setDrawingCacheEnabled(true);
                editor_img.buildDrawingCache();
                Bitmap bitmap = editor_img.getDrawingCache();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                data = baos.toByteArray();
                isdrawable = true;*/


                ImageLoader imageLoader = ImageLoader.getInstance();
                int defaultImage = getApplicationContext().getResources().getIdentifier("@drawable/nobookimg", null, getApplicationContext().getPackageName());
                DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
                        .cacheOnDisc(true).resetViewBeforeLoading(true)
                        .showImageForEmptyUri(defaultImage)
                        .showImageOnFail(defaultImage)
                        .showImageOnLoading(defaultImage).build();
                imageLoader.displayImage(imagePathVariable.imagePath, editor_img, options, new ImageLoadingListener() {
                            @Override
                            public void onLoadingStarted(String imageUri, View view) {
                            }

                            @Override
                            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                            }

                            @Override
                            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                                editor_img.setColorFilter(Color.rgb(155, 155, 155), android.graphics.PorterDuff.Mode.MULTIPLY);
                            }

                            @Override
                            public void onLoadingCancelled(String imageUri, View view) {
                            }
                        }
                );
            }
        });

        imagePathVariable.setImagePath(intent.getStringExtra("image"));
        storage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        //editor_title = (EditText) findViewById(R.id.editor_title);
        editor_content_edittext = (EditText) findViewById(R.id.editoractivity_editor_edittext);
        editor_content_edittext.setOnTouchListener(this);
        editor_content_date = (EditText) findViewById(R.id.editoractivity_editor_datetext);
        SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd");
        editor_content_date.setText(mFormat.format(new Date(System.currentTimeMillis())));
        editor_content_date.setOnTouchListener(this);
        editor_content_watermark = (EditText) findViewById(R.id.editoractivity_editor_watermarktext);
        StringTokenizer tokens = new StringTokenizer(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        editor_content_watermark.setText(tokens.nextToken("@"));
        editor_content_watermark.setOnTouchListener(this);
        /*btn_toolbar_background = (Button) findViewById(R.id.editoractivity_btn_background_color_select);
        btn_toolbar_background.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditorBackgroundDialog bgdialog = new EditorBackgroundDialog(EditorActivity.this, imagePathVariable);
                bgdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                bgdialog.setCancelable(true);
                bgdialog.show();
            }
        });*/
        btn_toolbar_img = (Button) findViewById(R.id.btn_editor_img);
        btn_toolbar_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);

                if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(EditorActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {

                        new AlertDialog.Builder(EditorActivity.this)
                                .setTitle("Request Permission Rationale")
                                .setMessage("앱 실행을 위해서는 저장공간 접근을 허용해야 합니다..")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int which) {
                                        ActivityCompat.requestPermissions(EditorActivity.this,
                                                new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                                                MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                                    }
                                }).show();
                    } else {
                        ActivityCompat.requestPermissions(EditorActivity.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                    }
                }

                if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                    startActivityForResult(intent, GALLERY_CODE);
            }
        });
        btn_editor_upload = (Button) findViewById(R.id.btn_editor_upload);
        btn_editor_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imagePathVariable.imagePath == null) {
                    imagePathVariable.setImagePath(Uri.parse("android.resource://" + "com.jy.revook_1111" + "/drawable/dialog_background_gray").toString());
                    editor_img.setImageResource(R.drawable.dialog_background_gray);
                }
                upload(imagePathVariable.imagePath);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GALLERY_CODE) {
            imagePathVariable.imagePath = getPath(data.getData());
            File file = new File(getPath(data.getData()));
            Glide.with(getApplicationContext()).load(file).into(editor_img);
            editor_img.setColorFilter(Color.rgb(155, 155, 155), android.graphics.PorterDuff.Mode.MULTIPLY);
            isdrawable = false;
        }
    }

    public String getPath(Uri uri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader cursorLoader = new CursorLoader(this, uri, proj, null, null, null);

        Cursor cursor = cursorLoader.loadInBackground();
        int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        cursor.moveToFirst();

        return cursor.getString(index);
    }

    private void upload(String uri) {
        editor_content_edittext.setCursorVisible(false);
        editor_content.setDrawingCacheEnabled(true);
        editor_content.buildDrawingCache();
        Bitmap bitmap = editor_content.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        data = baos.toByteArray();


//        System.out.println(getPath(data.getData()));
        StorageReference storageRef = storage.getReference();

        Uri file = Uri.fromFile(new File(uri));
        StorageReference riversRef = storageRef.child("images/" + file.getLastPathSegment());
        UploadTask uploadTask;
        //if (isdrawable)
        uploadTask = riversRef.putBytes(data);
        /*else
            uploadTask = riversRef.putFile(file);*/
        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                setResult(UPLOADFAIL_CODE);
                Toast.makeText(getApplicationContext(), "업로드 실패", Toast.LENGTH_LONG).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getDownloadUrl();

                final ReviewDTO reviewDTO = new ReviewDTO();
                reviewDTO.imageUrl = downloadUrl.toString();
                //reviewDTO.title = editor_title.getText().toString();
                reviewDTO.content = editor_content_edittext.getText().toString();
                reviewDTO.uid = auth.getCurrentUser().getUid();
                reviewDTO.userName = auth.getCurrentUser().getDisplayName();

                database.getReference().child("reviews").push().setValue(reviewDTO, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        if (databaseError != null) {
                            System.out.println("Data could not be saved " + databaseError.getMessage());
                        } else {
                            reviewDTO.reviewkey = databaseReference.getKey();
                            database.getReference().child("reviews").child(reviewDTO.reviewkey).child("reviewkey").setValue(reviewDTO.reviewkey);
                            ApplicationController.currentUser.reviews.put(reviewDTO.reviewkey, true);
                            database.getReference().child("users").child(ApplicationController.currentUser.uid).child("reviewCount").setValue(ApplicationController.currentUser.reviewCount = ApplicationController.currentUser.reviewCount + 1);
                            database.getReference().child("users").child(ApplicationController.currentUser.uid).child("reviews").setValue(ApplicationController.currentUser.reviews);
                        }
                    }
                });

                setResult(UPLOADSUCESS_CODE);
                finish();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "저장공간 접근 권한을 사용자가 승인함.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "저장공간 접근 권한 거부됨.", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bg_dialog_color_yellow:
                imagePathVariable.setImagePath(Uri.parse("android.resource://" + "com.jy.revook_1111" + "/drawable/bg_yellow").toString());
                break;
            case R.id.bg_dialog_color_brown:
                imagePathVariable.setImagePath(Uri.parse("android.resource://" + "com.jy.revook_1111" + "/drawable/bg_brown").toString());
                break;
            case R.id.bg_dialog_color_mint:
                imagePathVariable.setImagePath(Uri.parse("android.resource://" + "com.jy.revook_1111" + "/drawable/bg_mint").toString());
                break;
            case R.id.bg_dialog_color_lightnavy:
                imagePathVariable.setImagePath(Uri.parse("android.resource://" + "com.jy.revook_1111" + "/drawable/bg_lightnavy").toString());
                break;
            case R.id.bg_dialog_color_navy:
                imagePathVariable.setImagePath(Uri.parse("android.resource://" + "com.jy.revook_1111" + "/drawable/bg_navy").toString());
                break;
            case R.id.bg_dialog_color_gray:
                imagePathVariable.setImagePath(Uri.parse("android.resource://" + "com.jy.revook_1111" + "/drawable/bg_gray").toString());
                break;
            case R.id.editoractivity_font_dxsaenal_bold:
                editor_content_edittext.setTypeface(Typeface.createFromAsset(assetManager, "dxsaenal_bold.ttf"));
                break;
            case R.id.editoractivity_font_typo_decosolidfill:
                editor_content_edittext.setTypeface(Typeface.createFromAsset(assetManager, "typo_decosolidfill.ttf"));
                break;
            case R.id.editoractivity_font_chungchunsidae_r:
                editor_content_edittext.setTypeface(Typeface.createFromAsset(assetManager, "chungchunsidae_r.ttf"));
                break;
            case R.id.editoractivity_font_nanumpen:
                editor_content_edittext.setTypeface(Typeface.createFromAsset(assetManager, "nanumpen.ttf"));
                break;
            case R.id.editoractivity_font_timemachine:
                editor_content_edittext.setTypeface(Typeface.createFromAsset(assetManager, "timemachine.ttf"));
                break;
            case R.id.editoractivity_font_typo_ssangmundongb:
                editor_content_edittext.setTypeface(Typeface.createFromAsset(assetManager, "typo_ssangmundongb.ttf"));
                break;

        }
    }

    private int xDelta, yDelta;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int width = ((ViewGroup) v.getParent()).getWidth() - v.getWidth();
        int height = ((ViewGroup) v.getParent()).getHeight() - v.getHeight();
        final int X = (int) event.getRawX();
        final int Y = (int) event.getRawY();
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                xDelta = (int) (X - v.getTranslationX());
                yDelta = (int) (Y - v.getTranslationY());
                break;
            case MotionEvent.ACTION_MOVE:
                v.setTranslationX(X - xDelta);
                v.setTranslationY(Y - yDelta);
                break;
        }
        return false;
    }


    public static class ImagePathVariable {
        public String imagePath;
        private ChangeListener listener;

        public void setListener(ChangeListener listener) {
            this.listener = listener;
        }

        public void setImagePath(String imagePath) {
            this.imagePath = imagePath;
            if (listener != null) listener.onChange();
        }

        public interface ChangeListener {
            void onChange();
        }
    }
}