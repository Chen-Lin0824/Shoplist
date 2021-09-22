package tw.tcnr02.shoplist;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main extends AppCompatActivity implements View.OnClickListener {

    String TAG = "tcnr02=>";
    private TextView count_t;
    private EditText b_id;

    private TextView tvTitle;
    private Button btNext, btPrev, btTop, btEnd;
    private ArrayList<String> recSet;
    private ArrayList<String> recSet_item;
    private int index = 0;
    String msg = null;
    //--------------------------

    private Button btEdit, btDel;
    String ttitle, trecipe_text,tadditem_edit;

    private Spinner mSpnName;
    int up_item = 0;
    //------------------------------
    protected static final int BUTTON_POSITIVE = -1;
    protected static final int BUTTON_NEGATIVE = -2;
    private Button btAdd, btAbandon, btquery, btcancel, btreport;

    ArrayList<String> cookbook_ArrayList;
    ArrayList<ArrayList<String>> SHOWRECIPE = new ArrayList<>();
    ArrayList<String> Edit_itemlist;//存放透過Edit輸入的品項資料
    ArrayAdapter<String> arrayAdapter_item;
    String [] Temp_Array;
    int tcount;

    // ------------------
    private RelativeLayout b_Relbutton;
    private RelativeLayout brelative01;
    private RelativeLayout blinear02;
    private ListView listView,addItemList,additem_List;
    private TextView bsubTitle;
    private LinearLayout itemlinearlayout,readitemlayout;
    //===============
    private DbHelper dbHper;
    private static final String DB_FILE = "Fridge.db";
    private static final String DB_TABLE_Shoplist01 = "shoplist01";
    private static final int DBversion = 1;
    //-----------------
    private String sqlctl;
    private String tid;
    private String s_id;
    private String taddress;
    private int old_index = 0;

    private MenuItem b_m_add, b_m_query, b_m_batch, b_m_list, b_m_mysql, b_m_edit_start, b_m_edit_stop, b_m_return;
    private Menu menu;
    private boolean touch_flag;

    private TextView b_servermsg;
    private String ser_msg;
    private int servermsgcolor;


    // ----------定時更新--------------------------------
    private Long startTime;
    private Handler handler = new Handler();

    int autotime = 3;// 要幾秒的時間 更新匯入MySQL資料
    //------------------------------
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    private TextView nowtime;  //顯示更新時間及次數
    int update_time = 0;
    private String str;
    // --------------------------------------------------------
    private boolean runAuto_flag = false;  //Runable updateTime 狀態
    private TextView b_editon;
    private String stHead;
    private boolean edittype_flag = false;  //編輯狀態
    private String showip; //顯示手機ip
    private EditText b_title,b_recipe_text;
    private ImageButton addrecipe_btn,delrecipe_btn;
    private Button newadd_button,newupdate_button,newdel_button,newadditem_btn,additeminfo_btn;

    private Intent intent = new Intent();
    private EditText additem_edit;
    private LinearLayout checkboxlinear;
    private CheckBox checkbox;
    private ListView checkbox_list;



    // --------------------------------------------------------

    // --------------------------------------------------------
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        enableStrictMode(this);//使用暫存堆疊，必須加入此方法
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shoplist_hp_main);
        //--------取得目前時間
        startTime = System.currentTimeMillis();
        // -----------------
        setupViewComponent();

        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void enableStrictMode(Context context) {
        //-------------抓取遠端資料庫設定執行續-------------------
        //----怕連上000時卡住，先把資料暫存，等主機OK再上傳
        StrictMode.setThreadPolicy(new
                StrictMode.
                        ThreadPolicy.Builder().
                detectDiskReads().
                detectDiskWrites().
                detectNetwork().
                penaltyLog().
                build());
        StrictMode.setVmPolicy(
                new
                        StrictMode.
                                VmPolicy.
                                Builder().
                        detectLeakedSqlLiteObjects().
                        penaltyLog().
                        penaltyDeath().
                        build());
    }

    private void setupViewComponent() {

//        // 設定class標題
//        Intent intent = this.getIntent();
//        String mode_title = intent.getStringExtra("class_title");
//        this.setTitle(mode_title);

        tvTitle = (TextView) findViewById(R.id.tvIdTitle);
        b_id = (EditText) findViewById(R.id.edid);
        b_title = (EditText) findViewById(R.id.et_title);
        b_recipe_text = (EditText) findViewById(R.id.et_content);

        count_t = (TextView) findViewById(R.id.count_t);


        addrecipe_btn=(ImageButton)findViewById(R.id.addButton);
        newadd_button=(Button)findViewById(R.id.newadd_button);//新增頁的新增按鈕
        newupdate_button=(Button)findViewById(R.id.newupdate_button);//新增頁的更新按鈕
        newdel_button=(Button)findViewById(R.id.newdel_button);//新增頁的刪除按鈕
        newadditem_btn=(Button)findViewById(R.id.newadditem_btn);//新增頁的新增購物項目按鈕
        additeminfo_btn=(Button)findViewById(R.id.button_additem);//新增頁的新增購物項目按鈕NEW
        additem_edit=(EditText)findViewById(R.id.additem_edit);//輸入購物品項的地方
        additem_List=(ListView)findViewById(R.id.item_list_show);
        checkbox_list=(ListView)findViewById(R.id.checkbox_list);

        checkboxlinear=(LinearLayout)findViewById(R.id.checkboxlinear);
        checkbox=(CheckBox)findViewById(R.id.checkbox);
        //addItemList=(ListView) findViewById(R.id.item_list_show);

        //----------------Edit_itemlist--------------
        Edit_itemlist = new ArrayList<String>();
        arrayAdapter_item = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1,Edit_itemlist);

        //addItemList.setAdapter(arrayAdapter_item);

        //----------------Edit_itemlist--------------

        brelative01 = (RelativeLayout) findViewById(R.id.chp_relative01);
        blinear02 = (RelativeLayout) findViewById(R.id.chp_rel_ceditlinear01);
        itemlinearlayout=(LinearLayout)findViewById(R.id.itemlinearlayout01);
        readitemlayout=(LinearLayout)findViewById(R.id.readitemlayout01);
        listView = (ListView) findViewById(R.id.listView);


        b_Relbutton = (RelativeLayout) findViewById(R.id.chp_Relbutton);
        bsubTitle = (TextView) findViewById(R.id.subTitle);
        b_editon = (TextView) findViewById(R.id.edit_on);

        b_servermsg = (TextView) findViewById(R.id.servermsg);
        tvTitle.setBackgroundColor(ContextCompat.getColor(this, R.color.Teal));
        tvTitle.setTextColor(ContextCompat.getColor(this, R.color.Yellow));
        //-----------------
        mSpnName = (Spinner) this.findViewById(R.id.spnName);
        nowtime = (TextView) findViewById(R.id.now_time);
        //---------設定layout 顯示---------------
        //u_layout_def();
        //-----------------------

        addrecipe_btn.setOnClickListener(this);
        newadd_button.setOnClickListener(this);
        newupdate_button.setOnClickListener(this);
        newdel_button.setOnClickListener(this);
        additeminfo_btn.setOnClickListener(this);
        newadditem_btn.setOnClickListener(this);
        //btreport.setOnClickListener(this);

        tvTitle.setTextColor(ContextCompat.getColor(this, R.color.Navy));
        //-----------------
        mSpnName = (Spinner) this.findViewById(R.id.spnName);
        //===================================
        nowtime = (TextView) findViewById(R.id.now_time);
        startTime = System.currentTimeMillis();        // 取得目前時間
//        ************************************
        if (runAuto_flag == false) {
            handler.postDelayed(updateTimer, 500);  // 設定Delay的時間
            runAuto_flag = true;
        }
        // ************************************
        //===================================
        java.sql.Date curDate = new java.sql.Date(System.currentTimeMillis()); //  獲取當前時間
        str = formatter.format(curDate);
        nowtime.setText(getString(R.string.now_time) + str);
        // ----------------------------------------
        initDB();

        if(recSet==null || recSet.size()<1){
            //無資料的時候
            Toast.makeText(this, "無資料顯示", Toast.LENGTH_SHORT).show();

        }else {
            //有資料的時候
            int a=0;
            showRec(index);
            u_setspinner();
            stHead = "顯示資料：第" + (index + 1) + " / " + tcount + " 筆";
            tvTitle.setText(stHead);
            b_id.setTextColor(ContextCompat.getColor(this, R.color.Red));
            // -------------------------
            mSpnName.setOnItemSelectedListener(mSpnNameOnItemSelLis);
        }


        //測試跳轉列表方法
        startlayout();
    }

    private Runnable updateTimer=new Runnable() {
        @Override
        public void run() {
            old_index = mSpnName.getSelectedItemPosition();
            Long spentTime = System.currentTimeMillis() - startTime;
            String hours = String.format("%02d", (spentTime / 1000) / 60 / 60);  // 計算目前已過分鐘數
            String minius = String.format("%02d", ((spentTime / 1000) / 60) % 60);  // 計算目前已過分鐘數
            String seconds = String.format("%02d", (spentTime / 1000) % 60);          // 計算目前已過秒數
            handler.postDelayed(this, autotime * 1000); // 真正延遲的時間
            // -------執行匯入MySQL
            dbmysql();

            recSet = dbHper.getRecSet_Shoplist01();  //重新載入SQLite
//            int a=0;
            u_setspinner();  //重新設定spinner內容
            index = old_index;
            showRec(index); //重設spainner 小窗顯示及細目內容
            //-------------------------------------------------------------------------------
            ++update_time;
            nowtime.setText(getString(R.string.now_time) + "(每" + autotime + "秒)" + str + "->"
                    + hours + ":" + minius + ":" + seconds
                    + " (" + (update_time) + "次)");

        }
    };


    private void initDB() {
        if (dbHper == null)
            dbHper = new DbHelper(this, DB_FILE, null, DBversion);
        //-----****此處要加 才不會造成 SQLite 無資料 閃退****------
        dbmysql();
        //-----------
        recSet = dbHper.getRecSet_Shoplist01();
    }

    private Spinner.OnItemSelectedListener mSpnNameOnItemSelLis = new Spinner.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView parent, View view, int position,
                                   long id) {
            int iSelect = mSpnName.getSelectedItemPosition(); //找到按何項
            String[] fld = recSet.get(iSelect).split("#");
            String s = "資料：共" + recSet.size() + " 筆," + "你按下  " + String.valueOf(iSelect + 1) + "項"; //起始為0
            tvTitle.setText(s);
            b_id.setTextColor(ContextCompat.getColor(parent.getContext(), R.color.Red));
            b_id.setText(fld[0]);
            b_title.setText(fld[1]);
            //b_recipe_text.setText(fld[2]);
            //-------目前所選的item---
            index = iSelect;

        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
            // TODO Auto-generated method stub
            b_id.setText("");
            b_title.setText("");
            b_recipe_text.setText("");

        }
    };


    private void  startlayout(){
        ttitle = b_title.getText().toString().trim();
        trecipe_text = b_recipe_text.getText().toString().trim();

        msg = null;
        recSet = dbHper.getRecSet_query_Shoplist01(ttitle, trecipe_text);//ttitle, trecipe_text



//===========取SQLite 資料=============
        List<Map<String, Object>> mList;
        mList = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < recSet.size(); i++) {
            Map<String, Object> item = new HashMap<String, Object>();
            String[] fld = recSet.get(i).split("#");
            //item.put("imgView", R.drawable.userconfig);
            item.put("txtView", "購物清單名稱:\n" + fld[1] );
            mList.add(item);

            cookbook_ArrayList = new ArrayList<String>();
            cookbook_ArrayList.add(fld[0]);
            cookbook_ArrayList.add(fld[1]);
            //cookbook_ArrayList.add(fld[2]);
            SHOWRECIPE.add(cookbook_ArrayList);
        }
//=========設定listview========
        brelative01.setVisibility(View.INVISIBLE);
        blinear02.setVisibility(View.VISIBLE);
        SimpleAdapter adapter = new SimpleAdapter(
                this,
                mList,
                R.layout.shoplist_item,
                new String[]{"txtView"},
                new int[]{R.id.txtView}
        );
        listView.setAdapter(adapter);
        listView.setTextFilterEnabled(true);
        listView.setOnItemClickListener(listviewOnItemClkLis);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.button_additem://----按下即新增EditText的內容

                String iteminfo = additem_edit.getText().toString();
                Edit_itemlist.add(iteminfo);
                additem_List.setAdapter(arrayAdapter_item);
                arrayAdapter_item.notifyDataSetChanged();//告知Adapter 刷新ListView
                additem_edit.setText("");
                break;

            case R.id.addButton: //----現有食譜頁的新增按鈕

                new_insert();
                break;

            case R.id.newadd_button: //----新增食譜頁確定新增按鈕


                // 查詢name是否有有此筆資料
                ttitle = b_title.getText().toString().trim();
                trecipe_text = "";

                if (ttitle.equals("")) {
                    Toast.makeText(getApplicationContext(), "標題空白是無法新增的 !", Toast.LENGTH_SHORT).show();
                    return;
                }
                //------直接增加到MySQL------------
                mysql_insert();
                dbmysql();
                //----------------------------------------
                msg = null;
                // -------------------------
                ContentValues newRow = new ContentValues();
                newRow.put("title", ttitle);
                newRow.put("recipe_text", trecipe_text);

                //------------------------------

                long rowID = dbHper.insertRec_Shoplist01(newRow);
                //int idnum=recSet.size() - 1;

                dbmysql(); //重新匯入

                //-----------新增item---------------
                Temp_Array = recSet.get(recSet.size() - 1).split("#");//----取得清單titleID
                //Toast.makeText(getApplicationContext(), Temp_Array[0], Toast.LENGTH_SHORT).show();
                int Edit_itemlist_Size=Edit_itemlist.size();
                int i;
                for(i = 0; i < Edit_itemlist_Size; i++){//----存入item

                    tadditem_edit = Edit_itemlist.get(i);
                    mysql_insert_shoplist02();//增加到MySQL

                }
                dbmysql();


                if (Edit_itemlist.equals("")) {
                    Toast.makeText(getApplicationContext(), "品項空白是無法新增的 !", Toast.LENGTH_SHORT).show();
                    return;
                }

                //------直接增加到MySQL------------
                mysql_insert_shoplist02();
                dbmysql_shoplist02();
                //----------------------------------------
                msg = null;
                // -------------------------
                ContentValues newRow_s02 = new ContentValues();
                newRow_s02.put("shoplist_item", tadditem_edit);
//                newRow_s02.put("recipe_text", trecipe_text);

                //------------------------------

                long rowID_s02 = dbHper.insertRec_Shoplist02(newRow_s02);

                end_insert();
                break;

            case R.id.newupdate_button:
                // 資料更新
                tid = b_id.getText().toString().trim();
                ttitle = b_title.getText().toString().trim();
                trecipe_text = b_recipe_text.getText().toString().trim();


                //-------------------------------------
                old_index=index;
                mysql_update(); // 執行MySQL更新
                dbmysql();
                //-------------------------------------
                recSet = dbHper.getRecSet_Shoplist01();
                u_setspinner();
                index=old_index;
                showRec(index);
                msg = "食譜  已修改完成 ! " ;
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                break;

            case R.id.newdel_button:// 資料刪除
                //使用對話盒
                MyAlertDialog myAltDlg = new MyAlertDialog(this);
                myAltDlg.getWindow().setBackgroundDrawableResource(R.color.Yellow);
                myAltDlg.setTitle("刪除食譜");
                myAltDlg.setMessage("食譜刪除後將無法復原\n確定將這筆食譜刪除嗎?");
                myAltDlg.setCancelable(false);
                myAltDlg.setIcon(android.R.drawable.ic_delete);
                myAltDlg.setButton(DialogInterface.BUTTON_POSITIVE, "確定刪除", aldBtListener);
                myAltDlg.setButton(DialogInterface.BUTTON_NEGATIVE, "取消刪除", aldBtListener);
                myAltDlg.show();
                break;

        }

    }//------onClick END

    // ---------------------------------------------
    private DialogInterface.OnClickListener aldBtListener = new DialogInterface.OnClickListener() {

        private int old_index;

        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case BUTTON_POSITIVE:
//                    int rowsAffected = dbHper.clearRec();  //刪除全部資料
//                tid = b_id.getText().toString().trim();
                    old_index=index;
                    // ---------------------------
                    mysql_del();// 執行MySQL刪除
                    dbmysql();
                    // ---------------------------
                    index=old_index;
                    u_setspinner();
                    if (index == dbHper.RecCount_Shoplist01()) {
                        index--;
                    }
                    showRec(index);
//                    mSpnName.setSelection(index, true); //spinner 小窗跳到第幾筆
//                }
                    msg = "食譜已刪除" ;
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();

                    startlayout();//回到列表
                    break;

                case BUTTON_NEGATIVE:
                    msg = "放棄刪除 !";
                    Toast.makeText(Main.this, msg, Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };



    private void mysql_update() {
        s_id = b_id.getText().toString().trim();
        ttitle = b_title.getText().toString().trim();
        trecipe_text = b_recipe_text.getText().toString().trim();

        ArrayList<String> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(s_id);
        nameValuePairs.add(ttitle);
        nameValuePairs.add(trecipe_text);

        try {
            Thread.sleep(100); //  延遲Thread 睡眠0.5秒
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//-----------------------------------------------
        String result = DBConnector.executeUpdate_Shoplist01( nameValuePairs);
//-----------------------------------------------
    }

    private void mysql_insert() {
//        sqlctl = "SELECT * FROM member ORDER BY id ASC";
        ArrayList<String> nameValuePairs = new ArrayList<>();
//        nameValuePairs.add(sqlctl);
        nameValuePairs.add(ttitle);
        nameValuePairs.add(trecipe_text);

        //--------寫死的參數
//        nameValuePairs.add("456456456");
//        nameValuePairs.add("454566");
//        nameValuePairs.add("777777");
        try {
            Thread.sleep(500); //  延遲Thread 睡眠0.5秒
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//-----------------------------------------------
        String result = DBConnector.executeInsert_Shoplist01(nameValuePairs);  //真正執行新增
//-----------------------------------------------

    }

    private void mysql_insert_shoplist02() {
//        sqlctl = "SELECT * FROM member ORDER BY id ASC";
        ArrayList<String> nameValuePairs = new ArrayList<>();
//        nameValuePairs.add(sqlctl);
        nameValuePairs.add(Temp_Array[0]);
        nameValuePairs.add(tadditem_edit);
        //nameValuePairs.add(trecipe_text);

        //--------寫死的參數
//        nameValuePairs.add("456456456");
//        nameValuePairs.add("454566");
//        nameValuePairs.add("777777");
        try {
            Thread.sleep(500); //  延遲Thread 睡眠0.5秒
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//-----------------------------------------------
        String result = DBConnector.executeInsert_Shoplist02(nameValuePairs);  //真正執行新增
//-----------------------------------------------

    }

    private ListView.OnItemClickListener listviewOnItemClkLis = new ListView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            //*************************************
            if (runAuto_flag == true) {
                handler.removeCallbacks(updateTimer); //關閉執行續
                runAuto_flag = false;
            }
            //***************************************

            brelative01.setVisibility(View.VISIBLE);
            blinear02.setVisibility(View.INVISIBLE);
            newadd_button.setVisibility(View.INVISIBLE);
            newupdate_button.setVisibility(View.INVISIBLE);
            newdel_button.setVisibility(View.INVISIBLE);
            readitemlayout.setVisibility(View.VISIBLE);
            itemlinearlayout.setVisibility(View.INVISIBLE);

            b_id.setText(SHOWRECIPE.get(position).get(0));
            b_title.setText(SHOWRECIPE.get(position).get(1));
            //b_recipe_text.setText(SHOWRECIPE.get(position).get(2));
            index = position;


            String get_titleID=SHOWRECIPE.get(position).get(0);

            get_shoplistitem(get_titleID);


        }
    };

    private void get_shoplistitem(String get_titleID){

        //--------------抓取shoplist02---------

        recSet_item= dbHper.getRecSet_query_Shoplist02(get_titleID);//查詢item

//===========取SQLite 資料=============
        List<Map<String, Object>> mList01;
        mList01 = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < recSet_item.size(); i++) {
            Map<String, Object> item = new HashMap<String, Object>();
            String[] fld = recSet_item.get(i).split("#");
            //item.put("imgView", R.drawable.userconfig);
            item.put("checkbox", fld[2] );
            mList01.add(item);


        }

        //=========設定checkbox listview========
        SimpleAdapter adapter01 = new SimpleAdapter(
                this,
                mList01,
                R.layout.shoplist_checkbox_item,
                new String[]{"checkbox"},
                new int[]{R.id.checkbox}
        );
        checkbox_list.setAdapter(adapter01);
        checkbox_list.setTextFilterEnabled(true);

    }


    private void mysql_del() {
        //---------
        s_id = b_id.getText().toString().trim();
        ArrayList<String> nameValuePairs = new ArrayList<>();
//        nameValuePairs.add(sqlctl);
        nameValuePairs.add(s_id);
        try {
            Thread.sleep(100); //  延遲Thread 睡眠0.5秒
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//-----------------------------------------------
        String result = DBConnector.executeDelet_Shoplist01(nameValuePairs);   //執行刪除
//-----------------------------------------------
    }

    private void u_setspinner() {//重構

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item);
        for (int i = 0; i < recSet.size(); i++) {
            String[] fld = recSet.get(i).split("#");
            adapter.add(fld[0] + " " + fld[1]);//fld[2]
        }
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpnName.setAdapter(adapter);

        mSpnName.setOnItemSelectedListener(mSpnNameOnItemSelLis);
        //        mSpnName.setSelection(index, true); //spinner 小窗跳到第幾筆

    }

    private void showRec(int index) {

        msg = "";
        if (recSet.size() != 0) {
            String stHead = "顯示資料：第 " + (index + 1) + " 筆 / 共 " + recSet.size() + " 筆";
            msg = getString(R.string.count_t) + recSet.size() + "筆";
            tvTitle.setBackgroundColor(ContextCompat.getColor(this, R.color.Teal));
            tvTitle.setTextColor(ContextCompat.getColor(this, R.color.Yellow));
            tvTitle.setText(stHead);


            String[] fld = recSet.get(index).split("#");

            b_id.setTextColor(ContextCompat.getColor(this, R.color.Red));
            b_id.setBackgroundColor(ContextCompat.getColor(this, R.color.Yellow));
            b_id.setText(fld[0]);
            b_title.setText(fld[1]);
            //b_recipe_text.setText(fld[2]);

            mSpnName.setSelection(index, true); //spinner 小窗跳到第幾筆

        } else {
            String stHead = "顯示資料：0 筆";
            msg = getString(R.string.count_t) + "0筆";
            tvTitle.setTextColor(ContextCompat.getColor(this, R.color.Blue));
            tvTitle.setText(stHead);
            b_id.setText("");
            b_title.setText("");
            b_recipe_text.setText("");

        }

        count_t.setText(msg);


    }


    // ---------------------------------------------
    public boolean onKeyDown(int keyCode, KeyEvent event) { //禁用返回鍵
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return false;
    }

    //---------------------------------------------------
    @Override
    protected void onPause() {
        super.onPause();
        if (runAuto_flag == true) {
            handler.removeCallbacks(updateTimer); //關閉執行續
            runAuto_flag = false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //********************************************
        if (runAuto_flag == false && edittype_flag == false) {
            handler.post(updateTimer);  //開啟執行續
            runAuto_flag = true;
        }
        //********************************************

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (dbHper != null) {
            dbHper.close();
            dbHper = null;
        }
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
        handler.removeCallbacks(updateTimer);
    }



    private void new_insert() {
        //*************************************
        if (runAuto_flag == true) {
            handler.removeCallbacks(updateTimer); //關閉執行續
            runAuto_flag = false;
        }
//***************************************

        brelative01.setVisibility(View.VISIBLE);
        blinear02.setVisibility(View.INVISIBLE);
        itemlinearlayout.setVisibility(View.VISIBLE);
        menu.setGroupVisible(R.id.m_group3, false);
//    btAdd.setVisibility(View.INVISIBLE);
//    btAbandon.setVisibility(View.INVISIBLE);
//    btEdit.setVisibility(View.INVISIBLE);
//    btDel.setVisibility(View.INVISIBLE);
        b_id.setEnabled(false);

        mSpnName.setEnabled(false);
        b_id.setHint("請繼續輸入");
        b_title.setHint("請為你的購物清單想一個名字吧！");
        b_recipe_text.setHint("XXXXXXXXXXX");
        b_id.setText("");
        b_title.setText("");
        b_recipe_text.setText("");

//    String iteminfo ="";
//    Edit_itemlist.add(iteminfo);
        additem_List.setAdapter(arrayAdapter_item);
        arrayAdapter_item.notifyDataSetChanged();//告知Adapter 刷新ListView
        arrayAdapter_item.clear();//清空

        //-------使用者IP取得
        //showip=NetworkDetect();
        //b_address.setText(showip);

    }

    private void end_insert() {
        //********************************************
        if (runAuto_flag == false && edittype_flag == false) {
            handler.post(updateTimer);  //開啟執行續
            runAuto_flag = true;
        }
        //********************************************
        brelative01.setVisibility(View.INVISIBLE);
        blinear02.setVisibility(View.VISIBLE);

        dbmysql(); //重新匯入
        startlayout();
    }
    //
//    private String NetworkDetect() {//取得手機IP
//
//        ConnectivityManager CM = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//        WifiManager wm = (WifiManager) getApplicationContext().getApplicationContext().getSystemService(WIFI_SERVICE);
//        String IPaddress = Finduserip.NetwordDetect(CM, wm);
//        return IPaddress;
//    }
//
    // 讀取MySQL 資料
    private void dbmysql() {
        sqlctl = "SELECT * FROM shoplist01 ORDER BY id ASC";
        //sqlctl = "SELECT * FROM recipe";
        ArrayList<String> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(sqlctl);
        try {
            String result = DBConnector.executeQuery_Shoplist01(nameValuePairs);//匯入mysql的資料
            //==========================================
            //chk_httpstate();  //檢查 連結狀態
            //==========================================
            /**************************************************************************
             * SQL 結果有多筆資料時使用JSONArray
             * 只有一筆資料時直接建立JSONObject物件 JSONObject
             * jsonData = new JSONObject(result);
             **************************************************************************/
            JSONArray jsonArray = new JSONArray(result);
            // -------------------------------------------------------
            if (jsonArray.length() > 0) { // MySQL 連結成功有資料
                int rowsAffected = dbHper.clearRec_Shoplist01(); // 匯入前,刪除所有SQLite資料(因為SQLite是暫存的資料，所以在匯入真正資料庫時要清空暫存資料，不然ID等資料會重複
                // 處理JASON 傳回來的每筆資料
                //Toast.makeText(getApplicationContext(), "匯入資料庫成功", Toast.LENGTH_LONG).show();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonData = jsonArray.getJSONObject(i);
                    ContentValues newRow = new ContentValues();
                    // --(1) 自動取的欄位 --取出 jsonObject 每個欄位("key","value")-----------------------
//                    Iterator itt = jsonData.keys();
//                    while (itt.hasNext()) {
//                        String key = itt.next().toString();
//                        String value = jsonData.getString(key); // 取出欄位的值
//                        if (value == null) {
//                            continue;
//                        } else if ("".equals(value.trim())) {
//                            continue;
//                        } else {
//                            jsonData.put(key, value.trim());
//                        }
//                        // ------------------------------------------------------------------
//                        newRow.put(key, value.toString()); // 動態找出有幾個欄位
//                        // -------------------------------------------------------------------
//                    }
                    // ---(2) 使用固定已知欄位---------------------------
                    newRow.put("id", jsonData.getString("id").toString());
                    newRow.put("shop_title",
                            jsonData.getString("shop_title").toString());
                    newRow.put("shoplist_contain", jsonData.getString("shoplist_contain").toString());

                    // -------------------加入SQLite---------------------------------------
                    long rowID = dbHper.insertRec_Shoplist01(newRow);
                    //Toast.makeText(getApplicationContext(), "共匯入 " + Integer.toString(jsonArray.length()) + " 筆資料", Toast.LENGTH_SHORT).show();
                }
                // ---------------------------
            } else {
                Toast.makeText(getApplicationContext(), "主機資料庫無資料", Toast.LENGTH_LONG).show();
            }
            recSet = dbHper.getRecSet_Shoplist01();  //重新載入SQLite
            u_setspinner();
            // --------------------------------------------------------
        } catch (Exception e) {
            Log.d(TAG, e.toString());
        }
    }

    // 讀取shoplist02 MySQL 資料
    private void dbmysql_shoplist02() {
        sqlctl = "SELECT * FROM shoplist02 ORDER BY id ASC";

        ArrayList<String> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(sqlctl);
        try {
            String result = DBConnector.executeQuery_Shoplist02(nameValuePairs);//匯入mysql的資料
            //==========================================
            //chk_httpstate();  //檢查 連結狀態
            //==========================================
            /**************************************************************************
             * SQL 結果有多筆資料時使用JSONArray
             * 只有一筆資料時直接建立JSONObject物件 JSONObject
             * jsonData = new JSONObject(result);
             **************************************************************************/
            JSONArray jsonArray = new JSONArray(result);
            // -------------------------------------------------------
            if (jsonArray.length() > 0) { // MySQL 連結成功有資料
                int rowsAffected = dbHper.clearRec_Shoplist02(); // 匯入前,刪除所有SQLite資料(因為SQLite是暫存的資料，所以在匯入真正資料庫時要清空暫存資料，不然ID等資料會重複
                // 處理JASON 傳回來的每筆資料
                //Toast.makeText(getApplicationContext(), "匯入資料庫成功", Toast.LENGTH_LONG).show();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonData = jsonArray.getJSONObject(i);
                    ContentValues newRow = new ContentValues();
                    // --(1) 自動取的欄位 --取出 jsonObject 每個欄位("key","value")-----------------------
//                    Iterator itt = jsonData.keys();
//                    while (itt.hasNext()) {
//                        String key = itt.next().toString();
//                        String value = jsonData.getString(key); // 取出欄位的值
//                        if (value == null) {
//                            continue;
//                        } else if ("".equals(value.trim())) {
//                            continue;
//                        } else {
//                            jsonData.put(key, value.trim());
//                        }
//                        // ------------------------------------------------------------------
//                        newRow.put(key, value.toString()); // 動態找出有幾個欄位
//                        // -------------------------------------------------------------------
//                    }
                    // ---(2) 使用固定已知欄位---------------------------
                    newRow.put("id", jsonData.getString("id").toString());
                    newRow.put("titleID",
                            jsonData.getString("titleID").toString());
                    newRow.put("shoplist_item",
                            jsonData.getString("shoplist_item").toString());


                    // -------------------加入SQLite---------------------------------------
                    long rowID_s02 = dbHper.insertRec_Shoplist02(newRow);
                    //Toast.makeText(getApplicationContext(), "共匯入 " + Integer.toString(jsonArray.length()) + " 筆資料", Toast.LENGTH_SHORT).show();
                }
                // ---------------------------
            } else {
                Toast.makeText(getApplicationContext(), "主機資料庫無資料", Toast.LENGTH_LONG).show();
            }
            recSet = dbHper.getRecSet_Shoplist02();  //重新載入SQLite
            u_setspinner();
            // --------------------------------------------------------
        } catch (Exception e) {
            Log.d(TAG, e.toString());
        }
    }
//
//    private void chk_httpstate() {
////**************************************************
////*       檢查連線狀況
////**************************************************
//        //存取類別成員 DBConnector01.httpstate 判定是否回應 200(連線要求成功)
//        if (Cook_DBConnector.httpstate == 200) {
//            ser_msg = "伺服器匯入資料(code:" + Cook_DBConnector.httpstate + ") ";
//            servermsgcolor = ContextCompat.getColor(this, R.color.Navy);
////                Toast.makeText(getBaseContext(), "由伺服器匯入資料 ",
////                        Toast.LENGTH_SHORT).show();
//        } else {
//            int checkcode = Cook_DBConnector.httpstate / 100;
//            switch (checkcode) {
//                case 1:
//                    ser_msg = "資訊回應(code:" + Cook_DBConnector.httpstate + ") ";
//                    break;
//                case 2:
//                    ser_msg = "已經完成由伺服器會入資料(code:" + Cook_DBConnector.httpstate + ") ";
//                    break;
//                case 3:
//                    ser_msg = "伺服器重定向訊息，請稍後在試(code:" + Cook_DBConnector.httpstate + ") ";
//                    servermsgcolor = ContextCompat.getColor(this, R.color.Red);
//                    break;
//                case 4:
//                    ser_msg = "用戶端錯誤回應，請稍後在試(code:" + Cook_DBConnector.httpstate + ") ";
//                    servermsgcolor = ContextCompat.getColor(this, R.color.Red);
//                    break;
//                case 5:
//                    ser_msg = "伺服器error responses，請稍後在試(code:" + Cook_DBConnector.httpstate + ") ";
//                    servermsgcolor = ContextCompat.getColor(this, R.color.Red);
//                    break;
//            }
////                Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT).show();
//        }
//        if (Cook_DBConnector.httpstate == 0) {
//            ser_msg = "遠端資料庫異常(code:" + Cook_DBConnector.httpstate + ") ";
//        }
//        b_servermsg.setText(ser_msg);
//        b_servermsg.setTextColor(servermsgcolor);
//
//        //-------------------------------------------------------------------
//    }
    //------MENU-------

    @Override
    public boolean onCreateOptionsMenu (Menu menu){
        getMenuInflater().inflate(R.menu.cedit_main, menu);
        this.menu = menu;
//
        b_m_add = menu.findItem(R.id.m_add);
        b_m_query = menu.findItem(R.id.m_query);
        b_m_list = menu.findItem(R.id.m_list);
        b_m_edit_start = menu.findItem(R.id.m_edit_start);
        b_m_edit_stop = menu.findItem(R.id.m_edit_stop);
        b_m_return = menu.findItem(R.id.m_return);
//        ========================
        u_menu_main();
        return true;
    }

    private void u_menu_main() {
//        brelative01.setVisibility(View.INVISIBLE);
//        blinear02.setVisibility(View.VISIBLE);
//        b_Relbutton.setVisibility(View.VISIBLE);
        menu.setGroupVisible(R.id.m_group1, false);
        menu.setGroupVisible(R.id.m_group2, false);
        menu.setGroupVisible(R.id.m_group3, true);
//        b_Relbutton.setVisibility(View.INVISIBLE);
//        mSpnName.setVisibility(View.INVISIBLE);
//        b_editon.setVisibility(View.INVISIBLE);
//        btEdit.setVisibility(View.INVISIBLE);
//        btDel.setVisibility(View.INVISIBLE);
        //--------------------------

    }
    //    private void u_menu_edit_main() {
////*************************************
//        if (runAuto_flag == true) {
//            handler.removeCallbacks(updateTimer); //關閉執行續
//            runAuto_flag = false;
//        }
////***************************************
//
//        menu.setGroupVisible(R.id.m_group1, false);
//        menu.setGroupVisible(R.id.m_group2, true);
//        menu.setGroupVisible(R.id.m_group3, false);
//        //--------------------------
//        b_Relbutton.setVisibility(View.VISIBLE);
//        b_editon.setVisibility(View.VISIBLE);
//        btAdd.setVisibility(View.INVISIBLE);
//        btAbandon.setVisibility(View.INVISIBLE);
//        btquery.setVisibility(View.INVISIBLE);
//        btreport.setVisibility(View.INVISIBLE);
//        btEdit.setVisibility(View.VISIBLE);
//        btDel.setVisibility(View.VISIBLE);
//        mSpnName.setVisibility(View.VISIBLE);
//        mSpnName.setEnabled(true);
//
//        u_button_ontouch();
//        touch_flag = true;  //開啟ontuchevent
//        index = mSpnName.getSelectedItemPosition(); // 找到按何項
//        //        mSpnName.setEnabled(false);
//        showRec(index); //重設spainner 小窗顯示及細目內容
//    }
//
//    private void u_button_ontouch() {
//        btTop.setVisibility(View.VISIBLE);
//        btNext.setVisibility(View.VISIBLE);
//        btPrev.setVisibility(View.VISIBLE);
//        btEnd.setVisibility(View.VISIBLE);
//    }
//
//    private void u_menu_return() {
//        menu.setGroupVisible(R.id.m_group1, false);
//        menu.setGroupVisible(R.id.m_group2, false);
//        menu.setGroupVisible(R.id.m_group3, true);
//    }
//
//    private void stop_edit() {
//        mSpnName.setEnabled(true);
//        old_index = mSpnName.getSelectedItemPosition();
//        u_menu_main();
//        edittype_flag = false;
//        // -------執行匯入MySQL
//        dbmysql();
//        recSet = dbHper.getRecSet_Cook();  //重新載入SQLite
//        u_setspinner();  //重新設定spinner內容
//        index = old_index;
//        showRec(index); //重設spainner 小窗顯示及細目內容
//        //********************************************
//        if (runAuto_flag == false && edittype_flag == false) {
//            handler.post(updateTimer);  //開啟執行續
//            runAuto_flag = true;
//        }
//        //********************************************
//    }
//
    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        if (item.getItemId() == android.R.id.home) {

        }
        return super.onOptionsItemSelected(item);

    }


//    // ---------------------------------------------

}//-----------END