package cz.cvut.fit.sp.chipin.application;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.material.textfield.TextInputEditText;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginTabFragment extends Fragment {

    TextView loginText;
    EditText email;
    EditText password;
    TextView forgetPassword;
    Button login;
    float v = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.login_tab_fragment, container, false);

        loginText = root.findViewById(R.id.loginText);
        email = root.findViewById(R.id.email_login);
        password = root.findViewById(R.id.password_login);
        forgetPassword = root.findViewById(R.id.forgotPassword);
        login = root.findViewById(R.id.loginButton);

        loginText.setTranslationX(800);
        email.setTranslationX(800);
        password.setTranslationX(800);
        forgetPassword.setTranslationX(800);
        login.setTranslationX(800);

        loginText.setAlpha(v);
        email.setAlpha(v);
        password.setAlpha(v);
        forgetPassword.setAlpha(v);
        login.setAlpha(v);

        loginText.animate().translationX(0).alpha(1).setDuration(500).setStartDelay(50).start();
        email.animate().translationX(0).alpha(1).setDuration(500).setStartDelay(100).start();
        password.animate().translationX(0).alpha(1).setDuration(500).setStartDelay(150).start();
        forgetPassword.animate().translationX(0).alpha(1).setDuration(500).setStartDelay(200).start();
        login.animate().translationX(0).alpha(1).setDuration(500).setStartDelay(250).start();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                } catch (Exception ignored) {}
                loginUser();
            }
        });

        return root;
    }

    private void loginUser() {

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(email.getText().toString());
        loginRequest.setPassword(password.getText().toString());


        AuthenticationService authenticationService =
                ServiceGenerator.createService(AuthenticationService.class);
        Call<LoginResponse> loginResponseCall = authenticationService.loginUser(loginRequest);

        loginResponseCall.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                if (response.isSuccessful()) {
                    LoginResponse loginResponse = response.body();
                    AuthenticationService updateAuthInterceptor = ServiceGenerator.createService(AuthenticationService.class, loginRequest.getEmail(), loginRequest.getPassword());

                    new Handler().postDelayed(() -> startActivity(new Intent(getActivity(), DashboardActivity.class).putExtra("name", loginResponse.getName())), 0);
                    getActivity().finish();
                } else {
                    Toast.makeText(getActivity(), "Login failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(getActivity(), "Throwable" + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}