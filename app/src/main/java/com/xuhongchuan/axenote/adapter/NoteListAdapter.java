package com.xuhongchuan.axenote.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xuhongchuan.axenote.R;
import com.xuhongchuan.axenote.data.Note;
import com.xuhongchuan.axenote.ui.activity.ContentActivity;
import com.xuhongchuan.axenote.util.GlobalConfig;

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

    /**
     * 初始化便签
     */
    void initData() {
        Note note;
        for(int i = 0; i < 50; i++) {
            note = new Note();
            note.setContent("这是一条便签" + i);
            mData.add(note);
        }
    }

    /**
     * 获取所有便签
     * @return
     */
    public List<Note> getData() {
        return mData;
    }

    /**
     * 获取指定便签
     * @param index
     * @return
     */
    public Note getDataByIndex(int index) {
        return mData.get(index);
    }

    /**
     * 新建一条便签
     */
    public void addData() {
        Note note = new Note();
        note.setContent("");
        mData.add(0, note);
    }

    /**
     * 更新指定便签
     * @param index
     * @param value
     */
    public void updateData(int index, String value) {
        mData.get(index).setContent(value);
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
        Resources res = mContext.getResources();
        if (GlobalConfig.getInstance().isNight(mContext)) {
            holder.itemView.setBackgroundColor(res.getColor(R.color.bg_night));
        } else {
            holder.itemView.setBackgroundColor(res.getColor(R.color.bg_note));
        }
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public static class NoteListViewHolder extends RecyclerView.ViewHolder {
        TextView mTextView;

        public NoteListViewHolder(final View view) {
            super(view);
            mTextView = (TextView) view.findViewById(R.id.tv_note_tittle);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 进入便签编辑Activity，并且传递当前便签内容和索引
                    Intent intent = new Intent(view.getContext(), ContentActivity.class);
                    intent.putExtra("content", mTextView.getText().toString());
                    intent.putExtra("index", getPosition());
                    view.getContext().startActivity(intent);
                }
            });
        }
    }

}
