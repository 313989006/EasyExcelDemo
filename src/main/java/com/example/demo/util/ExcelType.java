package com.example.demo.util;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.Getter;
import lombok.Setter;

/**
* @Description: 用于判断上传稽核明细文件与所选稽核类型是否一致
* @Param:
* @return:
* @Author: ma.kangkang
* @Date: 2020/10/22
*/
@Getter
@Setter
public class ExcelType extends BaseRowModel {

    // 省份名称
    @ExcelProperty(value = "所属省份(string、必填)",index = 0)
    private String prvName;

    // 地市名称
    @ExcelProperty(value = "所属地市(string、必填)",index = 1)
    private String cityName;

    // 区县名称
    @ExcelProperty(value = "所属区县(string、必填)",index = 2)
    private String regName;

    // 室分系统名称
    @ExcelProperty(value = "室分系统名称(string，必填)",index = 3)
    private String shifenName;

    // 物理站点编号
    @ExcelProperty(value = "物理站点编号(string，必填)",index = 4)
    private String wuliCode;

    // 生命周期状态
    @ExcelProperty(value = "生命周期状态(string、枚举，必填)",index = 5)
    private String lifeCycleStatus;

    // 精度
    @ExcelProperty(value = "经度(float，非必填)",index = 6)
    private String jingdu;

    // 纬度
    @ExcelProperty(value = "纬度(float，非必填)",index = 7)
    private String weidu;

    // 维护单位
    @ExcelProperty(value = "维护单位(string，非必填)",index = 8)
    private String weihuDanwei;

    // 有无配套设备
    @ExcelProperty(value = "有无配套设备(string,枚举，必填)",index = 9)
    private String IsHavePeitao;

    // VIP级别

    @ExcelProperty(value = "VIP级别(string,枚举，必填)",index = 10)
    private String vipLevel;

    // 天线数量
    @ExcelProperty(value = "天线数量(int，必填)",index = 11)
    private String tianXianNum;

    // 是否长期退服
    @ExcelProperty(value = "是否长期退服(String，枚举，非必填)",index = 12)
    private String isTuifuLongTime;

    // 代维单位
    @ExcelProperty(value = "代维单位(String，枚举，必填)",index = 13)
    private String daiweiDanwei;

    // 是否包年
    @ExcelProperty(value = "是否包年（String，枚举，必填）",index = 14)
    private String isBaoNian;


}
