package com.bajera.xlog.trax.data;

import android.content.Context;
import android.content.res.Resources;

import androidx.core.content.ContextCompat;

import com.bajera.xlog.trax.R;

public class AndroidResourceManager implements ResourceManager {
    private Resources resources;
    private Context context;

    public AndroidResourceManager(Context context) {
        this.resources = context.getResources();
        this.context = context;
    }

    @Override
    public int getCalendarNumbersDrawableId() {
//        return context.getDrawable(R.drawable.calendar_numbers);
        return 0;
    }

    @Override
    public int getCalendarDailyTaskDrawableId() {
        return R.drawable.calendar_daily_task;
    }

    @Override
    public int getChartDatasetColour() {
        return ContextCompat.getColor(context, R.color.chartColor);
    }

    @Override
    public int getSecondaryColour() {
        return ContextCompat.getColor(context, R.color.colorSecondary);
    }
}
