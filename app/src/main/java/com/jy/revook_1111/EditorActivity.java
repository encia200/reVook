package com.jy.revook_1111;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

public class EditorActivity extends AppCompatActivity {

    private static final int GALLERY_CODE = 10;
    private static final int UPLOADSUCESS_CODE = 101;
    private static final int UPLOADFAIL_CODE = 100;
    private FirebaseStorage storage;
    private FirebaseAuth auth;
    private FirebaseDatabase database;
    public final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    private ImageView editor_img;
    private String imagePath;
    private EditText editor_title;
    private EditText editor_content;
    Button btn_editor_img;
    Button btn_editor_upload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        storage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        /* 권한 */
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
        }
*/
        editor_img = (ImageView) findViewById(R.id.editor_img);
        editor_title = (EditText) findViewById(R.id.editor_title);
        editor_content = (EditText) findViewById(R.id.editor_content);
        btn_editor_img = (Button) findViewById(R.id.btn_editor_img);
        btn_editor_img.setOnClickListener(new View.OnClickListener() {
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
                if (imagePath != null) {
                    upload(imagePath);
                }
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GALLERY_CODE) {

            imagePath = getPath(data.getData());
            File file = new File(getPath(data.getData()));
            Glide.with(getApplicationContext()).load(Uri.fromFile(file)).into(editor_img);
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
//        System.out.println(getPath(data.getData()));
        StorageReference storageRef = storage.getReference();

        Uri file = Uri.fromFile(new File(uri));
        StorageReference riversRef = storageRef.child("images/" + file.getLastPathSegment());
        UploadTask uploadTask = riversRef.putFile(file);

        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                setResult(UPLOADFAIL_CODE);
                Toast.makeText(getApplicationContext(),"업로드 실패",Toast.LENGTH_LONG).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getDownloadUrl();

                ImageDTO imageDTO = new ImageDTO();
                imageDTO.imageUrl = downloadUrl.toString();
                imageDTO.title = editor_title.getText().toString();
                imageDTO.description = editor_content.getText().toString();
                imageDTO.uid = auth.getCurrentUser().getUid();
                imageDTO.userId = auth.getCurrentUser().getEmail();

                database.getReference().child("images").push().setValue(imageDTO);

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
}
