package com.nadim.csedashboard;

import android.app.SearchManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nadim.csedashboard.dataset.UserProfileSet;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class UserRequest extends AppCompatActivity {

    List<UserProfileSet> userProfileSets = new ArrayList<>();
    List<UserProfileSet> original = new ArrayList<>();
    BaseAdapter adapter;
    //private ItemFilter mFilter = new ItemFilter();


    String user = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_request);

        SharedPreferences settings = getSharedPreferences("user_data", 0);
        user = settings.getString("userid","");

        ListView listView = findViewById(R.id.listView_userRequest);

        DatabaseReference uref = FirebaseDatabase.getInstance().getReference().child("dashboarduser");
        uref.keepSynced(true);

        uref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot session : dataSnapshot.getChildren()) {
                    for (DataSnapshot id : session.getChildren()) {

                        userProfileSets.add(id.getValue(UserProfileSet.class));
                        original.add(id.getValue(UserProfileSet.class));

                        Log.d("baal", String.valueOf(id));

                    }
                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        adapter = new BaseAdapter() {


            @Override
            public int getCount() {
                return userProfileSets.size();
            }

            @Override
            public Object getItem(int i) {
                return null;
            }

            @Override
            public long getItemId(int i) {
                return 0;
            }

            @Override
            public View getView(final int i, View view, ViewGroup viewGroup) {

                LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                view = layoutInflater.inflate(R.layout.model_user_approve, null, true);

                TextView textView[] = new TextView[10];
                Button button[] = new Button[4];

                textView[0] = view.findViewById(R.id.u_name);
                textView[1] = view.findViewById(R.id.u_id);
                textView[2] = view.findViewById(R.id.u_ss);
                textView[3] = view.findViewById(R.id.u_mobile);
                textView[4] = view.findViewById(R.id.u_email);
                textView[5] = view.findViewById(R.id.u_adress);
                textView[6] = view.findViewById(R.id.u_type);
                textView[7] = view.findViewById(R.id.u_editreq);
                textView[8] = view.findViewById(R.id.u_approve);

                button[0] = view.findViewById(R.id.button_ua_teacher);
                button[1] = view.findViewById(R.id.button_ua_admin);
                button[2] = view.findViewById(R.id.button_ua_user);
                button[3] = view.findViewById(R.id.button_ua_reject);


                textView[0].setText(userProfileSets.get(i).getName());
                textView[1].setText(userProfileSets.get(i).getId());
                textView[2].setText(userProfileSets.get(i).getSession());
                textView[3].setText(userProfileSets.get(i).getMobile());
                textView[4].setText(userProfileSets.get(i).getEmail());
                textView[5].setText(userProfileSets.get(i).getAddress());
                textView[6].setText("User Type: " + userProfileSets.get(i).getUsertype());
                textView[7].setText("Edit Request Status: " + userProfileSets.get(i).getEditconf());
                textView[8].setText("Approved by: " + userProfileSets.get(i).getApprovedby());

                button[0].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        UserProfileSet userProfileSet = new UserProfileSet(userProfileSets.get(i).getName(), userProfileSets.get(i).getId(), userProfileSets.get(i).getSession(), userProfileSets.get(i).getAddress(), userProfileSets.get(i).getMobile(), userProfileSets.get(i).getEmail(), "approve", "teacher", user, userProfileSets.get(i).getSignuptime(), userProfileSets.get(i).getPassword());
                        FirebaseDatabase.getInstance().getReference()
                                .child("dashboarduser").child(userProfileSets.get(i).getSession()).child(userProfileSets.get(i).getId()).setValue(userProfileSet)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(UserRequest.this, userProfileSets.get(i).getName() + "Approved", Toast.LENGTH_LONG).show();
                                    }
                                });
                    }
                });

                button[1].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        UserProfileSet userProfileSet = new UserProfileSet(userProfileSets.get(i).getName(), userProfileSets.get(i).getId(), userProfileSets.get(i).getSession(), userProfileSets.get(i).getAddress(), userProfileSets.get(i).getMobile(), userProfileSets.get(i).getEmail(), "approve", "admin", user,  userProfileSets.get(i).getSignuptime(), userProfileSets.get(i).getPassword());
                        FirebaseDatabase.getInstance().getReference()
                                .child("dashboarduser").child(userProfileSets.get(i).getSession()).child(userProfileSets.get(i).getId()).setValue(userProfileSet)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(UserRequest.this, userProfileSets.get(i).getName() + "Approved", Toast.LENGTH_LONG).show();
                                    }
                                });
                    }
                });

                button[2].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        UserProfileSet userProfileSet = new UserProfileSet(userProfileSets.get(i).getName(), userProfileSets.get(i).getId(), userProfileSets.get(i).getSession(), userProfileSets.get(i).getAddress(), userProfileSets.get(i).getMobile(), userProfileSets.get(i).getEmail(), "approve", "student", user,  userProfileSets.get(i).getSignuptime(), userProfileSets.get(i).getPassword());
                        FirebaseDatabase.getInstance().getReference()
                                .child("dashboarduser").child(userProfileSets.get(i).getSession()).child(userProfileSets.get(i).getId()).setValue(userProfileSet)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(UserRequest.this, userProfileSets.get(i).getName() + "Approved", Toast.LENGTH_LONG).show();
                                    }
                                });
                    }
                });

                return view;
            }

/*            class ItemFilter extends Filter {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {

                    String filterString = constraint.toString().toLowerCase();

                    FilterResults results = new FilterResults();

                    //final List<UserProfileSet> original = userProfileSets;

                    //final List<UserProfileSet> list = userProfileSets;


                    int count = original.size();
                    final ArrayList<UserProfileSet> nlist = new ArrayList<UserProfileSet>(count);


                    for (UserProfileSet u : original) {

                        if(u.getId().equals(filterString))
                        {
                            nlist.add(u);
                        }
                    }

                    results.values = nlist;
                    results.count = nlist.size();

                    return results;
                }

                @SuppressWarnings("unchecked")
                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    userProfileSets = (ArrayList<UserProfileSet>) results.values;
                    adapter.notifyDataSetChanged();
                }

            }*/

        };


        listView.setAdapter(adapter);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.user_req, menu);

        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));


        SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
            public boolean onQueryTextChange(String newText) {
                // this is your adapter that will be filtered
                //adapter.
                List<UserProfileSet> sets = new ArrayList<>();
                //List<UserProfileSet> backup = new ArrayList<>();
                //boolean issearched = false;
                for (UserProfileSet u : original) {
                    if (u.getId().contains(newText)) {
                        sets.add(u);
                    }
                }

                userProfileSets.clear();
                userProfileSets.addAll(sets);
                adapter.notifyDataSetChanged();

                if (newText.isEmpty()) {
                    userProfileSets.clear();
                    userProfileSets.addAll(original);
                    adapter.notifyDataSetChanged();
                }


                return true;
            }

            public boolean onQueryTextSubmit(String query) {

                //Here u can get the value "query" which is entered in the search box.
                //showAlertbox(query);


                return true;
            }
        };
        searchView.setOnQueryTextListener(queryTextListener);

        return true;
    }


    //List<UserProfileSet> original = userProfileSets;


}
