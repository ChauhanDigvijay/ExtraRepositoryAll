package vthink.co.in.myapplication;

import android.os.Parcelable;
import android.support.annotation.BoolRes;
import android.support.annotation.StringDef;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity {
    public static JSONArray originalJsArray = new JSONArray();
    public static ArrayList<String> oriArrayList = new ArrayList<String>();
    public static ArrayList<String> finArrayList = new ArrayList<String>();
    public static ArrayList<String> coreArrayList = new ArrayList<String>();
    public static ArrayList<Integer> clickCount = new ArrayList<Integer>();
    public static ArrayList<String> idCount = new ArrayList<String>();
    public static ArrayList<String> selectedIds = new ArrayList<String>();
    public static ArrayList<String> selectedHeaderIds = new ArrayList<>();
    public static HashMap<String, Integer> hm = new HashMap<String, Integer>();
    public static HashMap<String, Integer> hmParent = new HashMap<String, Integer>();
    public static HashMap<String, String> hmIndexPos = new HashMap<>();
    public static int lastClickedIndex = 0;
    public static int lastClickedIndexId,lastClickParentIndexId = 0;
    private RecyclerView recyclerView;
    private Adapter mAdapter;
    public static String lastClickParentid = null;
    public static int mn = 0;


    public static JSONArray oriArray = new JSONArray();
    public static JSONArray jss = new JSONArray();
    public static JSONArray oss = new JSONArray();
    public static JSONArray rss = new JSONArray();
    public static String sxml;
    public static boolean atFirst = false;
    public static String sName = "";
    public static int ccpos = 0;
    public static boolean executeFun = false;
    public static Parcelable recyclerViewState;
    public static int mScrollY, mStateScrollY;
    public String ARGS_SCROLL_Y = "gety";
    public static String subshow = "remove";
    public static Boolean checkBoxType = true;
    public static String arrayManipulation = "manipulate";
    public static ArrayList<String> clickedIndexPos = new ArrayList<>();
    public final int recyclerHeader = 1;
    public static int clickedChildRmCount = 0;
    public static int clickedParentRmCount = 0;
    public static ArrayList<Integer> clickedChildRmCountArr = new ArrayList<Integer>();
    public static int lastClickParentIndex = 0;
    public static String isHeaderClick = "no";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_layout);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        if (savedInstanceState != null) {
            mScrollY = savedInstanceState.getInt(ARGS_SCROLL_Y);
        }
            init(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(ARGS_SCROLL_Y, mScrollY);
        outState.putString("original",originalJsArray.toString());
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        try {
            originalJsArray = new JSONArray(savedInstanceState.getString("original"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void init(Bundle savedInstanceState) {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        clickCount = new ArrayList<>();

        try {
            InputStream fileStream = getResources().openRawResource(R.raw.jsondata1);
            sxml = readTextFile(fileStream);
            JSONObject json = new JSONObject(sxml.trim());
            jss.put(json);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(savedInstanceState == null)
        oriArrayList = prepareArrayList(arrayListJsonConv(firstVisibleItems(jsonPrepare(jss))));
        else
        oriArrayList = prepareArrayList(originalJsArray);
        coreArrayList = oriArrayList;
        mAdapter = new Adapter();
        GridLayoutManager manager =
                new GridLayoutManager(this, 3);

/**
 * Helper class to set span size for grid items based on orientation and device type
 */
        GridLayoutManager.SpanSizeLookup onSpanSizeLookup = new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                Adapter aa = new Adapter();
                return aa.getItemViewType(position) == recyclerHeader ? 1 : 3;
            }
        };
        manager.setSpanSizeLookup(onSpanSizeLookup);
        recyclerView.setLayoutManager(manager);



        recyclerView.setAdapter(mAdapter);

        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mScrollY += dy;
            }
        });
    }

    String clickedParentId(int ppos){

        int h = 0;
        String parentId = null;
        JSONArray childJs = new JSONArray();

        for(int l=0;l<originalJsArray.length();l++) {
            if (originalJsArray.optJSONObject(ppos).has("options")) {
                parentId = originalJsArray.optJSONObject(ppos).optString("id");
                return parentId;
            }else {
                h+=1;
                ppos--;
            }
        }
        return parentId;
    }



    ArrayList arraylistAdd(int clickedIndex, int id,String parentId) {
        ArrayList<String> subResult = new ArrayList<String>();
        int countincrementor = 0;
        int skipper = 0;
        int count = 0;
        int clickPos = 0;
        int idd = id;
        String pid = parentId;
        finArrayList = new ArrayList<>();
        JSONArray iterJson = new JSONArray();
        for (int i = 0; i < oriArrayList.size(); i++) {
            if(arrayManipulation.equals("manipulate")) {
                finArrayList.add(oriArrayList.get(i));
            }
            else{
                if(skipper<1) {
                    finArrayList.add(oriArrayList.get(i));
                }else{
                    skipper-=1;
                }
            }
            if (clickedIndex == i) {
                if (originalJsArray.optJSONObject(i).has("options")) {
                    arrayManipulation = "manipulate";
                    subResult = subJsonArryLst(i,idd,pid);
                    for (int k = 0; k < subResult.size(); k++) {
                        finArrayList.add(subResult.get(k));
                    }
                } else {
                    hmIndexPos.put(originalJsArray.optJSONObject(i).optString("id"),String.valueOf(clickedIndex));
                    clickedIndexPos.add(String.valueOf(clickedIndex));
                    count =  Integer.parseInt(childCount(clickedIndex).split("@@")[0].toString());
                    clickPos = Integer.parseInt(childCount(clickedIndex).split("@@")[1].toString());
                    countincrementor = i;
                    arrayManipulation = "dont";
                    for (int r = 0; r < count - clickPos; r++) {
                        skipper +=1;
                        countincrementor++;
                        finArrayList.add(oriArrayList.get(countincrementor));
                    }
                    subResult = subJsonArryLst(i, idd,pid);
                    for (int k = 0; k < subResult.size(); k++) {
                        finArrayList.add(subResult.get(k));
                    }
                }
            }
        }
        oriArrayList = new ArrayList<>();
        oriArrayList = finArrayList;
        return finArrayList;
    }

   private String mandatoryCheck(int x){
       String mand = "";
       JSONArray childJs = new JSONArray();

           for(int l=0;l<originalJsArray.length();l++) {
               if (originalJsArray.optJSONObject(x).has("options")) {
                   mand =  originalJsArray.optJSONObject(x).optString("mandatory").toString();
                   return mand;
               }else {
                   x--;
               }
           }
       return mand;
   }

    private String childCount(int x) {
        int h = 0;
        JSONArray childJs = new JSONArray();
        try {

            for(int l=0;l<originalJsArray.length();l++) {
                if (originalJsArray.optJSONObject(x).has("options")) {
                    childJs = new JSONArray(originalJsArray.optJSONObject(x).optString("options").toString());
                    return childJs.length()+"@@"+h;
                }else {
                    h+=1;
                    x--;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return childJs.length()+"@@"+h;
    }



    ArrayList arrayListRemoveParent(int clickedIndex,int remove)
    {

        int removecount,removeClickPos =0;
        for (int i = 0; i < oriArrayList.size(); i++) {
            if (clickedIndex == i) {
                removecount = Integer.parseInt(childCount(clickedIndex).split("@@")[0].toString());
                removeClickPos = Integer.parseInt(childCount(clickedIndex).split("@@")[1].toString());
                            for(int l=0;l<remove;l++){
                                oriArrayList.remove(i+(removecount-removeClickPos)+1);
                            }
            }
        }
        return oriArrayList;
      }



    ArrayList arraylistRemove(int clickedIndex,int id) {
        ArrayList<String> removeResult = new ArrayList<>();
        finArrayList = new ArrayList<>();
        JSONArray removeJss = new JSONArray();
        JSONArray itorJson = new JSONArray();
        JSONArray removeSubJss = new JSONArray();
        int removecount = 0;
        int removeClickPos = 0;
        int removeFromBase =0;
        for (int i = 0; i < oriArrayList.size(); i++) {
            if (clickedIndex == i) {
                try {
                        removecount = Integer.parseInt(childCount(clickedIndex).split("@@")[0].toString());
                        removeClickPos = Integer.parseInt(childCount(clickedIndex).split("@@")[1].toString());

                        if (originalJsArray.getJSONObject(i).has("modifiers")) {
                            removeJss = new JSONArray(originalJsArray.optJSONObject(i).optString("modifiers"));

                            for (int z = 0; z < removeJss.length(); z++) {
                                removeFromBase+=1;
                                oriArrayList.remove(i + (removecount - removeClickPos) + 1);
                            }
                            for (int h = 0; h < removeJss.length(); h++) {
                                if (removeJss.optJSONObject(h).has("options")) {
                                    removeSubJss = new JSONArray(removeJss.optJSONObject(h).optString("options"));
                                }
                                {
                                for (int y = 0; y < removeSubJss.length(); y++) {
                                    removeFromBase+=1;
                                    oriArrayList.remove(i + (removecount - removeClickPos) + 1);
                                }


                                    if(clickedIndexPos!=null)
                                        clickedIndexPos.removeAll(clickedIndexPos);
                                }
                            }
                        }
//                        }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        clickedChildRmCount = clickedChildRmCount-removeFromBase;
        return oriArrayList;
    }


    ArrayList removeSubList(int posstion)
    {
        for (int i = 0; i < oriArrayList.size(); i++) {
            if (posstion == i){

            }
        }
       return oriArrayList;
    }

    JSONArray arrayListJsonConv(ArrayList<String> arralistFinal) {
        try {
            originalJsArray = new JSONArray(arralistFinal.toString().trim());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return originalJsArray;
    }

    JSONArray escapeSpecialOccurance(String s) {
        JSONArray properJson = new JSONArray();
        String jsonFormattedString = s.replaceAll("\\\\", "");
        try {
            properJson = new JSONArray(jsonFormattedString);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return properJson;
    }


    ArrayList subJsonArryLst(int pos, int id, String pid) {
        ArrayList<String> subList = new ArrayList<String>();
        JSONArray rss = new JSONArray();
        try {
            if (originalJsArray.optJSONObject(pos).has("options")) {
                oss = new JSONArray(originalJsArray.optJSONObject(pos).optString("options").toString().trim());
                for(int i=0;i<oss.length();i++){
                    oss.optJSONObject(i).put("mandatorychild",originalJsArray.optJSONObject(pos).optString("mandatory"));
                    oss.optJSONObject(i).put("parentid",pid);
                }
                subList = prepareArrayList(oss);
            } else {
                oss = new JSONArray(originalJsArray.getJSONObject(pos).optString("modifiers").toString().trim());

                rss = new JSONArray(childJsonArray(oss,pos,pid).toString());
                subList = prepareArrayList(rss);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        hm.put(String.valueOf(id),clickedChildRmCount+subList.size());

        hmParent.put(String.valueOf(pid),clickedParentRmCount = clickedParentRmCount+subList.size());
        return subList;
    }


    JSONArray jsonPrepare(JSONArray jj) {
        try {
            originalJsArray = new JSONArray(jj.getJSONObject(0).optString("optiongroups").toString().trim());
            }

         catch (JSONException e) {
            e.printStackTrace();
        }
        return originalJsArray;
    }


    ArrayList firstVisibleItems(JSONArray corejson){
        JSONArray childjson = new JSONArray();
        ArrayList<String> al = new ArrayList<String>();
        int k=0;
        String s = "";
        String id = "";
        for (int i = 0; i < corejson.length(); i++) {
            s = corejson.optJSONObject(i).optString("mandatory");
            id = corejson.optJSONObject(i).optString("id");
            k=i;
            try {
                al.add(corejson.getString(i));
                childjson = new JSONArray(corejson.optJSONObject(i).optString("options"));
                for(int j=0;j<childjson.length();j++) {
                    childjson.optJSONObject(j).put("mandatorychild",s);
                    childjson.optJSONObject(j).put("level","one");
                    childjson.optJSONObject(j).put("parentid",id);
                    al.add(childjson.getString(j));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return al;
    }





    JSONArray prepareJson(String str) {

        try {
            oriArray = new JSONArray(str);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return oriArray;
    }


    ArrayList<String> prepareArrayList(JSONArray jsonArray) {
        ArrayList<String> al = new ArrayList<String>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                al.add(jsonArray.getString(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return al;
    }


//    ArrayList<String> removeArrayList(JSONArray jsonArray){
//        ArrayList<String> al = new ArrayList<String>();
//        for (int i = 0; i < jsonArray.length(); i++) {
//            try {
//                al.add(jsonArray.getString(i));
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//        return al;
//    }


    JSONArray addId(JSONArray originalJsArray){
        JSONArray mop = new JSONArray();
        JSONArray shallow = new JSONArray();
        for(int i=0;i<originalJsArray.length();i++)
        {
            try {
                originalJsArray.getJSONObject(i).put("parentid",originalJsArray.optJSONObject(i).optString("id"));
                if(originalJsArray.optJSONObject(i).has("options"))
                {
                    JSONArray jop = new JSONArray(originalJsArray.optJSONObject(i).getString("options"));
                    for(int j=0;j<jop.length();j++){
                        originalJsArray.getJSONObject(j).put("parentid",originalJsArray.optJSONObject(i).optString("id"));
                        if(jop.optJSONObject(j).has("modifiers"))
                         mop = new JSONArray(jop.optJSONObject(j).optString("modifiers"));
                        for(int u=0;u<mop.length();u++){
                            originalJsArray.optJSONObject(u).put("parentid",originalJsArray.optJSONObject(i).optString("id"));
                            if(mop.optJSONObject(u).has("options")){
                                shallow = new JSONArray(mop.optJSONObject(u).optString("options"));
                                addId(shallow);
                            }
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return originalJsArray;
    }


    public String readTextFile(InputStream inputStream) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        byte buf[] = new byte[1024];
        int len;
        try {
            while ((len = inputStream.read(buf)) != -1) {
                outputStream.write(buf, 0, len);
            }
            outputStream.close();
            inputStream.close();
        } catch (IOException e) {
        }
        return outputStream.toString();
    }


    public void recyclerViewOri(String orri, int positn) {
        if (orri.equals("vertical")) {
            recyclerView.findViewHolderForAdapterPosition(positn);

            LinearLayoutManager verticalLayoutmanager
                    = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(verticalLayoutmanager);
        } else {
            LinearLayoutManager horizontalLayoutManagaer
                    = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);
            recyclerView.setLayoutManager(horizontalLayoutManagaer);
        }
    }

    JSONArray childJsonArray(JSONArray oss,int pos,String pid) {
        JSONArray tempJss = new JSONArray();
        JSONArray css1 = new JSONArray();
        JSONArray opp1 = new JSONArray();
        String mandatory = "";
        String bgName = "";
        String parentId =pid;
        try {
            for (int i = 0; i < oss.length(); i++) {
                bgName = originalJsArray.optJSONObject(pos).optString("name")+":";
                mandatory = oss.optJSONObject(i).optString("mandatory");
                oss.optJSONObject(i).put("bgname",bgName);
                tempJss.put(oss.get(i));
//                if(childExistence.equals("true")) {
                css1 = new JSONArray(oss.optJSONObject(i).optString("options").toString().trim());
                for (int j = 0; j < css1.length(); j++) {
                    bgName = css1.optJSONObject(j).optString("name")+":";
                    css1.optJSONObject(j).put("mandatorychild",mandatory);
//                    parentId = oss.optJSONObject(i).optString("parentid");
                    css1.optJSONObject(j).put("parentid",parentId);
                    css1.optJSONObject(j).put("level","two");
                    tempJss.put(css1.get(j));
                    String childExistence = oss.optJSONObject(i).optString("children").toString().trim();
                    if (childExistence.equals("true")) {
                        opp1 = new JSONArray(css1.optJSONObject(i).optString("modifiers").toString().trim());
                        if (opp1.length() > 0) {
                            for (int k = 0; k < opp1.length(); k++) {
                                mandatory = opp1.getJSONObject(k).getString("mandatory");
                                opp1.optJSONObject(k).put("bgname",bgName);
                                tempJss.put(opp1.get(k));
                            }
                            for (int m = 0; m < opp1.length(); m++) {
                                if (opp1.optJSONObject(m).has("options"))
                                    try {
                                        JSONArray gg = new JSONArray(opp1.optJSONObject(m).optString("options").toString().trim());
                                        for (int p = 0; p < childJsonArray(gg,pos,parentId).length(); p++) {
                                            opp1.optJSONObject(p).put("mandatorychild",mandatory);
                                            opp1.optJSONObject(p).put("parentid",parentId);
                                            opp1.optJSONObject(p).put("level","three");
                                            tempJss.put(opp1.get(p));
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                            }
                        }
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return tempJss;
    }


    public class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        LayoutInflater inflater;

        private final int headerItem = 1;
        private final int footerItem = 2;
        private  String mandatoryValue = "true";

        public Adapter() {

        }


        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            RecyclerView.ViewHolder viewHolder;
            if (viewType == headerItem) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_child, parent, false);
                viewHolder = new MyViewHolderParent(view);
            } else {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_child1, parent, false);
                viewHolder = new MyViewHolderChild(view);
            }
            return viewHolder;
        }

        @Override
        public int getItemViewType(int position) {
            if (originalJsArray.optJSONObject(position).optString("description").equals(null) || originalJsArray.optJSONObject(position).optString("description").equals(""))
                return headerItem;
            else
                return footerItem;
        }




        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder1, final int position) {
            if (holder1 instanceof MyViewHolderParent) {
                MyViewHolderParent holder = (MyViewHolderParent) holder1;
                if (originalJsArray.optJSONObject(position).optString("id").equals(null) || originalJsArray.optJSONObject(position).optString("id").equals(""))
                    holder.selectorImgVw.setVisibility(View.GONE);
                else {
                    holder.selectorImgVw.setVisibility(View.VISIBLE);
                    holder.selectorImgVw.setTag(position + "@@" + originalJsArray.optJSONObject(position).optString("id") + "##" + "invisible");
                }

                if (originalJsArray.optJSONObject(position).optString("name").equals(null) || originalJsArray.optJSONObject(position).optString("name").equals("")) {
                    holder.labelTxtVw.setVisibility(View.GONE);
                    holder.selectorImgVw.setVisibility(View.GONE);
                } else {
                    holder.labelTxtVw.setVisibility(View.VISIBLE);
                    holder.selectorImgVw.setVisibility(View.VISIBLE);
                        if (originalJsArray.optJSONObject(position).optString("mandatorychild").equals("true"))
                            holder.selectorImgVw.setImageResource(R.drawable.radiounselect);
                        else
                            holder.selectorImgVw.setImageResource(R.drawable.checkunselect);
                    if (checkListView(originalJsArray.optJSONObject(position).optString("id"))) {

                        if (originalJsArray.optJSONObject(position).optString("mandatorychild").equals("true"))
                        {
                            holder.selectorImgVw.setImageResource(R.drawable.radioselect);
                            holder.selectorImgVw.setId(position);
                        }
                        else {
                            holder.selectorImgVw.setImageResource(R.drawable.checkselect);
                            holder.selectorImgVw.setId(position);
                        }
                        holder.selectorImgVw.setTag(position + "@@" + originalJsArray.optJSONObject(position).optString("id") + "##" + "visible");
                    } else {
                        holder.selectorImgVw.setTag(position + "@@" + originalJsArray.optJSONObject(position).optString("id") + "##" + "invisible");
                    }
                    holder.labelTxtVw.setText(originalJsArray.optJSONObject(position).optString("name"));
                    if (position == ccpos)
                        if (originalJsArray.optJSONObject(ccpos).has("children")) {
                            if (originalJsArray.optJSONObject(ccpos).optString("children").equals("true")) {
                                sName = originalJsArray.optJSONObject(position).optString("name");
                                sName = sName + ":";
                            }
                        }
                }
                // do your stuff
            } else {
                MyViewHolderChild holder = (MyViewHolderChild) holder1;
                if (originalJsArray.optJSONObject(position).optString("description").equals(null) || originalJsArray.optJSONObject(position).optString("description").equals("")) {
                    holder.optionHeaderTxt.setVisibility(View.GONE);
                    holder.labelHeaderTxt.setVisibility(View.GONE);
                } else {
                    holder.optionHeaderTxt.setVisibility(View.VISIBLE);
                    holder.labelHeaderTxt.setVisibility(View.VISIBLE);
                    holder.labelHeaderTxt.setText(originalJsArray.optJSONObject(position).optString("bgname"));
                    holder.optionHeaderTxt.setText(originalJsArray.optJSONObject(position).optString("description"));
                    holder.optionHeaderTxt.setTag(position + "@@" + originalJsArray.optJSONObject(position).optString("id"));
                    holder.optionHeaderTxt.setId(position);
                }
            }
        }

        @Override
        public long getItemId(int position) {
            int pos = 0;
            try {
                pos = Integer.parseInt(originalJsArray.optJSONObject(position).getString("id").toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return pos;
        }

        @Override
        public int getItemCount() {
            return originalJsArray.length();
        }


        class MyViewHolderChild extends RecyclerView.ViewHolder {

            TextView optionHeaderTxt, labelHeaderTxt;

            public MyViewHolderChild(View itemView1) {
                super(itemView1);
                optionHeaderTxt = (TextView) itemView1.findViewById(R.id.headertitleTxtVw);
                labelHeaderTxt = (TextView) itemView1.findViewById(R.id.labelHeaderTxt);
            }
        }


        class MyViewHolderParent extends RecyclerView.ViewHolder {

            TextView headerTxtVw, titleTxtVw, labelTxtVw;
            ImageView selectorImgVw;
            LinearLayout optionHorizontal, optionVertical, optionHeader;


            public MyViewHolderParent(View itemView) {
                super(itemView);

                titleTxtVw = (TextView) itemView.findViewById(R.id.title);//change Here
                headerTxtVw = (TextView) itemView.findViewById(R.id.headerTxtVw);
                labelTxtVw = (TextView) itemView.findViewById(R.id.lblTxtVw);
                selectorImgVw = (ImageView) itemView.findViewById(R.id.selectorImgVw);
                optionHorizontal = (LinearLayout) itemView.findViewById(R.id.option_item);
                optionVertical = (LinearLayout) itemView.findViewById(R.id.option_holderLay);
                optionHeader = (LinearLayout) itemView.findViewById(R.id.option_header);
                selectorImgVw.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int clickedIndex = Integer.parseInt(view.getTag().toString().split("@@")[0]);
                        ccpos = clickedIndex;
                        int rm = 0;
                        int prm = 0;
                        int id = Integer.parseInt(view.getTag().toString().split("@@")[1].toString().split("##")[0]);
                        String parentId = originalJsArray.optJSONObject(ccpos).optString("parentid");

                        String visibility = view.getTag().toString().split("##")[1];
                        if (visibility.equals("invisible")) {
                            executeFun = false;
                            subshow = "add";
                            if (!selectedIds.contains(String.valueOf(id)))
                                selectedIds.add(String.valueOf((id)));
                            selectorImgVw.setImageResource(R.drawable.checkselect);
                            View v = view;
                            if (clickedIndex != lastClickedIndex || atFirst == false) {
                                if(atFirst == true){

                                    if(originalJsArray.optJSONObject(clickedIndex).optString("mandatorychild").equals("true"))
                                     {
                                         if(clickedParentId(clickedIndex).equals(clickedParentId(lastClickedIndex))) {

                                             idCount.remove(String.valueOf(lastClickedIndexId));
                                             selectedIds.remove(String.valueOf(lastClickedIndexId));


                                             rm = hm.get(String.valueOf(lastClickedIndexId));

                                             if(!originalJsArray.optJSONObject(clickedIndex).optString("level").equals("one"))
                                                 clickedParentRmCount = clickedParentRmCount-rm;

                                             arrayListRemoveParent(lastClickedIndex,rm);


                                         }else if(clickedParentId(lastClickParentIndex).equals(clickedParentId(clickedIndex))) {
                                             if (originalJsArray.optJSONObject(clickedIndex).optString("level").equals("one")) {
                                                 idCount.remove(String.valueOf(lastClickParentIndexId));
                                                 selectedIds.remove(String.valueOf(lastClickParentIndexId));
                                                 idCount.remove(String.valueOf(lastClickedIndexId));
                                                 selectedIds.remove(String.valueOf(lastClickedIndexId));

                                                 prm = hmParent.get(String.valueOf(lastClickParentid));
                                                 isHeaderClick = "yes";
                                                 arrayListRemoveParent(lastClickParentIndex, prm);
                                             }
                                         }
                                    }
                                }
                                atFirst = true;
                                if (clickCount.contains(clickedIndex)) {
                                    if (idCount.contains(String.valueOf(id))) {

                                    } else {
                                        try {
                                            if(isHeaderClick.equals("yes")) {
                                                if(originalJsArray.optJSONObject(clickedIndex).optString("level").equals("one"))
                                                 clickedParentRmCount = 0;
                                            }
                                            arrayListJsonConv(arraylistAdd(clickedIndex,id,parentId));
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        lastClickedIndex = clickedIndex;
                                        if(originalJsArray.optJSONObject(clickedIndex).optString("level").equals("one")) {
                                            lastClickParentIndex = clickedIndex;
                                            lastClickParentIndexId = id;
                                            lastClickParentid = parentId;
                                        }
                                        lastClickedIndexId = id;
                                        clickCount.add(clickedIndex);

                                        GridLayoutManager manager =
                                                new GridLayoutManager(getApplicationContext(), 3);
                                        GridLayoutManager.SpanSizeLookup onSpanSizeLookup = new GridLayoutManager.SpanSizeLookup() {
                                            @Override
                                            public int getSpanSize(int position) {
                                                Adapter aa = new Adapter();
                                                return aa.getItemViewType(position) == headerItem ? 1 : 3;
                                            }
                                        };
                                        manager.setSpanSizeLookup(onSpanSizeLookup);
                                        recyclerView.setLayoutManager(manager);
                                        recyclerViewState = recyclerView.getLayoutManager().onSaveInstanceState();
                                        mAdapter.notifyDataSetChanged();
                                        recyclerView.scrollBy(0, mScrollY);
                                    }
                                } else {
                                    try {
                                        if(isHeaderClick.equals("yes")) {
                                            if(originalJsArray.optJSONObject(clickedIndex).optString("level").equals("one"))
                                             clickedParentRmCount = 0;
                                        }
                                        arrayListJsonConv(arraylistAdd(clickedIndex,id,parentId));
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    lastClickedIndex = clickedIndex;
                                    if(originalJsArray.optJSONObject(clickedIndex).optString("level").equals("one")) {
                                        lastClickParentIndex = clickedIndex;
                                        lastClickParentIndexId = id;
                                        lastClickParentid = parentId;
                                    }

                                    lastClickedIndexId = id;
                                    clickCount.add(clickedIndex);
                                    idCount.add(String.valueOf(id));
                                    GridLayoutManager manager =
                                            new GridLayoutManager(getApplicationContext(), 3);
                                    GridLayoutManager.SpanSizeLookup onSpanSizeLookup = new GridLayoutManager.SpanSizeLookup() {
                                        @Override
                                        public int getSpanSize(int position) {
                                            Adapter aa = new Adapter();
                                            return aa.getItemViewType(position) == headerItem ? 1 : 3;
                                        }
                                    };
                                    manager.setSpanSizeLookup(onSpanSizeLookup);
                                    recyclerView.setLayoutManager(manager);
                                    recyclerViewState = recyclerView.getLayoutManager().onSaveInstanceState();
                                    mAdapter.notifyDataSetChanged();
                                    recyclerView.scrollBy(0, mScrollY);

                                }
                            } else {
                                try {
                                    arrayListJsonConv(arraylistAdd(clickedIndex,id,parentId));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                lastClickedIndex = clickedIndex;
                                if(originalJsArray.optJSONObject(clickedIndex).optString("level").equals("one")) {
                                    lastClickParentIndex = clickedIndex;
                                    lastClickParentIndexId = id;
                                    lastClickParentid = parentId;
                                }
                                lastClickedIndexId = id;
                                clickCount.add(clickedIndex);
                                idCount.add(String.valueOf(id));
                                GridLayoutManager manager =
                                        new GridLayoutManager(getApplicationContext(), 3);
                                GridLayoutManager.SpanSizeLookup onSpanSizeLookup = new GridLayoutManager.SpanSizeLookup() {
                                    @Override
                                    public int getSpanSize(int position) {
                                        Adapter aa = new Adapter();
                                        return aa.getItemViewType(position) == headerItem ? 1 : 3;
                                    }
                                };
                                manager.setSpanSizeLookup(onSpanSizeLookup);
                                recyclerView.setLayoutManager(manager);
                                recyclerViewState = recyclerView.getLayoutManager().onSaveInstanceState();
                                mAdapter.notifyDataSetChanged();
                                recyclerView.scrollBy(0, mScrollY);
                            }
                        } else {
                            subshow = "remove";
                            executeFun = true;
                            if (originalJsArray.optJSONObject(ccpos).has("children")) {
                                if (originalJsArray.optJSONObject(ccpos).optString("children").equals("true")) {
                                    selectedIds.remove(String.valueOf((id)));
                                    idCount.remove(String.valueOf(id));
                                    selectorImgVw.setImageResource(R.drawable.checkunselect);
                                    if(originalJsArray.optJSONObject(ccpos).optString("level").equals("one"))
                                    arrayListJsonConv(arrayListRemoveParent(ccpos,rm));
                                    else
                                        arrayListJsonConv(arraylistRemove(ccpos,id));

                                    view.setTag(clickedIndex + "@@" + String.valueOf(id) + "##" + "invisible");
                                    mAdapter.notifyDataSetChanged();
                                    recyclerView.scrollBy(0, mScrollY);
                                } else {
                                    selectorImgVw.setImageResource(R.drawable.checkunselect);
                                    selectedIds.remove(String.valueOf((id)));
                                    idCount.remove(String.valueOf((id)));
                                    view.setTag(clickedIndex + "@@" + String.valueOf(id) + "##" + "invisible");
                                }

                            }
                        }
                    }
                });
            }
        }

        Boolean checkListView(String id) {
            if (selectedIds.contains(id) || idCount.contains(id))
                return true;
            else
                return false;
        }

        Boolean selectionOption(String selOpt) {
            if (selOpt.trim().equals("true"))
                return true;
            else
                return false;
        }

        Boolean headerVisibility(String headerIdStr) {
            if (selectedHeaderIds.contains(headerIdStr))
                return true;
            else
                return false;
        }

    }
}
