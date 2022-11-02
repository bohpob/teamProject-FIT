package cz.cvut.fit.sp.chipin.application;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.POST;

public interface AuthenticationService {

    @POST("login")
    Call<LoginResponse> loginUser (@Body LoginRequest loginRequest);

    @POST("registration")
    Call<String> registerUser (@Body RegisterRequest RegisterRequest);
}
