package com.demo.annotation.springDI.scope;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
//当一个Spring Bean被声明为prototype（原型模式）时，在每次需要使用到该类的时候，Spring IoC容器都会初始化一个新的改类的实例。需要注意的是，由于每个请求都会创建一个新的实例，因此@Scope("prototype")注解可能会导致内存消耗过多的问题。
// 如果应用程序需要创建大量的对象，请考虑使用@Scope("singleton"),这样Spring只会创建一个对象并在以后的请求中重复使用它
// 在定义一个Bean时，可以设置Bean的scope属性为prototype：scope=“prototype”,也可以使用@Scope注解设置，如@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class UserImpl implements IUser {
}
