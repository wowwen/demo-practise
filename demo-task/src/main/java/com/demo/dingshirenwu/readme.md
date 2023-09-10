### SpringBoot创建定时任务，目前主要有以下三种实现方式：

####1.基于注解(@Scheduled)： 基于注解@Scheduled默认为单线程，开启多个任务时，任务的执行时机会受上一个任务执行时间的影响；
####2.基于接口（SchedulingConfigurer）： 用于实现从数据库获取指定时间来动态执行定时任务；
####3.基于注解设定多线程定时任务： 
####4.动态设置定时任务。之前的定时任务都是提前定义好放在配置文件或者数据库；不能再项目运行中动态修改任务执行时间，不太灵活