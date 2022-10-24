package cz.cvut.fit.sp.chipin.application;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class LoginTabFragment extends Fragment {

    TextView loginText;
    TextView email;
    TextView password;
    TextView forgetPassword;
    Button login;
    Float v = 0f;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.login_tab_fragment, container, false);

        loginText = root.findViewById(R.id.loginText);
        email = root.findViewById(R.id.email);
        password = root.findViewById(R.id.password);
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

        return root;
    }
}