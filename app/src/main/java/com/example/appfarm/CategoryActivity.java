package com.example.appfarm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import static com.example.appfarm.DBqueries.lists;
import static com.example.appfarm.DBqueries.loadFragmentData;
import static com.example.appfarm.DBqueries.loadedCategoriesNames;

public class CategoryActivity extends AppCompatActivity {

    private RecyclerView categoryRecyclerView;
    private List<HomePageModel> homePageFakeModelList =  new ArrayList<>();
    private HomePageAdapter adapter;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        String title = getIntent().getStringExtra("CategoryName");
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

        ////home page fake list
        List<SliderModel>sliderModelFakeList =  new ArrayList<>();
        sliderModelFakeList.add(new SliderModel("null","#ffffff"));
        sliderModelFakeList.add(new SliderModel("null","#ffffff"));
        sliderModelFakeList.add(new SliderModel("null","#ffffff"));
        sliderModelFakeList.add(new SliderModel("null","#ffffff"));
        sliderModelFakeList.add(new SliderModel("null","#ffffff"));

        List<HorizontalProductScrollModel>horizontalProductScrollModelFakeList = new ArrayList<>();
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("","","","",""));
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("","","","",""));
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("","","","",""));
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("","","","",""));
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("","","","",""));
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("","","","",""));
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("","","","",""));
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("","","","",""));

        homePageFakeModelList.add(new HomePageModel(0,sliderModelFakeList));
        homePageFakeModelList.add(new HomePageModel(1,"","#ffffff"));
        homePageFakeModelList.add(new HomePageModel(2,"","#ffffff",horizontalProductScrollModelFakeList));
        homePageFakeModelList.add(new HomePageModel(3,"","#ffffff",horizontalProductScrollModelFakeList));

        ////home page fake list

        categoryRecyclerView = findViewById(R.id.category_recyclerView);
        LinearLayoutManager testingLayoutManager = new LinearLayoutManager(this);
        testingLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        categoryRecyclerView.setLayoutManager(testingLayoutManager);

        adapter = new HomePageAdapter(homePageFakeModelList);

        int listPosition = 0;
        for (int x=0;x < loadedCategoriesNames.size();x++){
            if (loadedCategoriesNames.get(x).equals(title.toUpperCase())){
                listPosition = x;
            }
        }
        if (listPosition == 0){
            loadedCategoriesNames.add(title.toUpperCase());
            lists.add(new ArrayList<HomePageModel>());
            loadFragmentData(categoryRecyclerView,this,loadedCategoriesNames.size() - 1,title);
        } else {
            adapter = new HomePageAdapter(lists.get(listPosition));
        }
        categoryRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search_icon, menu);

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id =  item.getItemId();

        if (id == R.id.imp_search_icon){
            Intent searchIntent = new Intent(this,SearchActivity.class);
            startActivity(searchIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
 
}