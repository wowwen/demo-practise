package com.demo.gateway.predicate;

import org.springframework.cloud.gateway.handler.predicate.AbstractRoutePredicateFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.ServerWebExchange;

import javax.validation.constraints.NotEmpty;
import java.util.function.Predicate;

/**
 * @author owen
 * @date 2025/4/25 3:04
 * @description 自定义谓词
 */
@Component
public class UserNameCheckRoutePredicateFactory extends AbstractRoutePredicateFactory<UserNameCheckRoutePredicateFactory.Config> {

    public UserNameCheckRoutePredicateFactory() {
        super(Config.class);
    }


    @Override
    public Predicate<ServerWebExchange> apply(Config config) {
        return new Predicate<ServerWebExchange>() {
            @Override
            public boolean test(ServerWebExchange exchange) {
                String userName = exchange.getRequest().getQueryParams().getFirst("userName");
                if(!StringUtils.hasText(userName)){
                    return false;
                }
                //检查请求参数中userName是否与配置的数据相同，如果相同则允许访问，否则不允许访问
                if(userName.equals(config.getName())){
                    return true;
                }
                return false;
            }
        };
    }

    @Validated
    public static class Config{
        @NotEmpty
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
