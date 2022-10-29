package cz.cvut.fit.sp.chipin.application;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpTabFragment extends Fragment {

    TextView signupText;
    TextInputEditText name;
    TextInputEditText email;
    TextInputEditText password;
    TextInputLayout name_layout;
    TextInputLayout email_layout;
    TextInputLayout password_layout;
    Button signUp;
    float v = 0;
    private AuthDataValidator authDataValidator;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.register_tab_fragment, container, false);

        authDataValidator = new AuthDataValidator();

        signupText = root.findViewById(R.id.signupText);
        name = root.findViewById(R.id.name_register);
        name_layout = root.findViewById(R.id.name_register_outer);
        email = root.findViewById(R.id.email_register);
        email_layout = root.findViewById(R.id.email_register_outer);
        password = root.findViewById(R.id.password_register);
        password_layout = root.findViewById(R.id.password_register_outer);
        signUp = root.findViewById(R.id.signupButton);

        signupText.setTranslationX(800);
        name_layout.setTranslationX(800);
        email_layout.setTranslationX(800);
        password_layout.setTranslationX(800);
        signUp.setTranslationX(800);

        signupText.setAlpha(v);
        name_layout.setAlpha(v);
        email_layout.setAlpha(v);
        password_layout.setAlpha(v);
        signUp.setAlpha(v);

        signupText.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(100).start();
        name_layout.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(300).start();
        email_layout.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        password_layout.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(700).start();
        signUp.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(900).start();


        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (authDataValidator.containsWhitespaces(s)) {
                    password_layout.setError("Password cannot contain whitespaces");
                }
                else if (authDataValidator.containsColon(s)) {
                    password_layout.setError("Invalid password format");
                }
                else if (!authDataValidator.containsLetterDigitAndSpecialSymbol(s)) {
                    password_layout.setError("At least 1 digit, 1 letter and 1 special character");
                }
                else if (!authDataValidator.minimumPasswordLength(s)) {
                    password_layout.setError("Password must contain at least 8 characters");
                }
                else if (!authDataValidator.maximumPasswordLength(s)) {
                    password_layout.setError("Password must contain maximum 64 characters");
                }
                else {
                    password_layout.setError(null);
                }
            }
        });

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                if (!authDataValidator.isValidEmail(s))
                    email_layout.setError("Email is not valid");
                else
                    email_layout.setError(null);
            }
        });

        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                if (!authDataValidator.isValidName(s))
                    name_layout.setError("Field is required");
                else
                    name_layout.setError(null);
            }
        });


        signUp.setOnClickListener(v -> {
            boolean err = false;
            if (!authDataValidator.isValidName(name.getText().toString())) {
                name_layout.setError("Field is required");
                err = true;
            }
            if (!authDataValidator.isValidEmail(email.getText().toString()))
            {
                err = true;
                email_layout.setError("Invalid email");
            }
            if (TextUtils.isEmpty(password.getText().toString().trim()))
            {
                err = true;
                password_layout.setError("Field is required");
            }
            else if (!authDataValidator.isValidPassword(password.getText().toString()))
            {
                err = true;
                password_layout.setError("Weak password");
            }
            if (!err)
                registerUser();
        });

        return root;
    }

    public void registerUser() {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setName(name.getText().toString());
        registerRequest.setEmail(email.getText().toString());
        registerRequest.setPassword(password.getText().toString());

        AuthenticationService authenticationService = ServiceGenerator.createService(AuthenticationService.class);
        Call<String> registerResponse = authenticationService.registerUser(registerRequest);
        registerResponse.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NotNull Call<String> call, @NotNull Response<String> response) {
                String message;
                if (response.isSuccessful()){
                    new Handler().postDelayed(() -> startActivity(new Intent(getActivity(), ConfirmEmailInfoActivity.class)), 0);
                    getActivity().finish();
                } else {
                    message = "Ann error occurred, please try again later...";
                    Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<String> call, @NotNull Throwable t) {
                String message = t.getLocalizedMessage();
                Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
            }
        });
    }

}