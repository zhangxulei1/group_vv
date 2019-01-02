package com.example.zjq.my_app.HomePage.Chat;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.zjq.my_app.R;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    private List<Message> mMsgLIst;

    //内部类
    static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout leftLayout;
        LinearLayout rightLayout;

        TextView leftMsg;
        TextView rightMsg;

        //找到子项布局中的控件
        public ViewHolder(View view) {
            super( view );
            leftLayout = view.findViewById( R.id.layout_left );
            rightLayout = view.findViewById( R.id.layout_right );
            leftMsg = view.findViewById( R.id.left_msg );
            rightMsg = view.findViewById( R.id.right_msg );
        }
    }

    //构造函数，用于把要展示的数据源传递进来
    public MessageAdapter(List<Message> msgList) {
        mMsgLIst = msgList;
    }

    //加载子布局，返回view
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int i) {

        View view = LayoutInflater.from( parent.getContext() ).inflate( R.layout.recyclerview_item, parent, false );
        return new ViewHolder( view );
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Message msg = mMsgLIst.get( position );
        if (msg.getType() == Message.TYPE_RECEIVED) {
            holder.leftLayout.setVisibility( View.VISIBLE );//显示左边
            holder.rightLayout.setVisibility( View.GONE );//隐藏右边
            holder.leftMsg.setText( msg.getContent() );
        } else if (msg.getType() == Message.TYPE_SENT) {
            holder.rightLayout.setVisibility( View.VISIBLE );
            holder.leftLayout.setVisibility( View.GONE );
            holder.rightMsg.setText( "我说："+msg.getContent() );
        }

    }

    @Override
    public int getItemCount() {
        return mMsgLIst.size();
    }
}
