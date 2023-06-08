package com.demo.practise.comparator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class User implements Comparable<User>{

    private int age;

    private String name;


    @Override
    public int compareTo(User o) {
        //age从小到大
        if (this.getAge() - o.getAge() > 0){
            return 1;
        }
        //如果年龄相同，那么比较姓名，按姓名的ascii码值来排序，例如：a-b-c
        if (this.getAge() - o.getAge() == 0){
            return this.getName().compareTo(o.getName());
        }

        return -1;
    }
}
