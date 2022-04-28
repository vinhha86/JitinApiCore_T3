package vn.gpay.jitin.core.api.stockout;

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

import vn.gpay.jitin.core.base.ResponseBase;
import vn.gpay.jitin.core.base.ResponseError;
import vn.gpay.jitin.core.security.GpayUser;
import vn.gpay.jitin.core.stockout.IStockOutDService;
import vn.gpay.jitin.core.stockout.IStockOutPklistService;
import vn.gpay.jitin.core.stockout.IStockOutService;
import vn.gpay.jitin.core.stockout.StockOut;
import vn.gpay.jitin.core.stockout.StockOutD;
import vn.gpay.jitin.core.stockout.StockOutPklist;
import vn.gpay.jitin.core.utils.ResponseMessage;
import vn.gpay.jitin.core.utils.StockoutStatus;
import vn.gpay.jitin.core.warehouse.IWarehouseService;

@RestController
@RequestMapping("/api/v1/stockout_d")
public class StockOutDAPI {
	@Autowired IStockOutService stockOutService;
	@Autowired IStockOutDService stockOutDService;
	@Autowired IStockOutPklistService stockOutPklistService;
	@Autowired IWarehouseService warehouseService;
	
	@RequestMapping(value = "/getbystockoutid",method = RequestMethod.POST)
	public ResponseEntity<?> getbystockoutid(@RequestBody stockout_getbyid_request entity, HttpServletRequest request ) {
		StockOutD_response response = new StockOutD_response();
		try { 
			GpayUser user = (GpayUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Long id = entity.id;
			StockOut stockout = stockOutService.findOne(id);
			List<StockOutD> list = stockOutDService.getBystockOutId(id);
			
			for(StockOutD stockOutD : list) {
				//
				Boolean loaiThanhPham1 = false;
				Boolean loaiThanhPham2 = false;
				Boolean loaiThanhPham3 = false;
				String loaiThanhPham = "";
				List<StockOutPklist> stockOutPklist_list = stockOutD.getStockout_packinglist();
				for(StockOutPklist stockOutPklist : stockOutPklist_list) {
					if(stockOutPklist.getStatus().equals(StockoutStatus.TP_LOAI1)) {
						loaiThanhPham1 = true;
					}
					if(stockOutPklist.getStatus().equals(StockoutStatus.TP_LOAI2)) {
						loaiThanhPham2 = true;
					}
					if(stockOutPklist.getStatus().equals(StockoutStatus.TP_LOAI3)) {
						loaiThanhPham3 = true;
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
				
				// stockid_link, skuid_link, 
				Long skuid_link = stockOutD.getSkuid_link();
				Long stockid_link = user.getOrg_grant_id_link();
				
				if(skuid_link != null && stockid_link != null) {
					String data_spaces = warehouseService.getspaces_bysku(stockid_link, skuid_link);
					stockOutD.setData_spaces(data_spaces);
				}
				
				// sl ton kho
				Long orgId = stockout.getOrgid_from_link(); // kho tp
				if(orgId != null) {
					Long totalSLTon = warehouseService.getSumBy_Sku_Stock(stockOutD.getSkuid_link(), orgId);
					stockOutD.setTotalSLTon(totalSLTon.intValue());
				}
			}
			
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
	
	@RequestMapping(value = "/stockoutd_delete",method = RequestMethod.POST)
	public ResponseEntity<?> StockoutDDelete(@RequestBody stockout_getbyid_request entity, HttpServletRequest request ) {
		StockOut_Create_response response = new StockOut_Create_response();
		try { 
			GpayUser user = (GpayUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Long userId = user.getId();
			Date newDate = new Date();
			Long stockOutDId = entity.id;
			
			StockOutD stockOutD = stockOutDService.findOne(stockOutDId);
			StockOut stockOut = stockOutService.findOne(stockOutD.getStockoutid_link());
			List<StockOutPklist> stockout_packinglist = stockOutD.getStockout_packinglist();
			
			if(stockout_packinglist.size() > 0) { // có cây -> ko xoá
				ResponseError errorBase = new ResponseError();
				errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
				errorBase.setMessage("Xoá thất bại: Đã chứa packing list.");
			    return new ResponseEntity<>(errorBase, HttpStatus.BAD_REQUEST);
			}
			
			// Xoá StockOutPklist
//			for(StockOutPklist item : stockout_packinglist) {
//				stockOutPklistService.delete(item);
//			}
			// Xoá StockOutD
			stockOutDService.delete(stockOutD);
			
			// Update StockOut
			stockOut.setLasttimeupdate(newDate);
			stockOut.setLastuserupdateid_link(userId);
			stockOutService.save(stockOut);
		  
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
}
