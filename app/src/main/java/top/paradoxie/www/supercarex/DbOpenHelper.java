package top.paradoxie.www.supercarex;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbOpenHelper extends SQLiteOpenHelper {

    //数据库名
    private static final String DATA_BASE_NAME = "book.db";

    //数据库版本号
    private static final int DATE_BASE_VERSION = 1;

    //表名-用户
    public static final String USER_TABLE_NAME = "user";

    //创建表-用户（主键自增长、用户名）
    private final String CREATE_USER_TABLE = "create table " + USER_TABLE_NAME
            + "(_id integer primary key autoincrement, userName text)";

    public DbOpenHelper(Context context) {
        super(context, DATA_BASE_NAME, null, DATE_BASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
