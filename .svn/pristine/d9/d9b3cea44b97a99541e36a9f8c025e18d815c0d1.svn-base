package com.goddess.ec.manage.tools;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Map.Entry;

import com.alibaba.druid.support.json.JSONUtils;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Record;

public class ToolJson {

    /**
     * Jfinal 用json转Model
     *
     * @param json
     * @param clazz
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <M extends Model<M>> M convertFromJson(String json, Class<M> clazz) {

        Map<String, Object> obj = (Map<String, Object>) JSONUtils.parse(json);
        M bean = null;
        try {
            bean = clazz.newInstance();

            // TODO json中的field key暂时删除
            if (obj != null ) {
                if (obj.containsKey("field")) {
                    obj.remove("field");
                }
                bean.setAttrs((Map<String, Object>) obj);
            }

        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }

        return bean;
    }


    /**
     * Java json转Javabean
     *
     * @param json
     * @param clazz
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <M> M fromJson2Bean(String json, Class<M> clazz) {

        Map<String, Object> obj = (Map<String, Object>) JSONUtils.parse(json);
        M bean = null;
        try {
            bean = clazz.newInstance();

            // TODO json中的field key暂时删除
            if (obj != null) {
                if (obj.containsKey("field")) {
                    obj.remove("field");
                }
            }

            for (Entry<String, Object> entry : obj.entrySet()) {

                PropertyDescriptor pro = new PropertyDescriptor(entry.getKey(), bean.getClass());
                Method method = pro.getWriteMethod();
                method.invoke(bean, entry.getValue());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return bean;
    }
    
    public static void main(String[] args) {
    	Object o = JSONUtils.parse("[{\"id\":1,\"price\":1}]");
    	System.out.println(o);
	}

}
