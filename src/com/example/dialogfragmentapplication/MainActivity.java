package com.example.dialogfragmentapplication;

import java.util.List;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.example.dialogfragmentapplication.EditDialogFragment.OnUpdateEditDataListener;

// getSupportFragmentManager()を使用するには
// FragmentActivityを継承する必要がある
public class MainActivity extends FragmentActivity implements OnClickListener, OnUpdateEditDataListener {
	
	MySQLiteOpenHelper mHelper = null;
	SQLiteDatabase mDb = null;
	EditData mData = null;
	boolean mHasDataNothingDB = true;
	
	private TextView mTitleText = null;
	private TextView mMemoText = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// DBオープン処理
		mHelper = new MySQLiteOpenHelper(getApplicationContext());
		mDb = mHelper.getWritableDatabase();
		
//		DatabaseExcuteUtility.deleteAllExcute(mDb);
		
		// テーブル内のデータを全て取得
		final List<EditData> dataList = DatabaseExcuteUtility.selectDataExcute(mDb);
		
		mData = null;
		if(dataList.size() == 0) {
			mData = new EditData("", "");
		}
		else {
			// 最初のデータだけ取得する
			mData = dataList.get(0);
			mHasDataNothingDB = false;
		}
		
		mTitleText = (TextView)findViewById(R.id.title_text);
		mTitleText.setText(mData.getTitle());
		
		mMemoText = (TextView)findViewById(R.id.memo_text);
		mMemoText.setText(mData.getMemo());
		
		Button editButton = (Button)findViewById(R.id.edit_button);
		editButton.setOnClickListener(this);
	}
	
	/**
	 * ダイアログ表示
	 */
	@Override
	public void onClick(View v) {
		
		if(v.getId() == R.id.edit_button) {
			EditDialogFragment customDialog = EditDialogFragment.newInstance(mData, mHasDataNothingDB);
			customDialog.show(getSupportFragmentManager(), "customDialog");
		}
	}

	@Override
	public void onUpdateEditData() {
		// テーブル内のデータを全て取得
		final List<EditData> dataList = DatabaseExcuteUtility.selectDataExcute(mDb);
		
		mData = null;
		if(dataList.size() == 0) {
			mData = new EditData("", "");
		}
		else {
			// 最初のデータだけ取得する
			mData = dataList.get(0);
		}
		
		mTitleText.setText(mData.getTitle());
		mMemoText.setText(mData.getMemo());
	}
}
