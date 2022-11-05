package cz.cvut.fit.sp.chipin.application.menu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import cz.cvut.fit.sp.chipin.application.R;
import cz.cvut.fit.sp.chipin.application.SessionManager;

public class ProfileItem extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.item_profile, container, false);

        SessionManager sessionManager = new SessionManager(getActivity());


        TextView name = root.findViewById(R.id.profile_name);
        TextView email = root.findViewById(R.id.profile_email);
        TextView menu_name = root.findViewById(R.id.profile_real_name_textview);
        TextView menu_email = root.findViewById(R.id.profile_real_email_textview);

        name.setText(sessionManager.getName());
        menu_name.setText(sessionManager.getName());
        email.setText(sessionManager.getEmail());
        menu_email.setText(sessionManager.getEmail());

        return root;
    }

}
