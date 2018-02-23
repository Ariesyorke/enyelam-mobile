package com.nyelam.android.helper;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/12/2018.
 */

public class NYSpacesItemDecoration extends RecyclerView.ItemDecoration {
    private int spaceRight;
    private int spaceLeft;
    private int spaceTop;
    private int spaceBottom;

    public NYSpacesItemDecoration(int space) {
        this.spaceLeft = space;
        this.spaceTop = space;
        this.spaceRight = space;
        this.spaceBottom = space;
    }

    public NYSpacesItemDecoration(int spaceLeft, int spaceTop, int spaceRight, int spaceBottom) {
        this.spaceLeft = spaceLeft;
        this.spaceTop = spaceTop;
        this.spaceRight = spaceRight;
        this.spaceBottom = spaceBottom;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {
        outRect.left = spaceLeft;
        outRect.right = spaceRight;
        outRect.bottom = spaceBottom;

        // Add top margin only for the first item to avoid double space between items
        if (parent.getChildLayoutPosition(view) == 0) {
            outRect.top = spaceTop;
        } else {
            outRect.top = 0;
        }
    }
}