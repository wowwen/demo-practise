package com.demo.test.spel;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.junit.jupiter.api.Test;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.SpelCompilerMode;
import org.springframework.expression.spel.SpelParserConfiguration;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.BooleanTypedValue;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.*;

/**
 * @author jiangyw
 * @date 2026/1/22 15:39
 * @description
 */
public class SpelTest {

    @Test
    public void spelTest() {
        ExpressionParser parser = new SpelExpressionParser();
        Expression exp = parser.parseExpression("'Hello World'"); //在这个例子中字符串表达式即用单引号括起来的字符串
        String message = (String) exp.getValue();
        System.out.println(message);
        //输出：Hello World
    }

    @Test
    public void spelTest1() {
        ExpressionParser parser = new SpelExpressionParser();
        Expression exp = parser.parseExpression("'Hello World'.concat('!')"); //支持方法调用
        String message = (String) exp.getValue();
        System.out.println(message);
        //输出：Hello World!
    }

    @Test
    public void spelTest2() {
        ExpressionParser parser = new SpelExpressionParser();
        // 调用'getBytes()'
        Expression exp = parser.parseExpression("'Hello World'.bytes");//访问JavaBean属性的例子，String类的Bytes属性通过以下的方法调用.
        // SpEL同时也支持级联属性调用、和标准的prop1.prop2.prop3方式是一样的；同样属性值设置也是类似的方式
        byte[] bytes = (byte[]) exp.getValue();
        System.out.println(bytes);
    }

    @Test
    public void spelTest3() {
        ExpressionParser parser = new SpelExpressionParser();
        // 调用 'getBytes().length'
        Expression exp = parser.parseExpression("'Hello World'.bytes.length"); //公共方法也可以被访问到
        int length = (Integer) exp.getValue();
        System.out.println(length);
    }

    @Test
    public void spelTest4() {
        ExpressionParser parser = new SpelExpressionParser();
        Expression exp = parser.parseExpression("new String('hello world').toUpperCase()");//除了使用字符串表达式、也可以调用String的构造函数
        String message = exp.getValue(String.class);
        System.out.println(message);
    }

    @Test
    public void spelTest5() {
        // 创建并设置一个calendar实例
        GregorianCalendar c = new GregorianCalendar();
        c.set(1856, 7, 9);
        // 构造器参数有： name, birthday, and nationality.
        Inventor tesla = new Inventor("Nikola Tesla", c.getTime(), "Serbian");
        ExpressionParser parser = new SpelExpressionParser();
        Expression exp = parser.parseExpression("name");
        EvaluationContext context = new StandardEvaluationContext(tesla);//通过StandardEvaluationContext类你能指定哪个对象的”name
        // ”会被求值。
        // 这种机制用于当根对象不会被改变的场景，在求值上下文中只会被设置一次。
        String name = (String) exp.getValue(context);
        System.out.println(name);
    }

    @Test
    public void spelTest6() {
        // 创建并设置一个calendar实例
        GregorianCalendar c = new GregorianCalendar();
        c.set(1856, 7, 9);
        // 构造器参数有： name, birthday, and nationality.
        Inventor tesla = new Inventor("Nikola Tesla", c.getTime(), "Serbian");
        ExpressionParser parser = new SpelExpressionParser();
        Expression exp = parser.parseExpression("name");
        String name = (String) exp.getValue(tesla);
        System.out.println(name);
    }

    @Test
    public void spelTest7() {
        // 创建并设置一个calendar实例
        GregorianCalendar c = new GregorianCalendar();
        c.set(1856, 7, 9);
        // 构造器参数有： name, birthday, and nationality.
        Inventor tesla = new Inventor("Nikola Tesla", c.getTime(), "Serbian");
        ExpressionParser parser = new SpelExpressionParser();
        EvaluationContext context = new StandardEvaluationContext(tesla);//通过StandardEvaluationContext类你能指定哪个对象的”name
        // ”会被求值。
        Expression exp = parser.parseExpression("name == 'Nikola Tesla'");
        boolean result = exp.getValue(context, Boolean.class); // 求值结果是True
        System.out.println(result); //输出true
    }

    @Test
    public void spelTest8() {
        ExpressionParser parser = new SpelExpressionParser();
        Simple simple = new Simple();
        simple.booleanList.add(true);
        StandardEvaluationContext simpleContext = new StandardEvaluationContext(simple);
        // false is passed in here as a string. SpEL and the conversion service will
        // correctly recognize that it needs to be a Boolean and convert it
        parser.parseExpression("booleanList[0]").setValue(simpleContext, "false");
        // b will be false
        Boolean b = simple.booleanList.get(0);
        System.out.println(b);//输出false
    }

    @Test
    public void spelTest9() {
        // Turn on:
        // - auto null reference initialization
        // - auto collection growing
        SpelParserConfiguration config = new SpelParserConfiguration(true, true);
        ExpressionParser parser = new SpelExpressionParser(config);
        Expression expression = parser.parseExpression("list[3]");
        Demo demo = new Demo();
        Object o = expression.getValue(demo);
        // demo.list will now be a real collection of 4 entries
        // Each entry is a new empty String
        System.out.println(o);//将打印出空
    }

    @Test
    public void spelTest10() {
        ExpressionParser parser = new SpelExpressionParser();
        // evals to "Hello World"
        String helloWorld = (String) parser.parseExpression("'Hello World'").getValue();
        //6.0221415E23
        double avogadrosNumber = (Double) parser.parseExpression("6.0221415E+23").getValue();
        // evals to 2147483647
        int maxValue = (Integer) parser.parseExpression("0x7FFFFFFF").getValue();
        boolean trueValue = (Boolean) parser.parseExpression("true").getValue();
        Object nullValue = parser.parseExpression("null").getValue();
        System.out.println(nullValue);
    }

    @Test
    public void spelTest11() {
        // 创建并设置一个calendar实例
        GregorianCalendar c = new GregorianCalendar();
        c.set(1856, 7, 9);
        Inventor tesla = new Inventor("Nikola Tesla", c.getTime(), "Serbian");
        tesla.setPlaceOfBirth(new PlaceOfBirth("北京"));
        EvaluationContext context = new StandardEvaluationContext(tesla);
        ExpressionParser parser = new SpelExpressionParser();
        // evals to 1856
        int year = (Integer) parser.parseExpression("Birthdate.Year + 1900").getValue(context);
        String city = (String) parser.parseExpression("placeOfBirth.City").getValue(context);
        System.out.println(city);
    }

    /**
     * 另一种取属性得方式如下，注意，上面一个是直接通过构造函数设置得rootObject，下面是设置得Map<String, Object> variables
     */
    @Test
    public void spelTest12() {
        // 创建并设置一个calendar实例
        GregorianCalendar c = new GregorianCalendar();
        c.set(1856, 7, 9);
        Inventor tesla = new Inventor("Nikola Tesla", c.getTime(), "Serbian");
        tesla.setPlaceOfBirth(new PlaceOfBirth("北京"));
        EvaluationContext context = new StandardEvaluationContext();
        ExpressionParser parser = new SpelExpressionParser();
        context.setVariable("inventor", tesla); //设置得是variable得话，取值就得用#号
        // evals to 1856
        int year = (Integer) parser.parseExpression("#inventor.getBirthdate().getYear() + 1900").getValue(context);
        int year1 = (Integer) parser.parseExpression("#inventor.Birthdate.Year + 1900").getValue(context);
        String city = (String) parser.parseExpression("#inventor.getPlaceOfBirth.getCity").getValue(context);
        System.out.println(city);
    }


    @Test
    public void spelTest13() {
        // 创建并设置一个calendar实例
        GregorianCalendar c = new GregorianCalendar();
        c.set(1856, 7, 9);
        Inventor tesla = new Inventor("Nikola Tesla", c.getTime(), "Serbian");
        tesla.setInventions(new String[]{"AAA", "BBB", "CCC", "DDD", "EEE", "FFF", "Wireless communication"});

        Society society = new Society();
        society.setMembers(Lists.newArrayList(tesla));

        ExpressionParser parser = new SpelExpressionParser();
        // Inventions Array
        StandardEvaluationContext teslaContext = new StandardEvaluationContext(tesla);
        // evaluates to "DDD"
        String invention = parser.parseExpression("inventions[3]").getValue(teslaContext, String.class);
        // Members List
        StandardEvaluationContext societyContext = new StandardEvaluationContext(society);
        // evaluates to "Nikola Tesla"
        String name = parser.parseExpression("Members[0].Name").getValue(societyContext, String.class);
        // List and Array navigation
        // evaluates to "Wireless communication"
        String minvention = parser.parseExpression("Members[0].Inventions[6]").getValue(societyContext, String.class);
    }

    @Test
    public void spelTest14() {
        //解析器
        ExpressionParser parser = new SpelExpressionParser();
        EvaluationContext context = new StandardEvaluationContext();
        //创建按空数组
        int[] numbers1 = (int[]) parser.parseExpression("new int[4]").getValue(context);
        // Array with initializer
        int[] numbers2 = (int[]) parser.parseExpression("new int[]{1,2,3}").getValue(context);
        // Multi dimensional array
        int[][] numbers3 = (int[][]) parser.parseExpression("new int[4][5]").getValue(context);
        //解析一维数组
        int[] oneArray = (int[]) parser.parseExpression("new int[]{3,4,5}").getValue();
        System.out.println("一维数组开始：");
        for (int i : oneArray) {
            System.out.println(i);
        }
        System.out.println("一维数组结束");

        //在创建多维数组时还不支持事先指定初始化的值。这里会抛出 SpelParseException
        //        int[][] twoArray = (int[][]) parser.parseExpression("new int[][]{3,4,5}{3,4,5}")
        //                .getValue();

        //SpEL内联List访问
        int result1 = parser.parseExpression("{1,2,3}[0]").getValue(int.class);

        //创建list
        List numbers = (List) parser.parseExpression("{1,2,3,4,5}").getValue();
        System.out.println(numbers.get(2) + "");
        List listOfLists = (List) parser.parseExpression("{{'a','b'},{'x','y'}}").getValue();
        System.out.println(((List) listOfLists.get(1)).get(1));

        //创建Map
        Map map = (Map) parser.parseExpression("{account:'deniro',footballCount:10}").getValue();
        // evaluates to a Java map containing the two entries
        Map inventorInfo = (Map) parser.parseExpression("{name:'Nikola',dob:'10-July-1856'}").getValue(context);
        Map mapOfMaps = (Map) parser.parseExpression("{name:{first:'Nikola',last:'Tesla'},dob:{day:10,month:'July'," +
                "year:1856}}").getValue(context);
        System.out.println("map:" + map);


    }

    @Test
    public void spelTest15() {
        //解析器
        ExpressionParser parser = new SpelExpressionParser();

        //3.修改map元素值
        Map<String, Integer> map = new HashMap<String, Integer>();
        map.put("a", 1);
        EvaluationContext context3 = new StandardEvaluationContext();
        context3.setVariable("map", map);
        int result3 = parser.parseExpression("#map['a'] = 2").getValue(context3, int.class);

        Society society = new Society();
        Map<Object, Object> officers = new HashMap<>();
        Inventor president = new Inventor();
        president.setPlaceOfBirth(new PlaceOfBirth("北京", "中国"));
        officers.put("president", president);
        officers.put("advisors", Lists.newArrayList(president));
        society.setOfficers(officers);

        EvaluationContext societyContext = new StandardEvaluationContext(society);
        // Officer's Dictionary
        Inventor pupin = parser.parseExpression("Officers['president']").getValue(societyContext, Inventor.class);
        // evaluates to "Idvor"
        String city = parser.parseExpression("Officers['president'].PlaceOfBirth.City").getValue(societyContext,
                String.class);
        // setting values
        parser.parseExpression("Officers['advisors'][0].PlaceOfBirth.Country").setValue(societyContext, "Croatia");
    }

    @Test
    public void spelTest16() {
        Society society = new Society();
        Map<Object, Object> officers = new HashMap<>();
        Inventor president = new Inventor();
        president.setPlaceOfBirth(new PlaceOfBirth("北京", "中国"));
        officers.put("president", president);
        officers.put("advisors", Lists.newArrayList(president));
        society.setOfficers(officers);

        //解析器
        ExpressionParser parser = new SpelExpressionParser();
        //解析上下文
        EvaluationContext societyContext = new StandardEvaluationContext(society);

        //调用String方法
        String c = parser.parseExpression("'abc'.substring(2, 3)").getValue(String.class);
        /**
         * 调用对象相关方法
         */
        //调用公开方法
        boolean isMember = parser.parseExpression("isMember('Mihajlo Pupin')").getValue(societyContext, Boolean.class);
        //调用私有方法，抛出SpelEvalucationException:EL1004E:Method call: Method call: Method write() cannot be found on type
        // com.demo.test.spel.SpelTest$Society
//        Boolean b = parser.parseExpression("write()").getValue(societyContext, Boolean.class);

    }

    @Test
    public void spelTest17() {
        /**
         * 运算符示例
         */
        ExpressionParser parser = new SpelExpressionParser();

        // evaluates to true
        boolean trueValue = parser.parseExpression("2 == 2").getValue(Boolean.class);
        // evaluates to false
        boolean falseValue = parser.parseExpression("2 lt -5.0").getValue(Boolean.class);
        // evaluates to true
        boolean trueValue1 = parser.parseExpression("'black' lt 'block'").getValue(Boolean.class);

        // 算术运算符
        System.out.println("加法: " + parser.parseExpression("1 + 2").getValue());  // 3
        System.out.println("减法: " + parser.parseExpression("5 - 2").getValue());  // 3
        System.out.println("乘法: " + parser.parseExpression("3 * 4").getValue());  // 12
        System.out.println("除法: " + parser.parseExpression("10 / 2").getValue()); // 5
        System.out.println("取模: " + parser.parseExpression("10 % 3").getValue()); // 1
        String testString = parser.parseExpression(
                "'test' + ' ' + 'string'").getValue(String.class); // 'test string'
        // Subtraction
        int four = parser.parseExpression("1 - -3").getValue(Integer.class); // 4
        double d = parser.parseExpression("1000.00 - 1e4").getValue(Double.class); // -9000
        // Multiplication
        int six = parser.parseExpression("-2 * -3").getValue(Integer.class); // 6
        double twentyFour = parser.parseExpression("2.0 * 3e0 * 4").getValue(Double.class); // 24.0
        // Division
        int minusTwo = parser.parseExpression("6 / -3").getValue(Integer.class); // -2
        double one = parser.parseExpression("8.0 / 4e0 / 2").getValue(Double.class); // 1.0
        // Modulus
        int three = parser.parseExpression("7 % 4").getValue(Integer.class); // 3
        int one1 = parser.parseExpression("8 / 5 % 2").getValue(Integer.class); // 1
        // Operator precedence
        int minusTwentyOne = parser.parseExpression("1+2-3*8").getValue(Integer.class); // -21

        // 关系运算符
        System.out.println("等于: " + parser.parseExpression("5 == 5").getValue());   // true
        System.out.println("不等于: " + parser.parseExpression("5 != 3").getValue()); // true
        System.out.println("大于: " + parser.parseExpression("5 > 3").getValue());    // true
        System.out.println("小于: " + parser.parseExpression("3 < 5").getValue());    // true
        System.out.println("大于等于: " + parser.parseExpression("5 >= 5").getValue());  // true

        // 逻辑运算符
        System.out.println("与: " + parser.parseExpression("true and false").getValue()); // false
        System.out.println("或: " + parser.parseExpression("true or false").getValue());  // true
        System.out.println("非: " + parser.parseExpression("!true").getValue());         // false

        // 三元运算符
        System.out.println("三元: " + parser.parseExpression("5 > 3 ? 'yes' : 'no'").getValue());  // yes

        Society society = new Society();
        EvaluationContext societyContext = new StandardEvaluationContext(society);
        parser.parseExpression("Name").setValue(societyContext, "IEEE");
        societyContext.setVariable("queryName", "Nikola Tesla");
        String expression = "isMember(#queryName)? #queryName + ' is a member of the ' " + "+ Name + ' Society' : " +
                "#queryName + ' is not a member of the ' + Name + ' Society'";
        String value = parser.parseExpression(expression).getValue(societyContext, String.class);
        System.out.println(value);

        //Elvis运算符(简化三元操作符)
        String name = parser.parseExpression("name?:'Unknown'").getValue(society, String.class);
        System.out.println(name); // IEEE

        Inventor tesla = new Inventor("Nikola Tesla", "Serbian");
        StandardEvaluationContext context = new StandardEvaluationContext(tesla);
        String name1 = parser.parseExpression("Name?:'Elvis Presley'").getValue(context, String.class);
        System.out.println(name1); // Nikola Tesla
        tesla.setName(null);
        name1 = parser.parseExpression("Name?:'Elvis Presley'").getValue(context, String.class);
        System.out.println(name1); // Elvis Presley

        // 字符串连接
        System.out.println("拼接: " + parser.parseExpression("'Hello' + ' ' + 'World'").getValue());

        //instanceof运算符
        //需要注意元数据类型会自动装箱成包装类型，因此1 instanceof T(int)结果是false，1 instanceof T(Integer)的结果是true.
        Boolean instanceofFlag = parser.parseExpression("'aaa' instanceof T(String)").getValue(Boolean.class);
        System.out.println("instanceof:" + instanceofFlag);
        Boolean instanceofFlag1 = parser.parseExpression("1 instanceof T(int)").getValue(Boolean.class);
        System.out.println("instanceof:" + instanceofFlag1);
        Boolean instanceofFlag2 = parser.parseExpression("1 instanceof T(Integer)").getValue(Boolean.class);
        System.out.println("instanceof:" + instanceofFlag2);

        /**
         * 正则表达式示例
         * matches 用于定义正则表达式，之后跟着单引号包裹着的正则表达式
         */
        // 邮箱验证
        Expression exp1 = parser.parseExpression("'zhangsan@example.com' matches '^[A-Za-z0-9+_.-]+@(.+)$'");
        Boolean isEmail = exp1.getValue(Boolean.class);
        System.out.println("是否为邮箱: " + isEmail);  // true

        // 手机号验证
        Expression exp2 = parser.parseExpression("'13800138000' matches '^1[3-9]\\d{9}$'");
        Boolean isPhone = exp2.getValue(Boolean.class);
        System.out.println("是否为手机号: " + isPhone);  // true

        // 在对象上使用
        Inventor user = new Inventor("张三", "invalid-email");
        user.setEmail("111@qq.com");
        Expression exp3 = parser.parseExpression("email matches '^[A-Za-z0-9+_.-]+@(.+)$'");
        Boolean validEmail = exp3.getValue(user, Boolean.class);
        System.out.println("邮箱格式正确: " + validEmail);  // true
    }

    @Test
    public void spelTest18() {
        ExpressionParser parser = new SpelExpressionParser();
        Inventor inventor = new Inventor();
        StandardEvaluationContext inventorContext = new StandardEvaluationContext(inventor);
        //两种设置方法，效果等同
        parser.parseExpression("Name").setValue(inventorContext, "Alexander Seovic2");
        //通过赋值表达式来设置属性的值
        String aleks = parser.parseExpression("Name = 'Alexandar Seovic'").getValue(inventorContext, String.class);
    }

    @Test
    public void spelTest19() {
        ExpressionParser parser = new SpelExpressionParser();
        Class dateClass = parser.parseExpression("T(java.util.Date)").getValue(Class.class);
        Class stringClass = parser.parseExpression("T(String)").getValue(Class.class);
        boolean trueValue = parser.parseExpression("T(java.math.RoundingMode).CEILING &lt; T(java.math.RoundingMode)" +
                ".FLOOR").getValue(Boolean.class);
        //静态方法调用
        Double random = parser.parseExpression("T(java.lang.Math).random() * 100").getValue(Double.class);
        System.out.println(random);
    }

    @Test
    public void spelTest20() {
        ExpressionParser parser = new SpelExpressionParser();
        Society society = new Society();
        //解析上下文
        EvaluationContext societyContext = new StandardEvaluationContext(society);
        SpelUser einstein =
                parser.parseExpression("new com.demo.test.spel.SpelUser('Albert Einstein', 'German')").getValue(SpelUser.class);
        //create new inventor instance within add method of List
        //todo:内部类这样好像new不了，待找找原因
        parser.parseExpression("Members.add(new com.demo.test.spel.SpelTest.Inventor('Albert Einstein', 'German'))").getValue(societyContext);
    }

    @Test
    public void spelTest21() {
        ExpressionParser parser = new SpelExpressionParser();
        Inventor tesla = new Inventor("Nikola Tesla", "Serbian");
        StandardEvaluationContext context = new StandardEvaluationContext(tesla);
        context.setVariable("newName", "Mike Tesla");
        parser.parseExpression("Name = #newName").getValue(context);
        System.out.println(tesla.getName()); // "Mike Tesla"
    }

    /**
     * #this变量永远指向当前表达式正在求值的对象（这时不需要限定全名）。变量#root总是指向根上下文对象。#this在表达式不同部分解析过程中可能会改变，但是#root总是指向根
     */
    @Test
    public void spelTest22() {
        // create an array of integers
        List<Integer> primes = new ArrayList<Integer>();
        primes.addAll(Arrays.asList(2, 3, 5, 7, 11, 13, 17));
        // create parser and set variable 'primes' as the array of integers
        ExpressionParser parser = new SpelExpressionParser();
        StandardEvaluationContext context = new StandardEvaluationContext();
        context.setVariable("primes", primes);
        // all prime numbers > 10 from the list (using selection ?{...})
        // evaluates to [11, 13, 17]
        List<Integer> primesGreaterThanTen =
                (List<Integer>) parser.parseExpression("#primes.?[#this>10]").getValue(context);
    }

    /**
     * 函数
     */
    @Test
    public void spelTest23() throws NoSuchMethodException {
        ExpressionParser parser = new SpelExpressionParser();
        StandardEvaluationContext context = new StandardEvaluationContext();
        context.registerFunction("reverseString", StringUtilsReverse.class.getDeclaredMethod("reverseString",
                new Class[]{String.class}));
        String helloWorldReversed = parser.parseExpression("#reverseString('hello')").getValue(context, String.class);
    }

    /**
     * 安全运算符
     * 安全引用运算符主要为了避免空指针,使用安全引用运算符只会返回null而不是抛出一个异常。
     */
    @Test
    public void spelTest24() {
        ExpressionParser parser = new SpelExpressionParser();
        Inventor tesla = new Inventor("Nikola Tesla", "Serbian");
        tesla.setPlaceOfBirth(new PlaceOfBirth("Smiljan"));
        StandardEvaluationContext context = new StandardEvaluationContext(tesla);
        String city = parser.parseExpression("PlaceOfBirth?.City").getValue(context, String.class);
        System.out.println(city); // Smiljan
        tesla.setPlaceOfBirth(null);
        city = parser.parseExpression("PlaceOfBirth?.City").getValue(context, String.class);
        System.out.println(city); // null - does not throw NullPointerException!!!
    }

    /**
     * 集合投影
     */
    @Test
    public void spelTest25(){
        ExpressionParser parser = new SpelExpressionParser();
        Society society = new Society();
        Inventor AAAA = new Inventor("AAAA", "Serbian");
        Inventor BBBB = new Inventor("BBBB", "Serbian");
        Inventor CCCC = new Inventor("CCCC", "CHINA");
        ArrayList<Inventor> inventors = Lists.newArrayList(AAAA, BBBB, CCCC);
        society.setMembers(inventors);
        StandardEvaluationContext societyContext = new StandardEvaluationContext(society);
        List<Inventor> list = (List<Inventor>) parser.parseExpression("Members.?[Nationality == 'Serbian']").getValue(societyContext);

        System.out.println("====");
    }

    @Test
    public void spelTest26(){
        ExpressionParser parser = new SpelExpressionParser();
        StandardEvaluationContext context = new StandardEvaluationContext();

        List<SpelUser> userList = Arrays.asList(
                new SpelUser("zhangsan", "China"),
                new SpelUser("wangermazi", "China"),
                new SpelUser("lisi", "Japan"),
                new SpelUser("wangwu", "USA")
        );
        context.setVariable("userlist", userList);

        List<String> list = (List<String>)parser.parseExpression("#userlist.![name]").getValue(context);
        //第一个nationality == 'China'的
        SpelUser spelUser = (SpelUser)parser.parseExpression("#userlist.^[nationality == 'China']").getValue(context);
        //最后一个nationality == 'China'的
        SpelUser spelUser1 = (SpelUser)parser.parseExpression("#userlist.$[nationality == 'China']").getValue(context);
        System.out.println(list);
    }

    @Test
    public void spelTest27(){
        ExpressionParser parser = new SpelExpressionParser();
        String randomPhrase = parser.parseExpression("random number is #{T(java.lang.Math).random()}", new TemplateParserContext()).getValue(String.class);
// evaluates to "random number is 0.7038186818312008"
    }


    public class Inventor {
        private String name;
        private String nationality;
        private String[] inventions;
        private Date birthdate;
        private PlaceOfBirth placeOfBirth;

        private String email;

        public Inventor(String name, String nationality) {
            GregorianCalendar c = new GregorianCalendar();
            this.name = name;
            this.nationality = nationality;
            this.birthdate = c.getTime();
        }

        public Inventor(String name, Date birthdate, String nationality) {
            this.name = name;
            this.nationality = nationality;
            this.birthdate = birthdate;
        }

        public Inventor() {
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getNationality() {
            return nationality;
        }

        public void setNationality(String nationality) {
            this.nationality = nationality;
        }

        public Date getBirthdate() {
            return birthdate;
        }

        public void setBirthdate(Date birthdate) {
            this.birthdate = birthdate;
        }

        public PlaceOfBirth getPlaceOfBirth() {
            return placeOfBirth;
        }

        public void setPlaceOfBirth(PlaceOfBirth placeOfBirth) {
            this.placeOfBirth = placeOfBirth;
        }

        public void setInventions(String[] inventions) {
            this.inventions = inventions;
        }

        public String[] getInventions() {
            return inventions;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }

    public class PlaceOfBirth {
        private String city;
        private String country;

        public PlaceOfBirth(String city) {
            this.city = city;
        }

        public PlaceOfBirth(String city, String country) {
            this(city);
            this.country = country;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String s) {
            this.city = s;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }
    }

    @Data
    public class Society {
        private String name;
        public String Advisors = "advisors";
        private List<Inventor> members = new ArrayList<Inventor>();
        private Map officers = new HashMap();

        public boolean isMember(String name) {
            for (Inventor inventor : members) {
                if (inventor.getName().equals(name)) {
                    return true;
                }
            }
            return false;
        }

        private Boolean write() {
            return false;
        }
    }

    class Simple {
        public List<Boolean> booleanList = new ArrayList<Boolean>();
    }

    class Demo {
        public List<String> list;
    }

}
