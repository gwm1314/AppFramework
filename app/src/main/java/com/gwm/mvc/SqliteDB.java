package com.gwm.mvc;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public abstract class SqliteDB extends SQLiteOpenHelper {
	private static final String DB_NAME = "appframework.db";
    private SQLiteDatabase db;
	protected SqliteDB(Context context,int version){
		super(context, DB_NAME, null, version);
	}

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;
        db.beginTransaction();
        onCreateTable();
        db.endTransaction();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        this.db = db;
        db.beginTransaction();
        onUpdate(oldVersion,newVersion);
        db.endTransaction();
    }
    public abstract void onCreateTable();
    public abstract void onUpdate(int oldVersion, int newVersion);

    protected void execSql(String sql){
        if(db != null && db.isOpen())
            db.execSQL(sql);
    }

}
