package com.dandan.wumingzhibei;

import com.alibaba.fastjson.JSONObject;
import com.dandan.wumingzhibei.bean.StatisticalConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 统计器启动类
 *
 * @author dandan
 * @version 2020/9/21 16:05
 */
public class StatisticalStarter {
    private static final Logger logger = LogManager.getLogger(StatisticalStarter.class);

    // 配置文件名要求
    private static final String CONFIG_FILE_NAME = "statistical_config.json";
    // xlsx文件后缀
    private static final String XLSX_SUFFIX = ".xlsx";
    // 读取文件编码
    private static final String INPUT_CHARSET = "UTF-8";

    public static void main(String[] args) {
        if (args == null) {
            logger.error("Need enter config file path when start Statistical.");
            return;
        }

        String configPath = args[0];
        StatisticalConfig statisticalConfig;
        try {
            // 读取配置文件
            String configStr = readFile(configPath);
            statisticalConfig = JSONObject.parseObject(configStr, StatisticalConfig.class);
            logger.info("Read config object:{}.", statisticalConfig);
        } catch (Exception e) {
            logger.error("Fail to read config.", e);
            return;
        }

        // 准备Excel表格 根据Excel后缀区分使用何种工作簿 -> HSSFWorkbook(.xls) XSSFWorkbook(.xlsx)
        // http://poi.apache.org/components/spreadsheet/quick-guide.html#NewSheet
        Workbook xssfWorkbook = new XSSFWorkbook();
        Sheet sheet = xssfWorkbook.createSheet("StatisticalResult");
        try {
            File file = new File(statisticalConfig.getFileAbsolutePath());
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));

            // 设置表头
            Row row = sheet.createRow(0);
            for (int i = 0; i < statisticalConfig.getColumnCount(); i++) {
                row.createCell(i).setCellValue("数据列" + (i + 1));
            }

            // 获取每一行需要包含的内容
            String logLine;
            int logLineNumber = 1;
            while ((logLine = bufferedReader.readLine()) != null) {
                if (logLine != "" && logLine.contains(statisticalConfig.getContainStr())) {
                    // 创建行
                    Row rowInfo = sheet.createRow(logLineNumber);

                    // 正则匹配数据
                    Pattern pattern = Pattern.compile(statisticalConfig.getRegexStr());
                    Matcher matcher = pattern.matcher(logLine);

                    // 将结果写入Excel
                    for (int i = 0; i < statisticalConfig.getColumnCount(); i++) {
                        // 创建单元格，如果匹配到指定列写入到Excel中，否则对应行设置为null
                        if (matcher.find()) {
                            rowInfo.createCell(i).setCellValue(matcher.group());
                        } else {
                            rowInfo.createCell(i).setCellValue("");
                        }
                    }
                    logLineNumber++;
                }
            }
        } catch (IOException e) {
            logger.error("File read has exception:{}.", e.getMessage());
        }

        // 导出文件
        FileOutputStream fos = null;
        try {
            String exportFilePath = configPath.replace(CONFIG_FILE_NAME, System.currentTimeMillis() + XLSX_SUFFIX);
            fos = new FileOutputStream(exportFilePath);
            xssfWorkbook.write(fos);
            logger.info("Export success, file:{}.", exportFilePath);
        } catch (IOException e1) {
            logger.error("Fail to export excel file.", e1);
        } finally {
            try {
                if (fos != null)
                    fos.close();
            } catch (IOException e2) {
                logger.error("Fail to close output stream.", e2);
            }
        }
    }

    /**
     * 读取文件内容。
     *
     * @param filepath
     * @return null:文件读取失败，"":文件内容为空，否则，返回非空的文件内容
     */
    public static String readFile(String filepath) {
        // 使用字符流按照指定编码格式读取文件
        try (InputStreamReader reader = new InputStreamReader(new FileInputStream(filepath), INPUT_CHARSET)) {
            StringBuilder stringBuilder = new StringBuilder();

            // 单次读取的字符缓冲区
            char[] buffer = new char[1024];

            // 读取文件内容，read方法返回-1时表示文件内容已完全读取
            int num;
            while ((num = reader.read(buffer)) != -1) {
                stringBuilder.append(new String(buffer, 0, num));
            }
            return stringBuilder.toString();
        } catch (IOException e) {
            logger.error("Failed to read file '{}'.", filepath, e);
            return null;
        }
    }
}
