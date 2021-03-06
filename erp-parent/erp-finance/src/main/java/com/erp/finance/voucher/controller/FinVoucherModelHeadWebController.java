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
package com.erp.finance.voucher.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.framework.controller.ControllerSupport;
import com.framework.dao.model.Pages;
import com.framework.util.JsonResultUtil;
import com.framework.util.JsonUtil;
import com.framework.util.ShiroUtil;
import com.springboot.dao.data.GlobalDataBox;

import redragon.basic.tools.TimeToolKit;
import redragon.frame.hibernate.SnowFlake;

import com.erp.dataset.service.DatasetCommonService;
import com.erp.finance.pay.dao.model.PayHead;
import com.erp.finance.pay.dao.model.PayHeadCO;
import com.erp.finance.pay.service.PayHeadService;
import com.erp.finance.pay.service.PayLineService;
import com.erp.finance.receipt.dao.model.ReceiptHead;
import com.erp.finance.receipt.dao.model.ReceiptHeadCO;
import com.erp.finance.receipt.service.ReceiptHeadService;
import com.erp.finance.receipt.service.ReceiptLineService;
import com.erp.finance.voucher.dao.data.DataBox;
import com.erp.finance.voucher.dao.model.FinVoucherHead;
import com.erp.finance.voucher.dao.model.FinVoucherLine;
import com.erp.finance.voucher.dao.model.FinVoucherModelHead;
import com.erp.finance.voucher.dao.model.FinVoucherModelHeadCO;
import com.erp.finance.voucher.dao.model.FinVoucherModelLine;
import com.erp.finance.voucher.service.FinVoucherHeadService;
import com.erp.finance.voucher.service.FinVoucherLineService;
import com.erp.finance.voucher.service.FinVoucherModelHeadService;
import com.erp.finance.voucher.service.FinVoucherModelLineService;
import com.erp.hr.dao.model.HrStaffInfoRO;
import com.erp.hr.service.HrCommonService;
import com.erp.masterdata.common.service.MasterDataCommonService;
import com.erp.order.po.dao.model.PoHead;
import com.erp.order.po.dao.model.PoHeadCO;

@Controller
@RequestMapping("/web/finVoucherModelHead")
public class FinVoucherModelHeadWebController extends ControllerSupport{
    
    //日志处理
    private Logger logger = LoggerFactory.getLogger(FinVoucherModelHeadWebController.class);
    
    //服务层注入
    @Autowired
    private FinVoucherModelHeadService finVoucherModelHeadService;
    @Autowired
    private FinVoucherModelLineService finVoucherModelLineService;
    @Autowired
    private DatasetCommonService datasetCommonService;
    @Autowired
    private HrCommonService hrCommonService;
    @Autowired
    private MasterDataCommonService masterDataCommonService;
    @Autowired
    private PayHeadService payHeadService;
    @Autowired
    private PayLineService payLineService;
    @Autowired
    private ReceiptHeadService receiptHeadService;
    @Autowired
    private ReceiptLineService receiptLineService;
    
    @Override
    public String getExceptionRedirectURL() {
        return "redirect:getFinVoucherModelHeadList";
    }
    
    
    
    /**
     * 
     * @description 查询数据列表
     * @date 2020-07-28 13:05:47
     * @author 
     * @param pages
     * @param finVoucherModelHeadCO
     * @param model
     * @return String
     *
     */
    @RequestMapping("getFinVoucherModelHeadList")
    public String getFinVoucherModelHeadList(Pages pages, FinVoucherModelHeadCO finVoucherModelHeadCO, Model model) {
        //分页查询数据
        if(pages.getPage()==0) {
            pages.setPage(1);
        }
        
        //分页查询数据
        List<FinVoucherModelHead> finVoucherHeadList = this.finVoucherModelHeadService.getDataObjects(pages, finVoucherModelHeadCO);
        
        //循环设置职员和组织信息
        for(FinVoucherModelHead finVoucherHead: finVoucherHeadList) {
            finVoucherHead.setStaffName(this.hrCommonService.getHrStaff(finVoucherHead.getStaffCode()).getStaffName());
            finVoucherHead.setDepartmentName(this.hrCommonService.getHrDepartment(finVoucherHead.getDepartmentCode()).getDepartmentName());
        }
        
        //获取凭证字
        Map voucherTypeMap = this.datasetCommonService.getVoucherType();
        //获取凭证业务类型
        Map voucherBusinessTypeMap = DataBox.getVoucherBusinessType();
        
        //页面属性设置
        model.addAttribute("finVoucherHeadList", finVoucherHeadList);
        model.addAttribute("pages", pages);
        model.addAttribute("voucherTypeMap", voucherTypeMap);
        model.addAttribute("voucherBusinessTypeMap", voucherBusinessTypeMap);
        
        return "basic.jsp?content=finVoucher/voucherModelList";
    }
    
    
    
    /**
     * 
     * @description 查询单条数据
     * @date 2020-07-28 13:05:47
     * @author 
     * @param finVoucherModelHead
     * @param model
     * @return String
     *
     */
    @RequestMapping("getFinVoucherModelHead")
    public String getFinVoucherModelHead(FinVoucherModelHead finVoucherModelHead, Model model) {
        List<FinVoucherModelLine> finVoucherLineList = new ArrayList<FinVoucherModelLine>();
        
        //获取凭证字
        Map voucherTypeMap = this.datasetCommonService.getVoucherType();
        //获取凭证业务类型
        Map<String, String> voucherBusinessTypeMap = DataBox.getVoucherBusinessType();
        
        //查询要编辑的数据
        if(finVoucherModelHead!=null&&finVoucherModelHead.getVoucherHeadId()!=null&&StringUtils.isNotBlank(finVoucherModelHead.getVoucherHeadCode())) {
            finVoucherModelHead = this.finVoucherModelHeadService.getDataObject(finVoucherModelHead.getVoucherHeadId());
            //设置显示字段
            finVoucherModelHead.setStaffName(this.hrCommonService.getHrStaff(finVoucherModelHead.getStaffCode()).getStaffName());
            finVoucherModelHead.setDepartmentName(this.hrCommonService.getHrDepartment(finVoucherModelHead.getDepartmentCode()).getDepartmentName());
            //获取凭证行
            finVoucherLineList = this.finVoucherModelLineService.getFinVoucherModelLineListByVoucherHeadCode(finVoucherModelHead.getVoucherHeadCode());
            //循环设置设置科目
            for(FinVoucherModelLine line: finVoucherLineList) {
                line.setSubjectDesc(this.masterDataCommonService.getSubjectMap().get(line.getSubjectCode()));
            }
        }else {
            //初始化默认字段
            
            //获取当前用户职员信息
            HrStaffInfoRO staffInfo = this.hrCommonService.getStaffInfo(ShiroUtil.getUsername());
            finVoucherModelHead.setStaffCode(staffInfo.getStaffCode());
            finVoucherModelHead.setDepartmentCode(staffInfo.getDepartmentCode());
            finVoucherModelHead.setStaffName(staffInfo.getStaffName());
            finVoucherModelHead.setDepartmentName(staffInfo.getDepartmentName());
            
            //不允许新建已经创建的默认业务类型的模板
            //获取已创建的业务类型
            List<String> businessTypeList = this.finVoucherModelHeadService.getFinVoucherModelHeadForBusinessType();
            //循环过滤掉已创建的业务类型
            Iterator<Entry<String, String>> iterator = voucherBusinessTypeMap.entrySet().iterator();
            while(iterator.hasNext()) {
                String businessType = iterator.next().getKey();
                for(String businessTypeTemp: businessTypeList) {
                    if(!businessTypeTemp.equals("CUSTOM")&&businessType.equals(businessTypeTemp)) {
                        iterator.remove();
                        break;
                    }
                }
            }
        }
        
        //页面属性设置
        model.addAttribute("finVoucherHead", finVoucherModelHead);
        model.addAttribute("finVoucherLineList", finVoucherLineList);
        model.addAttribute("voucherTypeMap", voucherTypeMap);
        model.addAttribute("voucherBusinessTypeMap", voucherBusinessTypeMap);
        
        return "basic.jsp?content=finVoucher/voucherModelEdit";
    }
    
    
    
    /**
     * 
     * @description 获取选择单据Modal
     * @date 2020年7月29日
     * @author dongbin
     * @param businessType
     * @param pages
     * @param poHeadCO
     * @param receiptHeadCO
     * @param model
     * @return
     * @return String
     *
     */
    @RequestMapping("getSelectBillModal")
    public String getSelectBillModal(String businessType, Pages pages, PayHeadCO poHeadCO, ReceiptHeadCO receiptHeadCO, Model model) {
        //分页查询数据
        if(pages.getPage()==0) {
            pages.setPage(1);
        }
        
        if(businessType.equals("PAY")) {
            //分页查询数据
            List<PayHead> payHeadList = this.payHeadService.getPayHeadListForNotCreateVoucher(pages, poHeadCO);
            //循环获取金额
            //循环设置职员和组织信息
            for(PayHead payHead: payHeadList) {
                payHead.setAmount(this.payLineService.getPayAmountByPayHeadCode(payHead.getPayHeadCode()).doubleValue());
                payHead.setStaffName(this.hrCommonService.getHrStaff(payHead.getStaffCode()).getStaffName());
                payHead.setDepartmentName(this.hrCommonService.getHrDepartment(payHead.getDepartmentCode()).getDepartmentName());
            }
            
            //付款来源类型
            Map paySourceTypeMap = com.erp.finance.pay.dao.data.DataBox.getPaySourceType();
            //状态
            Map payStatusMap = com.erp.finance.pay.dao.data.DataBox.getPayStatusMap();
            //获取出纳状态
            Map paidStatusMap = com.erp.finance.pay.dao.data.DataBox.getPaidStatusMap();
            //获取供应商
            Map vendorMap = this.masterDataCommonService.getVendorMap();
            
            //页面属性设置
            model.addAttribute("payHeadList", payHeadList);
            model.addAttribute("pages", pages);
            model.addAttribute("paySourceTypeMap", paySourceTypeMap);
            model.addAttribute("payStatusMap", payStatusMap);
            model.addAttribute("paidStatusMap", paidStatusMap);
            model.addAttribute("vendorMap", vendorMap);
            
            return "finVoucher/pop/selectPayBillModal";
        }else if(businessType.equals("RECEIPT")) {
          //分页查询数据
            List<ReceiptHead> receiptHeadList = this.receiptHeadService.getReceiptHeadListForNotCreateVoucher(pages, receiptHeadCO);
            //循环获取金额
            //循环设置职员和组织信息
            for(ReceiptHead receiptHead: receiptHeadList) {
                receiptHead.setAmount(this.receiptLineService.getReceiptAmountByReceiptHeadCode(receiptHead.getReceiptHeadCode()).doubleValue());
                receiptHead.setStaffName(this.hrCommonService.getHrStaff(receiptHead.getStaffCode()).getStaffName());
                receiptHead.setDepartmentName(this.hrCommonService.getHrDepartment(receiptHead.getDepartmentCode()).getDepartmentName());
            }
            
            //收款来源类型
            Map receiptSourceTypeMap = com.erp.finance.receipt.dao.data.DataBox.getReceiptSourceType();
            //状态
            Map receiptStatusMap = com.erp.finance.receipt.dao.data.DataBox.getReceiptStatusMap();
            //获取出纳状态
            Map receivedStatusMap = com.erp.finance.receipt.dao.data.DataBox.getReceivedStatusMap();
            //获取客户
            Map customerMap = this.masterDataCommonService.getCustomerMap();
            
            //页面属性设置
            model.addAttribute("receiptHeadList", receiptHeadList);
            model.addAttribute("pages", pages);
            model.addAttribute("receiptSourceTypeMap", receiptSourceTypeMap);
            model.addAttribute("receiptStatusMap", receiptStatusMap);
            model.addAttribute("receivedStatusMap", receivedStatusMap);
            model.addAttribute("customerMap", customerMap);
            
            return "finVoucher/pop/selectReceiptBillModal";
        }
        
        return "common/blank";
    }
    
    
    
    /**
     * 
     * @description 编辑数据
     * @date 2020-07-28 13:05:47
     * @author 
     * @param finVoucherModelHead
     * @param bindingResult
     * @param model
     * @return String
     *
     */
    @RequestMapping("editFinVoucherModelHead")
    public String editFinVoucherModelHead(@Valid FinVoucherModelHead finVoucherModelHead, BindingResult bindingResult, Integer[] voucherLineId, String[] memo, String[] subjectCode, String[] drAmount, String[] crAmount, Model model, RedirectAttributes attr) {
        //参数校验
        Map<String, String> errorMap = this.validateParameters(bindingResult, model);
        if(errorMap.size()>0) {
            return "forward:getFinVoucherModelHead";
        }
        
        //对当前编辑的对象初始化默认的字段
        if(finVoucherModelHead.getVoucherHeadId()==null) {
            finVoucherModelHead.setVoucherHeadCode(SnowFlake.generateId().toString());
        }
        
        List<FinVoucherModelLine> finVoucherLineList = new ArrayList<FinVoucherModelLine>();
        //循环设置凭证行
        for(int a=0;a<subjectCode.length;a++) {
            FinVoucherModelLine finVoucherLine = new FinVoucherModelLine();
            //判断是新增还是修改
            if(voucherLineId[a]==null) {
                finVoucherLine.setVoucherLineCode(SnowFlake.generateId().toString());
                finVoucherLine.setVoucherHeadCode(finVoucherModelHead.getVoucherHeadCode());
            }else {
                finVoucherLine.setVoucherLineId(voucherLineId[a]);
            }
            
            finVoucherLine.setCrAmount(crAmount[a]);
            finVoucherLine.setDrAmount(drAmount[a]);
            finVoucherLine.setMemo(memo[a]);
            finVoucherLine.setSubjectCode(subjectCode[a]);
            
            finVoucherLineList.add(finVoucherLine);
        }
        
        //保存编辑的数据
        this.finVoucherModelHeadService.insertOrUpdateFinVoucherModel(finVoucherModelHead, finVoucherLineList);
        //提示信息
        attr.addFlashAttribute("hint", "success");
        
        return "redirect:getFinVoucherModelHeadList";
    }
    
    
    
    /**
     * 
     * @description 删除数据
     * @date 2020-07-28 13:05:47
     * @author 
     * @param finVoucherModelHead
     * @return String
     *
     */
    @RequestMapping("deleteFinVoucherModelHead")
    public String deleteFinVoucherModelHead(FinVoucherModelHead finVoucherModelHead, RedirectAttributes attr) {
        //删除数据前验证数据
        if(finVoucherModelHead!=null&&finVoucherModelHead.getVoucherHeadId()!=null&&StringUtils.isNotBlank(finVoucherModelHead.getVoucherHeadCode())) {
            //删除数据
            this.finVoucherModelHeadService.deleteDataObject(finVoucherModelHead);
            this.finVoucherModelLineService.deleteFinVoucherModelLineByVoucherHeadCode(finVoucherModelHead.getVoucherHeadCode());
            
            //提示信息
            attr.addFlashAttribute("hint", "success");
        }
        
        return "redirect:getFinVoucherModelHeadList";
    }
    
    
    
    /**
     * 
     * @description 获取凭证模板Json数据
     * @date 2020年7月28日
     * @author dongbin
     * @param finVoucherHead
     * @return
     * @return String
     *
     */
    @RequestMapping("getVoucherModelForJson")
    @ResponseBody
    public String getVoucherModelForJson(FinVoucherModelHead finVoucherModelHead) {
        List<FinVoucherModelLine> finVoucherLineList = new ArrayList<FinVoucherModelLine>();
        
        //获取凭证模板头信息
        if(finVoucherModelHead!=null&&finVoucherModelHead.getVoucherHeadId()!=null) {
            finVoucherModelHead = this.finVoucherModelHeadService.getDataObject(finVoucherModelHead.getVoucherHeadId());
            //获取凭证行
            finVoucherLineList = this.finVoucherModelLineService.getFinVoucherModelLineListByVoucherHeadCode(finVoucherModelHead.getVoucherHeadCode());
            //循环设置设置科目
            for(FinVoucherModelLine line: finVoucherLineList) {
                line.setSubjectDesc(this.masterDataCommonService.getSubjectMap().get(line.getSubjectCode()));
            }
            finVoucherModelHead.setFinVoucherModelLineList(finVoucherLineList);
        }
        
        System.out.println(JsonUtil.objectToJson(finVoucherModelHead, "yyyy-MM-dd"));
        
        return JsonUtil.objectToJson(finVoucherModelHead, "yyyy-MM-dd");
    }
    
    
    
    /**
     * 
     * @description 自动创建凭证
     * @date 2020年7月30日
     * @author dongbin
     * @param payHeadCode
     * @param amount
     * @param businessType
     * @param model
     * @param attr
     * @return
     * @return String
     *
     */
    @RequestMapping("autoCreateVoucher")
    @ResponseBody
    public String autoCreateVoucher(String billHeadCode, Double amount, String businessType, Model model, RedirectAttributes attr) {
        //判断参数
        if(StringUtils.isNotBlank(billHeadCode)&&StringUtils.isNotBlank(businessType)&&amount!=null&&amount>0&&DataBox.getVoucherBusinessType().containsKey(businessType)) {
            try {
                //调用自动创建方法
                return this.finVoucherModelHeadService.autoCreateVoucher(billHeadCode, amount, businessType);
            }catch(Exception e) {
                return JsonResultUtil.getErrorJson(-1, "自动生成凭证执行错误");
            }
        }else {
            return JsonResultUtil.getErrorJson(-1, "付款单参数传递错误");
        }
    }
}
