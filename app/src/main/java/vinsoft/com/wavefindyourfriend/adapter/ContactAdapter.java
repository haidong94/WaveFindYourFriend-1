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
 * Created by kienmit95 on 05/04/17.
 */

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder>{

    private List<Contact> contactList;
    private Context context;
    private List<String> listFriend;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_contact_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Contact c = contactList.get(position);
        for (String s : listFriend){
            if(!c.getPhone().equals(s)) {
                holder.imgAdd.setVisibility(View.VISIBLE);
                holder.imgContactAvatar.setImageResource(R.drawable.ic_profile);
            }
            else {
                /*holder.imgAdd.setVisibility(View.GONE);
                roof.child("database").child("Person").child(c.getPhone()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Person c=dataSnapshot.getValue(Person.class);
                        String url=c.getImage();
                        if(url==null)
                            holder.avatar.setImageResource(R.drawable.ic_profile_);
                        else
                        {
                            Glide.with(context).load(url).thumbnail(0.5f)
                                    .crossFade().diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .into(holder.avatar);
                        }
                        //
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });
                break;*/
            }
        }


        holder.tvContactName.setText(String.valueOf(contactList.get(position).getName()));
        holder.tvContactPhone.setText(String.valueOf(contactList.get(position).getPhone()));
    }

    @Override
    public int getItemCount() {
        if (contactList.size()<=0||contactList==null)
            return 0;
        else return contactList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvContactName,tvContactPhone;
        ImageView imgAdd;
        CircleImageView imgContactAvatar;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            tvContactName= (TextView) itemView.findViewById(R.id.tv_contact_name);
            tvContactPhone= (TextView) itemView.findViewById(R.id.tv_contact_phone);
            imgAdd= (ImageView) itemView.findViewById(R.id.img_add);
            imgContactAvatar= (CircleImageView) itemView.findViewById(R.id.img_contact_avatar);

        }

        @Override
        public void onClick(View v) {
            //Toast.makeText(v.getContext(), "Clicked Country Position = " + getPosition(), Toast.LENGTH_SHORT).show();
        }
    }
}
