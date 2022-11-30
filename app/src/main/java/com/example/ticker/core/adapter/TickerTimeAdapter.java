package com.example.ticker.core.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.DiffUtil.ItemCallback;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.example.ticker.databinding.LayoutDefaultTickerTimeBinding;
import com.example.ticker.utils.TickerExtensionsKt;

import org.jetbrains.annotations.NotNull;

import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

public final class TickerTimeAdapter extends ListAdapter {
    @NotNull
    private static final ItemCallback diffUtil = (ItemCallback)(new ItemCallback() {
        public boolean areItemsTheSame(int oldItem, int newItem) {
            return Integer.hashCode(oldItem) == Integer.hashCode(newItem);
        }

        // $FF: synthetic method
        // $FF: bridge method
        public boolean areItemsTheSame(Object var1, Object var2) {
            return this.areItemsTheSame(((Number)var1).intValue(), ((Number)var2).intValue());
        }

        public boolean areContentsTheSame(int oldItem, int newItem) {
            return oldItem == newItem;
        }

        // $FF: synthetic method
        // $FF: bridge method
        public boolean areContentsTheSame(Object var1, Object var2) {
            return this.areContentsTheSame(((Number)var1).intValue(), ((Number)var2).intValue());
        }
    });
    @NotNull
    public static final TickerTimeAdapter.Companion Companion = new TickerTimeAdapter.Companion((DefaultConstructorMarker)null);

    @NotNull
    public DefaultTickerViewHolder onCreateViewHolder(@NotNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        LayoutDefaultTickerTimeBinding binding = LayoutDefaultTickerTimeBinding.inflate(inflater, parent, false);
        return new TickerTimeAdapter.DefaultTickerViewHolder(binding);
    }

    // $FF: synthetic method
    // $FF: bridge method
    public ViewHolder onCreateViewHolder(ViewGroup var1, int var2) {
        return (ViewHolder)this.onCreateViewHolder(var1);
    }

    public void onBindViewHolder(@NotNull TickerTimeAdapter.DefaultTickerViewHolder holder, int position) {
        Intrinsics.checkNotNullParameter(holder, "holder");
        Integer time = (Integer)this.getItem(position);
        Intrinsics.checkNotNullExpressionValue(time, "time");
        holder.bindTime(time);
    }

    // $FF: synthetic method
    // $FF: bridge method
    public void onBindViewHolder(ViewHolder var1, int var2) {
        this.onBindViewHolder((TickerTimeAdapter.DefaultTickerViewHolder)var1, var2);
    }

    public TickerTimeAdapter() {
        super(diffUtil);
    }

    public final class DefaultTickerViewHolder extends ViewHolder {
        @NotNull
        private final LayoutDefaultTickerTimeBinding binding;

        public final void bindTime(int position) {
            TextView var10000 = this.binding.getRoot();
            Intrinsics.checkNotNullExpressionValue(var10000, "binding.root");
            var10000.setText((CharSequence)TickerExtensionsKt.toDoubleDigitString(position));
        }

        @NotNull
        public final LayoutDefaultTickerTimeBinding getBinding() {
            return this.binding;
        }

        public DefaultTickerViewHolder(@NotNull LayoutDefaultTickerTimeBinding binding) {
            super((View)binding.getRoot());
            this.binding = binding;
        }
    }

    public static final class Companion {
        @NotNull
        public final ItemCallback getDiffUtil() {
            return TickerTimeAdapter.diffUtil;
        }

        private Companion() {
        }

        // $FF: synthetic method
        public Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }
    }
}
