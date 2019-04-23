package top.paradoxie.www.supercarex.xp;

import android.app.ActivityManager;
import android.app.AndroidAppHelper;
import android.app.Application;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static android.content.Context.ACTIVITY_SERVICE;

public class Care implements IXposedHookLoadPackage {

    private Uri userUri = Uri.parse("content://top.paradoxie.www.supercarex.provider.BookProvider/user");
    private List<String> users = new ArrayList<>();
    private Context mContext;
    private static final String TAG = "获取当前的运行包名";
    private static final String WECHAT = "com.tencent.mm";

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        XposedBridge.log("炒鸡关心: " + lpparam.packageName);
        final Class<?> clazz = XposedHelpers.findClass("android.app.Instrumentation", null);
        if (lpparam.packageName.equals("top.paradoxie.www.supercarex")) {
            XposedHelpers.findAndHookMethod("top.paradoxie.www.supercarex.MainActivity", lpparam.classLoader, "isModuleActive", XC_MethodReplacement.returnConstant(true));
        }

        XposedHelpers.findAndHookMethod("android.app.NotificationManager"
                , lpparam.classLoader, "notify"
                , String.class, int.class, Notification.class
                , new XC_MethodHook() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        XposedBridge.log("炒鸡关心.拿到消息弹窗:  " + Arrays.toString(param.args));
                        //通过param拿到第三个入参notification对象
                        Notification notification = (Notification) param.args[2];
                        mContext = AndroidAppHelper.currentApplication();
//                        Toast.makeText(mContext, "炒鸡关心.微信在顶部吗:  " + isTopActivy(WECHAT, mContext), Toast.LENGTH_SHORT).show();
                        XposedBridge.log("炒鸡关心.上下文:  " + mContext);
                        CharSequence charSequence = notification.tickerText;
                        String s = charSequence.toString().split(":")[0];
                        XposedBridge.log("炒鸡关心.标题:  " + s);
                        users = getUser();

                        if (charSequence != null && users.contains(s)) {
                            PendingIntent pendingIntent = notification.contentIntent;
                            try {
                                pendingIntent.send();
                            } catch (PendingIntent.CanceledException e) {
                                e.printStackTrace();
                            }

                        }
                        super.beforeHookedMethod(param);
                    }
                });

//        XposedHelpers.findAndHookMethod(clazz, "callApplicationOnCreate", Application.class
////                , new XC_MethodHook() {
////                    @Override
////                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
////                        super.beforeHookedMethod(param);
////                    }
////
////                    @Override
////                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
////                        super.afterHookedMethod(param);
////                        Context context = null;
////                        if(param.args[0] instanceof Application){
////                            context = ((Application) param.args[0]).getApplicationContext();
////                        } else {
////                            XposedBridge.log(TAG+"hook callApplicationOnCreate failed");
////                            return;
////                        }
////                        Toast.makeText(context, context.getPackageName(), Toast.LENGTH_SHORT).show();
////                    }
////                });

    }

    /**
     * 判断当前程序是否在栈顶
     *
     * @param packageName
     * @param context
     * @return
     */
    public static boolean isTopActivy(String packageName, Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
        String currentPackageName = cn.getPackageName();

        return (currentPackageName != null && currentPackageName.equals(packageName));
    }


    private List<String> getUser() {
        users.clear();
        ContentResolver contentResolver = mContext.getContentResolver();
        Cursor userCursor = contentResolver.query(userUri, new String[]{"_id", "userName"}, null, null, null);
        if (userCursor != null) {
            while (userCursor.moveToNext()) {
                String user = userCursor.getString(userCursor.getColumnIndex("userName"));
                users.add(user);
            }
            userCursor.close();
        }
        return users;
    }
}
