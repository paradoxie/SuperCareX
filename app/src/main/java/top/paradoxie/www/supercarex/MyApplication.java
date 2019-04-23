package top.paradoxie.www.supercarex;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;


/**
 * Created by xiehehe on 2017/11/27.
 */

public class MyApplication extends Application {
    public static Context context;
    public static boolean isShow = true;
    private static Toast mToast;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();

//        if (isDebug) {
//            ARouter.openLog();//开启日志
//            ARouter.openDebug();//开启debug模式
//        }
//
//        ARouter.init(this);


    }
    public static Context getContext(){
        return context;
    }

    public static void showToast(CharSequence message) {

        if (isShow && message != null && !MyApplication.isStrNull(message + ""))
            if (mToast == null) {
                mToast = Toast.makeText(getContext(), message, Toast.LENGTH_SHORT);
            } else {
                mToast.setText(message);
            }
        mToast.show();
    }

    public static boolean isStrNull(String str) {
        if (null == str) {
            return true;
        } else if ("".equals(str.trim())) {
            return true;
        } else if ("null".equals(str.trim())) {
            return true;
        } else {
            return false;
        }
    }

}
