package com.identity.arx;

import android.app.SearchManager;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.internal.view.SupportMenu;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics.Event;
import com.identity.arx.db.InstituteDetailTable;
import com.identity.arx.general.ApplicationDialog;
import com.identity.arx.general.ConnectionDetector;
import com.identity.arx.general.MasterActivity;
import com.identity.arx.general.WebUrl;
import com.identity.arx.httpasynctask.AsyncResponse;
import com.identity.arx.httpasynctask.HttpAsyncTask;
import com.identity.arx.objectclass.InstituteVo;
import com.identity.arx.textdrawable.TextDrawable;

import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SelectInstituteActivity extends MasterActivity {
    InstituteListAdapter adapter;
    EditText inputSearch;
    InstituteDetailTable instituteDetailTable;
    ListView instituteList;
    List<InstituteVo> listInstituteVo;
    InstituteVo user;

    class C07541 implements AsyncResponse {
        C07541() {
        }

        public void asyncResponse(ResponseEntity<?> response) {
            try {
                ArrayList<Map<String, String>> usersMap = (ArrayList) response.getBody();
                SelectInstituteActivity.this.instituteDetailTable = new InstituteDetailTable(SelectInstituteActivity.this);
                for (int i = 0; i < usersMap.size(); i++) {
                    Map<String, String> insList = (Map) usersMap.get(i);
                    String InstituteName = (String) insList.get("InstituteName");
                    SelectInstituteActivity.this.user.setDbName((String) insList.get("DbName"));
                    SelectInstituteActivity.this.user.setInstituteName(InstituteName);
                    SelectInstituteActivity.this.instituteDetailTable.addInstitute_Details(SelectInstituteActivity.this.user);
                }
                SelectInstituteActivity.this.listInstituteVo = SelectInstituteActivity.this.instituteDetailTable.getInstituteDetails();
                SelectInstituteActivity.this.adapter = new InstituteListAdapter(SelectInstituteActivity.this.listInstituteVo);
                SelectInstituteActivity.this.instituteList.setAdapter(SelectInstituteActivity.this.adapter);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class C07552 implements OnClickListener {
        C07552() {
        }

        public void onClick(DialogInterface dialog, int which) {
            SelectInstituteActivity.this.finish();
        }
    }

    class C07563 implements OnItemClickListener {
        C07563() {
        }

        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            InstituteVo instituteVo = (InstituteVo) parent.getAdapter().getItem(position);
            SelectInstituteActivity.this.sharedPreferenceEdit.putString("INTITUTE_ID", instituteVo.getDbName());
            SelectInstituteActivity.this.sharedPreferenceEdit.commit();
            Intent intent = new Intent();
            intent.putExtra("INTITUTE_NAME", instituteVo.getInstituteName());
            SelectInstituteActivity.this.setResult(0, intent);
            SelectInstituteActivity.this.finish();
        }
    }

    class C07574 implements OnQueryTextListener {
        C07574() {
        }

        public boolean onQueryTextChange(String newText) {
            try {
                SelectInstituteActivity.this.adapter.getFilter().filter(newText);
            } catch (NullPointerException n) {
                n.printStackTrace();
            }
            System.out.println("on text chnge text: " + newText);
            return true;
        }

        public boolean onQueryTextSubmit(String query) {
            SelectInstituteActivity.this.adapter.getFilter().filter(query);
            return true;
        }
    }

    private class InstituteListAdapter extends BaseAdapter implements Filterable {
        List<InstituteVo> listInstituteVo;
        List<InstituteVo> mStringFilterList;
        ValueFilter valueFilter;

        private class ValueFilter extends Filter {
            private ValueFilter() {
            }

            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                if (constraint == null || constraint.length() <= 0) {
                    results.count = InstituteListAdapter.this.mStringFilterList.size();
                    results.values = InstituteListAdapter.this.mStringFilterList;
                } else {
                    ArrayList<InstituteVo> filterList = new ArrayList();
                    for (int i = 0; i < InstituteListAdapter.this.mStringFilterList.size(); i++) {
                        if (((InstituteVo) InstituteListAdapter.this.mStringFilterList.get(i)).getInstituteName().toUpperCase().contains(constraint.toString().toUpperCase())) {
                            filterList.add((InstituteVo) InstituteListAdapter.this.mStringFilterList.get(i));
                        }
                    }
                    results.count = filterList.size();
                    results.values = filterList;
                }
                return results;
            }

            protected void publishResults(CharSequence constraint, FilterResults results) {
                InstituteListAdapter.this.listInstituteVo = (List) results.values;
                InstituteListAdapter.this.notifyDataSetChanged();
            }
        }

        public InstituteListAdapter(List<InstituteVo> listInstituteVo) {
            this.listInstituteVo = listInstituteVo;
            this.mStringFilterList = listInstituteVo;
        }

        public int getCount() {
            return this.listInstituteVo.size();
        }

        public Object getItem(int position) {
            return this.listInstituteVo.get(position);
        }

        public long getItemId(int position) {
            return (long) this.listInstituteVo.indexOf(getItem(position));
        }

        @NonNull
        public View getView(int position, View convertView, ViewGroup parent) {
            TextDrawable drawable;
            View view = SelectInstituteActivity.this.getLayoutInflater().inflate(R.layout.institute_list_item, parent, false);
            InstituteVo instituteVo = (InstituteVo) this.listInstituteVo.get(position);
            ImageView imageView = (ImageView) view.findViewById(R.id.ivImage);
            TextView textView = (TextView) view.findViewById(R.id.tvDescr);
            if (position % 2 == 0) {
                drawable = TextDrawable.builder().buildRound(String.valueOf(instituteVo.getInstituteName().charAt(0)), SupportMenu.CATEGORY_MASK);
            } else {
                drawable = TextDrawable.builder().buildRound(String.valueOf(instituteVo.getInstituteName().charAt(0)), -16776961);
            }
            textView.setText(instituteVo.getInstituteName());
            imageView.setImageDrawable(drawable);
            return view;
        }

        public Filter getFilter() {
            if (this.valueFilter == null) {
                this.valueFilter = new ValueFilter();
            }
            return this.valueFilter;
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_select_institute);
        setActionBarTitle("Select Institute Name");
        this.instituteList = (ListView) findViewById(R.id.institute_list);
        if (ConnectionDetector.isConnectingToInternet(getApplicationContext())) {
            this.user = new InstituteVo();
            new HttpAsyncTask(this, WebUrl.SELECT_INSTITUTE, this.user, new C07541()).execute(new ResponseEntity[0]);
        } else {
            ApplicationDialog.setMessage("Internet Issue", "PLease check your Internet Connection", "", "OK", this).buildDialog(new C07552());
        }
        this.instituteList.setOnItemClickListener(new C07563());
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.institute_list_menu, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(((SearchManager) getSystemService(Event.SEARCH)).getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new C07574());
        return true;
    }
}
