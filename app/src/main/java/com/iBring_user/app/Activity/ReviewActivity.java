package com.iBring_user.app.Activity;

import android.os.Handler;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.iBring_user.app.R;
import com.iBring_user.app.Adapter.ReviewListAdapter;
import com.iBring_user.app.Utils.PaginationScrollListener;

public class ReviewActivity extends AppCompatActivity {
    RecyclerView recycler;
    ReviewListAdapter reviewListAdapter;
    LinearLayoutManager layoutManager;
    private static final int PAGE_START = 1;
    private boolean isLoading;
    private boolean isLastPage;
    private int TOTAL_PAGES;
    private int currentPages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        recycler=(RecyclerView)findViewById(R.id.Recyclerview);
        reviewListAdapter = new ReviewListAdapter(this);
        currentPages = PAGE_START;
        isLastPage = false;
        isLoading = false;
        // serviceListAdapter.setServiceClickListener(this);
        layoutManager = new LinearLayoutManager(this);
        recycler.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));;
        recycler.setHasFixedSize(true);

        recycler.setAdapter(reviewListAdapter);

        recycler.addOnScrollListener(new PaginationScrollListener(layoutManager)
        {
            @Override
            protected void loadMoreItems()
            {
                isLoading = true;
                currentPages += 1;

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //loadNextPage();
                    }
                }, 1000);
            }

            @Override
            public int getTotalPageCount() {
                return TOTAL_PAGES;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });

    }

}
