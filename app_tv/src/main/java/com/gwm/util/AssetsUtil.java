package com.gwm.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;
import android.widget.Toast;
/**
 * Assets目录工具类，用于访问各种Assets目录下的文件
 * @author asus1
 *
 */
public class AssetsUtil {
	private Context context;
	private AssetManager assets;

	public AssetsUtil(Context context) {
		this.context = context;
		assets = context.getAssets();
	}
	/**
	 * 安装assets目录下的文件
	 * @param fileName 文件名称
	 */
	public void installapk(String fileName) {
		try {
			InputStream stream = assets.open(fileName);
			if (stream == null) {
				Toast.makeText(context, "文件不存在", Toast.LENGTH_SHORT).show();
				return;
			}

			String folder = Environment.getExternalStorageDirectory().getPath()+"/sm/";
			File f = new File(folder);
			if (!f.exists()) {
				f.mkdir();
			}
			String apkPath = Environment.getExternalStorageDirectory().getPath()+"/sm/test.apk";
			File file = new File(apkPath);
			if(!file.exists())
				file.createNewFile();
			writeStreamToFile(stream, file);
			installApk(apkPath);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	private void writeStreamToFile(InputStream stream, File file) {
		try {
			OutputStream output = null;
			try {
				output = new FileOutputStream(file);
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
			try {
				try {
					final byte[] buffer = new byte[1024];
					int read;

					while ((read = stream.read(buffer)) != -1)
						output.write(buffer, 0, read);
					output.flush();
				} finally {
					output.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} finally {
			try {
				stream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	private void installApk(String apkPath) {
        AppUtils.getInstance(context).installApk(apkPath);
	}
}
