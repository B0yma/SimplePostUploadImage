package com.boyma.simplepostuploadimage;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface IUploadService {
    @Multipart
    @POST("AndroidFileUpload/fileUpload.php")
    Call<ResponseBody> postImage(@Part MultipartBody.Part file, @Part("description") RequestBody description);
}
