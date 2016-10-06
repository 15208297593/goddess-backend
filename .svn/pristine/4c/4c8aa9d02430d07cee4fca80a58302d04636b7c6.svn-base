package com.goddess.ec.manage.model;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.jfinal.plugin.activerecord.ActiveRecordException;
import com.jfinal.plugin.activerecord.Config;
import com.jfinal.plugin.activerecord.DbKit;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Table;
import com.jfinal.plugin.activerecord.TableMapping;
import com.goddess.ec.manage.tools.ToolDateTime;
import com.goddess.ec.manage.tools.ToolJson;
import com.goddess.ec.manage.tools.ToolSqlXml;
import com.goddess.ec.manage.tools.ToolUtils;

/**
 * Model基础类
 */
public abstract class BaseModel<M extends Model<M>> extends Model<M> {

    private static final long serialVersionUID = -900378319414539856L;

    private static Logger log = Logger.getLogger(BaseModel.class);

    /**
     * 根据i18n参数查询获取哪个字段的值
     * @param i18n
     * @return
     */
    public static String i18n(String i18n) {
        String val = null;
        if (i18n.equals("zh") || i18n.equals("zh_cn")) {
            val = "_zhcn";

        } else if (i18n.equals("en") || i18n.equals("en_us")) {
            val = "_enus";

        } else if (i18n.equals("ja") || i18n.equals("ja_jp")) {
            val = "_ja";

        } else if (i18n.equals("zh_hk")) {
            val = "_zhhk";

        } else if (i18n.equals("zh_tw")) {
            val = "_zhtw";

        }
        return val;
    }

    /**
     * 获取表映射对象
     * @return
     */
    public Table getTable() {
        return TableMapping.me().getTable(getClass());
    }

    /**
     * 获取主键值
     * @return
     */
    public String getPrimaryKeyValue() {
        return this.getStr(getTable().getPrimaryKey()[0]);
    }

    /**
     * 重写save方法
     */
    public boolean save() {
    	
		if (this.getTable().getColumnType(getPrimaryKeyValue()) == String.class) {
			this.set(getPrimaryKeyValue(), ToolUtils.getUuidByJdk(true));
		}
        
        if (getTable().hasColumnLabel("delete_flag")) {
            this.set("DELETE_FLAG", "0");
        }
        // TODO
        if (getTable().hasColumnLabel("create_user_id")) {
            this.set("CREATE_USER_ID", "0");
            this.set("CREATE_DATE", ToolDateTime.getNow());
        }
        if (getTable().hasColumnLabel("update_user_id")) {
            this.set("UPDATE_USER_ID", "0");
            this.set("UPDATE_DATE", ToolDateTime.getNow());
        }

        return super.save();
    }

    /**
     * 重写save方法
     */
    public int saveToGetAI() {
        boolean successFlg = super.save();
        if (!successFlg) {
            // 失败的时候，返回-1
            return -1;
        }
        return findFirst("SELECT LAST_INSERT_ID() AS ID").getInt("ID");
    }
//
//    /**
//     * 重写update方法
//     */
//    @SuppressWarnings("unchecked")
//    public boolean update() {
//        Table table = getTable();
//        String name = table.getName();
//        String pk = table.getPrimaryKey();
//
//        // 1.数据是否还存在
//        Map<String, Object> param = new HashMap<String, Object>();
//        param.put("table", name);
//        param.put("pk", pk);
//        String sql = ToolSqlXml.getSql("pf.baseModel.version", param);
//        Model<M> modelOld = findFirst(sql, getStr("ids"));
//        if (null == modelOld) { // 数据已经被删除
//            throw new RuntimeException("数据库中此数据不存在，可能数据已经被删除，请刷新数据后在操作");
//        }
//
//        // 2.乐观锁控制
//        Set<String> modifyFlag = null;
//        try {
//            Field field = this.getClass().getSuperclass().getSuperclass().getDeclaredField("modifyFlag");
//            field.setAccessible(true);
//            Object object = field.get(this);
//            if (null != object) {
//                modifyFlag = (Set<String>) object;
//            }
//            field.setAccessible(false);
//        } catch (NoSuchFieldException | SecurityException e) {
//            log.error("业务Model类必须继承BaseModel");
//            e.printStackTrace();
//            throw new RuntimeException("业务Model类必须继承BaseModel");
//        } catch (IllegalArgumentException | IllegalAccessException e) {
//            log.error("BaseModel访问modifyFlag异常");
//            e.printStackTrace();
//            throw new RuntimeException("BaseModel访问modifyFlag异常");
//        }
//        boolean versionModify = modifyFlag.contains("version");
//        if (versionModify && getTable().hasColumnLabel("version")) { // 是否需要乐观锁控制
//            Long versionDB = modelOld.getNumber("version").longValue(); // 数据库中的版本号
//            Long versionForm = getNumber("version").longValue(); // 表单中的版本号
//            if (!(versionForm > versionDB)) {
//                throw new RuntimeException("表单数据版本号和数据库数据版本号不一致，可能数据已经被其他人修改，请重新编辑");
//            }
//        }
//
//        return super.update();
//    }

    /**
     * execute sql.
     */
    private boolean execute(Connection conn, String sql, Object... paras) throws Exception {
        Config config = DbKit.getConfig(getClass());

        PreparedStatement pst = conn.prepareStatement(sql);
        config.getDialect().fillStatement(pst, paras);
        boolean result = pst.execute();
        closeQuietly(pst);
        return result;
    }

    public final void closeQuietly(Statement st) {
        if (st != null) {
            try {
                st.close();
            } catch (SQLException e) {
            }
        }
    }

    /**
     * execute sql
     * @param sql an SQL statement that may contain one or more '?' IN parameter placeholders
     * @param paras the parameters of sql
     * @return the list of Model
     */
    public boolean execute(String sql, Object... paras) {
        Config config = DbKit.getConfig(getClass());
        Connection conn = null;
        try {
            conn = config.getConnection();
            return execute(conn, sql, paras);
        } catch (Exception e) {
            throw new ActiveRecordException(e);
        } finally {
            config.close(conn);
        }
    }

    /**
     * 根据条件删除数据
     * @param sql
     * @param paras
     * @return
     */
    public boolean delete(String sql, Object... paras) {
        return !execute(sql, paras);
    }

    @SuppressWarnings("unchecked")
    public Model<M> fromJson(String json) {
        return ToolJson.convertFromJson(json, getClass());
    }

}
