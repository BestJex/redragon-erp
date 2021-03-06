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
package com.erp.finance.pay.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
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
import com.framework.controller.ControllerSupport;
import com.framework.dao.model.Pages;
import com.framework.util.JsonResultUtil;
import com.framework.util.JsonUtil;
import com.framework.util.ShiroUtil;
import com.springboot.dao.data.GlobalDataBox;
import com.erp.dataset.service.DatasetCommonService;
import com.erp.finance.pay.dao.data.DataBox;
import com.erp.finance.pay.dao.model.PayHead;
import com.erp.finance.pay.dao.model.PayHeadCO;
import com.erp.finance.pay.service.PayHeadService;
import com.erp.finance.pay.service.PayLineService;
import com.erp.hr.dao.model.HrStaffInfoRO;
import com.erp.hr.service.HrCommonService;
import com.erp.masterdata.common.service.MasterDataCommonService;
import com.erp.masterdata.customer.dao.model.MdCustomerBank;
import com.erp.masterdata.customer.dao.model.MdCustomerBankCO;
import com.erp.masterdata.vendor.dao.model.MdVendorBank;
import com.erp.masterdata.vendor.dao.model.MdVendorBankCO;
import com.erp.masterdata.vendor.service.MdVendorBankService;
import com.erp.order.po.dao.model.PoHead;
import com.erp.order.po.dao.model.PoHeadCO;
import com.erp.order.po.service.PoHeadService;
import com.erp.order.po.service.PoLineService;

@Controller
@RequestMapping("/web/payHead")
public class PayHeadWebController extends ControllerSupport{
    
    //日志处理
    private Logger logger = LoggerFactory.getLogger(PayHeadWebController.class);
    
    //服务层注入
    @Autowired
    private PayHeadService payHeadService;
    @Autowired
    private PayLineService payLineService;
    @Autowired
    private DatasetCommonService datasetCommonService;
    @Autowired
    private HrCommonService hrCommonService;
    @Autowired
    private MasterDataCommonService masterDataCommonService;
    @Autowired
    private PoHeadService poHeadService;
    @Autowired
    private MdVendorBankService mdVendorBankService;
    @Autowired
    private PoLineService poLineService;
    
    @Override
    public String getExceptionRedirectURL() {
        return "redirect:getPayHeadList";
    }
    
    
    
    /**
     * 
     * @description 查询数据列表
     * @date 2020-07-19 14:16:19
     * @author 
     * @param pages
     * @param payHeadCO
     * @param model
     * @return String
     *
     */
    @RequestMapping("getPayHeadList")
    public String getPayHeadList(Pages pages, PayHeadCO payHeadCO, Model model) {
        //分页查询数据
        if(pages.getPage()==0) {
            pages.setPage(1);
        }
        
        //分页查询数据
        List<PayHead> payHeadList = this.payHeadService.getDataObjects(pages, payHeadCO);
        
        //付款来源类型
        Map paySourceTypeMap = DataBox.getPaySourceType();
        //状态
        Map payStatusMap = DataBox.getPayStatusMap();
        //获取出纳状态
        Map paidStatusMap = DataBox.getPaidStatusMap();
        //获取供应商
        Map vendorMap = this.masterDataCommonService.getVendorMap();
        //审批状态
        Map approveStatusMap = GlobalDataBox.getApproveStatusMap();
        
        //循环设置职员和组织信息
        for(PayHead payHead: payHeadList) {
            payHead.setStaffName(this.hrCommonService.getHrStaff(payHead.getStaffCode()).getStaffName());
            payHead.setDepartmentName(this.hrCommonService.getHrDepartment(payHead.getDepartmentCode()).getDepartmentName());
        }
        
        //页面属性设置
        model.addAttribute("payHeadList", payHeadList);
        model.addAttribute("pages", pages);
        model.addAttribute("paySourceTypeMap", paySourceTypeMap);
        model.addAttribute("payStatusMap", payStatusMap);
        model.addAttribute("paidStatusMap", paidStatusMap);
        model.addAttribute("vendorMap", vendorMap);
        model.addAttribute("approveStatusMap", approveStatusMap);
        
        return "basic.jsp?content=pay/payList";
    }
    
    
    
    /**
     * 
     * @description 查询单条数据
     * @date 2020-07-19 14:16:19
     * @author 
     * @param payHead
     * @param model
     * @return String
     *
     */
    @RequestMapping("getPayHead")
    public String getPayHead(PayHead payHead, Model model) {
        //查询要编辑的数据
        if(payHead!=null&&StringUtils.isNotBlank(payHead.getPayHeadCode())) {
            payHead = this.payHeadService.getDataObject(payHead.getPayHeadCode());
            //设置显示字段
            payHead.setStaffName(this.hrCommonService.getHrStaff(payHead.getStaffCode()).getStaffName());
            payHead.setDepartmentName(this.hrCommonService.getHrDepartment(payHead.getDepartmentCode()).getDepartmentName());
            //获取采购订单信息
            if(payHead.getPaySourceType().equals("PO")) {
                PoHead poHead = this.poHeadService.getDataObject(payHead.getPaySourceHeadCode());
                payHead.setPaySourceHeadName(poHead.getPoName());
                //获取采购订单金额
                BigDecimal amount = this.poLineService.getPoAmount(payHead.getPaySourceHeadCode());
                payHead.setPaySourceHeadAmount(amount==null?0D:amount.doubleValue());
                //获取已付款历史金额
                BigDecimal HISAmount = this.payLineService.getHISPayAmountForPO(payHead.getPaySourceHeadCode(), payHead.getPayHeadCode());
                payHead.setPaySourceHeadHISAmount(HISAmount==null?0D:HISAmount.doubleValue());
            }
            //获取收款人信息
            payHead.setReceiverName(this.masterDataCommonService.getVendorMap().get(payHead.getReceiver()));
            //获取银行信息
            payHead.setBankName(this.datasetCommonService.getBank().get(payHead.getBankCode()));
        }else {
            //初始化默认字段
            payHead.setAmount(0D);
            payHead.setPaidStatus("N");
            payHead.setPrepayFlag("N");
            payHead.setStatus("NEW");
            
            //获取当前用户职员信息
            HrStaffInfoRO staffInfo = this.hrCommonService.getStaffInfo(ShiroUtil.getUsername());
            payHead.setStaffCode(staffInfo.getStaffCode());
            payHead.setDepartmentCode(staffInfo.getDepartmentCode());
            payHead.setStaffName(staffInfo.getStaffName());
            payHead.setDepartmentName(staffInfo.getDepartmentName());
        }
        
        //币种
        Map currencyTypeMap = this.datasetCommonService.getCurrencyType();
        //付款来源类型
        Map paySourceTypeMap = DataBox.getPaySourceType();
        //状态
        Map payStatusMap = DataBox.getPayStatusMap();
        //获取出纳状态
        Map paidStatusMap = DataBox.getPaidStatusMap();
        //获取付款方式
        Map payModeMap = this.datasetCommonService.getPayMode();
        //获取供应商
        Map vendorMap = this.masterDataCommonService.getVendorMap();
        //获取本公司
        Map vendorOwnMap = this.masterDataCommonService.getOwnVendorMap();
        //审批状态
        Map approveStatusMap = GlobalDataBox.getApproveStatusMap();
        
        //页面属性设置
        model.addAttribute("payHead", payHead);
        model.addAttribute("currencyTypeMap", currencyTypeMap);
        model.addAttribute("paySourceTypeMap", paySourceTypeMap);
        model.addAttribute("payStatusMap", payStatusMap);
        model.addAttribute("paidStatusMap", paidStatusMap);
        model.addAttribute("payModeMap", payModeMap);
        model.addAttribute("vendorMap", vendorMap);
        model.addAttribute("vendorOwnMap", vendorOwnMap);
        model.addAttribute("approveStatusMap", approveStatusMap);
        
        return "basic.jsp?content=pay/payEdit";
    }
    
    
    
    /**
     * 
     * @description 获取采购订单选择框
     * @date 2020年7月20日
     * @author dongbin
     * @return
     * @return String
     *
     */
    @RequestMapping("getSelectPOModal")
    public String getSelectPOModal(Pages pages, PoHeadCO poHeadCO, Model model) {
        //分页查询数据
        if(pages.getPage()==0) {
            pages.setPage(1);
        }
        
        //分页查询数据
        List<PoHead> poHeadList = this.poHeadService.getDataObjects(pages, poHeadCO);
        
        //采购订单类型
        Map poTypeMap = this.datasetCommonService.getPOType();
        //状态
        Map poStatusMap = com.erp.order.po.dao.data.DataBox.getPoStatusMap();
        //获取供应商
        Map vendorMap = this.masterDataCommonService.getVendorMap();
        //获取项目
        Map projectMap = this.masterDataCommonService.getProjectMap();
        
        //循环设置职员和组织信息
        for(PoHead poHead: poHeadList) {
            poHead.setStaffName(this.hrCommonService.getHrStaff(poHead.getStaffCode()).getStaffName());
            poHead.setDepartmentName(this.hrCommonService.getHrDepartment(poHead.getDepartmentCode()).getDepartmentName());
        }
        
        //页面属性设置
        model.addAttribute("poHeadList", poHeadList);
        model.addAttribute("pages", pages);
        model.addAttribute("poTypeMap", poTypeMap);
        model.addAttribute("poStatusMap", poStatusMap);
        model.addAttribute("vendorMap", vendorMap);
        model.addAttribute("projectMap", projectMap);
        
        return "pay/pop/selectPOModal";
    }
    
    
    
    /**
     * 
     * @description 获取收款方对应的银行选择框
     * @date 2020年7月20日
     * @author dongbin
     * @return
     * @return String
     *
     */
    @RequestMapping("getSelectBankModal")
    public String getSelectBankModal(Pages pages, MdVendorBankCO mdVendorBankCO, Model model) {
        //分页查询数据
        if(pages.getPage()==0) {
            pages.setPage(1);
        }
        pages.setMax(100);
        
        //分页查询数据
        List<MdVendorBank> mdCustomerBankList = this.mdVendorBankService.getBankListByVendorCode(pages, mdVendorBankCO);
        
        //获取银行
        Map bankMap = this.datasetCommonService.getBank();
        
        
        //页面属性设置
        model.addAttribute("mdCustomerBankList", mdCustomerBankList);
        model.addAttribute("pages", pages);
        model.addAttribute("bankMap", bankMap);
        
        return "pay/pop/selectBankModal";
    }
    
    
    
    /**
     * 
     * @description 编辑数据
     * @date 2020-07-19 14:16:19
     * @author 
     * @param payHead
     * @param bindingResult
     * @param model
     * @return String
     *
     */
    @RequestMapping("editPayHead")
    public String editPayHead(@Valid PayHead payHead, BindingResult bindingResult, Model model, RedirectAttributes attr) {
        //参数校验
        Map<String, String> errorMap = this.validateParameters(bindingResult, model);
        if(errorMap.size()>0) {
            return "forward:getPayHead";
        }
        
        //对当前编辑的对象初始化默认的字段
        
        //保存编辑的数据
        this.payHeadService.insertOrUpdateDataObject(payHead);
        //提示信息
        attr.addFlashAttribute("hint", "success");
        attr.addAttribute("payHeadCode", payHead.getPayHeadCode());
        
        return "redirect:getPayHead";
    }
    
    
    
    /**
     * 
     * @description 删除数据
     * @date 2020-07-19 14:16:19
     * @author 
     * @param payHead
     * @return String
     *
     */
    @RequestMapping("deletePayHead")
    public String deletePayHead(PayHead payHead, RedirectAttributes attr) {
        //删除数据前验证数据
        if(payHead!=null&&payHead.getPayHeadId()!=null) {
            if(payHead.getStatus().equals("NEW")) {
                //删除数据
                this.payHeadService.deleteDataObject(payHead);
                this.payLineService.deletePayLineByPayHeadCode(payHead.getPayHeadCode());
                
                //提示信息
                attr.addFlashAttribute("hint", "success");
            }else {
                //提示信息
                attr.addFlashAttribute("hint", "hint");
                attr.addFlashAttribute("alertMessage", "非新建状态的付款不能删除");
            }
        }
        
        return "redirect:getPayHeadList";
    }
    
    
    
    /**
     * 
     * @description 更新审批状态
     * @date 2020年8月4日
     * @author dongbin
     * @param code
     * @param approveStatus
     * @param attr
     * @return
     * @return String
     *
     */
    @RequestMapping("updateApproveStatus")
    public String updateApproveStatus(String code, String approveStatus, RedirectAttributes attr) {
        
        if(StringUtils.isNotBlank(code)&&StringUtils.isNotBlank(approveStatus)) {
            //更新审核状态
            this.payHeadService.updateApproveStatus(code, approveStatus);
          //提示信息
            attr.addFlashAttribute("hint", "success");
            attr.addAttribute("payHeadCode", code);
        }else {
            //提示信息
            attr.addFlashAttribute("hint", "hint");
            attr.addFlashAttribute("alertMessage", "提交或审批数据错误");
            attr.addAttribute("payHeadCode", code);
        }
        
        return "redirect:getPayHead";
    }
}
