package org.revengeos.ota;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

class ChangeAdapter extends RecyclerView.Adapter<ChangeAdapter.ViewHolder> {

    ArrayList<String> changelog;

    public ChangeAdapter(ArrayList<String> mchangelog) {
        changelog = mchangelog;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.mlog.setText((CharSequence) changelog.get(position));
    }

    @Override
    public int getItemCount() {
        return changelog.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView mlog;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mlog = itemView.findViewById(R.id.log);

        }
    }
}
