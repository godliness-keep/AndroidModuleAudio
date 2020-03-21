package com.longrise.android.mvp.internal;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.longrise.android.mvp.utils.MvpLog;

/**
 * Created by godliness on 2019-06-29.
 *
 * @author godliness
 */
@SuppressWarnings("unused")
abstract class BaseSuperActivity extends AppCompatActivity {

    private static Resources sResources;
    private final LifeCycleGuard mGuard = LifeCycleGuard.get();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        beforeSetContentView();
    }

    /**
     * So setContentView does something about the Window
     */
    protected void beforeSetContentView() {
        resetStatusBarColor();
    }

    /**
     * Set the status bar color, default empty implementation
     */
    protected void resetStatusBarColor() {
    }

    /**
     * StatusBar visible state
     * View.SYSTEM_UI_FLAG_VISIBLE
     * View.INVISIBLE
     * View.SYSTEM_UI_FLAG_FULLSCREEN
     * View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
     *
     * @param visible visible state
     */
    protected void setStatusBarVisible(int visible) {
        getWindow().getDecorView().setSystemUiVisibility(visible);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        try {
            super.startActivityForResult(intent, requestCode);
        } catch (Exception e) {
            MvpLog.print(e);
        }
    }

    @Override
    public ComponentName startService(Intent service) {
        mGuard.startService(service);
        ComponentName name = null;
        try {
            name = super.startService(service);
        } catch (Exception e) {
            MvpLog.print(e);
        }
        return name;
    }

    @Override
    public boolean stopService(Intent name) {
        mGuard.stopService(name);
        boolean stopService = false;
        try {
            stopService = super.stopService(name);
        } catch (Exception e) {
            MvpLog.print(e);
        }
        return stopService;
    }

    @Override
    public boolean bindService(Intent service, ServiceConnection conn, int flags) {
        boolean bindService = false;
        try {
            bindService = super.bindService(service, conn, flags);
        } catch (Exception e) {
            MvpLog.print(e);
        }
        return bindService;
    }

    @Override
    public void unbindService(ServiceConnection conn) {
        try {
            super.unbindService(conn);
        } catch (Exception e) {
            MvpLog.print(e);
        }
    }

    @Override
    public final Intent registerReceiver(BroadcastReceiver receiver, IntentFilter filter, String broadcastPermission, Handler scheduler) {
        mGuard.register(receiver);
        Intent intent = null;
        try {
            intent = super.registerReceiver(receiver, filter, broadcastPermission, scheduler);
        } catch (Exception e) {
            MvpLog.print(e);
        }
        return intent;
    }

    @Override
    public final void unregisterReceiver(BroadcastReceiver receiver) {
        mGuard.unRegister(receiver);
        try {
            super.unregisterReceiver(receiver);
        } catch (Exception e) {
            MvpLog.print(e);
        }
    }

    @Override
    public final Resources getResources() {
        if (sResources == null) {
            sResources = super.getResources();
            final Configuration configuration = new Configuration();
            configuration.setToDefaults();
            sResources.updateConfiguration(configuration, sResources.getDisplayMetrics());
        }
        return sResources;
    }

    @Override
    public void onBackPressed() {
        try {
            super.onBackPressed();
        } catch (Exception e) {
            MvpLog.print(e);
        }
    }
}
