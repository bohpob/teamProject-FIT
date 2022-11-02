package cz.cvut.fit.sp.chipin.application.menu;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import cz.cvut.fit.sp.chipin.application.R;
import org.jetbrains.annotations.NotNull;

public class GroupsAdapter extends RecyclerView.Adapter<GroupsAdapter.GroupsViewHolder> {

    private int itemsCount;

    public GroupsAdapter(int count) {
        itemsCount = count;
    }

    public class GroupsViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public GroupsViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.groupElementStart);
        }
    }

    @NonNull
    @NotNull
    @Override
    public GroupsViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_element, parent, false);
        return new GroupsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull GroupsViewHolder holder, int position) {
        Animation animation = AnimationUtils.loadAnimation(holder.itemView.getContext(), android.R.anim.slide_in_left);
        holder.textView.setText(String.valueOf(position));
        holder.itemView.startAnimation(animation);
    }

    @Override
    public int getItemCount() {
        return itemsCount;
    }

}
