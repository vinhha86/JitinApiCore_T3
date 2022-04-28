package vn.gpay.jitin.core.api.stockout_order;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import vn.gpay.jitin.core.base.ResponseBase;
import vn.gpay.jitin.core.stock.IStockspaceService;
import vn.gpay.jitin.core.stock.Stockspace;
import vn.gpay.jitin.core.stockout_order.IStockout_order_d_service;
import vn.gpay.jitin.core.stockout_order.IStockout_order_service;
import vn.gpay.jitin.core.stockout_order.Stockout_order;
import vn.gpay.jitin.core.stockout_order.Stockout_order_d;
import vn.gpay.jitin.core.stockout_order.Stockout_order_pkl;
import vn.gpay.jitin.core.utils.ResponseMessage;
import vn.gpay.jitin.core.utils.WareHouseStatus;
import vn.gpay.jitin.core.warehouse.IWarehouseService;
import vn.gpay.jitin.core.warehouse.Warehouse;

@RestController
@RequestMapping("/api/v1/stockoutorder_d")
public class Stockout_order_dAPI {
	
	@Autowired IStockout_order_d_service stockout_order_d_Service;
	@Autowired IStockout_order_service stockout_order_Service;
	@Autowired IWarehouseService warehouseService;
	@Autowired IStockspaceService stockspaceService;
	
	@RequestMapping(value = "/getByStockoutOrderId",method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> getByStockoutOrderId(@RequestBody Stockout_order_d_getByStockoutOrderId_request entity,HttpServletRequest request ) {
		Stockout_order_d_getByStockoutOrderId_response response = new Stockout_order_d_getByStockoutOrderId_response();
		try {
			Stockout_order stockout_order = stockout_order_Service.findOne(entity.stockoutorderid_link);
			List<Stockout_order_d> list = stockout_order_d_Service.getByStockoutOrderId(entity.stockoutorderid_link);
			for(Stockout_order_d item : list) {
				// spaces
				Long stockid_link = stockout_order.getOrgid_from_link();
				Long skuid_link = item.getMaterial_skuid_link();
				String data_spaces = warehouseService.getspaces_bysku(stockid_link, skuid_link);
				item.setData_spaces(data_spaces);
				//
				if(item.getTotalmet() == null) item.setTotalmet((float) 0);
				if(item.getTotalyds() == null) item.setTotalyds((float) 0); 
				if(item.getTotalmetcheck() == null) item.setTotalmetcheck((float) 0); 
				if(item.getTotalydscheck() == null) item.setTotalydscheck((float) 0); 
				if(item.getTotalpackage() == null) item.setTotalpackage(0); 
				if(item.getTotalpackagecheck() == null) item.setTotalpackagecheck(0);
				
				List<Stockout_order_pkl> pkl_list = item.getStockout_order_pkl();
				for(Stockout_order_pkl pkl_item : pkl_list) {
					String epc = pkl_item.getEpc();
					List<Warehouse> warehouse_list = warehouseService.findMaterialByEPCAndStock(epc, stockout_order.getOrgid_from_link());
					if(warehouse_list.size() > 0) {
						Warehouse warehouse = warehouse_list.get(0);
						String spaceepc_link = warehouse.getSpaceepc_link();
						List<Stockspace> stockspace_list = stockspaceService.findStockspaceByEpc(spaceepc_link);
						
						if(stockspace_list.size() > 0) {
							Stockspace stockspace = stockspace_list.get(0);
							String spaceString = "" + stockspace.getStockrow_code() + "-" + stockspace.getSpacename() + "-" + stockspace.getFloorid();
							pkl_item.setSpaceString(spaceString);
						}else {
							pkl_item.setSpaceString("KXD");
						}
						
						pkl_item.setWarehouseStatus(warehouse.getStatus());
						if(warehouse.getStatus() == WareHouseStatus.WAREHOUSE_STATUS_UNCHECKED) {
							pkl_item.setWarehouseStatusString("Chưa tở");
						}
						if(warehouse.getStatus() == WareHouseStatus.WAREHOUSE_STATUS_CHECKED) {
							pkl_item.setWarehouseStatusString("Đã tở");
						}
						if(warehouse.getStatus() == WareHouseStatus.WAREHOUSE_STATUS_CUT) {
							pkl_item.setWarehouseStatusString("Đã cắt");
						}
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

}
