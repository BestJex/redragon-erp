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
package com.erp.masterdata.vendor.dao.hibernate;

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
import com.erp.masterdata.customer.dao.model.MdCustomerBank;
import com.erp.masterdata.vendor.dao.MdVendorBankDao;
import com.erp.masterdata.vendor.dao.model.MdVendorBank;
import com.erp.masterdata.vendor.dao.model.MdVendorBankCO;

@Repository
public class MdVendorBankDaoImpl implements MdVendorBankDao{ 

    //注入DaoSupport工具类
    @Autowired
    private DaoSupport daoSupport;
    
    @Override
    public void insertDataObject(MdVendorBank obj) {
        this.daoSupport.insertDataTransaction(obj);
    }

    @Override
    public void updateDataObject(MdVendorBank obj) {
        this.daoSupport.updateDataTransaction(obj);
    }
    
    @Override
    public void insertOrUpdateDataObject(MdVendorBank obj) {
        this.daoSupport.insertOrUpdateDataTransaction(obj);
    }

    @Override
    public void deleteDataObject(MdVendorBank obj) {
        this.daoSupport.deleteDataTransactionJPA(obj);
    }

    @Override
    public List<MdVendorBank> getDataObjects() {
        return this.daoSupport.getDataAllObject(MdVendorBank.class);
    }

    @Override
    public MdVendorBank getDataObject(int id) {
        return (MdVendorBank)this.daoSupport.getDataObject(MdVendorBank.class, id);
    }
    
    @Override
    public MdVendorBank getDataObject(String code) {
        return null;
    }
    
    @Override
    public List<MdVendorBank> getDataObjects(MdVendorBankCO paramObj) {
        return null;
    }
    
    @Override
    public List<MdVendorBank> getDataObjects(Pages pages) {
        return null;
    }
    
    @Override
    public List<MdVendorBank> getDataObjects(Pages pages, MdVendorBankCO paramObj) {
        return null;
    }

    @Override
    public List<Map<String, Object>> getDataObjectsArray(Pages pages, MdVendorBankCO paramObj) {
        return null;
    }
    
    @Override
    @Permissions(PermissionType.OWN)
    public List<MdVendorBank> getDataObjectsForDataAuth(@SqlParam String dataAuthSQL, Pages pages, MdVendorBankCO paramObj) {
        return null;
    }
    
    @Override
    public int getBankCountByVendorCode(String vendorCode) {
        String sql = "select count(1) from md_vendor_bank b where b.vendor_code = :vendorCode";
        
        Map<String, Object> args = new HashMap<String, Object>();
        args.put("vendorCode", vendorCode);
        
        List list = this.daoSupport.selectDataSqlCount(sql, args);
        if(list.size()>0) {
            return Integer.parseInt(String.valueOf(list.get(0)));
        }
        
        return 0;
    }
    
    @Override
    public List<MdVendorBank> getBankListByVendorCode(Pages pages, MdVendorBankCO paramObj) {
        String sql = "select b.* from md_vendor_bank b where vendor_code=:code";
        
        Map<String, Object> args = new HashMap<String, Object>();
        args.put("code", paramObj.getVendorCode());
        
        sql = sql + " order by b.bank_id desc";
        
        Map<String, Class<?>> entity = new HashMap<String, Class<?>>();
        entity.put("b", MdVendorBank.class);
        
        return this.daoSupport.getDataSqlByPage(sql, entity, args, pages);
    }
    
}
