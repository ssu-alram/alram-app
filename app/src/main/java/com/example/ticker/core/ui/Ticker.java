// Ticker.java
package com.example.ticker.core.ui;

import android.content.Context;
import android.content.res.Resources.Theme;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;

import androidx.annotation.ColorRes;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.OnScrollListener;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.example.ticker.R.color;
import com.example.ticker.R.styleable;
import com.example.ticker.core.adapter.TickerTimeAdapter;
import com.example.ticker.core.adapter.ZoomCenterItemLayoutManager;
import com.example.ticker.databinding.LayoutTickerBinding;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import kotlin.collections.CollectionsKt;
import kotlin.jvm.JvmOverloads;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.IntProgression;
import kotlin.ranges.IntRange;
import kotlin.ranges.RangesKt;
import kotlin.text.StringsKt;

public class Ticker extends ConstraintLayout {
    private LayoutTickerBinding binding;
    private TickerTimeAdapter hoursAdapter;
    private TickerTimeAdapter minutesAdapter;
    private boolean shouldBeIn12HourFormat;
    private boolean isAmSelected;
    private String currentlySelectedHour;
    private String currentlySelectedMinute;
    private int minutesInterval = 1;
    private AttributeSet attrs;
    private int defStyleAttr;
    private static  int HOURS_12_FORMAT = 12;
    private static  int HOURS_24_FORMAT = 24;
    @NotNull
    public static Ticker.Companion Companion = new Ticker.Companion();

    class smoothScrollToPosition implements Runnable {
        int timeInInt;
        Ticker ticker;
        RecyclerView rv;

        smoothScrollToPosition(int var1, @Nullable Ticker var2, RecyclerView var3) {
            this.timeInInt = var1;
            this.ticker = var2;
            this.rv = var3;
        }
        public void run() {
            if (ticker != null) {
                int position = Ticker.shouldBeIn12HourFormat(this.ticker) ? this.timeInInt - 1 : this.timeInInt;
                this.rv.smoothScrollToPosition(position);
            } else {
                this.rv.smoothScrollToPosition(this.timeInInt);
            }
        }
    }
    @JvmOverloads
    public Ticker(@NotNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.attrs = attrs;
        this.defStyleAttr = defStyleAttr;
        this.binding = LayoutTickerBinding.inflate(LayoutInflater.from(context), (ViewGroup)this, true);
        this.hoursAdapter = new TickerTimeAdapter();
        this.minutesAdapter = new TickerTimeAdapter();
        this.shouldBeIn12HourFormat = true;
        this.isAmSelected = true;
        this.currentlySelectedHour = "";
        this.currentlySelectedMinute = "";
        this.minutesInterval = 1;
        this.initConfigurations();
        this.initViews();
        this.initHoursAndMinutesList();
    }

    private TickerTimeAdapter getHoursAdapter() {
        return hoursAdapter;
    }

    private TickerTimeAdapter getMinutesAdapter() {
        return minutesAdapter;
    }

    private void initConfigurations() {
        Context context = this.getContext();
        Intrinsics.checkNotNullExpressionValue(context, "context");
        TypedArray var = context.getTheme().obtainStyledAttributes(this.attrs, styleable.Ticker, this.defStyleAttr, 0);
        boolean var3 = false;

        try {
            int hoursFormat = var.getInt(styleable.Ticker_hoursFormat, 12);
            this.shouldBeIn12HourFormat = hoursFormat == 12;
            int var5 =var.getInt(styleable.Ticker_minutesInterval, 1);
            byte var6 = 1;
            this.minutesInterval = Math.max(var5, var6);
            this.isAmSelected = var.getBoolean(styleable.Ticker_shouldAmSelected, true);
        } finally {
            var.recycle();
        }

    }

    private void initViews() {
        this.binding.tvAm.setVisibility(this.shouldBeIn12HourFormat ? View.VISIBLE : View.GONE);
        this.binding.tvPm.setVisibility(this.shouldBeIn12HourFormat ? View.VISIBLE : View.GONE);
        this.binding.tvAm.setOnClickListener((new OnClickListener() {
            public  void onClick(View it) {
                Ticker.this.isAmSelected = true;
                Ticker.this.binding.tvAm.setTextColor(ResourcesCompat.getColor(Ticker.this.getResources(), color.black, (Theme)null));
                Ticker.this.binding.tvPm.setTextColor(ResourcesCompat.getColor(Ticker.this.getResources(), color.grey, (Theme)null));
            }
        }));
        this.binding.tvPm.setOnClickListener((new OnClickListener() {
            public void onClick(View it) {
                Ticker.this.isAmSelected = false;
                Ticker.this.binding.tvPm.setTextColor(ResourcesCompat.getColor(Ticker.this.getResources(), color.black, (Theme)null));
                Ticker.this.binding.tvAm.setTextColor(ResourcesCompat.getColor(Ticker.this.getResources(), color.grey, (Theme)null));
            }
        }));
        if (this.isAmSelected) {
            this.binding.tvAm.performClick();
        } else {
            this.binding.tvPm.performClick();
        }

    }

    private void initHoursAndMinutesList() {
        List minutesList = CollectionsKt.toList(RangesKt.step((IntProgression)RangesKt.until(0, 60), this.minutesInterval));
        List hoursList;
        if (this.shouldBeIn12HourFormat) {
            hoursList = CollectionsKt.toList((Iterable)(new IntRange(1, 12)));
        } else {
            hoursList = CollectionsKt.toList((Iterable)(new IntRange(0, 24)));
        }
        this.initTickerRecyclerViews(this.binding.rvHours, this.getHoursAdapter(), hoursList, true);
        this.initTickerRecyclerViews(this.binding.rvMinutes, this.getMinutesAdapter(), minutesList, false);
        this.setupTopBottomPadding();
    }

    private void initTickerRecyclerViews( RecyclerView rv, TickerTimeAdapter adapter, List<Integer> unitsList,  boolean isHours) {
        rv.setLayoutManager((new ZoomCenterItemLayoutManager(rv.getContext())));
        rv.setAdapter(adapter);
         LinearSnapHelper linearSnapHelper = new LinearSnapHelper();
        linearSnapHelper.attachToRecyclerView(rv);
        rv.addOnScrollListener((new OnScrollListener() {
            public void onScrollStateChanged(@NotNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    this.updateTextColorForSelectedTimeText(recyclerView, color.time_unit_selected_color, isHours);
                } else {
                    this.updateTextColorForSelectedTimeText(recyclerView, color.time_unit_unselected_color, isHours);
                }
            }

            private void updateTextColorForSelectedTimeText(RecyclerView recyclerView, @ColorRes int textColorRes, boolean isHours) {
                View snappedChildView = linearSnapHelper.findSnapView(recyclerView.getLayoutManager());
                if (snappedChildView == null) return;
                ViewHolder snappedViewHolder = rv.getChildViewHolder(snappedChildView);
            }
        }));
        adapter.submitList(unitsList);
        this.scrollToCurrentTime(rv, isHours);
    }

    private void updateCurrentlySelectedValues(boolean isHours, String timeString, @ColorRes int appliedColorRes) {
        if (appliedColorRes == color.time_unit_selected_color) {
            if (isHours) {
                this.currentlySelectedHour = timeString;
            } else {
                this.currentlySelectedMinute = timeString;
            }
        }

    }

    private void setupTopBottomPadding() {
        this.binding.getRoot().getViewTreeObserver().addOnGlobalLayoutListener((OnGlobalLayoutListener)(new OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                int height = Ticker.this.binding.getRoot().getHeight();
                int padding = height / 2 - 20;
                Ticker.this.binding.rvHours.setPadding(0, padding, 0, padding);
                Ticker.this.binding.rvMinutes.setPadding(0, padding, 0, padding);
                Ticker.this.binding.getRoot().getViewTreeObserver().removeOnGlobalLayoutListener((OnGlobalLayoutListener)this);
            }
        }));
    }

    private void scrollToCurrentTime( RecyclerView rv, boolean isHours) {
        Runnable scrollRunnable;
        Integer currentTimeIntPosition;
        int timeInInt;
        if (isHours) {
            currentTimeIntPosition = StringsKt.toIntOrNull(this.currentlySelectedHour);
            if (currentTimeIntPosition != null) {
                timeInInt = (currentTimeIntPosition).intValue();
                scrollRunnable = new smoothScrollToPosition(timeInInt, this, rv);
            } else {
                scrollRunnable = (new Runnable() {
                    public void run() {
                        rv.smoothScrollBy(0, 20);
                    }
                });
            }
        } else {
            currentTimeIntPosition = StringsKt.toIntOrNull(this.currentlySelectedMinute);
            if (currentTimeIntPosition != null) {
                timeInInt = (currentTimeIntPosition).intValue();
                scrollRunnable = new smoothScrollToPosition(timeInInt, null, rv);
            } else {
                scrollRunnable = (new Runnable() {
                    public void run() {
                        rv.smoothScrollBy(0, 20);
                    }
                });
            }
        }
        rv.postDelayed(scrollRunnable, 100L);
    }

    @NotNull
    public String getCurrentlySelectedTime(@NotNull String format) {
        Log.d("진입","여기까지는 문제 없음");
        if (format == null) format = "HH:MM FORMAT";
        return StringsKt.replace(StringsKt.replace(StringsKt.replace(format,
                                "HH",
                                this.currentlySelectedHour,
                                false),
                        "MM",
                        this.currentlySelectedMinute,
                        false),
                "FORMAT",
                this.isAmSelected ? "Am" : "Pm",
                false);
    }

    @NotNull
    public String getCurrentlySelectedTime() {
        return getCurrentlySelectedTime("HH:MM FORMAT");
    }

    public void setInitialSelectedTime(@NotNull String initialTime) {
        List timeAndAMPmSplit = StringsKt.split(initialTime, new String[]{" "}, false, 0);
        if (timeAndAMPmSplit.size() >= 2) {
            List hourAndMinuteSplit = StringsKt.split((CharSequence)timeAndAMPmSplit.get(0), new String[]{":"}, false, 0);
            if (hourAndMinuteSplit.size() >= 2) {
                this.currentlySelectedHour = String.valueOf(hourAndMinuteSplit.get(0));
                this.currentlySelectedMinute = String.valueOf(hourAndMinuteSplit.get(1));
                this.isAmSelected = StringsKt.equals(String.valueOf(timeAndAMPmSplit.get(1)), "Am", true);
                if (this.isAmSelected) {
                    this.binding.tvAm.performClick();
                } else {
                    this.binding.tvPm.performClick();
                }

                this.scrollToCurrentTime(this.binding.rvHours, true);
                this.scrollToCurrentTime(this.binding.rvMinutes, false);
            }
        }
    }



    // $FF: synthetic method
    public static  boolean shouldBeIn12HourFormat(Ticker $this) {
        return $this.shouldBeIn12HourFormat;
    }

    // $FF: synthetic method
    public static  void access$setShouldBeIn12HourFormat$p(Ticker $this, boolean var1) {
        $this.shouldBeIn12HourFormat = var1;
    }
    public static  class Companion {
        private Companion() {
        }

        // $FF: synthetic method
        public Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }
    }
}
