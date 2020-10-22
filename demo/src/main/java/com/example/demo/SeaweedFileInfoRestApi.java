package com.example.demo;

import com.example.demo.util.ExcelType;
import com.example.demo.util.ExcelUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@RequestMapping("/system/file")
@RestController
public class SeaweedFileInfoRestApi {

    private final Logger logger = LoggerFactory.getLogger(SeaweedFileInfoRestApi.class);

    @GetMapping(value = "/excel")
    public RestResponse getFilePath() throws IOException {
        // 读取文件
        InputStream inputStream = getInputStream("C:\\Users\\cooly\\Desktop\\包年 - 覆盖延伸系统 基础数据 50K - 导入.xls");
        // 解析文件
        List<ExcelType> list = ExcelUtil.excelToObj(inputStream, 1, ExcelType.class);
        int num = 10000;
        int size = list.size();
        int spliNum = size/num + 1;
        List<ExcelType> excelTypes = new ArrayList<>();
        for (int i = 0; i < spliNum; i++) {
            if (size > num*(i+1)){
                excelTypes = list.subList(i * num,num*(i+1));
            } else {
                excelTypes = list.subList(i * num,size);
            }
            String fileName = "测试exportExcel" + i;
            FileOutputStream outputStream = new FileOutputStream("G:\\" + fileName + ".xlsx");
            ExcelUtil.export(excelTypes,outputStream,"第一个sheet",null);
            System.out.println("导出成功");
        }
        return null;
    }

    private InputStream getInputStream(String fileName) {
        try {
            return new FileInputStream(new File(fileName));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

}
