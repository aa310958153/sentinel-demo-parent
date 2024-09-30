package com.yxt.starter.sentinel;

import com.yxt.starter.sentinel.constants.YxtSentinelConstants;
import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Author: user
 * @Date: 2024/9/29 11:08
 * @Description: YxtSentinel配置
 */
@ConfigurationProperties(prefix = YxtSentinelConstants.PROPERTY_PREFIX)
public class YxtSentinelProperties {

    private Auth auth;

    public Auth getAuth() {
        return auth;
    }

    public void setAuth(Auth auth) {
        this.auth = auth;
    }

    public static class Auth {

        private List<String> tagList;
        private List<String> userList;

        public List<String> getTagList() {
            return tagList;
        }

        public void setTagList(List<String> tagList) {
            this.tagList = tagList;
        }

        public List<String> getUserList() {
            return userList;
        }

        public void setUserList(List<String> userList) {
            this.userList = userList;
        }
    }
}
