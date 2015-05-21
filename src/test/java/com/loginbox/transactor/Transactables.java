package com.loginbox.transactor;

public interface Transactables {
    public void action(Context c) throws Exception;

    public void sink(Context c, String v) throws Exception;

    public String query(Context c) throws Exception;

    public String transform(Context c, String v) throws Exception;

    public String merge(Context c, String a, String b) throws Exception;
}
