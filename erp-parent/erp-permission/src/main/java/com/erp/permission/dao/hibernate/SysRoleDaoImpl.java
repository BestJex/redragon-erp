/*
 * Copyright 2020-2021 redragon.dongbin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.erp.permission.dao.hibernate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import com.framework.annotation.Cache;
import com.framework.annotation.Permissions;
import com.framework.annotation.Permissions.PermissionType;
import com.framework.annotation.SqlParam;
import com.framework.dao.DaoSupport;
import com.framework.dao.model.Pages;
import com.framework.util.DaoUtil;
import com.erp.permission.dao.SysRoleDao;
import com.erp.permission.dao.model.SysRole;
import com.erp.permission.dao.model.SysRoleCO;

@Repository
public class SysRoleDaoImpl implements SysRoleDao{ 

    //注入DaoSupport工具类
    @Autowired
    private DaoSupport daoSupport;
    
    @Override
    public void insertDataObject(SysRole obj) {
        this.daoSupport.insertDataTransaction(obj);
    }

    @Override
    public void updateDataObject(SysRole obj) {
        this.daoSupport.updateDataTransaction(obj);
    }
    
    @Override
    public void insertOrUpdateDataObject(SysRole obj) {
        this.daoSupport.insertOrUpdateDataTransaction(obj);
    }

    @Override
    public void deleteDataObject(SysRole obj) {
        this.daoSupport.deleteDataTransactionJPA(obj);
    }

    @Override
    public List<SysRole> getDataObjects() {
        return this.daoSupport.getDataAllObject(SysRole.class);
    }

    @Override
    public SysRole getDataObject(int id) {
        return (SysRole)this.daoSupport.getDataObject(SysRole.class, id);
    }
    
    @Override
    public SysRole getDataObject(String code) {
        return null;
    }
    
    @Override
    public List<SysRole> getDataObjects(SysRoleCO paramObj) {
        return null;
    }
    
    @Override
    public List<SysRole> getDataObjects(Pages pages) {
        return null;
    }
    
    @Override
    public List<SysRole> getDataObjects(Pages pages, SysRoleCO paramObj) {
        String sql = "select r.* from sys_role r where 1=1";
        
        Map<String, Object> args = new HashMap<String, Object>();
        sql = sql + DaoUtil.getSQLCondition(paramObj, "roleCode", "and r.", args);
        sql = sql + DaoUtil.getSQLCondition(paramObj, "roleName", "and r.", args);
        sql = sql + DaoUtil.getSQLCondition(paramObj, "status", "and r.", args);
        sql = sql + " order by r.role_id desc";
        
        Map<String, Class<?>> entity = new HashMap<String, Class<?>>();
        entity.put("r", SysRole.class);
        
        return this.daoSupport.getDataSqlByPage(sql, entity, args, pages);
    }

    @Override
    public List<Map<String, Object>> getDataObjectsArray(Pages pages, SysRoleCO paramObj) {
        return null;
    }
    
    @Override
    @Permissions(PermissionType.OWN)
    public List<SysRole> getDataObjectsForDataAuth(@SqlParam String dataAuthSQL, Pages pages, SysRoleCO paramObj) {
        return null;
    }
    
    @Override
    public List<SysRole> getSysRoleListByStatus(String status) {
        String sql = "select r.* from sys_role r where r.status = :status order by r.role_code";
        
        Map<String, Object> args = new HashMap<String, Object>();
        args.put("status", status);
        
        Map<String, Class<?>> entity = new HashMap<String, Class<?>>();
        entity.put("r", SysRole.class);
        
        return this.daoSupport.selectDataSql(sql, entity, args);
    }
    
    @Override
    public List<SysRole> getRelateSysRoleListByUsername(String username) {
        String sql = "select r.* from sys_user_role_r ur,sys_role r where ur.role_code=r.role_code and ur.username=:username order by r.role_code";
        
        Map<String, Object> args = new HashMap<String, Object>();
        args.put("username", username);
        
        Map<String, Class<?>> entity = new HashMap<String, Class<?>>();
        entity.put("r", SysRole.class);
        
        return this.daoSupport.selectDataSql(sql, entity, args);
    }
    
    @Override
    public boolean isExistRelateDataForSysRole(String roleCode) {
        String sql = "select (select count(*) from sys_role_auth_r ra where ra.role_code = :roleCode)+(select count(*) from sys_user_role_r ur where ur.role_code = :roleCode) as num from dual";
        
        Map<String, Object> args = new HashMap<String, Object>();
        args.put("roleCode", roleCode);
        
        List list = this.daoSupport.selectDataSqlCount(sql, args);
        if(list.size()>0) {
            int num = this.daoSupport.convertSQLCount(list.get(0));
            if(num==0) {
                return false;
            }
        }
        
        return true;
    }
    
}
