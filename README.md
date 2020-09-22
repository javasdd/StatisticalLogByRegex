# Welcome to StatisticalLogByRegex
## 1 功能介绍
### 1.1 本工具用于统计日志中统一格式的所需字段，并导出至Excel中
        例如一个日志文件中需要统计调用创建用户接口的频率，可以将调用日志统计输出到Excel中进行统计。
## 2 使用方式
### 2.1 配置文件
        需要有一个 statistical_config.json 配置文件，见示例(example)，配置文件字段含义见 StatisticalConfig 类中注释，统计结果表格生成在此文件的同级目录，命名为(时间戳.xlsx)。
### 2.2 启动
        IDEA编译启动，Maven打包启动，启动时需要加入参数 java -jar D:\xxxstatistical_config.json(配置文件所在路径)。