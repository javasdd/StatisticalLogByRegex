package com.dandan.wumingzhibei.bean;

/**
 * @author dandan
 * @version 2020/9/21 17:05
 */
public class StatisticalConfig {
    // 待统计文件绝对路径
    private String fileAbsolutePath;

    // 文件每一行内容需要包含的字符(串)
    private String containStr;

    // 正则表达式(自行测试正则结果)
    private String regexStr;

    // 需要匹配的列数
    private Integer columnCount;

    public String getFileAbsolutePath() {
        return fileAbsolutePath;
    }

    public void setFileAbsolutePath(String fileAbsolutePath) {
        this.fileAbsolutePath = fileAbsolutePath;
    }

    public String getContainStr() {
        return containStr;
    }

    public void setContainStr(String containStr) {
        this.containStr = containStr;
    }

    public String getRegexStr() {
        return regexStr;
    }

    public void setRegexStr(String regexStr) {
        this.regexStr = regexStr;
    }

    public Integer getColumnCount() {
        return columnCount;
    }

    public void setColumnCount(Integer columnCount) {
        this.columnCount = columnCount;
    }

    @Override
    public String toString() {
        return "StatisticalConfig{" +
                "fileAbsolutePath='" + fileAbsolutePath + '\'' +
                ", containStr='" + containStr + '\'' +
                ", regexStr='" + regexStr + '\'' +
                ", columnCount=" + columnCount +
                '}';
    }
}
