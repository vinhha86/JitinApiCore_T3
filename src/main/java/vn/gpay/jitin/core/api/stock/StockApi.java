package vn.gpay.jitin.core.api.stock;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import vn.gpay.jitin.core.base.ResponseBase;
import vn.gpay.jitin.core.base.ResponseError;
import vn.gpay.jitin.core.org.IOrgService;
import vn.gpay.jitin.core.org.Org;
import vn.gpay.jitin.core.security.GpayUser;
import vn.gpay.jitin.core.security.GpayUserOrg;
import vn.gpay.jitin.core.security.IGpayUserOrgService;
import vn.gpay.jitin.core.stock.IStockrowService;
import vn.gpay.jitin.core.stock.IStockspaceService;
import vn.gpay.jitin.core.stock.StockTree;
import vn.gpay.jitin.core.stock.Stockrow;
import vn.gpay.jitin.core.stock.StockrowResponse;
import vn.gpay.jitin.core.stock.Stockspace;
import vn.gpay.jitin.core.utils.OrgType;
import vn.gpay.jitin.core.utils.ResponseMessage;
import vn.gpay.jitin.core.warehouse.IWarehouseService;
import vn.gpay.jitin.core.warehouse.Warehouse;

@RestController
@RequestMapping("/api/v1/stock")
public class StockApi {
	@Autowired IStockrowService stockrowService;
	@Autowired IStockspaceService stockspaceService;
	@Autowired IOrgService orgService;
	@Autowired IGpayUserOrgService userOrgService;
	@Autowired IWarehouseService warehouseService;
	
	@RequestMapping(value = "/stockmenu_tree",method = RequestMethod.POST)
	public ResponseEntity<?> stockmenu_tree(@RequestBody Stock_menu_request entity, HttpServletRequest request ) {
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			StockResponse response = new StockResponse();
			
			//
			List<Org> listorg = new ArrayList<Org>();
			List<Long> listorgId = new ArrayList<Long>();
			if(user.getOrgid_link().equals((long) 1)) {
				listorgId = null; // lấy hết
			}else {
				// Tìm trong bảng app_user_org
				for(GpayUserOrg userorg:userOrgService.getall_byuser_andtype(user.getId(), OrgType.ORG_TYPE_XUONGSX)){
					listorg.add(orgService.findOne(userorg.getOrgid_link()));
				}
				// nếu không có lấy theo user orgid_link
				if(listorg.size() == 0) {
					Org userOrg = orgService.findOne(user.getOrgid_link());
					listorg.add(userOrg);
				}
				//
				for(Org org : listorg) {
					listorgId.add(org.getId());
				}
			}
			
			// tree
			List<Stockrow> stockrow_list = stockrowService.findStockRowByOrgtype(OrgType.ORG_TYPE_KHONGUYENLIEU);
			List<StockTree> children = stockrowService.createTree(stockrow_list, listorgId, entity);
			
//			response.stockrow_list=stockrow_list;
			response.children=children;
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<StockResponse>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			e.printStackTrace();
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/stockmenu_space_list",method = RequestMethod.POST)
	public ResponseEntity<?> stockmenu_space_list(@RequestBody Stock_menu_request entity, HttpServletRequest request ) {
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			StockResponse response = new StockResponse();
			//
			List<Org> listorg = new ArrayList<Org>();
			List<Long> listorgId = new ArrayList<Long>();
			if(user.getOrgid_link().equals((long) 1)) {
				listorgId = null; // lấy hết
			}else {
				// Tìm trong bảng app_user_org
				for(GpayUserOrg userorg:userOrgService.getall_byuser_andtype(user.getId(), OrgType.ORG_TYPE_XUONGSX)){
					listorg.add(orgService.findOne(userorg.getOrgid_link()));
				}
				// nếu không có lấy theo user orgid_link
				if(listorg.size() == 0) {
					Org userOrg = orgService.findOne(user.getOrgid_link());
					listorg.add(userOrg);
				}
				//
				for(Org org : listorg) {
					listorgId.add(org.getId());
				}
			}
			
			// ds khoang
			List<String> space_list = new ArrayList<String>();
			if(entity.maHangId != null || entity.donHang != null || entity.maSP != null) {
//				System.out.println("listorgId: " + listorgId);
//				System.out.println("--------------------");
				
				Long maHangId = entity.maHangId;
				String donHang = entity.donHang == null ? "" : entity.donHang;
				String maSP = entity.maSP == null ? "" : entity.maSP;
				
				if(listorgId == null) { // tat ca cac px
					listorgId = new ArrayList<Long>();
					List<Org> org_list = orgService.findAllorgByTypeId(OrgType.ORG_TYPE_XUONGSX, user.getRootorgid_link());
					for(Org org : org_list) {
						listorgId.add(org.getId());
					}
				}
				for(Long pxId : listorgId) {
//					Org orgPx = orgService.findOne(pxId);
					List<String> lstSstring = new ArrayList<String>();
					lstSstring.add("3");
					List<Org> orgKho_list = orgService.getorgChildrenbyOrg(pxId, lstSstring);
					
					for(Org orgKho : orgKho_list) {
//						List<String> spaceepc_list = warehouseService.getSpaceEpcBySkucode_Pcontract_Org(
//								maHangId, donHang, orgKho.getId()
//								);
						List<String> spaceepc_list = warehouseService.getSpaceEpcBySkucode_Pcontract_Buyercode_Org(
								maHangId, donHang, maSP, orgKho.getId()
								);
						
						String khoString = "";
						for(String s : spaceepc_list) {
//							System.out.println("+ spaceepc: " + s);
							List<Stockspace> stockspace_list = stockspaceService.findStockspaceByEpc(s);
							if(stockspace_list.size() > 0) {
								Stockspace stockspace = stockspace_list.get(0);
								Stockrow stockrow = stockrowService.findOne(stockspace.getRowid_link());
								String rowCode = stockrow.getCode();
								String spacename = stockspace.getSpacename();
								Integer floorid = stockspace.getFloorid();
								String rowSpaceFloor = rowCode + "-" + spacename + "-" + floorid;
								
								if(khoString.equals("")) {
									khoString+="" + orgKho.getName() + ": " + rowSpaceFloor;
								}else {
									khoString+="; " + rowSpaceFloor;
								}
							}
						}
						if(!khoString.equals("")) {
							space_list.add(khoString);
						}
					}
					
				}
				
				response.space_list = space_list;
			}
			
//			response.stockrow_list=stockrow_list;
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<StockResponse>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			e.printStackTrace();
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/stockproductmenu_tree",method = RequestMethod.POST)
	public ResponseEntity<?> stockproductmenu_tree(@RequestBody Stock_menu_request entity, HttpServletRequest request ) {
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			StockResponse response = new StockResponse();
			
			//
			List<Org> listorg = new ArrayList<Org>();
			List<Long> listorgId = new ArrayList<Long>();
			if(user.getOrgid_link().equals((long) 1)) {
				Long org_grant_id_link = user.getOrg_grant_id_link();
				if(org_grant_id_link != null) {
					Org orgGrant = orgService.findOne(org_grant_id_link);
					Integer orgGrantType = orgGrant.getOrgtypeid_link();
					if(orgGrantType.equals(OrgType.ORG_TYPE_CUAHANG)) {
						// nếu user là của cửa hàng thêm cửa hàng
						// chú ý lấy cả tất cả các cửa hàng được user phụ trách
						for(GpayUserOrg userorg:userOrgService.getall_byuser_andtype(user.getId(), OrgType.ORG_TYPE_CUAHANG)){
							listorg.add(orgService.findOne(userorg.getOrgid_link()));
						}
						// nếu không có lấy theo user org_grant_id_link
						if(listorg.size() == 0) {
							Org userOrg = orgService.findOne(org_grant_id_link);
							listorg.add(userOrg);
						}
						//
						for(Org org : listorg) {
							listorgId.add(org.getId());
						}
						
					}else {
						// các trường hợp còn lại
						listorgId = null; // lấy hết
					}
				}else {
					// nếu org_grant_id_link == null
					listorgId = null; // lấy hết
				}
			}else {
				// Trường hợp user là người của phân xưởng
				// Tìm trong bảng app_user_org
				for(GpayUserOrg userorg:userOrgService.getall_byuser_andtype(user.getId(), OrgType.ORG_TYPE_XUONGSX)){
					listorg.add(orgService.findOne(userorg.getOrgid_link()));
				}
				// nếu không có lấy theo user orgid_link
				if(listorg.size() == 0) {
					Org userOrg = orgService.findOne(user.getOrgid_link());
					listorg.add(userOrg);
				}
				//
				for(Org org : listorg) {
					listorgId.add(org.getId());
				}
			}

			List<Stockrow> stockrow_list = stockrowService.findStockRowByOrgtype(OrgType.ORG_TYPE_KHOTHANHPHAM);
			List<StockTree> children = stockrowService.createStockProductTree(stockrow_list, listorgId, entity);
			
//			response.stockrow_list=stockrow_list;
			response.children=children;
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<StockResponse>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			e.printStackTrace();
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/create_row",method = RequestMethod.POST)
	public ResponseEntity<?> create_row(@RequestBody Stock_createRow_request entity, HttpServletRequest request ) {
		try {
			StockrowResponse response = new StockrowResponse();
			
			Stockrow stockrow = entity.data;
			if(stockrow.getId() == null) { // new 
				List<Stockrow> listStockrow = stockrowService.findStockrowByOrgIDAndCode(
						stockrow.getOrgid_link(), stockrow.getCode());
				if(listStockrow.size() > 0) {
					ResponseError errorBase = new ResponseError();
					errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
					errorBase.setMessage("Dãy đã tồn tại");
				    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
				}else {
					stockrow = stockrowService.save(stockrow);
					response.isNew = true;
				}
			}else { //edit
				List<Stockrow> listStockrow = stockrowService.findStockrowByOrgIDAndCodeAndNotId(
						stockrow.getOrgid_link(), stockrow.getCode(), stockrow.getId());
				if(listStockrow.size() > 0) {
					ResponseError errorBase = new ResponseError();
					errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
					errorBase.setMessage("Dãy đã tồn tại");
				    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
				}else {
					stockrow = stockrowService.save(stockrow);
					response.isNew = false;
				}
			}
			
			response.stockrow = stockrow;
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<StockrowResponse>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/create_space",method = RequestMethod.POST)
	public ResponseEntity<?> create_space(@RequestBody Stock_createFloor_request entity, HttpServletRequest request ) {
		try {
			StockspaceResponse response = new StockspaceResponse();
			
			Long orgid_link = entity.orgid_link;
			String spacename = entity.spacename;
			String spacename_old = entity.spacename_old;
			Long rowid_link = entity.rowid_link;
			boolean isCreateNew = entity.isCreateNew;
			
			if(isCreateNew) { // new
				List<Stockspace> stockspace_list = stockspaceService.findStockspaceByRowIdSpace(rowid_link, spacename);
				if(stockspace_list.size() > 0) {
					ResponseError errorBase = new ResponseError();
					errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
					errorBase.setMessage("Hàng đã tồn tại");
				    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
				}else {
					// thiếu thông tin epc (khoá chính) nên ko lưu được
					// epc có ở phần tạo tầng
					Stockspace stockspace = new Stockspace();
					stockspace.setOrgid_link(orgid_link);
					stockspace.setSpacename(spacename);
					stockspace.setRowid_link(rowid_link);
					response.stockspace = stockspaceService.save(stockspace);
					response.isSpaceNew = true;
				}
			}else { // edit
				if(!spacename.equals(spacename_old)) {
					List<Stockspace> stockspace_list = stockspaceService.findStockspaceByRowIdSpace(rowid_link, spacename);
					if(stockspace_list.size() > 0) {
						ResponseError errorBase = new ResponseError();
						errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
						errorBase.setMessage("Hàng đã tồn tại");
					    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
					}else {
						stockspace_list = stockspaceService.findStockspaceByRowIdSpace(rowid_link, spacename_old);
						for(Stockspace stockspace : stockspace_list) {
							stockspace.setSpacename(spacename);
							response.stockspace = stockspaceService.save(stockspace);
							response.isSpaceNew = false;
						}
					}
				}
			}
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<StockspaceResponse>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/create_floor",method = RequestMethod.POST)
	public ResponseEntity<?> create_floor(@RequestBody Stock_createFloor_request entity, HttpServletRequest request ) {
		try {
			StockspaceResponse response = new StockspaceResponse();
			
			Long orgid_link = entity.orgid_link;
//			String spaceepc = entity.spaceepc;
			String spacename = entity.spacename;
			Long rowid_link = entity.rowid_link;
			Integer floorid = entity.floorid;
			boolean isCreateNew = entity.isCreateNew;
			
			if(isCreateNew) { // new
				// check spaceepc tồn tại
//				List<Stockspace> stockspace_list = stockspaceService.findStockspaceByEpc(orgid_link, spaceepc);
//				List<Stockspace> stockspace_list = stockspaceService.findStockspaceByEpc(spaceepc);
//				if(stockspace_list.size() > 0) {
//					ResponseError errorBase = new ResponseError();
//					errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
//					errorBase.setMessage("EPC đã tồn tại");
//				    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
//				}
				// check spacename và floorid tồn tại
				List<Stockspace> stockspace_list = stockspaceService.findStockspaceByRowIdSpaceFloor(rowid_link, spacename, floorid);
				if(stockspace_list.size() > 0) {
					ResponseError errorBase = new ResponseError();
					errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
					errorBase.setMessage("Hàng và tầng đã tồn tại");
				    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
				}
				//
				stockspace_list = stockspaceService.findStockspaceByRowIdSpace(rowid_link, spacename);
				if(stockspace_list.size() > 0) {
					response.isSpaceNew = false;
				}else {
					response.isSpaceNew = true;
				}
				
				UUID uuid = UUID.randomUUID();
			    String spaceepc = uuid.toString();
				
				Stockspace stockspace = new Stockspace();
				stockspace.setOrgid_link(orgid_link);
				stockspace.setSpaceepc(spaceepc);
				stockspace.setSpacename(spacename);
				stockspace.setRowid_link(rowid_link);
				stockspace.setFloorid(floorid);
				stockspace = stockspaceService.save(stockspace);
				response.stockspace = stockspace;
				response.isFloorNew = true;
			}else { // edit
//				String spaceepc_old = entity.spaceepc_old;
				String spacename_old = entity.spacename_old;
				Integer floorid_old = entity.floorid_old;
				
//				if(!spaceepc_old.equals(spaceepc)) {
////					List<Stockspace> stockspace_list = stockspaceService.findStockspaceByEpc(orgid_link, spaceepc);
//					List<Stockspace> stockspace_list = stockspaceService.findStockspaceByEpc(spaceepc);
//					if(stockspace_list.size() > 0) {
//						ResponseError errorBase = new ResponseError();
//						errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
//						errorBase.setMessage("EPC đã tồn tại");
//					    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
//					}
//				}
				
				if(!spacename_old.equals(spacename) || !floorid_old.equals(floorid)) {
					List<Stockspace> stockspace_list = stockspaceService.findStockspaceByRowIdSpaceFloor(
							rowid_link, spacename, floorid
							);
					if(stockspace_list.size() > 0) {
						ResponseError errorBase = new ResponseError();
						errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
						errorBase.setMessage("Hàng và tầng đã tồn tại");
					    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
					}
				}
				
				List<Stockspace> stockspace_list = stockspaceService.findStockspaceByRowIdSpaceFloor(
						rowid_link, spacename_old, floorid_old
						);
				if(stockspace_list.size() > 0) {
					Stockspace stockspace = stockspace_list.get(0);
					
					Stockspace newStockspace = new Stockspace();
					newStockspace.setOrgid_link(stockspace.getOrgid_link());
					newStockspace.setRowid_link(stockspace.getRowid_link());
					newStockspace.setSpaceepc(stockspace.getSpaceepc());
					newStockspace.setSpacename(spacename);
					newStockspace.setFloorid(floorid);
					
					stockspaceService.delete(stockspace);
					response.stockspace = stockspaceService.save(newStockspace);
				}
			}
			
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<StockspaceResponse>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/delete_row",method = RequestMethod.POST)
	public ResponseEntity<?> delete_row(@RequestBody Stock_getById_request entity, HttpServletRequest request ) {
		try {
			StockspaceResponse response = new StockspaceResponse();
			Long id = entity.id;
			Stockrow stockrow = stockrowService.findOne(id);
			List<Stockspace> stockspace_list = stockrow.getStockspaces();
			
			// delete Stockspace
			for(Stockspace stockspace : stockspace_list) {
				stockspaceService.delete(stockspace);
			}
			
			// delete Stockrow
			stockrowService.delete(stockrow);
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<StockspaceResponse>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/delete_space",method = RequestMethod.POST)
	public ResponseEntity<?> delete_space(@RequestBody Stock_delete_request entity, HttpServletRequest request ) {
		try {
			StockspaceResponse response = new StockspaceResponse();
			List<Stockspace> stockspace_list = stockspaceService.findStockspaceByRowIdSpace(entity.rowid_link, entity.spacename);
			for(Stockspace stockspace : stockspace_list) {
				stockspaceService.delete(stockspace);
			}
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<StockspaceResponse>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/delete_floor",method = RequestMethod.POST)
	public ResponseEntity<?> delete_floor(@RequestBody Stock_delete_request entity, HttpServletRequest request ) {
		try {
			Stockspace_delete_Response response = new Stockspace_delete_Response();
			List<Stockspace> stockspace_list = stockspaceService.findStockspaceByRowIdSpaceFloor(
						entity.rowid_link, entity.spacename, entity.floorid
					);
			if(stockspace_list.size() > 0) {
				Stockspace stockspace = stockspace_list.get(0);
				stockspaceService.delete(stockspace);
			}
			
			stockspace_list = stockspaceService.findStockspaceByRowIdSpace(
					entity.rowid_link, entity.spacename
				);
			
			if(stockspace_list.size() == 0) {
				response.isSpaceDelete = true;
			}else {
				response.isSpaceDelete = false;
			}
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<Stockspace_delete_Response>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/change_stock",method = RequestMethod.POST)
	public ResponseEntity<?> change_stock(@RequestBody Stock_changeStock_request entity, HttpServletRequest request ) {
		try {
			ResponseBase response = new ResponseBase();
			List<Warehouse> warehouse_list = entity.selection;
//			StockTree stockTree = entity.selectedStock;
			Long orgid_link = entity.orgid_link; // id kho
			
			if(entity.row == null && entity.space == null && entity.floor == null) {
				for(Warehouse warehouse : warehouse_list) {
					Long warehouseid = warehouse.getId();
					Warehouse warehouseOld = warehouseService.findOne(warehouseid);
					warehouseOld.setStockid_link(orgid_link);
					warehouseOld.setSpaceepc_link(null);
					warehouseService.save(warehouseOld);
				}
			}else {
				List<Stockspace> stockspace_list = stockspaceService.findStockspaceByRowSpaceFloor(
						entity.row, entity.space, entity.floor, entity.orgid_link
						);
				
				if(stockspace_list.size() == 0) {
					response.setRespcode(ResponseMessage.KEY_RC_BAD_REQUEST);
					response.setMessage("Khoang này không tồn tại");
				    return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
				}else {
					Stockspace stockspace = stockspace_list.get(0);
					for(Warehouse warehouse : warehouse_list) {
						Long warehouseid = warehouse.getId();
						Warehouse warehouseOld = warehouseService.findOne(warehouseid);
						warehouseOld.setStockid_link(orgid_link);
						warehouseOld.setSpaceepc_link(stockspace.getSpaceepc());
						warehouseService.save(warehouseOld);
					}
				}
			}

//			Long orgid_link = stockTree.getOrgid_link(); // id kho
//			if(stockTree.getName().equals("KXD") && stockTree.getSpaceepc() == null) {
//				// chuyen sang khoang kxd
//				for(Warehouse warehouse : warehouse_list) {
//					Long warehouseid = warehouse.getId();
//					Warehouse warehouseOld = warehouseService.findOne(warehouseid);
//					warehouseOld.setStockid_link(orgid_link);
//					warehouseOld.setSpaceepc_link(null);
//					warehouseService.save(warehouseOld);
//				}
//			} else {
//				String spaceepc = stockTree.getSpaceepc();
//				for(Warehouse warehouse : warehouse_list) {
//					Long warehouseid = warehouse.getId();
//					Warehouse warehouseOld = warehouseService.findOne(warehouseid);
//					warehouseOld.setStockid_link(orgid_link);
//					warehouseOld.setSpaceepc_link(spaceepc);
//					warehouseService.save(warehouseOld);
//				}
//			}
			
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
