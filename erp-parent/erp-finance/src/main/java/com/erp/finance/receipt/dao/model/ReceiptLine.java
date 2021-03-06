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
@Table(name="receipt_line", schema="erp")
@DynamicInsert(true)
@DynamicUpdate(true)
public class ReceiptLine implements java.io.Serializable {

    //serialVersionUID
    private static final long serialVersionUID = 1L;

    //Constructors
    public ReceiptLine() {
    }
    
    //Fields
    
    //收款行id
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "receipt_line_id", unique = true, nullable = false)
    private Integer receiptLineId;
    
    public Integer getReceiptLineId() {
        return this.receiptLineId;
    }
    public void setReceiptLineId(Integer receiptLineId) {
        this.receiptLineId = receiptLineId;
    }
    
    //收款行编码
    @Column(name = "receipt_line_code", unique = true, nullable = false, length = 45)
    private String receiptLineCode;
    
    public String getReceiptLineCode() {
        return this.receiptLineCode;
    }
    public void setReceiptLineCode(String receiptLineCode) {
        this.receiptLineCode = receiptLineCode;
    }
    
    //收款头编码
    @Column(name = "receipt_head_code", unique = false, nullable = false, length = 45)
    private String receiptHeadCode;
    
    public String getReceiptHeadCode() {
        return this.receiptHeadCode;
    }
    public void setReceiptHeadCode(String receiptHeadCode) {
        this.receiptHeadCode = receiptHeadCode;
    }
    
    //收款来源行编码
    @NotBlank(message="收款来源行编码不能为空")
    @Column(name = "receipt_source_line_code", unique = false, nullable = false, length = 45)
    private String receiptSourceLineCode;
    
    public String getReceiptSourceLineCode() {
        return this.receiptSourceLineCode;
    }
    public void setReceiptSourceLineCode(String receiptSourceLineCode) {
        this.receiptSourceLineCode = receiptSourceLineCode;
    }
    
    //行金额
    @NotNull(message="行金额不能为空")
    @Column(name = "amount", unique = false, nullable = false)
    private Double amount;
    
    public Double getAmount() {
        return this.amount;
    }
    public void setAmount(Double amount) {
        this.amount = amount;
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
    
    //状态
    @Column(name = "status", unique = false, nullable = false, length = 1)
    private String status;
    
    public String getStatus() {
        return this.status;
    }
    public void setStatus(String status) {
        this.status = status;
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
     * 用于显示的字段
     */
    @Transient
    private String materialCode;
    @Transient
    private String materialName;
    @Transient
    private Double price;
    @Transient
    private Double quantity;
    @Transient
    private String unit;
    @Transient
    private Double soLineAmount;

    public String getMaterialCode() {
        return materialCode;
    }
    public void setMaterialCode(String materialCode) {
        this.materialCode = materialCode;
    }
    public String getMaterialName() {
        return materialName;
    }
    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }
    public Double getPrice() {
        return price;
    }
    public void setPrice(Double price) {
        this.price = price;
    }
    public Double getQuantity() {
        return quantity;
    }
    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }
    public String getUnit() {
        return unit;
    }
    public void setUnit(String unit) {
        this.unit = unit;
    }
    public Double getSoLineAmount() {
        return soLineAmount;
    }
    public void setSoLineAmount(Double soLineAmount) {
        this.soLineAmount = soLineAmount;
    }
    
}