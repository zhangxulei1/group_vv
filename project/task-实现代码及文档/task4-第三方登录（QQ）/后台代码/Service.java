package service;

import java.sql.Connection;
import bean.InteractBean;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import db.DBManager;

public class Service {
	public Boolean login(String userphone, String password) {

        // 获取Sql查询语句
		
		
		
        String logSql = "select * from users where user_phone= '" + userphone +  "' and user_psw= '" + password + "'";

        // 获取DB对象
        DBManager sql = DBManager.createInstance();
        sql.connectDB();

        // 操作DB对象
        try {
            ResultSet rs = sql.executeQuery(logSql);
            if (rs.next()) {
                sql.closeDB();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        sql.closeDB();
        return false;
    }

    public Boolean register(String username, String password,String phone) {
    	
    	   
    	// 获取Sql查询语句
        String regSql = "insert into users(user_name,user_psw,user_phone) "
        		+ "values('"+ username+ "','"+ password+ "','"+ phone+ "') ";

        // 获取DB对象
        DBManager sql = DBManager.createInstance();
        sql.connectDB();

        int ret = sql.executeUpdate(regSql);
        if (ret != 0) {
            sql.closeDB();
            return true;
        }
        sql.closeDB();
        
        return false;
    }

}
