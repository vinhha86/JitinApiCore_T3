package vn.gpay.jitin.core.api.stockout;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

import vn.gpay.jitin.core.JitinCommon;
import vn.gpay.jitin.core.api.stockin.StockinList_ByInvoiceAndSku_Request;
import vn.gpay.jitin.core.base.ResponseBase;
import vn.gpay.jitin.core.base.ResponseError;
import vn.gpay.jitin.core.org.IOrgService;
import vn.gpay.jitin.core.org.Org;
import vn.gpay.jitin.core.security.GpayAuthentication;
import vn.gpay.jitin.core.security.GpayUser;
import vn.gpay.jitin.core.security.GpayUserOrg;
import vn.gpay.jitin.core.security.IGpayUserOrgService;
import vn.gpay.jitin.core.security.IGpayUserService;
import vn.gpay.jitin.core.sku.ISKU_Service;
import vn.gpay.jitin.core.sku.SKU;
import vn.gpay.jitin.core.stock.IStockspaceService;
import vn.gpay.jitin.core.stock.Stockspace;
import vn.gpay.jitin.core.stockin.IStockInPklistService;
import vn.gpay.jitin.core.stockin.IStockInService;
import vn.gpay.jitin.core.stockin.IStockinLotService;
import vn.gpay.jitin.core.stockin.StockIn;
import vn.gpay.jitin.core.stockin.StockInD;
import vn.gpay.jitin.core.stockin.StockInPklist;
import vn.gpay.jitin.core.stockin.StockinLot;
import vn.gpay.jitin.core.stocking_uniquecode.IStocking_UniqueCode_Service;
import vn.gpay.jitin.core.stocking_uniquecode.Stocking_UniqueCode;
import vn.gpay.jitin.core.stockout.IStockOutDService;
import vn.gpay.jitin.core.stockout.IStockOutPklistService;
import vn.gpay.jitin.core.stockout.IStockOutService;
import vn.gpay.jitin.core.stockout.StockOut;
import vn.gpay.jitin.core.stockout.StockOutD;
import vn.gpay.jitin.core.stockout.StockOutObj;
import vn.gpay.jitin.core.stockout.StockOutPklist;
import vn.gpay.jitin.core.stockout_order.IStockout_order_service;
import vn.gpay.jitin.core.stockout_order.Stockout_order;
import vn.gpay.jitin.core.stockout_order.Stockout_order_d;
import vn.gpay.jitin.core.stockout_type.IStockoutTypeService;
import vn.gpay.jitin.core.utils.StockoutType;
import vn.gpay.jitin.core.utils.WareHouseStatus;
import vn.gpay.jitin.core.utils.EPCStockStatus;
import vn.gpay.jitin.core.utils.OrgType;
import vn.gpay.jitin.core.utils.ResponseMessage;
import vn.gpay.jitin.core.utils.StockinStatus;
import vn.gpay.jitin.core.utils.StockinTypeConst;
import vn.gpay.jitin.core.utils.StockoutOrderStatus;
import vn.gpay.jitin.core.utils.StockoutStatus;
import vn.gpay.jitin.core.utils.WarehouseEventType;
import vn.gpay.jitin.core.warehouse.IWarehouseService;
import vn.gpay.jitin.core.warehouse.IWarehouse_logs_Service;
import vn.gpay.jitin.core.warehouse.Warehouse;
import vn.gpay.jitin.core.warehouse.Warehouse_logs;
import vn.gpay.jitin.core.warehouse_check.IWarehouseCheckService;
import vn.gpay.jitin.core.warehouse_check.WarehouseCheck;

@RestController
@RequestMapping("/api/v1/stockout")
public class StockOutAPI {
	@Autowired IStockOutService stockOutService;
	@Autowired IStockInService stockInService;
	@Autowired IStockInPklistService stockInPklistService;
	@Autowired IStockinLotService stockinLotService;
	@Autowired IStockOutDService stockOutDService;
	@Autowired IStockout_order_service stockOutOrderService;
	@Autowired IWarehouseService warehouseService;
	@Autowired IStockOutPklistService stockOutPklistService;
	@Autowired IStockoutTypeService stockouttypeService;
	@Autowired JitinCommon commonService;
	@Autowired IStocking_UniqueCode_Service stockingService;
	@Autowired IWarehouse_logs_Service warehouse_logs_Service;
	@Autowired IOrgService orgService;
	@Autowired IGpayUserService  userDetailsService ;
	@Autowired IGpayUserOrgService userOrgService;
	@Autowired IStockspaceService stockspaceService;
	@Autowired IWarehouseCheckService warehouseCheckService;
	@Autowired ISKU_Service skuService;
	
	@RequestMapping(value = "/stockout_create",method = RequestMethod.POST)
	@Transactional(rollbackFor = RuntimeException.class)
	public ResponseEntity<?> StockoutCreate(@RequestBody StockoutCreateRequest entity, HttpServletRequest request ) {
		StockOut_Create_response response = new StockOut_Create_response();
		try {
			GpayUser user = (GpayUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		    String stockoutcode = commonService.GetStockoutCode();
		  //Neu them moi isNew = true
			boolean isNew = false;
			
			if(entity.data.size()>0) {
				StockOut stockout = entity.data.get(0);
				if(stockout.getId()==null || stockout.getId()==0) {
					stockout.setUsercreateid_link(user.getId());
					stockout.setOrgrootid_link(user.getRootorgid_link());
//					stockout.setOrgid_from_link(user.getOrgId());
					stockout.setTimecreate(new Date());
					stockout.setStockoutcode(stockoutcode);
					stockout.setStatus(StockoutStatus.STOCKOUT_STATUS_OK);
					
					isNew = true;
					
			    }else {
			    	stockout.setOrgrootid_link(user.getRootorgid_link());
			    	stockout.setLastuserupdateid_link(user.getId());
			    	stockout.setLasttimeupdate(new Date());
			    }
				
				response.epc_err.clear();
				for (StockOutD stockoutd : stockout.getStockout_d())
				{
					stockoutd.setStatus(StockoutStatus.STOCKOUT_D_STATUS_ERR);
					stockoutd.setOrgrootid_link(user.getRootorgid_link());
					if(stockoutd.getId() == null) {
						stockoutd.setUsercreateid_link(user.getId());
						stockoutd.setTimecreate(new Date());
					}
					
					// xuất kho thành phẩm
					if(
						stockout.getStockouttypeid_link().equals(StockoutType.STOCKOUT_TYPE_TP_PO) ||
						stockout.getStockouttypeid_link().equals(StockoutType.STOCKOUT_TYPE_TP_MOVE)
						) {
						// chỉ với phiếu chưa duyệt
						if(stockout.getStatus() < StockoutStatus.STOCKOUT_STATUS_APPROVED) {
							List<StockOutPklist> stockOutPklist_list = stockoutd.getStockout_packinglist();
							Integer totalpackagecheck = stockoutd.getTotalpackagecheck() == null ? 0 : stockoutd.getTotalpackagecheck();
							Integer size = stockOutPklist_list == null ? 0 : stockOutPklist_list.size();
							// nếu số xuất > số pklist, thêm pklist
							if(totalpackagecheck > size) {
								Integer soThem = totalpackagecheck - size; // số lượng pkl thêm
								// kiểm tra warehouse số tồn thành phẩm có đủ hay ko
								Long slHangTon = warehouseService.getByStock_Sku_isNotFreeze_number(
											stockoutd.getSkuid_link(),
											stockout.getOrgid_from_link()
										);
								System.out.println(stockoutd.getSkuid_link());
								System.out.println(stockout.getOrgid_from_link());
								if(slHangTon.intValue() < soThem) {
									System.out.println(slHangTon);
									System.out.println(soThem);
									ResponseError errorBase = new ResponseError();
									errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
									errorBase.setMessage("SL xuất lớn hơn SL hàng tồn");
								    return new ResponseEntity<>(errorBase, HttpStatus.BAD_REQUEST);
								}
								// tìm trong warehouse thành phẩm theo (stockid_link, skuid_link, is_freeze)
								List<Warehouse> warehouse_list = warehouseService.getByStock_Sku_isNotFreeze(
											stockoutd.getSkuid_link(),
											stockout.getOrgid_from_link(),
											soThem
										);
//	    						for(Integer i = 0; i < soThem; i++) {
	    						for(Warehouse warehouse : warehouse_list) {
	    							StockOutPklist newPklist = new StockOutPklist();
	    							newPklist.setId(null);
	    							newPklist.setStockoutid_link(stockout.getId());
	    							newPklist.setStockoutdid_link(stockoutd.getId());
	    							newPklist.setSkuid_link(stockoutd.getSkuid_link());
	    							newPklist.setColorid_link(stockoutd.getColorid_link());
	    							newPklist.setEpc(warehouse.getEpc()); // set epc theo warehouse thành phẩm
	    							newPklist.setStatus(StockoutStatus.TP_LOAI1);
	    							newPklist.setRssi(1);
	    							stockOutPklist_list.add(newPklist);
	    							// đổi trạng thái thành phẩm thành freeze
	    							warehouse.setIs_freeze(true);
	    							warehouseService.save(warehouse);
	    						}
    							stockoutd.setStockout_packinglist(stockOutPklist_list);
							}
							// nếu số xuất < số pklist, xoá bỏ bớt pklist 
	    					if(totalpackagecheck < size) {
	    						Integer soXoa = size - totalpackagecheck; // số lượng pkl xoá
	    						for(Integer i = 0; i < soXoa; i++) {
	    							StockOutPklist stockOutPklist = stockOutPklist_list.get(0);
	    							String epc = stockOutPklist.getEpc();
	    							List<Warehouse> warehouse = warehouseService.findMaterialByEPC(epc);
	    							if(warehouse.size() > 0) {
	    								warehouse.get(0).setIs_freeze(false);
	    								warehouseService.save(warehouse.get(0));
	    							}
	    							stockOutPklist_list.remove(0);
	    						}
	    						stockoutd.setStockout_packinglist(stockOutPklist_list);
	    					}
						}
					}
					
					for (StockOutPklist item_epc : stockoutd.getStockout_packinglist()) {
						item_epc.setOrgrootid_link(user.getRootorgid_link());
						item_epc.setSkuid_link(stockoutd.getSkuid_link());
						item_epc.setStockoutid_link(stockoutd.getStockoutid_link());
						
						// Xuat Nguyen lieu - vai
						if(stockout.getStockouttypeid_link() >= 1 && stockout.getStockouttypeid_link() <= 10) {
							//Kiem tra xem hang co ma epc ton tai trong warehouse ko?
							if (null == item_epc.getId()) {
								if (warehouseService.epcExistedInStock(item_epc.getEpc(), stockout.getOrgid_from_link())){
									item_epc.setStatus(null!=item_epc.getRssi() && item_epc.getRssi() ==1?StockoutStatus.STOCKOUT_EPC_STATUS_OK:StockoutStatus.STOCKOUT_EPC_STATUS_ERR);
									
									//Có cây vải đã quét RSSI --> đổi trạng thái Stockout
									if (item_epc.getStatus().equals(StockoutStatus.STOCKOUT_EPC_STATUS_OK)){
										stockout.setStatus(StockoutStatus.STOCKOUT_STATUS_OK);
									}
								} else {
									//Thêm thông tin epc lỗi vào response để trả về frontend thông báo cho User
									item_epc.setStatus(StockoutStatus.STOCKOUT_EPC_STATUS_ERR_WAREHOUSENOTEXIST);
									response.epc_err.add(item_epc);
								}
								
							} else {
								item_epc.setStatus(null!=item_epc.getRssi() && item_epc.getRssi() ==1?StockoutStatus.STOCKOUT_EPC_STATUS_OK:StockoutStatus.STOCKOUT_EPC_STATUS_ERR);
								//Có cây vải đã quét RSSI --> đổi trạng thái Stockout
								if (item_epc.getStatus().equals(StockoutStatus.STOCKOUT_EPC_STATUS_OK)){
									stockout.setStatus(StockoutStatus.STOCKOUT_STATUS_OK);
								}
							}
							//Update lai so tong hang da kiem tra trong bang D
							Long totalpackagecheck = stockoutd.getStockout_packinglist().stream().filter(x->null!=x.getStatus() && x.getStatus() == StockoutStatus.STOCKOUT_EPC_STATUS_OK).count();
							if (null!=totalpackagecheck && totalpackagecheck >0){
								stockoutd.setTotalpackagecheck(totalpackagecheck.intValue());
								
								Double totalmet_check = stockoutd.getStockout_packinglist().stream().mapToDouble(x->null==x.getMet_check()?0:x.getMet_check()).sum();
								stockoutd.setTotalmet_check(totalmet_check.floatValue());
								Double totalyds_check = stockoutd.getStockout_packinglist().stream().mapToDouble(x->null==x.getYdscheck()?0:x.getYdscheck()).sum();
								stockoutd.setTotalydscheck(totalyds_check.floatValue());
							}
						}
						
						// Xuat thanh pham
						if(stockout.getStockouttypeid_link() >= 21 && stockout.getStockouttypeid_link() <= 30) {
//							item_epc.setStatus(StockoutStatus.STOCKOUT_EPC_STATUS_OK);
							if(stockout.getStatus() < StockoutStatus.STOCKOUT_STATUS_APPROVED) {
								if (null == item_epc.getId()) {
//									if (warehouseService.epcExistedInStock(item_epc.getEpc(), stockout.getOrgid_from_link())){
//										item_epc.setStatus(null!=item_epc.getRssi() && item_epc.getRssi() ==1?StockoutStatus.STOCKOUT_EPC_STATUS_OK:StockoutStatus.STOCKOUT_EPC_STATUS_ERR);
//										
//										//Có cây vải đã quét RSSI --> đổi trạng thái Stockout
//										if (item_epc.getStatus().equals(StockoutStatus.STOCKOUT_EPC_STATUS_OK)){
//											stockout.setStatus(StockoutStatus.STOCKOUT_STATUS_OK);
//										}
//									} else {
//										//Thêm thông tin epc lỗi vào response để trả về frontend thông báo cho User
//										item_epc.setStatus(StockoutStatus.STOCKOUT_EPC_STATUS_ERR_WAREHOUSENOTEXIST);
//										response.epc_err.add(item_epc);
//									}
									stockout.setStatus(StockoutStatus.STOCKOUT_STATUS_OK);
									
								} else {
//									item_epc.setStatus(null!=item_epc.getRssi() && item_epc.getRssi() ==1?StockoutStatus.STOCKOUT_EPC_STATUS_OK:StockoutStatus.STOCKOUT_EPC_STATUS_ERR);
//									//Có cây vải đã quét RSSI --> đổi trạng thái Stockout
//									if (item_epc.getStatus().equals(StockoutStatus.STOCKOUT_EPC_STATUS_OK)){
//										stockout.setStatus(StockoutStatus.STOCKOUT_STATUS_OK);
//									}
									stockout.setStatus(StockoutStatus.STOCKOUT_STATUS_OK);
								}
							}
						}
					}
					
					// Xuat Nguyen lieu - vai
					if(stockout.getStockouttypeid_link() >= 1 && stockout.getStockouttypeid_link() <= 10) {
						//Neu so kiem > so yeu cau --> can luu y
						if (stockout.getStatus() >= StockoutType.STOCKOUT_TYPE_TP_PO){
							if (null!=stockoutd.getTotalpackage()){
								if (stockoutd.getTotalpackage() > (null==stockoutd.getTotalpackagecheck()?0:stockoutd.getTotalpackagecheck()))
									stockoutd.setStatus(StockoutStatus.STOCKOUT_D_STATUS_ERR);
								else
									stockoutd.setStatus(StockoutStatus.STOCKOUT_D_STATUS_OK);
							}
						} else {
							if (null!=stockoutd.getTotalmet_origin()){
								if (stockoutd.getTotalmet_origin() > (null==stockoutd.getTotalmet_check()?0:stockoutd.getTotalmet_check()))
									stockoutd.setStatus(StockoutStatus.STOCKOUT_D_STATUS_ERR);
								else
									stockoutd.setStatus(StockoutStatus.STOCKOUT_D_STATUS_OK);
							}
						}
					}
					// Xuat thanh pham
					if(stockout.getStockouttypeid_link() >= 21 && stockout.getStockouttypeid_link() <= 30) {
						if(stockoutd.getTotalpackage().equals(stockoutd.getTotalpackagecheck())) {
							stockoutd.setStatus(StockoutStatus.STOCKOUT_D_STATUS_OK);
						}else {
							stockoutd.setStatus(StockoutStatus.STOCKOUT_D_STATUS_ERR);
						}
						if(stockout.getStatus() < StockoutStatus.STOCKOUT_STATUS_APPROVED) {
							stockout.setStatus(StockoutStatus.STOCKOUT_D_STATUS_OK);
						}
					}
		    	}
				
				if (response.epc_err.size() > 0){
					//Nếu có EPC lỗi --> Trả về lỗi cho Front end
					response.data = stockout;
					response.setRespcode(ResponseMessage.KEY_STOCKOUT_WRONGEPC);
					response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_STOCKOUT_WRONGEPC));
					return new ResponseEntity<ResponseBase>(response,HttpStatus.BAD_REQUEST);						
				} else {
			
					//Ghi Phiếu xuất kho
//					Long totalerr = stockout.getStockout_d().stream().filter(x->x.getStatus() == StockoutStatus.STOCKOUT_D_STATUS_ERR).count();
//					stockout.setStatus(totalerr > 0?StockoutStatus.STOCKOUT_STATUS_PICKING:StockoutStatus.STOCKOUT_STATUS_OK);
					Integer totalpakage_check = stockout.getStockout_d().stream().mapToInt(x->null==x.getTotalpackagecheck()?0:x.getTotalpackagecheck()).sum();
					stockout.setTotalpackagecheck(totalpakage_check);
					stockout = stockOutService.save(stockout);
					
					//Cap nhat bang unique code
					if(isNew) {
						Stocking_UniqueCode unique = stockingService.getby_type(2);
						unique.setStocking_max(unique.getStocking_max()+ 1);
						stockingService.save(unique);
					}
					StockOut responseObj = stockOutService.findOne(stockout.getId());
					
					response.id = stockout.getId();
					response.data = responseObj;
//					response.data = stockout;
					response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
					response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
					return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);					
				}
			} else {
				response.setRespcode(ResponseMessage.KEY_STOCKOUT_NOSKU);
				response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_STOCKOUT_NOSKU));
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
	
	@RequestMapping(value = "/stockout_create_material",method = RequestMethod.POST)
	@Transactional(rollbackFor = RuntimeException.class)
	public ResponseEntity<?> StockoutCreate_Material(@RequestBody StockoutCreateRequest entity, HttpServletRequest request ) {
		StockOut_Create_response response = new StockOut_Create_response();
//		List<StockInPklist> epcInCorrect = new ArrayList<StockInPklist>();
		try {
			if(entity.data.size()>0) {
				GpayAuthentication user = (GpayAuthentication)SecurityContextHolder.getContext().getAuthentication();
				if (user != null) {
					//Nếu thêm mới isNew = true
					boolean isNew = false;
					
				    StockOut stockout =entity.data.get(0);
//				    boolean isStockoutStatusOK = true;
				    if(stockout.getId()==null || stockout.getId()==0) {
				    	isNew = true;
				    	stockout.setOrgrootid_link(user.getRootorgid_link());
				    	
				    	//Nếu không cung cấp kho đích, mặc định kho đích là đơn vị của user đăng nhập
//				    	if (null == stockin.getOrgid_to_link())
//				    		stockin.setOrgid_to_link(user.getOrgId());
				    	stockout.setUsercreateid_link(user.getUserId());
				    	stockout.setTimecreate(new Date());
				    	stockout.setStockoutcode(commonService.GetStockoutCode());
				    	stockout.setStatus(0);
				    }
				    else {
				    	stockout.setOrgrootid_link(user.getRootorgid_link());
				    	
				    	//Nếu không cung cấp kho đích, mặc định kho đích là đơn vị của user đăng nhập
//				    	if (null == stockin.getOrgid_to_link())
//				    		stockin.setOrgid_to_link(user.getOrgId());
				    	
				    	stockout.setLastuserupdateid_link(user.getUserId());
				    	stockout.setLasttimeupdate(new Date());
				    }
				    
				    List<StockOutD> listd = stockout.getStockout_d();
//				    List<Warehouse_logs> lswarehouse_logs =  new ArrayList<Warehouse_logs>();
		    		
				    for (StockOutD stockoutd : listd) {
//				    	boolean isStockoutDStatusOK = true;
				    	if(stockoutd.getId()==null || stockoutd.getId()==0) {
				    		stockoutd.setOrgrootid_link(user.getRootorgid_link());
				    		stockoutd.setUsercreateid_link(user.getUserId());
				    		stockoutd.setTimecreate(new Date());
				    	}
				    	
				    	if(stockoutd.getStockout_packinglist().size() > 0) {
					    	Float morigin = (float)0;
					    	Float mcheck = (float)0;
					    	Float yorigin = (float)0;
					    	Float ycheck = (float)0;
					    	Integer totalpackage = 0;
					    	
					    	for (StockOutPklist pklist : stockoutd.getStockout_packinglist()) {
				    			if(pklist.getId()==null || pklist.getUsercreateid_link()==null || pklist.getUsercreateid_link()==0) {
				    				pklist.setOrgrootid_link(user.getRootorgid_link());
					    			pklist.setId(null);
					    			pklist.setUsercreateid_link(stockoutd.getUsercreateid_link());
					    			pklist.setTimecreate(new Date());
				    			}
				    			
//				    			// nếu cây vải status = 0 (ok), nếu status = -1 (thiếu) thì phải có comment
//				    			if(pklist.getStatus() == null) {
//				    				pklist.setStatus(-1);
//				    			}
//				    			if(pklist.getExtrainfo() == null) {
//				    				pklist.setExtrainfo("");
//				    			}
//				    			if(pklist.getStatus() == -1 && pklist.getExtrainfo() == "") {
//				    				isStockoutDStatusOK = false;
//				    			}
				    			
				    			totalpackage++;
				    			morigin+=pklist.getMet_origin() == null ? 0 : pklist.getMet_origin();
				    			mcheck+=pklist.getMet_check() == null ? 0 : pklist.getMet_check();
				    			yorigin+=pklist.getYdsorigin() == null ? 0 : pklist.getYdsorigin();
				    			ycheck+=pklist.getYdscheck() == null ? 0 : pklist.getYdscheck();
				    		};
				    		
//				    		stockoutd.setTotalmet_origin(morigin);
				    		stockoutd.setTotalmet_check(mcheck);
//				    		stockoutd.setTotalydsorigin(yorigin);
				    		stockoutd.setTotalydscheck(ycheck);
				    		if(stockoutd.getStockout_packinglist().size() != 0) {
				    			stockoutd.setTotalpackage(totalpackage);
				    		}
				    	}

			    		// nếu tất cả các cây vải ok hoặc có comment thì stockoutd status = 0, ko thì status = -1
//			    		if(!isStockoutDStatusOK) {
//			    			stockoutd.setStatus(-1);
//			    			isStockoutStatusOK = false;
//			    		}else {
//			    			stockoutd.setStatus(0);
//			    		}
			    	};
			    	
//			    	 nếu tất cả các stockoutd ok thì stockin status = 0, ko thì status = -1
//			    	 status = 0 mới được duyệt
//			    	if(stockout.getStatus() <= 1) {
//				    	if(!isStockoutStatusOK) {
//				    		stockout.setStatus(-1);
//				    	}else {
//				    		stockout.setStatus(0);
//				    	}
//			    	}
				    
			    	stockout = stockOutService.save(stockout);
			    	
			    	if(isNew) {
						Stocking_UniqueCode unique = stockingService.getby_type(2);
						unique.setStocking_max(unique.getStocking_max()+ 1);
						stockingService.save(unique);
					}
					
					response.id = stockout.getId();
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
	
	@RequestMapping(value = "/stockout_approve",method = RequestMethod.POST)
	@Transactional(rollbackFor = RuntimeException.class)
	public ResponseEntity<?> StockoutApprove(@RequestBody StockoutConfirmRequest entity, HttpServletRequest request ) {
		StockOut_Create_response response = new StockOut_Create_response();
		try {
			GpayAuthentication user = (GpayAuthentication)SecurityContextHolder.getContext().getAuthentication();
			
			Date today = new Date();
			StockOut stockout = stockOutService.findOne(entity.stockoutId);
			StockOut stockout_entity = entity.stockout;
			
			if (null!=stockout){
//			if (stockout.getStatus() == StockoutStatus.STOCKOUT_STATUS_OK){
				
				// Kiểm tra trạng thái phiếu xuất
				if(stockout.getStatus() >= StockoutStatus.STOCKOUT_STATUS_APPROVED) {
					response.setRespcode(ResponseMessage.KEY_RC_BAD_REQUEST);
					response.setMessage("Phiếu xuất kho đã được duyệt xuất");
					return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
				}
				
				// Kiểm tra nếu ko có đơn vị đích, thông báo lỗi
				if(stockout.getOrgid_to_link() == null) {
					response.setRespcode(ResponseMessage.KEY_RC_BAD_REQUEST);
					response.setMessage("Phiếu xuất kho phải có thông tin đơn vị nhận");
					return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
				}
				
				// Kiểm tra nếu ko chứa pklist, thông báo lỗi
				Boolean isContainPkl = false;
				List<StockOutD> stockOutD__list = stockout.getStockout_d();
				for(StockOutD stockOutD : stockOutD__list) {
					List<StockOutPklist> stockOutPklist__list = stockOutD.getStockout_packinglist();
					if(stockOutPklist__list.size() > 0) {
						isContainPkl = true;
						break;
					}
				}
				if(!isContainPkl) {
					response.setRespcode(ResponseMessage.KEY_RC_BAD_REQUEST);
					response.setMessage("Phiếu xuất kho chưa có NPL/thành phẩm");
					return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
				}
				
				//Nếu là xuất thành phẩm, kiểm tra các epc còn trong kho hay không
				if(
					stockout.getStockouttypeid_link().equals(StockoutType.STOCKOUT_TYPE_TP_PO) ||
					stockout.getStockouttypeid_link().equals(StockoutType.STOCKOUT_TYPE_TP_MOVE)
				) {
					// Kiểm tra yêu cầu xuất kho đã xuất đủ hay chưa -> xuất đủ -> thông báo
					if(stockout_entity != null) {
						//
						StockOut stockoutObj = stockOutService.findOne(stockout_entity.getId());
						Long stockoutorderid_link = stockoutObj.getStockoutorderid_link();
						
						if(stockoutObj != null && stockoutorderid_link != null) {
							// ds cac phieu xuat da duyet xuat theo stockout order
							Stockout_order stockout_order = stockOutOrderService.findOne(stockoutorderid_link);
							List<StockOut> stockOut_list_approved = stockOutService.findBy_StockoutOrder_approved(stockoutorderid_link);
							
							//
							List<StockOutD> stockOutD_list = stockoutObj.getStockout_d();
							for(StockOutD stockOutD : stockOutD_list) {
								Long skuid_link = stockOutD.getSkuid_link();
								SKU sku = skuService.findOne(skuid_link);
								
								Integer totalpackagecheck = stockOutD.getTotalpackagecheck();
								Integer slSpCanXuatTheoYC = getSlCanXuatTheoYC(stockoutorderid_link, skuid_link);
								
//								System.out.println("totalpackagecheck " + totalpackagecheck);
//								System.out.println("slSpCanXuatTheoYC " + slSpCanXuatTheoYC);
								
								if(slSpCanXuatTheoYC <= 0) {
									response.setRespcode(ResponseMessage.KEY_RC_BAD_REQUEST);
									response.setMessage("Sản phẩm " + sku.getCode() + " đã được xuất đủ theo lệnh xuất kho");
									return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
								}else if(slSpCanXuatTheoYC < totalpackagecheck) {
									response.setRespcode(ResponseMessage.KEY_RC_BAD_REQUEST);
									response.setMessage("Số lượng sản phẩm " + sku.getCode() + " cần được xuất nhỏ hơn số lượng xuất kho");
									return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
								}
//								else {
//									response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
//									response.setMessage("OK");
//									return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
//								}
								
							}
							
							
							//
							
							
//							if(isEpcNotInStock) {
//								// return here
//								response.data = stockout_entity;
//								response.id = stockout_entity.getId();
//								
//								response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
//								response.setMessage("EPC không có trong kho");
//								return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
//							}
						}
						
					}
					

					// Kiểm tra epc này nếu có stockid_link == orgid_from_link (còn trong kho)
					if(stockout_entity != null) {
						stockout_entity = checkEpcInStock(stockout_entity);
						Boolean isEpcNotInStock = isEpcNotInStock(stockout_entity);
						if(isEpcNotInStock) {
							// return here
							response.data = stockout_entity;
							response.id = stockout_entity.getId();
							
							response.setRespcode(ResponseMessage.KEY_RC_BAD_REQUEST);
							response.setMessage("EPC không có trong kho");
							return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
						}
					}
				}
				
				// Nếu là xuất nl theo ycx
				if(stockout.getStockouttypeid_link().equals(StockoutType.STOCKOUT_TYPE_NL_CUTHOUSE)) {
					// kiểm tra trạng thái ycx
					if(null!=stockout.getStockoutorderid_link()) {
						Long stockoutorderid_link = stockout.getStockoutorderid_link();
						Stockout_order stockout_order = stockOutOrderService.findOne(stockoutorderid_link);
						if(stockout_order.getStatus().equals(StockoutOrderStatus.STOCKOUT_ORDER_STATUS_OK)) {
							// ycs đã đủ, trạng thái 1
							response.setRespcode(ResponseMessage.KEY_RC_BAD_REQUEST);
							response.setMessage("Đã xuất kho đủ theo yêu cầu xuất kho");
							return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
						}
					}
					
					// kiểm tra các cây vải thêm khi mất tem, tạo warehouse (set stockid_link = -1)
					List<StockOutD> stockOutD_list = stockout.getStockout_d();
					for(StockOutD stockOutD : stockOutD_list) {
						List<StockOutPklist> stockOutPklist_list = stockOutD.getStockout_packinglist();
						for(StockOutPklist stockOutPklist : stockOutPklist_list) {
							if(stockOutPklist.getEpc() == null) {
								String uuid = genUUID();
								stockOutPklist.setEpc(uuid);
								stockOutPklist = stockOutPklistService.save(stockOutPklist);
								
								Warehouse newWarehouse = new Warehouse();
								newWarehouse.setId(null);
								newWarehouse.setOrgrootid_link(user.getRootorgid_link());
								newWarehouse.setSkucode(stockOutD.getSkucode());
								newWarehouse.setEpc(uuid);
								newWarehouse.setEncryptdatetime(today);
								newWarehouse.setSkuid_link(stockOutD.getSkuid_link());
								newWarehouse.setLotnumber(stockOutPklist.getLotnumber());
								newWarehouse.setPackageid(stockOutPklist.getPackageid());
								newWarehouse.setMet(stockOutPklist.getMet_check());
								newWarehouse.setYds((float) (stockOutPklist.getMet_check()  / 0.9144));
								newWarehouse.setWidth(stockOutPklist.getWidthcheck());
								newWarehouse.setWidth_met(stockOutPklist.getWidthcheck());
								newWarehouse.setWidth_yds((float) (stockOutPklist.getWidthcheck()  / 0.9144));
								newWarehouse.setUsercreateid_link(user.getUserId());
								newWarehouse.setTimecreate(today);
								newWarehouse.setStockid_link((long) -1);
								newWarehouse.setUnitid_link(1);
								newWarehouse.setStatus(WareHouseStatus.WAREHOUSE_STATUS_UNCHECKED);
								newWarehouse = warehouseService.save(newWarehouse);
							}
						}
					}
				}
				
				
				
				//Nếu các EPC đều hợp lệ, Đưa epc trong bảng Warehouse về onLogistic Stockid=-1
				List<StockOutD> stockoutd = stockout.getStockout_d();
				List<Warehouse_logs> lswarehouse_logs =  new ArrayList<Warehouse_logs>();
				
				if(stockoutd !=null && stockoutd.size() >0) {
					for (StockOutD item_sku : stockoutd) {
						List<StockOutPklist> Pklist = item_sku.getStockout_packinglist();
						if(Pklist !=null && Pklist.size() >0) {
							for (StockOutPklist item_epc : Pklist) {
								if (item_epc.getStatus() >= StockoutStatus.STOCKOUT_EPC_STATUS_OK){
									if(item_epc.getStatus().equals(StockoutStatus.STOCKOUT_EPC_STATUS_RIP)) {
										// cây vải xé -> ko thay đổi warehouse
									}else {
										// cây vải chưa xé -> thay đổi warehouse
										warehouseService.setEpcOnLogistic(item_epc.getEpc());
									}
									
									//Tao Warehouse_logs trong danh sach de update sau
									Warehouse_logs warehouse_logs = new Warehouse_logs();
									warehouse_logs.setEpc(item_epc.getEpc());
									warehouse_logs.setOrglogid_link(stockout.getOrgid_from_link());
									warehouse_logs.setEventtype(WarehouseEventType.EVENT_TYPE_STOCKOUT);
									warehouse_logs.setEvent_objid_link(null);
									warehouse_logs.setTimelog(new Date());
									lswarehouse_logs.add(warehouse_logs);
								} else {
									throw new RuntimeException(ResponseMessage.getMessage(ResponseMessage.KEY_STOCKOUT_EPC_NOTCHECK));
								}
							}
						}
					}
				}
				
				//Ghi Warehouse_logs
		    	for(Warehouse_logs ws_log:lswarehouse_logs){
		    		ws_log.setEvent_objid_link(stockout.getId());
		    		warehouse_logs_Service.save(ws_log);
		    	}
							
				
				// update Approverid
				stockout.setApprove_date(today);
				stockout.setApprover_userid_link(entity.approver_userid_link);
				stockout.setLasttimeupdate(today);
				stockout.setLastuserupdateid_link(user.getUserId());
				stockout.setStatus(StockoutStatus.STOCKOUT_STATUS_APPROVED);
				stockOutService.save(stockout);
				
				//Kiểm tra và update trạng thái của Yêu cầu xuất
				if (null!=stockout.getStockoutorderid_link()){
					checkStockoutOrder_Complete(stockout);
				}
	
				//Nếu là xuất điều chuyển --> Tạo yêu cầu nhập cho đơn vị nhận
				if (stockout.getStockouttypeid_link().equals(StockoutType.STOCKOUT_TYPE_NL_MOVE)
						|| stockout.getStockouttypeid_link().equals(StockoutType.STOCKOUT_TYPE_PL_MOVE)
						|| stockout.getStockouttypeid_link().equals(StockoutType.STOCKOUT_TYPE_TP_MOVE)
						|| stockout.getStockouttypeid_link().equals(StockoutType.STOCKOUT_TYPE_CH_MOVE))
				{
					StockIn newStockin = new StockIn();
					newStockin.setStockoutid_link(stockout.getId());
					newStockin.setPcontractid_link(stockout.getPcontractid_link());
					newStockin.setPcontract_poid_link(stockout.getPcontract_poid_link());
					newStockin.setOrgid_from_link(stockout.getOrgid_from_link());
					newStockin.setOrgid_to_link(stockout.getOrgid_to_link());
					newStockin.setUnitid_link(stockout.getUnitid_link());
					newStockin.setStockout_code(stockout.getStockoutcode());
					
					newStockin.setTotalm3(stockout.getTotalm3());
					newStockin.setOrgrootid_link(stockout.getOrgrootid_link());
					newStockin.setUsercreateid_link(user.getUserId());
					newStockin.setTimecreate(today);
					newStockin.setStockincode(commonService.GetStockinCode());
					newStockin.setStatus(StockinStatus.STOCKIN_STATUS_ERR);
					newStockin.setStockindate(today);
					newStockin.setUsercreateid_link(user.getUserId());
					newStockin.setTimecreate(today);
					newStockin.setLastuserupdateid_link(user.getUserId());
					newStockin.setLasttimeupdate(today);
					// update Stocking_UniqueCode
					Stocking_UniqueCode unique = stockingService.getby_type(1);
					unique.setStocking_max(unique.getStocking_max()+ 1);
					stockingService.save(unique);
					
					if (stockout.getStockouttypeid_link().equals(StockoutType.STOCKOUT_TYPE_NL_MOVE))
						newStockin.setStockintypeid_link(StockinTypeConst.STOCKIN_TYPE_NL_MOVE);
					else if (stockout.getStockouttypeid_link().equals(StockoutType.STOCKOUT_TYPE_PL_MOVE))
						newStockin.setStockintypeid_link(StockinTypeConst.STOCKIN_TYPE_PL_MOVE);
					else if (stockout.getStockouttypeid_link().equals(StockoutType.STOCKOUT_TYPE_TP_MOVE))
						newStockin.setStockintypeid_link(StockinTypeConst.STOCKIN_TYPE_TP_MOVE);
					else if (stockout.getStockouttypeid_link().equals(StockoutType.STOCKOUT_TYPE_CH_MOVE))
						newStockin.setStockintypeid_link(StockinTypeConst.STOCKIN_TYPE_CH_MOVE);
					
					for(StockOutD theStockoutD: stockout.getStockout_d()){
						StockInD newStockinD = new StockInD();
						newStockinD.setOrgrootid_link(stockout.getOrgrootid_link());
						newStockinD.setSkuid_link(theStockoutD.getSkuid_link());
						newStockinD.setColorid_link(theStockoutD.getColorid_link());
						newStockinD.setUnitid_link(theStockoutD.getUnitid_link());
						
						newStockinD.setTotalpackage_order(theStockoutD.getTotalpackagecheck());
						newStockinD.setTotalpackage(theStockoutD.getTotalpackagecheck());
						newStockinD.setTotalpackagecheck(theStockoutD.getTotalpackagecheck());
						newStockinD.setTotalydsorigin(theStockoutD.getTotalydscheck());
						newStockinD.setTotalmet_origin(theStockoutD.getTotalmet_check());

						newStockinD.setUnitprice(theStockoutD.getUnitprice());
						newStockinD.setP_skuid_link(theStockoutD.getP_skuid_link());
						newStockinD.setSkucode(theStockoutD.getSkucode());
						newStockinD.setSizeid_link(theStockoutD.getSizeid_link());
						
						newStockinD.setUsercreateid_link(user.getUserId());
						newStockinD.setTimecreate(today);
						
						if(entity.isAutoChecked) {
							newStockinD.setStatus(StockinStatus.STOCKIN_D_STATUS_OK);
						}else {
							newStockinD.setStatus(StockinStatus.STOCKIN_D_STATUS_ERR);
						}
						
						for (StockOutPklist theStockout_Pklist: theStockoutD.getStockout_packinglist()){
							StockInPklist newStockin_Pklist = new StockInPklist();
							newStockin_Pklist.setOrgrootid_link(stockout.getOrgrootid_link());
							newStockin_Pklist.setSkuid_link(theStockout_Pklist.getSkuid_link());
							newStockin_Pklist.setColorid_link(theStockout_Pklist.getColorid_link());
							newStockin_Pklist.setLotnumber(theStockout_Pklist.getLotnumber());
							newStockin_Pklist.setPackageid(theStockout_Pklist.getPackageid());
							
							newStockin_Pklist.setYdsorigin(theStockout_Pklist.getYdscheck());
							newStockin_Pklist.setYdscheck(theStockout_Pklist.getYdscheck());
							
							newStockin_Pklist.setMet_origin(theStockout_Pklist.getMet_check());
							newStockin_Pklist.setMet_check(theStockout_Pklist.getMet_check());
							
							newStockin_Pklist.setWidth(theStockout_Pklist.getWidthcheck());
							newStockin_Pklist.setWidth_met(theStockout_Pklist.getWidthcheck());
							newStockin_Pklist.setWidth_met_check(theStockout_Pklist.getWidthcheck());
							
							newStockin_Pklist.setNetweight(theStockout_Pklist.getNetweight());
							newStockin_Pklist.setGrossweight(theStockout_Pklist.getGrossweight());
							newStockin_Pklist.setGrossweight_check(theStockout_Pklist.getGrossweight());
							newStockin_Pklist.setEpc(theStockout_Pklist.getEpc());
							newStockin_Pklist.setUnitid_link(theStockout_Pklist.getUnitid_link());
							newStockin_Pklist.setSkutypeid_link(theStockout_Pklist.getSkutypeid_link());
							newStockin_Pklist.setComment(theStockout_Pklist.getExtrainfo());
							
							newStockin_Pklist.setUsercreateid_link(user.getUserId());
							newStockin_Pklist.setTimecreate(new Date());
							
//							if(entity.isAutoChecked) {
//								newStockin_Pklist.setStatus(StockinStatus.STOCKIN_EPC_STATUS_OK);
//							}else {
//								newStockin_Pklist.setStatus(StockinStatus.STOCKIN_EPC_STATUS_ERR);
//							}
							
							if(stockout.getStockouttypeid_link().equals(StockoutType.STOCKOUT_TYPE_TP_MOVE)) {
								newStockin_Pklist.setStatus(theStockout_Pklist.getStatus());
							}else {
								newStockin_Pklist.setStatus(StockinStatus.STOCKIN_EPC_STATUS_OK);
							}
							
							newStockinD.getStockin_packinglist().add(newStockin_Pklist);
						}
						newStockin.getStockin_d().add(newStockinD);
					}
					
					//Luu va tao yeu cau nhap cho Noi nhan
					newStockin = stockInService.save(newStockin);
				}
				
				//Nếu là xuất thành phẩm theo đơn -> update stockid_link thành id vendor
				if(stockout.getStockouttypeid_link().equals(StockoutType.STOCKOUT_TYPE_TP_PO)) {
					List<StockOutD> stockOutD_list = stockout.getStockout_d();
					for(StockOutD stockOutD : stockOutD_list) {
						List<StockOutPklist> stockOutPklist_list = stockOutD.getStockout_packinglist();
						for(StockOutPklist stockOutPklist : stockOutPklist_list) {
							if(stockOutPklist.getEpc() != null) {
								List<Warehouse> warehouse_list = warehouseService.findMaterialByEPC(stockOutPklist.getEpc());
								if(warehouse_list.size() > 0) {
									Warehouse warehouse = warehouse_list.get(0);
									warehouse.setStockid_link(stockout.getOrgid_to_link());
									warehouse.setIs_freeze(false);
									warehouseService.save(warehouse);
								}
							}
						}
					}
				}
				
				response.data = stockout;
				response.id = stockout.getId();
				
				response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
				response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
				return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
			} else {
				response.setRespcode(ResponseMessage.KEY_STOCKOUT_NOT_FOUND);
				response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_STOCKOUT_NOT_FOUND));
				return new ResponseEntity<ResponseBase>(response,HttpStatus.BAD_REQUEST);	
			}
		}catch (RuntimeException e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			e.printStackTrace();
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value = "/stockout_approve_xuatDieuChuyenVai",method = RequestMethod.POST)
	@Transactional(rollbackFor = RuntimeException.class)
	public ResponseEntity<?> stockout_approve_xuatDieuChuyenVai(@RequestBody StockoutConfirmRequest entity, HttpServletRequest request ) {
		StockOut_Create_response response = new StockOut_Create_response();
		try {
			GpayAuthentication user = (GpayAuthentication)SecurityContextHolder.getContext().getAuthentication();
			
			Date today = new Date();
			StockOut stockout = stockOutService.findOne(entity.stockoutId);
			
			
			if (null!=stockout){
//			if (stockout.getStatus() == StockoutStatus.STOCKOUT_STATUS_OK){
				
				// Kiểm tra trạng thái phiếu xuất
				if(stockout.getStatus() >= StockoutStatus.STOCKOUT_STATUS_APPROVED) {
					response.setRespcode(ResponseMessage.KEY_RC_BAD_REQUEST);
					response.setMessage("Phiếu xuất kho đã được duyệt xuất");
					return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
				}
				
				// Kiểm tra nếu ko có đơn vị đích, thông báo lỗi
				if(stockout.getOrgid_to_link() == null) {
					response.setRespcode(ResponseMessage.KEY_RC_BAD_REQUEST);
					response.setMessage("Phiếu xuất kho phải có thông tin đơn vị nhận");
					return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
				}
				
				// Kiểm tra nếu ko chứa pklist, thông báo lỗi
				Boolean isContainPkl = false;
				List<StockOutD> stockOutD__list = stockout.getStockout_d();
				for(StockOutD stockOutD : stockOutD__list) {
					List<StockOutPklist> stockOutPklist__list = stockOutD.getStockout_packinglist();
					if(stockOutPklist__list.size() > 0) {
						isContainPkl = true;
						break;
					}
				}
				if(!isContainPkl) {
					response.setRespcode(ResponseMessage.KEY_RC_BAD_REQUEST);
					response.setMessage("Phiếu xuất kho chưa có NPL/thành phẩm");
					return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
				}
				
				//Nếu các EPC đều hợp lệ, Đưa epc trong bảng Warehouse về onLogistic Stockid=-1
				List<StockOutD> stockoutd = stockout.getStockout_d();
				List<Warehouse_logs> lswarehouse_logs =  new ArrayList<Warehouse_logs>();
				
				if(stockoutd !=null && stockoutd.size() >0) {
					for (StockOutD item_sku : stockoutd) {
						List<StockOutPklist> Pklist = item_sku.getStockout_packinglist();
						if(Pklist !=null && Pklist.size() >0) {
							for (StockOutPklist item_epc : Pklist) {
								if (item_epc.getStatus() >= StockoutStatus.STOCKOUT_EPC_STATUS_OK){
									if(item_epc.getStatus().equals(StockoutStatus.STOCKOUT_EPC_STATUS_RIP)) {
										// cây vải xé -> ko thay đổi warehouse
									}else {
										// cây vải chưa xé -> thay đổi warehouse
										List<Warehouse> epc_ls = warehouseService.findMaterialByEPC(item_epc.getEpc());
										if (epc_ls.size() > 0){
											Warehouse myepc = epc_ls.get(0);
											myepc.setStockid_link(EPCStockStatus.EPCSTOCK_ONLOGISTIC);
											myepc.setP_skuid_link(stockout.getPcontract_productid_link()); // set value là sản phẩm mược cây vải này
											warehouseService.save(myepc);
										}
									}
									
									//Tao Warehouse_logs trong danh sach de update sau
									Warehouse_logs warehouse_logs = new Warehouse_logs();
									warehouse_logs.setEpc(item_epc.getEpc());
									warehouse_logs.setOrglogid_link(stockout.getOrgid_from_link());
									warehouse_logs.setEventtype(WarehouseEventType.EVENT_TYPE_STOCKOUT);
									warehouse_logs.setEvent_objid_link(null);
									warehouse_logs.setTimelog(today);
									lswarehouse_logs.add(warehouse_logs);
								} else {
									throw new RuntimeException(ResponseMessage.getMessage(ResponseMessage.KEY_STOCKOUT_EPC_NOTCHECK));
								}
							}
						}
					}
				}
				
				//Ghi Warehouse_logs
		    	for(Warehouse_logs ws_log:lswarehouse_logs){
		    		ws_log.setEvent_objid_link(stockout.getId());
		    		warehouse_logs_Service.save(ws_log);
		    	}
							
				
				// update Approverid
				stockout.setApprove_date(today);
				stockout.setApprover_userid_link(entity.approver_userid_link);
				stockout.setLasttimeupdate(today);
				stockout.setLastuserupdateid_link(user.getUserId());
				stockout.setStatus(StockoutStatus.STOCKOUT_STATUS_APPROVED);
				stockOutService.save(stockout);
				
				//Nếu là xuất điều chuyển --> Tạo yêu cầu nhập cho đơn vị nhận
				if (stockout.getStockouttypeid_link().equals(StockoutType.STOCKOUT_TYPE_NL_MOVE))
				{
					StockIn newStockin = new StockIn();
					newStockin.setStockoutid_link(stockout.getId());
					newStockin.setPcontractid_link(stockout.getPcontractid_link());
					newStockin.setPcontract_poid_link(stockout.getPcontract_poid_link());
					newStockin.setOrgid_from_link(stockout.getOrgid_from_link());
					newStockin.setOrgid_to_link(stockout.getOrgid_to_link());
					newStockin.setUnitid_link(stockout.getUnitid_link());
					newStockin.setStockout_code(stockout.getStockoutcode());
					
					newStockin.setTotalm3(stockout.getTotalm3());
					newStockin.setOrgrootid_link(stockout.getOrgrootid_link());
					newStockin.setUsercreateid_link(user.getUserId());
					newStockin.setTimecreate(today);
					newStockin.setLasttimeupdate(today);
					newStockin.setStockincode(commonService.GetStockinCode());
					newStockin.setStatus(StockinStatus.STOCKIN_STATUS_OK);
					newStockin.setStockindate(today);
					newStockin.setUsercreateid_link(user.getUserId());
					newStockin.setTimecreate(today);
					
					// update Stocking_UniqueCode
					Stocking_UniqueCode unique = stockingService.getby_type(1);
					unique.setStocking_max(unique.getStocking_max()+ 1);
					stockingService.save(unique);
					
					if (stockout.getStockouttypeid_link().equals(StockoutType.STOCKOUT_TYPE_NL_MOVE))
						newStockin.setStockintypeid_link(StockinTypeConst.STOCKIN_TYPE_NL_MOVE);
					
					for(StockOutD theStockoutD: stockout.getStockout_d()){
						StockInD newStockinD = new StockInD();
						newStockinD.setOrgrootid_link(stockout.getOrgrootid_link());
						newStockinD.setSkuid_link(theStockoutD.getSkuid_link());
						newStockinD.setColorid_link(theStockoutD.getColorid_link());
						newStockinD.setUnitid_link(theStockoutD.getUnitid_link());
						
						newStockinD.setTotalpackage_order(theStockoutD.getTotalpackagecheck());
						newStockinD.setTotalpackage(theStockoutD.getTotalpackagecheck());
						newStockinD.setTotalydsorigin(theStockoutD.getTotalydscheck());
						newStockinD.setTotalmet_origin(theStockoutD.getTotalmet_check());

						newStockinD.setUnitprice(theStockoutD.getUnitprice());
						newStockinD.setP_skuid_link(theStockoutD.getP_skuid_link());
						newStockinD.setSkucode(theStockoutD.getSkucode());
						newStockinD.setSizeid_link(theStockoutD.getSizeid_link());
						
						newStockinD.setUsercreateid_link(user.getUserId());
						newStockinD.setTimecreate(today);
						
						newStockinD.setStatus(StockinStatus.STOCKIN_D_STATUS_OK);
						
						
						
						for (StockOutPklist theStockout_Pklist: theStockoutD.getStockout_packinglist()){
							StockInPklist newStockin_Pklist = new StockInPklist();
							newStockin_Pklist.setOrgrootid_link(stockout.getOrgrootid_link());
							newStockin_Pklist.setSkuid_link(theStockout_Pklist.getSkuid_link());
							newStockin_Pklist.setColorid_link(theStockout_Pklist.getColorid_link());
							newStockin_Pklist.setLotnumber(theStockout_Pklist.getLotnumber());
							newStockin_Pklist.setPackageid(theStockout_Pklist.getPackageid());
							
							newStockin_Pklist.setYdsorigin(theStockout_Pklist.getYdscheck());
							newStockin_Pklist.setYdscheck(theStockout_Pklist.getYdscheck());
							
							newStockin_Pklist.setMet_origin(theStockout_Pklist.getMet_check());
							newStockin_Pklist.setMet_check(theStockout_Pklist.getMet_check());
							
							newStockin_Pklist.setWidth(theStockout_Pklist.getWidthcheck());
							newStockin_Pklist.setWidth_met(theStockout_Pklist.getWidthcheck());
							newStockin_Pklist.setWidth_met_check(theStockout_Pklist.getWidthcheck());
							
							newStockin_Pklist.setNetweight(theStockout_Pklist.getNetweight());
							newStockin_Pklist.setGrossweight(theStockout_Pklist.getGrossweight());
							newStockin_Pklist.setGrossweight_check(theStockout_Pklist.getGrossweight());
							newStockin_Pklist.setGrossweight_lbs((float) (theStockout_Pklist.getGrossweight() * 2.205));
							newStockin_Pklist.setGrossweight_lbs_check((float) (theStockout_Pklist.getGrossweight() * 2.205));
							newStockin_Pklist.setEpc(theStockout_Pklist.getEpc());
							newStockin_Pklist.setUnitid_link(theStockout_Pklist.getUnitid_link());
							newStockin_Pklist.setSkutypeid_link(theStockout_Pklist.getSkutypeid_link());
							newStockin_Pklist.setComment(theStockout_Pklist.getExtrainfo());
							
							newStockin_Pklist.setUsercreateid_link(user.getUserId());
							newStockin_Pklist.setTimecreate(today);
							
							newStockin_Pklist.setStatus(StockinStatus.STOCKIN_EPC_STATUS_OK);
							
							newStockinD.getStockin_packinglist().add(newStockin_Pklist);
						}
						newStockin.getStockin_d().add(newStockinD);
					}
					
					//Luu va tao phieu nhap cho Noi nhan
					newStockin = stockInService.save(newStockin);
					
					//Tao danh sach lot
					StockIn stockin = stockInService.findOne(newStockin.getId());
					List<StockInD> stockInD_list = stockin.getStockin_d();
					for(StockInD stockInD : stockInD_list ) {
						List<StockInPklist> stockInPklist_list = stockInD.getStockin_packinglist();
						
//						System.out.println("-------");
//						System.out.println("stockInPklist_list: " + stockInPklist_list.size());
						
						Map<String, StockinLot> lot_map = new HashMap<String, StockinLot>();
						for(StockInPklist stockInPklist : stockInPklist_list) {
							String lotnumber = stockInPklist.getLotnumber();
							
//							System.out.println("lotnumber: " + lotnumber);
							if(!lot_map.containsKey(lotnumber)) {
								StockinLot newStockinLot = new StockinLot();
								newStockinLot.setId(null);
								newStockinLot.setOrgrootid_link(user.getRootorgid_link());
								newStockinLot.setStockinid_link(stockin.getId());
								newStockinLot.setMaterialid_link(stockInD.getSkuid_link());
								newStockinLot.setLot_number(lotnumber);
								newStockinLot.setTotalpackage(1);
								newStockinLot.setTotalpackagecheck(0);
								newStockinLot.setTotalyds(stockInPklist.getYdscheck());
								newStockinLot.setTotalmet(stockInPklist.getMet_check());
								newStockinLot.setStatus(StockinStatus.STOCKIN_LOT_CHECKED);
								newStockinLot.setTotalydscheck(stockInPklist.getYdscheck());
								newStockinLot.setTotalmetcheck(stockInPklist.getMet_check());
								newStockinLot.setGrossweight(stockInPklist.getGrossweight());
								newStockinLot.setGrossweight_check(stockInPklist.getGrossweight_check());
								newStockinLot.setTotalpackagepklist(1);
								newStockinLot.setStockindid_link(stockInD.getId());
								newStockinLot.setGrossweight_lbs(stockInPklist.getGrossweight_lbs());
								newStockinLot.setGrossweight_lbs_check(stockInPklist.getGrossweight_lbs_check());
								lot_map.put(lotnumber, newStockinLot);
							}else {
								StockinLot stockinLot = lot_map.get(lotnumber);
								stockinLot.setTotalpackage(stockinLot.getTotalpackage() + 1);
								stockinLot.setTotalyds(stockinLot.getTotalyds() + stockInPklist.getYdscheck());
								stockinLot.setTotalmet(stockinLot.getTotalmet() + stockInPklist.getMet_check());
								stockinLot.setTotalydscheck(stockinLot.getTotalydscheck() + stockInPklist.getYdscheck());
								stockinLot.setTotalmetcheck(stockinLot.getTotalmetcheck() + stockInPklist.getMet_check());
								stockinLot.setGrossweight(stockinLot.getGrossweight() + stockInPklist.getGrossweight());
								stockinLot.setGrossweight_check(stockinLot.getGrossweight_check() + stockInPklist.getGrossweight_check());
								stockinLot.setTotalpackagepklist(stockinLot.getTotalpackagepklist() + 1);
								stockinLot.setGrossweight_lbs(stockinLot.getGrossweight_lbs() + stockInPklist.getGrossweight_lbs());
								stockinLot.setGrossweight_lbs_check(stockinLot.getGrossweight_lbs_check() + stockInPklist.getGrossweight_lbs_check());
								lot_map.put(lotnumber, stockinLot);
							}
						}
						List<StockinLot> lot_list = new ArrayList<StockinLot>(lot_map.values());
						for(StockinLot lot : lot_list) {
							stockinLotService.save(lot);
						}
					}
				}
				
				response.data = stockout;
				response.id = stockout.getId();
				
				response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
				response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
				return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
			} else {
				response.setRespcode(ResponseMessage.KEY_STOCKOUT_NOT_FOUND);
				response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_STOCKOUT_NOT_FOUND));
				return new ResponseEntity<ResponseBase>(response,HttpStatus.BAD_REQUEST);	
			}
		}catch (RuntimeException e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			e.printStackTrace();
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value = "/stockout_unapprove",method = RequestMethod.POST)
	@Transactional(rollbackFor = RuntimeException.class)
	public ResponseEntity<?> StockoutUnApprove(@RequestBody StockoutConfirmRequest entity, HttpServletRequest request ) {
		StockOut_Create_response response = new StockOut_Create_response();
		try {
			GpayAuthentication user = (GpayAuthentication)SecurityContextHolder.getContext().getAuthentication();
			
			Date today = new Date();
			StockOut stockout = stockOutService.findOne(entity.stockoutId);
//			StockOut stockout_entity = entity.stockout;
			
			if (null!=stockout){
				// kiểm tra trạng thái phieu
				if(stockout.getStatus() < StockoutStatus.STOCKOUT_STATUS_APPROVED) {
					response.setRespcode(ResponseMessage.KEY_RC_BAD_REQUEST);
					response.setMessage("Phiếu xuất kho chưa được duyệt xuất");
					return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
				}
				
				if(stockout.getStatus() >= StockoutStatus.STOCKOUT_STATUS_RECEIVED) {
					response.setRespcode(ResponseMessage.KEY_RC_BAD_REQUEST);
					response.setMessage("Phiếu xuất kho đã được nơi nhận duyệt nhận");
					return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
				}
				
				//Nếu các EPC đều hợp lệ, Đưa epc trong bảng Warehouse về onLogistic Stockid=-1
				List<StockOutD> stockoutd = stockout.getStockout_d();
				
				if(stockoutd !=null && stockoutd.size() >0) {
					for (StockOutD item_sku : stockoutd) {
						List<StockOutPklist> Pklist = item_sku.getStockout_packinglist();
						if(Pklist !=null && Pklist.size() >0) {
							for (StockOutPklist item_epc : Pklist) {
								String epc = item_epc.getEpc();
								List<Warehouse> warehouse_list = warehouseService.findMaterialByEPC(epc);
								if(warehouse_list.size() > 0) {
									Warehouse warehouse = warehouse_list.get(0);
									if(warehouse.getStockinid_link() == null && warehouse.getStockindid_link() == null) { 
//										System.out.println("tren");
										// cây vải thêm, không có stockinid -> xóa
										warehouseService.delete(warehouse);
										// set epc của cây vải trong danh sách phiếu xuất về null
										item_epc.setEpc(null);
									}else {
//										System.out.println("duoi");
										// cây vải nhập theo stockin -> chuyển stockid về đơn vị cũ
										Long orgid_from_link = stockout.getOrgid_from_link();
										warehouse.setStockid_link(orgid_from_link);
										warehouseService.save(warehouse);
									}
								}
								
							}
						}
					}
				}
				stockout.setStatus(StockoutStatus.STOCKOUT_STATUS_OK);
				stockout.setApprove_date(null);
				stockout.setApprover_userid_link(null);
				stockout.setLasttimeupdate(today);
				stockout.setLastuserupdateid_link(user.getUserId());
				stockout = stockOutService.save(stockout);
				
				response.data = stockout;
				response.id = stockout.getId();
				
				response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
				response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
				return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
			} else {
				response.setRespcode(ResponseMessage.KEY_STOCKOUT_NOT_FOUND);
				response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_STOCKOUT_NOT_FOUND));
				return new ResponseEntity<ResponseBase>(response,HttpStatus.BAD_REQUEST);	
			}
		}catch (RuntimeException e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			e.printStackTrace();
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value = "/stockout_unapprove_xuatDieuChuyenVai",method = RequestMethod.POST)
	@Transactional(rollbackFor = RuntimeException.class)
	public ResponseEntity<?> stockout_unapprove_xuatDieuChuyenVai(@RequestBody StockoutConfirmRequest entity, HttpServletRequest request ) {
		StockOut_Create_response response = new StockOut_Create_response();
		try {
			GpayAuthentication user = (GpayAuthentication)SecurityContextHolder.getContext().getAuthentication();
			
			Date today = new Date();
			StockOut stockout = stockOutService.findOne(entity.stockoutId);
//			StockOut stockout_entity = entity.stockout;
			
			if (null!=stockout){
				// kiểm tra trạng thái phieu
				if(stockout.getStatus() < StockoutStatus.STOCKOUT_STATUS_APPROVED) {
					response.setRespcode(ResponseMessage.KEY_RC_BAD_REQUEST);
					response.setMessage("Phiếu xuất kho chưa được duyệt xuất");
					return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
				}
				
				List<StockIn> stockinList = stockInService.findByStockoutId(stockout.getId());
//				System.out.println("stockinList.size()");
//				System.out.println(stockinList.size());
				if(stockinList.size() > 0) {
					for(StockIn stockIn : stockinList) {
						if(stockIn.getStatus() >= StockinStatus.STOCKIN_STATUS_APPROVED) {
							response.setRespcode(ResponseMessage.KEY_RC_BAD_REQUEST);
							response.setMessage("Phiếu xuất kho đã được nơi nhận duyệt nhận");
							return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
						}
					}
					
					// xóa phiếu nhập
					for(StockIn stockIn : stockinList) {
//						System.out.println("deleted status -2");
						stockIn.setStatus(StockinStatus.STOCKIN_STATUS_DELETED);
						stockInService.save(stockIn);
					}
				}
				
				List<StockOutD> stockoutd = stockout.getStockout_d();
				
				if(stockoutd !=null && stockoutd.size() >0) {
					for (StockOutD item_sku : stockoutd) {
						List<StockOutPklist> Pklist = item_sku.getStockout_packinglist();
						if(Pklist !=null && Pklist.size() >0) {
							for (StockOutPklist item_epc : Pklist) {
								String epc = item_epc.getEpc();
								List<Warehouse> warehouse_list = warehouseService.findMaterialByEPC(epc);
								if(warehouse_list.size() > 0) {
									Warehouse warehouse = warehouse_list.get(0);
									warehouse.setP_skuid_link(null);
									warehouse.setStockid_link(stockout.getOrgid_from_link());
									warehouseService.save(warehouse);
								}
							}
						}
					}
				}
				stockout.setStatus(StockoutStatus.STOCKOUT_STATUS_OK);
				stockout.setApprove_date(null);
				stockout.setApprover_userid_link(null);
				stockout.setLasttimeupdate(today);
				stockout.setLastuserupdateid_link(user.getUserId());
				stockout = stockOutService.save(stockout);
				
				response.data = stockout;
				response.id = stockout.getId();
				
				response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
				response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
				return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
			} else {
				response.setRespcode(ResponseMessage.KEY_STOCKOUT_NOT_FOUND);
				response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_STOCKOUT_NOT_FOUND));
				return new ResponseEntity<ResponseBase>(response,HttpStatus.BAD_REQUEST);	
			}
		}catch (RuntimeException e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			e.printStackTrace();
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value = "/stockout_approve_material",method = RequestMethod.POST)
	@Transactional(rollbackFor = RuntimeException.class)
	public ResponseEntity<?> StockoutApprove_Material(@RequestBody StockoutConfirmRequest entity, HttpServletRequest request ) {
		StockOut_Create_response response = new StockOut_Create_response();
		try {
//			System.out.println(entity.stockinId);
//			System.out.println(entity.approver_userid_link);
			
//			GpayAuthentication user = (GpayAuthentication)SecurityContextHolder.getContext().getAuthentication();
			Date today = new Date();
			StockOut stockout = stockOutService.findOne(entity.stockoutId);
			
			// update Approverid
			stockout.setApprove_date(today);
			stockout.setApprover_userid_link(entity.approver_userid_link);
			stockout.setStatus(1);
			stockOutService.save(stockout);
			
			response.data = stockout;
			
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
	
	@RequestMapping(value = "/stockout_receive_material",method = RequestMethod.POST)
	public ResponseEntity<?> StockoutReceive_Material(@RequestBody StockoutConfirmRequest entity, HttpServletRequest request ) {
		StockOut_Create_response response = new StockOut_Create_response();
		try {
//			System.out.println(entity.stockinId);
//			System.out.println(entity.approver_userid_link);
			
//			GpayAuthentication user = (GpayAuthentication)SecurityContextHolder.getContext().getAuthentication();
			Date today = new Date();
			StockOut stockout = stockOutService.findOne(entity.stockoutId);
			
			// update stockid_link các cây vải trong warehouse
			List<StockOutD> stockOutD_list = stockout.getStockout_d();
			for(StockOutD stockOutD : stockOutD_list) {
				List<StockOutPklist> stockOutPklist_list = stockOutD.getStockout_packinglist();
				for(StockOutPklist stockOutPklist : stockOutPklist_list) {
					String epc = stockOutPklist.getEpc();
					List<Warehouse> warehouse_list = warehouseService.findMaterialByEPC(epc);
					if(warehouse_list.size() > 0) {
						Warehouse warehouse = warehouse_list.get(0);
						warehouse.setStockid_link(stockout.getOrgid_to_link());
						warehouseService.save(warehouse);
					}
				}
			}
			
			// update Receiveid
			stockout.setReceive_date(today);
			stockout.setReceiver_userid_link(entity.receiver_userid_link);
			stockout.setStatus(2);
			stockout = stockOutService.save(stockout);
			
			response.data = stockout;
			response.id = stockout.getId();
			
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
	
	@RequestMapping(value = "/stockout_getone",method = RequestMethod.POST)
	public ResponseEntity<?> StockoutGetone(@RequestBody StockoutGetoneRequest entity, HttpServletRequest request ) {
		StockoutResponse response = new StockoutResponse();
		try {
			GpayAuthentication user = (GpayAuthentication)SecurityContextHolder.getContext().getAuthentication();
			response.data = stockOutService.stockout_getone(user.getOrgId(),entity.stockoutcode, entity.stockcode);
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<StockoutResponse>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	@RequestMapping(value = "/gettype",method = RequestMethod.POST)
	public ResponseEntity<?> GetType(@RequestBody StockoutTypeRequest entity, HttpServletRequest request ) {
		Stockout_gettype_response response = new Stockout_gettype_response();
		try {
//			response.data = stockouttypeService.findAll();
			
			Long idFrom = entity.typeFrom;
			Long idTo = entity.typeTo;
			
			response.data = stockouttypeService.findStockoutTypeByIdRange(idFrom, idTo); // <= ; >=
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<Stockout_gettype_response>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/stockout_list",method = RequestMethod.POST)
	public ResponseEntity<?> StockoutList(@RequestBody StockoutListRequest entity, HttpServletRequest request ) {
		StockoutResponse response = new StockoutResponse();
		try {
//			GpayAuthentication user = (GpayAuthentication)SecurityContextHolder.getContext().getAuthentication();
//			Long orgid_from_link = null;
//			long orgrootid_link = user.getRootorgid_link();
//			if(!user.isOrgRoot()) {
//				orgid_from_link =entity.orgid_from_link;
//			}
			
			
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Long orgid_link = user.getOrgid_link();
			Org userOrg = orgService.findOne(orgid_link);
			Integer userOrgType = userOrg.getOrgtypeid_link();
			
			if (entity.page == 0) entity.page = 1;
			if (entity.limit == 0) entity.limit = 100;
			
			if(userOrgType == OrgType.ORG_TYPE_TRUSOCHINH) {
				Page<StockOut> pageStockout = stockOutService.stockout_list_page(
						user.getRootorgid_link(),
						entity.stockouttypeid_link,
						entity.stockouttypefrom,
						entity.stockouttypeto,
						entity.stockoutcode,
						entity.orgid_from_link,
						entity.orgid_to_link,
						entity.stockoutdate_from, 
						entity.stockoutdate_to ,
						entity.page, 
						entity.limit, 
						entity.statuses,
						null);
				response.data = pageStockout.getContent();
				response.totalCount = pageStockout.getTotalElements();
			}else if(userOrgType == OrgType.ORG_TYPE_XUONGSX) {
				Long org_grant_id_link = user.getOrg_grant_id_link();
				List<Long> listStockoutId = new ArrayList<Long>();
				if(org_grant_id_link != null) {
					// Nếu có org_grant_id_link
					listStockoutId = stockOutService.getStockoutIdByOrgTo(org_grant_id_link);
					if(listStockoutId.size() == 0) {
						response.data = new ArrayList<StockOut>();
						response.totalCount = 0;
						response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
						response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
						return new ResponseEntity<StockoutResponse>(response,HttpStatus.OK);
					}
				}
				if(listStockoutId.size() == 0) listStockoutId = null;
				Page<StockOut> pageStockout = stockOutService.stockout_list_page(
						user.getRootorgid_link(),
						entity.stockouttypeid_link,
						entity.stockouttypefrom,
						entity.stockouttypeto,
						entity.stockoutcode,
						entity.orgid_from_link,
						entity.orgid_to_link,
						entity.stockoutdate_from, 
						entity.stockoutdate_to ,
						entity.page, 
						entity.limit,
						entity.statuses,
						listStockoutId);
				response.data = pageStockout.getContent();
				response.totalCount = pageStockout.getTotalElements();
			}
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<StockoutResponse>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			e.printStackTrace();
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/stockout_listbyorgto",method = RequestMethod.POST)
	public ResponseEntity<?> Stockout_listbyorgto(@RequestBody StockoutListRequest entity, HttpServletRequest request ) {
		StockoutResponse response = new StockoutResponse();
		try {
			response.data = stockOutService.stockout_listByOrgTo(entity.stockouttypeid_link, entity.orgid_to_link, entity.status);
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<StockoutResponse>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@RequestMapping(value = "/stockout_activate",method = RequestMethod.POST)
	public ResponseEntity<?> StockoutActivate(@RequestBody StockOutActivateRequest entity, HttpServletRequest request ) {
		StockoutResponse response = new StockoutResponse();
		try {
			GpayAuthentication user = (GpayAuthentication)SecurityContextHolder.getContext().getAuthentication();
			response.data = stockOutService.stockout_listByOrgTo(entity.stockouttypeid_link, user.getOrgId(), null);
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<StockoutResponse>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@RequestMapping(value = "/stockout_getbyid",method = RequestMethod.POST)
	public ResponseEntity<?> StockoutGetByID(@RequestBody GetStockoutByIDRequest entity, HttpServletRequest request ) {
		StockoutByIDReponse response = new StockoutByIDReponse();
		try {
			StockOut stockout = stockOutService.findOne(entity.id);
			List<StockOutD> stockOutD_list = stockout.getStockout_d();
			for(StockOutD stockOutD : stockOutD_list) {
				
				// loai thanh pham
				
				Boolean loaiThanhPham1 = false;
				Boolean loaiThanhPham2 = false;
				Boolean loaiThanhPham3 = false;
				String loaiThanhPham = "";
				
				List<StockOutPklist> stockOutPklist_list = stockOutD.getStockout_packinglist();
				for(StockOutPklist stockOutPklist : stockOutPklist_list) {
					if(stockOutPklist.getStatus().equals(StockoutStatus.TP_LOAI1)){
						loaiThanhPham1 = true;
					}
					if(stockOutPklist.getStatus().equals(StockoutStatus.TP_LOAI2)){
						loaiThanhPham2 = true;
					}
					if(stockOutPklist.getStatus().equals(StockoutStatus.TP_LOAI3)){
						loaiThanhPham3 = true;
					}
					
					//
					String epc = stockOutPklist.getEpc();
					//
					List<Warehouse> warehouse_list = warehouseService.findMaterialByEPCAndStock(epc, stockout.getOrgid_from_link());
					if(warehouse_list.size() > 0) {
						Warehouse warehouse = warehouse_list.get(0);
						String spaceepc_link = warehouse.getSpaceepc_link();
						List<Stockspace> stockspace_list = stockspaceService.findStockspaceByEpc(spaceepc_link);
						
						if(stockspace_list.size() > 0) {
							Stockspace stockspace = stockspace_list.get(0);
							String spaceString = "" + stockspace.getStockrow_code() + "-" + stockspace.getSpacename() + "-" + stockspace.getFloorid();
							stockOutPklist.setSpaceString(spaceString);
						}else {
							stockOutPklist.setSpaceString("KXD");
						}
					}
					//
					//
					warehouse_list = warehouseService.findMaterialByEPC(epc);
					if(warehouse_list.size() > 0) {
						Warehouse warehouse = warehouse_list.get(0);
						if(warehouse.getStatus() > WareHouseStatus.WAREHOUSE_STATUS_UNCHECKED) {
							// đã tở -> lấy ngày tở
							List<WarehouseCheck> warehouseCheck_list = warehouseCheckService.findBy_Warehouse_StockoutOrder(null, warehouse.getId());
							if(warehouseCheck_list.size() > 0) {
								WarehouseCheck warehouseCheck = warehouseCheck_list.get(0);
								Date date_check = warehouseCheck.getDate_check();
								stockOutPklist.setDate_check(date_check);
							}
						}
						//
						if(warehouse.getStatus() == WareHouseStatus.WAREHOUSE_STATUS_UNCHECKED) {
							stockOutPklist.setWarehouseStatusString("Chưa tở");
						}else
						if(warehouse.getStatus() == WareHouseStatus.WAREHOUSE_STATUS_CHECKED) {
							stockOutPklist.setWarehouseStatusString("Đã tở");
						}else
						if(warehouse.getStatus() == WareHouseStatus.WAREHOUSE_STATUS_CUT) {
							stockOutPklist.setWarehouseStatusString("Đã cắt");
						}
					}
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
				stockOutD.setLoaiThanhPham(loaiThanhPham);
				
				// sl ton kho
				Integer totalSLTon = 0;
				if(stockout.getOrgid_from_link() != null) {
					Long skuid_link = stockOutD.getSkuid_link();
					Long totalSLTon_Long = warehouseService.getSumBy_Sku_Stock(skuid_link, stockout.getOrgid_from_link());
					totalSLTon = totalSLTon_Long.intValue();
				}
				stockOutD.setTotalSLTon(totalSLTon);
			}
			
			response.data = stockout;
			response.listepc = stockOutPklistService.inv_getbyid(entity.id);
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<StockoutByIDReponse>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/stockout_deleteid",method = RequestMethod.POST)
	@Transactional(rollbackFor = RuntimeException.class)
	public ResponseEntity<?> StockoutDeleteByID(@RequestBody GetStockoutByIDRequest entity, HttpServletRequest request ) {
		ResponseBase response = new ResponseBase();
		try {
			StockOut stockout = stockOutService.findOne(entity.id);
			
			if(stockout.getStatus() >= StockoutStatus.STOCKOUT_STATUS_APPROVED) {
				ResponseError errorBase = new ResponseError();
				errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
				errorBase.setMessage("Phiếu xuất đã được duyệt");
			    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
			}else {
				stockout.setStatus(StockoutStatus.STOCKOUT_STATUS_DELETED);
				stockOutService.save(stockout);
				
				if(
					stockout.getStockouttypeid_link().equals(StockoutType.STOCKOUT_TYPE_TP_PO) ||
					stockout.getStockouttypeid_link().equals(StockoutType.STOCKOUT_TYPE_TP_MOVE)
					) {
					// xoá phiếu xuất thành phẩm -> set is_freeze warehouse false
					List<StockOutD> stockOutD_list = stockout.getStockout_d();
					for(StockOutD stockOutD : stockOutD_list) {
						List<StockOutPklist> stockOutPklist_list = stockOutD.getStockout_packinglist();
						for(StockOutPklist stockOutPklist : stockOutPklist_list) {
							String epc = stockOutPklist.getEpc();
							if(epc != null) {
								List<Warehouse> warehouse = warehouseService.findMaterialByEPC(epc);
								if(warehouse.size() > 0) {
									warehouse.get(0).setIs_freeze(false);
									warehouseService.save(warehouse.get(0));
								}
							}
						}
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
	
	@RequestMapping(value = "/stockout_material_list",method = RequestMethod.POST)
	public ResponseEntity<?> stockout_material_list(@RequestBody StockoutListRequest entity, HttpServletRequest request ) {
		StockoutObjResponse response = new StockoutObjResponse();
		try {
//			GpayAuthentication user = (GpayAuthentication)SecurityContextHolder.getContext().getAuthentication();
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			GpayUser user_info = userDetailsService.findById(user.getId());
			
			List<Long> orgid_from_linkArray = new ArrayList<Long>();
			
			if (null!= user_info && user_info.getOrgid_link()!=1){//Neu la user cap cao --> Ko han che dvi nhap
				entity.orgid_from_link = null!= user_info.getOrg_grant_id_link()?user_info.getOrg_grant_id_link():user_info.getOrgid_link();
				
				Org orgFrom = orgService.findOne(entity.orgid_from_link); 
				Integer orgFromType = orgFrom.getOrgtypeid_link();
				if(orgFromType.equals(OrgType.ORG_TYPE_KHONGUYENLIEU)) {
					// thêm kho vào array
					orgid_from_linkArray.add(orgFrom.getId());
				}
				if(orgFromType.equals(OrgType.ORG_TYPE_XUONGSX)) {
					// Tìm danh sách kho của xưởng sản xuất
					List<Org> khoList = orgService.findOrgByParentAndType(OrgType.ORG_TYPE_KHONGUYENLIEU, orgFrom.getId());
					for(Org kho : khoList) { // loop thêm danh sách kho vào array
						orgid_from_linkArray.add(kho.getId());
					}
				}
			}
			if (null!= user_info && user_info.getOrgid_link() == (long)1){
				Long org_grant_id_link = user_info.getOrg_grant_id_link();
				if(org_grant_id_link != null) {
					Org orgFrom = orgService.findOne(org_grant_id_link); 
					if(orgFrom.getOrgtypeid_link() == OrgType.ORG_TYPE_CUAHANG) {
						orgid_from_linkArray.add(org_grant_id_link);
					}
				}
			}
			
			// Dùng cho pop up cột Xuất kho ở Tiến độ giao hàng -> tab Cân đối NPL
			// Tìm theo loại vải (skuid_link)
			// Tìm danh sách id stockout chứa stockoutD có skuid_link 
			List<Long> listStockoutId;
			if(entity.skuid_link == null) {
				listStockoutId = null;
			}else {
				listStockoutId = stockOutDService.getStockoutIdBySkuId(entity.skuid_link);
				if(listStockoutId.size() == 0) {
					// nếu có gửi skuid_link lên, nếu list tìm được có size 0 -> ko tìm thấy -> trả về rỗng
					response.data = new ArrayList<StockOutObj>();
					response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
					response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
					return new ResponseEntity<StockoutObjResponse>(response,HttpStatus.OK);
				}
			}
			
			List<StockOut> stockout_list = stockOutService.stockout_material_list(
					user.getRootorgid_link(), 
					entity.stockouttypeid_link, 
					orgid_from_linkArray,
//					entity.orgid_from_link, 
					entity.orgid_to_link, 
					entity.stockoutdate_from, 
					entity.stockoutdate_to,
					entity.statuses,
					listStockoutId);
			
			if(entity.product == null) entity.product = "";
			entity.product = entity.product.trim().toLowerCase();
			if(entity.maNpl == null) entity.maNpl = "";
			entity.maNpl = entity.maNpl.trim().toLowerCase();
			if(entity.lotnumber == null) entity.lotnumber = "";
			entity.lotnumber = entity.lotnumber.trim().toLowerCase();
			
			List<StockOutObj> result = new ArrayList<StockOutObj>();
			for(StockOut stockOut : stockout_list) {
				StockOutObj stockOutObj = new StockOutObj();
				stockOutObj.setId(stockOut.getId());
				stockOutObj.setStockoutorderid_link(stockOut.getStockoutorderid_link());
				stockOutObj.setStockout_order_code(stockOut.getStockout_order_code());
				stockOutObj.setStockoutcode(stockOut.getStockoutcode());
				stockOutObj.setStockoutdate(stockOut.getStockoutdate());
				stockOutObj.setStockouttypeid_link(stockOut.getStockouttypeid_link());
				stockOutObj.setStockouttype_name(stockOut.getStockouttype_name());
				stockOutObj.setOrgid_from_link(stockOut.getOrgid_from_link());
				stockOutObj.setOrg_from_name(stockOut.getOrg_from_name());
				stockOutObj.setOrgid_to_link(stockOut.getOrgid_to_link());
				stockOutObj.setOrg_to_name(stockOut.getOrg_to_name());
				stockOutObj.setPorderid_link(stockOut.getPorderid_link());
				stockOutObj.setPordercode(stockOut.getPordercode());
				stockOutObj.setShipperson(stockOut.getShipperson());
				stockOutObj.setReason(stockOut.getReason());
				stockOutObj.setContract_number(stockOut.getContract_number());
				stockOutObj.setInvoice_number(stockOut.getInvoice_number());
				stockOutObj.setInvoice_date(stockOut.getInvoice_date());
				stockOutObj.setPcontract_poid_link(stockOut.getPcontract_poid_link());
				stockOutObj.setPcontractid_link(stockOut.getPcontractid_link());
				stockOutObj.setTotalpackage(stockOut.getTotalpackage());
				stockOutObj.setTotalpackagecheck(stockOut.getTotalpackagecheck());
				stockOutObj.setTotalyds(stockOut.getTotalyds());
				stockOutObj.setTotalydscheck(stockOut.getTotalydscheck());
				stockOutObj.setTotalm3(stockOut.getTotalm3());
				stockOutObj.setTotalnetweight(stockOut.getTotalnetweight());
				stockOutObj.setTotalgrossweight(stockOut.getTotalgrossweight());
				stockOutObj.setP_skuid_link(stockOut.getP_skuid_link());
				stockOutObj.setP_skucode(stockOut.getP_skucode());
				stockOutObj.setExtrainfo(stockOut.getExtrainfo());
				stockOutObj.setStatus(stockOut.getStatus());
				stockOutObj.setStatusString(stockOut.getStatusString());
				stockOutObj.setUsercreateid_link(stockOut.getUsercreateid_link());
				stockOutObj.setUsercreate_name(stockOut.getUsercreate_name());
				stockOutObj.setTimecreate(stockOut.getTimecreate());
				stockOutObj.setLastuserupdateid_link(stockOut.getLastuserupdateid_link());
				stockOutObj.setLasttimeupdate(stockOut.getLasttimeupdate());
				stockOutObj.setStockout_userid_link(stockOut.getStockout_userid_link());
				stockOutObj.setApprover_userid_link(stockOut.getApprover_userid_link());
				stockOutObj.setReceiver_userid_link(stockOut.getReceiver_userid_link());
				stockOutObj.setApprove_date(stockOut.getApprove_date());
				stockOutObj.setReceive_date(stockOut.getReceive_date());
				stockOutObj.setUnitid_link(stockOut.getUnitid_link());
				stockOutObj.setPorder_product_buyercode(stockOut.getPorder_product_buyercode());
				stockOutObj.setProduct_buyercode(stockOut.getProduct_buyercode());
				
				if(!entity.product.equals("")) {
					Boolean isContainProductbuyercode = false;
					String buyercode = stockOutObj.getPorder_product_buyercode().trim().toLowerCase();
					if(buyercode.contains(entity.product)) {
						isContainProductbuyercode = true;
					}
					if(!isContainProductbuyercode) continue;
				}
				
				if(!entity.maNpl.equals("")) {
					Boolean isContainNpl = false;
					List<StockOutD> stockoutD_list = stockOutDService.getBy_MaNpl_StockoutId(entity.maNpl, stockOut.getId());
					if(stockoutD_list.size() > 0) {
						isContainNpl = true;
					}
					if(!isContainNpl) continue;
				}
				
				if(!entity.lotnumber.equals("")) {
					Boolean isContainLotnumber = false;
					List<StockOutPklist> stockOutPklist_list = stockOutPklistService.getBy_Lotnumber_StockoutId(entity.lotnumber, stockOut.getId());
					if(stockOutPklist_list.size() > 0) {
						isContainLotnumber = true;
					}
					if(!isContainLotnumber) continue;
				}
				
				result.add(stockOutObj);
			}
			response.data = result;
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<StockoutObjResponse>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			e.printStackTrace();
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@RequestMapping(value = "/stockout_submaterial_list",method = RequestMethod.POST)
	public ResponseEntity<?> stockout_submaterial_list(@RequestBody StockoutListRequest entity, HttpServletRequest request ) {
		StockoutObjResponse response = new StockoutObjResponse();
		try {
//			GpayAuthentication user = (GpayAuthentication)SecurityContextHolder.getContext().getAuthentication();
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			GpayUser user_info = userDetailsService.findById(user.getId());
			if (null!= user_info && user_info.getOrgid_link()!=1){//Neu la user cap cao --> Ko han che dvi nhap
				entity.orgid_from_link = null!= user_info.getOrg_grant_id_link()?user_info.getOrg_grant_id_link():user_info.getOrgid_link();
			}
			
			List<StockOut> stockout_list = stockOutService.stockout_submaterial_list(
					user.getRootorgid_link(), 
					entity.stockouttypeid_link, 
					entity.orgid_from_link, 
					entity.orgid_to_link, 
					entity.stockoutdate_from, 
					entity.stockoutdate_to);
			
			List<StockOutObj> result = new ArrayList<StockOutObj>();
			for(StockOut stockOut : stockout_list) {
				StockOutObj stockOutObj = new StockOutObj();
				stockOutObj.setId(stockOut.getId());
				stockOutObj.setStockoutorderid_link(stockOut.getStockoutorderid_link());
				stockOutObj.setStockout_order_code(stockOut.getStockout_order_code());
				stockOutObj.setStockoutcode(stockOut.getStockoutcode());
				stockOutObj.setStockoutdate(stockOut.getStockoutdate());
				stockOutObj.setStockouttypeid_link(stockOut.getStockouttypeid_link());
				stockOutObj.setStockouttype_name(stockOut.getStockouttype_name());
				stockOutObj.setOrgid_from_link(stockOut.getOrgid_from_link());
				stockOutObj.setOrg_from_name(stockOut.getOrg_from_name());
				stockOutObj.setOrgid_to_link(stockOut.getOrgid_to_link());
				stockOutObj.setOrg_to_name(stockOut.getOrg_to_name());
				stockOutObj.setPorderid_link(stockOut.getPorderid_link());
				stockOutObj.setPordercode(stockOut.getPordercode());
				stockOutObj.setShipperson(stockOut.getShipperson());
				stockOutObj.setReason(stockOut.getReason());
				stockOutObj.setContract_number(stockOut.getContract_number());
				stockOutObj.setInvoice_number(stockOut.getInvoice_number());
				stockOutObj.setInvoice_date(stockOut.getInvoice_date());
				stockOutObj.setPcontract_poid_link(stockOut.getPcontract_poid_link());
				stockOutObj.setPcontractid_link(stockOut.getPcontractid_link());
				stockOutObj.setTotalpackage(stockOut.getTotalpackage());
				stockOutObj.setTotalpackagecheck(stockOut.getTotalpackagecheck());
				stockOutObj.setTotalyds(stockOut.getTotalyds());
				stockOutObj.setTotalydscheck(stockOut.getTotalydscheck());
				stockOutObj.setTotalm3(stockOut.getTotalm3());
				stockOutObj.setTotalnetweight(stockOut.getTotalnetweight());
				stockOutObj.setTotalgrossweight(stockOut.getTotalgrossweight());
				stockOutObj.setP_skuid_link(stockOut.getP_skuid_link());
				stockOutObj.setP_skucode(stockOut.getP_skucode());
				stockOutObj.setExtrainfo(stockOut.getExtrainfo());
				stockOutObj.setStatus(stockOut.getStatus());
				stockOutObj.setStatusString(stockOut.getStatusString());
				stockOutObj.setUsercreateid_link(stockOut.getUsercreateid_link());
				stockOutObj.setUsercreate_name(stockOut.getUsercreate_name());
				stockOutObj.setTimecreate(stockOut.getTimecreate());
				stockOutObj.setLastuserupdateid_link(stockOut.getLastuserupdateid_link());
				stockOutObj.setLasttimeupdate(stockOut.getLasttimeupdate());
				stockOutObj.setStockout_userid_link(stockOut.getStockout_userid_link());
				stockOutObj.setApprover_userid_link(stockOut.getApprover_userid_link());
				stockOutObj.setReceiver_userid_link(stockOut.getReceiver_userid_link());
				stockOutObj.setApprove_date(stockOut.getApprove_date());
				stockOutObj.setReceive_date(stockOut.getReceive_date());
				stockOutObj.setUnitid_link(stockOut.getUnitid_link());
				stockOutObj.setPorder_product_buyercode(stockOut.getPorder_product_buyercode());
				
				result.add(stockOutObj);
			}
			response.data = result;
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<StockoutObjResponse>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			e.printStackTrace();
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}	
	@RequestMapping(value = "/stockout_product_list",method = RequestMethod.POST)
	public ResponseEntity<?> stockout_product_list(@RequestBody StockoutListRequest entity, HttpServletRequest request ) {
		StockoutObjResponse response = new StockoutObjResponse();
		try {
//			GpayAuthentication user = (GpayAuthentication)SecurityContextHolder.getContext().getAuthentication();
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			GpayUser user_info = userDetailsService.findById(user.getId());
			
//			Long orgid_link = user.getOrgid_link();
			List<Long> orgid_from_linkArray = new ArrayList<Long>();
			
			if (null!= user_info && user_info.getOrgid_link() != (long)1){//Neu la user cap cao --> Ko han che dvi nhap
				entity.orgid_from_link = null!= user_info.getOrg_grant_id_link()?user_info.getOrg_grant_id_link():user_info.getOrgid_link();
				
				Org orgFrom = orgService.findOne(entity.orgid_from_link); 
				Integer orgFromType = orgFrom.getOrgtypeid_link();
				if(orgFromType.equals(OrgType.ORG_TYPE_KHOTHANHPHAM)) {
					// thêm kho vào array
					orgid_from_linkArray.add(orgFrom.getId());
				}
				if(orgFromType.equals(OrgType.ORG_TYPE_XUONGSX)) {
					// Tìm danh sách kho của xưởng sản xuất
					List<Org> khoList = orgService.findOrgByParentAndType(OrgType.ORG_TYPE_KHOTHANHPHAM, orgFrom.getId());
					for(Org kho : khoList) { // loop thêm danh sách kho vào array
						orgid_from_linkArray.add(kho.getId());
					}
				}
			}
			if (null!= user_info && user_info.getOrgid_link() == (long)1){
				Long org_grant_id_link = user_info.getOrg_grant_id_link();
				if(org_grant_id_link != null) {
					Org orgFrom = orgService.findOne(org_grant_id_link); 
					if(orgFrom.getOrgtypeid_link() == OrgType.ORG_TYPE_CUAHANG) {
						orgid_from_linkArray.add(org_grant_id_link);
					}
				}
			}
			
			// Nếu user phu trách nhiều cửa hàng
			List<GpayUserOrg> list_user_org = userOrgService.getall_byuser(user.getId());
			for (GpayUserOrg userorg : list_user_org) {
				if(!orgid_from_linkArray.contains(userorg.getOrgid_link())){
					orgid_from_linkArray.add(userorg.getOrgid_link());
				}
			}
			
			List<StockOut> stockout_list = stockOutService.stockout_product_list(
					user.getRootorgid_link(), 
					entity.stockouttypeid_link, 
//					entity.orgid_from_link,
					orgid_from_linkArray,
					entity.orgid_to_link, 
					entity.stockoutdate_from, 
					entity.stockoutdate_to);
			
			List<StockOutObj> result = new ArrayList<StockOutObj>();
			for(StockOut stockOut : stockout_list) {
				StockOutObj stockOutObj = new StockOutObj();
				stockOutObj.setId(stockOut.getId());
				stockOutObj.setStockoutorderid_link(stockOut.getStockoutorderid_link());
				stockOutObj.setStockout_order_code(stockOut.getStockout_order_code());
				stockOutObj.setStockoutcode(stockOut.getStockoutcode());
				stockOutObj.setStockoutdate(stockOut.getStockoutdate());
				stockOutObj.setStockouttypeid_link(stockOut.getStockouttypeid_link());
				stockOutObj.setStockouttype_name(stockOut.getStockouttype_name());
				stockOutObj.setOrgid_from_link(stockOut.getOrgid_from_link());
				stockOutObj.setOrg_from_name(stockOut.getOrg_from_name());
				stockOutObj.setOrgid_to_link(stockOut.getOrgid_to_link());
				stockOutObj.setOrg_to_name(stockOut.getOrg_to_name());
				stockOutObj.setPorderid_link(stockOut.getPorderid_link());
				stockOutObj.setPordercode(stockOut.getPordercode());
				stockOutObj.setShipperson(stockOut.getShipperson());
				stockOutObj.setReason(stockOut.getReason());
				stockOutObj.setContract_number(stockOut.getContract_number());
				stockOutObj.setInvoice_number(stockOut.getInvoice_number());
				stockOutObj.setInvoice_date(stockOut.getInvoice_date());
				stockOutObj.setPcontract_poid_link(stockOut.getPcontract_poid_link());
				stockOutObj.setPcontractid_link(stockOut.getPcontractid_link());
				stockOutObj.setTotalpackage(stockOut.getTotalpackage());
				stockOutObj.setTotalpackagecheck(stockOut.getTotalpackagecheck());
				stockOutObj.setTotalyds(stockOut.getTotalyds());
				stockOutObj.setTotalydscheck(stockOut.getTotalydscheck());
				stockOutObj.setTotalm3(stockOut.getTotalm3());
				stockOutObj.setTotalnetweight(stockOut.getTotalnetweight());
				stockOutObj.setTotalgrossweight(stockOut.getTotalgrossweight());
				stockOutObj.setP_skuid_link(stockOut.getP_skuid_link());
				stockOutObj.setP_skucode(stockOut.getP_skucode());
				stockOutObj.setExtrainfo(stockOut.getExtrainfo());
				stockOutObj.setStatus(stockOut.getStatus());
				stockOutObj.setStatusString(stockOut.getStatusString());
				stockOutObj.setUsercreateid_link(stockOut.getUsercreateid_link());
				stockOutObj.setUsercreate_name(stockOut.getUsercreate_name());
				stockOutObj.setTimecreate(stockOut.getTimecreate());
				stockOutObj.setLastuserupdateid_link(stockOut.getLastuserupdateid_link());
				stockOutObj.setLasttimeupdate(stockOut.getLasttimeupdate());
				stockOutObj.setStockout_userid_link(stockOut.getStockout_userid_link());
				stockOutObj.setApprover_userid_link(stockOut.getApprover_userid_link());
				stockOutObj.setReceiver_userid_link(stockOut.getReceiver_userid_link());
				stockOutObj.setApprove_date(stockOut.getApprove_date());
				stockOutObj.setReceive_date(stockOut.getReceive_date());
				stockOutObj.setUnitid_link(stockOut.getUnitid_link());
				stockOutObj.setPorder_product_buyercode(stockOut.getPorder_product_buyercode());
				
				// org parent name
				if(stockOutObj.getOrgid_from_link() != null) {
					Org orgFrom = orgService.findOne(stockOutObj.getOrgid_from_link());
					if(orgFrom.getParentid_link() != null) {
						Org orgParentFrom = orgService.findOne(orgFrom.getParentid_link());
						stockOutObj.setOrg_from_parent_name(orgParentFrom.getName());
					}
				}
				if(stockOutObj.getOrgid_to_link() != null) {
					Org orgTo = orgService.findOne(stockOutObj.getOrgid_to_link());
					if(orgTo.getParentid_link() != null) {
						Org orgParentTo = orgService.findOne(orgTo.getParentid_link());
						stockOutObj.setOrg_to_parent_name(orgParentTo.getName());
					}
				}
				
				
				result.add(stockOutObj);
			}
			response.data = result;
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<StockoutObjResponse>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			e.printStackTrace();
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}	
	@RequestMapping(value = "/stockout_shop_list",method = RequestMethod.POST)
	public ResponseEntity<?> stockout_shop_list(@RequestBody StockoutListRequest entity, HttpServletRequest request ) {
		StockoutResponse response = new StockoutResponse();
		try {
//			GpayAuthentication user = (GpayAuthentication)SecurityContextHolder.getContext().getAuthentication();
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			GpayUser user_info = userDetailsService.findById(user.getId());
			if (null!= user_info && user_info.getOrgid_link()!=1){//Neu la user cap cao --> Ko han che dvi nhap
				entity.orgid_from_link = null!= user_info.getOrg_grant_id_link()?user_info.getOrg_grant_id_link():user_info.getOrgid_link();
			}
			
			response.data = stockOutService.stockout_shop_list(
					user.getRootorgid_link(), 
					entity.stockouttypeid_link, 
					entity.orgid_from_link, 
					entity.orgid_to_link, 
					entity.stockoutdate_from, 
					entity.stockoutdate_to);
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<StockoutResponse>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			e.printStackTrace();
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	//Xuất Nguyen lieu + Phu lieu cho sản xuất theo đơn hàng
	@RequestMapping(value = "/stockoutd_bypcontract_and_sku",method = RequestMethod.POST)
	public ResponseEntity<StockoutList_ByInvoiceAndSku_Response> To_Production_M_StockoutD_bypcontract_and_sku(@RequestBody StockinList_ByInvoiceAndSku_Request entity, HttpServletRequest request ) {
		StockoutList_ByInvoiceAndSku_Response response = new StockoutList_ByInvoiceAndSku_Response();
		try {
			response.data = stockOutDService.to_production_m_findBy_PContractAndSku(entity.pcontractid_link, entity.pcontract_poid_link, entity.porderid_link, entity.stockid_link, entity.skuid_link);
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<StockoutList_ByInvoiceAndSku_Response>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<StockoutList_ByInvoiceAndSku_Response>(response, HttpStatus.BAD_REQUEST);
		}
	}
	@RequestMapping(value = "/stockoutd_bypcontract",method = RequestMethod.POST)
	public ResponseEntity<StockoutList_ByInvoiceAndSku_Response> To_Production_M_StockoutD_bypcontract(@RequestBody StockinList_ByInvoiceAndSku_Request entity, HttpServletRequest request ) {
		StockoutList_ByInvoiceAndSku_Response response = new StockoutList_ByInvoiceAndSku_Response();
		try {
			response.data = stockOutDService.to_production_m_findBy_PContract(entity.pcontractid_link, entity.pcontract_poid_link, entity.porderid_link, entity.stockid_link);
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<StockoutList_ByInvoiceAndSku_Response>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<StockoutList_ByInvoiceAndSku_Response>(response, HttpStatus.BAD_REQUEST);
		}
	}
	
	//Xuất thành phẩm cho khách hàng theo đơn hàng
	@RequestMapping(value = "/to_vendor_p_stockoutd_bypcontract_and_sku",method = RequestMethod.POST)
	public ResponseEntity<StockoutList_ByInvoiceAndSku_Response> To_Vendor_P_StockoutD_bypcontract_and_sku(@RequestBody StockinList_ByInvoiceAndSku_Request entity, HttpServletRequest request ) {
		StockoutList_ByInvoiceAndSku_Response response = new StockoutList_ByInvoiceAndSku_Response();
		try {
			response.data = stockOutDService.to_vendor_p_findBy_PContractAndSku(entity.pcontractid_link, entity.pcontract_poid_link, entity.porderid_link, entity.stockid_link, entity.skuid_link);
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<StockoutList_ByInvoiceAndSku_Response>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<StockoutList_ByInvoiceAndSku_Response>(response, HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value = "/to_vendor_p_stockoutd_bypcontract",method = RequestMethod.POST)
	public ResponseEntity<StockoutList_ByInvoiceAndSku_Response> To_Vendor_P_StockoutD_bypcontract(@RequestBody StockinList_ByInvoiceAndSku_Request entity, HttpServletRequest request ) {
		StockoutList_ByInvoiceAndSku_Response response = new StockoutList_ByInvoiceAndSku_Response();
		try {
			response.data = stockOutDService.to_vendor_p_findBy_PContract(entity.pcontractid_link, entity.pcontract_poid_link, entity.porderid_link, entity.stockid_link);
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<StockoutList_ByInvoiceAndSku_Response>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<StockoutList_ByInvoiceAndSku_Response>(response, HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value = "/save_loai_thanh_pham",method = RequestMethod.POST)
	public ResponseEntity<StockoutList_ByInvoiceAndSku_Response> save_loai_thanh_pham(@RequestBody StockoutCreateRequest entity, HttpServletRequest request ) {
		StockoutList_ByInvoiceAndSku_Response response = new StockoutList_ByInvoiceAndSku_Response();
		try {
			Integer TPGroupStoreValue = entity.TPGroupStoreValue;
			Long stockoutdid_link = entity.stockoutdid_link;
			List<StockOutPklist> stockOutPklist_list = stockOutPklistService.getBystockOutDId(stockoutdid_link);
			for(StockOutPklist stockOutPklist : stockOutPklist_list) {
				stockOutPklist.setStatus(TPGroupStoreValue);
				stockOutPklistService.save(stockOutPklist);
			}
			
//			System.out.println(TPGroupStoreValue);
//			System.out.println(stockoutdid_link);
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<StockoutList_ByInvoiceAndSku_Response>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<StockoutList_ByInvoiceAndSku_Response>(response, HttpStatus.BAD_REQUEST);
		}
	}
	
	public StockOut checkEpcInStock(StockOut stockout) {
			Long orgid_from_link = stockout.getOrgid_from_link();
			List<StockOutD> stockOutD_list = stockout.getStockout_d();
			if(stockOutD_list != null && stockOutD_list.size() > 0) {
				for(StockOutD stockOutD : stockOutD_list) {
					List<StockOutPklist> stockOutPklist_list = stockOutD.getStockout_packinglist();
					if(stockOutPklist_list != null && stockOutPklist_list.size() > 0) {
						for(StockOutPklist item_epc : stockOutPklist_list) {
							String epc = item_epc.getEpc();
							List<Warehouse> warehouse_list = warehouseService.findMaterialByEPCAndStock(epc, orgid_from_link);
							if(warehouse_list.size() > 0) {
								// sp ở trong kho -> ok
								item_epc.setStatus(StockoutStatus.STOCKOUT_EPC_STATUS_OK);
							}else {
								// sp không trong kho -> lỗi
								item_epc.setStatus(StockoutStatus.STOCKOUT_EPC_STATUS_ERR_WAREHOUSENOTEXIST);
								stockOutD.setIsPklistNotInStore(true);
							}
						}
					}
				}
			}
			return stockout;
	}
	
	public Boolean isEpcNotInStock(StockOut stockout) {
		Boolean result = false;
		List<StockOutD> stockOutD_list = stockout.getStockout_d();
		if(stockOutD_list != null && stockOutD_list.size() > 0) {
			for(StockOutD stockOutD : stockOutD_list) {
				List<StockOutPklist> stockOutPklist_list = stockOutD.getStockout_packinglist();
				if(stockOutPklist_list != null  && stockOutPklist_list.size() > 0) {
					for(StockOutPklist stockOutPklist : stockOutPklist_list) {
						if(stockOutPklist.getStatus().equals(StockoutStatus.STOCKOUT_EPC_STATUS_ERR_WAREHOUSENOTEXIST)) {
							return true;
						}
					}
				}
			}
		}
		return result;
	}
	
	public void checkStockoutOrder_Complete(StockOut stockout_new) {
		try {
			if(stockout_new.getStockouttypeid_link().equals(StockoutType.STOCKOUT_TYPE_NL_CUTHOUSE)) {
				Boolean isComplete = true;
				Long stockoutorderid_link = stockout_new.getStockoutorderid_link();
				if(stockoutorderid_link != null) {
					Stockout_order stockoutOrder = stockOutOrderService.findOne(stockoutorderid_link);
					// Tìm danh sách các phiếu xuất đã được duyệt xuất của yêu cầu xuất
					List<StockOut> stockout_list = stockOutService.findBy_StockoutOrder_approved(stockoutorderid_link);
					// chú ý: Transactional nên danh sách StockOut chưa chứa stockout vừa duyệt xuất, khi tính tổng phải + thêm
					
					// danh sách vải của ycx
					List<Stockout_order_d> stockout_order_d_list = stockoutOrder.getStockout_order_d();
					for(Stockout_order_d stockout_order_d : stockout_order_d_list) {
						Float totalmet = stockout_order_d.getTotalmet() == null ? (float) 0 : stockout_order_d.getTotalmet(); // so met yc
						Long skuid_link = stockout_order_d.getMaterial_skuid_link(); // sku id
						
						Float metApproved = (float)0;
						// cac phieu da duyet
						for(StockOut stockOut : stockout_list) {
							List<StockOutD> stockOutD_list = stockOut.getStockout_d();
							for(StockOutD stockOutD : stockOutD_list) {
								Long stockOutD_sku = stockOutD.getSkuid_link();
								if(stockOutD_sku.equals(skuid_link)) {
									Float totalmet_check = stockOutD.getTotalmet_check() == null ? (float) 0 : stockOutD.getTotalmet_check();
									metApproved += totalmet_check;
								}
							}
						}
						// phieu vua duyet
						List<StockOutD> stockOutD_list = stockout_new.getStockout_d();
						for(StockOutD stockOutD : stockOutD_list) {
							Long stockOutD_sku = stockOutD.getSkuid_link();
							if(stockOutD_sku.equals(skuid_link)) {
								Float totalmet_check = stockOutD.getTotalmet_check() == null ? (float) 0 : stockOutD.getTotalmet_check();
								metApproved += totalmet_check;
							}
						}
						// so sanh so met yeu cau xuat va so met da duyet nhap cua sku nay
						if(totalmet > metApproved) {
							isComplete = false;
							break;
						}
					}
					
					// duyet nhap du so vai ycx
					if(isComplete) {
						stockoutOrder.setStatus(StockoutOrderStatus.STOCKOUT_ORDER_STATUS_OK);
						System.out.println("STOCKOUT_ORDER_STATUS_OK");
					}else {
						stockoutOrder.setStatus(StockoutOrderStatus.STOCKOUT_ORDER_STATUS_ERR);
						System.out.println("STOCKOUT_ORDER_STATUS_ERR");
					}
					stockOutOrderService.save(stockoutOrder);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
//	@RequestMapping(value = "/p_stockoutd_bypcontract_and_sku",method = RequestMethod.POST)
//	public ResponseEntity<StockoutList_ByInvoiceAndSku_Response> Product_StockoutD_bypcontract_and_sku(@RequestBody StockinList_ByInvoiceAndSku_Request entity, HttpServletRequest request ) {
//		StockoutList_ByInvoiceAndSku_Response response = new StockoutList_ByInvoiceAndSku_Response();
//		try {
//			response.data = stockOutDService.p_findBy_PContractAndSku(entity.pcontractid_link, entity.pcontract_poid_link, entity.porderid_link, entity.stockid_link, entity.skuid_link);
//			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
//			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
//			return new ResponseEntity<StockoutList_ByInvoiceAndSku_Response>(response,HttpStatus.OK);
//		}catch (RuntimeException e) {
//			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
//			response.setMessage(e.getMessage());
//		    return new ResponseEntity<StockoutList_ByInvoiceAndSku_Response>(response, HttpStatus.BAD_REQUEST);
//		}
//	}
	
	private String genUUID(){
		UUID uuid = UUID.randomUUID();
        return uuid.toString();
	}
	
	private Integer getSlCanXuatTheoYC(Long stockoutorderid_link, Long skuid_link) {
//		System.out.println(stockoutorderid_link);
//		System.out.println(skuid_link);
		Integer slCanXuat = 0;
		Integer slCanXuatTong = 0;
		Integer slDaXuat = 0;
		
		// tim sl can xuat tong 
		Stockout_order stockoutOrder = stockOutOrderService.findOne(stockoutorderid_link);
		List<Stockout_order_d> stockoutOrderD_list = stockoutOrder.getStockout_order_d();
		for(Stockout_order_d stockout_order_d : stockoutOrderD_list) {
			Long skuId = stockout_order_d.getMaterial_skuid_link() == null ? stockout_order_d.getP_skuid_link() : stockout_order_d.getMaterial_skuid_link();
			if(skuid_link.equals(skuId)) {
				slCanXuatTong = stockout_order_d.getTotalpackage() == null ? 0 : stockout_order_d.getTotalpackage();
				break;
			}
		}
		
		// tim sl da xuat theo yeu cau
		
		List<StockOutD> stockoutDList = stockOutDService.getBy_StockoutOrder_and_SkuId(
				stockoutorderid_link, skuid_link, StockoutStatus.STOCKOUT_STATUS_APPROVED, null);
		for(StockOutD stockOutD : stockoutDList) {
			Integer totalpackagecheck = stockOutD.getTotalpackagecheck() == null ? 0 : stockOutD.getTotalpackagecheck();
			slDaXuat += totalpackagecheck;
		}
		slCanXuat = slCanXuatTong - slDaXuat;
		return slCanXuat;
	}
}
