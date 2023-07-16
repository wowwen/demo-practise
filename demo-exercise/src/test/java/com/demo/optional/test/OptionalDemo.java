package com.demo.optional.test;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.Test;
import org.junit.platform.commons.util.StringUtils;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;
import java.util.function.Supplier;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = OptionalDemo.class)
public class OptionalDemo {

    @Test
    public void testOptional() throws Exception {
        //工厂方法：创建一个包装对象为空的Optional对象
        Optional<Object> empty = Optional.empty();
        //工厂方法：创建包装对象值非空的Optional对象
        Optional<Person> zhangsan = Optional.of(Person.builder().name("zhangsan").age(18).build());
        //工厂方法：创建包装对象值允许为空也可以不为空的Optional对象
        Optional<String> o = Optional.ofNullable(null);
        Optional<Person> person = Optional.ofNullable(Person.builder().build());
        Optional<Person> lisi = Optional.ofNullable(Person.builder().name("lisi").age(20).build());

        //true:创建的对象不为空，只是对象的属性值为空
        System.out.println(person.isPresent());

        //如果value值不为空，则返回，否则抛NoSuchElementException("No value present")
        Person 王五 = Optional.ofNullable(Person.builder().name("王五").build()).get();

        //Optional.isPresent()判断是否为空
        if (Optional.ofNullable(Person.builder().name("赵六").build()).isPresent()){
            System.out.println("不为空");
        }else{
            System.out.println("为空");
        }

        //Optional.ifPresent()方法(判读是否为空并返回函数),这个意思是如果对象非空，则运行函数体
        Optional.ofNullable(Person.builder().name("王二麻子").build()).ifPresent(p -> System.out.println(p.getName()));

        //Optional.filter()方法(过滤对象):filter()方法大致意思是，接受一个对象，然后对他进行条件过滤，
        // 如果条件符合则返回Optional对象本身，如果不符合则返回空Optional
        Optional<Person> person1 = Optional.ofNullable(Person.builder().age(18).build()).filter(p -> p.getAge() > 20);
        System.out.println(person1.isPresent());

        //map()方法将对应Funcation函数式接口中的对象，进行二次运算，封装成新的对象然后返回在Optional中
        Optional<String> name = Optional.ofNullable(Person.builder().name("李大傻子").build()).map(p -> p.getName());
        if (name.isPresent()){
            System.out.println(name.get());
        }

        //flatMap()方法将对应Optional<Funcation>函数式接口中的对象，进行二次运算，封装成新的对象然后返回在Optional中,和map()方法有点类似，但是flatMap()的
        // 参数是Optional对象
        Optional<String> aaa = Optional.ofNullable(Person.builder().name("aaa").build()).flatMap(p -> Optional.ofNullable(p.getName()));
        if (aaa.isPresent()){
            System.out.println(aaa.get());
        }

        //常用方法之一，这个方法意思是如果包装对象为空的话，就执行orElse方法里的value，如果非空，则返回写入对象 源码:
        Person bbb = Optional.ofNullable(Person.builder().build()).orElse(Person.builder().name("bbb").build());
        System.out.println("orElse()" + bbb.getName());
        String sss = Optional.ofNullable(Person.builder().name("前面的").build().getName()).orElse("");
        System.out.println("sss:" + sss);//输出“前面的”
        Object o1 = Optional.ofNullable(null).orElse("空");
        System.out.println("o1:" + o1);//输出“空”


        //注意此处需要在Optional的参数中指定Supplier<T>才能使用Person::new,否则会报不是一个函数式接口，同时Person还要加上无参构造
        Optional<Supplier<Person>> supplierOpt = Optional.ofNullable(Person::new);
        //调用supplier.get()方法，此时才会调用对象的构造方法，即获得到真正对象.如果前面的ofNullable()中的对象不为空，则返回前面的对象；
        //如果前面的对象为空，则返回orElseGet()中的对象的get方法获取到的对象
        //注意，supplierOpt.get()是从Optional<Supplier<Person>>中获取Supplier对象
        //说真的对于Supplier对象我也懵逼了一下，去网上简单查阅才得知 Supplier也是创建对象的一种方式,
        // 简单来说，Suppiler是一个接口，是类似Spring的懒加载，声明之后并不会占用内存，只有执行了get()方法之后，才会调用构造方法创建出对象
        // 创建对象的语法的话就是Supplier<Person> supPerson= Person::new; 需要使用时supPerson.get()即可
        //如果计算备选值在计算上太过繁琐，即可使用 orElseGet 方法。该方法接受一个Supplier 对象，只有在 Optional 对象真正为空时才会调用。
        Person person2 = Optional.ofNullable(Person.builder().build()).orElseGet(supplierOpt.get());

        //Optional.orElseThrow()方法(为空返回异常)
        Person queryPerson = new PersonService().queryperson();
        Optional.ofNullable(queryPerson).orElseThrow(() -> new Exception());


        //异同点
        /**
         * orElse()和orElseGet()还有orElseThrow()很相似,
         * 方法效果类似，如果对象不为空，则返回对象，如果为空，则返回方法体中的对应参数，
         * 所以可以看出这三个方法体中参数是不一样的 orElse（T 对象） orElseGet（Supplier < T >对象） orElseThrow（异常）
         */
        /**
         * map()与flatMap（）方法的异同点
         * 方法效果类似，对方法参数进行二次包装，并返回,入参不同 map（function函数） flatmap（Optional< function >函数）
         */

        //实战场景
        //场景1
        //查询一个对象
        Person queryPerson1 = new PersonService().queryperson();
        Optional.ofNullable(queryPerson1).orElseThrow(() -> new Exception());
        //场景2：我们可以在dao接口层中定义返回值时就加上Optional 例如：我使用的是jpa，其他也同理
        /*public interface LocationRepository extends JpaRepository<Location, String> {
            Optional<Location> findLocationById(String id);
        }
        //然在是Service中
        public TerminalVO findById(String id) {
            //这个方法在dao层也是用了Optional包装了
            Optional<Terminal> terminalOptional = terminalRepository.findById(id);
            //直接使用isPresent()判断是否为空
            if (terminalOptional.isPresent()) {
                //使用get()方法获取对象值
                Terminal terminal = terminalOptional.get();
                //在实战中，我们已经免去了用set去赋值的繁琐，直接用BeanCopy去赋值
                TerminalVO terminalVO = BeanCopyUtils.copyBean(terminal, TerminalVO.class);
                //调用dao层方法返回包装后的对象
                Optional<Location> location = locationRepository.findLocationById(terminal.getLocationId());
                if (location.isPresent()) {
                    terminalVO.setFullName(location.get().getFullName());
                }
                return terminalVO;
            }
            //不要忘记抛出异常
            throw new ServiceException("该终端不存在");
        }*/

        //Optional使用注意事项
        //Optional真么好用，真的可以完全替代if判断吗？我想这肯定是大家使用完之后Optional之后可能会产生的想法，答案是否定的 举一个最简单的栗子：
        //例子1：如果我只想判断对象的某一个变量是否为空并且做出判断呢？
        Person person11=new Person();
        person11.setName("");
        person11.setAge(2);
        //普通判断
        if(StringUtils.isNotBlank(person11.getName())){
            //名称不为空执行代码块
        }
        //使用Optional做判断
        Optional.ofNullable(person11).map(p -> p.getName()).orElse("name为空");
        //我觉得这个例子就能很好的说明这个问题，只是一个很简单判断，如果用了Optional我们还需要考虑包装值，考虑代码书写，考虑方法调用，虽然只有一行，但是可读性并不好，如果别的程序员去读，我觉得肯定没有if看的明显。

        //Jdk 9对Optional优化
        /*
        首先增加了三个方法: or()、ifPresentOrElse() 和 stream()。
        or() 与orElse等方法相似，如果对象不为空返回对象，如果为空则返回or()方法中预设的值。
        ifPresentOrElse() 方法有两个参数：一个 Consumer 和一个 Runnable。如果对象不为空，会执行 Consumer 的动作，否则运行 Runnable。
        相比ifPresent（）多了OrElse判断。
        stream()将Optional转换成stream，如果有值就返回包含值的stream，如果没值，就返回空的stream。
        */
    }


    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Person{
        private String name;
        private Integer age;
    }

    public class PersonService{
        public Person queryperson(){
            return Person.builder().name("robot").age(1).build();
        }
    }



    
}
