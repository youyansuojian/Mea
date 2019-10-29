package com.hrbeu.mes.pojo;

public class Template {
    private long id;
    private String table_name;
    private String tp_name;
    private String header_index;
    private String header_content;
    private String header_merge;
    private String data_merge;
    private int fix_column_left;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTable_name() {
        return table_name;
    }

    public void setTable_name(String table_name) {
        this.table_name = table_name;
    }

    public String getTp_name() {
        return tp_name;
    }

    public void setTp_name(String tp_name) {
        this.tp_name = tp_name;
    }

    public String getHeader_index() {
        return header_index;
    }

    public void setHeader_index(String header_index) {
        this.header_index = header_index;
    }

    public String getHeader_content() {
        return header_content;
    }

    public void setHeader_content(String header_content) {
        this.header_content = header_content;
    }

    public String getHeader_merge() {
        return header_merge;
    }

    public void setHeader_merge(String header_merge) {
        this.header_merge = header_merge;
    }

    public String getData_merge() {
        return data_merge;
    }

    public void setData_merge(String data_merge) {
        this.data_merge = data_merge;
    }

    public int getFix_column_left() {
        return fix_column_left;
    }

    public void setFix_column_left(int fix_column_left) {
        this.fix_column_left = fix_column_left;
    }


}
