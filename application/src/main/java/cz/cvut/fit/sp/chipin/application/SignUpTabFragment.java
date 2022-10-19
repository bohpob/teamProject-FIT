package cz.cvut.fit.sp.chipin.application;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class SignUpTabFragment extends Fragment {

    TextView signupText;
    TextView name;
    TextView email;
    TextView password;
    Button signUp;
    float v = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.register_tab_fragment, container, false);

        signupText = root.findViewById(R.id.signupText);
        name = root.findViewById(R.id.name);
        email = root.findViewById(R.id.email);
        password = root.findViewById(R.id.password);
        signUp = root.findViewById(R.id.signupButton);

        signupText.setTranslationX(800);
        name.setTranslationX(800);
        email.setTranslationX(800);
        password.setTranslationX(800);
        signUp.setTranslationX(800);

        signupText.setAlpha(v);
        name.setAlpha(v);
        email.setAlpha(v);
        password.setAlpha(v);
        signUp.setAlpha(v);

        signupText.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(100).start();
        name.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(300).start();
        email.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        password.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(700).start();
        signUp.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(900).start();

        return root;
    }

}