package com.demo.aop.controller;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * @author juven
 * @date 2025/5/3 22:02
 * @description
 */
@RestController
public class I18Controller {
    @Resource
    MessageSource messageSource;

    @GetMapping("/i18n/test")
    public String testI18n(){
        Locale locale = LocaleContextHolder.getLocale();
        System.out.println("=====" + locale.getDisplayName());
        String unknown_error = messageSource.getMessage("UNKNOWN_ERROR", null, locale);
        System.out.println(unknown_error);
        return unknown_error;
    }

    public static void main(String[] args) {
        //格式化日期和时间
        Date date = new Date();
        // 使用 Locale 设置日期格式
        // 法国的日期格式
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.LONG, Locale.FRANCE);
        System.out.println("Date in French Locale: " + dateFormat.format(date));
        // 输出时间部分
        dateFormat = DateFormat.getTimeInstance(DateFormat.FULL, Locale.FRANCE);
        System.out.println(dateFormat.format(date));
        // 输出日期和时间
        dateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.LONG,Locale.CHINA);
        System.out.println(dateFormat.format(date));

        //也可以通过ResourceBundle获取国际化信息
        // 获取默认的 Locale
        Locale locale = Locale.getDefault();
        // 根据 Locale 加载对应的 ResourceBundle
        ResourceBundle bundle = ResourceBundle.getBundle("messages", locale);
        // 获取国际化内容
        String greeting = bundle.getString("UNKNOWN_ERROR");
        // 打印本地化内容
        System.out.println(greeting);


        //更多本地化日期和时间格式化的功能
        LocalDate date1 = LocalDate.now();
        // 使用 Locale 格式化日期
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.FRENCH);
        System.out.println("Date in French Locale: " + date1.format(formatter));


        double number = 1234567.89;
        // 使用 Locale 设置数字格式
        // 德国数字格式
        NumberFormat numberFormat = NumberFormat.getInstance(Locale.GERMANY);
        System.out.println("Number in German Locale: " + numberFormat.format(number));
        // 使用 Locale 设置货币格式
        // 英国货币格式
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.UK);
        System.out.println("Currency in UK Locale: " + currencyFormat.format(number));
        //英国整数形式
        NumberFormat integerFormat = NumberFormat.getIntegerInstance(Locale.UK);
        System.out.println(integerFormat.format(number));

        NumberFormat percentFormat = NumberFormat.getPercentInstance(Locale.GERMANY);
        System.out.println(percentFormat.format(number));

        Locale locale1 = Locale.US;
        Currency currency = Currency.getInstance(locale1);
        // 输出货币的符号和代码
        System.out.println("Currency Symbol: " + currency.getSymbol(locale));
        System.out.println("Currency Code: " + currency.getCurrencyCode());

        //模式字符串
        String pattern = "On {0}, a hurricance destroyed {1} houses and caused {2} of damage.";
        //实例化MessageFormat对象，并装载相应的模式字符串
        MessageFormat format = new MessageFormat(pattern, Locale.CHINA);
        Object arr[] = {new Date(), 99, 100000000};
        //格式化模式字符串，参数数组中指定占位符相应的替换对象
        String result = format.format(arr);
        System.out.println(result);

        //模式字符串
        String pattern1 = "At {0, time, short} on {0, date}, a destroyed {1} houses and caused {2, number, currency} " +
                "of damage.";
        //实例化MessageFormat对象，并装载相应的模式字符串
        MessageFormat format1 = new MessageFormat(pattern1, Locale.US);
        Object arr1[] = {new Date(), 99, 100000000};
        //格式化模式字符串，参数数组中指定占位符相应的替换对象
        String result1 = format1.format(arr1);
        System.out.println(result1);
    }

}
