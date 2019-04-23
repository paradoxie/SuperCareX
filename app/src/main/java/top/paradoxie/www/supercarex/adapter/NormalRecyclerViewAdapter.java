package top.paradoxie.www.supercarex.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import top.paradoxie.www.supercarex.DbOpenHelper;
import top.paradoxie.www.supercarex.MainActivity;
import top.paradoxie.www.supercarex.MyApplication;
import top.paradoxie.www.supercarex.R;


/**
 * Created by xiehehe on 2017/11/27.
 */

public class NormalRecyclerViewAdapter extends RecyclerView.Adapter<NormalRecyclerViewAdapter.NormalTextViewHolder> {
    private final LayoutInflater mLayoutInflater;
    private final Context mContext;
    private List<String> mTitles;
    private SQLiteDatabase db;
    private DbOpenHelper dbHelper;


    public NormalRecyclerViewAdapter(Context context, List<String> array) {
        mTitles = array;
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        dbHelper = new DbOpenHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    @Override
    public NormalTextViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NormalTextViewHolder(mLayoutInflater.inflate(R.layout.item_text, parent, false));
    }

    @Override
    public void onBindViewHolder(final NormalTextViewHolder holder, final int position) {
        holder.mTextView.setText(mTitles.get(position));
        holder.cv_item.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Dialog alertDialog = new AlertDialog.Builder(mContext)
                        .setTitle("删除此条")
                        .setMessage("不再接收此条消息了嘛？")
                        .setPositiveButton("是的", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    db.delete(DbOpenHelper.USER_TABLE_NAME, "_id=" + MainActivity.ids.get(position), null);
                                    removeData(position);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    MyApplication.showToast("好像遇到了什么问题，删除失败~");
                                }
                            }
                        }).setNegativeButton("按错了", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create();
                alertDialog.show();
                return false;
            }
        });
    }

    //  删除数据
    public void removeData(int position) {
        mTitles.remove(position);
        //删除动画
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mTitles == null ? 0 : mTitles.size();
    }

    public class NormalTextViewHolder extends RecyclerView.ViewHolder {

        TextView mTextView;
        CardView cv_item;

        NormalTextViewHolder(View view) {
            super(view);
            mTextView = view.findViewById(R.id.text_view);
            cv_item = view.findViewById(R.id.cv_item);

        }
    }
}