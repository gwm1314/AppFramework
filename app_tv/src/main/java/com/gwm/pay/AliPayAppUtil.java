package com.gwm.pay;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
/**
 * 支付宝包钱包App支付
 * @author zwb
 */
public class AliPayAppUtil {
	private Activity mActivity;
	private AliPayInfauce listener;
	private static final int SDK_PAY_FLAG = 1;

	public static interface AliPayInfauce {
		public void onSussess();
		public void onError();
	}

	public void setListener(AliPayInfauce listener) {
		this.listener = listener;
	}

	public AliPayAppUtil(Activity activity) {
		this.mActivity = activity;
	}

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SDK_PAY_FLAG:
				Result resultObj = new Result((String) msg.obj);
				String resultStatus = resultObj.resultStatus;
				// 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
				if (TextUtils.equals(resultStatus, "9000")) {
					Toast.makeText(mActivity, "支付成功", Toast.LENGTH_SHORT).show();
					if(listener != null){
						listener.onSussess();
					}
				} else {
					// 判断resultStatus 为非“9000”则代表可能支付失败
					// “8000”
					// 代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
					if (TextUtils.equals(resultStatus, "8000")) {
						Toast.makeText(mActivity, "支付结果确认中", Toast.LENGTH_SHORT).show();
					} else{
						Toast.makeText(mActivity, "支付终止", Toast.LENGTH_SHORT).show();
					}
					if(listener != null){
						listener.onError();
					}
				}
				break;
			}
		};
	};

	
	public void pay(final String info) {
		Runnable payRunnable = new Runnable() {
			@Override
			public void run() {
				// 构造PayTask 对象
				PayTask alipay = new PayTask(mActivity);
				// 调用支付接口
				String result = alipay.pay(info);
				Message msg = Message.obtain();
				msg.what = SDK_PAY_FLAG;
				msg.obj = result;
				mHandler.sendMessage(msg);
			}
		};
		Thread payThread = new Thread(payRunnable);
		payThread.start();
	}
	static class Result {
		public String resultStatus;
		public String result;
		public String memo;

		public Result(String rawResult) {
			try {
				String[] resultParams = rawResult.split(";");
				for(String resultParam : resultParams) {
					if(resultParam.startsWith("resultStatus")) {
						resultStatus = gatValue(resultParam, "resultStatus");
					}
					if (resultParam.startsWith("result")) {
						result = gatValue(resultParam, "result");
					} 
					if (resultParam.startsWith("memo")) {
						memo = gatValue(resultParam, "memo");
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		public String toString() {
			return "resultStatus={" + resultStatus + "};memo={" + memo
					+ "};result={" + result + "}";
		}

		private String gatValue(String content, String key) {
			String prefix = key + "={";
			return content.substring(content.indexOf(prefix) + prefix.length(),
					content.lastIndexOf("}"));
		}
	}
}
