package vn.gpay.jitin.core.api.salebill;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import vn.gpay.jitin.core.base.ResponseBase;
import vn.gpay.jitin.core.base.ResponseError;
import vn.gpay.jitin.core.salebill.ISaleBillService;
import vn.gpay.jitin.core.salebill.SaleBill;
import vn.gpay.jitin.core.salebill.SaleBillEpc;
import vn.gpay.jitin.core.security.GpayAuthentication;
import vn.gpay.jitin.core.utils.ResponseMessage;
import vn.gpay.jitin.core.utils.WarehouseEventType;
import vn.gpay.jitin.core.warehouse.IWarehouseService;
import vn.gpay.jitin.core.warehouse.IWarehouse_logs_Service;
import vn.gpay.jitin.core.warehouse.Warehouse_logs;

@RestController
@RequestMapping("/api/v1/salebill")
public class SaleBillAPI {

	@Autowired ISaleBillService saleBillService;
	@Autowired IWarehouseService warehouseService;
	@Autowired	IWarehouse_logs_Service warehouse_logs_Service;
	
	@RequestMapping(value = "/salebill_create",method = RequestMethod.POST)
	@Transactional(rollbackFor = RuntimeException.class)
	public ResponseEntity<?> SaleBillCreate(@RequestBody SaleBillCreateRequest entity,HttpServletRequest request ) {
		ResponseBase response = new ResponseBase();
		try {
			GpayAuthentication user = (GpayAuthentication)SecurityContextHolder.getContext().getAuthentication();
			if(entity.data.size()>0) {
				UUID uuid = UUID.randomUUID();
			    String billcode = uuid.toString();
				SaleBill saleBill = entity.data.get(0);
				if(saleBill.getId()==null || saleBill.getId()==0) {
					saleBill.setBillcode(billcode);
					saleBill.setUsercreateid_link(user.getUserId());
					saleBill.setOrgid_link(user.getRootorgid_link());
					saleBill.setOrgbillid_link(user.getOrgId());
					saleBill.setTimecreate(new Date());
					saleBill.setStatus(0);
			    }else {
			    	saleBill.setOrgid_link(user.getRootorgid_link());
					saleBill.setOrgbillid_link(user.getOrgId());
			    	saleBill.setLastuserupdateid_link(user.getUserId());
			    	saleBill.setLasttimeupdate(new Date());
			    }
				saleBill.getSkus().forEach(sku -> {
					sku.setOrgid_link(user.getRootorgid_link());
					if(sku.getId()==null || sku.getId()==0) {
						sku.setUsercreateid_link(user.getUserId());
						sku.setOrgid_link(user.getRootorgid_link());
						sku.setTimecreate(new Date());					
					}else {
						sku.setLastuserupdateid_link(user.getUserId());
						sku.setOrgid_link(user.getRootorgid_link());
						sku.setLasttimeupdate(new Date());
					}
					
		    	});
				
				List<Warehouse_logs> lswarehouse_logs =  new ArrayList<Warehouse_logs>();
				for(SaleBillEpc epc:saleBill.getEpcs()){
					epc.setOrgid_link(user.getRootorgid_link());
					if(epc.getId()==null || epc.getId()==0) {
						epc.setUsercreateid_link(user.getUserId());
						epc.setOrgid_link(user.getRootorgid_link());
						epc.setTimecreate(new Date());
						
			    		//mark epc soldout from warehouse of the shop
			    		if (warehouseService.setEpcSoldout(epc.getOldepc().length()>0?epc.getOldepc():epc.getEpc())){
							//Tao Warehouse_logs trong danh sach de update sau
							Warehouse_logs warehouse_logs = new Warehouse_logs();
							warehouse_logs.setEpc(epc.getEpc());
							warehouse_logs.setOrglogid_link(user.getOrgId());
							warehouse_logs.setEventtype(WarehouseEventType.EVENT_TYPE_SOLDOUT);
							warehouse_logs.setEvent_objid_link(null);
							warehouse_logs.setTimelog(new Date());
							lswarehouse_logs.add(warehouse_logs);
			    		} else {
			    			throw new RuntimeException("Không tìm thấy chíp: " + epc.getEpc());
			    		}
					}else {
						epc.setLastuserupdateid_link(user.getUserId());
						epc.setOrgid_link(user.getRootorgid_link());
						epc.setLasttimeupdate(new Date());
					}
					
		    	}
				
				//Ghi hoa don ban hang
				saleBill = saleBillService.save(saleBill);
				
				//Ghi Warehouse_logs
		    	for(Warehouse_logs ws_log:lswarehouse_logs){
		    		ws_log.setEvent_objid_link(saleBill.getId());
		    		warehouse_logs_Service.save(ws_log);
		    	}
			}
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
		    return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@RequestMapping(value = "/salebill_list",method = RequestMethod.POST)
	public ResponseEntity<?> SalebillList(@RequestBody SaleBillListRequest entity, HttpServletRequest request ) {
		SaleBillResponse response = new SaleBillResponse();
		try {
			GpayAuthentication user = (GpayAuthentication)SecurityContextHolder.getContext().getAuthentication();
			if(entity.billcode==null) entity.billcode="";
			Long orgbillid_link = entity.orgbillid_link;
			if(!user.isOrgRoot()) {
				orgbillid_link=user.getOrgId();
			}		
			response.data = saleBillService.salebill_list(orgbillid_link,entity.billcode, entity.salebilldate_from, entity.salebilldate_to);
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<SaleBillResponse>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@RequestMapping(value = "/salebill_getbyid",method = RequestMethod.POST)
	public ResponseEntity<?> SalebillGetByID(@RequestBody GetSaleBillByIDRequest entity, HttpServletRequest request ) {
		SaleBillByIDResponse response = new SaleBillByIDResponse();
		try {
			response.data = saleBillService.findOne(entity.id);
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<SaleBillByIDResponse>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/salebill_deleteid",method = RequestMethod.POST)
	public ResponseEntity<?> SalebillDeleteByID(@RequestBody GetSaleBillByIDRequest entity, HttpServletRequest request ) {
		ResponseBase response = new ResponseBase();
		try {
			saleBillService.deleteById(entity.id);
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
