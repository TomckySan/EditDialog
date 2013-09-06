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
	public static String editDataExcute(SQLiteDatabase db, EditData editData, boolean hasDataNothingDB) {
		// ContentValuesにデータを格納
		ContentValues values = new ContentValues();
		values.put("title", editData.getTitle());
		values.put("memo", editData.getMemo());
		// 新規の場合テーブルにデータ追加
		if(hasDataNothingDB) {
			// データの挿入
			db.insert("sample_table", null, values);
			return "新規登録しました";
		}
		// 既にデータがある場合は更新
		else {
			// データの更新
			final String UPSATE_QUERY = "update sample_table " +
										"set title = '" + editData.getTitle() +
										"', memo = '" + editData.getMemo() +
										"' where _id = (select max(_id) from sample_table)";
			db.execSQL(UPSATE_QUERY);
			return "データを更新しました";
		}
	}
	
	/**
	 * テーブルから全データを取得
	 */
	public static List<EditData> selectDataExcute(SQLiteDatabase db) {
		
		List<EditData> tableDataList = new ArrayList<EditData>();
		
		Cursor cursor = db.query(
				"sample_table", new String[] {"max(_id)", "title", "memo"}, 
				null, null, null, null, "_id DESC");
		
		// 参照先を一番始めに(検索直後は0件目を指しているため)
		boolean isEof = cursor.moveToFirst();
		
		// データがなかった時のためにもこのループに入る条件文は必須
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
	
	/**
	 * 無条件全削除(デバッグ用)
	 */
	public static void deleteAllExcute(SQLiteDatabase db) {
		db.delete("sample_table", null, null);
	}
}
