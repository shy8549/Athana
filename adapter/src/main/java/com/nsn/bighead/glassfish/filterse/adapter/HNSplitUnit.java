package com.nsn.bighead.glassfish.filterse.adapter;

import com.nokia.sai.stream.access.entity.QueueEntity;
import com.nokia.sai.stream.core.exception.AppException;
import com.nokia.sai.stream.core.framework.unit.ItemUnit;
import com.nokia.sai.stream.core.interfaces.AppLogger;
import com.nokia.sai.stream.core.logger.AppLoggerFactory;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
        使用说明：
        1、元数据正常配置，数据进入后会自动根据逻辑进行业务拆分
        2、在 config/topology/define/unit_define.xml 中注册新的unit
        3、将新增的unit 在  component/accessDataProcessComponent.xml 引入
        */

public class HNSplitUnit extends ItemUnit {
    private static final long serialVersionUID = 1L;
    private static final AppLogger logger = AppLoggerFactory.getLogger(HNSplitUnit.class);
    private static final String SEPARATOR = "|";

    /** VoLTE 业务集合（用于判断是否需要映射） */
    private static final Set<String> VOLTE_SET = new HashSet<>(Arrays.asList(
            "O_ODS_PM_BIZ_USER_XDR_VOLTE_SIP_GM_V220_5MI", "O_ODS_PM_BIZ_USER_XDR_VOLTE_SIP_MW_V220_5MI",
            "O_ODS_PM_BIZ_USER_XDR_VOLTE_SIP_MG_V220_5MI", "O_ODS_PM_BIZ_USER_XDR_VOLTE_SIP_MJ_V220_5MI",
            "O_ODS_PM_BIZ_USER_XDR_VOLTE_SIP_ISC_V220_5MI", "O_ODS_PM_BIZ_USER_XDR_VOLTE_SIP_I2_V220_5MI",
            "O_ODS_PM_BIZ_USER_XDR_VOLTE_SIP_MX_V220_5MI", "O_ODS_PM_BIZ_USER_XDR_VOLTE_SIP_ATCF_SCCAS_V220_5MI",
            "O_ODS_PM_BIZ_USER_XDR_VOLTE_CX_V220_5MI", "O_ODS_PM_BIZ_USER_XDR_VOLTE_SH_V220_5MI",
            "O_ODS_PM_BIZ_USER_XDR_5G_5GMC_E_5MI", "O_ODS_PM_BIZ_USER_XDR_5G_5GMC_C_5MI",
            "O_ODS_PM_BIZ_USER_XDR_VOLTE_SDP_SESSION_GM_5MI", "O_ODS_PM_BIZ_USER_XDR_VOLTE_SDP_SESSION_SBC_SCSCF_5MI",
            "O_ODS_PM_BIZ_USER_XDR_5G_NEW_CALL_HTTP_3_5MI", "O_ODS_PM_BIZ_USER_XDR_5G_NEW_CALL_HTTP_5_5MI",
            "O_ODS_PM_BIZ_USER_XDR_5G_NEW_CALL_HTTP_7_5MI", "O_ODS_PM_BIZ_USER_XDR_5G_NEW_CALL_HTTP_9_5MI",
            "O_ODS_PM_BIZ_USER_XDR_5G_5GMC_SIP_5GM_01_5MI", "O_ODS_PM_BIZ_USER_XDR_5G_5GMC_SIP_NNI_01_5MI",
            "O_ODS_PM_BIZ_USER_XDR_5G_5GMC_SIP_CP_01_5MI"
    ));

    /** XDR → ServiceName 映射 */
    private static final Map<Integer, String> XDR_MAP = new HashMap<Integer, String>() {{
        put(91, "O_ODS_PM_BIZ_USER_XDR_VOLTE_SIP_GM_V220_5MI");
        put(92, "O_ODS_PM_BIZ_USER_XDR_VOLTE_SIP_MW_V220_5MI");
        put(93, "O_ODS_PM_BIZ_USER_XDR_VOLTE_SIP_MG_V220_5MI");
        put(95, "O_ODS_PM_BIZ_USER_XDR_VOLTE_SIP_MJ_V220_5MI");
        put(96, "O_ODS_PM_BIZ_USER_XDR_VOLTE_SIP_ISC_V220_5MI");
        put(105, "O_ODS_PM_BIZ_USER_XDR_VOLTE_SIP_I2_V220_5MI");
        put(192, "O_ODS_PM_BIZ_USER_XDR_VOLTE_SIP_MX_V220_5MI");
        put(107, "O_ODS_PM_BIZ_USER_XDR_VOLTE_SIP_ATCF_SCCAS_V220_5MI");
        put(98, "O_ODS_PM_BIZ_USER_XDR_VOLTE_CX_V220_5MI");
        put(100, "O_ODS_PM_BIZ_USER_XDR_VOLTE_SH_V220_5MI");
        put(147, "O_ODS_PM_BIZ_USER_XDR_5G_5GMC_E_5MI");
        put(152, "O_ODS_PM_BIZ_USER_XDR_5G_5GMC_C_5MI");
        put(187, "O_ODS_PM_BIZ_USER_XDR_VOLTE_SDP_SESSION_GM_5MI");
        put(188, "O_ODS_PM_BIZ_USER_XDR_VOLTE_SDP_SESSION_SBC_SCSCF_5MI");
        put(167, "O_ODS_PM_BIZ_USER_XDR_5G_NEW_CALL_HTTP_3_5MI");
        put(169, "O_ODS_PM_BIZ_USER_XDR_5G_NEW_CALL_HTTP_5_5MI");
        put(171, "O_ODS_PM_BIZ_USER_XDR_5G_NEW_CALL_HTTP_7_5MI");
        put(173, "O_ODS_PM_BIZ_USER_XDR_5G_NEW_CALL_HTTP_9_5MI");
        put(131, "O_ODS_PM_BIZ_USER_XDR_5G_5GMC_SIP_5GM_01_5MI");
        put(138, "O_ODS_PM_BIZ_USER_XDR_5G_5GMC_SIP_NNI_01_5MI");
        put(140, "O_ODS_PM_BIZ_USER_XDR_5G_5GMC_SIP_CP_01_5MI");
    }};

    @Override
    public Object execute(Object data) {
        QueueEntity queueEntity = (QueueEntity) data;

        try {
            String inputLine = queueEntity.getData();

            // 1. 清理 NIL/null（忽略大小写）
            inputLine = inputLine.replaceAll("(?i)null|nil", "");
            queueEntity.setData(inputLine);

            // 2. 分割
            String[] line = StringUtils.splitByWholeSeparatorPreserveAllTokens(inputLine, SEPARATOR);

            // 3. 长度检查（避免数组越界）
            if (line.length <= 6) {
                logger.error("HNSplitUnit: line length < 7, cannot get XDR field: " + inputLine);
                return queueEntity;
            }

            // 4. 只对 VoLTE 业务执行映射
            String currentService = queueEntity.getServiceName();
            if (!VOLTE_SET.contains(currentService)) {
                return queueEntity;
            }

            // 5. XDR 类型解析
            int xdr;
            try {
                xdr = Integer.parseInt(line[6]);
            } catch (NumberFormatException e) {
                logger.error("HNSplitUnit: invalid XDR field: " + line[6]);
                return queueEntity;
            }

            // 6. XDR → ServiceName 映射
            String mappedService = XDR_MAP.get(xdr);
            if (mappedService != null) {
                queueEntity.setServiceName(mappedService);
            }

        } catch (Exception e) {
            logger.error("HNSplitUnit execute error!", e);
        }

        return queueEntity;
    }

    @Override
    public void init() throws AppException {}

    @Override
    public void prepare() throws AppException {}
}
