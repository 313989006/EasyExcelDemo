package com.example.demo.util;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
* @Description: TODO
* @Param:
* @return:
* @Author: ma.kangkang
* @Date: 2020/10/22
*/
public class ExcelDataListener<T> extends AnalysisEventListener<T> {

    private static Logger log = LoggerFactory.getLogger(ExcelDataListener.class);

    private List<T> dataList = new ArrayList<>();

    private List<Map<Integer, String>> headMap = new ArrayList<>();

    @Override
    public void invoke(T data, AnalysisContext context) {

        // 排除空行，将非空行加入datas
        if (validateField(data)) {
            try {
                beanAttributeValueTrim(data);
            } catch (Exception e) {
                log.info("对象String属性trim操作异常，将原始对象加入列表");
            }
            dataList.add(data);
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {

    }

    public List<T> getDataList() {
        return dataList;
    }

    public List<Map<Integer, String>> getHeadMap() {
        return headMap;
    }

    /**
     * 判断对象是否每个字段都为空，若都为空返回false，否则返回true
     * @param object 待处理对象
     * @return 结果，true：不全为空，false：全为空
     */
    private boolean validateField(Object object) {
        boolean target = false;
        for (Field f : object.getClass().getDeclaredFields()) {
            f.setAccessible(true);
            try {
                if (!"".equals(f.get(object)) && f.get(object) != null) {
                    target = true;
                    break;
                }
            } catch (IllegalArgumentException | IllegalAccessException e) {
                return false;
            }
        }
        return target;
    }

    /**
     * 去掉bean中所有属性为字符串的前后空格
     *
     * @param bean 待处理对象
     * @throws Exception 异常
     */
    private void beanAttributeValueTrim(Object bean) throws Exception {
        if (bean != null) {

            //获取所有的字段包括public,private,protected,private
            Field[] fields = bean.getClass().getDeclaredFields();
            for (Field f : fields) {
                if (f.getType().getName().equals("java.lang.String")) {

                    //获取字段名
                    String key = f.getName();
                    Object value = getFieldValue(bean, key);
                    if (value == null) {
                        continue;
                    }
                    setFieldValue(bean, key, value.toString().trim());
                }
            }
        }
    }

    /**
     * 利用反射通过get方法获取bean中字段fieldName的值
     *
     * @param bean      待处理对象
     * @param fieldName 字段名称
     * @return 字段值
     * @throws Exception 异常
     */
    private Object getFieldValue(Object bean, String fieldName) throws Exception {
        String methodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        Class[] classArr = new Class[0];
        Method method = bean.getClass().getMethod(methodName, classArr);
        return method.invoke(bean);
    }

    /**
     * 利用发射调用bean.set方法将value设置到字段
     *
     * @param bean      待处理对象
     * @param fieldName 字段名称
     * @param value     字段值
     * @throws Exception 异常
     */
    private void setFieldValue(Object bean, String fieldName, Object value) throws Exception {
        String methodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);

        // 利用反射调用bean.set方法将value设置到字段
        Class[] classArr = new Class[1];
        classArr[0] = "java.lang.String".getClass();
        Method method = bean.getClass().getMethod(methodName, classArr);
        method.invoke(bean, value);
    }
}
