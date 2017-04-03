package vinsoft.com.wavefindyourfriend.adapter;

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import vinsoft.com.wavefindyourfriend.R;
import vinsoft.com.wavefindyourfriend.model.ChatMessage;
import vinsoft.com.wavefindyourfriend.myinterface.IDeleteMessage;

/**
 * Created by kienmit95 on 3/30/17.
 */

public class MessageApdapter extends RecyclerView.Adapter<MessageApdapter.ViewHolder> {

    private List<ChatMessage> listMessage;
    private Context mContext;
    private String currentSendId;
    Date dateGroup=null;
    IDeleteMessage  iDeleteMessage= null;

    int i=0;


    public List<ChatMessage> getListMessage() {
        return listMessage;
    }

    public void setListMessage(List<ChatMessage> listMessage) {
        this.listMessage = listMessage;
    }

    public Context getmContext() {
        return mContext;
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

    public MessageApdapter(List<ChatMessage> listMessage, Context mContext, String currentSendId, IDeleteMessage iDeleteMessage) {
        this.listMessage = listMessage;
        this.mContext = mContext;
        this.currentSendId = currentSendId;
        this.iDeleteMessage = iDeleteMessage;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.recyclerview_message_custom, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

        ChatMessage message = listMessage.get(position);

        setLayoutChat(holder, message.getPersonSendID(), message.getMessageVision());

        holder.tvMessage.setText(message.getContentMessage());
        holder.tvInfo.setText(message.getTimeSend());
        try {
            if(!formatter.parse(message.getDateSend()).equals(formatter.parse(listMessage.get(i).getDateSend()))) {
                holder.tvDate.setVisibility(View.GONE);
                //holder.tvDate.setText(message.getDateSend());
                //dateGroup=formatter.parse(message.getDateSend());
                i=position;
            }
            else{
                holder.tvDate.setVisibility(View.VISIBLE);
                holder.tvDate.setText(message.getDateSend());
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        if(listMessage==null||listMessage.size()<=0)
            return 0;
        return listMessage.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvInfo, tvMessage;
        LinearLayout llBubbleMessage;
        //RelativeLayout rllContent;
        CircleImageView imgAvatar;
        TextView tvDate;

        public ViewHolder(final View itemView) {
            super(itemView);


            tvInfo = (TextView) itemView.findViewById(R.id.tv_info);
            tvMessage = (TextView) itemView.findViewById(R.id.tv_message);
            //rllContent = (RelativeLayout) itemView.findViewById(R.id.rll_content);
            llBubbleMessage = (LinearLayout) itemView.findViewById(R.id.ll_bubble_message);
            imgAvatar = (CircleImageView) itemView.findViewById(R.id.img_message_avatar);
            tvDate= (TextView) itemView.findViewById(R.id.tv_date);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    PopupMenu popup = new PopupMenu(itemView.getContext(), itemView);
                    //Inflating the Popup using xml file
                    popup.getMenuInflater()
                            .inflate(R.menu.popup_mess_menu, popup.getMenu());

                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            if(item.getItemId()==R.id.menu_delete){
                                deleteMessage(getAdapterPosition());
                            }
                            if(item.getItemId()==R.id.menu_copy){
                                Toast.makeText(itemView.getContext(),"Copy",Toast.LENGTH_LONG).show();
                            }
                            return true;
                        }
                    });
                    popup.show();
                    return true;
                }
            });

        }
    }

    public void addMessage(ChatMessage msg) {
        listMessage.add(msg);
        this.notifyDataSetChanged();
    }

    public void deleteMessage(int position) {

        ChatMessage message=listMessage.remove(position);
        this.notifyItemRemoved(position);

        iDeleteMessage.onDeteteMessage(message.getMessageID());
    }

    public void setLayoutChat(ViewHolder viewHolder, String sendId, String messageVision) {
       /* if(dateTemp!=dateGroup||dateGroup==null){
            dateGroup=dateTemp;
            viewHolder.tvDate.setVisibility(View.VISIBLE);
            viewHolder.tvDate.setText(dateTemp.toString());
        }
        else {
            viewHolder.tvDate.setVisibility(View.GONE);
        }*/
        if (!currentSendId.equals(sendId)) {
            /*if(messageVision==3){
                viewHolder.imgAvatar.setVisibility(View.GONE);
                viewHolder.llBubbleMessage.setVisibility(View.GONE);
                viewHolder.tvDate.setVisibility(View.GONE);
                viewHolder.tvInfo.setVisibility(View.GONE);
                viewHolder.tvMessage.setVisibility(View.GONE);
                return;
            }*/
            viewHolder.llBubbleMessage.setBackgroundResource(R.drawable.out_message_bg);

            RelativeLayout.LayoutParams paramsAvatar = (RelativeLayout.LayoutParams) viewHolder.imgAvatar.getLayoutParams();
           /* paramsAvatar.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
            paramsAvatar.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
            paramsAvatar.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);*/
            paramsAvatar.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,0);
            paramsAvatar.addRule(RelativeLayout.ALIGN_PARENT_LEFT,RelativeLayout.TRUE);
            paramsAvatar.addRule(RelativeLayout.BELOW,viewHolder.tvDate.getId());
            viewHolder.imgAvatar.setLayoutParams(paramsAvatar);

            RelativeLayout.LayoutParams llParams = (RelativeLayout.LayoutParams) viewHolder.llBubbleMessage.getLayoutParams();
            /*llParams.addRule(RelativeLayout.START_OF, 0);
            llParams.addRule(RelativeLayout.LEFT_OF, 0);
            llParams.addRule(RelativeLayout.END_OF, viewHolder.imgAvatar.getId());
            llParams.addRule(RelativeLayout.RIGHT_OF, viewHolder.imgAvatar.getId());*/
            llParams.addRule(RelativeLayout.LEFT_OF,0);
            llParams.addRule(RelativeLayout.RIGHT_OF,viewHolder.imgAvatar.getId());
            llParams.addRule(RelativeLayout.BELOW,viewHolder.tvDate.getId());
            viewHolder.llBubbleMessage.setLayoutParams(llParams);

            RelativeLayout.LayoutParams paramsInfo = (RelativeLayout.LayoutParams) viewHolder.tvInfo.getLayoutParams();
            paramsInfo.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
            paramsInfo.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
            paramsInfo.addRule(RelativeLayout.BELOW, viewHolder.llBubbleMessage.getId());
            viewHolder.tvInfo.setLayoutParams(paramsInfo);

        } else {

            if(messageVision.equals(currentSendId)){
                viewHolder.imgAvatar.setVisibility(View.GONE);
                viewHolder.llBubbleMessage.setVisibility(View.GONE);
                viewHolder.tvDate.setVisibility(View.GONE);
                viewHolder.tvInfo.setVisibility(View.GONE);
                viewHolder.tvMessage.setVisibility(View.GONE);
                return;
            }
            viewHolder.llBubbleMessage.setBackgroundResource(R.drawable.in_message_bg);

            RelativeLayout.LayoutParams paramsAvatar = (RelativeLayout.LayoutParams) viewHolder.imgAvatar.getLayoutParams();
           /* paramsAvatar.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
            paramsAvatar.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0);
            paramsAvatar.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);*/
            paramsAvatar.addRule(RelativeLayout.ALIGN_PARENT_LEFT,0);
            paramsAvatar.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,RelativeLayout.TRUE);
            paramsAvatar.addRule(RelativeLayout.BELOW,viewHolder.tvDate.getId());
            paramsAvatar.addRule(RelativeLayout.BELOW,viewHolder.tvDate.getId());
            viewHolder.imgAvatar.setLayoutParams(paramsAvatar);

            RelativeLayout.LayoutParams llParams = (RelativeLayout.LayoutParams) viewHolder.llBubbleMessage.getLayoutParams();
            /*llParams.addRule(RelativeLayout.START_OF, viewHolder.imgAvatar.getId());
            llParams.addRule(RelativeLayout.LEFT_OF, viewHolder.imgAvatar.getId());
            llParams.addRule(RelativeLayout.END_OF, 0);
            llParams.addRule(RelativeLayout.RIGHT_OF, 0);*/
            llParams.addRule(RelativeLayout.RIGHT_OF,0);
            llParams.addRule(RelativeLayout.LEFT_OF,viewHolder.imgAvatar.getId());
            llParams.addRule(RelativeLayout.BELOW,viewHolder.tvDate.getId());
            viewHolder.llBubbleMessage.setLayoutParams(llParams);

            RelativeLayout.LayoutParams paramsInfo = (RelativeLayout.LayoutParams) viewHolder.tvInfo.getLayoutParams();
            paramsInfo.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0);
            paramsInfo.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);

            paramsInfo.addRule(RelativeLayout.BELOW, viewHolder.llBubbleMessage.getId());
            viewHolder.tvInfo.setLayoutParams(paramsInfo);


        }
    }
}
