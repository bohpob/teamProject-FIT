package cz.cvut.fit.sp.chipin.application.authentication;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import cz.cvut.fit.sp.chipin.application.menu.MenuActivity;
import cz.cvut.fit.sp.chipin.application.R;
import cz.cvut.fit.sp.chipin.application.ServiceGenerator;
import cz.cvut.fit.sp.chipin.application.SessionManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginTabFragment extends Fragment {

    TextView loginText;
    TextInputLayout email_layout;
    TextInputLayout password_layout;

    TextInputEditText email;
    EditText password;
    TextView forgetPassword;
    View login;
    float v = 0;

    ProgressBar progressBar;
    TextView progressButtonName;

    AuthDataValidator authDataValidator;

    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.login_tab_fragment, container, false);

        progressBar = root.findViewById(R.id.progressBar);
        progressButtonName = root.findViewById(R.id.buttonName);
        progressButtonName.setText(R.string.login_button_text);

        authDataValidator = new AuthDataValidator();

        loginText = root.findViewById(R.id.loginText);
        email = root.findViewById(R.id.email_login);
        email_layout = root.findViewById(R.id.email_login_outer);
        password = root.findViewById(R.id.password_login);
        password_layout = root.findViewById(R.id.password_login_outer);
        forgetPassword = root.findViewById(R.id.forgotPassword);
        login = root.findViewById(R.id.loginButton);

        loginText.setTranslationX(800);
        email_layout.setTranslationX(800);
        password_layout.setTranslationX(800);
        forgetPassword.setTranslationX(800);
        login.setTranslationX(800);

        loginText.setAlpha(v);
        email_layout.setAlpha(v);
        password_layout.setAlpha(v);
        forgetPassword.setAlpha(v);
        login.setAlpha(v);

        loginText.animate().translationX(0).alpha(1).setDuration(500).setStartDelay(50).start();
        email_layout.animate().translationX(0).alpha(1).setDuration(500).setStartDelay(100).start();
        password_layout.animate().translationX(0).alpha(1).setDuration(500).setStartDelay(150).start();
        forgetPassword.animate().translationX(0).alpha(1).setDuration(500).setStartDelay(200).start();
        login.animate().translationX(0).alpha(1).setDuration(500).setStartDelay(250).start();

        login.setOnClickListener(v -> {

            login.setClickable(false);

            ProgressButton pb = new ProgressButton(v);
            pb.buttonActivated();

            try {
                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
            } catch (Exception ignored) {}

            boolean err = false;

            if (TextUtils.isEmpty(email.getText().toString().trim()))
            {
                err = true;
                email_layout.setError("Field is required");
            }
            else if (!authDataValidator.isValidEmail(email.getText().toString()))
            {
                err = true;
                email_layout.setError("Invalid email");
            }

            if (TextUtils.isEmpty(password.getText().toString().trim()))
            {
                err = true;
                password_layout.setError("Field is required");
            }
            else if (authDataValidator.containsWhitespaces(password.getText().toString()) ||
                    authDataValidator.containsColon(password.getText().toString()))
            {
                err = true;
                password_layout.setError("Invalid password format");
            }

            if (!err)
                loginUser(pb::buttonFinished);
            else {
                pb.buttonFinished();
                login.setClickable(true);
            }

        });

        root.setOnTouchListener((v, event) -> {
            InputMethodManager inputMethodManager =
                    (InputMethodManager) getActivity().getSystemService(
                            Activity.INPUT_METHOD_SERVICE);
            if(inputMethodManager.isAcceptingText()){
                inputMethodManager.hideSoftInputFromWindow(
                        getActivity().getCurrentFocus().getWindowToken(),
                        0
                );
            }
            return false;
        });

        email.setOnClickListener(v -> email_layout.setError(null));

        password.setOnClickListener(v -> password_layout.setError(null));

        email.setOnFocusChangeListener((v, hasFocus) -> email_layout.setError(null));

        password.setOnFocusChangeListener((v, hasFocus) -> password_layout.setError(null));

        return root;
    }

    private void loginUser(@NonNull Runnable pb_runnable) {

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
                    if (loginResponse != null) {
                        if (loginResponse.isEnabled()) {
                            ServiceGenerator.createService(AuthenticationService.class, loginRequest.getEmail(), loginRequest.getPassword());
                            new Handler().postDelayed(() -> startActivity(new Intent(getActivity(), MenuActivity.class)), 0);
                            SessionManager sessionManager = new SessionManager(getActivity());
                            sessionManager.createSession(loginResponse.getId(), loginResponse.getName(), loginResponse.getEmail(), loginResponse.isEnabled());
                            getActivity().finish();
                        }
                        else{
                            Toast.makeText(getActivity(), "Confirm your email first", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(getActivity(), "Login failed", Toast.LENGTH_SHORT).show();
                }

                pb_runnable.run();
                login.setClickable(true);
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(getActivity(), "Throwable" + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                pb_runnable.run();
                login.setClickable(true);
            }
        });
    }
}