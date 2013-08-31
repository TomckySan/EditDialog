package com.example.dialogfragmentapplication;

/**
 * 編集するデータクラス
 */
final public class EditData {
	
	private final String TITLE;
	private final String MEMO;
	
	public EditData(String title, String memo) {
		this.TITLE = title;
		this.MEMO = memo;
	}
	
	public String getTitle() {
		return TITLE;
	}
	
	public String getMemo() {
		return MEMO;
	}
}
