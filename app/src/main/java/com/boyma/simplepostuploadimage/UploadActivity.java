package com.boyma.simplepostuploadimage;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UploadActivity extends AppCompatActivity {

    private ImageView imgPreview;
    private Button btnUpload;
    private File uploadfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        uploadfile = new File(getIntent().getStringExtra("filePath"));

        initUI();
    }

    private void initUI() {
        //txtPercentage = (TextView) findViewById(R.id.txtPercentage);
        btnUpload = findViewById(R.id.btnUpload);
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //upload();
                uplaod2();
            }
        });
        //progressBar = (ProgressBar) findViewById(R.id.progressBar);
        imgPreview = findViewById(R.id.imgPreview);
        setImage();
    }

    private void uplaod2() {
        IUploadService service = ServiceGenerator.createService(IUploadService.class);

        // https://github.com/iPaulPro/aFileChooser/blob/master/aFileChooser/src/com/ipaulpro/afilechooser/utils/FileUtils.java
        // use the FileUtils to get the actual file by uri

        // create RequestBody instance from file
        RequestBody requestFile = RequestBody.create(
                MediaType.parse("image/*"),
                uploadfile
        );

        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body = MultipartBody.Part.createFormData("image", uploadfile.getName(), requestFile);

        // add another part within the multipart request
        String descriptionString = "hello, this is description speaking";
        RequestBody description = RequestBody.create(MediaType.parse("text/plain"), descriptionString);

        // finally, execute the request
        Call<ResponseBody> call = service.postImage(body,description);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.v("Upload", "success");
                System.out.println(response.toString());
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Upload error:", t.getMessage());
            }
        });
    }

    /*private void upload() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        // Change base URL to your upload server URL.

        IUploadService service = new Retrofit.Builder().baseUrl("http://192.168.1.14/").addConverterFactory(GsonConverterFactory.create()).client(client).build().create(IUploadService.class);

        RequestBody reqFile = RequestBody.create(MediaType.parse("image/png"), uploadfile);
        MultipartBody.Part body = MultipartBody.Part.createFormData("upload", uploadfile.getName(), reqFile);

        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), "upload_test");


        retrofit2.Call<okhttp3.ResponseBody> req = service.postImage(body, name);
        req.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                // Do Something
                System.out.println(response.toString());
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }*/

    private void setImage() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 8;

        System.out.println(uploadfile.getPath());
        final Bitmap bitmap = BitmapFactory.decodeFile(uploadfile.getPath(), options);
        imgPreview.setImageBitmap(bitmap);
    }
}
