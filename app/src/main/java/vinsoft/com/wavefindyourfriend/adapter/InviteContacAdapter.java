package vinsoft.com.wavefindyourfriend.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import vinsoft.com.wavefindyourfriend.R;
import vinsoft.com.wavefindyourfriend.model.Contact;

/**
 * Created by kienmit95 on 4/7/17.
 */

public class InviteContacAdapter extends RecyclerView.Adapter<InviteContacAdapter.ViewHolder>{

    List<Contact> contactList;
    Context mContext;

    public InviteContacAdapter(List<Contact> contactList, Context mContext) {
        this.contactList = contactList;
        this.mContext = mContext;
    }

    public List<Contact> getContactList() {
        return contactList;
    }

    public void setContactList(List<Contact> contactList) {
        this.contactList = contactList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_invite_layout,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Contact contact = contactList.get(position);

        holder.tvName.setText(contact.getName());
        holder.tvPhone.setText(contact.getPhone());
    }

    @Override
    public int getItemCount() {
        if(contactList==null||contactList.size()<=0)
            return 0;
        else return contactList.size();

    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private CircleImageView cimgInviteAvatar;
        private TextView tvName, tvPhone;
        private ImageView imgInvite;

        public ViewHolder(View itemView) {
            super(itemView);

            cimgInviteAvatar = (CircleImageView) itemView.findViewById(R.id.img_invite_avatar);
            tvName = (TextView) itemView.findViewById(R.id.tv_invite_name);
            tvPhone= (TextView) itemView.findViewById(R.id.tv_invite_phone);
            imgInvite= (ImageView) itemView.findViewById(R.id.img_invite);

        }
    }
}
