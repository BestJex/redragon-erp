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
package com.erp.finance.voucher.dao;

import com.framework.api.DaoCRUDIF;
import com.erp.finance.voucher.dao.model.FinVoucherBillR;
import com.erp.finance.voucher.dao.model.FinVoucherBillRCO;

public interface FinVoucherBillRDao extends DaoCRUDIF<FinVoucherBillR, FinVoucherBillRCO>{
    
    //删除凭证与单据关联（根据凭证头code）
    public abstract void deleteFinVoucherBillRByVoucherHeadCode(String voucherHeadCode);
    
}