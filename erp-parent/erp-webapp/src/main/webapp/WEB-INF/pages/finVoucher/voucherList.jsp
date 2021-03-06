<%--

	Copyright 2020-2021 redragon.dongbin

	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at

      https://www.apache.org/licenses/LICENSE-2.0

	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.
	
--%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<%-- 导入面包屑 --%>
<jsp:include page="../common/nav.jsp"></jsp:include>

<div class="wrapper wrapper-content animated fadeInRight">

    <%-- 导入提示信息框 --%>
    <c:if test="${hint!=null&&hint!=''}">
   		<jsp:include page="../common/alert/alert.jsp">
   			<jsp:param value="${hint}" name="alertType"/>
   			<jsp:param value="${alertMessage}" name="alertMessage"/>
   		</jsp:include>
    </c:if>
	
	<%-- 导入查询框 --%>
	<jsp:include page="search/voucherSearch.jsp"></jsp:include>
	
	<div class="row">
		<div class="col-lg-12">
			<div class="ibox ">
		        <div class="ibox-title">
		            <h4>凭证列表</h4>
		            <div class="ibox-tools">
		                
		                <div class="btn-group" role="group">
						    <button type="button" class="btn btn-primary dropdown-toggle btn-sm" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
						      自动凭证
						      <span class="caret"></span>
						    </button>
						    <ul class="dropdown-menu">
						      <li><a id="selectPayButton" href="javascript:void(0)">付款单生成凭证</a></li>
						      <li><a id="selectReceiptButton" href="javascript:void(0)">收款单生成凭证</a></li>
						    </ul>
						</div>
		                
		                <button id="addButton" class="btn btn-success btn-sm" type="button"><i class="fa fa-plus"></i>&nbsp;&nbsp;<span class="bold">手工凭证</span></button>
		                <button id="searchButton" class="btn btn-default btn-sm" type="button"><i class="fa fa-search"></i>&nbsp;&nbsp;展开查询</button>
		            </div>
		        </div>
				<div class="ibox-content border-bottom" style="padding-bottom: 0px;">
					<div class="table-responsive">
						<table class="table table-striped table-hover table-bordered border-top">
							<thead>
								<tr>
									<%-- 
									<th></th>
									--%>
									<th width="5%">序号</th>
									<th>凭证字</th>
									<th>凭证号</th>
									<th>凭证日期</th>
									<th>凭证金额</th>
									<th>制单人</th>
									<th width="5%">状态</th>
									<th width="7%">审批状态</th>
									<th width="10%">操作</th>
								</tr>
							</thead>
							<tbody>
							
							    <c:forEach items="${requestScope.finVoucherHeadList}" var="data" varStatus="status">
								<tr>
									<%-- 
									<td><input type="checkbox" class="i-checks"
										name="input[]"></td>
									--%>	
									<td>${status.count}</td>
									<td>${requestScope.voucherTypeMap[data.voucherType]}</td>
									<td>${data.voucherNumber}</td>
									<td><fmt:formatDate value="${data.voucherDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
									<td>${data.amount}</td>
									<td>${data.staffName}</td>
									<td>
									<c:choose>
									   <c:when test="${data.status=='Y'}">
									       <span class="label label-primary">有效</span>
									   </c:when>
									   <c:otherwise>
									       <span class="label label-danger">作废</span>
									   </c:otherwise>
									</c:choose>
									</td>
									<td>
									<c:choose>
									   <c:when test="${data.approveStatus=='UNSUBMIT'}">
									       <span class="label">${requestScope.approveStatusMap[data.approveStatus]}</span>
									   </c:when>
									   <c:when test="${data.approveStatus=='SUBMIT'}">
									       <span class="label label-primary">${requestScope.approveStatusMap[data.approveStatus]}</span>
									   </c:when>
									   <c:when test="${data.approveStatus=='APPROVE'}">
									       <span class="label label-success">${requestScope.approveStatusMap[data.approveStatus]}</span>
									   </c:when>
									   <c:when test="${data.approveStatus=='REJECT'}">
									       <span class="label label-warning">${requestScope.approveStatusMap[data.approveStatus]}</span>
									   </c:when>
									</c:choose>
									</td>
									<td>
										<div class="btn-group">
											<button class="btn-white btn btn-xs" onclick="editData(${data.voucherHeadId},'${data.voucherHeadCode}')"><i class="fa fa-edit"></i>&nbsp;编辑</button>&nbsp;
											
											<c:if test="${data.approveStatus!='APPROVE'&&data.approveStatus!='SUBMIT'}">
												<button class="btn-white btn btn-xs" onclick="deleteData(${data.voucherHeadId},'${data.voucherHeadCode}','${data.approveStatus}')"><i class="fa fa-trash"></i>&nbsp;删除</button>
											</c:if>
										</div>
									</td>
								</tr>
								</c:forEach>
								
							</tbody>
							<tfoot>
							    <%-- 导入页码 --%>
								<jsp:include page="../common/pages.jsp"></jsp:include>
							</tfoot>
						</table>
					</div>

				</div>
			</div>
		</div>
		
	</div>
</div>

<div id="selectBillModal"></div>

<script>
	$(document).ready(function() {
		/*
		$('.i-checks').iCheck({
			checkboxClass : 'icheckbox_square-green',
			radioClass : 'iradio_square-green',
		});
		*/
		$("#addButton").click(function(){
		    window.location.href="web/finVoucherHead/getFinVoucherHead";
		});
		
		$("#selectPayButton").click(function(){
		    getSelectBillModal("PAY");
		});
		
		$("#selectReceiptButton").click(function(){
		    getSelectBillModal("RECEIPT");
		});
		
		$("#searchButton").click(function(){
		    if($("#searchDiv").css("display")=="none"){
		        $("#searchDiv").show();
		        $("#searchButton").html('<i class="fa fa-search"></i>&nbsp;&nbsp;关闭查询');
		        $("#searchButton").addClass("btn-outline btn-warning");
		        $("#searchButton").blur();
		    }else{
		        $("#searchDiv").hide();
		        $("#searchButton").html('<i class="fa fa-search"></i>&nbsp;&nbsp;展开查询');
		        $("#searchButton").removeClass("btn-outline btn-warning");
		        $("#searchButton").blur();
		    }
		});
	});
	
	function editData(id, code){
		window.location.href="web/finVoucherHead/getFinVoucherHead?voucherHeadId="+id+"&voucherHeadCode="+code;
	}
	
	function deleteData(id, code, approveStatus) {
		redragonJS.confirm("确认删除数据？", function(){
			window.location.href="web/finVoucherHead/deleteFinVoucherHead?voucherHeadId="+id+"&voucherHeadCode="+code+"&approveStatus="+approveStatus;
		});
	}
	
	//返回单据选择框
	function getSelectBillModal(businessType){
		$('#selectBillDiv').modal('hide');
		redragonJS.loading("ibox-content");
		$.ajax({
			type: "post",
			url: "web/finVoucherModelHead/getSelectBillModal",
			data: {"businessType": businessType, 
				   "payHeadCode": $("#payHeadCodeSearch").val(), "paySourceHeadCode": $("#paySourceHeadCodeSearch").val(), "receiver": $("#receiverSearch").val(),
				   "receiptHeadCode": $("#receiptHeadCodeSearch").val(), "receiptSourceHeadCode": $("#receiptSourceHeadCodeSearch").val(), "payer": $("#payerSearch").val()},
			async: false,
			dataType: "html",
			cache: false,
			success: function(data){
				redragonJS.removeLoading("ibox-content");
				if(data!=""){
					$("#selectBillModal").html(data);
					$('#selectBillDiv').modal('show');
				}
			},
			error: function(XMLHttpRequest, textStatus, errorThrown){
				redragonJS.alert(textStatus);
			}
		});
	}
</script>       