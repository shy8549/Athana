package com.nsn.bighead.glassfish.filterse.adapter;

import com.nokia.sai.stream.access.entity.QueueEntity;
import com.nokia.sai.stream.core.exception.AppException;
import com.nokia.sai.stream.core.framework.unit.ItemUnit;
import com.nokia.sai.stream.core.interfaces.AppLogger;
import com.nokia.sai.stream.core.logger.AppLoggerFactory;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: create by suhy
 * @version: v1.0
 * @description: HljSplitUnit
 * @className: HljSplitUnit
 * @date:2022/9/5 17:17
 */
public class HljSplitUnit extends ItemUnit {
    private static final long serialVersionUID = 1L;
    private static final AppLogger logger = AppLoggerFactory.getLogger(HljSplitUnit.class);
    private static final String SEPARATOR = "|";

    @Override
    public Object execute(Object data) {
        QueueEntity queueEntity = (QueueEntity)data;
        try
        {
            String inputLine = queueEntity.getData();
            if ((inputLine.contains("NIL")) || (inputLine.contains("nil")) || (inputLine.contains("null")))
            {
                inputLine = inputLine.replace("NIL", "").replace("nil", "").replace("null", "");
                queueEntity.setData(inputLine);
            }
            String[] line = StringUtils.splitByWholeSeparatorPreserveAllTokens(inputLine, "|");

            List<String> list = new ArrayList<>();
            list.add("VoLTE_SIP_Gm");
            list.add("VoLTE_SIP_Mw");
            list.add("VoLTE_SIP_Mg");
            list.add("VoLTE_SIP_Mi");
            list.add("VoLTE_SIP_Mj");
            list.add("VoLTE_SIP_ISC");
            list.add("VoLTE_Sv");
            list.add("VoLTE_Cx");
            list.add("VoLTE_Dx");
            list.add("VoLTE_Sh");
            list.add("VoLTE_Dh");
            list.add("VoLTE_Zh");
            list.add("VoLTE_Gx");
            list.add("VoLTE_Rx");
            list.add("VoLTE_I2");
            list.add("VoLTE_Nc");
            list.add("VoLTE_ATCF_SCCAS");
            list.add("VoLTE_Ut");
            list.add("VoLTE_GX_RX_SLICE");

            String cuServiceName = queueEntity.getServiceName();
            if (list.contains(cuServiceName))
            {
                int xdr = Integer.parseInt(line[6]);
                switch (xdr)
                {
                    case 13:
                        queueEntity.setServiceName("VoLTE_SIP_Gm");
                        break;
                    case 14:
                        queueEntity.setServiceName("VoLTE_SIP_Mw");
                        break;
                    case 15:
                        queueEntity.setServiceName("VoLTE_SIP_Mg");
                        break;
                    case 16:
                        queueEntity.setServiceName("VoLTE_SIP_Mi");
                        break;
                    case 17:
                        queueEntity.setServiceName("VoLTE_SIP_Mj");
                        break;
                    case 18:
                        queueEntity.setServiceName("VoLTE_SIP_ISC");
                        break;
                    case 19:
                        queueEntity.setServiceName("VoLTE_Sv");
                        break;
                    case 20:
                        queueEntity.setServiceName("VoLTE_Cx");
                        break;
                    case 21:
                        queueEntity.setServiceName("VoLTE_Dx");
                        break;
                    case 22:
                        queueEntity.setServiceName("VoLTE_Sh");
                        break;
                    case 23:
                        queueEntity.setServiceName("VoLTE_Dh");
                        break;
                    case 24:
                        queueEntity.setServiceName("VoLTE_Zh");
                        break;
                    case 25:
                        queueEntity.setServiceName("VoLTE_Gx");
                        break;
                    case 26:
                        queueEntity.setServiceName("VoLTE_Rx");
                        break;
                    case 27:
                        queueEntity.setServiceName("VoLTE_I2");
                        break;
                    case 28:
                        queueEntity.setServiceName("VoLTE_Nc");
                        break;
                    case 29:
                        queueEntity.setServiceName("VoLTE_ATCF_SCCAS");
                        break;
                    case 30:
                        queueEntity.setServiceName("VoLTE_Ut");
                        break;
                    case 32:
                        queueEntity.setServiceName("VoLTE_GX_RX_SLICE");
                        break;
                    case 31:
                    default:
                        return queueEntity;
                }
            }
            else
            {
                return queueEntity;
            }
        }
        catch (Exception e)
        {
            logger.error("HljSplitUnit excute error ! ", e);
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
