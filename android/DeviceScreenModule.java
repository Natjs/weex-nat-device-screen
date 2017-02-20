package com.nat.weex;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.widget.Toast;

import com.nat.device_screen.HLConstant;
import com.nat.device_screen.HLModuleResultListener;
import com.nat.device_screen.HLScreenModule;
import com.nat.device_screen.HLUtil;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.bridge.JSCallback;
import com.taobao.weex.common.WXModule;

/**
 * Created by Daniel on 17/2/17.
 * Copyright (c) 2017 Nat. All rights reserved.
 */

public class DeviceScreenModule extends WXModule{

    JSCallback mSetBritnessCallback;
    float mBritness;
    @JSMethod
    public void info(final JSCallback jsCallback){
        HLScreenModule.getInstance(mWXSDKInstance.getContext()).info(new HLModuleResultListener() {
            @Override
            public void onResult(Object o) {
                jsCallback.invoke(o);
            }
        });
    }

    @JSMethod
    public void getBrightness(final JSCallback jsCallback){
        HLScreenModule.getInstance(mWXSDKInstance.getContext()).getBrightness(new HLModuleResultListener() {
            @Override
            public void onResult(Object o) {
                jsCallback.invoke(o);
            }
        });
    }

    @JSMethod
    public void getOrientation(final JSCallback jsCallback){
        HLScreenModule.getInstance(mWXSDKInstance.getContext()).getOrientation(new HLModuleResultListener() {
            @Override
            public void onResult(Object o) {
                jsCallback.invoke(o);
            }
        });
    }

    @JSMethod
    public void lockOrientation(String orientation, final JSCallback jsCallback){
        HLScreenModule.getInstance(mWXSDKInstance.getContext()).lockOrientation((Activity) mWXSDKInstance.getContext(), orientation, new HLModuleResultListener() {
            @Override
            public void onResult(Object o) {
                jsCallback.invoke(o);
            }
        });
    }

    @JSMethod
    public void unlockOrientation(final JSCallback jsCallback){
        HLScreenModule.getInstance(mWXSDKInstance.getContext()).unlockOrientation((Activity) mWXSDKInstance.getContext(), new HLModuleResultListener() {
            @Override
            public void onResult(Object o) {
                jsCallback.invoke(o);
            }
        });
    }

    @JSMethod
    public void setBrightness(float brightness, JSCallback jsCallback){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mSetBritnessCallback = jsCallback;
            mBritness = brightness;
            Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
            intent.setData(Uri.parse("package:" + mWXSDKInstance.getContext().getPackageName()));
            ((Activity)mWXSDKInstance.getContext()).startActivityForResult(intent, HLConstant.WRITE_SETTINGS_REQUEST_CODE);
        } else {
            setBrightness(brightness);
            getBrightness(jsCallback);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == HLConstant.WRITE_SETTINGS_REQUEST_CODE) {

            if (Settings.System.canWrite(mWXSDKInstance.getContext())) {
                setBrightness(mBritness);
                getBrightness(mSetBritnessCallback);
            } else {
                mSetBritnessCallback.invoke(HLUtil.getError(HLConstant.CAMERA_PERMISSION_DENIED, HLConstant.CAMERA_PERMISSION_DENIED_CODE));
            }
        }
    }

    private void setScreenMode(int value) {
        Settings.System.putInt(mWXSDKInstance.getContext().getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, value);
    }

    public void setBrightness(float value) {
        int screenMode = 0;
        try {
            screenMode = Settings.System.getInt(mWXSDKInstance.getContext().getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }

        if(screenMode == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC){
            setScreenMode(Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
        }

        Settings.System.putInt(mWXSDKInstance.getContext().getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, (int) (value * 255));
    }
}
