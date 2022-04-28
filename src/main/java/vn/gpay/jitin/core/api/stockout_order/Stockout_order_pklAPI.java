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
import vn.gpay.jitin.core.stockout_order.IStockout_order_d_service;
import vn.gpay.jitin.core.stockout_order.IStockout_order_pkl_Service;
import vn.gpay.jitin.core.stockout_order.Stockout_order_d;
import vn.gpay.jitin.core.stockout_order.Stockout_order_pkl;
import vn.gpay.jitin.core.utils.ResponseMessage;
import vn.gpay.jitin.core.utils.WareHouseStatus;
import vn.gpay.jitin.core.warehouse.IWarehouseService;
import vn.gpay.jitin.core.warehouse.Warehouse;
import vn.gpay.jitin.core.warehouse_check.IWarehouseCheckService;
import vn.gpay.jitin.core.warehouse_check.WarehouseCheck;

@RestController
@RequestMapping("/api/v1/stockoutorder_pkl")
public class Stockout_order_pklAPI {
	@Autowired IStockout_order_pkl_Service stockout_order_pkl_Service;
	@Autowired IStockout_order_d_service stockout_order_d_service;
	@Autowired IWarehouseService warehouseService;
	@Autowired IWarehouseCheckService warehouseCheckService;
	
	@RequestMapping(value = "/getByStockoutOrderDId",method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> getByStockoutOrderDId(@RequestBody Stockout_order_pkl_getByStockoutOrderDId_request entity,HttpServletRequest request ) {
		Stockout_order_pkl_response response = new Stockout_order_pkl_response();
		try {
			
			response.data = stockout_order_pkl_Service.getByStockoutOrderDId(entity.stockoutorderdid_link);
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<ResponseBase>(response,HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value = "/getByStockoutOrderDId_ToVai",method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> getByStockoutOrderDId_ToVai(@RequestBody Stockout_order_pkl_getByStockoutOrderDId_request entity,HttpServletRequest request ) {
		Stockout_order_pkl_response response = new Stockout_order_pkl_response();
		try {
			
			List<Stockout_order_pkl> stockout_order_pkl_list = stockout_order_pkl_Service.getByStockoutOrderDId(entity.stockoutorderdid_link);
			Stockout_order_d stockout_order_d = stockout_order_d_service.findOne(entity.stockoutorderdid_link);
			Long stockoutorderid_link = stockout_order_d.getStockoutorderid_link();
			
			for(Stockout_order_pkl stockout_order_pkl : stockout_order_pkl_list) {
				String epc = stockout_order_pkl.getEpc();
				List<Warehouse> warehouse_list = warehouseService.findMaterialByEPC(epc);
				if(warehouse_list.size() > 0) {
					Warehouse warehouse = warehouse_list.get(0);
					// status
					stockout_order_pkl.setWarehouseStatus(warehouse.getStatus());
					if(warehouse.getStatus() == WareHouseStatus.WAREHOUSE_STATUS_UNCHECKED) {
						stockout_order_pkl.setWarehouseStatusString("Chưa tở");
					}
					if(warehouse.getStatus() == WareHouseStatus.WAREHOUSE_STATUS_CHECKED) {
						stockout_order_pkl.setWarehouseStatusString("Đã tở");
					}
					if(warehouse.getStatus() == WareHouseStatus.WAREHOUSE_STATUS_CUT) {
						stockout_order_pkl.setWarehouseStatusString("Đã cắt");
					}
					// met, width
					Long warehouseId = warehouse.getId();
					List<WarehouseCheck> warehouseCheck_list = warehouseCheckService.findBy_Warehouse_StockoutOrder(stockoutorderid_link, warehouseId);
					if(warehouseCheck_list.size() > 0) {
						WarehouseCheck warehouseCheck = warehouseCheck_list.get(0);
						stockout_order_pkl.setWarehouse_check_met_check(warehouseCheck.getMet_check());
						stockout_order_pkl.setWarehouse_check_width_check(warehouseCheck.getWidth_check());
						stockout_order_pkl.setWarehouse_check_met_err(warehouseCheck.getMet_err());
					}
				}
			}
			
			response.data = stockout_order_pkl_list;
			
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
