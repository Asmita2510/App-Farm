package com.example.appfarm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private SearchView searchView;
    private TextView textView;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchView = findViewById(R.id.search_view);
        textView = findViewById(R.id.textview);
        recyclerView = findViewById(R.id.recycler_view);

        recyclerView.setVisibility(View.VISIBLE);
        LinearLayoutManager layoutManager =  new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(layoutManager);

        final List<HorizontalProductScrollModel> list = new ArrayList<>();
        final List<String> ids = new ArrayList<>();

        Adapter adapter = new Adapter(list);
        recyclerView.setAdapter(adapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String s) {
                list.clear();
                ids.clear();

                String[] tags = s.toLowerCase().split(" ");
                for (String tag : tags){
                    tag.trim();
                    FirebaseFirestore.getInstance().collection("PRODUCTS").whereArrayContains("tags",tag)
                            .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()){
                                for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()){
                                    /* List<HorizontalProductScrollModel> horizontalProductScrollModelList = new ArrayList<>();
                                     List<HorizontalProductScrollModel> viewAllList= new ArrayList<>();
                                     long no_of_products = ((long)documentSnapshot.get("no_of_products"));
                                     for (long x = 1;x < no_of_products+1;x++){*/
                                         HorizontalProductScrollModel model = new HorizontalProductScrollModel(documentSnapshot.getId().toString()
                                                /* ,documentSnapshot.get("product_image_"+x).toString()
                                                 ,documentSnapshot.get("product_title_"+x).toString()
                                                 ,documentSnapshot.get("product_subtitle_"+x).toString()
                                                 ,documentSnapshot.get("product_price_"+x).toString());*/
                                                ,documentSnapshot.get("product_image").toString()
                                                ,documentSnapshot.get("product_title").toString()
                                                ,documentSnapshot.get("product_description").toString()
                                                ,documentSnapshot.get("product_price").toString());

                                         if (!ids.contains(model.getProductID())){
                                             list.add(model);
                                             ids.add(model.getProductID());
                                         }
                                }
                                if (tag.equals(tags[tags.length-1])){
                                    if (list.size()==0){
                                        textView.setVisibility(View.VISIBLE);
                                        recyclerView.setVisibility(View.GONE);
                                    }else {
                                        textView.setVisibility(View.GONE);
                                        recyclerView.setVisibility(View.VISIBLE);
                                        adapter.getFilter().filter(s);
                                    }
                                }
                            }else {
                                String error = task.getException().getMessage();
                                Toast.makeText(SearchActivity.this,error,Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    class Adapter extends HorizontalProductScrollAdapter implements Filterable{

        public Adapter(List<HorizontalProductScrollModel> horizontalProductScrollModelList) {
            super(horizontalProductScrollModelList);
        }

        @Override
        public Filter getFilter() {
            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {

                    ////FILTER LOGIC

                    return null;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    notifyDataSetChanged();
                }
            };
        }
    }
}