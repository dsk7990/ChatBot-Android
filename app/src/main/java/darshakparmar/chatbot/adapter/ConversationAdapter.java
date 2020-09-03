package darshakparmar.chatbot.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import darshakparmar.chatbot.R;
import darshakparmar.chatbot.model.ConversationModel;

public class ConversationAdapter extends RecyclerView.Adapter {
    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;
    private static final int VIEW_TYPE_LOADING = 3;

    private Context mContext;
    private List<ConversationModel> mMessageList;
    private int lastPosition = -1;
    private MsgClick msgClick;

    public ConversationAdapter(Context context, List<ConversationModel> messageList, MsgClick msgClick) {
        mContext = context;
        mMessageList = messageList;
        this.msgClick = msgClick;


    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }

    // Determines the appropriate ViewType according to the sender of the message.
    @Override
    public int getItemViewType(int position) {
        ConversationModel message = mMessageList.get(position);


        if (message.getType().equals("other")) {
            // If some other user sent the message
            return VIEW_TYPE_MESSAGE_RECEIVED;
        } else if (message.getType().equals("loading")) {
            // If some other user sent the message
            return VIEW_TYPE_LOADING;
        } else {
            // If the current user is the sender of the message
            return VIEW_TYPE_MESSAGE_SENT;
        }
    }

    // Inflates the appropriate layout according to the ViewType.
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        if (viewType == VIEW_TYPE_MESSAGE_SENT) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_chat_right, parent, false);
            return new SentMessageHolder(view);
        } else if (viewType == VIEW_TYPE_MESSAGE_RECEIVED) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_chat_left, parent, false);
            return new ReceivedMessageHolder(view);
        } else if (viewType == VIEW_TYPE_LOADING) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_loading, parent, false);
            return new LoadingMessageHolder(view);
        }

        return null;
    }

    // Passes the message object to a ViewHolder so that the contents can be bound to UI.
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ConversationModel message = (ConversationModel) mMessageList.get(position);

        switch (holder.getItemViewType()) {

            case VIEW_TYPE_MESSAGE_SENT:
                ((SentMessageHolder) holder).bind(message, position);
                break;
            case VIEW_TYPE_MESSAGE_RECEIVED:
                ((ReceivedMessageHolder) holder).bind(message, position);
                break;
            case VIEW_TYPE_LOADING:
                ((LoadingMessageHolder) holder).bind(message, position);
                break;
        }
    }

    private class LoadingMessageHolder extends RecyclerView.ViewHolder {


        LoadingMessageHolder(View itemView) {
            super(itemView);


        }

        void bind(final ConversationModel message, int position) {

        }
    }

    private class SentMessageHolder extends RecyclerView.ViewHolder {

        public TextView txtRight;
        private LinearLayout linearInput;
        private EditText etInput;
        private Button btnInput;

        SentMessageHolder(View itemView) {
            super(itemView);
            txtRight = (TextView) itemView.findViewById(R.id.txtRightMsg);
            linearInput = (LinearLayout) itemView.findViewById(R.id.linearInput);
            etInput = (EditText) itemView.findViewById(R.id.etInput);
            btnInput = (Button) itemView.findViewById(R.id.btnInput);
        }

        void bind(final ConversationModel message, final int position) {
            setRightAnimation(itemView, position);
            txtRight.setText(message.getMsg());
            if (message.getMsgType().equalsIgnoreCase("input")) {
                linearInput.setVisibility(View.VISIBLE);
                txtRight.setVisibility(View.GONE);
                btnInput.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String mobNo = etInput.getText().toString().trim();
                        if (TextUtils.isEmpty(mobNo)) {
                            Toast.makeText(mContext, "Please enter data.", Toast.LENGTH_SHORT).show();
                        } else {
                            message.setMobNo(mobNo);
                            msgClick.onImageClick(position, message);
                        }
                    }
                });
            } else {
                linearInput.setVisibility(View.GONE);
                txtRight.setVisibility(View.VISIBLE);
            }
        }
    }

    public String getDateCurrentTimeZone(long timestamp) {
        try {
            Calendar cal = Calendar.getInstance();
            TimeZone tz = cal.getTimeZone();
//            AndroidLogger.debug("Time zone: ", tz.getDisplayName());
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
            sdf.setTimeZone(tz);
            String localTime = sdf.format(new Date(timestamp)); // I assume your timestamp is in seconds*1000 and you're converting to milliseconds?
//            AndroidLogger.debug("Time: ", localTime);
            return localTime;
        } catch (Exception e) {
        }
        return "";
    }

    private class ReceivedMessageHolder extends RecyclerView.ViewHolder {

        public TextView txtLeft;

        ReceivedMessageHolder(View itemView) {
            super(itemView);
            txtLeft = (TextView) itemView.findViewById(R.id.txtLeftMsg);

        }

        void bind(final ConversationModel message, int position) {
            txtLeft.setText(message.getMsg());
            setLeftAnimation(itemView, position);
        }
    }
    private void setLeftAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(mContext, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }
    private void setRightAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.item_animation_fall_down);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

   /* @Override
    public void onViewDetachedFromWindow(final RecyclerView.ViewHolder holder)
    {
        ((ReceivedMessageHolder)holder).clearAnimation();
    }
    public void clearAnimation()
    {
        mRootLayout.clearAnimation();
    }*/
//    private boolean checkSameDay(int position) {
//        //Group by Date
//        long previousTs = 0;
//        if (position >= 1) {
//            ConversationModel previousMessage = mMessageList.get(position - 1);
//            previousTs = Long.parseLong(previousMessage.getMsgTimeStamp());
//        }
//        Calendar cal1 = Calendar.getInstance();
//        Calendar cal2 = Calendar.getInstance();
//        cal1.setTimeInMillis(Long.parseLong(mMessageList.get(position).getMsgTimeStamp()));
//        cal2.setTimeInMillis(previousTs);
//        boolean sameDay = cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
//                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
//        if (!sameDay) {
//            TextView dateView = new TextView(mContext);
//            dateView.setText(mMessageList.get(position).getMsgTimeStamp());
////            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
////                    LinearLayout.LayoutParams.WRAP_CONTENT);
////            params.gravity = Gravity.CENTER_HORIZONTAL;
////            dateView.setLayoutParams(params);
////            holder.timeText.addView(dateView);
//        }
//        return sameDay;
//    }
//
//    private void loadImage(final String url, final ImageView img) {
//        if (!TextUtils.isEmpty(url)) {
//            Picasso.get().load(url).networkPolicy(NetworkPolicy.OFFLINE).into(img, new Callback() {
//                @Override
//                public void onSuccess() {
//                }
//
//                @Override
//                public void onError(Exception e) {
//                    Picasso.get()
//                            .load(url)
//                            .into(img, new Callback() {
//                                @Override
//                                public void onSuccess() {
//                                }
//
//                                @Override
//                                public void onError(Exception e) {
//                                    AndroidLogger.error("Picasso", "Could not fetch image");
//                                }
//                            });
//                }
//            });
//        } else {
//
//        }
//
//
//    }
//
//    private void loadGlide(String url, ImageView img) {
////        Glide.with(mContext)
////                .load(url)
//////                .apply(new RequestOptions().placeholder(R.drawable.placeholder_sqare).error(R.drawable.placeholder_sqare))
////                .transition(DrawableTransitionOptions.withCrossFade())
////                .into(img);
//
//        GlideApp.with(mContext)
//                .load(url)
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .transition(DrawableTransitionOptions.withCrossFade())
////                .apply(new RequestOptions().placeholder(R.drawable.placeholder_sqare).error(R.drawable.placeholder_sqare))
//                .into(img);
//    }

    public interface MsgClick {
        public void onImageClick(int pos, ConversationModel conversationModel);
    }
}