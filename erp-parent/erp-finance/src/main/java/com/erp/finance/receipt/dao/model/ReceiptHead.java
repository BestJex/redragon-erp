package com.erp.finance.receipt.dao.model;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name="receipt_head", schema="erp")
@DynamicInsert(true)
@DynamicUpdate(true)
public class ReceiptHead implements java.io.Serializable {

    //serialVersionUID
    private static final long serialVersionUID = 1L;

    //Constructors
    public ReceiptHead() {
    }
    
    //Fields
    
    //收款头id
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "receipt_head_id", unique = true, nullable = false)
    private Integer receiptHeadId;
    
    public Integer getReceiptHeadId() {
        return this.receiptHeadId;
    }
    public void setReceiptHeadId(Integer receiptHeadId) {
        this.receiptHeadId = receiptHeadId;
    }
    
    //收款头编码
    @NotBlank(message="收款头编码不能为空")
    @Column(name = "receipt_head_code", unique = true, nullable = false, length = 45)
    private String receiptHeadCode;
    
    public String getReceiptHeadCode() {
        return this.receiptHeadCode;
    }
    public void setReceiptHeadCode(String receiptHeadCode) {
        this.receiptHeadCode = receiptHeadCode;
    }
    
    //收款来源类型（采购订单SO、入库单OUTPUT）
    @NotBlank(message="收款来源类型不能为空")
    @Column(name = "receipt_source_type", unique = false, nullable = false, length = 45)
    private String receiptSourceType;
    
    public String getReceiptSourceType() {
        return this.receiptSourceType;
    }
    public void setReceiptSourceType(String receiptSourceType) {
        this.receiptSourceType = receiptSourceType;
    }
    
    //收款来源头编码（销售订单头编码、出库单头编码）
    @NotBlank(message="收款来源头编码不能为空")
    @Column(name = "receipt_source_head_code", unique = false, nullable = false, length = 45)
    private String receiptSourceHeadCode;
    
    public String getReceiptSourceHeadCode() {
        return this.receiptSourceHeadCode;
    }
    public void setReceiptSourceHeadCode(String receiptSourceHeadCode) {
        this.receiptSourceHeadCode = receiptSourceHeadCode;
    }
    
    //付款方
    @NotBlank(message="付款方不能为空")
    @Column(name = "payer", unique = false, nullable = false, length = 45)
    private String payer;
    
    public String getPayer() {
        return this.payer;
    }
    public void setPayer(String payer) {
        this.payer = payer;
    }
    
    //收款方
    @NotBlank(message="收款方不能为空")
    @Column(name = "receiver", unique = false, nullable = false, length = 45)
    private String receiver;
    
    public String getReceiver() {
        return this.receiver;
    }
    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }
    
    //币种
    @NotBlank(message="币种不能为空")
    @Column(name = "currency_code", unique = false, nullable = false, length = 45)
    private String currencyCode;
    
    public String getCurrencyCode() {
        return this.currencyCode;
    }
    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }
    
    //收款时间
    @NotNull(message="收款时间不能为空")
    @Column(name = "receipt_date", unique = false, nullable = false)
    private Date receiptDate;
    
    public Date getReceiptDate() {
        return this.receiptDate;
    }
    public void setReceiptDate(Date receiptDate) {
        this.receiptDate = receiptDate;
    }
    
    //预收款标识
    @NotBlank(message="预收款标识不能为空")
    @Column(name = "pre_receipt_flag", unique = false, nullable = false, length = 1)
    private String preReceiptFlag;
    
    public String getPreReceiptFlag() {
        return this.preReceiptFlag;
    }
    public void setPreReceiptFlag(String preReceiptFlag) {
        this.preReceiptFlag = preReceiptFlag;
    }
    
    //收款方式
    @NotBlank(message="收款方式不能为空")
    @Column(name = "receipt_mode", unique = false, nullable = false, length = 45)
    private String receiptMode;
    
    public String getReceiptMode() {
        return this.receiptMode;
    }
    public void setReceiptMode(String receiptMode) {
        this.receiptMode = receiptMode;
    }
    
    //银行编码
    @Column(name = "bank_code", unique = false, nullable = true, length = 45)
    private String bankCode;
    
    public String getBankCode() {
        return this.bankCode;
    }
    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }
    
    //分行编码
    @Column(name = "sub_bank_code", unique = false, nullable = true, length = 45)
    private String subBankCode;
    
    public String getSubBankCode() {
        return this.subBankCode;
    }
    public void setSubBankCode(String subBankCode) {
        this.subBankCode = subBankCode;
    }
    
    //银行账户
    @Column(name = "bank_account", unique = false, nullable = true, length = 45)
    private String bankAccount;
    
    public String getBankAccount() {
        return this.bankAccount;
    }
    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }
    
    //摘要
    @Column(name = "memo", unique = false, nullable = true, length = 200)
    private String memo;
    
    public String getMemo() {
        return this.memo;
    }
    public void setMemo(String memo) {
        this.memo = memo;
    }
    
    //版本
    @Column(name = "version", unique = false, nullable = false)
    private Integer version;
    
    public Integer getVersion() {
        return this.version;
    }
    public void setVersion(Integer version) {
        this.version = version;
    }
    
    //状态（新建NEW，确认CONFIRM，取消CANCEL）
    @Column(name = "status", unique = false, nullable = false, length = 10)
    private String status;
    
    public String getStatus() {
        return this.status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    
    //审批状态（未提交UNSUBMIT、已提交SUBMIT、已审批APPROVE、已驳回REJECT）
    @Column(name = "approve_status", unique = false, nullable = false, length = 10)
    private String approveStatus;
    
    public String getApproveStatus() {
        return this.approveStatus;
    }
    public void setApproveStatus(String approveStatus) {
        this.approveStatus = approveStatus;
    }
    
    //收款状态（未付款N，已付款Y）
    @Column(name = "received_status", unique = false, nullable = true, length = 10)
    private String receivedStatus;
    
    public String getReceivedStatus() {
        return this.receivedStatus;
    }
    public void setReceivedStatus(String receivedStatus) {
        this.receivedStatus = receivedStatus;
    }
    
    //制单人
    @Column(name = "staff_code", unique = false, nullable = false, length = 45)
    private String staffCode;
    
    public String getStaffCode() {
        return this.staffCode;
    }
    public void setStaffCode(String staffCode) {
        this.staffCode = staffCode;
    }
    
    //制单人部门
    @Column(name = "department_code", unique = false, nullable = false, length = 45)
    private String departmentCode;
    
    public String getDepartmentCode() {
        return this.departmentCode;
    }
    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }
    
    //创建时间
    @Column(name = "created_date", unique = false, nullable = false)
    private Date createdDate;
    
    public Date getCreatedDate() {
        return this.createdDate;
    }
    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
    
    //创建人
    @Column(name = "created_by", unique = false, nullable = false, length = 45)
    private String createdBy;
    
    public String getCreatedBy() {
        return this.createdBy;
    }
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
    
    //最后修改时间
    @Column(name = "last_updated_date", unique = false, nullable = true)
    private Date lastUpdatedDate;
    
    public Date getLastUpdatedDate() {
        return this.lastUpdatedDate;
    }
    public void setLastUpdatedDate(Date lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }
    
    //最后修改人
    @Column(name = "last_updated_by", unique = false, nullable = true, length = 45)
    private String lastUpdatedBy;
    
    public String getLastUpdatedBy() {
        return this.lastUpdatedBy;
    }
    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }
    
    //组织机构
    @Column(name = "org_code", unique = false, nullable = false, length = 10)
    private String orgCode;
    
    public String getOrgCode() {
        return this.orgCode;
    }
    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }
    
    
    /*
     * 用于展示的字段
     */
    @Transient
    private String staffName;
    @Transient
    private String departmentName;
    @Transient
    private Double amount = 0D;
    @Transient
    private String receiptSourceHeadName;
    @Transient
    private Double receiptSourceHeadAmount = 0D;
    @Transient
    private Double receiptSourceHeadHISAmount = 0D;
    @Transient
    private String payerName;
    @Transient
    private String bankName;

    public String getStaffName() {
        return staffName;
    }
    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }
    public String getDepartmentName() {
        return departmentName;
    }
    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }
    public Double getAmount() {
        return amount;
    }
    public void setAmount(Double amount) {
        this.amount = amount;
    }
    public String getReceiptSourceHeadName() {
        return receiptSourceHeadName;
    }
    public void setReceiptSourceHeadName(String receiptSourceHeadName) {
        this.receiptSourceHeadName = receiptSourceHeadName;
    }
    public Double getReceiptSourceHeadAmount() {
        return receiptSourceHeadAmount;
    }
    public void setReceiptSourceHeadAmount(Double receiptSourceHeadAmount) {
        this.receiptSourceHeadAmount = receiptSourceHeadAmount;
    }
    public Double getReceiptSourceHeadHISAmount() {
        return receiptSourceHeadHISAmount;
    }
    public void setReceiptSourceHeadHISAmount(Double receiptSourceHeadHISAmount) {
        this.receiptSourceHeadHISAmount = receiptSourceHeadHISAmount;
    }
    public String getPayerName() {
        return payerName;
    }
    public void setPayerName(String payerName) {
        this.payerName = payerName;
    }
    public String getBankName() {
        return bankName;
    }
    public void setBankName(String bankName) {
        this.bankName = bankName;
    }
    
}