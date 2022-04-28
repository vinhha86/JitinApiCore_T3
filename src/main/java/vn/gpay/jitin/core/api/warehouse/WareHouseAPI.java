package vn.gpay.jitin.core.api.warehouse;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import vn.gpay.jitin.core.api.stockout.StockOutD_response;
import vn.gpay.jitin.core.base.ResponseBase;
import vn.gpay.jitin.core.base.ResponseError;
import vn.gpay.jitin.core.org.IOrgService;
import vn.gpay.jitin.core.org.Org;
import vn.gpay.jitin.core.packinglist.IPackingListService;
import vn.gpay.jitin.core.packinglist.PackingList;
import vn.gpay.jitin.core.pcontract.IPContractService;
import vn.gpay.jitin.core.pcontract.PContract;
import vn.gpay.jitin.core.pcontractproduct.IPContractProductService;
import vn.gpay.jitin.core.pcontractproduct.PContractProduct;
import vn.gpay.jitin.core.porder.IPOrderService;
import vn.gpay.jitin.core.porder_grant.IPOrderGrant_Service;
import vn.gpay.jitin.core.porder_grant.POrder_Grant;
import vn.gpay.jitin.core.security.GpayAuthentication;
import vn.gpay.jitin.core.security.GpayUser;
import vn.gpay.jitin.core.security.IGpayUserOrgService;
import vn.gpay.jitin.core.security.IGpayUserService;
import vn.gpay.jitin.core.stock.IStockspaceService;
import vn.gpay.jitin.core.stock.Stockspace;
import vn.gpay.jitin.core.stockin.IStockInService;
import vn.gpay.jitin.core.stockout.IStockOutDService;
import vn.gpay.jitin.core.stockout.IStockOutPklistService;
import vn.gpay.jitin.core.stockout.IStockOutService;
import vn.gpay.jitin.core.stockout.StockOut;
import vn.gpay.jitin.core.stockout.StockOutD;
import vn.gpay.jitin.core.stockout.StockOutPklist;
import vn.gpay.jitin.core.stockout_order.IStockout_order_d_service;
import vn.gpay.jitin.core.stockout_order.IStockout_order_pkl_Service;
import vn.gpay.jitin.core.stockout_order.IStockout_order_service;
import vn.gpay.jitin.core.stockout_order.Stockout_order;
import vn.gpay.jitin.core.stockout_order.Stockout_order_d;
import vn.gpay.jitin.core.stockout_order.Stockout_order_pkl;
import vn.gpay.jitin.core.utils.OrgType;
import vn.gpay.jitin.core.utils.ResponseMessage;
import vn.gpay.jitin.core.utils.StockoutStatus;
import vn.gpay.jitin.core.utils.WareHouseStatus;
import vn.gpay.jitin.core.warehouse.IEpcWarehouseCheckService;
import vn.gpay.jitin.core.warehouse.IWarehouseService;
import vn.gpay.jitin.core.warehouse.Warehouse;
import vn.gpay.jitin.core.warehouse_check.IWarehouseCheckService;
import vn.gpay.jitin.core.warehouse_check.WarehouseCheck;

@RestController
@RequestMapping("/api/v1/warehouse")
public class WareHouseAPI {

	@Autowired IPackingListService packingListService;
	@Autowired IWarehouseService warehouseService;
	@Autowired IEpcWarehouseCheckService epcWarehouseCheckService;
	@Autowired IOrgService orgService;
	@Autowired IGpayUserOrgService userOrgService;
	@Autowired IPOrderService porderService;
	@Autowired IPContractService pcontractService;
	@Autowired IGpayUserService  userDetailsService ;
	@Autowired IStockout_order_service stockout_order_service;
	@Autowired IStockout_order_pkl_Service stockout_order_pkl_Service;
	@Autowired IStockout_order_d_service stockout_order_d_service;
	@Autowired IWarehouseCheckService warehouseCheckService;
	@Autowired IStockspaceService stockspaceService;
	@Autowired IStockInService stockinService;
	@Autowired IPContractProductService pcontractProductService;
	@Autowired IStockOutPklistService stockOutPklistService;
	@Autowired IStockOutDService stockOutDService;
	@Autowired IStockOutService stockOutService;
	@Autowired IPOrderGrant_Service porderGrantService;
	
	@RequestMapping(value = "/updateWarehouseEPC",method = RequestMethod.POST)
	public ResponseEntity<?> UpdateWarehouseEPC( @RequestBody PackingList entity,HttpServletRequest request ) {
		ResponseBase responseBase = new ResponseBase();
		try {
//			PackingList data = packingListService.findOne(entity.getId());
//			if(data!=null) {
//				packingListService.update(entity);
//
//				Warehouse whdata = null;
//				try {
//					whdata=	warehouseService.findOne(data.getEpc());
//				}catch(Exception ex) {}
//				if (whdata==null)
//				{
//					Warehouse newWarehouse = new Warehouse();
//				//	newWarehouse.setOrgid_link(data.getOrgid_link());
//					//newWarehouse.setStockindid_link(data.getStockindid_link());
//					//newWarehouse.setInvoiceid_link(data.getInvoiceid_link());
//					newWarehouse.setLotnumber(data.getLotnumber());
//					newWarehouse.setPackageid(data.getPackageid());
//					newWarehouse.setColorid_link(data.getColorid_link());
//					//newWarehouse.setAmount(data.getAmountorigin());
//					newWarehouse.setSkuid_link(data.getSkuid_link());
//					//newWarehouse.setWidth(data.getWidth());
//					//newWarehouse.setWeight(data.getNetweight());
//				//	newWarehouse.setEpc(data.getEpc());
//					newWarehouse.setEncryptdatetime(data.getEncryptdatetime());
//				    warehouseService.create(newWarehouse);
//				}
//				else {
//				//	whdata.setEpc(data.getEpc());
//					whdata.setEncryptdatetime(data.getEncryptdatetime());
//					warehouseService.update(whdata);
//				}
//				
//				responseBase.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
//				responseBase.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
//				return new ResponseEntity<ResponseBase>(responseBase,HttpStatus.OK);
//			}
			responseBase.setRespcode(ResponseMessage.KEY_RC_SERVER_ERROR);
			responseBase.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SERVER_ERROR));
			return new ResponseEntity<ResponseBase>(responseBase,HttpStatus.OK);
			
			
		}catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/getMaterialListBySpaceEPC",method = RequestMethod.POST)
	public ResponseEntity<?> GetMaterialListBySpaceEPC( @RequestBody GetMaterialListBySpaceEPC entity,HttpServletRequest request ) {
		WareHouseResponse responseBase = new WareHouseResponse();
		try {
			responseBase.data = warehouseService.findBySpaceepc(entity.spaceepc);
			responseBase.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			responseBase.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<WareHouseResponse>(responseBase,HttpStatus.OK);
		}catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/getMaterialListByPContract",method = RequestMethod.POST)
	public ResponseEntity<?> GetMaterialListByPContract( @RequestBody GetMaterialListByPContract entity,HttpServletRequest request ) {
		WareHouseResponse responseBase = new WareHouseResponse();
		try {
			responseBase.data = warehouseService.findByPContract(entity.pcontractid_link, entity.stockid_link, entity.skuid_link);
			responseBase.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			responseBase.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<WareHouseResponse>(responseBase,HttpStatus.OK);
		}catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/getby_org",method = RequestMethod.POST)
	public ResponseEntity<getby_org_response> GetByOrg(HttpServletRequest request, @RequestBody getby_org_request entity ) {
		getby_org_response responseBase = new getby_org_response();
		try {
//			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//			Long orgid_link = user.getOrgid_link();
//			Org org = orgService.findOne(orgid_link);
//			Long orgparentid_link = org.getParentid_link();
//			
//			List<String> orgTypes = new ArrayList<String>();
//			orgTypes.add("19");
//			orgTypes.add("3");
			
			List<Long> list_id_org = new ArrayList<Long>();
			list_id_org.add(entity.org_from_id_link);
			
//			orgid_link = orgparentid_link == null ? orgid_link : orgparentid_link;
//			List<Org> list_org = orgService.getorgChildrenbyOrg(orgid_link, orgTypes);
			
//			POrder porder = porderService.findOne(entity.porderid_link);
			Long pcontractid_link = entity.pcontractid_link;
			PContract contract = pcontractService.findOne(pcontractid_link);
			Long orgbuyerid_link = contract.getOrgbuyerid_link();
			
			switch (entity.typeFilter) {
			case 1:
				orgbuyerid_link = null;
				break;
			case 2:
				pcontractid_link = null;
				break;
			case 3:
				orgbuyerid_link = null;
				pcontractid_link = null;
				list_id_org = null;
				break;
			default:
				break;
			}
			
			responseBase.data = warehouseService.getby_org(list_id_org, entity.material_skuid_link, pcontractid_link, orgbuyerid_link, entity.stockout_orderid_link);
			responseBase.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			responseBase.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<getby_org_response>(responseBase,HttpStatus.OK);
		}catch (RuntimeException e) {
			responseBase.setRespcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			responseBase.setMessage(e.getMessage());
		    return new ResponseEntity<getby_org_response>(responseBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/getMaterialListByLotNumber",method = RequestMethod.POST)
	public ResponseEntity<?> GetMaterialListByLotNumber( @RequestBody GetMaterialListByLotNumber entity,HttpServletRequest request ) {
		WareHouseResponse responseBase = new WareHouseResponse();
//		System.out.println("lotnumber:" + entity.lotnumber);
		try {
			responseBase.data = warehouseService.findByLotNumber(entity.lotnumber);
			responseBase.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			responseBase.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<WareHouseResponse>(responseBase,HttpStatus.OK);
		}catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}	
	
	@RequestMapping(value = "/addRollToSpace",method = RequestMethod.POST)
	public ResponseEntity<?> AddRollToSpace( @RequestBody AddRollToSpace entity,HttpServletRequest request ) {
		ResponseBase responseBase = new ResponseBase();
		try {
			for(Warehouse data : entity.data) {
				Warehouse theWs_data = warehouseService.findOne(data.getId());
				if (null!=theWs_data){
					theWs_data.setSpaceepc_link(data.getSpaceepc_link());
					warehouseService.save(theWs_data);
				}
			}
			responseBase.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			responseBase.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ResponseBase>(responseBase,HttpStatus.OK);
		}catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@RequestMapping(value = "/getMaterialByEPC",method = RequestMethod.POST)
	public ResponseEntity<?> GetMaterialByEPC( @RequestBody GetMaterialByEPC entity,HttpServletRequest request ) {
		WareHouseResponse responseBase = new WareHouseResponse();
		try {
			GpayAuthentication user = (GpayAuthentication)SecurityContextHolder.getContext().getAuthentication();
			GpayUser user_info = userDetailsService.findById(user.getUserId());
			if (null!= user_info && user_info.getOrgid_link()!=1){//Neu la user cap cao --> Ko han che dvi nhap
				Long stockid_link = null!= user_info.getOrg_grant_id_link()?user_info.getOrg_grant_id_link():user_info.getOrgid_link();
				List<Warehouse> data = warehouseService.findMaterialByEPCAndStock(entity.epc, stockid_link);
				
//				System.out.println(stockid_link + "-" + entity.epc + "-" + data.size());
				
				responseBase.data = data;
				responseBase.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
				responseBase.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
				return new ResponseEntity<WareHouseResponse>(responseBase,HttpStatus.OK);
			} else {
				ResponseError errorBase = new ResponseError();
				errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
				errorBase.setMessage("Người dùng đăng nhập không hợp lệ");
			    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);				
			}
		}catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/checkEPCExisted",method = RequestMethod.POST)
	public ResponseEntity<?> CheckEPCExisted( @RequestBody GetMaterialByEPC entity,HttpServletRequest request ) {
		ResponseBase  responseBase= new ResponseBase();
		try {
			GpayAuthentication user = (GpayAuthentication)SecurityContextHolder.getContext().getAuthentication();
			
			List<Warehouse> data = warehouseService.findMaterialByEPCAndStock(entity.epc, user.getOrgId());
			if(data!=null && data.size()>0) {
//				Warehouse input= data.get(0);
//				EpcWarehouseCheck epc = new EpcWarehouseCheck();
//				epc.setEpc(input.getEpc());
//				epc.setToken(UUID.fromString(entity.token));
//				epcWarehouseCheckService.create(epc);
				responseBase.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
				responseBase.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			}
			else {
				responseBase.setRespcode(ResponseMessage.KEY_RC_RS_NOT_FOUND);
				responseBase.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_RS_NOT_FOUND));
			}

			return new ResponseEntity<ResponseBase>(responseBase,HttpStatus.OK);
		}catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/getCheckedEPC",method = RequestMethod.POST)
	public ResponseEntity<?> GetCheckedEPC( @RequestBody GetCheckedEPC entity,HttpServletRequest request ) {
		WareHouseResponse  responseBase= new WareHouseResponse();
		try {
			
			List<Warehouse> data = warehouseService.findCheckedEPC(entity.token);
			responseBase.data = data;
			responseBase.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			responseBase.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<WareHouseResponse>(responseBase,HttpStatus.OK);
		}catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/lockmaterial_cutplan",method = RequestMethod.POST)
	public ResponseEntity<lock_material_cutplan_response> LockMaterialCutPlan( @RequestBody lock_material_cutplan_request entity,HttpServletRequest request ) {
		lock_material_cutplan_response  responseBase= new lock_material_cutplan_response();
		try {
			for(Warehouse wh : entity.data) {
				wh.setCutplanrowid_link(entity.cutplanrowid_link);
				warehouseService.save(wh);
			}
			
			
			responseBase.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			responseBase.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<lock_material_cutplan_response>(responseBase,HttpStatus.OK);
		}catch (RuntimeException e) {
			responseBase.setRespcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			responseBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(responseBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/getby_cutplan",method = RequestMethod.POST)
	public ResponseEntity<getby_cutplan_response> GetByCutPlan( @RequestBody getby_cutplan_request entity,HttpServletRequest request ) {
		getby_cutplan_response  responseBase= new getby_cutplan_response();
		try {
			
			responseBase.data = warehouseService.getby_cutplan(entity.cutplanrowid_link);
			
			responseBase.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			responseBase.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<getby_cutplan_response>(responseBase,HttpStatus.OK);
		}catch (RuntimeException e) {
			responseBase.setRespcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			responseBase.setMessage(e.getMessage());
		    return new ResponseEntity<getby_cutplan_response>(responseBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/unlock_material",method = RequestMethod.POST)
	public ResponseEntity<unlock_material_response> UnLockMaterial( @RequestBody unlock_material_request entity,HttpServletRequest request ) {
		unlock_material_response  responseBase= new unlock_material_response();
		try {
			Warehouse warehouse = warehouseService.findOne(entity.id);
			warehouse.setCutplanrowid_link(null);
			warehouseService.save(warehouse);
			
			responseBase.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			responseBase.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<unlock_material_response>(responseBase,HttpStatus.OK);
		}catch (RuntimeException e) {
			responseBase.setRespcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			responseBase.setMessage(e.getMessage());
		    return new ResponseEntity<unlock_material_response>(responseBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@RequestMapping(value = "/get_spaces_bysku",method = RequestMethod.POST)
	public ResponseEntity<Get_Spaces_BySKU_Response> Get_Spaces_bySKU( @RequestBody Get_Spaces_BySKU_Request entity,HttpServletRequest request ) {
		Get_Spaces_BySKU_Response  response= new Get_Spaces_BySKU_Response();
		try {
			response.data_spaces = warehouseService.getspaces_bysku(entity.stockid_link, entity.skuid_link);
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<Get_Spaces_BySKU_Response>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			response.setRespcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<Get_Spaces_BySKU_Response>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/getByLotAndPackageId",method = RequestMethod.POST)
	public ResponseEntity<getby_org_response> getByLotAndPackageId( @RequestBody getByLotAndPackageId_request entity,HttpServletRequest request ) {
		getby_org_response  response= new getby_org_response();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Long stockid_link = user.getOrg_grant_id_link();
//			System.out.println(stockid_link);
			List<Warehouse> warehouse_list = warehouseService.getByLotAndPackageId(entity.skuid_link, entity.lotnumber, entity.packageid, stockid_link, null);
			List<Warehouse> result = new ArrayList<Warehouse>();
			// chỉ lấy warehouse nếu stockid_link != -2 (đã xuất, kho vải xả)
			for(Warehouse warehouse : warehouse_list) {
				if(!warehouse.getStockid_link().equals((long) -2)) {
					result.add(warehouse);
				}
			}
			
			if(warehouse_list.size() == 0) {
				response.setRespcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
				response.setMessage("Cây vải không tồn tại");
			    return new ResponseEntity<getby_org_response>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			if(warehouse_list.size() > 0) {
				if(result.size() == 0) {
					response.setRespcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
					response.setMessage("Cây vải đã ở kho vải xả");
				    return new ResponseEntity<getby_org_response>(response, HttpStatus.INTERNAL_SERVER_ERROR);
				}
			}

			response.data = result;
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<getby_org_response>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			response.setRespcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<getby_org_response>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/getByLotAndPackageIdForStockout",method = RequestMethod.POST)
	public ResponseEntity<getby_org_response> getByLotAndPackageIdForStockout( @RequestBody getByLotAndPackageId_request entity,HttpServletRequest request ) {
		getby_org_response  response= new getby_org_response();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Long stockid_link = user.getOrg_grant_id_link();
			List<Warehouse> warehouse_list = warehouseService.getByLotAndPackageId(entity.skuid_link, entity.lotnumber, entity.packageid, stockid_link, entity.status);
			List<Warehouse> result = new ArrayList<Warehouse>();
			// chỉ lấy warehouse nếu stockid_link != -2 (đã xuất, kho vải xả)
			for(Warehouse warehouse : warehouse_list) {
//				if(warehouse.getStockid_link().equals((long) -2)) {
//					result.add(warehouse);
//				}
				result.add(warehouse);
			}
			
			if(warehouse_list.size() == 0) {
				response.setRespcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
				response.setMessage("Cây vải không tồn tại");
			    return new ResponseEntity<getby_org_response>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
//			if(warehouse_list.size() > 0) {
//				if(result.size() == 0) {
//					response.setRespcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
//					response.setMessage("Cây vải chưa ở kho vải xả");
//				    return new ResponseEntity<getby_org_response>(response, HttpStatus.INTERNAL_SERVER_ERROR);
//				}
//			}

			response.data = result;
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<getby_org_response>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			response.setRespcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<getby_org_response>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
//	@RequestMapping(value = "/updateToVai",method = RequestMethod.POST)
////	@Transactional(rollbackFor = RuntimeException.class)
//	public ResponseEntity<getby_org_response> updateToVai( @RequestBody updateToVai_request entity,HttpServletRequest request ) {
//		getby_org_response  response= new getby_org_response();
//		try {
//			GpayAuthentication user = (GpayAuthentication)SecurityContextHolder.getContext().getAuthentication();
//			Date currentDate = new Date();
////			Warehouse warehouse = entity.warehouse;
////			Stockout_order_pkl stockoutOrderPklist = entity.stockoutOrderPklist;
//			WarehouseCheck warehouseCheckRequest = entity.warehouse_check;
//			
//			// update Warehouse
//			// - Tìm Warehouse
////			List<Warehouse> listWarehouse = warehouseService.getByLotAndPackageId(
////					warehouse.getSkuid_link(), warehouse.getLotnumber(), warehouse.getPackageid());
//			Warehouse oldWarehouse = warehouseService.findOne(warehouseCheckRequest.getWarehouseid_link()); 
////			if(listWarehouse.size() > 0) {
////				Warehouse oldWarehouse = listWarehouse.get(0);
////				// check status cay vai da to hay chua
////				// code here
//				
//				// tao warehouse_check (luu thong tin truoc va sau khi to)
//			if(warehouseCheckRequest.getId() == null) {
//				// thêm mới
//				WarehouseCheck warehouseCheck = new WarehouseCheck();
//				warehouseCheck.setId(null);
//				warehouseCheck.setStockoutorderid_link(entity.stockoutorderid_link);
//				warehouseCheck.setWarehouseid_link(oldWarehouse.getId());
//				warehouseCheck.setYds_origin(oldWarehouse.getYds());
//				warehouseCheck.setYds_check(warehouseCheckRequest.getYds_check());
//				warehouseCheck.setMet_origin(oldWarehouse.getMet());
//				warehouseCheck.setMet_check(warehouseCheckRequest.getMet_check());
//				warehouseCheck.setWidth_origin(oldWarehouse.getWidth_met());
//				warehouseCheck.setWidth_check(warehouseCheckRequest.getWidth_check());
//				warehouseCheck.setNetweight_origin(oldWarehouse.getNetweight());
//				warehouseCheck.setNetweight_check(warehouseCheckRequest.getNetweight_check());
//				warehouseCheck.setGrossweight_origin(oldWarehouse.getGrossweight());
//				warehouseCheck.setGrossweight_check(warehouseCheckRequest.getGrossweight_check());
//				warehouseCheck.setUsercheckid_link(user.getUserId());
//				warehouseCheck.setDate_check(currentDate);
//				warehouseCheck.setMet_err(warehouseCheckRequest.getMet_err());
//				warehouseCheck = warehouseCheckService.save(warehouseCheck);
//			}else {
//				// sửa
//				WarehouseCheck warehouseCheckOld = warehouseCheckService.findOne(warehouseCheckRequest.getId());
//				warehouseCheckOld.setYds_check(warehouseCheckRequest.getYds_check());
//				warehouseCheckOld.setMet_check(warehouseCheckRequest.getMet_check());
//				warehouseCheckOld.setWidth_check(warehouseCheckRequest.getWidth_check());
//				warehouseCheckOld.setUsercheckid_link(user.getUserId());
//				warehouseCheckOld.setDate_check(currentDate);
//				warehouseCheckOld.setMet_err(warehouseCheckRequest.getMet_err());
//				warehouseCheckOld = warehouseCheckService.save(warehouseCheckOld);
//			}
//				// update warehouse
//			oldWarehouse.setMet(warehouseCheckRequest.getMet_check());
//			oldWarehouse.setYds(warehouseCheckRequest.getYds_check());
//			oldWarehouse.setWidth(warehouseCheckRequest.getWidth_check());
//			oldWarehouse.setWidth_met(warehouseCheckRequest.getWidth_check());
//			oldWarehouse.setWidth_yds((float)warehouseCheckRequest.getWidth_check() / (float)0.9144);
//			oldWarehouse.setLasttimeupdate(currentDate);
//			oldWarehouse.setLastuserupdateid_link(user.getUserId());
//			oldWarehouse.setMet_err(warehouseCheckRequest.getMet_err());
//			if(oldWarehouse.getStatus().equals(WareHouseStatus.WAREHOUSE_STATUS_UNCHECKED)) {
//				oldWarehouse.setStatus(WareHouseStatus.WAREHOUSE_STATUS_CHECKED);
//			}
//			oldWarehouse = warehouseService.save(oldWarehouse);
//			
//			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
//			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
//			return new ResponseEntity<getby_org_response>(response,HttpStatus.OK);
//		}catch (RuntimeException e) {
//			response.setRespcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
//			response.setMessage(e.getMessage());
//		    return new ResponseEntity<getby_org_response>(response, HttpStatus.INTERNAL_SERVER_ERROR);
//		}
//	}
	
	@RequestMapping(value = "/updateToVai",method = RequestMethod.POST)
//	@Transactional(rollbackFor = RuntimeException.class)
	public ResponseEntity<getby_org_response> updateToVai( @RequestBody updateToVai_request entity,HttpServletRequest request ) {
		getby_org_response  response= new getby_org_response();
		try {
			GpayAuthentication user = (GpayAuthentication)SecurityContextHolder.getContext().getAuthentication();
			Date currentDate = new Date();
//			
//			Stockout_order_pkl stockout_order_pkl = entity.stockout_order_pkl;
			WarehouseCheck warehouseCheckRequest = entity.warehouse_check;
			Long stockoutorderid_link = entity.stockoutorderid_link;
			Long stockoutorderdid_link = entity.stockoutorderdid_link;
			Long warehouseid_link = warehouseCheckRequest.getWarehouseid_link();
			Warehouse warehouseObj = warehouseService.findOne(warehouseid_link);
			
//			Float warehouse_check_met_check = stockout_order_pkl.getWarehouse_check_met_check() == null ? 0 : stockout_order_pkl.getWarehouse_check_met_check();
//			Float warehouse_check_width_check = stockout_order_pkl.getWarehouse_check_width_check()  == null ? 0 : stockout_order_pkl.getWarehouse_check_width_check() / 100;
//			Float warehouse_check_met_err = stockout_order_pkl.getWarehouse_check_met_err() == null ? 0 : stockout_order_pkl.getWarehouse_check_met_err();
//			stockout_order_pkl.setWarehouse_check_width_check(warehouse_check_width_check / 100);
			
			Float met_check = warehouseCheckRequest.getMet_check() == null ? 0 : warehouseCheckRequest.getMet_check();
			Float width_check = warehouseCheckRequest.getWidth_check()  == null ? 0 : warehouseCheckRequest.getWidth_check();
			Float met_err = warehouseCheckRequest.getMet_err() == null ? 0 : warehouseCheckRequest.getMet_err();
//			warehouseCheckRequest.setWidth_check(width_check / 100);
			
//			Stockout_order_d stockout_order_d = stockout_order_d_service.findOne(stockoutorderdid_link);
			Stockout_order stockout_order = stockout_order_service.findOne(stockoutorderid_link);
			
			String epc = warehouseObj.getEpc();
			List<Warehouse> warehouse_list = warehouseService.findMaterialByEPC(epc);
			if(warehouse_list.size() > 0) {
				Warehouse warehouse = warehouse_list.get(0);
				List<WarehouseCheck> warehouseCheck_list = warehouseCheckService.findBy_Warehouse_StockoutOrder(stockout_order.getId(), warehouse.getId());
				if(warehouseCheck_list.size() == 0) {
					// new
					WarehouseCheck newWarehouseCheck = new WarehouseCheck();
					// stockoutorderid_link, warehouseid_link, yds_origin, yds_check, met_origin, met_check, width_origin, width_check, 
					// netweight_origin, netweight_check, grossweight_origin, grossweight_check, usercheckid_link, date_check, met_err
					newWarehouseCheck.setId(null);
					newWarehouseCheck.setStockoutorderid_link(stockout_order.getId());
					newWarehouseCheck.setWarehouseid_link(warehouse.getId());
					newWarehouseCheck.setMet_origin(warehouse.getMet());
					newWarehouseCheck.setMet_check(met_check);
					newWarehouseCheck.setYds_origin(warehouse.getYds());
					newWarehouseCheck.setYds_check((float) (met_check * 1.094));
					newWarehouseCheck.setWidth_origin(warehouse.getWidth_met());
					newWarehouseCheck.setWidth_check(width_check);
					newWarehouseCheck.setMet_err(met_err);
					//
					newWarehouseCheck.setUsercheckid_link(user.getUserId());
					newWarehouseCheck.setDate_check(currentDate);
					warehouseCheckService.save(newWarehouseCheck);
				}else {
					// exist
					WarehouseCheck warehouseCheck = warehouseCheck_list.get(0);
					warehouseCheck.setMet_check(met_check);
					warehouseCheck.setYds_check((float) (met_check * 1.094));
					warehouseCheck.setWidth_check(width_check);
					warehouseCheck.setMet_err(met_err);
					// 
					warehouseCheck.setUsercheckid_link(user.getUserId());
					warehouseCheck.setDate_check(currentDate);
					warehouseCheckService.save(warehouseCheck);
					
				}
				
				if(warehouse.getStatus() == WareHouseStatus.WAREHOUSE_STATUS_UNCHECKED) {
					warehouse.setStatus(WareHouseStatus.WAREHOUSE_STATUS_CHECKED);
				}
				warehouse.setMet(met_check);
				warehouse.setYds((float) (met_check * 1.094));
				warehouse.setWidth(width_check);
				warehouse.setWidth_met(width_check);
				warehouse.setWidth_yds((float) (width_check * 1.094));
				warehouseService.save(warehouse);

				// Thêm vào danh sách stockout_order_pkl
				List<Stockout_order_pkl> stockout_order_pkl_list = stockout_order_pkl_Service.getByStockoutOrderDId_epc(stockoutorderdid_link, epc);
				if(stockout_order_pkl_list.size() > 0) {
					Stockout_order_pkl stockout_order_pkl = stockout_order_pkl_list.get(0);
					stockout_order_pkl.setMetcheck(met_check);
					stockout_order_pkl.setYdscheck((float) (met_check * 1.094));
					stockout_order_pkl.setWidth_met_check(width_check);
					stockout_order_pkl.setWidth_yds_check((float) (width_check * 1.094));
					stockout_order_pkl.setLastuserupdateid_link(user.getUserId());
					stockout_order_pkl.setLasttimeupdate(currentDate);
					stockout_order_pkl_Service.save(stockout_order_pkl);
				}else {
					Stockout_order_pkl newStockout_order_pkl = new Stockout_order_pkl();
					newStockout_order_pkl.setId(null);
					newStockout_order_pkl.setStockoutorderid_link(stockoutorderid_link);
					newStockout_order_pkl.setStockoutorderdid_link(stockoutorderdid_link);
					newStockout_order_pkl.setSkuid_link(warehouse.getSkuid_link());
					newStockout_order_pkl.setColorid_link(warehouse.getColorid_link());
					newStockout_order_pkl.setLotnumber(warehouse.getLotnumber());
					newStockout_order_pkl.setPackageid(warehouse.getPackageid());
					newStockout_order_pkl.setYdsorigin(warehouse.getYds());
					newStockout_order_pkl.setMetorigin(warehouse.getMet());
					newStockout_order_pkl.setMet(warehouse.getMet());
					newStockout_order_pkl.setWidth_met(warehouse.getWidth_met());
					newStockout_order_pkl.setWidth(warehouse.getWidth_met());
					newStockout_order_pkl.setWidth_yds(warehouse.getWidth_yds());
					newStockout_order_pkl.setNetweight(warehouse.getNetweight());
					newStockout_order_pkl.setGrossweight(warehouse.getGrossweight());
					newStockout_order_pkl.setEpc(warehouse.getEpc());
					newStockout_order_pkl.setUsercreateid_link(user.getUserId());
					newStockout_order_pkl.setTimecreate(currentDate);
					newStockout_order_pkl.setLastuserupdateid_link(user.getUserId());
					newStockout_order_pkl.setLasttimeupdate(currentDate);
					newStockout_order_pkl.setSpaceepc_link(warehouse.getSpaceepc_link());
					newStockout_order_pkl.setStatus(1);
					
					newStockout_order_pkl.setMetcheck(met_check);
					newStockout_order_pkl.setYdscheck((float) (met_check * 1.094));
					newStockout_order_pkl.setWidth_met_check(width_check);
					newStockout_order_pkl.setWidth_yds_check((float) (width_check * 1.094));
					stockout_order_pkl_Service.save(newStockout_order_pkl);
				}
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
	
	@RequestMapping(value = "/deleteToVai",method = RequestMethod.POST)
//	@Transactional(rollbackFor = RuntimeException.class)
	public ResponseEntity<getby_org_response> deleteToVai( @RequestBody updateToVai_request entity,HttpServletRequest request ) {
		getby_org_response  response= new getby_org_response();
		try {
//			GpayAuthentication user = (GpayAuthentication)SecurityContextHolder.getContext().getAuthentication();
			WarehouseCheck warehouse_check_request = entity.warehouse_check;
//			Long stockoutorderid_link = entity.stockoutorderid_link;
			
			WarehouseCheck warehouse_check = warehouseCheckService.findOne(warehouse_check_request.getId());
			Warehouse warehouse = warehouseService.findOne(warehouse_check.getWarehouseid_link());
			Long warehouseid_link = warehouse.getId();
			
			// tim danh sach cac warehouse check cua cay vai va stockoutorder
//			List<WarehouseCheck> warehouseCheck_list = warehouseCheckService.findBy_Warehouse_StockoutOrder(
//					stockoutorderid_link, warehouseid_link);
			List<WarehouseCheck> warehouseCheck_list = warehouseCheckService.findBy_Warehouse_StockoutOrder(
					null, warehouseid_link);
			
			// set thong tin cay vai truoc check
			if(warehouseCheck_list.size() > 0) {
				WarehouseCheck warehouseCheckFirst = warehouseCheck_list.get(0);
				// set lai thong tin warehouse truoc khi check
				warehouse.setYds(warehouseCheckFirst.getYds_origin());
				warehouse.setMet(warehouseCheckFirst.getMet_origin());
				
				Float width = warehouseCheckFirst.getWidth_origin() == null ? (float) 0 : warehouseCheckFirst.getWidth_origin();
				warehouse.setWidth(width);
				warehouse.setWidth_met(width);
				warehouse.setWidth_yds((float) (width / 0.9144));
				
				warehouse.setGrossweight(warehouseCheckFirst.getGrossweight_origin());
				warehouse.setMet_err(null);
				
				if(warehouse.getStatus().equals(WareHouseStatus.WAREHOUSE_STATUS_CHECKED)) {
					warehouse.setStatus(WareHouseStatus.WAREHOUSE_STATUS_UNCHECKED);
				}
				warehouseService.save(warehouse);
			}
			
			// Xoa thong tin check
			for(WarehouseCheck item : warehouseCheck_list) {
				warehouseCheckService.delete(item);
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

	@RequestMapping(value = "/getMaterialListBySpaceEPC_search",method = RequestMethod.POST)
	public ResponseEntity<?> getMaterialListBySpaceEPC_search( @RequestBody GetMaterialListBySpaceEPC entity,HttpServletRequest request ) {
		WareHouseResponse responseBase = new WareHouseResponse();
		try {
			if(entity.spaceepc != null) {
				responseBase.data = warehouseService.findBySpaceepc(entity.spaceepc, entity.stockid_link);
			}else {
				responseBase.data = warehouseService.findBySpaceepcKXD(entity.stockid_link);
			}
			responseBase.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			responseBase.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<WareHouseResponse>(responseBase,HttpStatus.OK);
		}catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/getMaterialListBySpaceEPC_stock_buyercode_search",method = RequestMethod.POST)
	public ResponseEntity<?> getMaterialListBySpaceEPC_stock_buyercode_search( @RequestBody GetMaterialListBySpaceEPC entity,HttpServletRequest request ) {
		WareHouseResponse responseBase = new WareHouseResponse();
		try {
			if(entity.spaceepc != null) {
//				responseBase.data = warehouseService.findBySpaceepc(entity.spaceepc, entity.stockid_link);
//				System.out.println("tren");
				if(entity.buyercode.equals("")) {
					responseBase.data = warehouseService.findBySpaceepc_stock(entity.spaceepc, entity.stockid_link);
				}else {
					responseBase.data = warehouseService.findBySpaceepc_stock_buyercode(entity.spaceepc, entity.buyercode, entity.stockid_link);
				}
			}else {
//				responseBase.data = warehouseService.findBySpaceepcKXD(entity.stockid_link);
//				System.out.println("duoi");
				if(entity.buyercode.equals("")) {
					responseBase.data = warehouseService.findBySpaceepcKXD_stock(entity.stockid_link);
				}else {
					responseBase.data = warehouseService.findBySpaceepcKXD_stock_buyercode(entity.buyercode, entity.stockid_link);
				}
				
			}
			responseBase.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			responseBase.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<WareHouseResponse>(responseBase,HttpStatus.OK);
		}catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/getProductListBySpaceEPC_search",method = RequestMethod.POST)
	public ResponseEntity<?> getProductListBySpaceEPC_search( @RequestBody GetMaterialListBySpaceEPC entity,HttpServletRequest request ) {
		WareHouseResponse responseBase = new WareHouseResponse();
		try {
			if(entity.spaceepc != null) {
				responseBase.data = warehouseService.findProductBySpaceepc(entity.spaceepc, entity.stockid_link);
			}else {
				responseBase.data = warehouseService.findProductBySpaceepcKXD(entity.stockid_link);
			}
			responseBase.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			responseBase.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<WareHouseResponse>(responseBase,HttpStatus.OK);
		}catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/getWarehouseCheckByStockoutOrderD",method = RequestMethod.POST)
	public ResponseEntity<?> getWarehouseCheckByStockoutD( @RequestBody updateToVai_request entity,HttpServletRequest request ) {
		WareHouseCheckResponse response = new WareHouseCheckResponse();
		try {
			
			Long stockoutorderdid_link = entity.stockoutorderdid_link;
			Stockout_order_d stockout_order_d = stockout_order_d_service.findOne(stockoutorderdid_link);
			Long stockoutorderid_link = stockout_order_d.getStockoutorderid_link();
			Long skuid_link = stockout_order_d.getMaterial_skuid_link();
			
			// đang test, tìm những warehousecheck có sku và stockout_order trùng
			List<WarehouseCheck> result = warehouseCheckService.findByStockoutOrderAndSku(stockoutorderid_link, skuid_link);
			
			// sau đó tìm cách lấy các warehousecheck có sku và stockout order trùng nhưng chỉ lấy những record có thời gian gần nhất
			// cái này dùng left outer join thì phải, thử xem
			
			
			response.data = result;
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<WareHouseCheckResponse>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/getByStockoutOrderDId_lotnumber",method = RequestMethod.POST)
	public ResponseEntity<?> getByStockoutOrderDId_lotnumber( @RequestBody updateToVai_request entity,HttpServletRequest request ) {
		WareHouseCheckResponse response = new WareHouseCheckResponse();
		GpayAuthentication user = (GpayAuthentication)SecurityContextHolder.getContext().getAuthentication();
		Date currentDate = new Date();
		try {
			List<WarehouseCheck> result = new ArrayList<WarehouseCheck>();
			
			String lotnumber = entity.lotnumber;
			Long stockoutorderdid_link = entity.stockoutorderdid_link;
			Stockout_order_d stockout_order_d = stockout_order_d_service.findOne(stockoutorderdid_link);
			Long stockoutorderid_link = stockout_order_d.getStockoutorderid_link();
			Stockout_order stockout_order = stockout_order_service.findOne(stockoutorderid_link);
//			Long skuid_link = stockout_order_d.getMaterial_skuid_link();
			
			// ds các cây vải yêu cầu của lot này
			List<Stockout_order_pkl> stockout_order_pkl_list = stockout_order_pkl_Service.getByStockoutOrderDId_lotnumber(stockoutorderdid_link, lotnumber);
			for(Stockout_order_pkl stockout_order_pkl : stockout_order_pkl_list) {
				String epc = stockout_order_pkl.getEpc();
				List<Warehouse> warehouse_list = warehouseService.findMaterialByEPC(epc);
				if(warehouse_list.size() > 0) {
					Warehouse warehouse = warehouse_list.get(0);
					
					List<WarehouseCheck> warehouseCheck_list = warehouseCheckService.findBy_Warehouse_StockoutOrder(stockout_order.getId(), warehouse.getId());
					if(warehouseCheck_list.size() == 0) {
						// new
						WarehouseCheck newWarehouseCheck = new WarehouseCheck();
						// stockoutorderid_link, warehouseid_link, yds_origin, yds_check, met_origin, met_check, width_origin, width_check, 
						// netweight_origin, netweight_check, grossweight_origin, grossweight_check, usercheckid_link, date_check, met_err
						newWarehouseCheck.setId(null);
						newWarehouseCheck.setStockoutorderid_link(stockout_order.getId());
						newWarehouseCheck.setWarehouseid_link(warehouse.getId());
						newWarehouseCheck.setMet_origin(warehouse.getMet());
						newWarehouseCheck.setMet_check((float) 0);
						newWarehouseCheck.setYds_origin(warehouse.getYds());
						newWarehouseCheck.setYds_check((float) 0);
						newWarehouseCheck.setWidth_origin(warehouse.getWidth_met());
						newWarehouseCheck.setWidth_check((float) 0);
						newWarehouseCheck.setMet_err((float) 0);
						//
						newWarehouseCheck.setUsercheckid_link(user.getUserId());
						newWarehouseCheck.setDate_check(currentDate);
						//
						newWarehouseCheck.setLotnumber(warehouse.getLotnumber());
						newWarehouseCheck.setPackageid(warehouse.getPackageid());
						newWarehouseCheck.setEpc(warehouse.getEpc());
						
						result.add(newWarehouseCheck);
//						warehouseCheckService.save(newWarehouseCheck);
					}else {
						// exist
						WarehouseCheck warehouseCheck = warehouseCheck_list.get(0);
						warehouseCheck.setLotnumber(warehouse.getLotnumber());
						warehouseCheck.setPackageid(warehouse.getPackageid());
						warehouseCheck.setEpc(warehouse.getEpc());
						result.add(warehouseCheck);
					}
				}
			}
			
			response.data = result;
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<WareHouseCheckResponse>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/getBySkuAndStock",method = RequestMethod.POST)
	public ResponseEntity<?> getBySkuAndStock( @RequestBody getByLotAndPackageId_request entity,HttpServletRequest request ) {
		WareHouseResponse response = new WareHouseResponse();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Long skuid_link = entity.skuid_link;
//			Long orgid_link = user.getOrgid_link();
			Long org_grant_id_link = user.getOrg_grant_id_link();

			Org org = new Org();
			if(org_grant_id_link != null) {
				org = orgService.findOne(org_grant_id_link);
			}

			if(org.getOrgtypeid_link() != null && org.getOrgtypeid_link() == OrgType.ORG_TYPE_CAT) {
				response.data = warehouseService.getBySkuAndToCat(skuid_link, org_grant_id_link);
			}
			else {
				response.data = warehouseService.getBySkuAndToCat(skuid_link, null);
			}
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<WareHouseResponse>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/check_inStock",method = RequestMethod.POST)
	public ResponseEntity<?> check_inStock( @RequestBody WareHouse_check_inStock_request entity,HttpServletRequest request ) {
		StockOutD_response response = new StockOutD_response();
		try {
//			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Long stockid_link = entity.stockid_link;
			List<Long> skuIdList = entity.skuIdList;
			response.data = new ArrayList<StockOutD>();
			
			if(skuIdList == null) {
				skuIdList = new ArrayList<>();
			}
			if(skuIdList != null && skuIdList.size() > 0) { 
				for(Long skuid_link : skuIdList) {
					List<StockOutD> list = warehouseService.get_inStock_bySku(skuid_link, stockid_link);
					if(list.size() > 0) {
						response.data.add(list.get(0));
					}else {
						StockOutD newStockOutD = new StockOutD();
						newStockOutD.setSkuid_link(skuid_link);
						newStockOutD.setTotalpackage(0);
						response.data.add(newStockOutD);
					}
				}
			}
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<StockOutD_response>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/getMaterialListBySku",method = RequestMethod.POST)
	public ResponseEntity<?> getMaterialListBySku( @RequestBody GetMaterialListByPContract entity,HttpServletRequest request ) {
		WareHouseResponse responseBase = new WareHouseResponse();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			
			Long skuid_link = entity.skuid_link;
			Long stockid_link = entity.stockid_link;
			Long pcontractid_link = entity.pcontractid_link;
			Long productid_link = entity.productid_link;
			
			List<Warehouse> result = new ArrayList<Warehouse>();
			if(productid_link == null) {
				result = warehouseService.findByPContract(null, stockid_link, skuid_link);
			}else {
				List<PContractProduct> PContractProduct_list = pcontractProductService.get_by_product_and_pcontract(user.getRootorgid_link(), productid_link, pcontractid_link);
				Long pcontractProduct_id = null;
				if(PContractProduct_list.size() > 0) {
					pcontractProduct_id = PContractProduct_list.get(0).getId();
				}
				List<Warehouse> warehouse_list = warehouseService.findByPContract_Product(pcontractid_link, productid_link ,stockid_link, skuid_link);
//				System.out.println(warehouse_list.size());
//				System.out.println(pcontractProduct_id);
				result.addAll(warehouse_list);
				
				List<Warehouse> loan_list = warehouseService.findByPContract_Product_loan(pcontractProduct_id ,stockid_link, skuid_link);
				result.addAll(loan_list);
			}
			
			for(Warehouse warehouse : result) {
				if(warehouse.getSpaceepc_link() == null) {
					warehouse.setSpaceString("KXD");
				}else {
					String spaceepc_link = warehouse.getSpaceepc_link();
					List<Stockspace> stockspace_list = stockspaceService.findStockspaceByEpc(spaceepc_link);
					if(stockspace_list.size() > 0) {
						Stockspace stockspace = stockspace_list.get(0);
						String spaceString = "" + stockspace.getStockrow_code() + "-" + stockspace.getSpacename() + "-" + stockspace.getFloorid();
						warehouse.setSpaceString(spaceString);
					}else {
						warehouse.setSpaceString("KXD");
					}
				}
				//
				if(warehouse.getStatus() > WareHouseStatus.WAREHOUSE_STATUS_UNCHECKED) {
					// đã tở -> lấy ngày tở
					List<WarehouseCheck> warehouseCheck_list = warehouseCheckService.findBy_Warehouse_StockoutOrder(null, warehouse.getId());
					if(warehouseCheck_list.size() > 0) {
						WarehouseCheck warehouseCheck = warehouseCheck_list.get(0);
						Date date_check = warehouseCheck.getDate_check();
						warehouse.setDate_check(date_check);
					}
				}
				//
				if(warehouse.getStatus() == WareHouseStatus.WAREHOUSE_STATUS_UNCHECKED) {
					warehouse.setWarehouseStatusString("Chưa tở");
				}else
				if(warehouse.getStatus() == WareHouseStatus.WAREHOUSE_STATUS_CHECKED) {
					warehouse.setWarehouseStatusString("Đã tở");
				}else
				if(warehouse.getStatus() == WareHouseStatus.WAREHOUSE_STATUS_CUT) {
					warehouse.setWarehouseStatusString("Đã cắt");
				}
			}
			
			responseBase.data = result;
			responseBase.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			responseBase.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<WareHouseResponse>(responseBase,HttpStatus.OK);
		}catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/getMaterialListBySku_xuatDieuChuyen",method = RequestMethod.POST)
	public ResponseEntity<?> getMaterialListBySku_xuatDieuChuyen( @RequestBody GetMaterialListByPContract entity,HttpServletRequest request ) {
		WareHouseResponse responseBase = new WareHouseResponse();
		try {
//			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			
			Long skuid_link = entity.skuid_link;
			Long stockid_link = entity.stockid_link;
			Long pcontractid_link = entity.pcontractid_link;
			Long productid_link = entity.productid_link;
			
//			System.out.println(warehouse_list.size());
			List<Warehouse> warehouse_list = warehouseService.findByPContract_Product_xuatDieuCHuyen(pcontractid_link, productid_link ,stockid_link, skuid_link);
//			System.out.println(warehouse_list.size());
			
			for(Warehouse warehouse : warehouse_list) {
				if(warehouse.getSpaceepc_link() == null) {
					warehouse.setSpaceString("KXD");
				}else {
					String spaceepc_link = warehouse.getSpaceepc_link();
					List<Stockspace> stockspace_list = stockspaceService.findStockspaceByEpc(spaceepc_link);
					if(stockspace_list.size() > 0) {
						Stockspace stockspace = stockspace_list.get(0);
						String spaceString = "" + stockspace.getStockrow_code() + "-" + stockspace.getSpacename() + "-" + stockspace.getFloorid();
						warehouse.setSpaceString(spaceString);
					}else {
						warehouse.setSpaceString("KXD");
					}
				}
				//
				if(warehouse.getStatus() > WareHouseStatus.WAREHOUSE_STATUS_UNCHECKED) {
					// đã tở -> lấy ngày tở
					List<WarehouseCheck> warehouseCheck_list = warehouseCheckService.findBy_Warehouse_StockoutOrder(null, warehouse.getId());
					if(warehouseCheck_list.size() > 0) {
						WarehouseCheck warehouseCheck = warehouseCheck_list.get(0);
						Date date_check = warehouseCheck.getDate_check();
						warehouse.setDate_check(date_check);
					}
				}
				//
				if(warehouse.getStatus() == WareHouseStatus.WAREHOUSE_STATUS_UNCHECKED) {
					warehouse.setWarehouseStatusString("Chưa tở");
				}else
				if(warehouse.getStatus() == WareHouseStatus.WAREHOUSE_STATUS_CHECKED) {
					warehouse.setWarehouseStatusString("Đã tở");
				}else
				if(warehouse.getStatus() == WareHouseStatus.WAREHOUSE_STATUS_CUT) {
					warehouse.setWarehouseStatusString("Đã cắt");
				}
			}
			
			responseBase.data = warehouse_list;
			responseBase.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			responseBase.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<WareHouseResponse>(responseBase,HttpStatus.OK);
		}catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/getLotNumber_ByStockout_order_d",method = RequestMethod.POST)
	public ResponseEntity<?> getLotNumber_ByStockout_order_d( @RequestBody GetMaterialListByPContract entity,HttpServletRequest request ) {
		WareHouseResponse responseBase = new WareHouseResponse();
		try {
//			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Long stockoutorderdid_link = entity.stockoutorderdid_link;
			Stockout_order_d stockout_order_d = stockout_order_d_service.findOne(stockoutorderdid_link);
			Long skuid_link = stockout_order_d.getMaterial_skuid_link();
			
			List<Warehouse> warehouse_list = new ArrayList<Warehouse>();
			List<String> lotnumber_list = warehouseService.findLotnumberBySku(skuid_link);
			
			for(String lotnumber : lotnumber_list) {
				Warehouse warehouse = new Warehouse();
				warehouse.setLotnumber(lotnumber);
				warehouse_list.add(warehouse);
			}
			
			responseBase.data = warehouse_list;
			responseBase.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			responseBase.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<WareHouseResponse>(responseBase,HttpStatus.OK);
		}catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/check_warehouse_in_stockout",method = RequestMethod.POST)
	public ResponseEntity<?> check_warehouse_in_stockout( @RequestBody Warehouse_checkInStockout_request entity,HttpServletRequest request ) {
		WareHouseResponse responseBase = new WareHouseResponse();
		try {
//			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			
			String returnMessage = "";
			List<Warehouse> warehouse_list = entity.data;
			Long stockoutid = entity.stockoutid_link;
			
			List<Integer> statuses = new ArrayList<Integer>();
			statuses.add(StockoutStatus.STOCKOUT_STATUS_OK);
			statuses.add(StockoutStatus.STOCKOUT_STATUS_PICKING);
			statuses.add(StockoutStatus.STOCKOUT_STATUS_ERR);
			
			for(Warehouse warehouse : warehouse_list) {
				String message = "";
				if(warehouse.getEpc() != null) {
					List<StockOutPklist> stockOutPklist_list = stockOutPklistService.getByEpc_in_Stockout(warehouse.getEpc(), statuses, stockoutid);
					if(stockOutPklist_list.size() > 0) {
						// đã nằm trong stockout khác
						for(StockOutPklist stockOutPklist : stockOutPklist_list) {
							Long stockoutdid_link = stockOutPklist.getStockoutdid_link();
							StockOutD stockoutd = stockOutDService.findOne(stockoutdid_link);
							Long stockoutid_link = stockoutd.getStockoutid_link();
							StockOut stockout = stockOutService.findOne(stockoutid_link);
							
							if(message.equals("")) {
								message += "Lot " + stockOutPklist.getLotnumber() + ",cây " + stockOutPklist.getPackageid();
								message += " đã nằm trong phiếu xuất: " + stockout.getStockoutcode();
							}else {
								message += ", " + stockout.getStockoutcode();
							}
//							message += System.lineSeparator();
							message += "%seperator%";
//							System.out.println(message);
						}
					}
				}
				returnMessage += message;
			}
			
//			System.out.println(returnMessage);
			
			if(!returnMessage.equals("")) {
//				System.out.println("in here");
				responseBase.data = new ArrayList<Warehouse>();
				responseBase.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
				responseBase.setMessage(returnMessage);
				return new ResponseEntity<WareHouseResponse>(responseBase,HttpStatus.OK);
			}
			
			responseBase.data = new ArrayList<Warehouse>();
			responseBase.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			responseBase.setMessage("");
			
//			responseBase.data = new ArrayList<Warehouse>();
//			responseBase.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
//			responseBase.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<WareHouseResponse>(responseBase,HttpStatus.OK);
		}catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/getBySku_pordergrant_stockoutorder",method = RequestMethod.POST)
	public ResponseEntity<?> getBySku_pordergrant_stockoutorder( @RequestBody Warehouse_list_request entity,HttpServletRequest request ) {
		WareHouseResponse response = new WareHouseResponse();
		try {
//			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Long stockoutorderid_link = entity.stockoutorderid_link;
			Long skuid_link = entity.skuid_link;
			Long pordergrantid_link = entity.pordergrantid_link;
			List<String> listSelectedEpc = entity.listSelectedEpc;
			if(listSelectedEpc.size() == 0) { // de cau lenh sql not in ko bi loi
				listSelectedEpc.add("");
			}
			
			List<Warehouse> result = new ArrayList<Warehouse>();
			POrder_Grant porderGrant = porderGrantService.findOne(pordergrantid_link);
			Org toChuyen = orgService.findOne(porderGrant.getGranttoorgid_link());
			Org phanXuong = orgService.findOne(toChuyen.getParentid_link());
			
			// lay danh sach kho vai cua phan xuong
			List<Org> khoVai_list = orgService.findOrgByParentAndType(OrgType.ORG_TYPE_KHONGUYENLIEU, phanXuong.getId());
			if(khoVai_list.size() == 0) {
				response.data = result;
				response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
				response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
				return new ResponseEntity<WareHouseResponse>(response,HttpStatus.OK);
			}
			List<Long> khoVai_Id_list = new ArrayList<Long>();
			for(Org org : khoVai_list) {
				khoVai_Id_list.add(org.getId());
			}
			
			if(stockoutorderid_link == null) {
				// danh sach cay vai trong khoVai_list, chua nam trong yeu cau xuat nao
				result = warehouseService.getBy_sku_stockList(skuid_link, khoVai_Id_list, listSelectedEpc);
			}else {
				result = warehouseService.getBy_sku_stockList(skuid_link, khoVai_Id_list, listSelectedEpc);
			}
			
			for(Warehouse warehouse : result) {
				//
				if(warehouse.getGrossweight() != null) {
					warehouse.setGrossweight_lbs((float) (warehouse.getGrossweight() * 2.205));
				}
				//
				if(warehouse.getStatus() > WareHouseStatus.WAREHOUSE_STATUS_UNCHECKED) {
					// đã tở -> lấy ngày tở
					List<WarehouseCheck> warehouseCheck_list = warehouseCheckService.findBy_Warehouse_StockoutOrder(null, warehouse.getId());
					if(warehouseCheck_list.size() > 0) {
						WarehouseCheck warehouseCheck = warehouseCheck_list.get(0);
						Date date_check = warehouseCheck.getDate_check();
						warehouse.setDate_check(date_check);
					}
				}
				//
				if(warehouse.getStatus() == WareHouseStatus.WAREHOUSE_STATUS_UNCHECKED) {
					warehouse.setWarehouseStatusString("Chưa tở");
				}else
				if(warehouse.getStatus() == WareHouseStatus.WAREHOUSE_STATUS_CHECKED) {
					warehouse.setWarehouseStatusString("Đã tở");
				}else
				if(warehouse.getStatus() == WareHouseStatus.WAREHOUSE_STATUS_CUT) {
					warehouse.setWarehouseStatusString("Đã cắt");
				}
				//
				if(warehouse.getSpaceepc_link() == null) {
					warehouse.setSpaceString("KXD");
				}else {
					String spaceepc_link = warehouse.getSpaceepc_link();
					List<Stockspace> stockspace_list = stockspaceService.findStockspaceByEpc(spaceepc_link);
					if(stockspace_list.size() > 0) {
						Stockspace stockspace = stockspace_list.get(0);
						String spaceString = "" + stockspace.getStockrow_code() + "-" + stockspace.getSpacename() + "-" + stockspace.getFloorid();
						warehouse.setSpaceString(spaceString);
					}else {
						warehouse.setSpaceString("KXD");
					}
				}
			}
			
			response.data = result;
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<WareHouseResponse>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
