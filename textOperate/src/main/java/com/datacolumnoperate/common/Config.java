package com.datacolumnoperate.common;

import java.util.List;

/**
 * Config 类用于映射和存储 config.json 配置文件中的参数，用于指导文件处理的流程与规则。
 *
 * 在程序的运行过程中，会先从指定的 JSON 配置文件中解析出一个 Config 对象。
 * Config 对象包含输入/输出文件的路径、输入/输出分隔符，以及对多个列进行操作的详细配置列表。
 *
 * 典型的 JSON 配置文件示例（config.json）可能如下：
 * {
 *   "inputFile": "data/input.csv",
 *   "outputFile": "data/output.csv",
 *   "inputDelimiter": ",",
 *   "outputDelimiter": ",",
 *   "columns": [
 *       {
 *           "columnIndex": 2,
 *           "operations": [
 *               {
 *                   "type": "REGEX_REPLACE",
 *                   "params": {
 *                       "regex": "\\d{3}-\\d{4}",
 *                       "replacement": "****-****"
 *                   }
 *               },
 *               {
 *                   "type": "ENCRYPT",
 *                   "params": {
 *                       "method": "AES",
 *                       "key": "mySecretKey12345"
 *                   }
 *               }
 *           ]
 *       }
 *   ]
 * }
 *
 * 在上述示例中，程序会读取 input.csv 文件，以逗号作为输入分隔符，对第3列（columnIndex=2）的值首先进行正则替换，然后进行AES加密处理，并将结果写入 output.csv 文件中。
 */
public class Config {
    /**
     * inputFile 表示要处理的输入文件的路径（相对或绝对路径）。
     * 程序会从该文件中读取原始数据行。
     */
    private String inputFile;

    /**
     * outputFile 表示处理后的数据要输出到的文件路径。
     * 程序在完成所有列操作后，会将结果写入该文件中。
     */
    private String outputFile;

    /**
     * inputDelimiter 表示输入文件的列分隔符（例如 "," 或 "\t"）。
     * 程序读取每一行时会根据此分隔符切分成多个列值。
     */
    private String inputDelimiter;

    /**
     * outputDelimiter 表示输出文件的列分隔符（例如 "," 或 "\t"）。
     * 当程序处理完成后，会使用此分隔符将处理后的列值重新拼接成一行写入输出文件。
     */
    private String outputDelimiter;

    /**
     * columns 是一个由 ColumnOperationConfig 对象组成的列表，用于描述对各个列进行的特定操作。
     * 每个 ColumnOperationConfig 都包含一个列索引（columnIndex）和一组操作（operations）。
     * 这些操作可以包括加密、正则替换、子字符串提取、时间格式转换等。
     */
    private List<ColumnOperationConfig> columns;

    /**
     * 默认构造函数。在反序列化 JSON 时（例如使用 Jackson 的 ObjectMapper），
     * 没有带参构造器时通常需要一个无参构造函数来创建对象实例。
     */
    public Config() {
    }

    /**
     * 带参构造函数。可以在需要时手动创建 Config 对象时使用。
     *
     * @param inputFile 输入文件路径
     * @param outputFile 输出文件路径
     * @param inputDelimiter 输入文件列分隔符
     * @param outputDelimiter 输出文件列分隔符
     * @param columns 列操作配置列表
     */
    public Config(String inputFile, String outputFile, String inputDelimiter, String outputDelimiter, List<ColumnOperationConfig> columns) {
        this.inputFile = inputFile;
        this.outputFile = outputFile;
        this.inputDelimiter = inputDelimiter;
        this.outputDelimiter = outputDelimiter;
        this.columns = columns;
    }

    // Getters and Setters

    /**
     * 获取输入文件路径
     *
     * @return 输入文件的路径字符串
     */
    public String getInputFile() {
        return inputFile;
    }

    /**
     * 设置输入文件路径
     *
     * @param inputFile 输入文件路径
     */
    public void setInputFile(String inputFile) {
        this.inputFile = inputFile;
    }

    /**
     * 获取输出文件路径
     *
     * @return 输出文件的路径字符串
     */
    public String getOutputFile() {
        return outputFile;
    }

    /**
     * 设置输出文件路径
     *
     * @param outputFile 输出文件路径
     */
    public void setOutputFile(String outputFile) {
        this.outputFile = outputFile;
    }

    /**
     * 获取输入文件的列分隔符
     *
     * @return 输入文件的列分隔符（如 ","）
     */
    public String getInputDelimiter() {
        return inputDelimiter;
    }

    /**
     * 设置输入文件的列分隔符
     *
     * @param inputDelimiter 输入文件的列分隔符（如 ","）
     */
    public void setInputDelimiter(String inputDelimiter) {
        this.inputDelimiter = inputDelimiter;
    }

    /**
     * 获取输出文件的列分隔符
     *
     * @return 输出文件的列分隔符（如 ","）
     */
    public String getOutputDelimiter() {
        return outputDelimiter;
    }

    /**
     * 设置输出文件的列分隔符
     *
     * @param outputDelimiter 输出文件的列分隔符（如 ","）
     */
    public void setOutputDelimiter(String outputDelimiter) {
        this.outputDelimiter = outputDelimiter;
    }

    /**
     * 获取列操作配置列表
     *
     * @return 一个包含多个 ColumnOperationConfig 的列表，
     *         每个 ColumnOperationConfig 定义了对特定列的操作集合
     */
    public List<ColumnOperationConfig> getColumns() {
        return columns;
    }

    /**
     * 设置列操作配置列表
     *
     * @param columns 列操作配置列表
     */
    public void setColumns(List<ColumnOperationConfig> columns) {
        this.columns = columns;
    }

    /**
     * 返回 Config 对象的字符串表示形式，用于调试和日志记录。
     *
     * @return 包含 inputFile、outputFile、inputDelimiter、outputDelimiter、columns 信息的字符串
     */
    @Override
    public String toString() {
        return "Config{" +
                "inputFile='" + inputFile + '\'' +
                ", outputFile='" + outputFile + '\'' +
                ", inputDelimiter='" + inputDelimiter + '\'' +
                ", outputDelimiter='" + outputDelimiter + '\'' +
                ", columns=" + columns +
                '}';
    }
}
