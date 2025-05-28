package com.demo.designmodel.iterator;

/**
 * @author juven
 * @date 2025/5/28 0:29
 * @description 创建实现了 Container 接口的实体类
 * 该类有实现了 Iterator 接口的内部类 NameIterator
 */
public class NameRepository implements Container {
    public String names[] = {"Robert" , "John" ,"Julie" , "Lora"};

    @Override
    public Iterator getIterator() {
        return new NameIterator();
    }

    private class NameIterator implements Iterator {

        int index;

        @Override
        public boolean hasNext() {
            if(index < names.length){
                return true;
            }
            return false;
        }

        @Override
        public Object next() {
            if(this.hasNext()){
                return names[index++];
            }
            return null;
        }
    }
}
