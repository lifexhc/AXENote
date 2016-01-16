package com.xuhongchuan.axenote.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xuhongchuan.axenote.R;
import com.xuhongchuan.axenote.data.Note;
import com.xuhongchuan.axenote.ui.activity.ContentActivity;
import com.xuhongchuan.axenote.utils.GlobalDataCache;

/**
 * Created by xuhongchuan on 15/10/17.
 */
public class NoteListAdapter extends RecyclerView.Adapter<NoteListAdapter.NoteListViewHolder> {

    private final LayoutInflater mLayoutInflater;
    private final Context mContext;

    public NoteListAdapter(Context context) {
        this.mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public NoteListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_list_item,
                parent, false);
        NoteListViewHolder holder = new NoteListViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(NoteListViewHolder holder, int position) {
        holder.mTextView.setText(GlobalDataCache.getInstance().getNotes().get(position).getContent());
    }

    @Override
    public int getItemCount() {
        return GlobalDataCache.getInstance().getNotes().size();
    }

    public static class NoteListViewHolder extends RecyclerView.ViewHolder {
        TextView mTextView;

        public NoteListViewHolder(final View view) {
            super(view);
            mTextView = (TextView) view.findViewById(R.id.tv_note_tittle);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Note note = GlobalDataCache.getInstance().getNotes().get(getPosition());
                    // 进入便签编辑Activity，并且传递当前便签内容和索引
                    Intent intent = new Intent(view.getContext(), ContentActivity.class);
                    intent.putExtra(ContentActivity.EXTRA_ID, note.getId());
                    intent.putExtra(ContentActivity.EXTRA_CONTENT, note.getContent());
                    intent.putExtra(ContentActivity.EXTRA_CREATE_TIME, note.getCreateTime());
                    intent.putExtra(ContentActivity.EXTRA_LAST_MODIFIED_TIME, note.getLastModifiedTime());
                    view.getContext().startActivity(intent);
                }
            });
        }
    }

}
