package com.example.boot.generateSql;


/**
 * 2020/2/27 15:14
 * fzj
 * 列信息
 */
class ColumnAndType {

    private String column;          //列名
    private String type;            //列类型
    private String comment;         //列注释

    String getColumn() {
        return column;
    }

    ColumnAndType setColumn(String column) {
        this.column = column;
        return this;
    }

    String getType() {
        return type;
    }

    ColumnAndType setType(String type) {
        this.type = type;
        return this;
    }

    String getComment() {
        return comment;
    }

    ColumnAndType setComment(String comment) {
        this.comment = comment;
        return this;
    }

    @Override
    public String toString() {
        return "ColumnAndType{" +
                "column='" + column + '\'' +
                ", type='" + type + '\'' +
                ", comment='" + comment + '\'' +
                '}';
    }
}
