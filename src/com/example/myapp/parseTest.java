package com.example.myapp;

import android.app.Activity;
import android.app.ListActivity;
import android.content.ContentValues;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.database.sqlite.SQLiteCantOpenDatabaseException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.os.Environment;
import android.widget.ArrayAdapter;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class parseTest extends ListActivity {
    String item;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        SQLiteDatabase db = null;
        String TABLE_NAME = "EMPLOYEE";
        String col1 = "FNAME";
        String col2 = "MINIT";
        String col3 = "LNAME";
        String col4 = "SSN";
        String col5 = "BDATE";
        String col6 = "ADDRESS";
        String col7 = "SEX";
        String col8 = "SALARY";
        String col9 = "SUPERSSN";
        String col10 = "DNO";
        String[] cols = {col1, col2, col3, col4 , col5, col6, col7, col8, col9, col10};


        try {
            String SDPath = "data/data/com.example.myapp";
            String dbPath = SDPath + "/myDB.db";
            db = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.CREATE_IF_NECESSARY);
            //if(db != null)
                db.execSQL("drop table " + TABLE_NAME + ";");
            db.execSQL("CREATE TABLE "+ TABLE_NAME + "(_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + col1 + " TEXT ," + col2 + " TEXT ," + col3 + " TEXT ," + col4 + " TEXT ," + col5 + " TEXT ,"
            +col6+ " TEXT ," + col7 + " TEXT ," + col8 + " TEXT ," + col9 + " TEXT ," + col10 + " TEXT );");
        }catch(SQLiteCantOpenDatabaseException CODE){CODE.printStackTrace();}
        catch(SQLiteException SE){SE.printStackTrace();}

        try{
            item = getItemFromXML(this);
        }catch (XmlPullParserException XPPE){XPPE.printStackTrace();}
        catch(IOException IOE){IOE.printStackTrace();
    }
        String[] items = item.split("\n");
        setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items));

        ContentValues[] cv = new ContentValues[items.length];


        for (int i = 0, j = 0; i < items.length; i++,j ++ ) {
            if(j%10 == 0) {
                j = 0;
            }
            String temp = items[i];
            ContentValues c = new ContentValues();
            //should have new ContentValue for each employee
            c.put(cols[j], temp);
            cv[i] = c;

        }
/*
        ContentValues c = new ContentValues();
        for(int i = 0, j = 0; i < items.length; i++, j++){
            String temp = items[i];
            c.put(cols[j%5],temp);
            cv[i] = c;
        }
*/
        if(db !=null){
            for (int i = 0; i < items.length; i ++){
                    db.insert(TABLE_NAME, null, cv[i]);
            }
        }

}

public String getItemFromXML(Activity activity)throws XmlPullParserException, IOException {
    StringBuffer sb = new StringBuffer();
    Resources res = activity.getResources();

    XmlResourceParser xrp = res.getXml(R.xml.employee);

    String FNAME=null, MINIT=null, LNAME=null, SSN=null, BDATE=null, ADDRESS = null,
    SEX = null, SALARY = null, SUPERSSN = null, DNO = null;

    xrp.next();
    int eventType = xrp.getEventType();
    while(eventType != xrp.END_DOCUMENT){
        if(eventType == xrp.START_TAG){
           /* if(xrp.getName().equals("book")){
               // sb.append("\n");
                //sb.append(xrp.getAttributeValue(null, "id"));
            }
            else*/ if(xrp.getName().equals("FNAME")){
                FNAME = xrp.nextText();
                sb.append(FNAME+"\n");
            }
            else if(xrp.getName().equals("MINIT")){
                MINIT = xrp.nextText();
                sb.append(MINIT+"\n");
            }
            else if(xrp.getName().equals("LNAME")){
                LNAME = xrp.nextText();
                sb.append(LNAME+"\n");
            }
            else if(xrp.getName().equals("SSN")){
                SSN = xrp.nextText();
                sb.append(SSN+"\n");
            }
            else if(xrp.getName().equals("BDATE")){
                BDATE = xrp.nextText();
                sb.append(BDATE+"\n");
            }
            else if(xrp.getName().equals("ADDRESS")){
                ADDRESS  = xrp.nextText();
                sb.append(ADDRESS);
            }else if(xrp.getName().equals("SEX")){
                SEX  = xrp.nextText();
                sb.append(SEX);
            }else if(xrp.getName().equals("SALARY")){
                SALARY  = xrp.nextText();
                sb.append(SALARY);
            }else if(xrp.getName().equals("SUPERSSN")){
                SUPERSSN  = xrp.nextText();
                sb.append(SUPERSSN);
            }else if(xrp.getName().equals("DNO")){
                DNO  = xrp.nextText();
                sb.append(DNO);
            }

        }
        //sb.append("\n");
        eventType = xrp.next();
    }
    return sb.toString();
}
}