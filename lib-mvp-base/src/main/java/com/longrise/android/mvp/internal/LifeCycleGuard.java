package com.longrise.android.mvp.internal;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Intent;
import android.support.annotation.NonNull;

/**
 * Created by godliness on 2019-07-08.
 *
 * @author godliness
 */
@SuppressWarnings("unused")
public final class LifeCycleGuard {

    private static volatile ReceiverTracker sReceiverTracker;
    private static volatile ServiceTracker sServiceTracker;

    private static volatile boolean ENABLED;

    @NonNull
    static LifeCycleGuard get() {
        if (!ENABLED) {
            return LifecycleHolder.GUARD;
        }
        return new LifeCycleGuard();
    }

    public static void setEnabled(boolean enabled) {
        ENABLED = enabled;
    }

    public static boolean isEnabled() {
        return ENABLED;
    }

    public static void setReceiverTracker(ReceiverTracker tracker) {
        sReceiverTracker = tracker;
    }

    public static void setServiceTracker(ServiceTracker tracker) {
        sServiceTracker = tracker;
    }

    void register(BroadcastReceiver receiver) {
        if (sReceiverTracker != null) {
            sReceiverTracker.register(receiver);
        }
    }

    void unRegister(BroadcastReceiver receiver) {
        if (sReceiverTracker != null) {
            sReceiverTracker.unregister(receiver);
        }
    }

    void startService(Intent service) {
        if (sServiceTracker != null) {
            final ComponentName name = service.getComponent();
            if (name != null) {
                sServiceTracker.startService(name.getClassName());
            }
        }
    }

    void stopService(Intent service) {
        if (sServiceTracker != null) {
            final ComponentName name = service.getComponent();
            if (name != null) {
                sServiceTracker.stopService(name.getClassName());
            }
        }
    }

    public interface ReceiverTracker {

        /**
         * Watch broadcast registration
         *
         * @param receiver BroadcastReceiver
         */
        void register(BroadcastReceiver receiver);

        /**
         * Watch broadcast uninstall
         *
         * @param receiver BroadcastReceiver
         */
        void unregister(BroadcastReceiver receiver);
    }

    public interface ServiceTracker {

        /**
         * Observe startup service
         *
         * @param service Service name
         */
        void startService(String service);

        /**
         * Observe discontinuation of service
         *
         * @param service Service name
         */
        void stopService(String service);
    }

    private LifeCycleGuard() {
    }

    private static final class LifecycleHolder {
        private static final LifeCycleGuard GUARD = new LifeCycleGuard();
    }
}
