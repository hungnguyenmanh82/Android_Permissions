package com.hung.phonepermission;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // sdk = 23 => Android 6.0, check permission at runtime
        if (Build.VERSION.SDK_INT >= 23) {
            askForPermissions(new String[]{
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
//                            Manifest.permission.READ_LOGS,
                            Manifest.permission.READ_CONTACTS,
                            Manifest.permission.CAMERA,
                            Manifest.permission.SEND_SMS,
                            Manifest.permission.INTERNET
                    },//for both GPS and Network Provider
                    REQUEST_PERMISSIONS_CODE);


        }
    }

    static final int REQUEST_PERMISSIONS_CODE = 1982;
    static final int REQUEST_PERMISSIONS_CODE2 = 1983;

    private void askForPermission2(){
        // kiểm tra permission đc cấp hay chưa
        if(ActivityCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS)
                !=PackageManager.PERMISSION_GRANTED){

            //check nếu hộp thoại này đc gọi lần thứ 2 trở đi thì hiển thị thông báo nhắc nhở
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_CONTACTS)){
                final AlertDialog.Builder dlgBuilder = new AlertDialog.Builder(this);
                dlgBuilder.setTitle("this permission is very important ")
                        .setMessage("The Application need this permission to run. Are you sure You want to deny this permission?")
                        .setPositiveButton("I'M SURE", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // ko goi request permision nưa
                                Toast.makeText(MainActivity.this,"I'M SURE is clicked", Toast.LENGTH_LONG).show();
                            }
                        })
                        .setNegativeButton("RE-TRY", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog
                                Toast.makeText(MainActivity.this,"request READ_CONTACT", Toast.LENGTH_LONG).show();
                                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_PERMISSIONS_CODE2);
                            }
                        });
                dlgBuilder.create().show();
            }else{
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_PERMISSIONS_CODE2);
            }
        }
    }

    protected final void askForPermissions(String[] permissions, int requestCode) {
        List<String> permissionsToRequest = new ArrayList<>();

        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(this, permission) !=
                    PackageManager.PERMISSION_GRANTED) {
                //permision which have not been granted need to be request again
                permissionsToRequest.add(permission);
            }
        }
        if (!permissionsToRequest.isEmpty()) {
            //nếu gửi 2 lệnh request liên tiếp thì, request đầu tiên sẽ đc xử lý, request sau sẽ bị loại bỏ
            // vì thế chỉ gửi từng request 1 thôi
            // nếu có nhiều permision thì phải đưa chúng vào Array cho 1 request thôi (dã test)
            ActivityCompat.requestPermissions(this, permissionsToRequest.toArray(new
                    String[permissionsToRequest.size()]), requestCode);
        }
    }

    /*
    * Permission on for UI (Activity, Fragment). Service can not receive the response from the OS
    * */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_PERMISSIONS_CODE) {//dùng chung permission code hoặc riêng thì tùy
            //check xem permission nào đc granted/deny
            for (int i = 0; i < permissions.length; i++) {
                String permission = permissions[i];
                int grantResult = grantResults[i];

                if (permission.equals(Manifest.permission.READ_CONTACTS)) {
                    if (grantResult == PackageManager.PERMISSION_GRANTED) {
                        //
                    } else {
                        //
                        askForPermission2();
                    }
                }
            }
        }

    }
}
