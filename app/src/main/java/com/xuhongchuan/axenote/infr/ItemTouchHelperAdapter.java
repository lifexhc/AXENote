package com.xuhongchuan.axenote.infr;

/**
 * Created by xuhongchuan on 16/5/6.
 */
public interface ItemTouchHelperAdapter {

    void onItemMove(int fromPosition, int toPosition);

    void onItemDismiss(int position);
}