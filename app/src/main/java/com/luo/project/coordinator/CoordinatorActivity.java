package com.luo.project.coordinator;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Window;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.luo.project.R;
import com.luo.project.db.InfoDB;
import com.luo.project.entity.Info;
import com.luo.project.recycler.DividerItemDecoration;
import com.luo.project.recycler.ViewHolder;
import com.luo.project.recycler.XAdapter;

import java.util.ArrayList;

public class CoordinatorActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private XAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_coordinator);

        recyclerView = (RecyclerView) findViewById(R.id.my_list);


        mAdapter = new XAdapter<Info>(this, new ArrayList<Info>(), R.layout.item_recycler_view) {
            @Override
            public void convert(ViewHolder holder, Info info) {
                holder.setText(R.id.text, info.getInfoId() + ". " + info.getTitle());
                SimpleDraweeView iamgeView = holder.getView(R.id.image);
                iamgeView.setImageURI(Uri.parse(info.getUrl()));
            }
        };

        recyclerView.setAdapter(mAdapter);

        recyclerView.setItemAnimator(new DefaultItemAnimator());

        //TODO 线性布局
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));

        mAdapter.setOnItemClickListener(new XAdapter.OnItemClickListeners<Info>() {
            @Override
            public void onItemClick(RecyclerView.ViewHolder holder, Info item, int position) {
                Toast.makeText(getApplicationContext(), item.getUrl(), Toast.LENGTH_SHORT).show();
            }
        });

        mAdapter.addAll(InfoDB.getInstance().selectAll(getApplicationContext()));
    }

}
