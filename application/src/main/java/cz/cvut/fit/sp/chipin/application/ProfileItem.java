package cz.cvut.fit.sp.chipin.application;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ProfileItem extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.item_profile, container, false);

        TextView id, name, email;

        id = root.findViewById(R.id.profile_item_id);
        name = root.findViewById(R.id.profile_item_name);
        email = root.findViewById(R.id.profile_item_email);

        SessionManager sessionManager = new SessionManager(getActivity());

        String id_text = "Id: " + sessionManager.getId();
        id.setText(id_text);
        name.setText(sessionManager.getName());
        email.setText(sessionManager.getEmail());

        return root;
    }

}
