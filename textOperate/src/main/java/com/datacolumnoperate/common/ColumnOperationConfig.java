package com.datacolumnoperate.common;

import java.util.List;

/**
 * ColumnOperationConfig 类用于描述对某一特定列（columnIndex）所要执行的一系列操作（operations）。
 *
 * 在程序中，通常会有多个列需要不同的处理逻辑，每个列都会对应一个 ColumnOperationConfig 实例。
 * 该类中包含两个主要元素：
 * 1. columnIndex：表示要处理的列的索引（从0开始计数）。
 * 2. operations：一个 OperationConfig 的列表，每个 OperationConfig 描述一种特定的列处理操作（如加密、截取、正则替换等）
 *    以及该操作所需的参数（如加密密钥、正则表达式、时间格式转换参数等）。
 *
 * 程序在解析 JSON 配置文件时，会将 JSON 中的配置信息映射为 Config 对象，而 Config 对象中的 "columns" 字段即为
 * 多个 ColumnOperationConfig 的列表。随后，程序会根据这些配置对输入文件的指定列执行相应的操作。
 *
 * 使用示例（假设在配置文件中定义）：
 * "columns": [
 *     {
 *         "columnIndex": 2,
 *         "operations": [
 *             {
 *                 "type": "REGEX_REPLACE",
 *                 "params": {
 *                     "regex": "\\d{3}-\\d{4}",
 *                     "replacement": "****-****"
 *                 }
 *             },
 *             {
 *                 "type": "ENCRYPT",
 *                 "params": {
 *                     "method": "AES",
 *                     "key": "mySecretKey12345"
 *                 }
 *             }
 *         ]
 *     }
 * ]
 *
 * 在上述示例中，columnIndex = 2表示对第3列进行处理，operations 列表中包含两个操作：
 * - 第一个操作是正则替换，将匹配正则 \\d{3}-\\d{4} 的字符串替换为 ****-****。
 * - 第二个操作是加密操作，使用AES方法和给定的密钥对列值进行加密。
 */
public class ColumnOperationConfig {
    /**
     * columnIndex 表示要处理的列的索引（从0开始计数）。
     * 例如：如果 inputDelimiter = "," 且第0列是 "Name"、第1列是 "Age"、第2列是 "Phone"，
     * columnIndex = 2 表示对 "Phone" 列执行操作。
     */
    private int columnIndex;

    /**
     * operations 是一个 OperationConfig 的列表，每个 OperationConfig 描述：
     * - type：操作类型（如 ENCRYPT、REGEX_REPLACE、SUBSTRING、TIME_CONVERT 等）。
     * - params：该操作所需的参数（如加密密钥、正则表达式、子字符串起止位置、时间格式等）。
     * 程序将在处理文件时依次对指定列的值执行这些操作。
     */
    private List<OperationConfig> operations;

    /**
     * 获取列索引
     *
     * @return 列的索引（从0开始）
     */
    public int getColumnIndex() {
        return columnIndex;
    }

    /**
     * 设置列索引
     *
     * @param columnIndex 要处理的列的索引（从0开始）
     */
    public void setColumnIndex(int columnIndex) {
        this.columnIndex = columnIndex;
    }

    /**
     * 获取此列要执行的操作列表
     *
     * @return 包含多个 OperationConfig 的列表
     */
    public List<OperationConfig> getOperations() {
        return operations;
    }

    /**
     * 设置此列要执行的操作列表
     *
     * @param operations 一个 OperationConfig 的列表，每个 OperationConfig 描述一种具体的操作及其参数
     */
    public void setOperations(List<OperationConfig> operations) {
        this.operations = operations;
    }

    /**
     * 返回 ColumnOperationConfig 对象的字符串表示形式，包含列索引和操作列表信息。
     *
     * @return 包含 columnIndex 和 operations 信息的字符串
     */
    @Override
    public String toString() {
        return "ColumnOperationConfig{" +
                "columnIndex=" + columnIndex +
                ", operations=" + operations +
                '}';
    }
}
