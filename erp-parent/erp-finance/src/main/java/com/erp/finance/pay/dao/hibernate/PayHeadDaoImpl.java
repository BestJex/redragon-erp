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
package com.erp.finance.pay.dao.hibernate;

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
import com.erp.finance.pay.dao.PayHeadDao;
import com.erp.finance.pay.dao.model.PayHead;
import com.erp.finance.pay.dao.model.PayHeadCO;

@Repository
public class PayHeadDaoImpl implements PayHeadDao{ 

    //注入DaoSupport工具类
    @Autowired
    private DaoSupport daoSupport;
    
    @Override
    public void insertDataObject(PayHead obj) {
        this.daoSupport.insertDataTransaction(obj);
    }

    @Override
    public void updateDataObject(PayHead obj) {
        this.daoSupport.updateDataTransaction(obj);
    }
    
    @Override
    public void insertOrUpdateDataObject(PayHead obj) {
        this.daoSupport.insertOrUpdateDataTransaction(obj);
    }

    @Override
    public void deleteDataObject(PayHead obj) {
        this.daoSupport.deleteDataTransactionJPA(obj);
    }

    @Override
    public List<PayHead> getDataObjects() {
        return this.daoSupport.getDataAllObject(PayHead.class);
    }

    @Override
    public PayHead getDataObject(int id) {
        return (PayHead)this.daoSupport.getDataObject(PayHead.class, id);
    }
    
    @Override
    public PayHead getDataObject(String code) {
        String sql = "select p.* from pay_head p where p.pay_head_code = :code";
        
        Map<String, Object> args = new HashMap<String, Object>();
        args.put("code", code);
        
        Map<String, Class<?>> entity = new HashMap<String, Class<?>>();
        entity.put("p", PayHead.class);
        
        List<PayHead> list = this.daoSupport.selectDataSql(sql, entity, args);
        if(list.size()>0) {
            return list.get(0);
        }
        
        return null;
    }
    
    @Override
    public List<PayHead> getDataObjects(PayHeadCO paramObj) {
        return null;
    }
    
    @Override
    public List<PayHead> getDataObjects(Pages pages) {
        return null;
    }
    
    @Override
    public List<PayHead> getDataObjects(Pages pages, PayHeadCO paramObj) {
        String sql = "select p.* from pay_head p where 1=1";
        
        Map<String, Object> args = new HashMap<String, Object>();
        sql = sql + DaoUtil.getSQLCondition(paramObj, "payHeadCode", "and p.", args);
        sql = sql + DaoUtil.getSQLCondition(paramObj, "paySourceType", "and p.", args);
        sql = sql + DaoUtil.getSQLCondition(paramObj, "paySourceHeadCode", "and p.", args);
        sql = sql + DaoUtil.getSQLCondition(paramObj, "receiver", "and p.", args);
        sql = sql + DaoUtil.getSQLCondition(paramObj, "prepayFlag", "and p.", args);
        sql = sql + DaoUtil.getSQLCondition(paramObj, "status", "and p.", args);
        sql = sql + " order by p.pay_head_id desc";
        
        Map<String, Class<?>> entity = new HashMap<String, Class<?>>();
        entity.put("p", PayHead.class);
        
        return this.daoSupport.getDataSqlByPage(sql, entity, args, pages);
    }

    @Override
    public List<Map<String, Object>> getDataObjectsArray(Pages pages, PayHeadCO paramObj) {
        return null;
    }
    
    @Override
    @Permissions(PermissionType.OWN)
    public List<PayHead> getDataObjectsForDataAuth(@SqlParam String dataAuthSQL, Pages pages, PayHeadCO paramObj) {
        return null;
    }
    
    @Override
    public List<PayHead> getPayHeadListForNotCreateVoucher(Pages pages, PayHeadCO paramObj) {
        String sql = "select p.* from pay_head p where 1=1";
        
        Map<String, Object> args = new HashMap<String, Object>();
        sql = sql + DaoUtil.getSQLCondition(paramObj, "payHeadCode", "and p.", args);
        sql = sql + DaoUtil.getSQLCondition(paramObj, "paySourceType", "and p.", args);
        sql = sql + DaoUtil.getSQLCondition(paramObj, "paySourceHeadCode", "and p.", args);
        sql = sql + DaoUtil.getSQLCondition(paramObj, "receiver", "and p.", args);
        sql = sql + DaoUtil.getSQLCondition(paramObj, "prepayFlag", "and p.", args);
        sql = sql + DaoUtil.getSQLCondition(paramObj, "status", "and p.", args);
        
        sql = sql + " and not exists (select 1 from fin_voucher_bill_r where bill_type = 'PAY' and bill_head_code = p.pay_head_code)";
        sql = sql + " order by p.pay_head_id desc";
        
        Map<String, Class<?>> entity = new HashMap<String, Class<?>>();
        entity.put("p", PayHead.class);
        
        return this.daoSupport.getDataSqlByPage(sql, entity, args, pages);
    }
    
    @Override
    public void updateApproveStatus(String code, String approveStatus) {
        String sql = "update pay_head set approve_status = :approveStatus where pay_head_code = :code";
        
        Map<String, Object> args = new HashMap<String, Object>();
        args.put("code", code);
        args.put("approveStatus", approveStatus);
        
        this.daoSupport.executeSQLTransaction(sql, args);
    }
    
    @Override
    public int getPayHeadNum(String startDate, String endDate) {
        String sql = "select count(*) from pay_head where created_date between :startDate and :endDate";
        
        Map<String, Object> args = new HashMap<String, Object>();
        args.put("startDate", startDate);
        args.put("endDate", endDate);
        
        List list = this.daoSupport.selectDataSqlCount(sql, args);
        if(list.size()>0) {
            return this.daoSupport.convertSQLCount(list.get(0));
        }
        
        return 0;
    }
    
}
