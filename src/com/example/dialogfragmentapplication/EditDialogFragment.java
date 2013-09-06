package com.example.dialogfragmentapplication;

import android.app.Activity;
import android.app.Dialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

public class EditDialogFragment extends DialogFragment {
	
	private static final String DEBUG_TAG = "EditDialogFragment_DB_Table";
	
	OnUpdateEditDataListener mListener;
	
	TextView mTxtTitle = null;
	TextView mTxtMemo = null;
	
	boolean mHasDataNothingDB;
	
	/**
	 * ダイアログ生成時イベント
	 */
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		
		// ダイアログ系は必ずActivityに紐付ける
		Dialog dialog = new Dialog(getActivity());
		// レイアウトビュー設定
		dialog.setContentView(R.layout.custom_dialog);
		
		// Bundleデータを取得・設定
		String title = getArguments().getString("Title");
		mTxtTitle = (TextView)dialog.findViewById(R.id.title);
		mTxtTitle.setText(title);

		String memo = getArguments().getString("Memo");
		mTxtMemo = (TextView)dialog.findViewById(R.id.memo);
		mTxtMemo.setText(memo);
		
		mHasDataNothingDB = getArguments().getBoolean("NothingData");
		
		dialog.findViewById(R.id.finish_button).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				// 未入力状態はNG
				if (mTxtTitle.getText() == null || mTxtTitle.getText().length() == 0) {
					Toast.makeText(getActivity(), "タイトルを入力してください", Toast.LENGTH_SHORT).show();
					return;
				}
				
				SQLiteDatabase db = ((MainActivity)getActivity()).mDb;
				
				// 編集後のデータをまとめる
				final EditData editData = new EditData(mTxtTitle.getText().toString(), mTxtMemo.getText().toString());
				// データ挿入
				String msg = DatabaseExcuteUtility.editDataExcute(db, editData, mHasDataNothingDB);
				Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
				
				// アクティビティへイベントを飛ばす
				mListener.onUpdateEditData();
				
				// ダイアログを閉じる
				dismiss();
			}
		});
		
		// ---デバッグ用---
		// テーブルデータをLogCatに出力
		Cursor cursor = ((MainActivity)getActivity()).mDb.query(
				"sample_table", new String[] {"_id", "title", "memo"},
				null, null, null, null, "_id DESC");
		// 参照先を一番初始めに
		boolean isEof = cursor.moveToFirst();
		while(isEof) {
			Log.d(DEBUG_TAG, ""+cursor.getInt(0)+":"+cursor.getString(1)+":"+cursor.getString(2));
			isEof = cursor.moveToNext();
		}
		// ---------------
		
		return dialog;
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mListener = (OnUpdateEditDataListener)activity;
		} catch (ClassCastException e){
			throw new IllegalStateException("activity should implement FragmentCallbacks", e);
			// throw new ClassCastException(activity.toString() + " must implement OnArticleSelectedListener");
		} 
	}
	
	public static interface OnUpdateEditDataListener {
		public void onUpdateEditData();
	}
	
	/**
	 * ここでダイアログのサイズを指定してやる
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		Dialog dialog = getDialog();
		customDialogWindow(dialog);
	}
	
	/**
	 * CustomDialogFragmentのインスタンス生成(できる限りMainActivityには書かない)
	 * @param data
	 */
	static EditDialogFragment newInstance(EditData data, boolean hasDataNothingDB) {
		
		final EditDialogFragment customDialog = new EditDialogFragment();
		
		String title = data.getTitle();
		String memo = data.getMemo();
		
		// 値をBundleに渡してsetArgumentsしてやる
		Bundle args = new Bundle();
		args.putString("Title", title);
		args.putString("Memo", memo);
		args.putBoolean("NothingData", hasDataNothingDB);
		
		customDialog.setArguments(args);
		
		return customDialog;
	}
		
	/**
	 * Dialogウィンドウのカスタマイズ
	 * @param dialog
	 */
	private void customDialogWindow(Dialog dialog) {
		
		WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
		
		// ディスプレイ情報取得
		DisplayMetrics metrics = getResources().getDisplayMetrics();
		lp.width = (int)(metrics.widthPixels * 0.8);
		lp.height = (int)(metrics.heightPixels * 0.8);
		dialog.getWindow().setAttributes(lp);
		
	}
}
