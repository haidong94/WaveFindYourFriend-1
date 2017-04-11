package vinsoft.com.wavefindyourfriend.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import vinsoft.com.wavefindyourfriend.R;
import vinsoft.com.wavefindyourfriend.adapter.ViewPagerAdapter;
import vinsoft.com.wavefindyourfriend.fragment.ConversationFragment;
import vinsoft.com.wavefindyourfriend.fragment.FriendFragment;

public class MainActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initWidget();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initParams();
    }

    private void initParams() {
        String userId = getIntent().getStringExtra("UserID");

        ConversationFragment conversationFragment = ConversationFragment.newInstance(userId);
        FriendFragment friendFragment = FriendFragment.newInstance(userId);

        ViewPagerAdapter viewAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewAdapter.addFragment(conversationFragment, "Messenger");
        viewAdapter.addFragment(friendFragment, "Friends");
        viewPager.setAdapter(viewAdapter);

        tabLayout.setupWithViewPager(viewPager);
    }

    public void initWidget(){
        tabLayout= (TabLayout) findViewById(R.id.tab_layout);
        toolbar= (Toolbar) findViewById(R.id.toolbar);
        viewPager= (ViewPager) findViewById(R.id.viewpager);
    }


}
