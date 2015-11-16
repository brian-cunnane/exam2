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

/**
 * Created by brian on 15/11/2015.
 */
public class parseTest extends ListActivity {
    String item;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        SQLiteDatabase db = null;
        String TABLE_NAME = "books";
        String col1 = "author";
        String col2 = "title";
        String col3 = "genre";
        String col4 = "price";
        String col5 = "publish date";
        String col6 = "description";


        try {
            String SDPath = "data/data/com.example.myapp";
            String dbPath = SDPath + "/myDB.db";
            db = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.CREATE_IF_NECESSARY);
            db.execSQL("drop table " + TABLE_NAME + ";");
            db.execSQL("CREATE TABLE "+ TABLE_NAME + "(_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + col1 + " TEXT ," + col2 + " TEXT ," + col3 + " TEXT ," + col4 + " TEXT );");
        }catch(SQLiteCantOpenDatabaseException CODE){CODE.printStackTrace();}
        catch(SQLiteException SE){SE.printStackTrace();}

        try{
            item = getItemFromXML(this);
        }catch (XmlPullParserException XPPE){XPPE.printStackTrace();}
        catch(IOException IOE){IOE.printStackTrace();
    }
        String[] items = item.split("\n" + " ");
        setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items));

        ContentValues cv = new ContentValues();
        if(db !=null){
            for (int i = 0; i < items.length; i ++){
                db.insert(TABLE_NAME, null, cv);
            }
        }

}

public String getItemFromXML(Activity activity)throws XmlPullParserException, IOException {
    StringBuffer sb = new StringBuffer();
    Resources res = activity.getResources();

    XmlResourceParser xrp = res.getXml(R.xml.books);

    String author=null, title=null,genre=null,price=null,publish_date=null;

    xrp.next();
    int eventType = xrp.getEventType();
    while(eventType != xrp.END_DOCUMENT){
        if(eventType == xrp.START_TAG){
            if(xrp.getName().equals("book")){
                sb.append("\n");
                sb.append(xrp.getAttributeValue(null, "id") + "\n");
            }
            else if(xrp.getName().equals("author")){
                author = xrp.nextText();
                sb.append("\n author: " +author);
            }
            else if(xrp.getName().equals("title")){
                title = xrp.nextText();
                sb.append("\n" + title);
            }
            else if(xrp.getName().equals("genre")){
                genre = xrp.nextText();
                sb.append("\nGenre: " + genre);
            }
            else if(xrp.getName().equals("price")){
                price = xrp.nextText();
                sb.append("\n" + price);
            }
            else if(xrp.getName().equals("publish_date")){
                publish_date = xrp.nextText();
                sb.append("\n" + publish_date);
            }
            else if(xrp.getName().equals("description")){
                String description  = xrp.nextText();
                sb.append("\n" + description);
            }
        }
        eventType = xrp.next();
    }
    return sb.toString();
}
}