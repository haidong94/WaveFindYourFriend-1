package vinsoft.com.wavefindyourfriend.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import vinsoft.com.wavefindyourfriend.R;
import vinsoft.com.wavefindyourfriend.model.Friend;
import vinsoft.com.wavefindyourfriend.myinterface.IOnCreateGroup;

/**
 * Created by kienmit95 on 4/10/17.
 */

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.ViewHolder> {

    List<Friend> friendList;
    Context mContext;
    IOnCreateGroup createGroup=null;

    public FriendAdapter() {
    }

    public FriendAdapter(List<Friend> friendList, Context mContext, IOnCreateGroup createGroup) {
        this.friendList = friendList;
        this.mContext = mContext;
        this.createGroup = createGroup;
    }

    public List<Friend> getFriendList() {
        return friendList;
    }

    public void setFriendList(List<Friend> friendList) {
        this.friendList = friendList;
    }

    public Context getmContext() {
        return mContext;
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.recyclerview_friend_layout,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Friend friend = friendList.get(position);

        holder.cimgFriendAvatar.setImageResource(R.drawable.ic_profile);
        holder.tvFriendName.setText(friend.getUserAccount().getUserName());
        holder.tvFrinedPhone.setText(friend.getUserAccount().getUserPhone());


    }

    @Override
    public int getItemCount() {
        if (friendList.isEmpty())
            return 0;
        else return friendList.size();
    }

    public void addFriend(Friend friend){
        friendList.add(friend);
        this.notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        CircleImageView cimgFriendAvatar;
        TextView tvFriendName,tvFrinedPhone;

        public ViewHolder(View itemView) {
            super(itemView);

            tvFriendName= (TextView) itemView.findViewById(R.id.tv_friend_name);
            tvFrinedPhone= (TextView) itemView.findViewById(R.id.tv_friend_phone);

            cimgFriendAvatar= (CircleImageView) itemView.findViewById(R.id.img_friend_avatar);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    createGroup.onCreateGroup(friendList.get(getAdapterPosition()).getUserAccount().getUserPhone(),friendList.get(getAdapterPosition()).getUserAccount().getUserName());
                }
            });
        }
    }
}
