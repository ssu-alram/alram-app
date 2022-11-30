package com.example.ticker.core.adapter;

import android.content.Context;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView.Recycler;
import androidx.recyclerview.widget.RecyclerView.State;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import kotlin.jvm.internal.Intrinsics;

public final class ZoomCenterItemLayoutManager extends LinearLayoutManager {
    private final float shrinkAmount;
    private final float shrinkDistance;
    private final Context context;

    public int scrollVerticallyBy(int dy, @Nullable Recycler recycler, @Nullable State state) {
        int scrolled = super.scrollVerticallyBy(dy, recycler, state);
        float midPoint = (float)this.getHeight() / 2.0F;
        float d0 = 0.0F;
        float d1 = this.shrinkDistance * midPoint;
        float s0 = 1.0F;
        float s1 = 1.0F - this.shrinkAmount;
        int i = 0;

        for(int var11 = this.getChildCount(); i < var11; ++i) {
            View var10000 = this.getChildAt(i);
            if (var10000 != null) {
                View var12 = var10000;
                float childMidPoint = (float)(this.getDecoratedTop(var12) + this.getDecoratedBottom(var12)) / 2.0F;
                float var17 = midPoint - childMidPoint;
                var17 = Math.abs(var17);
                float distance = Math.min(d1, var17);
                float scale = s0 + (s1 - s0) * (distance - d0) / (d1 - d0);
                Intrinsics.checkNotNullExpressionValue(var12, "childView");
                var12.setScaleX(scale);
                var12.setScaleY(scale);
            }
        }

        return scrolled;
    }

    public void onLayoutChildren(@Nullable Recycler recycler, @Nullable State state) {
        super.onLayoutChildren(recycler, state);
        this.scrollVerticallyBy(0, recycler, state);
    }

    public ZoomCenterItemLayoutManager(@NotNull Context context) {
        super(context);
        this.context = context;
        this.shrinkAmount = 0.8F;
        this.shrinkDistance = 0.9F;
    }
}
