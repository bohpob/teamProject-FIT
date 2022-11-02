package cz.cvut.fit.sp.chipin.application.authentication;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import cz.cvut.fit.sp.chipin.application.R;

public class ProgressButton {

    private ProgressBar progressBar;
    private TextView textView;

    private String buttonName;

    Animation fade_in;

    ProgressButton(View view) {
        progressBar = view.findViewById(R.id.progressBar);
        textView = view.findViewById(R.id.buttonName);
        buttonName = textView.getText().toString();
    }

    void buttonActivated() {
        progressBar.setVisibility(View.VISIBLE);
        textView.setText(R.string.progress_button_text);
    }

    void buttonFinished() {
        progressBar.setVisibility(View.GONE);
        textView.setText(buttonName);
    }


}
