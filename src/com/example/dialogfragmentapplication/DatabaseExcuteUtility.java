package com.example.dialogfragmentapplication;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * データベースに対する実行処理群クラス
 */
final class DatabaseExcuteUtility {
	
	// インスタンス化不可
	private DatabaseExcuteUtility() {}
	
	/**
	 * テーブルに編集データを追加
	 * @param db
	 * @param editData
	 */
	static public void insertDataExcute(SQLiteDatabase db, EditData editData) {
		// テーブルにデータ追加
		// ContentValuesにデータを格納
		ContentValues values = new ContentValues();
		values.put("title", editData.getTitle());
		values.put("memo", editData.getMemo());
		// データの挿入
		db.insert("sample_table", null, values);
	}
	
	/**
	 * テーブルから全データを取得
	 */
	static public List<EditData> selectAllExcute(SQLiteDatabase db) {
		
		List<EditData> tableDataList = new ArrayList<EditData>();
		
		Cursor cursor = db.query(
				"sample_table", new String[] {"_id", "title", "memo"}, 
				null, null, null, null, "_id DESC");
		
		// 参照先を一番始めに
		boolean isEof = cursor.moveToFirst();
		
		while(isEof) {
			String title = cursor.getString(cursor.getColumnIndex("title"));
			String memo = cursor.getString(cursor.getColumnIndex("memo"));
			
			// 取得データオブジェクト生成
			EditData data = new EditData(title, memo);
			tableDataList.add(data);
			
			isEof = cursor.moveToNext();
		}
		cursor.close();
		
		return tableDataList;
	}
}
