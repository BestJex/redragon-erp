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
package com.erp.finance.voucher.service.spring;

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
import com.framework.util.JsonResultUtil;
import com.framework.util.ShiroUtil;

import redragon.basic.tools.TimeToolKit;
import redragon.frame.hibernate.SnowFlake;

import com.erp.finance.voucher.dao.FinVoucherModelHeadDao;
import com.erp.finance.voucher.dao.FinVoucherModelLineDao;
import com.erp.finance.voucher.dao.model.FinVoucherBillR;
import com.erp.finance.voucher.dao.model.FinVoucherHead;
import com.erp.finance.voucher.dao.model.FinVoucherLine;
import com.erp.finance.voucher.dao.model.FinVoucherModelHead;
import com.erp.finance.voucher.dao.model.FinVoucherModelHeadCO;
import com.erp.finance.voucher.dao.model.FinVoucherModelLine;
import com.erp.finance.voucher.service.FinVoucherBillRService;
import com.erp.finance.voucher.service.FinVoucherHeadService;
import com.erp.finance.voucher.service.FinVoucherLineService;
import com.erp.finance.voucher.service.FinVoucherModelHeadService;
import com.erp.hr.dao.model.HrStaffInfoRO;
import com.erp.hr.service.HrCommonService;

@Service
@Transactional(rollbackFor=Exception.class)
public class FinVoucherModelHeadServiceImpl implements FinVoucherModelHeadService {

    //注入Dao
    @Autowired
    private FinVoucherModelHeadDao finVoucherModelHeadDao;
    @Autowired
    private FinVoucherModelLineDao finVoucherModelLineDao;
    @Autowired
    private HrCommonService hrCommonService;
    @Autowired
    private FinVoucherHeadService finVoucherHeadService;
    @Autowired
    private FinVoucherLineService finVoucherLineService;
    @Autowired
    private FinVoucherBillRService finVoucherBillRService;
    
    @Override
    public void insertDataObject(FinVoucherModelHead obj) {
        this.finVoucherModelHeadDao.insertDataObject(obj);
    }

    @Override
    public void updateDataObject(FinVoucherModelHead obj) {
        this.finVoucherModelHeadDao.updateDataObject(obj);
    }
    
    @Override
    public void insertOrUpdateDataObject(FinVoucherModelHead obj) {
        this.finVoucherModelHeadDao.insertOrUpdateDataObject(obj);
    }

    @Override
    public void deleteDataObject(FinVoucherModelHead obj) {
        this.finVoucherModelHeadDao.deleteDataObject(obj);
    }

    @Override
    public List<FinVoucherModelHead> getDataObjects() {
        return this.finVoucherModelHeadDao.getDataObjects();
    }

    @Override
    public FinVoucherModelHead getDataObject(int id) {
        return this.finVoucherModelHeadDao.getDataObject(id);
    }

    @Override
    public FinVoucherModelHead getDataObject(String code) {
        return this.finVoucherModelHeadDao.getDataObject(code);
    }

    @Override
    public List<FinVoucherModelHead> getDataObjects(FinVoucherModelHeadCO paramObj) {
        return this.finVoucherModelHeadDao.getDataObjects(paramObj);
    }

    @Override
    public List<FinVoucherModelHead> getDataObjects(Pages pages) {
        return this.finVoucherModelHeadDao.getDataObjects(pages);
    }
    
    @Override
    public List<FinVoucherModelHead> getDataObjects(Pages pages, FinVoucherModelHeadCO paramObj) {
        return this.finVoucherModelHeadDao.getDataObjects(pages, paramObj);
    }
    
    @Override
    public List<Map<String, Object>> getDataObjectsArray(Pages pages, FinVoucherModelHeadCO paramObj) {
        return this.finVoucherModelHeadDao.getDataObjectsArray(pages, paramObj);
    }
    
    @Override
    public List<FinVoucherModelHead> getDataObjectsForDataAuth(String dataAuthSQL, Pages pages, FinVoucherModelHeadCO paramObj) {
        return this.finVoucherModelHeadDao.getDataObjectsForDataAuth(dataAuthSQL, pages, paramObj);
    }
    
    @Override
    public void insertOrUpdateFinVoucherModel(FinVoucherModelHead finVoucherModelHead, List<FinVoucherModelLine> finVoucherModelLineList) {
        //编辑头
        this.finVoucherModelHeadDao.insertOrUpdateDataObject(finVoucherModelHead);
        //循环编辑行
        for(FinVoucherModelLine finVoucherModelLine: finVoucherModelLineList) {
            this.finVoucherModelLineDao.insertOrUpdateDataObject(finVoucherModelLine);
        }
    }
    
    @Override
    public List<FinVoucherModelHead> getFinVoucherModelHeadListForCustom() {
        return this.finVoucherModelHeadDao.getFinVoucherModelHeadListForCustom();
    }
    
    @Override
    public List<String> getFinVoucherModelHeadForBusinessType() {
        return this.finVoucherModelHeadDao.getFinVoucherModelHeadForBusinessType();
    }
    
    @Override
    public FinVoucherModelHead getFinVoucherModelHeadByBusinessType(String businessType) {
        return this.finVoucherModelHeadDao.getFinVoucherModelHeadByBusinessType(businessType);
    }
    
    @Override
    public String autoCreateVoucher(String billHeadCode, Double amount, String businessType) {
        //获取凭证模板头
        FinVoucherModelHead finVoucherModelHead = this.finVoucherModelHeadDao.getFinVoucherModelHeadByBusinessType(businessType);
        
        //获取凭证模板头
        if(finVoucherModelHead!=null) {
           List<FinVoucherModelLine> finVoucherModelLineList = this.finVoucherModelLineDao.getFinVoucherModelLineListByVoucherHeadCode(finVoucherModelHead.getVoucherHeadCode());
           
           if(finVoucherModelLineList.size()>0) {
               //设置凭证头
               FinVoucherHead finVoucherHead = new FinVoucherHead();
               finVoucherHead.setBillNum(0);
               TimeToolKit time = new TimeToolKit();
               finVoucherHead.setVoucherDate(time.getJavaDate());
               String code = SnowFlake.generateId().toString();
               finVoucherHead.setVoucherHeadCode(code);
               finVoucherHead.setVoucherType(finVoucherModelHead.getVoucherType());
               //bbc凭证编号临时处理（不能采用雪花算法，各企业算法可能不同）
               finVoucherHead.setVoucherNumber(code);
               //获取当前用户职员信息
               HrStaffInfoRO staffInfo = this.hrCommonService.getStaffInfo(ShiroUtil.getUsername());
               finVoucherHead.setStaffCode(staffInfo.getStaffCode());
               finVoucherHead.setDepartmentCode(staffInfo.getDepartmentCode());
               //插入凭证头
               this.finVoucherHeadService.insertDataObject(finVoucherHead);
               
               //循环设置凭证行
               for(FinVoucherModelLine finVoucherModelLine: finVoucherModelLineList) {
                   FinVoucherLine finVoucherLine = new FinVoucherLine();
                   //金额设置
                   if(finVoucherModelLine.getDrAmount().equals("AMOUNT")) {
                       finVoucherLine.setDrAmount(amount);
                       finVoucherLine.setCrAmount(0D);
                   }else if(finVoucherModelLine.getCrAmount().equals("AMOUNT")) {
                       finVoucherLine.setCrAmount(amount);
                       finVoucherLine.setDrAmount(0D);
                   }
                   
                   finVoucherLine.setMemo(finVoucherModelLine.getMemo());
                   finVoucherLine.setSubjectCode(finVoucherModelLine.getSubjectCode());
                   finVoucherLine.setVoucherHeadCode(finVoucherHead.getVoucherHeadCode());
                   finVoucherLine.setVoucherLineCode(SnowFlake.generateId().toString());
                   //插入凭证行
                   this.finVoucherLineService.insertDataObject(finVoucherLine);
               }
               
               //记录凭证和单据的关联
               FinVoucherBillR finVoucherBillR = new FinVoucherBillR();
               finVoucherBillR.setBillType(businessType);
               finVoucherBillR.setVoucherHeadCode(finVoucherHead.getVoucherHeadCode());
               finVoucherBillR.setBillHeadCode(billHeadCode);
               //插入关联
               this.finVoucherBillRService.insertDataObject(finVoucherBillR);
               
               //返回json数据
               return "{\"errCode\":0, \"errMsg\": \"\", \"voucherHeadId\": "+finVoucherHead.getVoucherHeadId()+", \"voucherHeadCode\": \""+finVoucherHead.getVoucherHeadCode()+"\"}";
           }
        }else {
            return JsonResultUtil.getErrorJson(-1, "请先创建自动凭证模板");
        }
        
        return JsonResultUtil.getErrorJson(-1, "自动生成凭证执行错误");
    }
    
}