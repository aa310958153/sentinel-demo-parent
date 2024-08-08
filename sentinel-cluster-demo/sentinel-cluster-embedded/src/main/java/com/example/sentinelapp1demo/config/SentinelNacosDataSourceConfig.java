package com.example.sentinelapp1demo.config;

import com.alibaba.csp.sentinel.cluster.client.config.ClusterClientAssignConfig;
import com.alibaba.csp.sentinel.cluster.client.config.ClusterClientConfig;
import com.alibaba.csp.sentinel.cluster.client.config.ClusterClientConfigManager;
import com.alibaba.csp.sentinel.datasource.ReadableDataSource;
import com.alibaba.csp.sentinel.datasource.nacos.NacosDataSource;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.alibaba.csp.sentinel.util.AppNameUtil;
import com.alibaba.csp.sentinel.util.StringUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.nacos.api.PropertyKeyConst;
import org.apache.commons.codec.binary.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Properties;

@Configuration
@ConfigurationProperties(prefix = "sentinel.nacos")
public class SentinelNacosDataSourceConfig implements InitializingBean {
    private static final String APP_NAME = AppNameUtil.getAppName();
    private static final String DEFAULT_ADDRESS = "localhost:8848";
    private static final String DEFAULT_GROUP_ID = "SENTINEL_GROUP";

    private String remoteAddress = DEFAULT_ADDRESS;
    private String groupId = DEFAULT_GROUP_ID;
    private String flowRuleDataId = APP_NAME+"-flow-rules";
    private String namespace;



    private final String configDataId = APP_NAME + "-cluster-client-config";
    @Override
    public void afterPropertiesSet() throws Exception {
        if (namespace != null && !namespace.equals("")) {
            loadFlowRuleNamespaceRules();
        } else {
            loadFlowRuleMRules();
        }

        //配置客户端连接服务端的超时时间
        initClientConfigProperty();
        //注册服务端传输配置
        initClientServerAssignProperty();
    }

    /**
     * 加载流控规则
     */
    private  void loadFlowRuleMRules() {
        ReadableDataSource<String, List<FlowRule>> flowRuleDataSource = new NacosDataSource<>(remoteAddress, groupId, flowRuleDataId,
                source -> JSON.parseObject(source, new TypeReference<List<FlowRule>>() {
                }));
        FlowRuleManager.register2Property(flowRuleDataSource.getProperty());
    }
    /**
     * 加载指定命名空间的流控规则
     */
    private  void loadFlowRuleNamespaceRules() {
        Properties properties = new Properties();
        properties.put(PropertyKeyConst.SERVER_ADDR, remoteAddress);
        properties.put(PropertyKeyConst.NAMESPACE, namespace);

        ReadableDataSource<String, List<FlowRule>> flowRuleDataSource = new NacosDataSource<>(properties, groupId, flowRuleDataId,
                source -> JSON.parseObject(source, new TypeReference<List<FlowRule>>() {
                }));
        FlowRuleManager.register2Property(flowRuleDataSource.getProperty());
    }
    private void initClientConfigProperty() {
        ReadableDataSource<String, ClusterClientConfig> clientConfigDs = new NacosDataSource<>(remoteAddress, groupId,
            configDataId, source -> JSON.parseObject(source, new TypeReference<ClusterClientConfig>() {}));
        ClusterClientConfigManager.registerClientConfigProperty(clientConfigDs.getProperty());
    }
    private void initClientServerAssignProperty() {

        ReadableDataSource<String, ClusterClientAssignConfig> clientAssignDs = new NacosDataSource<>(this.buildProperties(), APP_NAME,"sentinel-cluster-server"
            , source -> JSON.parseObject(source, new TypeReference<ClusterClientAssignConfig>(){}));
        ClusterClientConfigManager.registerServerAssignProperty(clientAssignDs.getProperty());
    }
    /**
     * 该方法构造nacos的地址、命名空间、账号、密码等，因为我是匿名的，所以只需要两个地址和命名空间
     * @return
     */
    private Properties buildProperties(){
        Properties properties = new Properties();
        properties.setProperty(PropertyKeyConst.SERVER_ADDR, remoteAddress);
        if(StringUtil.isNotBlank(namespace)) {
            properties.setProperty(PropertyKeyConst.NAMESPACE, namespace);
        }

        return properties;
    }
    public String getRemoteAddress() {
        return remoteAddress;
    }

    public void setRemoteAddress(String remoteAddress) {
        this.remoteAddress = remoteAddress;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }


    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }
}
