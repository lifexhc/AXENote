package com.xuhongchuan.axenote.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xuhongchuan.axenote.R;
import com.xuhongchuan.axenote.data.Note;
import com.xuhongchuan.axenote.util.L;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuhongchuan on 15/10/17.
 */
public class NoteListAdapter extends RecyclerView.Adapter<NoteListAdapter.NoteListViewHolder> {

    private final LayoutInflater mLayoutInflater;
    private final Context mContext;

    public NoteListAdapter(Context context) {
        this.mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        initData();
    }

    private List<Note> mData = new ArrayList<Note>(); // 测试数据

    void initData() {
        Note note;
        for(int i = 0; i < 50; i++) {
            note = new Note();
            note.setContent("这是一条便签" + i);
            mData.add(note);
        }
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
        holder.mTextView.setText(mData.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public static class NoteListViewHolder extends RecyclerView.ViewHolder {
        TextView mTextView;

        public NoteListViewHolder(View view) {
            super(view);
            mTextView = (TextView) view.findViewById(R.id.tv_note_tittle);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    L.d(this, "onClick--> position = " + getPosition());
                }
            });
        }
    }

}
