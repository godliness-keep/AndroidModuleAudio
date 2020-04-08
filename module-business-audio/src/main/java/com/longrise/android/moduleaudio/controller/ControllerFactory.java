package com.longrise.android.moduleaudio.controller;

import android.content.Context;
import android.support.v4.util.ArrayMap;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by godliness on 2020-03-19.
 *
 * @author godliness
 */
public final class ControllerFactory {

    private static final ArrayMap<String, BaseAudioController> sControllerMaps = new ArrayMap<>(3);

    public static BaseAudioController findAudioController(Class<? extends BaseAudioController> controllerClz, Context cxt) {
        final String name = controllerClz.getName();
        BaseAudioController controller = sControllerMaps.get(name);
        if (controller == null) {
            try {
                final Constructor<BaseAudioController> constructor = (Constructor<BaseAudioController>) controllerClz.getConstructor(Context.class);
                sControllerMaps.put(name, controller = constructor.newInstance(cxt));
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return controller;
    }

    static void removeController(BaseAudioController controller) {
        sControllerMaps.remove(controller.getClass().getName());
    }

    private ControllerFactory() {
        throw new IllegalStateException("ControllerFactory not init");
    }
}
