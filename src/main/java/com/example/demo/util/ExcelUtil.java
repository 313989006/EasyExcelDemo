package com.example.demo.util;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.event.WriteHandler;
import com.alibaba.excel.metadata.BaseRowModel;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.support.ExcelTypeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
* @Description: TODO
* @Param:
* @return:
* @Author: ma.kangkang
* @Date: 2020/10/22
*/
public class ExcelUtil {

    private static final Logger logger = LoggerFactory.getLogger(ExcelUtil.class);

    /**
     * 导出excel
     *
     * @param list         数据列表
     * @param out          输出流
     * @param sheetName    表格名
     * @param writeHandler 导出表格格式
     */
    public static <T extends BaseRowModel> void export(List<T> list, OutputStream out, String sheetName, WriteHandler writeHandler) throws IOException {
        if (!CollectionUtils.isEmpty(list)) {
            ExcelWriter writer = new ExcelWriter(null, out, ExcelTypeEnum.XLSX, true, (com.alibaba.excel.write.handler.WriteHandler) writeHandler);
            Sheet sheet = new Sheet(1, 0, list.get(0).getClass());
            sheet.setSheetName(sheetName);
            writer.write(list, sheet);
            writer.finish();
            out.flush();
        }
    }

    /**
     * 解析excel
     *
     * @param inputStream 目标excel输入流
     * @param clazz       目标对象类型
     * @return 解析结果
     */
    public static <T extends BaseRowModel> List<T> excelToObj(InputStream inputStream, int headLineNum, Class<ExcelType> clazz) {
        List<T> datas = new ArrayList<>();
        ExcelListener excelListener = new ExcelUtil().getListener(clazz);
        try{
            ExcelReader excelReader = new ExcelReader(inputStream, null, excelListener);
            excelReader.read(new Sheet(1, headLineNum, clazz));
        } catch (Exception e ){
            logger.error("为了解决GC溢出问题", e);
        }
        List<Object> list = excelListener.getDatas();
        for (Object o : list) {
            datas.add((T) o);
        }
        return datas;
    }

    public static <T extends BaseRowModel> List<T> excelFirstToObj(InputStream inputStream, int headLineNum, Class<T> clazz) {
        List<T> datas = new ArrayList<>();
        ExcelListener excelListener = new ExcelUtil().getListener(clazz);
        try{
        ExcelReader excelReader = new ExcelReader(inputStream, null, excelListener);
        excelReader.read(new Sheet(1, headLineNum, clazz));
        } catch (Exception e ){
            logger.error("为了解决GC溢出问题");
        }
        List<Object> list = excelListener.getDatas();
        for (Object o : list) {
            datas.add((T) o);
        }
        return datas;
    }

    private ExcelListener getListener(Class clazz) {
        return new ExcelListener(clazz);
    }

    private class ExcelListener extends AnalysisEventListener {

        public ExcelListener(Class clazz) {
            this.clazz = clazz;
        }

        private Class clazz;

        private int lineNum = 0;

        // 从excel读取到的数据
        private List<Object> datas = new ArrayList<>();

        // 每次读取一行数据，读取到数据时进行invoke中的操作
        @Override
        public void invoke(Object o, AnalysisContext analysisContext) {

            if (clazz == ExcelType.class) {
//                if (lineNum == 0) {
                    datas.add(o);
                    lineNum++;
//                }
            }
//            else {
//                // 排除空行，将非空行加入datas
//                if (validateField(o)) {
//                    datas.add(o);
//                }
//            }
        }

        // excel读取完毕时执行的操作，一般用于释放资源，此处不能调用datas.clear()，否则返回的数据始终为空
        @Override
        public void doAfterAllAnalysed(AnalysisContext analysisContext) {

        }

        // 获取解析到的数据
        List<Object> getDatas() {
            return datas;
        }

        // 判断对象是否每个字段都为空，若都为空返回false，否则返回true
        private boolean validateField(Object object) {
            boolean target = false;
            for (Field f : object.getClass().getDeclaredFields()) {
                f.setAccessible(true);
                try {
//                    if (StringUtils.isNotEmpty(f.get(object))) {
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

    }

    /**
     * 解析excel
     *
     * @param clazz       目标对象类型
     * @param inputStream 目标excel输入流
     * @param headLineNum 表头行数
     * @param <T>         泛型
     * @return 解析结果
     */
    public static <T> List<T> read(Class<T> clazz, InputStream inputStream, int headLineNum) throws Exception {
        ExcelDataListener<T> objectreadAuditExcelLisner = new ExcelDataListener<>();
        EasyExcel.read(inputStream, clazz, objectreadAuditExcelLisner).sheet().headRowNumber(headLineNum).doRead();
        return objectreadAuditExcelLisner.getDataList();
    }
}
