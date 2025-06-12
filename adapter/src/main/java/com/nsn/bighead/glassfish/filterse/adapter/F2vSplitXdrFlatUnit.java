package com.nsn.bighead.glassfish.filterse.adapter;

import org.apache.commons.lang3.StringUtils;

import com.nokia.sai.stream.access.entity.QueueEntity;
import com.nokia.sai.stream.core.exception.AppException;
import com.nokia.sai.stream.core.framework.unit.ItemUnit;
import com.nokia.sai.stream.core.interfaces.AppLogger;
import com.nokia.sai.stream.core.logger.AppLoggerFactory;

/**
 *  需求：模型：a|b|c|v_length|v11||v12|v21|v22|..|vx1|vx2|d|e，根据v_length确定业务长度，模型定长为2
 *       比如：a|b|c|1|v11|v12|v21|v22|d|e,输出为：a|b|c|1|v11|v12|v21|v22|d|e
 *       比如：a|b|c|3|v11|v12|v21|v22|d|e,输出为：a|b|c|1|v11|v12|v21|v22|||d|e
 *  使用说明
 *  1、继承ItemUnit
 *  2、创建queueEntity对象
 *  3、获取数据输入行
 *  4、针对每行第四字段进行业务区分
 *  5、区分业务后进行业务处理
 */
public class F2vSplitXdrFlatUnit  extends ItemUnit{
    private static final AppLogger logger = AppLoggerFactory.getLogger(F2vSplitXdrFlatUnit.class);
    private static final String SEPARATOR = "|";


    @Override
    public Object execute(Object data) {

        QueueEntity queueEntity = (QueueEntity) data;

        try{
            String inputLine = queueEntity.getData();
            String[] line = StringUtils.splitByWholeSeparatorPreserveAllTokens(inputLine, SEPARATOR);


            int fixlength = Integer.parseInt(line[2]);
            String splitServerName ;

            switch (fixlength) {
                case 2:
                    splitServerName = "ZX_MDT_IMM_MM";
                    break;
                case 3:
                    splitServerName = "ZX_MDT_IMM_MM_1";
                    break;
                default:
                    return null;
            }

            if (fixlength==2) {

                int limit = line.length;
                if (limit >= 10) {
                    String[] ericFinalLine = new String[10];
                    System.arraycopy(line, 0, ericFinalLine, 0, 3);
                    System.arraycopy(line, 3, ericFinalLine, 3, 4);
                    System.arraycopy(line, limit - 3, ericFinalLine, 7, 3);

                    StringBuilder sb = new StringBuilder();

                    for (int i = 0; i < 10; i++) {
                        for (String attr : ericFinalLine) {
                            if (StringUtils.isNotBlank(attr)){
                                if (i == 9) {
                                    sb.append(attr);
                                    i++;
                                } else {
                                    sb.append(attr);
                                    sb.append(SEPARATOR);
                                    i++;
                                }
                            } else {
                                if (i == 9) {
                                    i++;
                                } else {
                                    sb.append(SEPARATOR);
                                    i++;
                                }
                            }
                        }
                    }
                    queueEntity.setData(sb.toString());
                    queueEntity.setDataSplit(ericFinalLine);
                    queueEntity.setServiceName(splitServerName);

                } else {
                    String[] ericFinalLine = new String[10];
                    System.arraycopy(line, 0, ericFinalLine, 0, 3);
                    System.arraycopy(line, 3, ericFinalLine, 3, limit - 6);
                    System.arraycopy(line, limit - 3, ericFinalLine, 7, 3);

                    StringBuilder sb = new StringBuilder();

                    for (int i = 0; i < 10; i++) {
                        for (String attr : ericFinalLine) {
                            if (StringUtils.isNotBlank(attr)) {
                                if (i == 9) {
                                    sb.append(attr);
                                    i++;
                                } else {
                                    sb.append(attr);
                                    sb.append(SEPARATOR);
                                    i++;
                                }
                            } else {
                                if (i == 9) {
                                    i++;
                                } else {
                                    sb.append(SEPARATOR);
                                    i++;
                                }

                            }

                        }
                    }
                    queueEntity.setData(sb.toString());
                    queueEntity.setDataSplit(ericFinalLine);
                    queueEntity.setServiceName(splitServerName);
                }
            }else {

                int limit = line.length;
                if (limit>=12) {
                    String[] ericFinalLine = new String[12];
                    System.arraycopy(line, 0, ericFinalLine, 0, 3);
                    System.arraycopy(line, 3, ericFinalLine, 3, 9);
                    System.arraycopy(line, limit - 3, ericFinalLine, 9, 3);

                    StringBuilder sb = new StringBuilder();

                    for (int i = 0; i < 12; i++) {
                        for (String attr : ericFinalLine) {
                            if (StringUtils.isNotBlank(attr)){
                                if (i == 11) {
                                    sb.append(attr);
                                    i++;
                                } else {
                                    sb.append(attr);
                                    sb.append(SEPARATOR);
                                    i++;
                                }
                            } else {
                                if (i == 11) {
                                    i++;
                                } else {
                                    sb.append(SEPARATOR);
                                    i++;
                                }
                            }
                        }
                    }
                    queueEntity.setData(sb.toString());
                    queueEntity.setDataSplit(ericFinalLine);
                    queueEntity.setServiceName(splitServerName);
                } else {
                    String[] ericFinalLine = new String[12];
                    System.arraycopy(line, 0, ericFinalLine, 0, 3);
                    System.arraycopy(line, 3, ericFinalLine, 3, limit - 6);
                    System.arraycopy(line, limit - 3, ericFinalLine, 9, 3);

                    StringBuilder sb = new StringBuilder();

                    for (int i = 0; i < 12; i++) {
                        for (String attr : ericFinalLine) {
                            if (StringUtils.isNotBlank(attr)) {
                                if (i == 11) {
                                    sb.append(attr);
                                    i++;
                                } else {
                                    sb.append(attr);
                                    sb.append(SEPARATOR);
                                    i++;
                                }
                            } else {
                                if (i == 11) {
                                    i++;
                                } else {
                                    sb.append(SEPARATOR);
                                    i++;
                                }

                            }

                        }
                    }
                    queueEntity.setData(sb.toString());
                    queueEntity.setDataSplit(ericFinalLine);
                    queueEntity.setServiceName(splitServerName);
                }
            }

        }catch (Exception e){
            logger.error("SplitXdrFlatUnit excute error ! ", e);
        }

        return queueEntity;
    }

    @Override
    public void init() throws AppException {

    }

    @Override
    public void prepare() throws AppException {

    }


}
