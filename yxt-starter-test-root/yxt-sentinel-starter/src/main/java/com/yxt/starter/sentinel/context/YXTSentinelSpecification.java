package com.yxt.starter.sentinel.context;

import java.util.Arrays;
import java.util.Objects;
import org.springframework.cloud.context.named.NamedContextFactory;

/**
 * @Author liqiang
 * @Date 2024/8/28 11:36
 */
public class YXTSentinelSpecification implements NamedContextFactory.Specification {

    private String name;

    private Class<?>[] configuration;

    public YXTSentinelSpecification() {
    }

    public YXTSentinelSpecification(String name, Class<?>[] configuration) {
        this.name = name;
        this.configuration = configuration;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Class<?>[] getConfiguration() {
        return this.configuration;
    }

    public void setConfiguration(Class<?>[] configuration) {
        this.configuration = configuration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        YXTSentinelSpecification that = (YXTSentinelSpecification) o;
        return Objects.equals(this.name, that.name)
            && Arrays.equals(this.configuration, that.configuration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name, Arrays.hashCode(this.configuration));
    }

    @Override
    public String toString() {
        return "YXTSentinelSpecification{" + "name='"
            + this.name + "', " + "configuration="
            + Arrays.toString(this.configuration) + "}";
    }

}
