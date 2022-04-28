package vn.gpay.jitin.core.api.stockin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

//import vn.gpay.jitin.core.api.stockin.StockinGettypeRequest;
import vn.gpay.jitin.core.porder_grant.IPOrderGrant_Service;
import vn.gpay.jitin.core.porder_grant.POrder_Grant;
import vn.gpay.jitin.core.porderprocessing.IPOrderProcessing_Service;
import vn.gpay.jitin.core.porderprocessing.POrderProcessing;
import vn.gpay.jitin.core.utils.DateFormat;
import vn.gpay.jitin.core.utils.POrderStatus;
import vn.gpay.jitin.core.security.GpayUserOrg;
import vn.gpay.jitin.core.security.IGpayUserOrgService;
import vn.gpay.jitin.core.porders_poline.IPOrder_POLine_Service;
import vn.gpay.jitin.core.JitinCommon;
import vn.gpay.jitin.core.Log4jCommon;
import vn.gpay.jitin.core.base.ResponseBase;
import vn.gpay.jitin.core.base.ResponseError;
import vn.gpay.jitin.core.invoice.IInvoiceService;
import vn.gpay.jitin.core.org.IOrgService;
import vn.gpay.jitin.core.org.Org;
import vn.gpay.jitin.core.packinglist.IPackingListService;
import vn.gpay.jitin.core.packinglist.PackingList;
import vn.gpay.jitin.core.pcontract.IPContractService;
import vn.gpay.jitin.core.pcontract_po.IPContract_POService;
import vn.gpay.jitin.core.pcontract_po.PContract_PO;
import vn.gpay.jitin.core.porder.IPOrderService;
import vn.gpay.jitin.core.porder.POrder;
import vn.gpay.jitin.core.security.GpayAuthentication;
import vn.gpay.jitin.core.security.GpayUser;
import vn.gpay.jitin.core.security.IGpayUserService;
import vn.gpay.jitin.core.sku.ISKU_Service;
import vn.gpay.jitin.core.sku.SKU;
import vn.gpay.jitin.core.stockin.IStockInDService;
import vn.gpay.jitin.core.stockin.IStockInPklistService;
import vn.gpay.jitin.core.stockin.IStockInService;
import vn.gpay.jitin.core.stockin.IStockinLotService;
import vn.gpay.jitin.core.stockin.StockIn;
import vn.gpay.jitin.core.stockin.StockInD;
import vn.gpay.jitin.core.stockin.StockInPklist;
import vn.gpay.jitin.core.stockin.StockinObj;
import vn.gpay.jitin.core.stockin.StockinProduct;
import vn.gpay.jitin.core.stockin_type.IStockinTypeService;
import vn.gpay.jitin.core.stocking_uniquecode.IStocking_UniqueCode_Service;
import vn.gpay.jitin.core.stocking_uniquecode.Stocking_UniqueCode;
import vn.gpay.jitin.core.stockout.IStockOutService;
import vn.gpay.jitin.core.stockout.StockOut;
import vn.gpay.jitin.core.tagencode.IWareHouse_Encode_EPC_Service;
import vn.gpay.jitin.core.utils.AtributeFixValues;
import vn.gpay.jitin.core.utils.EPCStockStatus;
import vn.gpay.jitin.core.utils.OrgType;
import vn.gpay.jitin.core.utils.ResponseMessage;
import vn.gpay.jitin.core.utils.StockinStatus;
import vn.gpay.jitin.core.utils.StockinTypeConst;
import vn.gpay.jitin.core.utils.StockoutStatus;
import vn.gpay.jitin.core.utils.WareHouseStatus;
import vn.gpay.jitin.core.utils.WarehouseEventType;
import vn.gpay.jitin.core.warehouse.IWarehouseService;
import vn.gpay.jitin.core.warehouse.IWarehouse_logs_Service;
import vn.gpay.jitin.core.warehouse.Warehouse;
import vn.gpay.jitin.core.warehouse.Warehouse_logs;

@RestController
@RequestMapping("/api/v1/stockin")
public class StockInAPI {

	@Autowired IStockInService stockInService;
	@Autowired IStockInDService stockInDService;
	@Autowired IPackingListService packingListService;
	@Autowired IInvoiceService invoiceService;
	@Autowired IStockOutService stockoutService;
	@Autowired IWarehouseService warehouseService;
	@Autowired IWareHouse_Encode_EPC_Service tagencodeService;
	@Autowired ISKU_Service skuService;
	@Autowired IStockinTypeService stockintypeService;
	@Autowired JitinCommon common;
	@Autowired IStocking_UniqueCode_Service stockingService;
	@Autowired	IWarehouse_logs_Service warehouse_logs_Service;
	@Autowired IOrgService orgService;
	@Autowired IGpayUserService userDetailsService;
	@Autowired IStockinLotService stockinLotService;
	@Autowired IStockInPklistService stockInPklistService;
	@Autowired IPOrderProcessing_Service porderProcessingService;
	@Autowired IPOrderGrant_Service porder_GrantService;
	@Autowired IGpayUserOrgService userOrgService;
	@Autowired IPContract_POService pcontractPoService;
	@Autowired IPOrder_POLine_Service porder_line_Service;
	@Autowired IPOrderService porderService;
	@Autowired IPContractService pcontractService;
	
	@RequestMapping(value = "/get",method = RequestMethod.GET)
	public ResponseEntity<?> get() {
		try {
			ResponseBase response = new ResponseBase();
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
	
	@RequestMapping(value = "/stockin_create",method = RequestMethod.POST)
	@Transactional(rollbackFor = RuntimeException.class)
	public ResponseEntity<?> StockinCreate(@RequestBody StockinCreateRequest entity, HttpServletRequest request ) {
		Stockin_create_response response = new Stockin_create_response();
		try {
			if(entity.data.size()>0) {
				GpayAuthentication user = (GpayAuthentication)SecurityContextHolder.getContext().getAuthentication();
				GpayUser app_user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				if (user != null) {
					//Nếu thêm mới isNew = true
					boolean isNew = false;
					
				    StockIn stockin =entity.data.get(0);
				    if(stockin.getId()==null || stockin.getId()==0) {
				    	isNew = true;
				    	stockin.setOrgrootid_link(user.getRootorgid_link());
				    	
				    	//Nếu không cung cấp kho đích, mặc định kho đích là đơn vị của user đăng nhập
				    	if (null == stockin.getOrgid_to_link()){
				    		if (null!= app_user.getOrg_grant_id_link())
				    			stockin.setOrgid_to_link(app_user.getOrg_grant_id_link());
				    		else
				    			stockin.setOrgid_to_link(user.getOrgId());
				    	}
				    	
				    	stockin.setUsercreateid_link(user.getUserId());
				    	stockin.setTimecreate(new Date());
				    	stockin.setLasttimeupdate(new Date());
				    	stockin.setStockincode(common.GetStockinCode());
				    	// status
				    	if(stockin.getStockintypeid_link().equals(StockinTypeConst.STOCKIN_TYPE_NL_NEW)) {
					    	stockin.setStatus(StockinStatus.STOCKIN_STATUS_ERR);
				    	}else if(
				    			stockin.getStockintypeid_link().equals(StockinTypeConst.STOCKIN_TYPE_TP_NEW) ||
				    			stockin.getStockintypeid_link().equals(StockinTypeConst.STOCKIN_TYPE_TP_MOVE)
				    		) {
				    		stockin.setStatus(StockinStatus.STOCKIN_STATUS_OK);
				    	}else {
				    		stockin.setStatus(StockinStatus.STOCKIN_STATUS_ERR);
				    	}
				    }
				    else {
				    	
				    	//Kiem tra xem co thay doi gi ke tu lan lay du lieu ko. Neu co yeu cau get lai du lieu
				    	StockIn stockin_lastupdate = stockInService.findOne(stockin.getId());
				    	if (stockin.getLasttimeupdate().before(stockin_lastupdate.getLasttimeupdate())) {
							ResponseError errorBase = new ResponseError();
							errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
							errorBase.setMessage("Phiếu đã bị sửa bởi người dùng khác. Yêu cầu tải lại phiếu");
						    return new ResponseEntity<>(errorBase, HttpStatus.BAD_REQUEST);
			
				    	}
				    	
				    	//Nếu không cung cấp kho đích, mặc định kho đích là đơn vị của user đăng nhập
				    	if (null == stockin.getOrgid_to_link()){
				    		if (null!= app_user.getOrg_grant_id_link())
				    			stockin.setOrgid_to_link(app_user.getOrg_grant_id_link());
				    		else
				    			stockin.setOrgid_to_link(user.getOrgId());
				    	}

				    	stockin.setOrgrootid_link(user.getRootorgid_link());
				    	stockin.setLastuserupdateid_link(user.getUserId());
				    	stockin.setLasttimeupdate(new Date());
				    	stockin.setStatus(StockinStatus.STOCKIN_STATUS_OK);
				    }
				    
				    List<StockInD> listd = stockin.getStockin_d();
				    List<StockInPklist> epcInCorrect = new ArrayList<StockInPklist>();
 		
				    for (StockInD stockind : listd) {
			    		stockind.setStatus(StockinStatus.STOCKIN_D_STATUS_ERR);
			    		stockind.setOrgrootid_link(user.getRootorgid_link());
			    		
			    		if(stockind.getId()==null || stockind.getId()==0) {
				    		stockind.setUsercreateid_link(user.getUserId());
				    		stockind.setTimecreate(new Date());
				    	}
			    		if(stockin.getStockintypeid_link() >= 1 && stockin.getStockintypeid_link() <= 10) {
				    		//Update lại giá trị của D theo mét hoặc Y
				    		if (null!= stockin.getUnitid_link() && stockin.getUnitid_link() == AtributeFixValues.UNIT_MET){
				    			stockind.setTotalydsorigin((float)(stockind.getTotalmet_origin()*1.09361));
				    			stockind.setTotalydscheck((float)(stockind.getTotalmet_check()*1.09361));
				    		} else if (null!= stockin.getUnitid_link() && stockin.getUnitid_link() == AtributeFixValues.UNIT_YDS){
				    			stockind.setTotalmet_origin((float)(stockind.getTotalydsorigin()*0.9144));
				    			stockind.setTotalmet_check((float)(stockind.getTotalydscheck()*0.9144));
				    		}
			    		}
				    	
				    	// nhập thành phẩm từ tổ hoàn thiện đến kho thành phẩm
		    			if(
		    				stockin.getStockintypeid_link().equals(StockinTypeConst.STOCKIN_TYPE_TP_NEW) ||
		    				stockin.getStockintypeid_link().equals(StockinTypeConst.STOCKIN_TYPE_TP_MOVE)
		    				) {
		    				// chỉ với phiếu chưa duyệt
		    				if(stockin.getStatus() < StockinStatus.STOCKIN_STATUS_APPROVED) {
		    					List<StockInPklist> stockInPklist_list = stockind.getStockin_packinglist();
		    					Integer totalpackagecheck = stockind.getTotalpackagecheck() == null ? 0 : stockind.getTotalpackagecheck();
		    					Integer size = stockInPklist_list == null ? 0 : stockInPklist_list.size();
		    					// nếu số nhận > số pklist, thêm pklist 
		    					if(totalpackagecheck > size) {
//		    						System.out.println("herher ehrh ehr ehreh re");
		    						Integer soThem = totalpackagecheck - size; // số lượng pkl thêm
		    						for(Integer i = 0; i < soThem; i++) {
		    							StockInPklist newPklist = new StockInPklist();
		    							newPklist.setId(null);
		    							newPklist.setStockinid_link(stockin.getId());
		    							newPklist.setStockindid_link(stockind.getId());
		    							newPklist.setSkuid_link(stockind.getSkuid_link());
		    							newPklist.setColorid_link(stockind.getColorid_link());
		    							newPklist.setEpc(null); // tự sinh ở dưới //Neu ko co EPC --> Sinh UUID
		    							newPklist.setStatus(StockinStatus.TP_LOAI1);
		    							newPklist.setRssi(1);
		    							stockInPklist_list.add(newPklist);
		    						}
	    							stockind.setStockin_packinglist(stockInPklist_list);
		    					}
		    					// nếu số nhận < số pklist, xoá bỏ bớt pklist 
		    					if(totalpackagecheck < size) {
		    						Integer soXoa = size - totalpackagecheck; // số lượng pkl xoá
		    						for(Integer i = 0; i < soXoa; i++) {
		    							stockInPklist_list.remove(0);
		    						}
		    					}
		    				}
		    			}
		    			
		    			// nhập mới phụ liệu đến kho phụ liệu
		    			if(stockin.getStockintypeid_link().equals(StockinTypeConst.STOCKIN_TYPE_PL_NEW)) {
		    				// chỉ với phiếu chưa duyệt
		    				if(stockin.getStatus() < StockinStatus.STOCKIN_STATUS_APPROVED) {
		    					List<StockInPklist> stockInPklist_list = stockind.getStockin_packinglist();
		    					Integer totalpackagecheck = stockind.getTotalpackagecheck() == null ? 0 : stockind.getTotalpackagecheck();
		    					Integer size = stockInPklist_list == null ? 0 : stockInPklist_list.size();
		    					// nếu số nhận > số pklist, thêm pklist 
		    					if(totalpackagecheck > size) {
		    						Integer soThem = totalpackagecheck - size; // số lượng pkl thêm
		    						for(Integer i = 0; i < soThem; i++) {
		    							StockInPklist newPklist = new StockInPklist();
		    							newPklist.setId(null);
		    							newPklist.setStockinid_link(stockin.getId());
		    							newPklist.setStockindid_link(stockind.getId());
		    							newPklist.setSkuid_link(stockind.getSkuid_link());
		    							newPklist.setColorid_link(stockind.getColorid_link());
		    							newPklist.setEpc(null); // tự sinh ở dưới //Neu ko co EPC --> Sinh UUID
		    							newPklist.setRssi(1);
		    							stockInPklist_list.add(newPklist);
		    						}
	    							stockind.setStockin_packinglist(stockInPklist_list);
		    					}
		    					// nếu số nhận < số pklist, xoá bỏ bớt pklist 
		    					if(totalpackagecheck < size) {
		    						Integer soXoa = size - totalpackagecheck; // số lượng pkl xoá
		    						for(Integer i = 0; i < soXoa; i++) {
		    							stockInPklist_list.remove(0);
		    						}
		    					}
		    				}
		    			}
		    			
				    	
			    		for (StockInPklist pklist : stockind.getStockin_packinglist()) {
			    			if(null == pklist.getId()) {//Item mới trong Stockin
			    				pklist.setOrgrootid_link(user.getRootorgid_link());
				    			pklist.setId(null);
				    			pklist.setUsercreateid_link(stockind.getUsercreateid_link());
				    			pklist.setTimecreate(new Date());
				    			
				    			//Neu ko co EPC --> Sinh UUID
				    			if (null == pklist.getEpc() || pklist.getEpc().length() == 0){
				    				pklist.setEpc(genUUID());
				    			}
				    			
			    				//Nếu epc đã có trong bảng Warehouse và đang thuộc 1 kho khác, bỏ qua ko nhập kho chíp này
				    			List<Warehouse> ls_EPC = warehouseService.findMaterialByEPC(pklist.getEpc());
				    			if (ls_EPC.size() == 0) {//EPC khong co trong warehouse (hang moi)
				    				//Kiem tra xem hang da co lien ket SKU chua?
				    				//Neu chua co va chi co ma vach --> Tim trong bang SKU
				    				//Neu khong tim thay trong bang SKU --> Bao loi (hang trong warehouse phai co lien ket sku)
				    				SKU theSKU = null;
				    				if (null == pklist.getSkuid_link() && null!=stockind.getSkucode())
				    					theSKU = skuService.get_bySkucode(stockind.getSkucode());
				    				else
				    					theSKU = skuService.findOne(pklist.getSkuid_link());
				    				
			    					if (null != theSKU){
			    						pklist.setSkuid_link(theSKU.getId());
			    						pklist.setColorid_link(theSKU.getColorid_link());
			    						pklist.setSizeid_link(theSKU.getSizeid_link());
			    						pklist.setUnitid_link(theSKU.getUnitid_link());
			    						pklist.setSkutypeid_link(theSKU.getSkutypeid_link());
			    						if(
			    							stockin.getStockintypeid_link().equals(StockinTypeConst.STOCKIN_TYPE_TP_NEW) ||
			    							stockin.getStockintypeid_link().equals(StockinTypeConst.STOCKIN_TYPE_TP_MOVE)
			    							) {
//			    							
			    						}else {
			    							pklist.setStatus(null!=pklist.getRssi() && pklist.getRssi() ==1?StockinStatus.STOCKIN_EPC_STATUS_OK:StockinStatus.STOCKIN_EPC_STATUS_ERR);
			    						}
			    						
			    					} else {
			    						pklist.setSkuid_link(null);
			    						pklist.setStatus(StockinStatus.STOCKIN_EPC_STATUS_ERR_OUTOFSKULIST);
			    						epcInCorrect.add(pklist);
			    						
			    						//Neu hang day len ko co trong bang sku --> loai bo
			    						stockind.setTotalpackage(stockind.getTotalpackage()-1);
			    						stockind.setStatus(StockinStatus.STOCKIN_D_STATUS_ERR);
			    						
//			    						//Bao loi sai SKU
//			    						throw new RuntimeException(ResponseMessage.MES_STOCKIN_SKU_NOTEXIST);
			    					}
			    				}
			    				else {//EPC co trong warehouse
			    					//Neu epc o trang thai -1 (dang logistic) thi duoc phep nhap kho
			    					Warehouse theEPC = ls_EPC.get(0);
			    					if (theEPC.getStockid_link() != EPCStockStatus.EPCSTOCK_ONLOGISTIC){
			    						if(
				    						stockin.getStockintypeid_link().equals(StockinTypeConst.STOCKIN_TYPE_TP_NEW) ||
				    						stockin.getStockintypeid_link().equals(StockinTypeConst.STOCKIN_TYPE_TP_MOVE)
				    						) {
			    							epcInCorrect.add(pklist);
			    						}else {
					    					pklist.setStatus(StockinStatus.STOCKIN_EPC_STATUS_ERR_WAREHOUSEEXISTED);
					    					epcInCorrect.add(pklist);
			    						}
			    					} else {
			    						if(
					    					stockin.getStockintypeid_link().equals(StockinTypeConst.STOCKIN_TYPE_TP_NEW) ||
					    					stockin.getStockintypeid_link().equals(StockinTypeConst.STOCKIN_TYPE_TP_MOVE)
					    					) {
				    						
				    					}else {
				    						pklist.setStatus(null!=pklist.getRssi() && pklist.getRssi() ==1?StockinStatus.STOCKIN_EPC_STATUS_OK:StockinStatus.STOCKIN_EPC_STATUS_ERR);
				    					}
				    				}
			    				}
	    			
			    			} else {
		    					if(
		    						stockin.getStockintypeid_link().equals(StockinTypeConst.STOCKIN_TYPE_TP_NEW) ||
				    				stockin.getStockintypeid_link().equals(StockinTypeConst.STOCKIN_TYPE_TP_MOVE)
		    							) {
		    						// nhap dieu chuyen thanh pham
		    					}else if(stockin.getStockintypeid_link().equals(StockinTypeConst.STOCKIN_TYPE_NL_NEW)) {
	    							// nhap moi nguyen lieu
	    						}else {
				    				pklist.setStatus(null!=pklist.getRssi() && pklist.getRssi() ==1?StockinStatus.STOCKIN_EPC_STATUS_OK:StockinStatus.STOCKIN_EPC_STATUS_ERR);
				    			}
			    			}
			    			
    						//Update lai so tong hang da kiem tra trong bang D
    						Long totalpackagecheck = stockind.getStockin_packinglist().stream().filter(x->null!=x.getStatus() && x.getStatus() >= StockinStatus.STOCKIN_EPC_STATUS_OK).count();
    				    	if (null!=totalpackagecheck && totalpackagecheck > 0){
	    						stockind.setTotalpackagecheck(totalpackagecheck.intValue());  // loi o doan nay
	    						Double totalmet_check = stockind.getStockin_packinglist().stream().mapToDouble(x->null==x.getMet_check()?0:x.getMet_check()).sum();
	    						stockind.setTotalmet_check(totalmet_check.floatValue());
	    						Double totalyds_check = stockind.getStockin_packinglist().stream().mapToDouble(x->null==x.getYdscheck()?0:x.getYdscheck()).sum();
	    						stockind.setTotalydscheck(totalyds_check.floatValue());
    				    	}
			    		}

				    	//Neu so kiem < so yeu cau --> can luu y
						if (stockin.getStockintypeid_link() >= StockinTypeConst.STOCKIN_TYPE_TP_NEW){
							if (null!=stockind.getTotalpackage()){
								if (stockind.getTotalpackage() > (null==stockind.getTotalpackagecheck()?0:stockind.getTotalpackagecheck()))
									stockind.setStatus(StockinStatus.STOCKIN_D_STATUS_ERR);
								else
									stockind.setStatus(StockinStatus.STOCKIN_D_STATUS_OK);
							}
						} else 
							///////
						if(stockin.getStockintypeid_link() >= StockinTypeConst.STOCKIN_TYPE_NL_NEW && stockin.getStockintypeid_link() <= 10){
							// nhap kho vai, kiem tra tung don vi tinh
							Long unitid_link = stockin.getUnitid_link();
							if(unitid_link.equals((long) 1) || unitid_link.equals((long) 3)) { // do dai
								if (null!=stockind.getTotalmet_origin()){
									if (stockind.getTotalmet_origin() > (null==stockind.getTotalmet_check()?0:stockind.getTotalmet_check())) {
										stockind.setStatus(StockinStatus.STOCKIN_D_STATUS_ERR);
									}
									else {
										stockind.setStatus(StockinStatus.STOCKIN_D_STATUS_OK);
									}
								}
							}
							if(unitid_link.equals((long) 4) || unitid_link.equals((long) 5)) { // trong luong
								if (null!=stockind.getGrossweight()){
									if (stockind.getGrossweight() > (null==stockind.getNetweight()?0:stockind.getNetweight())) {
										stockind.setStatus(StockinStatus.STOCKIN_D_STATUS_ERR);
									}
									else {
										stockind.setStatus(StockinStatus.STOCKIN_D_STATUS_OK);
									}
								}
							}
							
						} else{
							if (null!=stockind.getTotalmet_origin()){
								if (stockind.getTotalmet_origin() > (null==stockind.getTotalmet_check()?0:stockind.getTotalmet_check()))
									stockind.setStatus(StockinStatus.STOCKIN_D_STATUS_ERR);
								else
									stockind.setStatus(StockinStatus.STOCKIN_D_STATUS_OK);
							}
						}
			    	}
				    
			    	if (epcInCorrect.size() > 0){
						response.setRespcode(ResponseMessage.KEY_STOCKIN_WRONGEPC);
						response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_STOCKIN_WRONGEPC));
						return new ResponseEntity<ResponseBase>(response,HttpStatus.BAD_REQUEST);	
			    		
			    	} else {
			    		//Ghi phieu nhap kho
			    		if(stockin.getStockintypeid_link().equals(StockinTypeConst.STOCKIN_TYPE_TP_NEW)) {
			    			// nhập thành phẩm từ tổ hoàn thiện đến kho thành phẩm
//			    			Long isChecked = stockin.getStockin_d().stream().filter(x->x.getTotalpackagecheck() > 0 && x.getTotalpackagecheck() != null).count();
//			    			stockin.setStatus(isChecked == 0?StockinStatus.STOCKIN_STATUS_ERR:StockinStatus.STOCKIN_STATUS_OK);
			    		}else if(stockin.getStockintypeid_link().equals(StockinTypeConst.STOCKIN_TYPE_PL_NEW)){
			    			// nhập mới phụ liệu
			    			Long isChecked = stockin.getStockin_d().stream().filter(x->x.getTotalpackagecheck() > 0 && x.getTotalpackagecheck() != null).count();
			    			stockin.setStatus(isChecked == 0?StockinStatus.STOCKIN_STATUS_ERR:StockinStatus.STOCKIN_STATUS_OK);
			    		}else {
//			    			Long totalerr = stockin.getStockin_d().stream().filter(x->x.getStatus() == StockinStatus.STOCKIN_D_STATUS_ERR).count();
//							stockin.setStatus(totalerr > 0?StockinStatus.STOCKIN_STATUS_ERR:StockinStatus.STOCKIN_STATUS_OK);
			    		}
			    		
//						Integer totalpakage_check = stockin.getStockin_d().stream().mapToInt(x->null==x.getTotalpackagecheck()?0:x.getTotalpackagecheck()).sum();
//						stockin.setTotalpackage(totalpakage_check);
				    	stockin = stockInService.save(stockin);
						
						//Cập nhật lại bảng unique_code khi thêm mới
						if(isNew) {
							Stocking_UniqueCode unique = stockingService.getby_type(1);
							unique.setStocking_max(unique.getStocking_max()+ 1);
							stockingService.save(unique);
						}
						
						response.data = stockin;
						response.id = stockin.getId();
						response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
						response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
						return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);	
			    	}
				}
				else {
					response.setRespcode(ResponseMessage.KEY_RC_AUTHEN_ERROR);
					response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_AUTHEN_ERROR));
					return new ResponseEntity<ResponseBase>(response,HttpStatus.BAD_REQUEST);						
				}
			}
			else {
				response.setRespcode(ResponseMessage.KEY_STOCKIN_NOSKU);
				response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_STOCKIN_NOSKU));
			    return new ResponseEntity<ResponseBase>(response,HttpStatus.BAD_REQUEST);
			}

		}catch (RuntimeException e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			e.printStackTrace();
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(null==e.getMessage()?"Lỗi hệ thống! Liên hệ IT để được hỗ trợ":e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value = "/stockin_create_material",method = RequestMethod.POST)
	@Transactional(rollbackFor = RuntimeException.class)
	public ResponseEntity<?> StockinCreate_Material(@RequestBody StockinCreateRequest entity, HttpServletRequest request ) {
		Stockin_create_response response = new Stockin_create_response();
//		List<StockInPklist> epcInCorrect = new ArrayList<StockInPklist>();
		try {
			if(entity.data.size()>0) {
				GpayAuthentication user = (GpayAuthentication)SecurityContextHolder.getContext().getAuthentication();
				if (user != null) {
					//Nếu thêm mới isNew = true
					boolean isNew = false;
					
				    StockIn stockin =entity.data.get(0);
				    boolean isStockinStatusOK = true;
				    if(stockin.getId()==null || stockin.getId()==0) {
				    	isNew = true;
				    	stockin.setOrgrootid_link(user.getRootorgid_link());
				    	
				    	//Nếu không cung cấp kho đích, mặc định kho đích là đơn vị của user đăng nhập
//				    	if (null == stockin.getOrgid_to_link())
//				    		stockin.setOrgid_to_link(user.getOrgId());
				    	stockin.setUsercreateid_link(user.getUserId());
				    	stockin.setTimecreate(new Date());
				    	stockin.setStockincode(common.GetStockinCode());
				    	stockin.setStatus(0);
				    }
				    else {
				    	stockin.setOrgrootid_link(user.getRootorgid_link());
				    	
				    	//Nếu không cung cấp kho đích, mặc định kho đích là đơn vị của user đăng nhập
//				    	if (null == stockin.getOrgid_to_link())
//				    		stockin.setOrgid_to_link(user.getOrgId());
				    	
				    	stockin.setLastuserupdateid_link(user.getUserId());
				    	stockin.setLasttimeupdate(new Date());
				    }
				    
				    List<StockInD> listd = stockin.getStockin_d();
//				    List<Warehouse_logs> lswarehouse_logs =  new ArrayList<Warehouse_logs>();
		    		
				    for (StockInD stockind : listd) {
				    	boolean isStockinDStatusOK = true;
				    	if(stockind.getId()==null || stockind.getId()==0) {
				    		stockind.setOrgrootid_link(user.getRootorgid_link());
				    		stockind.setUsercreateid_link(user.getUserId());
				    		stockind.setTimecreate(new Date());
				    	}
				    	
				    	Float grossweight = (float)0;
				    	Float netweight = (float)0;
				    	Float m3 = (float)0;
				    	Float morigin = (float)0;
				    	Float mcheck = (float)0;
				    	Float yorigin = (float)0;
				    	Float ycheck = (float)0;
				    	Integer totalpackage = 0;
				    	
				    	for (StockInPklist pklist : stockind.getStockin_packinglist()) {
			    			if(pklist.getId()==null || pklist.getUsercreateid_link()==null || pklist.getUsercreateid_link()==0) {
			    				pklist.setOrgrootid_link(user.getRootorgid_link());
				    			pklist.setId(null);
				    			pklist.setUsercreateid_link(stockind.getUsercreateid_link());
				    			pklist.setTimecreate(new Date());
				    			
				    			//Neu ko co EPC --> Sinh UUID
				    			if (null == pklist.getEpc() || pklist.getEpc().length() == 0){
				    				pklist.setEpc(genUUID());
				    			}
			    			}
			    			pklist.setUnitid_link((stockin.getUnitid_link().intValue()));
			    			
			    			// nếu cây vải status = 0 (ok), nếu status = -1 (thiếu) thì phải có comment
			    			if(pklist.getStatus() == null) {
			    				pklist.setStatus(-1);
			    			}
			    			if(pklist.getComment() == null) {
			    				pklist.setComment("");
			    			}
			    			if(pklist.getStatus() == -1 && pklist.getComment() == "") {
			    				isStockinDStatusOK = false;
			    			}
			    			
			    			totalpackage++;
			    			grossweight+=pklist.getGrossweight() == null ? 0 : pklist.getGrossweight();
			    			netweight+=pklist.getNetweight() == null ? 0 : pklist.getNetweight();
			    			m3+=pklist.getM3() == null ? 0 : pklist.getM3();
			    			morigin+=pklist.getMet_origin() == null ? 0 : pklist.getMet_origin();
			    			mcheck+=pklist.getMet_check() == null ? 0 : pklist.getMet_check();
			    			yorigin+=pklist.getYdsorigin() == null ? 0 : pklist.getYdsorigin();
			    			ycheck+=pklist.getYdscheck() == null ? 0 : pklist.getYdscheck();
			    		};
			    		
			    		stockind.setGrossweight(grossweight);
			    		stockind.setNetweight(netweight);
			    		stockind.setM3(m3);
//			    		stockind.setTotalmet_origin(morigin);
			    		stockind.setTotalmet_check(mcheck);
//			    		stockind.setTotalydsorigin(yorigin);
			    		stockind.setTotalydscheck(ycheck);
			    		if(stockind.getStockin_packinglist().size() !=0) {
			    			stockind.setTotalpackage(totalpackage);
			    		}
			    		

			    		// nếu tất cả các cây vải ok hoặc có comment thì stockind status = 0, ko thì status = -1
			    		if (stockind.getStockin_packinglist().size() > 0){
				    		if(!isStockinDStatusOK) { //Có Pklist và có cây vải lỗi
				    			stockind.setStatus(-1);
				    			isStockinStatusOK = false;
				    		}else { //Có Pklist và các cây vải đều ok
				    			stockind.setStatus(0);
				    		}
			    		} else { //Không có Pklist --> Mặc nhiên là chưa kiểm tra
			    			stockind.setStatus(-1);
			    			isStockinStatusOK = false;
			    		}
			    	};
			    	
			    	// nếu tất cả các stockind ok thì stockin status = 0, ko thì status = -1
			    	// status = 0 mới được duyệt để lưu vào warehouse
			    	if(stockin.getStatus() != 1) {
				    	if(!isStockinStatusOK) {
				    		stockin.setStatus(-1);
				    	}else {
				    		stockin.setStatus(0);
				    	}
			    	}
				    
			    	stockin = stockInService.save(stockin);
			    	
			    	if(isNew) {
						Stocking_UniqueCode unique = stockingService.getby_type(1);
						unique.setStocking_max(unique.getStocking_max()+ 1);
						stockingService.save(unique);
					}
					
					//System.out.println("stocking type:" + stockin.getStockintypeid_link().toString());
					//if stockin from invoice
					if (stockin.getStockintypeid_link() == StockinTypeConst.STOCKIN_TYPE_NL_NEW) {
						if(stockin.getInvoice_number() != null) {
							invoiceService.updateStatusByInvoicenumber(stockin.getInvoice_number());
				    	}
					}
					//if stockin from stockout
					if (stockin.getStockintypeid_link() == StockinTypeConst.STOCKIN_TYPE_NL_MOVE) {
						if(stockin.getStockoutid_link() != null) {
							stockoutService.updateStatusById(stockin.getStockoutid_link());
				    	}
					}
					
					response.id = stockin.getId();
					response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
					response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
					return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);	
				}
				else {
					response.setRespcode(ResponseMessage.KEY_RC_AUTHEN_ERROR);
					response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_AUTHEN_ERROR));
					return new ResponseEntity<ResponseBase>(response,HttpStatus.BAD_REQUEST);						
				}
			}
			else {
				response.setRespcode(ResponseMessage.KEY_STOCKIN_NOSKU);
				response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_STOCKIN_NOSKU));
			    return new ResponseEntity<ResponseBase>(response,HttpStatus.BAD_REQUEST);
			}

		}catch (RuntimeException e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.BAD_REQUEST);
		}
	}	
	
	@RequestMapping(value = "/stockin_approve",method = RequestMethod.POST)
	@Transactional(rollbackFor = RuntimeException.class)
	public ResponseEntity<?> StockinApprove(@RequestBody StockinConfirmRequest entity, HttpServletRequest request ) {
		Stockin_create_response response = new Stockin_create_response();
		try {
			Date today = new Date();
			StockIn stockin = stockInService.findOne(entity.stockinId);
			StockIn stockin_entity = entity.stockin;
			
			// Nếu là nhập vải, kiểm tra cây vải có độ dài hoặc cân nặng = 0;
			if(
					stockin.getStockintypeid_link().equals(StockinTypeConst.STOCKIN_TYPE_NL_NEW)
				) {
				List<StockInD> stockin_d_list = stockin.getStockin_d();
				for(StockInD stockInD : stockin_d_list) {
					List<StockInPklist> stockin_packinglist_list = stockInD.getStockin_packinglist();
					for(StockInPklist stockInPklist : stockin_packinglist_list) {
						if(stockin.getUnitid_link() == null || stockin.getUnitid_link().equals((long) 1) || stockin.getUnitid_link().equals((long) 3)) {
							// met, yds
							if(stockInPklist.getMet_check() == null || stockInPklist.getMet_check() <= 0) {
								response.setRespcode(ResponseMessage.KEY_RC_BAD_REQUEST);
								response.setMessage(
										"Cây vải " + stockInD.getSkuCode() + ", lot: " + stockInPklist.getLotnumber() + ", số: " + stockInPklist.getPackageid() +
										" có độ dài <= 0"
										);
								return new ResponseEntity<ResponseBase>(response,HttpStatus.BAD_REQUEST);
							}
						}
						if(stockin.getUnitid_link().equals((long) 4) || stockin.getUnitid_link().equals((long) 5)) {
							// kg, lbs
							if(stockInPklist.getGrossweight_lbs_check() == null || stockInPklist.getGrossweight_lbs_check() <= 0) {
								response.setRespcode(ResponseMessage.KEY_RC_BAD_REQUEST);
								response.setMessage(
										"Cây vải " + stockInD.getSkuCode() + ", lot: " + stockInPklist.getLotnumber() + ", số: " + stockInPklist.getPackageid() +
										" có khối lượng <= 0"
										);
								return new ResponseEntity<ResponseBase>(response,HttpStatus.BAD_REQUEST);
							}
						}

					}
				}
			}
			
			//Nếu là nhập thành phẩm, kiểm tra các epc  không có trong kho
			if(
				stockin.getStockintypeid_link().equals(StockinTypeConst.STOCKIN_TYPE_TP_NEW) ||
				stockin.getStockintypeid_link().equals(StockinTypeConst.STOCKIN_TYPE_TP_MOVE)
			) {
				// Kiểm tra epc này nếu có stockid_link != orgid_to_link (không có trong kho)
				if(stockin_entity != null) {
					stockin_entity = checkEpcInStock(stockin_entity);
					Boolean isEpcInStock = isEpcInStock(stockin_entity);
					if(isEpcInStock) {
						// return here
						response.data = stockin_entity;
						response.id = stockin_entity.getId();
						
						response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
						response.setMessage("EPC đã có trong kho");
						return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
					}
				}
			}
			
			// Nếu là nhập điều chuyển thành phẩm , kiểm tra xem kho xuất có thành phẩm không
			if(
					stockin.getStockintypeid_link().equals(StockinTypeConst.STOCKIN_TYPE_TP_MOVE)
				) {
					// Kiểm tra epc này nếu có stockid_link != orgid_to_link (không có trong kho)
					if(stockin != null) {
						List<StockInD> stockinDList = stockin.getStockin_d();
						for(StockInD stockinD : stockinDList) {
							Long skuid_link = stockinD.getSkuid_link();
							Long orgFromId = stockin.getOrgid_from_link();
							Long orgToId = stockin.getOrgid_to_link();
							SKU sku = skuService.findOne(skuid_link);
							Org orgFrom = orgService.findOne(orgFromId);
							
							List<Warehouse> warehouseList = warehouseService.getBySkuAndStock(skuid_link, orgFromId, null);
							List<StockInPklist> stockinPklistList = stockinD.getStockin_packinglist();
							
//							System.out.println(warehouseList.size());
//							System.out.println(stockinPklistList.size());
//							System.out.println(sku.getProduct_code());
//							System.out.println(sku.getColor_name());
//							System.out.println(sku.getSize_name());
//							System.out.println("Số lượng sản phẩm " + sku.getProduct_code() + "(" + sku.getColor_name() + " - " + sku.getSize_name() + ") trong kho "
//									+ orgFrom.getName() + "(" + orgFrom.getParentname() + ") "
//									+ "ít hơn số lượng sản phẩm cần nhập kho");
							if(warehouseList.size() < stockinPklistList.size()) {
								// sl trong kho xuất ít hơn sl nhập
								response.data = stockin_entity;
								response.id = stockin_entity.getId();
								
								response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
								response.setMessage(
										"Số lượng sản phẩm " + sku.getProduct_code() + "(" + sku.getColor_name() + " - " + sku.getSize_name() + ") trong kho "
												+ orgFrom.getName() + "(" + orgFrom.getParentname() + ") "
												+ "ít hơn số lượng sản phẩm cần nhập kho"
										);
								return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
							}else { // warehouseList.size() >= stockinPklistList.size()
								// sl đủ, -> set lại ds epc cho stockin trùng với ds warehouse
								// vì khi tạo stockin thì epc đang tự sinh (không lấy theo epc warehouse)
								Integer i = 0;
								for(StockInPklist stockInPklist : stockinPklistList) {
									if(i < warehouseList.size()) {
										Warehouse warehouse = warehouseList.get(i);
										stockInPklist.setEpc(warehouse.getEpc());
										stockInPklistService.save(stockInPklist);
										i++;
									}
								}
							}
						}
					}
				}
			
			
			//Nguoi duyet phai != null va Phieu phai o trang thai OK moi dc duyet
			if (null!= entity.approver_userid_link && stockin.getStatus() == StockinStatus.STOCKIN_STATUS_OK){
				stockin = stockInService.findOne(stockin.getId());
			    List<StockInD> stockind = stockin.getStockin_d();
			    
			    //Ghi Item vao trong warehouse
				if(stockind !=null && stockind.size() >0) {
					for (StockInD item_sku : stockind) {
						List<StockInPklist> Pklist = item_sku.getStockin_packinglist();
						if(Pklist !=null && Pklist.size() >0) {
							for (StockInPklist item_epc : Pklist) {
								List<Warehouse> ls_EPC = warehouseService.findMaterialByEPC(item_epc.getEpc());
								
								if (ls_EPC.size() == 0) {//EPC khong co trong warehouse (hang moi)
									Warehouse stockin_ws = new Warehouse();
									
									stockin_ws.setSkucode(item_sku.getSkucode());
									
					    			//Stockid_link trong Warehouse la noi hang dc chuyyen toi
									stockin_ws.setStockid_link(stockin.getOrgid_to_link());
									stockin_ws.setPcontractid_link(stockin.getPcontractid_link());
									
									stockin_ws.setUnitprice(item_sku.getUnitprice());
									stockin_ws.setColorid_link(item_epc.getColorid_link());
									stockin_ws.setEncryptdatetime(new Date());
									stockin_ws.setEpc(item_epc.getEpc());
									stockin_ws.setLasttimeupdate(item_epc.getLasttimeupdate());
									stockin_ws.setLastuserupdateid_link(item_epc.getLastuserupdateid_link());
									stockin_ws.setLotnumber(item_epc.getLotnumber());
									stockin_ws.setGrossweight(item_epc.getGrossweight_check());
									stockin_ws.setGrossweight_lbs(item_epc.getGrossweight_lbs_check());
									stockin_ws.setNetweight(item_epc.getNetweight());
									stockin_ws.setOrgrootid_link(item_epc.getOrgrootid_link());
									stockin_ws.setPackageid(item_epc.getPackageid());
									stockin_ws.setSizeid_link(item_epc.getSizeid_link());
									stockin_ws.setSkuid_link(item_epc.getSkuid_link());
									stockin_ws.setSkutypeid_link(item_epc.getSkutypeid_link());
									
									stockin_ws.setStockinid_link(stockin.getId());
									stockin_ws.setStockindid_link(item_sku.getId());
									
									stockin_ws.setTimecreate(item_epc.getTimecreate());
									stockin_ws.setUnitid_link(item_epc.getUnitid_link());
					    			stockin_ws.setUsercreateid_link(item_epc.getUsercreateid_link());
					    			
					    			stockin_ws.setWidth(item_epc.getWidth());
					    			stockin_ws.setWidth_yds(item_epc.getWidth_yds_check());
					    			stockin_ws.setWidth_met(item_epc.getWidth_met_check());
					    			stockin_ws.setYds(item_epc.getYdscheck());
					    			stockin_ws.setMet(item_epc.getMet_check());
					    			if(
					    				stockin.getStockintypeid_link().equals(StockinTypeConst.STOCKIN_TYPE_TP_NEW) ||
					    				stockin.getStockintypeid_link().equals(StockinTypeConst.STOCKIN_TYPE_TP_MOVE)
					    					) {
					    				stockin_ws.setStatus(item_epc.getStatus());
					    			}else {
						    			stockin_ws.setStatus(WareHouseStatus.WAREHOUSE_STATUS_UNCHECKED);
					    			}
					    			stockin_ws.setSpaceepc_link(item_epc.getSpaceepc_link());
					    			
					    			warehouseService.save(stockin_ws);
								} else {
									Warehouse theEPC = ls_EPC.get(0);
									//Hang nhap kho o trang thai dang van chuyen thi moi cho nhap
									if (theEPC.getStockid_link() == EPCStockStatus.EPCSTOCK_ONLOGISTIC){
			    						theEPC.setStockid_link(stockin.getOrgid_to_link());
			    						if(stockin.getStockintypeid_link().equals(StockinTypeConst.STOCKIN_TYPE_TP_MOVE)) {
			    							theEPC.setIs_freeze(false);
			    						}
			    						warehouseService.save(theEPC);
			    					}else if(stockin.getStockintypeid_link().equals(StockinTypeConst.STOCKIN_TYPE_TP_MOVE)) {
			    						// nếu là nhập điều chuyển trực tiếp từ kho thành phẩm đích (không có phiếu xuất kho ở kho gốc)
			    						// chuyển stockid_link từ kho gốc sang kho đích
			    						theEPC.setStockid_link(stockin.getOrgid_to_link());
			    						theEPC.setIs_freeze(false);
			    						warehouseService.save(theEPC);
			    					}
								}
				    			
								//Tao Warehouse_logs trong danh sach de update sau
								Warehouse_logs warehouse_logs = new Warehouse_logs();
								warehouse_logs.setEpc(item_epc.getEpc());
								warehouse_logs.setOrglogid_link(stockin.getOrgid_to_link());
								warehouse_logs.setEventtype(WarehouseEventType.EVENT_TYPE_STOCKIN);
								warehouse_logs.setEvent_objid_link(stockin.getId());
								warehouse_logs.setTimelog(new Date());
								warehouse_logs_Service.save(warehouse_logs);
							}
						}
					}
				}
		    	
				// update Approverid
				stockin.setApprove_date(today);
				stockin.setApproverid_link(entity.approver_userid_link);
				stockin.setStatus(StockinStatus.STOCKIN_STATUS_APPROVED);
				stockInService.save(stockin);
				
				//Update Stockout tuong ung ve trang thai Da nhan
				if (null != stockin.getStockoutid_link()){
					StockOut theStockout = stockoutService.findOne(stockin.getStockoutid_link());
					if (null!=theStockout){
						theStockout.setStatus(StockoutStatus.STOCKOUT_STATUS_RECEIVED);
						stockoutService.save(theStockout);
					}
				}
				
				// Nhập thành phẩm, update porder về Đã hoàn thành mã hàng nếu nhập thành phẩm đủ số lượng
				if(null != stockin.getPcontract_poid_link() && stockin.getStockintypeid_link().equals(StockinTypeConst.STOCKIN_TYPE_TP_NEW)) {
					PContract_PO pcontract_po = pcontractPoService.findOne(stockin.getPcontract_poid_link());
					Integer po_quantity = pcontract_po.getPo_quantity() == null ? 0 : pcontract_po.getPo_quantity();
					Integer amountstockedsum = 0;
					
					List<StockIn> stockin_list = stockInService.findByPO_Type_Status(
							stockin.getPcontract_poid_link(), StockinTypeConst.STOCKIN_TYPE_TP_NEW, StockinStatus.STOCKIN_STATUS_APPROVED
							);
					if(stockin_list.size() > 0) {
						for(StockIn item : stockin_list) {
							amountstockedsum += item.getTotalpackage();
						}
					}
					
					// so sanh po_quantity, amountstockedsum
					List<POrder> porder_list = porder_line_Service.getporder_by_po(stockin.getPcontract_poid_link());
					if(porder_list.size() > 0) {
						POrder porder = porder_list.get(0);
						if(po_quantity <= amountstockedsum) {
							Date date = new Date();
							porder.setStatus(POrderStatus.PORDER_STATUS_FINISHED);
							porder.setFinishdate_fact(date);
							porderService.save(porder);
						}
					}
					
				}
				
				response.data = stockin;
				
				response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
				response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
				return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
			} else {
				response.setRespcode(ResponseMessage.KEY_STOCKIN_WRONGSTATUS);
				response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_STOCKIN_WRONGSTATUS));
				return new ResponseEntity<ResponseBase>(response,HttpStatus.BAD_REQUEST);	
			}
	
		}catch (RuntimeException e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value = "/stockin_approve_nhapDieuChuyenVai",method = RequestMethod.POST)
	@Transactional(rollbackFor = RuntimeException.class)
	public ResponseEntity<?> stockin_approve_nhapDieuChuyenVai(@RequestBody StockinConfirmRequest entity, HttpServletRequest request ) {
		Stockin_create_response response = new Stockin_create_response();
		try {
			Date today = new Date();
			StockIn stockin = stockInService.findOne(entity.stockinId);
			StockIn stockin_entity = entity.stockin;
			
			
			
			//Nếu là nhập thành phẩm, kiểm tra các epc phải không có trong kho
			// Kiểm tra epc này nếu có stockid_link != orgid_to_link (không có trong kho)
			if(
				stockin.getStockintypeid_link().equals(StockinTypeConst.STOCKIN_TYPE_TP_MOVE)
			) {
				if(stockin_entity != null) {
					stockin_entity = checkEpcInStock(stockin_entity);
					Boolean isEpcInStock = isEpcInStock(stockin_entity);
					if(isEpcInStock) {
						// return here
						response.data = stockin_entity;
						response.id = stockin_entity.getId();
						
						response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
						response.setMessage("EPC đã có trong kho");
						return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
					}
				}
			}
			
			//Nguoi duyet phai != null va Phieu phai o trang thai OK moi dc duyet
			if (null!= entity.approver_userid_link && stockin.getStatus() == StockinStatus.STOCKIN_STATUS_OK){
			    List<StockInD> stockind = stockin.getStockin_d();
			    
			    //Ghi Item vao trong warehouse
				if(stockind !=null && stockind.size() >0) {
					for (StockInD item_sku : stockind) {
						List<StockInPklist> Pklist = item_sku.getStockin_packinglist();
						if(Pklist !=null && Pklist.size() >0) {
							for (StockInPklist item_epc : Pklist) {
								List<Warehouse> ls_EPC = warehouseService.findMaterialByEPC(item_epc.getEpc());
								
								if (ls_EPC.size() == 0) {//EPC khong co trong warehouse (hang moi)
									Warehouse stockin_ws = new Warehouse();
									
									stockin_ws.setSkucode(item_sku.getSkucode());
									
					    			//Stockid_link trong Warehouse la noi hang dc chuyyen toi
									stockin_ws.setStockid_link(stockin.getOrgid_to_link());
									stockin_ws.setPcontractid_link(stockin.getPcontractid_link());
									
									stockin_ws.setUnitprice(item_sku.getUnitprice());
									stockin_ws.setColorid_link(item_epc.getColorid_link());
									stockin_ws.setEncryptdatetime(new Date());
									stockin_ws.setEpc(item_epc.getEpc());
									stockin_ws.setGrossweight(item_epc.getGrossweight());
									stockin_ws.setLasttimeupdate(item_epc.getLasttimeupdate());
									stockin_ws.setLastuserupdateid_link(item_epc.getLastuserupdateid_link());
									stockin_ws.setLotnumber(item_epc.getLotnumber());
									stockin_ws.setNetweight(item_epc.getNetweight());
									stockin_ws.setOrgrootid_link(item_epc.getOrgrootid_link());
									stockin_ws.setPackageid(item_epc.getPackageid());
									stockin_ws.setSizeid_link(item_epc.getSizeid_link());
									stockin_ws.setSkuid_link(item_epc.getSkuid_link());
									stockin_ws.setSkutypeid_link(item_epc.getSkutypeid_link());
									
									stockin_ws.setStockinid_link(stockin.getId());
									stockin_ws.setStockindid_link(item_sku.getId());
									
									stockin_ws.setTimecreate(item_epc.getTimecreate());
									stockin_ws.setUnitid_link(item_epc.getUnitid_link());
					    			stockin_ws.setUsercreateid_link(item_epc.getUsercreateid_link());
					    			
					    			stockin_ws.setWidth(item_epc.getWidth());
					    			stockin_ws.setWidth_yds(item_epc.getWidth_yds_check());
					    			stockin_ws.setWidth_met(item_epc.getWidth_met_check());
					    			stockin_ws.setYds(item_epc.getYdscheck());
					    			stockin_ws.setMet(item_epc.getMet_check());
					    			stockin_ws.setStatus(WareHouseStatus.WAREHOUSE_STATUS_UNCHECKED);
					    			stockin_ws.setSpaceepc_link(item_epc.getSpaceepc_link());
					    			
					    			warehouseService.save(stockin_ws);
								} else {
									Warehouse theEPC = ls_EPC.get(0);
									//Hang nhap kho o trang thai dang van chuyen thi moi cho nhap
									if (theEPC.getStockid_link() == EPCStockStatus.EPCSTOCK_ONLOGISTIC){
			    						theEPC.setStockid_link(stockin.getOrgid_to_link());
			    						theEPC.setSpaceepc_link(null);
			    						warehouseService.save(theEPC);
			    					}
								}
				    			
								//Tao Warehouse_logs trong danh sach de update sau
								Warehouse_logs warehouse_logs = new Warehouse_logs();
								warehouse_logs.setEpc(item_epc.getEpc());
								warehouse_logs.setOrglogid_link(stockin.getOrgid_to_link());
								warehouse_logs.setEventtype(WarehouseEventType.EVENT_TYPE_STOCKIN);
								warehouse_logs.setEvent_objid_link(stockin.getId());
								warehouse_logs.setTimelog(new Date());
								warehouse_logs_Service.save(warehouse_logs);
							}
						}
					}
				}
		    	
				// update Approverid
				stockin.setApprove_date(today);
				stockin.setApproverid_link(entity.approver_userid_link);
				stockin.setStatus(StockinStatus.STOCKIN_STATUS_APPROVED);
				stockInService.save(stockin);
				
				//Update Stockout tuong ung ve trang thai Da nhan
				if (null != stockin.getStockoutid_link()){
					StockOut theStockout = stockoutService.findOne(stockin.getStockoutid_link());
					if (null!=theStockout){
						theStockout.setStatus(StockoutStatus.STOCKOUT_STATUS_RECEIVED);
						stockoutService.save(theStockout);
					}
				}
				
				response.data = stockin;
				
				response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
				response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
				return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
			} else {
				response.setRespcode(ResponseMessage.KEY_STOCKIN_WRONGSTATUS);
				response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_STOCKIN_WRONGSTATUS));
				return new ResponseEntity<ResponseBase>(response,HttpStatus.BAD_REQUEST);	
			}
	
		}catch (RuntimeException e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value = "/stockin_approve_material",method = RequestMethod.POST)
	@Transactional(rollbackFor = RuntimeException.class)
	public ResponseEntity<?> StockinApprove_Material(@RequestBody StockinConfirmRequest entity, HttpServletRequest request ) {
		Stockin_create_response response = new Stockin_create_response();
		try {
//			System.out.println(entity.stockinId);
//			System.out.println(entity.approver_userid_link);
			
			GpayAuthentication user = (GpayAuthentication)SecurityContextHolder.getContext().getAuthentication();
			Date today = new Date();
			StockIn stockin = stockInService.findOne(entity.stockinId);
			
			// nếu warehouse có bản ghi có stockinid_link, ko save
			List<Warehouse> warehouseList = warehouseService.inv_getbyid(stockin.getId());
			if(warehouseList.size() > 0) {
				response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
				response.setMessage("Stockin đã được duyệt (tồn tại StockinId trong warehouse)");
				return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);	
			}
			
			// update Approverid
			stockin.setApprove_date(today);
			stockin.setApproverid_link(entity.approver_userid_link);
			stockin.setStatus(1);
			stockInService.save(stockin);
			
			// save to warehouse
			List<StockInD> stockin_dList = stockin.getStockin_d();
			
			for(StockInD stockin_d : stockin_dList) {
				// id, orgrootid_link, _skucode, stockinid_link, stockindid_link, skuid_link, 
				// lotnumber, packageid, yds, width, netweight, grossweight, _unitprice, 
				// usercreateid_link, timecreate, lastuserupdateid_link, lasttimeupdate, 
				// unitid_link, met, stockinpklistid_link
				
				List<StockInPklist> stockin_packinglists = stockin_d.getStockin_packinglist();
				for(StockInPklist pklist : stockin_packinglists) {
					Warehouse newWarehouse = new Warehouse();
					
					newWarehouse.setSkucode(stockin_d.getSkucode());
	    			//Orgid_link trong Warehouse la noi hang dc chuyyen toi
					newWarehouse.setStockid_link(stockin.getOrgid_to_link());
					newWarehouse.setUnitprice(stockin_d.getUnitprice());
					newWarehouse.setColorid_link(pklist.getColorid_link());
					newWarehouse.setEncryptdatetime(today);
					newWarehouse.setEpc(pklist.getEpc());
    				newWarehouse.setGrossweight(pklist.getGrossweight());
    				newWarehouse.setLasttimeupdate(pklist.getLasttimeupdate());
    				newWarehouse.setLastuserupdateid_link(pklist.getLastuserupdateid_link());
    				newWarehouse.setLotnumber(pklist.getLotnumber());
    				newWarehouse.setNetweight(pklist.getNetweight());
    				newWarehouse.setColorid_link(pklist.getColorid_link());
    				newWarehouse.setOrgrootid_link(pklist.getOrgrootid_link());
    				newWarehouse.setPackageid(pklist.getPackageid());
    				newWarehouse.setSizeid_link(pklist.getSizeid_link());
    				newWarehouse.setSkuid_link(pklist.getSkuid_link());
    				newWarehouse.setSkutypeid_link(pklist.getSkutypeid_link());
    				newWarehouse.setStockinpklistid_link(pklist.getId());
    				newWarehouse.setStockindid_link(stockin_d.getId());
    				newWarehouse.setStockinid_link(stockin.getId());
    				newWarehouse.setTimecreate(pklist.getTimecreate());
    				newWarehouse.setUnitid_link(pklist.getUnitid_link());
    				newWarehouse.setUsercreateid_link(user.getUserId());
    				
    				newWarehouse.setWidth(pklist.getWidth_check());
    				newWarehouse.setYds(pklist.getYdscheck());
    				newWarehouse.setMet(pklist.getMet_check());
    				
    				newWarehouse.setPcontractid_link(stockin.getMaterialinvoice_pcontractid());
	    			
	    			warehouseService.save(newWarehouse);
				}
			}
			
			response.data = stockin;
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);	
		}catch (RuntimeException e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value = "/stockin_getone",method = RequestMethod.POST)
	public ResponseEntity<?> StockinGetone(@RequestBody StockinGetoneRequest entity, HttpServletRequest request ) {
		StockInResponse response = new StockInResponse();
		try {
			GpayAuthentication user = (GpayAuthentication)SecurityContextHolder.getContext().getAuthentication();
			response.data = stockInService.stockin_getone(user.getOrgId(),entity.stockincode, entity.stockcode);
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<StockInResponse>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/stockind_byinvoice_and_sku",method = RequestMethod.POST)
	public ResponseEntity<StockinList_ByInvoiceAndSku_Response> StockinD_byinvoice_and_sku(@RequestBody StockinList_ByInvoiceAndSku_Request entity, HttpServletRequest request ) {
		StockinList_ByInvoiceAndSku_Response response = new StockinList_ByInvoiceAndSku_Response();
		try {
			response.data = stockInDService.findBy_InvoiceAndSku(entity.material_invoiceid_link, entity.skuid_link);
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<StockinList_ByInvoiceAndSku_Response>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<StockinList_ByInvoiceAndSku_Response>(response, HttpStatus.BAD_REQUEST);
		}
	}
	
	//Nhap mua moi NPL tu nha cung cap
	@RequestMapping(value = "/stockind_bypcontract_and_sku",method = RequestMethod.POST)
	public ResponseEntity<StockinList_ByInvoiceAndSku_Response> From_Vendor_StockinD_bypcontract_and_sku(@RequestBody StockinList_ByInvoiceAndSku_Request entity, HttpServletRequest request ) {
		StockinList_ByInvoiceAndSku_Response response = new StockinList_ByInvoiceAndSku_Response();
		try {
			response.data = stockInDService.from_vendor_findBy_PContractAndSku(entity.pcontractid_link, entity.stockid_link, entity.skuid_link);
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<StockinList_ByInvoiceAndSku_Response>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<StockinList_ByInvoiceAndSku_Response>(response, HttpStatus.BAD_REQUEST);
		}
	}
	@RequestMapping(value = "/stockind_bypcontract",method = RequestMethod.POST)
	public ResponseEntity<StockinList_ByInvoiceAndSku_Response> From_Vendor_StockinD_bypcontract(@RequestBody StockinList_ByInvoiceAndSku_Request entity, HttpServletRequest request ) {
		StockinList_ByInvoiceAndSku_Response response = new StockinList_ByInvoiceAndSku_Response();
		try {
			response.data = stockInDService.from_vendor_findBy_PContract(entity.pcontractid_link, entity.stockid_link);
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<StockinList_ByInvoiceAndSku_Response>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<StockinList_ByInvoiceAndSku_Response>(response, HttpStatus.BAD_REQUEST);
		}
	}
	
	//Nhap thanh pham vao kho
	@RequestMapping(value = "/p_stockind_bypcontract_and_sku",method = RequestMethod.POST)
	public ResponseEntity<StockinList_ByInvoiceAndSku_Response> PFrom_Packing_StockinD_bypcontract_and_sku(@RequestBody StockinList_ByInvoiceAndSku_Request entity, HttpServletRequest request ) {
		StockinList_ByInvoiceAndSku_Response response = new StockinList_ByInvoiceAndSku_Response();
		try {
			response.data = stockInDService.pfrom_packing_findBy_PContractAndSku(entity.pcontractid_link, entity.stockid_link, entity.skuid_link);
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<StockinList_ByInvoiceAndSku_Response>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<StockinList_ByInvoiceAndSku_Response>(response, HttpStatus.BAD_REQUEST);
		}
	}
	@RequestMapping(value = "/p_stockind_bypcontract",method = RequestMethod.POST)
	public ResponseEntity<StockinList_ByInvoiceAndSku_Response> PFrom_Packing_StockinD_bypcontract(@RequestBody StockinList_ByInvoiceAndSku_Request entity, HttpServletRequest request ) {
		StockinList_ByInvoiceAndSku_Response response = new StockinList_ByInvoiceAndSku_Response();
		try {
			response.data = stockInDService.pfrom_packing_findBy_PContract(entity.pcontractid_link, entity.stockid_link);
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<StockinList_ByInvoiceAndSku_Response>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<StockinList_ByInvoiceAndSku_Response>(response, HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value = "/gettype",method = RequestMethod.POST)
	public ResponseEntity<?> Gettype(@RequestBody StockinGettypeRequest entity,HttpServletRequest request ) {
		Stockin_gettype_response response = new Stockin_gettype_response();
		try {
			response.data = stockintypeService.findType(entity.typeFrom, entity.typeTo);
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<Stockin_gettype_response>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
//	@RequestMapping(value = "/stockin_list",method = RequestMethod.POST)
//	public ResponseEntity<?> StockinList(@RequestBody StockinListRequest entity, HttpServletRequest request ) {
//		StockInResponse response = new StockInResponse();
//		try {
////			GpayAuthentication user = (GpayAuthentication)SecurityContextHolder.getContext().getAuthentication();
//			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//			Long orgid_link = user.getOrgid_link();
//			Org userOrg = orgService.findOne(orgid_link);
//			Long userOrgId = userOrg.getId();
//			Integer userOrgType = userOrg.getOrgtypeid_link();
//			
//			List<StockIn> listStockin = stockInService.stockin_list(
//					user.getRootorgid_link(), 
//					entity.stockintypeid_link, 
//					entity.orgid_from_link, 
//					entity.orgid_to_link, 
//					entity.stockindate_from, 
//					entity.stockindate_to);
//			
////			response.data = listStockin;
////			response.totalCount = listStockin.size();
//			
//			List<StockIn> temp = new ArrayList<StockIn>();
//			
//			for(StockIn stockin : listStockin) {
//				switch(userOrgType) {
//					case 1:
//						break;
//					case 13:
//						Org stockinOrg = orgService.findOne(stockin.getOrgid_to_link());
//						if(stockinOrg.getId().equals(userOrgId) || stockinOrg.getParentid_link().equals(userOrgId)) {
//							break;
//						}else {
//							continue;
//						}
//					default: 
//						break;
//					
//				}
//				temp.add(stockin);
//			}
//			
//			response.totalCount = temp.size();
//			
//			PageRequest pageRequest = PageRequest.of(entity.page - 1, entity.limit);
//			int start = (int) pageRequest.getOffset();
//			int end = (start + pageRequest.getPageSize()) > temp.size() ? temp.size() : (start + pageRequest.getPageSize());
//			Page<StockIn> pageToReturn = new PageImpl<StockIn>(temp.subList(start, end), pageRequest, temp.size()); 
//			
//			response.data = pageToReturn.getContent();
//			
//			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
//			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
//			return new ResponseEntity<StockInResponse>(response,HttpStatus.OK);
//		}catch (RuntimeException e) {
//			ResponseError errorBase = new ResponseError();
//			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
//			errorBase.setMessage(e.getMessage());
//		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
//		}
//	}
	
	@RequestMapping(value = "/stockin_list",method = RequestMethod.POST)
	public ResponseEntity<?> StockinList(@RequestBody StockinListRequest entity, HttpServletRequest request ) {
		StockInResponse response = new StockInResponse();
		try {
//			GpayAuthentication user = (GpayAuthentication)SecurityContextHolder.getContext().getAuthentication();
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Long orgid_link = user.getOrgid_link();
			Org userOrg = orgService.findOne(orgid_link);
			Long userOrgId = userOrg.getId();
			Integer userOrgType = userOrg.getOrgtypeid_link();
			
			if (entity.page == 0) entity.page = 1;
			if (entity.limit == 0) entity.limit = 100;
			
			if(userOrgType == OrgType.ORG_TYPE_TRUSOCHINH) {
				Page<StockIn> pageToReturn = stockInService.stockin_page(
//						userOrgType, userOrgId, 
						user.getRootorgid_link(), entity.stockintypeid_link, 
						entity.orgid_from_link, entity.orgid_to_link, 
						entity.stockindate_from, entity.stockindate_to, 
						entity.limit, entity.page);
				response.data = pageToReturn.getContent();
				response.totalCount = pageToReturn.getTotalElements();
			}else if(userOrgType == OrgType.ORG_TYPE_XUONGSX) {
				Page<StockIn> pageToReturn = stockInService.stockin_page(
						userOrgType, userOrgId, 
						user.getRootorgid_link(), entity.stockintypeid_link, 
						entity.orgid_from_link, entity.orgid_to_link, 
						entity.stockindate_from, entity.stockindate_to, 
						entity.limit, entity.page);
				response.data = pageToReturn.getContent();
				response.totalCount = pageToReturn.getTotalElements();
			}
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<StockInResponse>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			e.printStackTrace();
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/stockin_product_list",method = RequestMethod.POST)
	public ResponseEntity<?> stockin_product_list(@RequestBody StockinListRequest entity, HttpServletRequest request ) {
		StockInResponse response = new StockInResponse();
		try {
//			GpayAuthentication user = (GpayAuthentication)SecurityContextHolder.getContext().getAuthentication();
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			GpayUser user_info = userDetailsService.findById(user.getId());
			
//			Long orgid_link = user.getOrgid_link();
			List<Long> orgid_to_linkArray = new ArrayList<Long>();
			
			if (null!= user_info && user_info.getOrgid_link() != (long)1){//Neu la user cap cao --> Ko han che dvi nhap
				entity.orgid_to_link = null!= user_info.getOrg_grant_id_link()?user_info.getOrg_grant_id_link():user_info.getOrgid_link();
				
				Org orgto = orgService.findOne(entity.orgid_to_link); 
				Integer orgToType = orgto.getOrgtypeid_link();
				if(orgToType.equals(OrgType.ORG_TYPE_KHOTHANHPHAM)) {
					// thêm kho vào array
					orgid_to_linkArray.add(orgto.getId());
				}
				if(orgToType.equals(OrgType.ORG_TYPE_XUONGSX)) {
					// Tìm danh sách kho của xưởng sản xuất
					List<Org> khoList = orgService.findOrgByParentAndType(OrgType.ORG_TYPE_KHOTHANHPHAM, orgto.getId());
					for(Org kho : khoList) { // loop thêm danh sách kho vào array
						orgid_to_linkArray.add(kho.getId());
					}
				}
			}
			if (null!= user_info && user_info.getOrgid_link() == (long)1){
				Long org_grant_id_link = user_info.getOrg_grant_id_link();
				if(org_grant_id_link != null) {
					Org orgto = orgService.findOne(org_grant_id_link); 
					if(orgto.getOrgtypeid_link() == OrgType.ORG_TYPE_CUAHANG) {
						orgid_to_linkArray.add(org_grant_id_link);
					}
				}
			}
			
			// Nếu user phu trách nhiều cửa hàng
			List<GpayUserOrg> list_user_org = userOrgService.getall_byuser(user.getId());
			for (GpayUserOrg userorg : list_user_org) {
				if(!orgid_to_linkArray.contains(userorg.getOrgid_link())){
					orgid_to_linkArray.add(userorg.getOrgid_link());
				}
			}

			if(entity.status.size() == 0) entity.status = null;
			
			response.data = stockInService.stockin_product_list(
					user.getRootorgid_link(), 
					entity.stockintypeid_link, 
					entity.orgid_from_link, 
//					entity.orgid_to_link,
					orgid_to_linkArray,
					entity.stockindate_from, 
					entity.stockindate_to,
					entity.stockintypeid_link_from, 
					entity.stockintypeid_link_to,
					entity.status
					);
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<StockInResponse>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			e.printStackTrace();
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/stockin_material_list",method = RequestMethod.POST)
	public ResponseEntity<?> stockin_material_list(@RequestBody StockinListRequest entity, HttpServletRequest request ) {
		StockinObjResponse response = new StockinObjResponse();
		try {
//			GpayAuthentication user = (GpayAuthentication)SecurityContextHolder.getContext().getAuthentication();
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			GpayUser user_info = userDetailsService.findById(user.getId());
			
			// mac dinh la nguyen lieu
			if(entity.stockintypeid_link_from == null) entity.stockintypeid_link_from = (long) 1;
			if(entity.stockintypeid_link_to == null) entity.stockintypeid_link_to = (long) 10;
			
			// tạo Array danh sách orgid_to_link
			List<Long> orgid_to_linkArray = new ArrayList<Long>();
			
			if (null!= user_info && !user_info.getOrgid_link().equals((long)1)){//Neu la user cap cao --> Ko han che dvi nhap
				entity.orgid_to_link = null!= user_info.getOrg_grant_id_link()?user_info.getOrg_grant_id_link():user_info.getOrgid_link();
				
				Org orgto = orgService.findOne(entity.orgid_to_link);
				Integer orgToType = orgto.getOrgtypeid_link();
				if(orgToType.equals(OrgType.ORG_TYPE_KHONGUYENLIEU) || orgToType.equals(OrgType.ORG_TYPE_KHOPHULIEU)) {
					// thêm kho vào array
					orgid_to_linkArray.add(orgto.getId());
				}else
				if(orgToType.equals(OrgType.ORG_TYPE_XUONGSX)) {
					// Tìm danh sách kho của xưởng sản xuất
					List<Org> khoList = orgService.findOrgByParentAndType(OrgType.ORG_TYPE_KHONGUYENLIEU, orgto.getId());
					for(Org kho : khoList) { // loop thêm danh sách kho vào array
						orgid_to_linkArray.add(kho.getId());
					}
					List<Org> khoPhuLieuList = orgService.findOrgByParentAndType(OrgType.ORG_TYPE_KHOPHULIEU, orgto.getId());
					for(Org kho : khoPhuLieuList) { // loop thêm danh sách kho vào array
						orgid_to_linkArray.add(kho.getId());
					}
				}
			}
			
			// Nếu user phu trách nhiều kho
			List<GpayUserOrg> list_user_org = userOrgService.getall_byuser(user.getId());
			for (GpayUserOrg userorg : list_user_org) {
				if(!orgid_to_linkArray.contains(userorg.getOrgid_link())){
					orgid_to_linkArray.add(userorg.getOrgid_link());
				}
			}

			// tong cong ty -> lay het -> set array id []
			if(user_info.getOrgid_link().equals(1L)) {
				System.out.println("remove");
				orgid_to_linkArray = new ArrayList<Long>();
			}
			
			// Dùng cho pop up cột Nhập kho ở Tiến độ giao hàng -> tab Cân đối NPL
			// Tìm theo loại vải (skuid_link)
			// Tìm danh sách id stockin chứa stockinD có skuid_link 
			List<Long> listStockinId;
			if(entity.skuid_link == null) {
				listStockinId = null;
			}else {
				listStockinId = stockInDService.getStockinIdBySkuId(entity.skuid_link);
				if(listStockinId.size() == 0) {
					// nếu có gửi skuid_link lên, nếu list tìm được có size 0 -> ko tìm thấy -> trả về rỗng
					response.data = new ArrayList<StockinObj>();
					response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
					response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
					return new ResponseEntity<StockinObjResponse>(response,HttpStatus.OK);
				}
			}
			
			List<Long> listPContractId;
			if(entity.pcontract == null) {
				listPContractId = null;
			}else {
				String contractcode = entity.pcontract.toLowerCase().trim();
				if(contractcode.equals("")) {
					listPContractId = null;
				}else {
					listPContractId = pcontractService.getidby_contractcode(contractcode);
					if(listPContractId.size() == 0) {
						// nếu có gửi contractcode lên, nếu list tìm được có size 0 -> ko tìm thấy -> trả về rỗng
						response.data = new ArrayList<StockinObj>();
						response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
						response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
						return new ResponseEntity<StockinObjResponse>(response,HttpStatus.OK);
					}
				}
				
			}
			
			List<StockIn> stockin_list = stockInService.stockin_material_list(
					user.getRootorgid_link(), 
					entity.stockintypeid_link, 
					entity.orgid_from_link, 
//					entity.orgid_to_link, 
					orgid_to_linkArray,
					entity.stockindate_from, 
					entity.stockindate_to,
					entity.pcontractid_link,
					entity.stockintypeid_link_from,
					entity.stockintypeid_link_to,
					entity.status,
					listStockinId,
					listPContractId
					);
			
			if(entity.product == null) entity.product = "";
			entity.product = entity.product.trim().toLowerCase();
			
			List<StockinObj> result = new ArrayList<StockinObj>();
			for(StockIn stockin : stockin_list) {
				// set list obj con null, chi lay obj stockin
				StockinObj stockinObj = new StockinObj();
				stockinObj.setId(stockin.getId());
				stockinObj.setInvoice_number(stockin.getInvoice_number());
				stockinObj.setInvoice_date(stockin.getInvoice_date());
				stockinObj.setStockincode(stockin.getStockincode());
				stockinObj.setStockintype_name(stockin.getStockintype_name());
				stockinObj.setStockintypeid_link(stockin.getStockintypeid_link());
				stockinObj.setOrgfrom_name(stockin.getOrgfrom_name());
				stockinObj.setOrgid_from_link(stockin.getOrgid_from_link());
				stockinObj.setOrgto_name(stockin.getOrgto_name());
				stockinObj.setOrgid_to_link(stockin.getOrgid_to_link());
				stockinObj.setStockoutid_link(stockin.getStockoutid_link());
				stockinObj.setStockout_code(stockin.getStockout_code());
				stockinObj.setTotalpackage(stockin.getTotalpackage());
				stockinObj.setTotalm3(stockin.getTotalm3());
				stockinObj.setTotalnetweight(stockin.getTotalnetweight());
				stockinObj.setTotalgrossweight(stockin.getTotalgrossweight());
				stockinObj.setStatus(stockin.getStatus());
				stockinObj.setStatusString(stockin.getStatusString());
				stockinObj.setUsercreateid_link(stockin.getUsercreateid_link());
				stockinObj.setUsercreate_name(stockin.getUsercreate_name());
				stockinObj.setTimecreate(stockin.getTimecreate());
				stockinObj.setStockindate(stockin.getStockindate());
				stockinObj.setStockinProductString(stockin.getStockinProductString());
				stockinObj.setReason(stockin.getReason());
				stockinObj.setUnitid_link(stockin.getUnitid_link());
				stockinObj.setWidth_unitid_link(stockin.getWidth_unitid_link());
				stockinObj.setPcontractid_link(stockin.getPcontractid_link());
				stockinObj.setPcontractcode(stockin.getPcontractcode());
				stockinObj.setExpected_date(stockin.getExpected_date());
				
				if(!entity.product.equals("")) {
					List<StockinProduct> stockinProduct_list = stockin.getStockin_product();
					Boolean isContainProductbuyercode = false;
					for(StockinProduct stockinProduct : stockinProduct_list) {
						String buyercode = stockinProduct.getProduct_code().trim().toLowerCase();
						if(buyercode.contains(entity.product)) {
							isContainProductbuyercode = true;
							break;
						}
					}
					if(!isContainProductbuyercode) continue;
				}
				
				result.add(stockinObj);
			}
			response.data = result;
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<StockinObjResponse>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			e.printStackTrace();
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/stockin_material_list_pcontract",method = RequestMethod.POST)
	public ResponseEntity<?> stockin_material_list_pcontract(@RequestBody StockinListRequest entity, HttpServletRequest request ) {
		StockinObjResponse response = new StockinObjResponse();
		try {
//			GpayAuthentication user = (GpayAuthentication)SecurityContextHolder.getContext().getAuthentication();
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//			GpayUser user_info = userDetailsService.findById(user.getId());
			Long userId = null;
			
			
			// quản lý nhiều xưởng thì load những xưởng quản lý
			// nếu ko load theo người tạo
			
			// mac dinh la nguyen lieu
			if(entity.stockintypeid_link_from == null) entity.stockintypeid_link_from = (long) 1;
			if(entity.stockintypeid_link_to == null) entity.stockintypeid_link_to = (long) 10;
			
			// tạo Array danh sách orgid_to_link
			List<Long> orgid_to_linkArray = new ArrayList<Long>();
			
			if(user.getRootorgid_link().equals(user.getOrgid_link()) && user.getRootorgid_link().equals((long)1)) {
				// orgrootid_link = orgid_link thì load all
				orgid_to_linkArray = new ArrayList<Long>();
			}else
			if(user.getRootorgid_link().equals(user.getOrgid_link()) && !user.getRootorgid_link().equals((long)1)) {
				Long orgid_link = user.getOrgid_link();
				Org userOrg = orgService.findOne(orgid_link);
				Integer orgType = userOrg.getOrgtypeid_link();
				if(orgType.equals(OrgType.ORG_TYPE_XUONGSX)) {
					List<Org> khoList = orgService.findOrgByParentAndType(OrgType.ORG_TYPE_KHONGUYENLIEU, userOrg.getId());
					for(Org kho : khoList) { // loop thêm danh sách kho vào array
						if(!orgid_to_linkArray.contains(kho.getId())){
							orgid_to_linkArray.add(kho.getId());
						}
					}
					List<Org> khoPhuLieuList = orgService.findOrgByParentAndType(OrgType.ORG_TYPE_KHOPHULIEU, userOrg.getId());
					for(Org kho : khoPhuLieuList) { // loop thêm danh sách kho vào array
						if(!orgid_to_linkArray.contains(kho.getId())){
							orgid_to_linkArray.add(kho.getId());
						}
					}
					userId = null;
				}
			}
			else
			if (!user.getRootorgid_link().equals(user.getOrgid_link())){
				Long orgid_link = user.getOrgid_link();
				Org userOrg = orgService.findOne(orgid_link);
				Integer orgType = userOrg.getOrgtypeid_link();
				if(orgType.equals(OrgType.ORG_TYPE_XUONGSX)) {
					List<GpayUserOrg> list_user_org = userOrgService.getall_byuser(user.getId());
					if(list_user_org.size() > 0) {
						// nếu quan ly nhiều px load những phiếu cua cac kho cua cac px day
						for (GpayUserOrg userorg : list_user_org) {
							List<Org> khoList = orgService.findOrgByParentAndType(OrgType.ORG_TYPE_KHONGUYENLIEU, userorg.getOrgid_link());
							for(Org kho : khoList) { // loop thêm danh sách kho vào array
								if(!orgid_to_linkArray.contains(kho.getId())){
									orgid_to_linkArray.add(kho.getId());
								}
							}
							List<Org> khoPhuLieuList = orgService.findOrgByParentAndType(OrgType.ORG_TYPE_KHOPHULIEU, userorg.getOrgid_link());
							for(Org kho : khoPhuLieuList) { // loop thêm danh sách kho vào array
								if(!orgid_to_linkArray.contains(kho.getId())){
									orgid_to_linkArray.add(kho.getId());
								}
							}
						}
						userId = null;
					}else {
						// neu khong load nhung phieu do user tao ra
						userId = user.getId();
					}
				}
			}
			
			// Dùng cho pop up cột Nhập kho ở Tiến độ giao hàng -> tab Cân đối NPL
			// Tìm theo loại vải (skuid_link)
			// Tìm danh sách id stockin chứa stockinD có skuid_link 
			List<Long> listStockinId;
			if(entity.skuid_link == null) {
				listStockinId = null;
			}else {
				listStockinId = stockInDService.getStockinIdBySkuId(entity.skuid_link);
				if(listStockinId.size() == 0) {
					// nếu có gửi skuid_link lên, nếu list tìm được có size 0 -> ko tìm thấy -> trả về rỗng
					response.data = new ArrayList<StockinObj>();
					response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
					response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
					return new ResponseEntity<StockinObjResponse>(response,HttpStatus.OK);
				}
			}
			
			List<StockIn> stockin_list = stockInService.stockin_material_list_pcontract(
					user.getRootorgid_link(), 
					entity.stockintypeid_link, 
					entity.orgid_from_link, 
//					entity.orgid_to_link, 
					orgid_to_linkArray,
					entity.stockindate_from, 
					entity.stockindate_to,
					entity.pcontractid_link,
					entity.stockintypeid_link_from,
					entity.stockintypeid_link_to,
					entity.status,
					listStockinId, 
					userId);
			
			List<StockinObj> result = new ArrayList<StockinObj>();
			for(StockIn stockin : stockin_list) {
				// set list obj con null, chi lay obj stockin
				StockinObj stockinObj = new StockinObj();
				stockinObj.setId(stockin.getId());
				stockinObj.setInvoice_number(stockin.getInvoice_number());
				stockinObj.setInvoice_date(stockin.getInvoice_date());
				stockinObj.setStockincode(stockin.getStockincode());
				stockinObj.setStockintype_name(stockin.getStockintype_name());
				stockinObj.setStockintypeid_link(stockin.getStockintypeid_link());
				stockinObj.setOrgfrom_name(stockin.getOrgfrom_name());
				stockinObj.setOrgid_from_link(stockin.getOrgid_from_link());
				stockinObj.setOrgto_name(stockin.getOrgto_name());
				stockinObj.setOrgid_to_link(stockin.getOrgid_to_link());
				stockinObj.setStockoutid_link(stockin.getStockoutid_link());
				stockinObj.setStockout_code(stockin.getStockout_code());
				stockinObj.setTotalpackage(stockin.getTotalpackage());
				stockinObj.setTotalm3(stockin.getTotalm3());
				stockinObj.setTotalnetweight(stockin.getTotalnetweight());
				stockinObj.setTotalgrossweight(stockin.getTotalgrossweight());
				stockinObj.setStatus(stockin.getStatus());
				stockinObj.setStatusString(stockin.getStatusString());
				stockinObj.setUsercreateid_link(stockin.getUsercreateid_link());
				stockinObj.setUsercreate_name(stockin.getUsercreate_name());
				stockinObj.setTimecreate(stockin.getTimecreate());
				stockinObj.setStockindate(stockin.getStockindate());
				stockinObj.setStockinProductString(stockin.getStockinProductString());
				stockinObj.setReason(stockin.getReason());
				stockinObj.setUnitid_link(stockin.getUnitid_link());
				stockinObj.setExpected_date(stockin.getExpected_date());
				
				result.add(stockinObj);
			}
			response.data = result;
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<StockinObjResponse>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			e.printStackTrace();
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/stockin_getbyid",method = RequestMethod.POST)
	public ResponseEntity<?> GetStockinByID(@RequestBody StockinByIDRequest entity, HttpServletRequest request ) {
		GetStockinByIDResponse response = new GetStockinByIDResponse();
		try {
			StockIn result = stockInService.findOne(entity.id);
			if(result.getStockin_d() != null) {
				Comparator<StockInD> compareBySkucode = (StockInD a1, StockInD a2) -> a1.getSkucode().compareTo( a2.getSkucode());
				Collections.sort(result.getStockin_d(), compareBySkucode);
				
				// set danh sách lot cho stockin_d
				List<StockInD> stockin_d_list = result.getStockin_d();
				for(StockInD stockInD : stockin_d_list) {
					Boolean loaiThanhPham1 = false;
					Boolean loaiThanhPham2 = false;
					Boolean loaiThanhPham3 = false;
					String loaiThanhPham = "";
					Integer tongSoCayVai = 0;
					Integer tongSoCayVaiKiemMobile = 0;
					Float tongSoCayVaiKiemMobilePhanTram = (float) 0;
					
					List<StockInPklist> stockInPklist_list = stockInD.getStockin_packinglist();
					for(StockInPklist stockInPklist : stockInPklist_list) {
						if(stockInPklist.getStatus().equals(StockinStatus.TP_LOAI1)) {
							loaiThanhPham1 = true;
						}
						if(stockInPklist.getStatus().equals(StockinStatus.TP_LOAI2)) {
							loaiThanhPham2 = true;
						}
						if(stockInPklist.getStatus().equals(StockinStatus.TP_LOAI3)) {
							loaiThanhPham3 = true;
						}
						tongSoCayVai++;
						if(stockInPklist.getIs_mobile_checked() != null) tongSoCayVaiKiemMobile++;
					}
					if(loaiThanhPham1) {
						loaiThanhPham += "Loại 1; ";
					}
					if(loaiThanhPham2) {
						loaiThanhPham += "Loại 2; ";
					}
					if(loaiThanhPham3) {
						loaiThanhPham += "Loại 3; ";
					}
					tongSoCayVaiKiemMobilePhanTram = tongSoCayVai == 0 ? (float)0 : (float)tongSoCayVaiKiemMobile/tongSoCayVai*100;
					
					stockInD.setLoaiThanhPham(loaiThanhPham);
					stockInD.setTongSoCayVai(tongSoCayVai);
					stockInD.setTongSoCayVaiKiemMobile(tongSoCayVaiKiemMobile);
					stockInD.setTongSoCayVaiKiemMobilePhanTram(tongSoCayVaiKiemMobilePhanTram);
				}
			}
			
			response.data = result;
//			List<Warehouse> list_wh = warehouseService.inv_getbyid(entity.id);
			
			response.listepc = stockInPklistService.getByStockinId(entity.id);;
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<GetStockinByIDResponse>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/save_loai_thanh_pham",method = RequestMethod.POST)
	public ResponseEntity<GetStockinByIDResponse> save_loai_thanh_pham(@RequestBody StockinCreateRequest entity, HttpServletRequest request ) {
		GetStockinByIDResponse response = new GetStockinByIDResponse();
		try {
			Integer TPGroupStoreValue = entity.TPGroupStoreValue;
			Long stockindid_link = entity.stockindid_link;
			List<StockInPklist> stockInPklist_list = stockInPklistService.getByStockinDId(stockindid_link);
			for(StockInPklist stockInPklist : stockInPklist_list) {
				stockInPklist.setStatus(TPGroupStoreValue);
				stockInPklistService.save(stockInPklist);
			}
			
//			System.out.println(TPGroupStoreValue);
//			System.out.println(stockoutdid_link);
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<GetStockinByIDResponse>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<GetStockinByIDResponse>(response, HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value = "/stockind_getbyid",method = RequestMethod.POST)
	public ResponseEntity<?> GetStockinDByID(@RequestBody StockinByIDRequest entity, HttpServletRequest request ) {
		GetStockinDByIDResponse response = new GetStockinDByIDResponse();
		try {
			StockInD result = stockInDService.findOne(entity.id);
			
			List<StockInPklist> pklist = result.getStockin_packinglist();
			
//			Comparator<StockInPklist> compareBySortValue = (StockInPklist a1, StockInPklist a2) -> a1.getLotnumber().compareTo( a2.getLotnumber());
//			Collections.sort(pklist, compareBySortValue);
			
			Collections.sort(pklist, Comparator.comparing(StockInPklist::getLotnumber)
		            .thenComparing(StockInPklist::getPackageid));
			
			Long stockInId = result.getStockinid_link();
			StockIn stockin = stockInService.findOne(stockInId);
			
			response.data = result;
			response.stockin = stockin;
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<GetStockinDByIDResponse>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/stockin_deleteid",method = RequestMethod.POST)
	public ResponseEntity<?> StockinDeleteByID(@RequestBody StockinByIDRequest entity, HttpServletRequest request ) {
		ResponseBase response = new ResponseBase();
		try {
			GpayAuthentication user = (GpayAuthentication)SecurityContextHolder.getContext().getAuthentication();
			StockIn stockin = stockInService.findOne(entity.id);
			if (null != stockin){
				if(stockin.getStatus() >= StockinStatus.STOCKIN_STATUS_APPROVED) {
					response.setRespcode(ResponseMessage.KEY_RC_BAD_REQUEST);
					response.setMessage("Phiếu đã được duyệt nhập kho");
					return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
				}
				
////				Xóa các dòng chíp trong bảng warehouse
//				for (StockInD sku : stockin.getStockin_d()) {
//					for (StockInPklist epc: sku.getStockin_packinglist()){
//						warehouseService.deleteByEpc(epc.getEpc(), stockin.getOrgid_to_link());
//					}
//				}
//				stockInService.delete(stockin);
				
				stockin.setStatus(StockinStatus.STOCKIN_STATUS_DELETED);
				stockin.setLasttimeupdate(new Date());
				stockin.setLastuserupdateid_link(user.getUserId());
				stockInService.save(stockin);
				
				Log4jCommon.log_deleteStockin(user.getName(), stockin.getId(), stockin.getStockincode(), stockin.getPcontractid_link());
				
				response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
				response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
				return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
			} else {
				response.setRespcode(ResponseMessage.KEY_RC_RS_NOT_FOUND);
				response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_RS_NOT_FOUND));
				return new ResponseEntity<ResponseBase>(response,HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@RequestMapping(value = "/stockind_deleteid",method = RequestMethod.POST)
	public ResponseEntity<?> StockinDDeleteByID(@RequestBody StockinByIDRequest entity, HttpServletRequest request ) {
		ResponseBase response = new ResponseBase();
		try {
			stockInDService.deleteById(entity.id);
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
	
	
	
	
	
	//cũ
	@RequestMapping(value = "/getStockin",method = RequestMethod.POST)
	public ResponseEntity<?> GetStockin(HttpServletRequest request ) {
		GetStockinByCodeOutput output = new GetStockinByCodeOutput();
		try {
			output.data = stockInService.findAll();
			
			output.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			output.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<GetStockinByCodeOutput>(output,HttpStatus.OK);
		}catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/getStockinByCode",method = RequestMethod.POST)
	public ResponseEntity<?> GetStockinByCode( @RequestBody GetStockinByCodeInput entity,HttpServletRequest request ) {
		GetStockinByCodeOutput output = new GetStockinByCodeOutput();
		try {
			GpayAuthentication user = (GpayAuthentication)SecurityContextHolder.getContext().getAuthentication();
			output.data = stockInService.findByStockinCode(user.getOrgId(),entity.stockincode);
			
			output.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			output.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<GetStockinByCodeOutput>(output,HttpStatus.OK);
		}catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/getPackingListByStockinDID",method = RequestMethod.POST)
	public ResponseEntity<?> GetPackingListByStockinDID( @RequestBody GetPackingListByStockinDID entity,HttpServletRequest request ) {
		GetPackingListByStockinDIDOutput output =new GetPackingListByStockinDIDOutput();
		try {
			output.data = packingListService.findByStockinDID(entity.stockindid);
			output.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			output.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<GetPackingListByStockinDIDOutput>(output,HttpStatus.OK);
		}catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/StockInInsert",method = RequestMethod.POST)
	public ResponseEntity<?> InvoiceInsert( @RequestBody StockInRequest entity,HttpServletRequest request ) {
		ResponseBase response = new ResponseBase();
		try {
			if(entity.stockin!=null) {
				entity.stockin.setId(null);
				entity.stockin.setStockindate(new Date());
				entity.stockin.setStatus(1);
				StockIn stockin= stockInService.create(entity.stockin);
				if(stockin!=null && !entity.stockind.isEmpty()) {
					for (StockInD entry : entity.stockind) {
						entry.setId(null);
						entry.setStockinid_link(stockin.getId());
						stockInDService.create(entry);
					}
				}
			}
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
    
	@RequestMapping(value = "/PackinglistInsert",method = RequestMethod.POST)
	public ResponseEntity<?> PackinglistInsert( @RequestBody List<PackingList> entity,HttpServletRequest request ){
		ResponseBase response = new ResponseBase();
		try {
			if(entity!=null && entity.size()>0) {
				for(PackingList entry : entity) {
					//entry.setStatus(1);
					packingListService.create(entry);
				}
			}
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
	
	@RequestMapping(value = "/getStockinProductByStockinId",method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> getStockinProductByStockinId(@RequestBody StockinD_getByStockinId_request entity,HttpServletRequest request ) {
		StockinProduct_getByStockinId_response response = new StockinProduct_getByStockinId_response();
		try {
			List<StockinProduct> result = stockInService.getStockinProductByStockinId(entity.stockinid_link);
			response.data = result;
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<ResponseBase>(response,HttpStatus.BAD_REQUEST);
		}
	}
	
	private String genUUID(){
		UUID uuid = UUID.randomUUID();
        return uuid.toString();
	}
	
	public String updatePOrderProcessing(
			Integer stockintypeid_link,
			Long granttoorgid_link, Long porderid_link, 
			Date receive_date, Integer sumProduct,
			String action
			) {
		
//		System.out.println("updatePOrderProcessing " + sumProduct);
//		System.out.println("stockintypeid_link " + stockintypeid_link);
		
		GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Long rootorgid_link = user.getRootorgid_link();
		// tìm xem lệnh này, tổ chuyền này, ngày này có POrderProcessinng không
		receive_date = DateFormat.atStartOfDay(receive_date);
		List<POrderProcessing> listPorderProcessing = porderProcessingService.getByPOrderAndLineAndDate(
				porderid_link, granttoorgid_link, receive_date
				);
		
		POrderProcessing pprocess;
		if(listPorderProcessing.size() > 0) {
			// Có thì check ngày, nếu trùng thì sửa, ko trùng thì tạo mới và set thông tin dựa vào ngày trước
			pprocess = listPorderProcessing.get(0);
			if(receive_date.equals(pprocess.getProcessingdate())) {
				// trùng ngày, thêm và tính toán amountinput
				if(action.equals("Xác nhận")) {
			        // Xác nhận lúc nào cũng là ngày hiện tại nên ko cần tính lại các ngày sau
					if(stockintypeid_link.equals(StockinTypeConst.STOCKIN_TYPE_TP_NEW)) {
						// kiểm tra amountstockedsum có > amountpackstockedsum không
						Integer amountstocked = null==pprocess.getAmountstocked()?0:pprocess.getAmountstocked() + sumProduct;
						Integer amountstockedsum = (null==pprocess.getAmountstockedsumprev()?0:pprocess.getAmountstockedsumprev())
								+ amountstocked;
						Integer amountpackstockedsum = null==pprocess.getAmountpackstockedsum()?0:pprocess.getAmountpackstockedsum();
						
						if(amountstockedsum <= amountpackstockedsum) {
							pprocess.setAmountstocked(amountstocked);
			    			pprocess.setAmountstockedsum(amountstockedsum);
						}else {
							return "Tổng SL nhập thành phẩm không được vượt quá SL nhập hoàn thiện";
						}
						
					}
				}else if(action.equals("Huỷ")) {
					// Huỷ là sau xác nhận nên lúc nào cũng tồn tại POrderProcessing
					if(stockintypeid_link.equals(StockinTypeConst.STOCKIN_TYPE_TP_NEW)) {
						pprocess.setAmountstocked(pprocess.getAmountstocked() - sumProduct);
		    			pprocess.setAmountstockedsum((null==pprocess.getAmountstockedsumprev()?0:pprocess.getAmountstockedsumprev()) 
		    					+ (null==pprocess.getAmountstocked()?0:pprocess.getAmountstocked()));
					}
	    			
	    			// Tính lại sl các ngày sau
	    			// Cộng dồn trong trường hợp sửa số của ngày trước ngày hiện tại
			        // Update Amount SUM of following days. In case update amount of prev day

			        POrder_Grant porder_grant = porder_GrantService.findOne(pprocess.getPordergrantid_link());
	    			
			        if (DateFormat.atStartOfDay(receive_date).before(DateFormat.atStartOfDay(new Date()))){
				        List<POrderProcessing> pprocessListAfter = porderProcessingService.getAfterDate(porderid_link, pprocess.getPordergrantid_link(), receive_date);
				        
				        int iAmountCutSum = null==pprocess.getAmountcutsum()?0:pprocess.getAmountcutsum();
				        int iAmountInputSum = null==pprocess.getAmountinputsum()?0:pprocess.getAmountinputsum();
				        int iAmountOuputSum = null==pprocess.getAmountoutputsum()?0:pprocess.getAmountoutputsum();
				        int iAmountErrorSum = null==pprocess.getAmounterrorsum()?0:pprocess.getAmounterrorsum();
				        int iAmountKcsSum = null==pprocess.getAmountkcssum()?0:pprocess.getAmountkcssum();
				        int iAmountPackedSum = null==pprocess.getAmountpackedsum()?0:pprocess.getAmountpackedsum();
				        int iAmountPackStockedSum = null==pprocess.getAmountpackstockedsum()?0:pprocess.getAmountpackstockedsum();
				        int iAmountStockedSum = null==pprocess.getAmountstockedsum()?0:pprocess.getAmountstockedsum();
				        int iLastStatus = pprocess.getStatus();
				        
				        for(POrderProcessing pprocessAfter: pprocessListAfter){
				        	pprocessAfter.setAmountcutsumprev(iAmountCutSum);
				        	pprocessAfter.setAmountcutsum(iAmountCutSum + (null==pprocessAfter.getAmountcut()?0:pprocessAfter.getAmountcut()));
				        	
				        	pprocessAfter.setAmountinputsumprev(iAmountInputSum);
				        	pprocessAfter.setAmountinputsum(iAmountInputSum + (null==pprocessAfter.getAmountinput()?0:pprocessAfter.getAmountinput()));
				        	
				        	pprocessAfter.setAmountoutputsumprev(iAmountOuputSum);
				        	pprocessAfter.setAmountoutputsum(iAmountOuputSum + (null==pprocessAfter.getAmountoutput()?0:pprocessAfter.getAmountoutput()));
				        	
				        	pprocessAfter.setAmounterrorsumprev(iAmountErrorSum);
				        	pprocessAfter.setAmounterrorsum(iAmountErrorSum + (null==pprocessAfter.getAmounterror()?0:pprocessAfter.getAmounterror()));
				        	
				        	pprocessAfter.setAmountkcssumprev(iAmountKcsSum);
				        	pprocessAfter.setAmountkcssum(iAmountKcsSum + (null==pprocessAfter.getAmountkcssum()?0:pprocessAfter.getAmountkcssum()));
				        	
				        	pprocessAfter.setAmountpackedsumprev(iAmountPackedSum);
				        	pprocessAfter.setAmountpackedsum(iAmountPackedSum + (null==pprocessAfter.getAmountpacked()?0:pprocessAfter.getAmountpacked()));
				        	
				        	pprocessAfter.setAmountpackstockedsumprev(iAmountPackStockedSum);
				        	pprocessAfter.setAmountpackstockedsum(iAmountPackStockedSum + (null==pprocessAfter.getAmountpackstocked()?0:pprocessAfter.getAmountpackstocked()));
				        	
				        	pprocessAfter.setAmountstockedsumprev(iAmountStockedSum);
				        	pprocessAfter.setAmountstockedsum(iAmountStockedSum + (null==pprocessAfter.getAmountstocked()?0:pprocessAfter.getAmountstocked()));
				        	
					        if ((null==pprocessAfter.getAmountinputsum()?0:pprocessAfter.getAmountinputsum()) > 0){
					        	if (((null==pprocessAfter.getAmountoutputsum()?0:pprocessAfter.getAmountoutputsum()) 
					        			+ (null==pprocessAfter.getAmounterrorsum()?0:pprocessAfter.getAmounterrorsum()))  
					        			< (null==pprocessAfter.getAmountcutsum()||0==pprocessAfter.getAmountcutsum()?pprocessAfter.getTotalorder():pprocessAfter.getAmountcutsum())){
					        		pprocessAfter.setStatus(POrderStatus.PORDER_STATUS_RUNNING);
					        	}
					        	else {
					        		if ((null==pprocessAfter.getAmountpackedsum()?0:pprocessAfter.getAmountpackedsum()) 
					        				< (null==pprocessAfter.getAmountcutsum()||0==pprocessAfter.getAmountcutsum()?pprocessAfter.getTotalorder():pprocessAfter.getAmountcutsum())){
					        			pprocessAfter.setStatus(POrderStatus.PORDER_STATUS_DONE);
					        		}
					        		else {
					        			pprocessAfter.setStatus(POrderStatus.PORDER_STATUS_FINISHED);
					        		}
					        	}
					        }
					        
					        porderProcessingService.save(pprocessAfter);
				        	
					        iAmountCutSum = null==pprocessAfter.getAmountcutsum()?0:pprocessAfter.getAmountcutsum();
					        iAmountInputSum = null==pprocessAfter.getAmountinputsum()?0:pprocessAfter.getAmountinputsum();
					        iAmountOuputSum = null==pprocessAfter.getAmountoutputsum()?0:pprocessAfter.getAmountoutputsum();
					        iAmountErrorSum = null==pprocessAfter.getAmounterrorsum()?0:pprocessAfter.getAmounterrorsum();
					        iAmountKcsSum = null==pprocessAfter.getAmountkcssum()?0:pprocessAfter.getAmountkcssum();
					        iAmountPackedSum = null==pprocessAfter.getAmountpackedsum()?0:pprocessAfter.getAmountpackedsum();
					        iAmountPackStockedSum = null==pprocessAfter.getAmountpackstockedsum()?0:pprocessAfter.getAmountpackstockedsum();
					        iAmountStockedSum = null==pprocessAfter.getAmountstockedsum()?0:pprocessAfter.getAmountstockedsum();
				        	
					        iLastStatus = pprocessAfter.getStatus();
				        }
				        
				        //Update status of Porder_Grant to last status of Processing
			        	porder_grant.setStatus(iLastStatus);
			        	porder_GrantService.save(porder_grant);		        	
			        } else {
			        	porder_grant.setStatus(pprocess.getStatus());
			        	porder_GrantService.save(porder_grant);	
			        }
	    			
				}
			}else {
				// ko trùng ngày, tạo và tính toán
				POrderProcessing temp = pprocess;
				pprocess = new POrderProcessing();
				
				pprocess.setId(null);
				pprocess.setOrgrootid_link(rootorgid_link); 
				pprocess.setProcessingdate(receive_date);
				pprocess.setPorderid_link(temp.getPorderid_link());
				pprocess.setPordergrantid_link(temp.getPordergrantid_link());
				pprocess.setGranttoorgid_link(temp.getGranttoorgid_link());
	        	
				pprocess.setAmountcut(temp.getAmountcut());
				pprocess.setAmountcutsumprev(temp.getAmountcutsum());
				pprocess.setAmountcutsum(temp.getAmountcutsum());
	
				if(temp.getAmountinputsum() == null) temp.setAmountinputsum(0);
				pprocess.setAmountinput(0);
				pprocess.setAmountinputsumprev(temp.getAmountinputsum());
				pprocess.setAmountinputsum(temp.getAmountinputsum());
		        
				pprocess.setAmountoutput(0);
				pprocess.setAmountoutputsumprev(temp.getAmountoutputsum());
				pprocess.setAmountoutputsum(temp.getAmountoutputsum());
		        
				pprocess.setAmounterror(0);
				pprocess.setAmounterrorsumprev(temp.getAmounterrorsum());
				pprocess.setAmounterrorsum(temp.getAmounterrorsum());
		        
				pprocess.setAmounttargetprev(temp.getAmounttarget());
				pprocess.setAmountkcsregprev(temp.getAmountkcsreg());
	        	
				pprocess.setAmountkcs(0);
				pprocess.setAmountkcssumprev(temp.getAmountkcssum());
				pprocess.setAmountkcssum(temp.getAmountkcssum());
		        
				pprocess.setAmountpacked(0);
				pprocess.setAmountpackedsumprev(temp.getAmountpackedsum());
				pprocess.setAmountpackedsum(temp.getAmountpackedsum());
		        
				if(temp.getAmountpackstockedsum() == null) temp.setAmountpackstockedsum(0);
				pprocess.setAmountpackstocked(0);
				pprocess.setAmountpackstockedsumprev(temp.getAmountpackstockedsum());
				pprocess.setAmountpackstockedsum(temp.getAmountpackstockedsum());
		        
				pprocess.setAmountstocked(0);
				pprocess.setAmountstockedsumprev(temp.getAmountstockedsum());
				pprocess.setAmountstockedsum(temp.getAmountstockedsum());
		        
//	        	pprocess.setOrdercode(porder_grant.getOrdercode());
				pprocess.setTotalorder(temp.getGrantamount());	  
	        	
				pprocess.setUsercreatedid_link(user.getId());
				pprocess.setTimecreated(new Date());
				
				if(stockintypeid_link.equals(StockinTypeConst.STOCKIN_TYPE_TP_NEW)) {
					// kiểm tra amountinputsum có > grantamount của porder grant không
					Integer amountstocked = sumProduct;
					Integer amountstockedsum = null == temp.getAmountstockedsum() ? amountstocked : temp.getAmountstockedsum() + amountstocked;
					Integer amountpackstockedsum = null == temp.getAmountpackstockedsum()?0:temp.getAmountpackstockedsum();
					
					if(amountstockedsum <= amountpackstockedsum) {
		    			pprocess.setAmountstocked(amountstocked);
						pprocess.setAmountstockedsumprev(temp.getAmountstockedsum());
						pprocess.setAmountstockedsum(amountstockedsum);
					}else {
						return "Tổng SL nhập thành phẩm không được vượt quá SL nhập hoàn thiện";
					}
					
					
				}
			}
			//Update trang thai lenh tuong ung
	        if ((null==pprocess.getAmountinputsum()?0:pprocess.getAmountinputsum()) > 0){
	        	if (((null==pprocess.getAmountoutputsum()?0:pprocess.getAmountoutputsum()) 
	        			+ (null==pprocess.getAmounterrorsum()?0:pprocess.getAmounterrorsum()))  
	        			< (null==pprocess.getAmountcutsum()||0==pprocess.getAmountcutsum()?pprocess.getTotalorder():pprocess.getAmountcutsum())){
	        		pprocess.setStatus(POrderStatus.PORDER_STATUS_RUNNING);
	        	}
	        	else {
	        		if ((null==pprocess.getAmountpackedsum()?0:pprocess.getAmountpackedsum()) 
	        				< (null==pprocess.getAmountcutsum()||0==pprocess.getAmountcutsum()?pprocess.getTotalorder():pprocess.getAmountcutsum())){
	        			pprocess.setStatus(POrderStatus.PORDER_STATUS_DONE);
	        		}
	        		else {
	        			pprocess.setStatus(POrderStatus.PORDER_STATUS_FINISHED);
	        		}
	        	}
	        }
	        porderProcessingService.save(pprocess);
		}else {
			return "Không tồn tại POrderProcessing";
			// ko còn tồn tại porder processing gốc được sinh ra khi kéo lệnh vào chuyền
		}
		return "OK";
		
	}
	
	public StockIn checkEpcInStock(StockIn stockin) {
//		Long orgid_to_link = stockin.getOrgid_to_link();
		List<StockInD> stockInD_list = stockin.getStockin_d();
		if(stockInD_list != null && stockInD_list.size() > 0) {
			for(StockInD stockInD : stockInD_list) {
				List<StockInPklist> stockInPklist_list = stockInD.getStockin_packinglist();
				if(stockInPklist_list != null && stockInPklist_list.size() > 0) {
					for(StockInPklist item_epc : stockInPklist_list) {
						String epc = item_epc.getEpc();
//						List<Warehouse> warehouse_list = warehouseService.findMaterialByEPCAndStock(epc, orgid_to_link);
						// Tìm trong ware house xem epc có trong kho hay không, stockid_link != -1
						List<Warehouse> warehouse_list = warehouseService.findMaterialByEPCAndNotInStock(epc, EPCStockStatus.EPCSTOCK_ONLOGISTIC);
						
						if(warehouse_list.size() > 0) {
							// sp ở trong kho -> lỗi
							item_epc.setStatus(StockinStatus.STOCKIN_EPC_STATUS_ERR_WAREHOUSEEXISTED);
							stockInD.setIsPklistInStore(true);
						}else {
							// sp không trong kho -> ok
							item_epc.setStatus(StockinStatus.STOCKIN_EPC_STATUS_OK);
						}
					}
				}
			}
		}
		return stockin;
	}
	
	public Boolean isEpcInStock(StockIn stockin) {
		Boolean result = false;
		List<StockInD> stockInD_list = stockin.getStockin_d();
		if(stockInD_list != null && stockInD_list.size() > 0) {
			for(StockInD stockInD : stockInD_list) {
				List<StockInPklist> stockInPklist_list = stockInD.getStockin_packinglist();
				if(stockInPklist_list != null  && stockInPklist_list.size() > 0) {
					for(StockInPklist stockInPklist : stockInPklist_list) {
						if(stockInPklist.getStatus().equals(StockinStatus.STOCKIN_EPC_STATUS_ERR_WAREHOUSEEXISTED)) {
							return true;
						}
					}
				}
			}
		}
		return result;
	}
	
}