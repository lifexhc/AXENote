package com.xuhongchuan.axenote.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xuhongchuan.axenote.R;
import com.xuhongchuan.axenote.dao.NoteDao;
import com.xuhongchuan.axenote.data.Note;
import com.xuhongchuan.axenote.ui.activity.ContentActivity;

/**
 * Created by xuhongchuan on 15/10/17.
 */
public class NoteListAdapter extends RecyclerView.Adapter<NoteListAdapter.NoteListViewHolder> {

    private final LayoutInflater mLayoutInflater;
    private final Context mContext;
    private NoteDao mDao;

    public NoteListAdapter(Context context) {
        this.mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        mDao = NoteDao.getInstance();
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
        holder.mTextView.setText(mDao.getAllNotes().get(position).getContent());
    }

    @Override
    public int getItemCount() {
        return mDao.getNoteCount();
    }

    public static class NoteListViewHolder extends RecyclerView.ViewHolder {
        TextView mTextView;

        public NoteListViewHolder(final View view) {
            super(view);
            mTextView = (TextView) view.findViewById(R.id.tv_note_tittle);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NoteDao dao = NoteDao.getInstance();
                    Note note = dao.getAllNotes().get(getPosition());
                    // 进入便签编辑Activity，并且传递当前便签内容和索引
                    Intent intent = new Intent(view.getContext(), ContentActivity.class);
                    intent.putExtra("id", note.getId());
                    intent.putExtra("content", note.getContent());
                    intent.putExtra("createTime", note.getCreateTime());
                    intent.putExtra("updateTime", note.getUpdateTime());
                    view.getContext().startActivity(intent);
                }
            });
        }
    }

}
