package darshakparmar.chatbot;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import darshakparmar.chatbot.adapter.ConversationAdapter;
import darshakparmar.chatbot.model.ConversationModel;

public class MainActivity extends AppCompatActivity implements ConversationAdapter.MsgClick {
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private LinearLayoutManager layoutManager;
    private ArrayList<ConversationModel> list;
    private ConversationAdapter conversationAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        init();

    }

    private void init() {
        layoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false);
//        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(false);
        list = new ArrayList<>();
        conversationAdapter = new ConversationAdapter(MainActivity.this, list, MainActivity.this);
        list.addAll(getLoadingList());
        recyclerView.setAdapter(conversationAdapter);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                list.remove(0);
                conversationAdapter.notifyItemRemoved(0);
                list.add(new ConversationModel("other", "Welcome to App, a simple revolutionary Restaurant Discount App."));
                conversationAdapter.notifyItemInserted(0);
                conversationAdapter.notifyDataSetChanged();
            }
        }, 1500);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                list.add(list.size(), new ConversationModel("other", "Let me know you first. What's your mobile number?"));
                conversationAdapter.notifyDataSetChanged();
            }
        }, 2000);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                list.add(list.size(), new ConversationModel("me", "Let me know you first. What's your mobile number?", "input", "", ""));
                conversationAdapter.notifyDataSetChanged();
            }
        }, 2500);
    }

    private ArrayList<ConversationModel> generateList() {
        ArrayList<ConversationModel> list = new ArrayList<>();
        list.add(new ConversationModel("other", "Hey"));
        list.add(new ConversationModel("me", "Hey"));
        return list;
    }

    private ArrayList<ConversationModel> getLoadingList() {
        ArrayList<ConversationModel> list = new ArrayList<>();
        list.add(new ConversationModel("loading", ""));

        return list;
    }


    @Override
    public void onImageClick(int pos, ConversationModel conversationModel) {
        list.remove(list.size() - 1);
        conversationAdapter.notifyItemRemoved(list.size() - 1);
        list.add(list.size(), new ConversationModel("other", "Thank you for submitting your mobile number. " + conversationModel.getMobNo()));
        conversationAdapter.notifyDataSetChanged();
    }
}
