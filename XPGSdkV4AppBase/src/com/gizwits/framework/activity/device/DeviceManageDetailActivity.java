/**
 * Project Name:XPGSdkV4AppBase
 * File Name:DeviceManageDetailActivity.java
 * Package Name:com.gizwits.aircondition.activity.device
 * Date:2015-1-12 12:04:07
 * Copyright (c) 2014~2015 Xtreme Programming Group, Inc.
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), 
 * to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, 
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.gizwits.framework.activity.device;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.gizwits.aircondition.R;
import com.gizwits.framework.activity.BaseActivity;
import com.gizwits.framework.utils.DialogManager;
import com.gizwits.framework.utils.StringUtils;
import com.xpg.common.system.IntentUtils;
import com.xpg.ui.utils.ToastUtils;
import com.xtremeprog.xpgconnect.XPGWifiDevice;

// TODO: Auto-generated Javadoc
//TODO: Auto-generated Javadoc
/**
 * 
 * ClassName: Class DeviceManageDetailActivity. <br/>
 * 设备详细信息<br/>
 * date: 2014-12-09 17:27:10 <br/>
 * 
 * @author StephenC
 */
public class DeviceManageDetailActivity extends BaseActivity implements
		OnClickListener {

	/** The iv back. */
	private ImageView ivBack;
	private ImageView ivTick;

	/** The tv init date. */
	private TextView tvDate;

	/** The tv init place. */
	private TextView tvPlace;;

	/** The tv device type. */
	private TextView tvDeviceType;

	/** The tv device code. */
	private TextView tvDeviceCode;

	/** The et device name. */
	private EditText etName;

	/** The btn delDevice. */
	private Button btnDelDevice;

	private XPGWifiDevice xpgWifiDevice;

	private Dialog unbindDialog;

	private ProgressDialog progressDialog;

	/**
	 * ClassName: Enum handler_key. <br/>
	 * <br/>
	 * date: 2014-11-26 17:51:10 <br/>
	 * 
	 * @author Lien
	 */
	private enum handler_key {

		CHANGE_SUCCESS,

		CHANGE_FAIL,

		DELETE_SUCCESS,

		DELETE_FAIL,

	}

	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			handler_key key = handler_key.values()[msg.what];
			switch (key) {
			case CHANGE_SUCCESS:
				ToastUtils.showShort(DeviceManageDetailActivity.this, "修改成功！");
				break;

			case CHANGE_FAIL:
				ToastUtils.showShort(DeviceManageDetailActivity.this, "修改失败:"
						+ msg.obj.toString());
				break;

			case DELETE_SUCCESS:
				ToastUtils.showShort(DeviceManageDetailActivity.this, "删除成功！");
				IntentUtils.getInstance().startActivity(
						DeviceManageDetailActivity.this,
						DeviceListActivity.class);
				finish();
				break;

			case DELETE_FAIL:
				ToastUtils.showShort(DeviceManageDetailActivity.this, "删除失败:"
						+ msg.obj.toString());
				break;

			}
		}

	};

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gizwits.aircondition.activity.BaseActivity#onCreate(android.os.Bundle
	 * )
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_devices_info);
		initParams();
		initViews();
		initEvents();

	}

	private void initParams() {
		if (getIntent() != null) {
			String mac = getIntent().getStringExtra("mac");
			String did = getIntent().getStringExtra("did");
			xpgWifiDevice = findDeviceByMac(mac, did);
			xpgWifiDevice.setListener(deviceListener);
		}

	}

	/**
	 * Inits the views.
	 */
	private void initViews() {
		ivBack = (ImageView) findViewById(R.id.ivBack);
		ivTick = (ImageView) findViewById(R.id.ivTick);
		tvDate = (TextView) findViewById(R.id.tvDate);
		tvPlace = (TextView) findViewById(R.id.tvPlace);
		tvDeviceType = (TextView) findViewById(R.id.tvDeviceType);
		tvDeviceCode = (TextView) findViewById(R.id.tvDeviceCode);
		etName = (EditText) findViewById(R.id.etName);
		btnDelDevice = (Button) findViewById(R.id.btnDelDevice);
		unbindDialog = DialogManager.getUnbindDialog(this, this);
		progressDialog = new ProgressDialog(this);
		progressDialog.setMessage("删除中，请稍候...");
		progressDialog.setCancelable(false);
		if (xpgWifiDevice != null) {
			etName.setText(xpgWifiDevice.getRemark());
		}
	}

	/**
	 * Inits the events.
	 */
	private void initEvents() {
		btnDelDevice.setOnClickListener(this);
		ivBack.setOnClickListener(this);
		ivTick.setOnClickListener(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ivBack:
			onBackPressed();
			break;
		case R.id.btnDelDevice:
			DialogManager.showDialog(this, unbindDialog);

			break;
		case R.id.ivTick:
			if (!StringUtils.isEmpty(etName.getText().toString())) {
				mCenter.cUpdateRemark(setmanager.getUid(), setmanager
						.getToken(), xpgWifiDevice.getDid(), xpgWifiDevice
						.getPasscode(), etName.getText().toString());
			} else {
				ToastUtils.showShort(DeviceManageDetailActivity.this,
						"请输入一个设备名称");
			}
			break;
		case R.id.right_btn:
			DialogManager.dismissDialog(this, unbindDialog);
			DialogManager.showDialog(this, progressDialog);
			mCenter.cUnbindDevice(setmanager.getUid(), setmanager.getToken(),
					xpgWifiDevice.getDid(), xpgWifiDevice.getPasscode());
			break;
		}

	}

	@Override
	public void onBackPressed() {
		finish();
	}

	@Override
	protected void didBindDevice(int error, String errorMessage, String did) {
		Log.d("Device扫描结果", "error=" + error + ";errorMessage=" + errorMessage
				+ ";did=" + did);
		if (error == 0) {
			handler.sendEmptyMessage(handler_key.CHANGE_SUCCESS.ordinal());
		} else {
			Message msg = new Message();
			msg.what = handler_key.CHANGE_FAIL.ordinal();
			msg.obj = errorMessage;
			handler.sendMessage(msg);
		}
	}

	@Override
	protected void didUnbindDevice(int error, String errorMessage, String did) {
		DialogManager.dismissDialog(this, progressDialog);
		if (error == 0) {
			handler.sendEmptyMessage(handler_key.DELETE_SUCCESS.ordinal());
			
		} else {
			Message msg = new Message();
			msg.what = handler_key.DELETE_FAIL.ordinal();
			msg.obj = errorMessage;
			handler.sendMessage(msg);
		}
	}
}
