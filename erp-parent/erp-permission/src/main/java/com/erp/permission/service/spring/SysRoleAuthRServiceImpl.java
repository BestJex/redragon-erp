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
package com.erp.permission.service.spring;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.framework.annotation.Cache;
import com.framework.annotation.Cache.CacheType;
import com.framework.annotation.Log;
import com.framework.dao.model.Pages;
import com.erp.permission.dao.SysRoleAuthRDao;
import com.erp.permission.dao.model.SysRoleAuthR;
import com.erp.permission.dao.model.SysRoleAuthRCO;
import com.erp.permission.service.SysRoleAuthRService;

@Service
@Transactional(rollbackFor=Exception.class)
public class SysRoleAuthRServiceImpl implements SysRoleAuthRService {

    //注入Dao
    @Autowired
    private SysRoleAuthRDao sysRoleAuthRDao;
    
    @Override
    public void insertDataObject(SysRoleAuthR obj) {
        this.sysRoleAuthRDao.insertDataObject(obj);
    }

    @Override
    public void updateDataObject(SysRoleAuthR obj) {
        this.sysRoleAuthRDao.updateDataObject(obj);
    }
    
    @Override
    public void insertOrUpdateDataObject(SysRoleAuthR obj) {
        this.sysRoleAuthRDao.insertOrUpdateDataObject(obj);
    }

    @Override
    public void deleteDataObject(SysRoleAuthR obj) {
        this.sysRoleAuthRDao.deleteDataObject(obj);
    }

    @Override
    public List<SysRoleAuthR> getDataObjects() {
        return this.sysRoleAuthRDao.getDataObjects();
    }

    @Override
    public SysRoleAuthR getDataObject(int id) {
        return this.sysRoleAuthRDao.getDataObject(id);
    }

    @Override
    public SysRoleAuthR getDataObject(String code) {
        return this.sysRoleAuthRDao.getDataObject(code);
    }

    @Override
    public List<SysRoleAuthR> getDataObjects(SysRoleAuthRCO paramObj) {
        return this.sysRoleAuthRDao.getDataObjects(paramObj);
    }

    @Override
    public List<SysRoleAuthR> getDataObjects(Pages pages) {
        return this.sysRoleAuthRDao.getDataObjects(pages);
    }
    
    @Override
    public List<SysRoleAuthR> getDataObjects(Pages pages, SysRoleAuthRCO paramObj) {
        return this.sysRoleAuthRDao.getDataObjects(pages, paramObj);
    }
    
    @Override
    public List<Map<String, Object>> getDataObjectsArray(Pages pages, SysRoleAuthRCO paramObj) {
        return this.sysRoleAuthRDao.getDataObjectsArray(pages, paramObj);
    }
    
    @Override
    public List<SysRoleAuthR> getDataObjectsForDataAuth(String dataAuthSQL, Pages pages, SysRoleAuthRCO paramObj) {
        return this.sysRoleAuthRDao.getDataObjectsForDataAuth(dataAuthSQL, pages, paramObj);
    }
    
    @Override
    public void deleteSysRoleAuthRByRoleCode(String roleCode) {
        this.sysRoleAuthRDao.deleteSysRoleAuthRByRoleCode(roleCode);
    }
    
}