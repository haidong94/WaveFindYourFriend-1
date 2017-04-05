package vinsoft.com.wavefindyourfriend.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import vinsoft.com.wavefindyourfriend.R;
import vinsoft.com.wavefindyourfriend.activity.ChatActivity;
import vinsoft.com.wavefindyourfriend.model.Group;

/**
 * Created by kienmit95 on 05/04/17.
 */

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.ViewHolder> {

    List<Group> groupList;
    Context mContext;

    public GroupAdapter(List<Group> groupList, Context mContext) {
        this.groupList = groupList;
        this.mContext = mContext;
    }

    public GroupAdapter(List<Group> groupList) {
        this.groupList = groupList;
    }

    public List<Group> getGroupList() {
        return groupList;
    }

    public void setGroupList(List<Group> groupList) {
        this.groupList = groupList;
    }

    public void addGroup(Group group){
        groupList.add(group);
        this.notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_group_custom,parent,false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Group group = groupList.get(position);

        holder.imgGroupAvatar.setImageResource(R.mipmap.ic_launcher_round);
        holder.tvGroupName.setText(group.getGroupName());
        holder.tvGroupCreateDate.setText(group.getGroupCreateDate());

    }

    @Override
    public int getItemCount() {
        if (groupList.size()<=0)
            return 0;
        else return groupList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView tvGroupName;
        private TextView tvGroupCreateDate;
        private ImageView imgGroupAvatar;
        public ViewHolder(final View itemView) {
            super(itemView);

            tvGroupCreateDate= (TextView) itemView.findViewById(R.id.tv_group_create_date);
            tvGroupName= (TextView) itemView.findViewById(R.id.tv_group_name);
            imgGroupAvatar= (ImageView) itemView.findViewById(R.id.img_conversation_avatar);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, ChatActivity.class);
                    intent.putExtra("groupId",groupList.get(getAdapterPosition()).getGroupId());
                    mContext.startActivity(intent);
                }
            });
        }

    }
}
