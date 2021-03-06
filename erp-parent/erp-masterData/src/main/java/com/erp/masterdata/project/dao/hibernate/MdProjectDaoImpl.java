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
package com.erp.masterdata.project.dao.hibernate;

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
import com.erp.masterdata.project.dao.MdProjectDao;
import com.erp.masterdata.project.dao.model.MdProject;
import com.erp.masterdata.project.dao.model.MdProjectCO;

@Repository
public class MdProjectDaoImpl implements MdProjectDao{ 

    //注入DaoSupport工具类
    @Autowired
    private DaoSupport daoSupport;
    
    @Override
    public void insertDataObject(MdProject obj) {
        this.daoSupport.insertDataTransaction(obj);
    }

    @Override
    public void updateDataObject(MdProject obj) {
        this.daoSupport.updateDataTransaction(obj);
    }
    
    @Override
    public void insertOrUpdateDataObject(MdProject obj) {
        this.daoSupport.insertOrUpdateDataTransaction(obj);
    }

    @Override
    public void deleteDataObject(MdProject obj) {
        this.daoSupport.deleteDataTransactionJPA(obj);
    }

    @Override
    public List<MdProject> getDataObjects() {
        return this.daoSupport.getDataAllObject(MdProject.class);
    }

    @Override
    public MdProject getDataObject(int id) {
        return (MdProject)this.daoSupport.getDataObject(MdProject.class, id);
    }
    
    @Override
    public MdProject getDataObject(String code) {
        return null;
    }
    
    @Override
    public List<MdProject> getDataObjects(MdProjectCO paramObj) {
        return null;
    }
    
    @Override
    public List<MdProject> getDataObjects(Pages pages) {
        return null;
    }
    
    @Override
    public List<MdProject> getDataObjects(Pages pages, MdProjectCO paramObj) {
        String sql = "select p.* from md_project p where 1=1";
        
        Map<String, Object> args = new HashMap<String, Object>();
        sql = sql + DaoUtil.getSQLCondition(paramObj, "projectCode", "and p.", args);
        sql = sql + DaoUtil.getSQLCondition(paramObj, "projectName", "and p.", args);
        sql = sql + DaoUtil.getSQLCondition(paramObj, "projectType", "and p.", args);
        sql = sql + DaoUtil.getSQLCondition(paramObj, "status", "and p.", args);
        sql = sql + " order by p.project_id desc";
        
        Map<String, Class<?>> entity = new HashMap<String, Class<?>>();
        entity.put("p", MdProject.class);
        
        return this.daoSupport.getDataSqlByPage(sql, entity, args, pages);
    }

    @Override
    public List<Map<String, Object>> getDataObjectsArray(Pages pages, MdProjectCO paramObj) {
        return null;
    }
    
    @Override
    @Permissions(PermissionType.OWN)
    public List<MdProject> getDataObjectsForDataAuth(@SqlParam String dataAuthSQL, Pages pages, MdProjectCO paramObj) {
        return null;
    }
    
    @Override
    public void updateApproveStatus(int id, String approveStatus) {
        String sql = "update md_project set approve_status = :approveStatus where project_id = :id";
        
        Map<String, Object> args = new HashMap<String, Object>();
        args.put("id", id);
        args.put("approveStatus", approveStatus);
        
        this.daoSupport.executeSQLTransaction(sql, args);
    }
    
}
