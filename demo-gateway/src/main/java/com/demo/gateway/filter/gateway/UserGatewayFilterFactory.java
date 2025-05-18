package com.demo.gateway.filter.gateway;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

/**
 * @author owen
 * @date 2025/4/25 3:55
 * @description
 */
@Component
public class UserGatewayFilterFactory extends AbstractGatewayFilterFactory<UserGatewayFilterFactory.UserConfig> {

    /**
     * 需要像父级告诉当前factory的配置类是哪一个
     */
    public UserGatewayFilterFactory() {
        super(UserGatewayFilterFactory.UserConfig.class);
    }

    @Override
    public GatewayFilter apply(UserConfig config) {
        System.out.println("user的apply方法");
        //写法1
//        return new GatewayFilter() {
//            /**
//             * 也可以通过匿名内部类实现
//             * @param exchange
//             * @param chain
//             * @return
//             */
//            @Override
//            public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//                System.out.println("MyAddRequestHeaderGatewayFilterFactory.apply is run...");
//                //exchange.getRequest().mutate() //目的是转化为装饰类，否则request为只读的，不能操作
//                //header方法用来设置header的值
//                ServerHttpRequest request = exchange.getRequest().mutate().header(config.getName(),config.getValue()).build();
//                //将request包裹继续向下传递
//                return chain.filter(exchange.mutate().request(request).build());
//            }

        return (exchange, chain) -> {
            System.out.println("MyAddRequestHeaderGatewayFilterFactory.apply is run...");
            //exchange.getRequest().mutate() //目的是转化为装饰类，否则request为只读的，不能操作
            //header方法用来设置header的值
            ServerHttpRequest request=exchange.getRequest().mutate().header(config.getName(),config.getAge()).build();
            ServerWebExchange build = exchange.mutate().request(request).build();
            return chain.filter(build);
            //response可以直接写
//            exchange.getResponse().getHeaders().set(config.getName(),config.getValue());
//            return chain.filter(exchange);
        };

        //只处理response可以这么写
//        return (exchange, chain) -> {
//            return chain.filter(exchange).then(Mono.fromRunnable(()-> {
//                System.out.println("PostLogGatewayFilterFactory is run...");
//            }));
//        };

    }


    /**
     * 所有当前传进来的参数
     */
    public static class UserConfig{
        private String name;
        private String age;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }
    }

}
