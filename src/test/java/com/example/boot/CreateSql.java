package com.example.boot;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 2020/2/27 14:01
 * fzj
 */
public class CreateSql {

    private static String tableName;

    /**
     * sql脚本所在的绝对路径
     */
    private static final String fileName = "E:\\workspace\\spring-boot\\src\\test\\java\\com\\example\\boot\\create.sql";
    /**
     * 是否要开启下划线转驼峰
     */
    private static boolean isHump = false;


    public static void main(String[] args) throws IOException {
        List<ColumnAndType> columnAndTypes = getColumnAndTypes();
        //不插入的字段
        List<String> insertIgnore = Arrays.asList("id", "deleted_at", "delete_status");
        insert(insertIgnore, columnAndTypes);

        System.out.println("\n");
        delete();

        //删除状态和删除时间的名字
        System.out.println();
        String statusName = "delete_status";
        String deleteTimeName = "deleted_at";
        deleteAsStatus(statusName, deleteTimeName);

        //查询不返回的字段
        System.out.println("\n");
        List<String> updateIgnore = Arrays.asList("id", "created_at", "deleted_at", "delete_status");
        update(updateIgnore, columnAndTypes);

        System.out.println();
        List<String> selectIgnore = new ArrayList<>();
        getById(selectIgnore, columnAndTypes);

        System.out.println();
        getAll(selectIgnore, columnAndTypes);

        System.out.println();
        getBySelectKey(selectIgnore, columnAndTypes);


    }

    private static void insert(List<String> insertIgnore, List<ColumnAndType> columnAndTypes) {
        columnAndTypes.removeIf(next -> insertIgnore.contains(next.getColumn()));
        StringBuilder sb = new StringBuilder();
        sb.append("<insert id=\"insert\" useGeneratedKeys=\"true\" keyProperty=\"id\">\n\t");
        sb.append("insert into ").append(tableName).append("\n\t\t(");
        int size = 0;
        for (ColumnAndType columnAndType : columnAndTypes) {
            size++;
            sb.append(columnAndType.getColumn());
            if (size != columnAndTypes.size()) {
                sb.append(",");
                if (size % 5 == 0) {
                    sb.append("\n\t\t");
                }
            }
        }
        sb.append(")\n\tvalues\n\t\t(");
        size = 0;
        for (ColumnAndType columnAndType : columnAndTypes) {
            size++;
            sb.append("#{").append(isHump ? hump(columnAndType.getColumn()) : columnAndType.getColumn()).append("}");
            if (size != columnAndTypes.size()) {
                sb.append(",");
                if (size % 5 == 0) {
                    sb.append("\n\t\t");
                }
            }
        }
        sb.append(")\n</insert>");
        System.out.println(sb);
    }

    private static void delete() {
        System.out.println(String.format("<delete id=\"delete\">\n\tdelete from %s where id = #{id}\n</delete>", tableName));
    }

    private static void deleteAsStatus(String statusName, String deleteTimeName) {
        System.out.println(String.format("<update id=\"deleteAsStatus\">\n\tupdate %s set %s = 1,%s = #{%s} where id = #{id}\n</update>", tableName, statusName, deleteTimeName, deleteTimeName));
    }

    private static void update(List<String> updateIgnore, List<ColumnAndType> columnAndTypes) {
        StringBuilder sb = new StringBuilder();
        sb.append("<update id=\"update\">\n\t");
        sb.append("update ").append(tableName).append("\n\t").append("<set>\n\t\t");
        for (ColumnAndType columnAndType : columnAndTypes) {
            if (updateIgnore.contains(columnAndType.getColumn())) {
                continue;
            }
            sb.append("<if test=\"").append(columnAndType.getColumn()).append(" != null");
            if ("char".equals(columnAndType.getType()) || "varchar".equals(columnAndType.getType()) || "text".equals(columnAndType.getType())) {
                sb.append(" and ").append(columnAndType.getColumn()).append(" !=''");
            }
            sb.append(">\n\t\t\t").append(columnAndType.getColumn()).append(" = #{").append(isHump ? hump(columnAndType.getColumn()) : columnAndType.getColumn()).append("},\n\t\t</if>\n\t\t");
        }
        sb.append("\n\twhere id = #{id}\n</update");
        System.out.println(sb);
    }

    private static void getById(List<String> selectIgnore, List<ColumnAndType> columnAndTypes) {
        StringBuilder sb = getSelectColumn(selectIgnore, columnAndTypes, "getById");
        sb.append("\n\twhere id = #{id}\n</select>");
        System.out.println(sb);
    }

    private static void getAll(List<String> selectIgnore, List<ColumnAndType> columnAndTypes) {
        StringBuilder sb = getSelectColumn(selectIgnore, columnAndTypes, "getAll");
        sb.append("\n</select>");
        System.out.println(sb);
    }

    private static void getBySelectKey(List<String> selectIgnore, List<ColumnAndType> columnAndTypes) {
        StringBuilder sb = getSelectColumn(selectIgnore, columnAndTypes, "getBySelectKey");
        sb.append("\n\twhere 1 = 1\n\t");
        for (ColumnAndType columnAndType : columnAndTypes) {
            sb.append("<if test=\"").append(columnAndType.getColumn()).append(" != null");
            if ("char".equals(columnAndType.getType()) || "varchar".equals(columnAndType.getType()) || "text".equals(columnAndType.getType())) {
                sb.append(" and ").append(columnAndType.getColumn()).append(" !=''");
            }
            sb.append(">\n\t\tand ").append(columnAndType.getColumn()).append(" = #{").append(isHump ? hump(columnAndType.getColumn()) : columnAndType.getColumn()).append("}\n\t</if>\n\t");
        }
        sb.append("\n</select>");
        System.out.println(sb);
    }

    private static StringBuilder getSelectColumn(List<String> selectIgnore, List<ColumnAndType> columnAndTypes, String methName) {
        columnAndTypes.removeIf(next -> selectIgnore.contains(next.getColumn()));
        StringBuilder sb = new StringBuilder();
        sb.append("<select id=\"").append(methName).append("\" resultType=\"").append(hump(tableName)).append("\">\n\t");
        sb.append("select ");
        int size = 0;
        for (ColumnAndType columnAndType : columnAndTypes) {
            size++;
            sb.append(columnAndType.getColumn());
            if (size != columnAndTypes.size()) {
                sb.append(",");
                if (size % 5 == 0) {
                    sb.append("\n\t\t");
                }
            }
        }
        sb.append("\n\tfrom ").append(tableName);
        return sb;
    }

    /**
     * 获取表名和列名类型
     */
    private static List<ColumnAndType> getColumnAndTypes() throws IOException {
        FileInputStream inputStream = new FileInputStream(new File(fileName));
        int available = inputStream.available();
        byte[] bytes = new byte[available];
        String sql = "";
        while (inputStream.read(bytes) != -1) {
            sql = new String(bytes);
        }
        inputStream.close();
        Pattern tablePattern = Pattern.compile("(?<=CREATE ?TABLE ?`)\\w+(?=`)");
        Matcher tableMatch = tablePattern.matcher(sql);
        while (tableMatch.find()) {
            tableName = tableMatch.group();
        }
        if (tableName == null) {
            throw new IOException("表名格式不正确");
        }

        Pattern columnPattern = Pattern.compile("(?<=`)\\w+` +(int|tinyint|bigint|decimal|timestamp|folat|double|char|varchar|text)");
        Matcher columnMatcher = columnPattern.matcher(sql);
        List<ColumnAndType> columnAndTypes = new ArrayList<>();
        while (columnMatcher.find()) {
            String[] columnAndType = columnMatcher.group().split("` +");
            columnAndTypes.add(new ColumnAndType().setColumn(columnAndType[0]).setType(columnAndType[1]));
        }
        return columnAndTypes;
    }

    /**
     * 下划线转驼峰
     */
    private static String hump(String str) {
        String[] split = str.split("_");
        if (split.length <= 1) {
            return str;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < split.length; i++) {
            if (i == 0) {
                sb.append(split[0]);
            } else {
                sb.append(toUpperFirst(split[i]));
            }
        }
        return sb.toString();
    }


    /**
     * 首字母大写
     */
    private static String toUpperFirst(String str) {
        char[] chars = str.toCharArray();
        if (chars[0] >= 'a' && chars[0] <= 'z') {
            chars[0] = (char) (chars[0] - 32);
        }
        return new String(chars);
    }
}
