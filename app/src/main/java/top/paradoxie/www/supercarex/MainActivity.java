package top.paradoxie.www.supercarex;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import top.paradoxie.www.supercarex.adapter.NormalRecyclerViewAdapter;
import top.paradoxie.www.supercarex.view.DialogView;
import top.paradoxie.www.supercarex.view.EmptyRecyclerView;

public class MainActivity extends AppCompatActivity {
    private EmptyRecyclerView mRecyclerView;
    private DialogView mDialogView;
    private Uri userUri = Uri.parse("content://top.paradoxie.www.supercarex.BookProvider/user");
    private ContentValues contentValues = new ContentValues();
    private List<String> users = new ArrayList<>();
    public static List<String> ids = new ArrayList<>();
    private NormalRecyclerViewAdapter adapter;
    private View mEmptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.menu);
        setSupportActionBar(toolbar);

        if (!isModuleActive()) {
            Toast.makeText(this, "模块死掉了！！！", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "模块运行中...", Toast.LENGTH_SHORT).show();
        }

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_help:
                        Dialog alertDialog = new AlertDialog.Builder(MainActivity.this)
                                .setTitle("使用说明")
                                .setMessage("1.首次使用请前往xposed模块中勾选并重启\n" +
                                        "2.添加到列表中的关键词收到通知后会直接跳转到相关应用\n" +
                                        "3.目前不支持消息内部内容提示\n" +
                                        "4.不区分消息来源，但QQ电脑端在线时手机貌似不会提示消息\n" +
                                        "5.本模块会引起游戏体验严重不适，请卸载游戏\n")
                                .setNegativeButton("晓得了", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                }).create();
                        alertDialog.show();
                        break;
                }
                return false;
            }
        });

        mRecyclerView = findViewById(R.id.recycler_view);
        mEmptyView = findViewById(R.id.empty_iv);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));//这里用线性显示 类似于listview
        users = getUser();
        adapter = new NormalRecyclerViewAdapter(this, users);

        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setEmptyView(mEmptyView);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialogView = new DialogView(MainActivity.this);

                mDialogView.setOnPosNegClickListener(new DialogView.OnPosNegClickListener() {
                    @Override
                    public void posClickListener(String value) {
                        addUser(value);
                        mRecyclerView.setAdapter(new NormalRecyclerViewAdapter(MainActivity.this, getUser()));
                    }

                    @Override
                    public void negCliclListener(String value) {

                    }
                });
                mDialogView.show();
            }
        });
    }

    private List<String> getUser() {
        users.clear();
        Cursor userCursor = getContentResolver().query(userUri, new String[]{"_id", "userName"}, null, null, null);
        if (userCursor != null) {
            while (userCursor.moveToNext()) {
                String id = userCursor.getString(userCursor.getColumnIndex("_id"));
                String user = userCursor.getString(userCursor.getColumnIndex("userName"));
                ids.add(id);
                users.add(user);
            }
            userCursor.close();
        }
        return users;
    }

    private void addUser(String user) {
        contentValues.clear();
        contentValues.put("userName", user);
        getContentResolver().insert(userUri, contentValues);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    private boolean isModuleActive() {
        return false;
    }

}
