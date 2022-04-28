package vn.gpay.jitin.core.api.stockout;


import java.util.Date;
import java.util.List;

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

import vn.gpay.jitin.core.api.warehouse.getby_org_response;
import vn.gpay.jitin.core.base.ResponseBase;
import vn.gpay.jitin.core.base.ResponseError;
import vn.gpay.jitin.core.product.IProductService;
import vn.gpay.jitin.core.rfprint_stockin.IRFPrint_StockinService;
import vn.gpay.jitin.core.security.GpayAuthentication;
import vn.gpay.jitin.core.security.GpayUser;
import vn.gpay.jitin.core.sku.ISKU_Service;
import vn.gpay.jitin.core.stockout.IStockOutDService;
import vn.gpay.jitin.core.stockout.IStockOutPklistService;
import vn.gpay.jitin.core.stockout.IStockOutService;
import vn.gpay.jitin.core.stockout.StockOut;
import vn.gpay.jitin.core.stockout.StockOutD;
import vn.gpay.jitin.core.stockout.StockOutPklist;
import vn.gpay.jitin.core.utils.ResponseMessage;
import vn.gpay.jitin.core.utils.StockoutStatus;
import vn.gpay.jitin.core.utils.WareHouseStatus;
import vn.gpay.jitin.core.warehouse.IWarehouseService;
import vn.gpay.jitin.core.warehouse.Warehouse;

@RestController
@RequestMapping("/api/v1/stockout_pklist")
public class StockOut_Pklist_API {
	@Autowired IStockOutService stockOutService;
	@Autowired IStockOutDService stockOutDService;
	@Autowired IStockOutPklistService stockOutPklistService;
	@Autowired ISKU_Service skuService;
	@Autowired IRFPrint_StockinService rfprintService;
	@Autowired IProductService productService;
	@Autowired IWarehouseService warehouseService;
	
	@RequestMapping(value = "/pklist_create",method = RequestMethod.POST)
	@Transactional(rollbackFor = RuntimeException.class)
	public ResponseEntity<?> Pklist_Create(@RequestBody Stockout_PklistUpdateRequest entity, HttpServletRequest request ) {
		ResponseBase response = new ResponseBase();
		try {
			StockOutD theStockOutD = entity.data_stockout_d;
			
			//Check Stockout và update trạng thái về Đang nhặt hàng
			StockOut theStockout = stockOutService.findOne(theStockOutD.getStockoutid_link());
			if (null!=theStockout){
				if (theStockOutD.getStockout_packinglist().size() > 0){
					theStockout.setStatus(StockoutStatus.STOCKOUT_STATUS_PICKING);
				}
				//Xóa toàn bộ danh sách Pklist cũ của dòng StockOutD và thêm lại danh sách mới
				//Xóa PKList
				stockOutPklistService.deleteByStockOutD(theStockOutD.getId());
				
				//Xóa toàn bộ Id của Pklist (nếu có) để thêm mới
				for(StockOutPklist thePklist:theStockOutD.getStockout_packinglist()){
					thePklist.setId(null);
					thePklist.setStockoutid_link(theStockOutD.getStockoutid_link());
					thePklist.setOrgrootid_link(theStockOutD.getOrgrootid_link());
					thePklist.setLasttimeupdate(new Date());
					thePklist.setStatus(StockoutStatus.STOCKOUT_EPC_STATUS_ERR);//Chua kiem tra
				}
				//Cap nhat tong so cay da nhat
				theStockOutD.setTotalpackagecheck(theStockOutD.getStockout_packinglist().size());
				
				stockOutDService.save(theStockOutD);
				response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
				response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
				return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);	
			} else {
				response.setRespcode(ResponseMessage.KEY_RC_RS_NOT_FOUND);
				response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_RS_NOT_FOUND));
				return new ResponseEntity<ResponseBase>(response,HttpStatus.BAD_REQUEST);	
			}

		}catch (RuntimeException e) {
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(null==e.getMessage()?"Lỗi hệ thống! Liên hệ IT để được hỗ trợ":e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value = "/pklist_create_single",method = RequestMethod.POST)
//	@Transactional(rollbackFor = RuntimeException.class)
	public ResponseEntity<getby_org_response> pklist_create_single( @RequestBody Stockout_PklistUpdateRequest entity,HttpServletRequest request ) {
		getby_org_response  response= new getby_org_response();
		try {
			GpayAuthentication user = (GpayAuthentication)SecurityContextHolder.getContext().getAuthentication();
			Date currentDate = new Date();
			StockOutPklist stockOutPklist = entity.data_pklist;
			
			// update StockOutPklist
			// - Tìm StockOutPklist đã tồn tại hay chưa
			Long stockoutdid_link = stockOutPklist.getStockoutdid_link();
//			String lotnumber = stockOutPklist.getLotnumber();
//			Integer packageid = stockOutPklist.getPackageid();
			String epc = stockOutPklist.getEpc();
			
			List<Warehouse> warehouse_list = warehouseService.findMaterialByEPC(epc);
			Warehouse warehouse = warehouse_list.get(0);
			
			List<StockOutPklist> listStockOutPklist = stockOutPklistService.getByEpc(
					stockoutdid_link, epc);
			StockOutD stockOutD;
			if(listStockOutPklist.size() == 0) { // chưa có -> thêm mới vào StockOutPklist
				stockOutPklist.setId(null);
				stockOutPklist.setLotnumber(warehouse.getLotnumber());
				stockOutPklist.setPackageid(warehouse.getPackageid());
				stockOutPklist.setOrgrootid_link(user.getRootorgid_link());
				stockOutPklist.setLasttimeupdate(currentDate);
				stockOutPklist.setLastuserupdateid_link(user.getUserId());
				stockOutPklistService.save(stockOutPklist);
				
				// update lại StockOutD
				stockOutD = stockOutDService.findOne(stockOutPklist.getStockoutdid_link());
				stockOutD.setLasttimeupdate(currentDate);
				stockOutD.setLastuserupdateid_link(user.getUserId());
				Float totalmetcheck = stockOutD.getTotalmet_check() == null ? 0 : stockOutD.getTotalmet_check();
				stockOutD.setTotalmet_check(totalmetcheck + stockOutPklist.getMet_check());
				Float totalydscheck = stockOutD.getTotalydscheck() == null ? 0 : stockOutD.getTotalydscheck();
				stockOutD.setTotalydscheck(totalydscheck + stockOutPklist.getYdscheck());
				Integer totalpackagecheck = stockOutD.getTotalpackagecheck() == null ? 0 : stockOutD.getTotalpackagecheck();
				stockOutD.setTotalpackagecheck(totalpackagecheck + 1);
				stockOutD = stockOutDService.save(stockOutD);
			}else { // có update
				StockOutPklist oldStockOutPklist = listStockOutPklist.get(0);
				oldStockOutPklist.setLotnumber(warehouse.getLotnumber());
				oldStockOutPklist.setPackageid(warehouse.getPackageid());
				oldStockOutPklist.setLasttimeupdate(currentDate);
				oldStockOutPklist.setLastuserupdateid_link(user.getUserId());
				oldStockOutPklist.setMet_check(stockOutPklist.getMet_check());
				oldStockOutPklist.setYdscheck(stockOutPklist.getYdscheck());
				oldStockOutPklist.setWidthcheck(stockOutPklist.getWidthcheck());
				oldStockOutPklist = stockOutPklistService.save(oldStockOutPklist);
				
				// update lại StockOutD
				stockOutD = stockOutDService.findOne(stockOutPklist.getStockoutdid_link());
				Float totalmetcheck = (float) 0;
				Float totalydscheck = (float) 0;
				Integer totalpackagecheck = 0;
				for(StockOutPklist item : stockOutD.getStockout_packinglist()) {
					if(item.getId().equals(oldStockOutPklist.getId())) {
						totalmetcheck+=oldStockOutPklist.getMet_check();
						totalydscheck+=oldStockOutPklist.getYdscheck();
					}else {
						totalmetcheck+=item.getMet_check();
						totalydscheck+=item.getYdscheck();
					}
					totalpackagecheck++;
				}
				stockOutD.setTotalmet_check(totalmetcheck);
				stockOutD.setTotalydscheck(totalydscheck);
				stockOutD.setTotalpackagecheck(totalpackagecheck);
				stockOutD = stockOutDService.save(stockOutD);
			}
			
			// update lại status của stockout
			if(stockOutD != null) {
				StockOut stockOut = stockOutService.findOne(stockOutD.getStockoutid_link());
//				List<StockOutD> stockOutD_list = stockOut.getStockout_d();
				boolean isComplete = true;
//				for(StockOutD item : stockOutD_list) {
//					Float totalmet = item.getTotalmet_origin() == null ? 0 : item.getTotalmet_origin();
//					Float totalmetcheck = item.getTotalmet_check() == null ? 0 : item.getTotalmet_check();
//					if(totalmetcheck < totalmet) {
//						isComplete = false;
//						break;
//					}
//				}
				if(isComplete) {
					stockOut.setStatus(StockoutStatus.STOCKOUT_STATUS_OK);
				}else {
					stockOut.setStatus(StockoutStatus.STOCKOUT_STATUS_PICKING);
				}
				stockOut.setLasttimeupdate(currentDate);
				stockOut.setLastuserupdateid_link(user.getUserId());
				stockOut = stockOutService.save(stockOut);
			}
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<getby_org_response>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			response.setRespcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<getby_org_response>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/getByStockoutDLotAndPackageId",method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> getByStockoutDLotAndPackageId(@RequestBody StockoutPkl_getByStockoutDLotAndPackageId_request entity,HttpServletRequest request ) {
		StockOut_Pklist_response response = new StockOut_Pklist_response();
		try {
			
			List<StockOutPklist> list = stockOutPklistService.getByLotnumberAndPackageId(entity.stockoutdid_link, entity.lotnumber, entity.packageid);
			for(StockOutPklist item : list) {
				if(item.getEpc() != null) {
					List<Warehouse> warehouse_list = warehouseService.findMaterialByEPC(item.getEpc());
					if(warehouse_list.size() > 0) {
						Warehouse warehouse = warehouse_list.get(0);
						if(item.getStatus() == StockoutStatus.STOCKOUT_EPC_STATUS_RIP) {
							// StockOutPklist đã xé -> warehouse giá trị đã thay đổi -> set vải còn là như warehouse
							item.setMet_remain(warehouse.getMet());
							item.setYds_remain(warehouse.getYds());
						}else {
							// StockOutPklist chưa xé -> warehouse giá trị vân như ban đầu -> set vải còn là 0
							item.setMet_remain((float) 0);
							item.setYds_remain((float) 0);
						}
						item.setMet_remain_and_check(item.getMet_remain() + item.getMet_check());
						item.setYds_remain_and_check(item.getYds_remain() + item.getYdscheck());
					}
				}
			}
			response.data = list;
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<ResponseBase>(response,HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value = "/getbystockoutdid",method = RequestMethod.POST)
	public ResponseEntity<?> getbystockoutdid(@RequestBody stockout_getbyid_request entity, HttpServletRequest request ) {
		StockOut_Pklist_response response = new StockOut_Pklist_response();
		try { 
//			GpayUser user = (GpayUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Long id = entity.id;
			List<StockOutPklist> list = stockOutPklistService.getBystockOutDId(id);
			response.data = list;
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);	
			
		}catch (RuntimeException e) {
			e.printStackTrace();
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(null==e.getMessage()?"Lỗi hệ thống! Liên hệ IT để được hỗ trợ":e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value = "/getbystockoutdid_rip",method = RequestMethod.POST)
	public ResponseEntity<?> getbystockoutdid_rip(@RequestBody stockout_getbyid_request entity, HttpServletRequest request ) {
		StockOut_Pklist_response response = new StockOut_Pklist_response();
		try { 
//			GpayUser user = (GpayUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Long id = entity.id;
			List<StockOutPklist> stockOutPklist_list = stockOutPklistService.getBystockOutDId_rip(id);
			for(StockOutPklist item : stockOutPklist_list) { 
				// Tìm cây vải warehouse tương ứng cây vải StockOutPklist để lấy giá trị vải còn (cho xé vải)
				if(item.getEpc() != null) {
					List<Warehouse> warehouse_list = warehouseService.findMaterialByEPC(item.getEpc());
					if(warehouse_list.size() > 0) {
						Warehouse warehouse = warehouse_list.get(0);
						item.setMet_remain(warehouse.getMet());
						item.setYds_remain(warehouse.getYds());
						item.setMet_remain_and_check(item.getMet_remain() + item.getMet_check());
						item.setYds_remain_and_check(item.getYds_remain() + item.getYdscheck());
					}
				}
			}
			response.data = stockOutPklist_list;
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);	
			
		}catch (RuntimeException e) {
			e.printStackTrace();
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(null==e.getMessage()?"Lỗi hệ thống! Liên hệ IT để được hỗ trợ":e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value = "/update_rip",method = RequestMethod.POST)
	public ResponseEntity<?> update_rip(@RequestBody StockOut_Pklist_rip_request entity, HttpServletRequest request ) {
		StockOut_Pklist_rip_response response = new StockOut_Pklist_rip_response();
		try { 
			GpayUser user = (GpayUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Date date = new Date();
			Long id = entity.id;
//			String lotnumber = entity.lotnumber;
//		    Integer packageid = entity.packageid;
		    Float met_check = entity.met_check;
		    Float ydscheck = entity.ydscheck;
		    Float met_remain = entity.met_remain;
		    Float yds_remain= entity.yds_remain;
		    
		    StockOutPklist stockOutPklist = stockOutPklistService.findOne(id);
		    String epc = stockOutPklist.getEpc();
		    if(epc == null) {
		    	ResponseError errorBase = new ResponseError();
		    	errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
				errorBase.setMessage("StockOutPklist epc = null");
			    return new ResponseEntity<>(errorBase, HttpStatus.BAD_REQUEST);
		    }
		    
		    // Tìm cây vải trong warehouse có epc = StockOutPklist epc
		    List<Warehouse> warehouseList = warehouseService.findMaterialByEPC(epc);
		    if(warehouseList.size() == 0) {
		    	ResponseError errorBase = new ResponseError();
		    	errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
				errorBase.setMessage("Không tồn tại cây vải trong kho có epc này");
			    return new ResponseEntity<>(errorBase, HttpStatus.BAD_REQUEST);
		    }

		    // update Warehouse
		    Warehouse warehouse = warehouseList.get(0);
		    warehouse.setMet(met_remain);
		    warehouse.setYds(yds_remain);
		    warehouse.setLasttimeupdate(date);
		    warehouse.setLastuserupdateid_link(user.getId());
		    warehouse = warehouseService.save(warehouse);
		    
		    // update StockOutPklist
		    stockOutPklist.setMet_check(met_check);
		    stockOutPklist.setYdscheck(ydscheck);
		    stockOutPklist.setLasttimeupdate(date);
		    stockOutPklist.setLastuserupdateid_link(user.getId());
		    stockOutPklist.setStatus(StockoutStatus.STOCKOUT_EPC_STATUS_RIP);
		    stockOutPklist = stockOutPklistService.save(stockOutPklist);
		    
		    response.warehouse = warehouse;
		    response.stockOutPklist = stockOutPklist;
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);	
			
		}catch (RuntimeException e) {
			e.printStackTrace();
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(null==e.getMessage()?"Lỗi hệ thống! Liên hệ IT để được hỗ trợ":e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value = "/pklist_delete",method = RequestMethod.POST)
//	@Transactional(rollbackFor = RuntimeException.class)
	public ResponseEntity<?> Pklist_Delete(@RequestBody stockout_getbyid_request entity, HttpServletRequest request ) {
		StockOut_Create_response response = new StockOut_Create_response();
		try {
			GpayUser user = (GpayUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Long id = entity.id;
			StockOutPklist stockOutPklist = stockOutPklistService.findOne(id);
			Long stockOutDId = stockOutPklist.getStockoutdid_link();
			
			// update lại warehouse nếu cây vải là xé
			if(stockOutPklist.getStatus().equals(StockoutStatus.STOCKOUT_EPC_STATUS_RIP)) {
				List<Warehouse> warehouse_list = warehouseService.findMaterialByEPC(stockOutPklist.getEpc());
				if(warehouse_list.size() > 0) {
					Warehouse warehouse = warehouse_list.get(0);
					Float ydscheck = stockOutPklist.getYdscheck() == null ? (float)0 : stockOutPklist.getYdscheck();
					Float metcheck = stockOutPklist.getMet_check() == null ? (float)0 : stockOutPklist.getMet_check();
					Float yds = warehouse.getYds() == null ? (float)0 : warehouse.getYds();
					Float met = warehouse.getMet() == null ? (float)0 : warehouse.getMet();
					warehouse.setYds(ydscheck + yds);
					warehouse.setMet(metcheck + met);
					warehouse.setLastuserupdateid_link(user.getId());
					warehouse.setLasttimeupdate(new Date());
					warehouseService.save(warehouse);
				}
			}
			
			// delete pkl
			stockOutPklistService.delete(stockOutPklist);
			
			//Update lại số tổng Check trong Stockout_d
			stockOutDService.recal_Totalcheck(stockOutDId);
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			e.printStackTrace();
//			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(null==e.getMessage()?"Lỗi hệ thống! Liên hệ IT để được hỗ trợ":e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value = "/pklist_delete_rip",method = RequestMethod.POST)
//	@Transactional(rollbackFor = RuntimeException.class)
	public ResponseEntity<?> Pklist_Delete_Rip(@RequestBody stockout_getbyid_request entity, HttpServletRequest request ) {
		StockOut_Create_response response = new StockOut_Create_response();
		try {
			GpayUser user = (GpayUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Long id = entity.id;
			StockOutPklist stockOutPklist = stockOutPklistService.findOne(id);
			Long stockOutDId = stockOutPklist.getStockoutdid_link();
			Date date = new Date();
			
			// update lại warehouse nếu cây vải là xé
			if(stockOutPklist.getStatus().equals(StockoutStatus.STOCKOUT_EPC_STATUS_RIP)) {
				List<Warehouse> warehouse_list = warehouseService.findMaterialByEPC(stockOutPklist.getEpc());
				if(warehouse_list.size() > 0) {
					Warehouse warehouse = warehouse_list.get(0);
					Float ydscheck = stockOutPklist.getYdscheck() == null ? (float)0 : stockOutPklist.getYdscheck();
					Float metcheck = stockOutPklist.getMet_check() == null ? (float)0 : stockOutPklist.getMet_check();
					Float yds = warehouse.getYds() == null ? (float)0 : warehouse.getYds();
					Float met = warehouse.getMet() == null ? (float)0 : warehouse.getMet();
					warehouse.setYds(ydscheck + yds);
					warehouse.setMet(metcheck + met);
					warehouse.setLastuserupdateid_link(user.getId());
					warehouse.setLasttimeupdate(date);
					warehouseService.save(warehouse);
					
					// set pkl status
					stockOutPklist.setStatus(StockoutStatus.STOCKOUT_EPC_STATUS_OK);
					stockOutPklist.setMet_check(metcheck + met);
				    stockOutPklist.setYdscheck(ydscheck + yds);
				    stockOutPklist.setLasttimeupdate(date);
				    stockOutPklist.setLastuserupdateid_link(user.getId());
				    stockOutPklistService.save(stockOutPklist);
				}
			}
			
			//Update lại số tổng Check trong Stockout_d
			stockOutDService.recal_Totalcheck(stockOutDId);
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			e.printStackTrace();
//			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(null==e.getMessage()?"Lỗi hệ thống! Liên hệ IT để được hỗ trợ":e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value = "/stockout_pklist_themMatTem",method = RequestMethod.POST)
//	@Transactional(rollbackFor = RuntimeException.class)
	public ResponseEntity<?> stockout_pklist_themMatTem(@RequestBody StockOut_Pklist_themMatTem_request entity, HttpServletRequest request ) {
		StockOut_Create_response response = new StockOut_Create_response();
		try {
			GpayUser user = (GpayUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Warehouse warehouse = entity.warehouse;
			Long skuid_link = entity.skuid_link;
			Long stockoutid_link = entity.stockoutid_link;
			Long stockoutdid_link = entity.stockoutdid_link;
			
			if(warehouse.getLotnumber() != null) {
				warehouse.setLotnumber(warehouse.getLotnumber().toUpperCase());
			}
			
//			StockOut stockout = stockOutService.findOne(stockoutid_link);
			StockOutPklist stockOutPklist = new StockOutPklist();
			stockOutPklist.setOrgrootid_link(user.getRootorgid_link());
			stockOutPklist.setStockoutid_link(stockoutid_link);
			stockOutPklist.setStockoutdid_link(stockoutdid_link);
			stockOutPklist.setSkuid_link(skuid_link);
			stockOutPklist.setLotnumber(warehouse.getLotnumber());
			stockOutPklist.setPackageid(warehouse.getPackageid());
			stockOutPklist.setMet_origin(warehouse.getMet());
			stockOutPklist.setMet_check(warehouse.getMet());
			stockOutPklist.setYdsorigin((float) (warehouse.getMet() / 0.9144));
			stockOutPklist.setYdscheck((float) (warehouse.getMet() / 0.9144));
			stockOutPklist.setWidthorigin(warehouse.getWidth_met() / 100);
			stockOutPklist.setWidthcheck(warehouse.getWidth_met() / 100);
			stockOutPklist.setRssi(1);
			stockOutPklist.setStatus(StockoutStatus.STOCKOUT_EPC_STATUS_OK);
			stockOutPklist.setUnitid_link(1);
			stockOutPklist.setWarehousestatus(WareHouseStatus.WAREHOUSE_STATUS_UNCHECKED);
			stockOutPklist = stockOutPklistService.save(stockOutPklist);
			
			//Update lại số tổng Check trong Stockout_d
			stockOutDService.recal_Totalcheck(stockoutdid_link);
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			e.printStackTrace();
//			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(null==e.getMessage()?"Lỗi hệ thống! Liên hệ IT để được hỗ trợ":e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.BAD_REQUEST);
		}
	}
}