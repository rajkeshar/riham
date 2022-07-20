package com.mobiato.sfa;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;


import com.mobiato.sfa.interfaces.OnKeyboardVisibilityListener;


public abstract class AbstractBaseActivity extends AppCompatActivity {
    private static Class<?> redirectClass;

    /**
     * <!-- Add this line application theme-->
     * <item name="android:windowContentTransitions" tools:targetApi="lollipop">true</item>
     * <!-- Add this lines to main view and details view->
     * <android:transitionName="@string/transition_string"
     * tools:targetApi="lollipop">
     ***/
    public void startActivity(View viewStart, String transactionName, Intent intent) {
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, viewStart, transactionName);
        ActivityCompat.startActivity(this, intent, options.toBundle());
    }

    @Override
    protected void onResume() {
        super.onResume();
        /**RegisterInternetReceiver */

    }

    public static Class<?> getRedirectClass() {
        return redirectClass;
    }

    public static void setRedirectClass(Class<?> redirectClass) {
        AbstractBaseActivity.redirectClass = redirectClass;
    }

    /**
     * This method allow us to know status of the Keyboard of the system weather its open or close
     *
     * @param listener Is OnKeyboardVisibilityListener interface in which onVisibilityChanged(boolean visible) method
     *                 which is fire while System Keyboard is open or close. visible is true when System Keyboard is open
     *                 or its false while System Keyboard is close.
     */
    public void setKeyboardListener(final OnKeyboardVisibilityListener listener) {
        final View activityRootView = ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);

        activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            private boolean wasOpened;

            private final int DefaultKeyboardDP = 100;

            // From @nathanielwolf answer...  Lollipop includes button bar in the root. Add height of button bar (48dp) to maxDiff
            private final int EstimatedKeyboardDP = DefaultKeyboardDP + (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ? 48 : 0);

            private final Rect r = new Rect();

            @Override
            public void onGlobalLayout() {
                // Convert the dp to pixels.
                int estimatedKeyboardHeight = (int) TypedValue
                        .applyDimension(TypedValue.COMPLEX_UNIT_DIP, EstimatedKeyboardDP, activityRootView.getResources().getDisplayMetrics());

                // Conclude whether the keyboard is shown or not.
                activityRootView.getWindowVisibleDisplayFrame(r);
                int heightDiff = activityRootView.getRootView().getHeight() - (r.bottom - r.top);
                boolean isShown = heightDiff >= estimatedKeyboardHeight;

                if (isShown == wasOpened) {
//                    Log.d("Keyboard state", "Ignoring global layout change...");
                    return;
                }
                wasOpened = isShown;
                if (listener != null)
                    listener.onVisibilityChanged(isShown);
            }
        });
    }

    /**
     * The ANDROID_ID value won't change on package uninstall/reinstall, as long as the package name
     * and signing key are the same. Apps can rely on this value to maintain state across reinstalls.
     * <p>
     * If an app was installed on a device running an earlier version of Android, the Android ID remains
     * the same when the device is updated to Android O, unless the app is uninstalled and reinstalled.
     * <p>
     * The Android ID value only changes if the device is factory reset or if the signing key rotates between
     * uninstall and reinstall events.
     * <p>
     * This change is only required for device manufacturers shipping with Google Play services and Advertising ID.
     * Other device manufacturers may provide an alternative resettable ID or continue to provide ANDROID ID.
     */
    @SuppressLint("HardwareIds")
    public static String getAndroidId(Context mContext) {
        return Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID);
    }
}
