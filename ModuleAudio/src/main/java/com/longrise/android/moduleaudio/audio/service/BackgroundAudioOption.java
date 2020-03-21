package com.longrise.android.moduleaudio.audio.service;

import android.content.Context;
import android.support.annotation.Nullable;

import com.longrise.android.moduleaudio.controller.BaseAudioController;
import com.longrise.android.moduleaudio.controller.ControllerFactory;
import com.longrise.android.moduleaudio.params.BaseParams;


/**
 * Created by godliness on 2020-03-20.
 *
 * @author godliness
 */
public final class BackgroundAudioOption {

    private final AudioBridge mBridge;

    private Class<? extends BaseAudioController> mControllerClz;
    @Nullable
    private BaseParams mParams;

    /**
     * 配置后台音频管理 {@link BaseAudioController}
     */
    public void controller(Class<? extends BaseAudioController> controllerClz) {
        this.mControllerClz = controllerClz;
    }

    /**
     * 配置后台音频管理相关参数
     */
    public <T extends BaseParams> void params(@Nullable T params) {
        this.mParams = params;
    }

    /**
     * 进入后台音频管理 {@link BaseAudioController}
     */
    public void runInBackground(Context context) {
        final BaseAudioController controller = ControllerFactory.findAudioController(mControllerClz, context);
        if (controller != null) {
            controller.bindAudioBridge(mBridge, mParams);
        }
    }

    BackgroundAudioOption(AudioBridge bridge) {
        this.mBridge = bridge;
    }
}
