package com.example.dialogfragmentapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * SQLiteOpenHelperのサブクラスSampleSQLiteOpenHelperの実装
 */
class MySQLiteOpenHelper extends SQLiteOpenHelper {
	
	private static final String DB_NAME = "sqlite_sample.db";
	private static final int DB_VERSION = 1;
	
	private static final String CREATE_TABLE =  "create table sample_table ( " +
										"_id integer primary key autoincrement, " +
										"title text not null," +
										"memo text);";
	
	private static final String DROP_TABLE = "drop table sample_table;";
	
	public MySQLiteOpenHelper(Context c) {
		super(c, DB_NAME, null, DB_VERSION);
	}
	
	/**
	 * データベースファイル初回使用時に実行される処理
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		// テーブル作成のクエリを発行
		db.execSQL(CREATE_TABLE);
	}
	
	/**
	 * データベースのバージョンアップ時に実行される処理
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// テーブルの破棄と再作成
		db.execSQL(DROP_TABLE);
		onCreate(db);
	}
}
