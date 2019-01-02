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
    
    
    

//动态放入到数据库
public Boolean putInteract(InteractBean interact){
	
	String interactSql = "insert into interact(user_name,user_touxiang,interact_time,"
			+ "interact_content,interact_photo,interat_praise)values('"+interact.getUserName()+"',"
					+ "'"+interact.getUserTouxiang()+ "','"+interact.getInteractTime()+"',"
							+ "'"+interact.getInteractContent()+ "','"+interact.getInteractPhoto()+"',"
									+ "'"+interact.getInteractPraise()+"')";

    // 获取DB对象
    DBManager sql = DBManager.createInstance();
    sql.connectDB();

    int ret = sql.executeUpdate(interactSql);
    if (ret != 0) {
        sql.closeDB();
        return true;
    }
    sql.closeDB();
    
    return false;
}
//查询动态界面信息
public List<InteractBean> interactSelect() {

	// 获取Sql查询语句

	List<InteractBean> interacts = new ArrayList<InteractBean>();

	String interactSql = "select * from interact";

	// 获取DB对象
	DBManager sql = DBManager.createInstance();
	sql.connectDB();

	// 操作DB对象
	try {
		ResultSet rs = sql.executeQuery(interactSql);
		while (rs.next()) {
			InteractBean interact = new InteractBean();
			interact.setInteactId(rs.getInt("interact_id"));
			interact.setUserName(rs.getString("user_name"));
			interact.setUserTouxiang(rs.getString("user_touxiang"));
			interact.setInteractTime(rs.getString("interact_time"));
			interact.setInteractContent(rs.getString("interact_content"));
			interact.setInteractPhoto(rs.getString("interact_photo"));
			interact.setInteractPraise(rs.getString("interact_praise"));
			interacts.add(interact);
		}
	} catch (SQLException e) {
		e.printStackTrace();
	}
	return interacts;
}



}
