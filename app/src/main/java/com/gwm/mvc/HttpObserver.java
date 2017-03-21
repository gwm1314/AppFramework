package com.gwm.mvc;

import java.io.File;
import java.util.List;
import android.util.SparseArray;

/**
 * MVC模式中Service跟V(视图层)通讯的回调接口
 * @author gwm
 */
public interface HttpObserver {
	/**
	 * Service跟V视图层通讯接口，当Serivice层解析完数据后会调用该方法把数据带到视图层
	 * @param model 服务层Service解析完之后所生成的序列化对象
	 */
	void handlerIntent(BaseEntry model, int urlId) throws Exception;
	
	void handlerIntent(List<? extends BaseEntry> models, int urlId) throws Exception;
	
	void handlerIntent(SparseArray<? extends BaseEntry> models, int urlId) throws Exception;
	
	void handlerIntent(File file, int urlId) throws Exception;
	
	void handlerIntent(String result, int urlId) throws Exception;

    void handlerIntent(boolean result, int urlId) throws Exception;
}
