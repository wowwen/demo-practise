package com.demo.designmodel.nullobject;

/**
 * @author juven
 * @date 2025/5/28 16:18
 * @description
 */
public abstract class AbstractCustomer
{
    protected String name;
    public abstract boolean isNil();
    public abstract String getName();
}

class RealCustomer extends AbstractCustomer{

    public RealCustomer(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isNil() {
        return false;
    }
}

class NullCustomer extends AbstractCustomer {

    @Override
    public String getName() {
        return "Not Available in Customer Database";
    }

    @Override
    public boolean isNil() {
        return true;
    }
}