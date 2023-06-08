package com.demo.practise.validator;

import javax.validation.groups.Default;

/**
 * ©Copyright ©1968-2020 Midea Group,IT
 *
 * @FileName: Create
 * @Author: jiangyw8
 * @Date: 2020-9-26 21:35
 * @Description: 分组校验接口，@Validated特有
 * 注意:在声明分组的时候尽量加上 extend javax.validation.groups.Default 否则,在你声明@Validated(Update.class)
 * 的时候,就会出现你在默认没添加groups = {}的时候的校验组@Email(message = "邮箱格式不对"),会不去校验,因为默认的校验组是groups = {Default.class}.
 *
 * 如此篇文章中不继承Default也是可以的，继承的区别在于继承了default之后，即使在DTO中没写group也会校验属性，如果没继承default，
 * 则DTO中没写group的属性不会校验
 * https://blog.csdn.net/qq_39632561/article/details/108546152?utm_medium=distribute.pc_aggpage_search_result.none-task-blog-2~all~first_rank_v2~rank_v25-19-108546152.nonecase&utm_term=validated%E6%B3%A8%E8%A7%A3
 */
public interface Create extends Default {
}
