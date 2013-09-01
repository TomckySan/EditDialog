package com.example.dialogfragmentapplication;

import android.app.Activity;
import android.app.Dialog;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.TextView;

public class EditDialogFragment extends DialogFragment {
	
	OnUpdateEditDataListener mListener;
	
	TextView mTxtTitle = null;
	TextView mTxtMemo = null;
	
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
		
		dialog.findViewById(R.id.finish_button).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				SQLiteDatabase db = ((MainActivity)getActivity()).mDb;
				
				// 編集後のデータをまとめる
				final EditData editData = new EditData(mTxtTitle.getText().toString(), mTxtMemo.getText().toString());
				// データ挿入
				DatabaseExcuteUtility.insertDataExcute(db, editData);
				
				// アクティビティへイベントを飛ばす
				mListener.onUpdateEditData();
				
				// ダイアログを閉じる
				dismiss();
			}
		});
		
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
	static EditDialogFragment newInstance(EditData data) {
		
		final EditDialogFragment customDialog = new EditDialogFragment();
		
		String title = data.getTitle();
		String memo = data.getMemo();
		
		// 値をBundleに渡してsetArgumentsしてやる
		Bundle args = new Bundle();
		args.putString("Title", title);
		args.putString("Memo", memo);
		
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
