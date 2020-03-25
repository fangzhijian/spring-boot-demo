package com.example.boot.generateSql;


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
 * 生成简单通用的sql以及bean中的成员变量
 * <p>
 * <p>创建表sql语句可以从NaVICat中直接复制
 * <p>首次就配置前6个，FILE_NAME 到 DELETE_TIME_NAME，表不用假删除没关系,前5个基本是全局配置
 * <p>首次配置后基本无需修改，除了mybatis不指定别名时要修改对应的JavaBean路径
 * <p>beanIgnore、insertIgnore、updateIgnore、selectIgnore可以忽略字段
 * <p>select要返回的字段不抽取到<sql><sql/>中共同使用,后期会与select*无区别,建议删除不使用的返回字段
 * 或者在selectIgnore中加入
 */
public class GenerateSql {

    /*** sql脚本所在的绝对路径*/
    private static final String FILE_NAME = "E:\\workspace\\spring-boot\\src\\test\\java\\com\\example\\boot\\generateSql\\createTable.sql";
    /*** 是否需要下划线转驼峰*/
    private static final boolean IS_HUMP = true;
    /*** mybatis是否开启别名,默认别名为表名驼峰格式*/
    private static final boolean USE_TYPE_ALIAS = true;
    /*** 假删除状态的字段名字,这里默认0-未删除 1-已删除*/
    private static final String DELETE_COLUMN_NAME = "delete_status";
    /*** 假删除时间的名字*/
    private static final String DELETE_TIME_NAME = "delete_time";
    /*** 如果未开启别名,这里需要指定bean所在的路径*/
    private static final String TYPE_PACKAGE = "com.directory1.directory2.JavaBean";

    /*** 根据sql得到 是否含有假删除字段*/
    private static boolean hasDeleteColumn;
    /*** 根据sql得到 表名*/
    private static String tableName;
    /*** 根据sql得到 id名*/
    private static String idName;
    /*** 根据sql得到 表的列信息*/
    private static List<ColumnAndType> columnAndTypes;

    static {
        try {
            columnAndTypes = getColumnAndTypes();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /*** 创建时间,没啥作用,就默认忽略时使用*/
    private static final String CREATE_TIME_NAME = "created_at";
    /*** 更新时间,没啥作用,就默认忽略时使用*/
    private static final String UPDATE_TIME_NAME = "updated_at";
    /*** 生成bean时需要忽略的成员变量,一般在父类中,例中字段没在父类中就删除掉*/
    private static final List<String> beanIgnore = Arrays.asList(idName, DELETE_TIME_NAME, DELETE_COLUMN_NAME, CREATE_TIME_NAME, UPDATE_TIME_NAME);
    /*** 新增忽略的字段*/
    private static final List<String> insertIgnore = Arrays.asList(idName, DELETE_TIME_NAME, DELETE_COLUMN_NAME);
    /*** 更新忽略的字段*/
    private static final List<String> updateIgnore = Arrays.asList(idName, DELETE_TIME_NAME, DELETE_COLUMN_NAME, CREATE_TIME_NAME);
    /*** 查询不返回的字段,gitById方法不会忽略假删除字段和时间*/
    private static final List<String> selectIgnore = Arrays.asList(DELETE_TIME_NAME, DELETE_COLUMN_NAME);


    public static void main(String[] args) {

        //生成bean的成员变量
        generateBean();

        //mybatis的resultMap
        if (!USE_TYPE_ALIAS) {
            resultMap();
        }

        //不用项目模板方法id可能不一样,可用于修改mybatis xml中对应的id
        String mybatisId;

        //新增
        mybatisId = "insert";
        insert(mybatisId);

        //批量新增
        mybatisId = "insertBatch";
        insertBatch(mybatisId);

        //删除(假删除)
        mybatisId = "delete";
        delete(mybatisId);

        //根据id批量删除(假删除)
        mybatisId = "deleteByIds";
        deleteByIds(mybatisId);

        //更新
        mybatisId = "update";
        update(mybatisId);

        //批量更新
        mybatisId = "updateBatch";
        updateBatch(mybatisId);

        //根据id查询
        mybatisId = "getById";
        getById(mybatisId);

        //查询所有数据
        mybatisId = "getAll";
        getAll(mybatisId);

        //根据条件查询一条数据
        mybatisId = "getOneBySelectKey";
        getOneBySelectKey(mybatisId);

        //根据条件查询所有数据
        mybatisId = "getAllBySelectKey";
        getAllBySelectKey(mybatisId);


    }

    /**
     * 生成bean的成员变量
     */
    private static void generateBean() {
        List<ColumnAndType> columnAndTypeList = new ArrayList<>(columnAndTypes);
        columnAndTypeList.removeIf(next -> beanIgnore.contains(next.getColumn()));
        StringBuilder sb = new StringBuilder();
        int maxColumnLength = 0;
        for (ColumnAndType columnAndType : columnAndTypeList) {
            int columnLength = columnAndType.getColumn().length();
            if (columnLength > maxColumnLength) {
                maxColumnLength = columnLength;
            }
        }
        for (ColumnAndType columnAndType : columnAndTypeList) {
            sb.append("\tprivate ");
            int typeLength = "LocalDateTime".length();
            if ("char".equals(columnAndType.getType()) || "varchar".equals(columnAndType.getType()) || "text".equals(columnAndType.getType())) {
                sb.append("String");
                typeLength = typeLength - "String".length();
            } else if ("bigint".equals(columnAndType.getType())) {
                sb.append("Long");
                typeLength = typeLength - "Long".length();
            } else if ("tinyint".equals(columnAndType.getType()) || "int".equals(columnAndType.getType())) {
                sb.append("Integer");
                typeLength = typeLength - "Integer".length();
            } else if ("float".equals(columnAndType.getType())) {
                sb.append("Float");
                typeLength = typeLength - "Float".length();
            } else if ("double".equals(columnAndType.getType())) {
                sb.append("Double");
                typeLength = typeLength - "Double".length();
            } else if ("decimal".equals(columnAndType.getType())) {
                sb.append("BigDecimal");
                typeLength = typeLength - "BigDecimal".length();
            } else if ("timestamp".equals(columnAndType.getType()) || "datetime".equals(columnAndType.getType())) {
                sb.append("LocalDateTime");
                typeLength = typeLength - "LocalDateTime".length();
            }
            sb.append(" ");
            sb.append(setColumnIfHump(columnAndType.getColumn()));
            sb.append(";");
            int spaceLength = maxColumnLength - columnAndType.getColumn().length() + typeLength + 2;
            //下划线转驼峰会使原来长度减1,所有需要加上以对齐
            spaceLength = IS_HUMP ? (spaceLength + columnAndType.getColumn().split("_").length - 1) : spaceLength;

            for (int i = 0; i < spaceLength; i++) {
                sb.append(" ");
            }
            sb.append("//").append(columnAndType.getComment() == null?"":columnAndType.getComment()).append("\n");
        }
        System.out.println(sb.toString());
    }

    /**
     * mybatis的resultMap
     */
    private static void resultMap() {
        StringBuilder sb = new StringBuilder();
        sb.append("\t<resultMap id=\"").append(hump(tableName)).append("Map\" type=\"").append(TYPE_PACKAGE).append("\">\n");
        sb.append("\t\t<id column=\"").append(idName).append("\" property=\"").append(hump(idName)).append("\"/>\n");
        for (ColumnAndType columnAndType : columnAndTypes) {
            if (columnAndType.getColumn().equals(idName)) {
                continue;
            }
            sb.append("\t\t<result column=").append(columnAndType.getColumn()).append("\" property=\"").append(hump(columnAndType.getColumn())).append("\"/>\n");
        }
        sb.append("\t</resultMap>\n");
        System.out.println(sb.toString());
    }

    /**
     * 新增
     *
     * @param mybatisId mybatis xml中方法的id
     */
    private static void insert(String mybatisId) {
        insertCommon("insert", mybatisId);
    }

    /**
     * 批量新增
     *
     * @param mybatisId mybatis xml中方法的id
     */
    private static void insertBatch(String mybatisId) {
        insertCommon("insertBatch", mybatisId);
    }

    /**
     * 新增的功能方法
     *
     * @param methodName 方法名 新增还是批量新增
     * @param mybatisId  mybatis xml中方法的id
     */
    private static void insertCommon(String methodName, String mybatisId) {
        List<ColumnAndType> columnAndTypeList = new ArrayList<>(columnAndTypes);
        columnAndTypeList.removeIf(next -> insertIgnore.contains(next.getColumn()));
        StringBuilder sb = new StringBuilder();
        sb.append("\t<insert id=\"").append(mybatisId).append("\" useGeneratedKeys=\"true\" keyProperty=\"").append(idName).append("\">\n\t\t");
        sb.append("insert into ").append(tableName).append("\n\t\t\t(");
        //拼接所有列名,逗号隔开,每5个换行
        displayAllColumn(columnAndTypeList, sb);
        sb.append(")\n\t\tvalues");

        int size = 0;
        boolean insertBatch = "insertBatch".equals(methodName);
        if (insertBatch) {
            sb.append("\n\t\t<foreach collection=\"list\" item=\"item\" separator=\",\">\n\t\t\t(");
        } else {
            sb.append("\n\t\t\t(");
        }
        for (ColumnAndType columnAndType : columnAndTypeList) {
            size++;
            sb.append("#{");
            if (insertBatch) {
                sb.append("item.");
            }
            sb.append(setColumnIfHump(columnAndType.getColumn())).append("}");
            if (size != columnAndTypeList.size()) {
                sb.append(",");
                if (size % 5 == 0) {
                    sb.append("\n\t\t\t");
                }
            }
        }
        if (insertBatch) {
            sb.append(")\n\t\t</foreach>");
        } else {
            sb.append(")");
        }
        sb.append("\n\t</insert>\n");
        System.out.println(sb);
    }

    /**
     * 删除或假删除
     *
     * @param mybatisId mybatis xml中方法的id
     */
    private static void delete(String mybatisId) {
        if (hasDeleteColumn) {
            System.out.println(String.format("\t<update id=\"%s\">\n\t\tupdate %s set %s = 1,%s = #{%s} where %s = #{%s}\n\t</update>\n",
                    mybatisId, tableName, DELETE_COLUMN_NAME, DELETE_TIME_NAME, setColumnIfHump(DELETE_TIME_NAME), idName, setColumnIfHump(idName)));
        } else {
            System.out.println(String.format("\t<delete id=\"%s\">\n\t\tdelete from %s where %s = #{%s}\n\t</delete>\n",
                    mybatisId, tableName, idName, setColumnIfHump(idName)));
        }
    }

    /**
     * 根据id批量删除
     *
     * @param mybatisId mybatis xml中方法的id
     */
    private static void deleteByIds(String mybatisId) {
        StringBuilder sb = new StringBuilder();
        if (hasDeleteColumn) {
            sb.append("\t<update id=\"").append(mybatisId).append("\">\n\t\tupdate ").append(tableName).append(" set ").append(DELETE_COLUMN_NAME).append(" = 1, ")
                    .append(DELETE_TIME_NAME).append(" = #{").append(setColumnIfHump(DELETE_TIME_NAME)).append("}");

        } else {
            sb.append("\t<delete id=\"deleteByIds\">\n\t\tdelete from ").append(tableName);
        }
        sb.append(" where ").append(idName).append(" in\n\t\t<foreach collection=\"list\" item=\"item\" separator=\",\" open=\"(\" close=\")\">\n\t\t\t#{item}\n\t\t</foreach>\n\t");
        if (hasDeleteColumn) {
            sb.append("</update>\n");
        } else {
            sb.append("</delete>\n");
        }
        System.out.println(sb);
    }

    /**
     * 更新
     *
     * @param mybatisId mybatis xml中方法的id
     */
    private static void update(String mybatisId) {
        updateCommon("update", mybatisId);
    }

    /**
     * 批量更新
     *
     * @param mybatisId mybatis xml中方法的id
     */
    private static void updateBatch(String mybatisId) {
        updateCommon("updateBatch", mybatisId);
    }

    /**
     * 更新的共同方法
     *
     * @param methodName 查询方法名
     * @param mybatisId  mybatis xml中方法的id
     */
    private static void updateCommon(String methodName, String mybatisId) {
        StringBuilder sb = new StringBuilder();
        sb.append("\t<update id=\"").append(mybatisId).append("\">\n\t\t");
        boolean updateBatch = "updateBatch".equals(methodName);
        if (updateBatch) {
            sb.append("<foreach collection=\"list\" item=\"item\" separator=\";\">\n\t\t\t");
        }
        sb.append("update ").append(tableName).append("\n\t\t");
        if (updateBatch) {
            sb.append("\t");
        }
        sb.append("<set>\n\t\t\t");
        if (updateBatch) {
            sb.append("\t");
        }
        for (ColumnAndType columnAndType : columnAndTypes) {
            if (updateIgnore.contains(columnAndType.getColumn())) {
                continue;
            }
            sb.append("<if test=\"");
            if (updateBatch) {
                sb.append("item.");
            }
            sb.append(setColumnIfHump(columnAndType.getColumn())).append(" != null ");
            if ("char".equals(columnAndType.getType()) || "varchar".equals(columnAndType.getType()) || "text".equals(columnAndType.getType())) {
                sb.append("and ");
                if (updateBatch) {
                    sb.append("item.");
                }
                sb.append(setColumnIfHump(columnAndType.getColumn())).append(" !='' ");
            }
            sb.append("\">\n\t\t\t\t");
            if (updateBatch) {
                sb.append("\t");
            }
            sb.append(columnAndType.getColumn()).append(" = #{");
            if (updateBatch) {
                sb.append("item.");
            }
            sb.append(setColumnIfHump(columnAndType.getColumn())).append("},\n\t\t\t");
            if (updateBatch) {
                sb.append("\t");
            }
            sb.append("</if>\n\t\t\t");
            if (updateBatch) {
                sb.append("\t");
            }
        }
        sb.append("\n\t\t");
        if (updateBatch) {
            sb.append("\t");
        }
        sb.append("</set>\n\t\t");
        if (updateBatch) {
            sb.append("\t");
        }
        sb.append("where ").append(idName).append(" = #{");
        if (updateBatch){
            sb.append("item.");
        }
        sb.append(setColumnIfHump(idName)).append("}");
        if (updateBatch) {
            sb.append("\n\t\t</foreach>");
        }
        sb.append("\n\t</update>\n");
        System.out.println(sb);
    }

    /**
     * 根据id查询
     *
     * @param mybatisId mybatis xml中方法的id
     */
    private static void getById(String mybatisId) {
        StringBuilder sb = getSelectColumn("getById", mybatisId);
        sb.append("\n\t\twhere ").append(idName).append(" = #{").append(setColumnIfHump(idName)).append("}\n\t</select>\n");
        System.out.println(sb);
    }

    /**
     * 查询所有数据
     *
     * @param mybatisId mybatis xml中方法的id
     */
    private static void getAll(String mybatisId) {
        StringBuilder sb = getSelectColumn("getAll", mybatisId);
        if (hasDeleteColumn) {
            sb.append("\n\t\twhere ").append(DELETE_COLUMN_NAME).append(" = 0 ");
        }
        sb.append("\n\t</select>\n");
        System.out.println(sb);
    }

    /**
     * 根据条件查询一条数据
     *
     * @param mybatisId mybatis xml中方法的id
     */
    private static void getOneBySelectKey(String mybatisId) {
        StringBuilder sb = getSelectColumn("getOneBySelectKey", mybatisId);
        getSelectWhere(sb);
        sb.append("\n\t\tlimit 1\n\t</select>\n");
        System.out.println(sb);
    }

    /**
     * 根据条件查询所有数据
     *
     * @param mybatisId mybatis xml中方法的id
     */
    private static void getAllBySelectKey(String mybatisId) {
        StringBuilder sb = getSelectColumn("getAllBySelectKey", mybatisId);
        getSelectWhere(sb);
        sb.append("\n\t</select>\n");
        System.out.println(sb);
    }

    /**
     * 获取查询的字符串头
     *
     * @param methodName 查询方法名
     * @param mybatisId  mybatis xml中方法的id
     * @return 拼接的字符串
     */
    private static StringBuilder getSelectColumn(String methodName, String mybatisId) {
        List<ColumnAndType> columnAndTypeList = new ArrayList<>(columnAndTypes);
        if ("getById".equals(methodName)) {
            columnAndTypeList.removeIf(next -> !DELETE_COLUMN_NAME.equals(next.getColumn()) && !DELETE_TIME_NAME.equals(next.getColumn()) && selectIgnore.contains(next.getColumn()));
        } else {
            columnAndTypeList.removeIf(next -> selectIgnore.contains(next.getColumn()));
        }
        StringBuilder sb = new StringBuilder();
        sb.append("\t<select id=\"").append(mybatisId);
        if (!USE_TYPE_ALIAS) {
            sb.append("\" resultMap=\"").append(hump(tableName)).append("Map\">\n\t\t");
        } else {
            sb.append("\" resultType=\"").append(hump(tableName)).append("\">\n\t\t");
        }
        sb.append("select\n\t\t\t");
        //拼接所有列名,逗号隔开,每5个换行
        displayAllColumn(columnAndTypeList, sb);
        sb.append("\n\t\tfrom ").append(tableName);
        return sb;
    }

    /**
     * 拼接where条件
     *
     * @param sb 之前的拼接字符串
     */
    private static void getSelectWhere(StringBuilder sb) {
        if (hasDeleteColumn) {
            sb.append("\n\t\twhere ").append(DELETE_COLUMN_NAME).append(" = 0 ");
        } else {
            sb.append("\n\t\twhere 1 = 1");
        }
        sb.append("\n\t\t");
        for (ColumnAndType columnAndType : columnAndTypes) {
            if ("id".equals(columnAndType.getColumn()) || DELETE_COLUMN_NAME.equals(columnAndType.getColumn()) || DELETE_TIME_NAME.equals(columnAndType.getColumn())) {
                continue;
            }
            sb.append("<if test=\"").append(setColumnIfHump(columnAndType.getColumn())).append(" != null");
            if ("char".equals(columnAndType.getType()) || "varchar".equals(columnAndType.getType()) || "text".equals(columnAndType.getType())) {
                sb.append(" and ").append(setColumnIfHump(columnAndType.getColumn())).append(" !=''");
            }
            sb.append("\">\n\t\t\tand ").append(columnAndType.getColumn()).append(" = #{").append(setColumnIfHump(columnAndType.getColumn())).append("}\n\t\t</if>\n\t\t");
        }
    }

    /**
     * 拼接所有列名,逗号隔开,每5个换行
     *
     * @param columnAndTypeList 列信息
     * @param sb                之前拼接的字符串
     */
    private static void displayAllColumn(List<ColumnAndType> columnAndTypeList, StringBuilder sb) {
        int size = 0;
        for (ColumnAndType columnAndType : columnAndTypeList) {
            size++;
            sb.append(columnAndType.getColumn());
            if (size != columnAndTypeList.size()) {
                sb.append(",");
                if (size % 5 == 0) {
                    sb.append("\n\t\t\t");
                }
            }
        }
    }

    /**
     * 获取表名和列名类型
     */
    private static List<ColumnAndType> getColumnAndTypes() throws IOException {
        FileInputStream inputStream = new FileInputStream(new File(FILE_NAME));
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

        Pattern columnPattern = Pattern.compile("(?<=`)(\\w+)` +(int|tinyint|bigint|decimal|datetime|timestamp|float|double|char|varchar|text)");
        Matcher columnMatcher = columnPattern.matcher(sql);
        List<ColumnAndType> columnAndTypes = new ArrayList<>();
        hasDeleteColumn = false;
        while (columnMatcher.find()) {
            String column = columnMatcher.group(1);
            if (DELETE_COLUMN_NAME.equals(column)) {
                hasDeleteColumn = true;
            }
            columnAndTypes.add(new ColumnAndType().setColumn(column).setType(columnMatcher.group(2)));
        }
        //添加注释,因为有的column没注释,所有要分开添加
        Pattern commentPattern = Pattern.compile("(?<=`)(\\w+)` +(int|tinyint|bigint|decimal|datetime|timestamp|float|double|char|varchar|text).*?COMMENT +'(.+)'");
        Matcher commentMatch = commentPattern.matcher(sql);
        while (commentMatch.find()){
            String column = commentMatch.group(1);
            for (ColumnAndType columnAndType:columnAndTypes){
                if (columnAndType.getColumn().equals(column)){
                    columnAndType.setComment(commentMatch.group(3));
                }
            }
        }
        Pattern idPattern = Pattern.compile("PRIMARY ?KEY ?\\(`(\\w+)`\\)");
        Matcher idMatcher = idPattern.matcher(sql);
        while (idMatcher.find()) {
            idName = idMatcher.group(1);
        }
        return columnAndTypes;
    }

    /**
     * 根据配置是否需要列名下划线转驼峰
     *
     * @param column 列名
     * @return 需要的列名
     */
    private static String setColumnIfHump(String column) {
        return IS_HUMP ? hump(column) : column;
    }


    /**
     * 下划线转驼峰
     *
     * @param str 传入的字符串
     * @return 下划线转驼峰后的字符串
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
     *
     * @param str 传入的字符串
     * @return 首字母大写后的字符串
     */
    private static String toUpperFirst(String str) {
        char[] chars = str.toCharArray();
        if (chars[0] >= 'a' && chars[0] <= 'z') {
            chars[0] = (char) (chars[0] - 32);
        }
        return new String(chars);
    }
}