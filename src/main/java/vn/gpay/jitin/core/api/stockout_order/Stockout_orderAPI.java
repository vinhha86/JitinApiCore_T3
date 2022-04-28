package vn.gpay.jitin.core.api.stockout_order;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import vn.gpay.jitin.core.security.GpayUser;
import vn.gpay.jitin.core.sku.ISKU_Service;
import vn.gpay.jitin.core.sku.SKU;
import vn.gpay.jitin.core.stock.IStockspaceService;
import vn.gpay.jitin.core.stock.Stockspace;
import vn.gpay.jitin.core.stockout.IStockOutService;
import vn.gpay.jitin.core.stockout.StockOut;
import vn.gpay.jitin.core.stockout.StockOutD;
import vn.gpay.jitin.core.stockout.StockOutPklist;
import vn.gpay.jitin.core.warehouse.IWarehouseService;
import vn.gpay.jitin.core.warehouse.Warehouse;
import vn.gpay.jitin.core.warehouse_check.IWarehouseCheckService;
import vn.gpay.jitin.core.warehouse_check.WarehouseCheck;
import vn.gpay.jitin.core.api.stockin.StockinList_ByInvoiceAndSku_Request;
import vn.gpay.jitin.core.base.ResponseError;
import vn.gpay.jitin.core.org.IOrgService;
import vn.gpay.jitin.core.org.Org;
import vn.gpay.jitin.core.stockout_order.IStockout_order_d_service;
import vn.gpay.jitin.core.stockout_order.IStockout_order_service;
import vn.gpay.jitin.core.stockout_order.Stockout_order;
import vn.gpay.jitin.core.stockout_order.Stockout_orderObj;
import vn.gpay.jitin.core.stockout_order.Stockout_order_d;
import vn.gpay.jitin.core.stockout_order.Stockout_order_pkl;
import vn.gpay.jitin.core.utils.OrgType;
import vn.gpay.jitin.core.utils.ResponseMessage;
import vn.gpay.jitin.core.utils.StockoutTypes;
import vn.gpay.jitin.core.utils.WareHouseStatus;

@RestController
@RequestMapping("/api/v1/stockoutorder")
public class Stockout_orderAPI {
	@Autowired IStockout_order_service stockout_order_Service;
	@Autowired IStockout_order_d_service stockout_order_d_Service;
	@Autowired IOrgService orgService;
	@Autowired IWarehouseService warehouseService;
	@Autowired IStockspaceService stockspaceService;
	@Autowired IStockOutService stockoutService;
	@Autowired IWarehouseCheckService warehouseCheckService;
	@Autowired ISKU_Service skuService;
	
	@RequestMapping(value = "/getStockoutorder", method = RequestMethod.POST)
	public ResponseEntity<Stockout_orderObj_response> getStockoutorder(HttpServletRequest request,
			@RequestBody Stockout_order_getBySearch_request entity) {
		Stockout_orderObj_response response = new Stockout_orderObj_response();
		try {
//			GpayAuthentication user = (GpayAuthentication)SecurityContextHolder.getContext().getAuthentication();
			GpayUser user = (GpayUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Long org_grant_id_link = user.getOrg_grant_id_link();
			
			List<Long> listStockoutOrderId = new ArrayList<Long>();
			// nếu là user của kho vải
			if(org_grant_id_link != null) {
				Org orgGrant = orgService.findOne(org_grant_id_link);
				if(orgGrant.getOrgtypeid_link() == OrgType.ORG_TYPE_KHONGUYENLIEU) {
					// Tìm danh sách id stockout_order xuất từ kho vải đấy
					listStockoutOrderId = stockout_order_Service.getStockoutOrderByOrgFromId(org_grant_id_link);
					if(listStockoutOrderId.size() == 0) {
						// Danh sách id stockout_order = 0 -> ko có stockout_order thoả mãn -> trả về rỗng
						response.data = new ArrayList<Stockout_orderObj>();
						response.totalCount = 0;
						response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
						response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
						return new ResponseEntity<Stockout_orderObj_response>(response,HttpStatus.OK);
					}
				}
			}else {
				listStockoutOrderId = null;
			}
			
			Date stockoutorderdate_from = entity.stockoutorderdate_from;
			Date stockoutorderdate_to = entity.stockoutorderdate_to;
			Integer status = entity.status;
			Integer stockouttypeid_link = entity.stockouttypeid_link;
			if (entity.page == 0) entity.page = 1;
			if (entity.limit == 0) entity.limit = 500;
			
			Page<Stockout_order> stockout_order_page = stockout_order_Service.findBySearch(
					stockoutorderdate_from, stockoutorderdate_to, entity.page, entity.limit, status, listStockoutOrderId, stockouttypeid_link);
			List<Stockout_order> stockout_order_list = stockout_order_page.getContent();
			
			List<Stockout_orderObj> result = new ArrayList<>();
			for(Stockout_order stockout_order : stockout_order_list) {
				Stockout_orderObj stockout_orderObj = new Stockout_orderObj();
				stockout_orderObj.setId(stockout_order.getId());
				stockout_orderObj.setOrdercode(stockout_order.getOrdercode());
				stockout_orderObj.setOrderdate(stockout_order.getOrderdate());
				stockout_orderObj.setStockouttypeid_link(stockout_order.getStockouttypeid_link());
				stockout_orderObj.setOrgid_from_link(stockout_order.getOrgid_from_link());
				stockout_orderObj.setOrg_from_name(stockout_order.getOrg_from_name());
				stockout_orderObj.setOrgid_to_link(stockout_order.getOrgid_to_link());
				stockout_orderObj.setOrg_to_name(stockout_order.getOrg_to_name());
				stockout_orderObj.setOrderperson(stockout_order.getOrderperson());
				stockout_orderObj.setTotalpackage(stockout_order.getTotalpackage());
				stockout_orderObj.setTotalm3(stockout_order.getTotalm3());
				stockout_orderObj.setTotalnetweight(stockout_order.getTotalnetweight());
				stockout_orderObj.setTotalgrossweight(stockout_order.getTotalgrossweight());
				stockout_orderObj.setP_skuid_link(stockout_order.getP_skuid_link());
				stockout_orderObj.setExtrainfo(stockout_order.getExtrainfo());
				stockout_orderObj.setStatus(stockout_order.getStatus());
				stockout_orderObj.setUsercreateid_link(stockout_order.getUsercreateid_link());
				stockout_orderObj.setTimecreate(stockout_order.getTimecreate());
				stockout_orderObj.setLastuserupdateid_link(stockout_order.getLastuserupdateid_link());
				stockout_orderObj.setLasttimeupdate(stockout_order.getLasttimeupdate());
				stockout_orderObj.setPorderid_link(stockout_order.getPorderid_link());
				stockout_orderObj.setStockout_order_code(stockout_order.getStockout_order_code());
				stockout_orderObj.setUnitid_link(stockout_order.getUnitid_link());
				stockout_orderObj.setPcontractid_link(stockout_order.getPcontractid_link());
				stockout_orderObj.setPcontract_poid_link(stockout_order.getPcontract_poid_link());
				stockout_orderObj.setPorder_product_buyercode(stockout_order.getPorder_product_buyercode());
				stockout_orderObj.setPorder_code(stockout_order.getPorder_code());
				stockout_orderObj.setDate_to_vai_yc(stockout_order.getDate_to_vai_yc());
				stockout_orderObj.setDate_xuat_yc(stockout_order.getDate_xuat_yc());
				stockout_orderObj.setPorder_grantid_link(stockout_order.getPorder_grantid_link());
				
				result.add(stockout_orderObj);
			}
			
			response.data = result;
			response.totalCount = stockout_order_page.getTotalElements();
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		}
		return new ResponseEntity<Stockout_orderObj_response>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/getStockoutorder_KeHoachSanXuat", method = RequestMethod.POST)
	public ResponseEntity<Stockout_orderObj_response> getStockoutorder_KeHoachSanXuat(HttpServletRequest request,
			@RequestBody Stockout_order_getBySearch_request entity) {
		Stockout_orderObj_response response = new Stockout_orderObj_response();
		try {
//			GpayAuthentication user = (GpayAuthentication)SecurityContextHolder.getContext().getAuthentication();
//			GpayUser user = (GpayUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//			Long org_grant_id_link = user.getOrg_grant_id_link();
			
			Date stockoutorderdate_from = entity.stockoutorderdate_from;
			Date stockoutorderdate_to = entity.stockoutorderdate_to;
			Integer status = entity.status;
			Integer stockouttypeid_link = entity.stockouttypeid_link;
			Long porder_grantid_link = entity.porder_grantid_link;
			if (entity.page == 0) entity.page = 1;
			if (entity.limit == 0) entity.limit = 500;
			
			Page<Stockout_order> stockout_order_page = stockout_order_Service.findBySearch_KeHoachSanXuat(
					stockoutorderdate_from, stockoutorderdate_to, entity.page, entity.limit, status, null, stockouttypeid_link, porder_grantid_link);
			List<Stockout_order> stockout_order_list = stockout_order_page.getContent();
			
			List<Stockout_orderObj> result = new ArrayList<>();
			for(Stockout_order stockout_order : stockout_order_list) {
				Stockout_orderObj stockout_orderObj = new Stockout_orderObj();
				stockout_orderObj.setId(stockout_order.getId());
				stockout_orderObj.setOrdercode(stockout_order.getOrdercode());
				stockout_orderObj.setOrderdate(stockout_order.getOrderdate());
				stockout_orderObj.setStockouttypeid_link(stockout_order.getStockouttypeid_link());
				stockout_orderObj.setOrgid_from_link(stockout_order.getOrgid_from_link());
				stockout_orderObj.setOrg_from_name(stockout_order.getOrg_from_name());
				stockout_orderObj.setOrgid_to_link(stockout_order.getOrgid_to_link());
				stockout_orderObj.setOrg_to_name(stockout_order.getOrg_to_name());
				stockout_orderObj.setOrderperson(stockout_order.getOrderperson());
				stockout_orderObj.setTotalpackage(stockout_order.getTotalpackage());
				stockout_orderObj.setTotalm3(stockout_order.getTotalm3());
				stockout_orderObj.setTotalnetweight(stockout_order.getTotalnetweight());
				stockout_orderObj.setTotalgrossweight(stockout_order.getTotalgrossweight());
				stockout_orderObj.setP_skuid_link(stockout_order.getP_skuid_link());
				stockout_orderObj.setExtrainfo(stockout_order.getExtrainfo());
				stockout_orderObj.setStatus(stockout_order.getStatus());
				stockout_orderObj.setUsercreateid_link(stockout_order.getUsercreateid_link());
				stockout_orderObj.setTimecreate(stockout_order.getTimecreate());
				stockout_orderObj.setLastuserupdateid_link(stockout_order.getLastuserupdateid_link());
				stockout_orderObj.setLasttimeupdate(stockout_order.getLasttimeupdate());
				stockout_orderObj.setPorderid_link(stockout_order.getPorderid_link());
				stockout_orderObj.setStockout_order_code(stockout_order.getStockout_order_code());
				stockout_orderObj.setUnitid_link(stockout_order.getUnitid_link());
				stockout_orderObj.setPcontractid_link(stockout_order.getPcontractid_link());
				stockout_orderObj.setPcontract_poid_link(stockout_order.getPcontract_poid_link());
				stockout_orderObj.setPorder_product_buyercode(stockout_order.getPorder_product_buyercode());
				stockout_orderObj.setPorder_code(stockout_order.getPorder_code());
				stockout_orderObj.setDate_to_vai_yc(stockout_order.getDate_to_vai_yc());
				stockout_orderObj.setDate_xuat_yc(stockout_order.getDate_xuat_yc());
				stockout_orderObj.setPorder_grantid_link(stockout_order.getPorder_grantid_link());
				stockout_orderObj.setPorder_Product_id(stockout_order.getPorder_Product_id());
				
				//
				List<Stockout_order_d> stockout_order_d_list = stockout_order.getStockout_order_d();
				for(Stockout_order_d stockout_order_d : stockout_order_d_list) {
					stockout_orderObj.setSkuid_link(stockout_order_d.getMaterial_skuid_link());
					stockout_orderObj.setSkuCode(stockout_order_d.getSkucode());
					stockout_orderObj.setSkuName(stockout_order_d.getSkuname());
					stockout_orderObj.setSkuDescription(stockout_order_d.getSku_product_desc());
					stockout_orderObj.setSkuColor(stockout_order_d.getSku_product_color());
					stockout_orderObj.setSkuSize(stockout_order_d.getSize_name());
					
					List<Stockout_order_pkl> stockout_order_pkl_list = stockout_order_d.getStockout_order_pkl();
					
					// yeu cau
					if(stockout_order_pkl_list.size() == 0) {
						stockout_orderObj.setCayYc(0);
//						stockout_orderObj.setMetYc(stockout_order_d.getTotalmet());
					}else {
						Integer cayYc = 0;
						Float metYc = (float) 0;
						if(stockout_order_pkl_list.size() > 0) {
							for(Stockout_order_pkl stockout_order_pkl : stockout_order_pkl_list) {
								cayYc++;
								metYc += stockout_order_pkl.getMetorigin() == null ? 0 : stockout_order_pkl.getMetorigin();
							}
						}else {
							cayYc = stockout_order_d.getTotalpackage() == null ? 0 : stockout_order_d.getTotalpackage();
							metYc = stockout_order_d.getTotalmet() == null ? (float) 0 : stockout_order_d.getTotalmet();
						}
						stockout_orderObj.setCayYc(cayYc);
//						stockout_orderObj.setMetYc(metYc);
					}
					stockout_orderObj.setMetYc(stockout_order_d.getTotalmet());
					// da to
					Integer cayTo = 0;
					Float metTo = (float) 0;
					
					List<Stockout_order_pkl> pkl_list = stockout_order_d.getStockout_order_pkl();
					for(Stockout_order_pkl pkl : pkl_list) {
						String epc = pkl.getEpc();
						List<Warehouse> warehouse_list = warehouseService.findMaterialByEPC(epc);
						if(warehouse_list.size() > 0) {
							Warehouse warehouse = warehouse_list.get(0);
							if(warehouse.getStatus() > WareHouseStatus.WAREHOUSE_STATUS_UNCHECKED) {
								cayTo++;
								metTo+=warehouse.getMet();
							}
						}
					}
					
					stockout_orderObj.setCayTo(cayTo);
					stockout_orderObj.setMetTo(metTo);
					
					
					// da xuat
					Integer cayXuat = 0;
					Float metXuat = (float) 0;
					
					List<StockOut> stockout_list_byLenhCapVai = stockoutService.findBy_StockoutOrder_DaXuat(stockout_orderObj.getId());
					for(StockOut stockOut : stockout_list_byLenhCapVai) {
						List<StockOutD> stockOutD_list = stockOut.getStockout_d();
						for(StockOutD stockOutD : stockOutD_list) {
							List<StockOutPklist> stockOutPklist_list = stockOutD.getStockout_packinglist();
							for(StockOutPklist stockOutPklist : stockOutPklist_list) {
								String epc = stockOutPklist.getEpc();
								List<Warehouse> warehouse_list = warehouseService.findMaterialByEPC(epc);
								if(warehouse_list.size() > 0) {
									Warehouse warehouse = warehouse_list.get(0);
									if(warehouse.getStatus() > WareHouseStatus.WAREHOUSE_STATUS_UNCHECKED) {
										cayXuat++;
										metXuat+=warehouse.getMet();
									}
								}
							}
						}
					}
					
					stockout_orderObj.setCayXuat(cayXuat);
					stockout_orderObj.setMetXuat(metXuat);
				}
				
				result.add(stockout_orderObj);
			}
			
			response.data = result;
			response.totalCount = stockout_order_page.getTotalElements();
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		}
		return new ResponseEntity<Stockout_orderObj_response>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/stockoutorder_getbyid",method = RequestMethod.POST)
	public ResponseEntity<?> stockoutorder_getbyid(@RequestBody Stockout_orderByIDRequest entity, HttpServletRequest request ) {
		Stockout_orderByIDResponse response = new Stockout_orderByIDResponse();
		try {
			Stockout_order result = stockout_order_Service.findOne(entity.id);
			if(result.getStockout_order_d() != null) {
				Comparator<Stockout_order_d> compareBySkucode = (Stockout_order_d a1, Stockout_order_d a2) -> a1.getSkucode().compareTo( a2.getSkucode());
				Collections.sort(result.getStockout_order_d(), compareBySkucode);
			}
			
			response.data = result;
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<Stockout_orderByIDResponse>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/stockout_orderd_bypcontract_and_sku",method = RequestMethod.POST)
	public ResponseEntity<Stockout_order_d_getByStockoutOrderId_response> Stockout_OrderD_bypcontract_and_sku(@RequestBody StockinList_ByInvoiceAndSku_Request entity, HttpServletRequest request ) {
		Stockout_order_d_getByStockoutOrderId_response response = new Stockout_order_d_getByStockoutOrderId_response();
		try {
			response.data = stockout_order_d_Service.findBy_PContractAndSku(entity.pcontractid_link, entity.pcontract_poid_link, entity.porderid_link, entity.stockid_link, entity.skuid_link);
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<Stockout_order_d_getByStockoutOrderId_response>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<Stockout_order_d_getByStockoutOrderId_response>(response, HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value = "/getById", method = RequestMethod.POST)
	public ResponseEntity<Stockout_orderByIDResponse> getById(HttpServletRequest request,
			@RequestBody Stockout_orderByIDRequest entity) {
		Stockout_orderByIDResponse response = new Stockout_orderByIDResponse();
		try {
			Stockout_order stockout_order = stockout_order_Service.findOne(entity.id);
			
			if( // yeu cau xuat thanh pham
				stockout_order.getStockouttypeid_link() >= StockoutTypes.STOCKOUT_TYPE_TP_PO &&
				stockout_order.getStockouttypeid_link() < StockoutTypes.STOCKOUT_TYPE_CH_MOVE
			) {
				List<Stockout_order_d> stockout_order_d_list = stockout_order.getStockout_order_d();
				for(Stockout_order_d stockout_order_d : stockout_order_d_list) {
					// skucode, sku_product_code, skuname, color_name, size_name, loaiThanhPham, totalSLTon
					Long skuId = stockout_order_d.getP_skuid_link();
					SKU productSKU = skuService.findOne(skuId);
					stockout_order_d.setSkucode_product(productSKU.getCode());
					stockout_order_d.setSku_product_code(productSKU.getProduct_code());
					stockout_order_d.setSkuname_product(productSKU.getName());
					stockout_order_d.setColor_name_product(productSKU.getColor_name());
					stockout_order_d.setSize_name_product(productSKU.getSize_name());
				}
			}else { 
				// set gia tri khoang
				List<Stockout_order_d> stockout_order_d_list = stockout_order.getStockout_order_d();
				for(Stockout_order_d stockout_order_d : stockout_order_d_list) {
					List<Stockout_order_pkl> stockout_order_pkl_list = stockout_order_d.getStockout_order_pkl();
					for(Stockout_order_pkl stockout_order_pkl : stockout_order_pkl_list) {
						String epc = stockout_order_pkl.getEpc();
						List<Warehouse> warehouse_list = warehouseService.findMaterialByEPC(epc);
						if(warehouse_list.size() > 0) {
							Warehouse warehouse = warehouse_list.get(0);
							if(warehouse.getStatus() > WareHouseStatus.WAREHOUSE_STATUS_UNCHECKED) {
								// đã tở -> lấy ngày tở
	//							List<WarehouseCheck> warehouseCheck_list = warehouseCheckService.findBy_Warehouse_StockoutOrder(stockout_order.getId(), warehouse.getId());
								List<WarehouseCheck> warehouseCheck_list = warehouseCheckService.findBy_Warehouse_StockoutOrder(null, warehouse.getId());
								if(warehouseCheck_list.size() > 0) {
									WarehouseCheck warehouseCheck = warehouseCheck_list.get(0);
									Date date_check = warehouseCheck.getDate_check();
									stockout_order_pkl.setDate_check(date_check);
								}
							}
							
							if(warehouse.getSpaceepc_link() == null) {
								warehouse.setSpaceString("KXD");
								stockout_order_pkl.setSpaceString("KXD");
							}else {
								String spaceepc_link = warehouse.getSpaceepc_link();
								List<Stockspace> stockspace_list = stockspaceService.findStockspaceByEpc(spaceepc_link);
								if(stockspace_list.size() > 0) {
									Stockspace stockspace = stockspace_list.get(0);
									String spaceString = "" + stockspace.getStockrow_code() + "-" + stockspace.getSpacename() + "-" + stockspace.getFloorid();
									warehouse.setSpaceString(spaceString);
									stockout_order_pkl.setSpaceString(spaceString);
								}else {
									warehouse.setSpaceString("KXD");
									stockout_order_pkl.setSpaceString("KXD");
								}
							}
							stockout_order_pkl.setWarehouseStatus(warehouse.getStatus());
							if(warehouse.getStatus() == WareHouseStatus.WAREHOUSE_STATUS_UNCHECKED) {
								stockout_order_pkl.setWarehouseStatusString("Chưa tở");
							}else
							if(warehouse.getStatus() == WareHouseStatus.WAREHOUSE_STATUS_CHECKED) {
								stockout_order_pkl.setWarehouseStatusString("Đã tở");
							}else
							if(warehouse.getStatus() == WareHouseStatus.WAREHOUSE_STATUS_CUT) {
								stockout_order_pkl.setWarehouseStatusString("Đã cắt");
							}
							
							if(warehouse.getStockid_link().equals(stockout_order.getOrgid_from_link())) {
								stockout_order_pkl.setIsInStock(true);
							}else {
								stockout_order_pkl.setIsInStock(false);
							}
						}
						
					}
				}
			}
			
			response.data = stockout_order;
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		}
		return new ResponseEntity<Stockout_orderByIDResponse>(response, HttpStatus.OK);
	}
}
