package cz.cvut.fit.sp.chipin.application;

import android.os.Bundle;
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.register_tab_fragment, container, false);

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

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = "Sign Up";
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

                RegisterRequest registerRequest = new RegisterRequest();
                EditText name = root.findViewById(R.id.name_register);
                registerRequest.setName(name.getText().toString());
                EditText email = root.findViewById(R.id.email_register);
                registerRequest.setEmail(email.getText().toString());
                EditText password = root.findViewById(R.id.password_register);
                registerRequest.setPassword(password.getText().toString());
                registerUser(registerRequest);
            }
        });

        return root;
    }

    public void registerUser(RegisterRequest registerRequest) {
        AuthenticationService authenticationService = ServiceGenerator.createService(AuthenticationService.class);
        Call<String> registerResponse = authenticationService.registerUser(registerRequest);
        registerResponse.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NotNull Call<String> call, @NotNull Response<String> response) {
                String message;
                if (response.isSuccessful()){
                    message = "Successful...";
                } else {
                    message = "Ann error occurred, please try again later...";
                }
                Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(@NotNull Call<String> call, @NotNull Throwable t) {
                String message = t.getLocalizedMessage();
                Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
            }
        });
    }

}