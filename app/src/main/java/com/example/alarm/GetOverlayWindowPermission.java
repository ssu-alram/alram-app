package com.example.alarm;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;

import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

// 권한 요청을 위해 Result API에 사용되는 클래스. ActivityResultContract을 상속받아야 한다.
public class GetOverlayWindowPermission extends ActivityResultContract<Void, Boolean> {

    private final Context mContext;

    public GetOverlayWindowPermission(Context mContext) {
        this.mContext = mContext;
    }

    // new Intent 와 같은 역할을 하는 메소드.
    @NonNull
    @Override
    public Intent createIntent(@NonNull Context context, Void input) {
        return new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + context.getPackageName()));
    }

    // onActivityResult 안에서 intent 를 체크하는 것과 같은 역할을 함.
    @Override
    public Boolean parseResult(int resultCode, @Nullable Intent intent) {
        return Settings.canDrawOverlays(mContext);
    }
}
