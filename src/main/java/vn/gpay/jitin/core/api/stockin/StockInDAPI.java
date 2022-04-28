package vn.gpay.jitin.core.api.stockin;

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
import vn.gpay.jitin.core.stockin.IStockInDService;
import vn.gpay.jitin.core.stockin.IStockInPklistService;
import vn.gpay.jitin.core.stockin.IStockInService;
import vn.gpay.jitin.core.stockin.IStockinLotService;
import vn.gpay.jitin.core.stockin.StockIn;
import vn.gpay.jitin.core.stockin.StockInD;
import vn.gpay.jitin.core.stockin.StockInPklist;
import vn.gpay.jitin.core.utils.ResponseMessage;
import vn.gpay.jitin.core.utils.StockinStatus;

@RestController
@RequestMapping("/api/v1/stockin_d")
public class StockInDAPI {
	@Autowired IStockInService stockInService;
	@Autowired IStockInDService stockInDService;
	@Autowired IStockInPklistService stockInPklistService;
	@Autowired IStockinLotService stockinLotService;
	
	@RequestMapping(value = "/stockind_delete",method = RequestMethod.POST)
	public ResponseEntity<?> StockinDDelete(@RequestBody StockinByIDRequest entity, HttpServletRequest request ) {
		ResponseBase response = new ResponseBase();
		try { 
			GpayUser user = (GpayUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Long userId = user.getId();
			Date newDate = new Date();
			Long stockInDId = entity.id;
			
			StockInD stockInD = stockInDService.findOne(stockInDId);
			StockIn stockIn = stockInService.findOne(stockInD.getStockinid_link());
			List<StockInPklist> stockin_packinglist = stockInD.getStockin_packinglist();
			
			if(stockin_packinglist.size() > 0) { // có cây -> ko xoá
				ResponseError errorBase = new ResponseError();
				errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
				errorBase.setMessage("Xoá thất bại: Đã chứa packing list.");
			    return new ResponseEntity<>(errorBase, HttpStatus.BAD_REQUEST);
			}
			
			// Xoá StockInPklist
//			for(StockInPklist item : stockin_packinglist) {
//				stockInPklistService.delete(item);
//			}
			// Xoá StockInD
			stockInDService.delete(stockInD);
			
			// Update StockIn
			stockIn.setLasttimeupdate(newDate);
			stockIn.setLastuserupdateid_link(userId);
			stockInService.save(stockIn);
		  
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
	
	@RequestMapping(value = "/getStockinDByStockinId",method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> getStockinDByStockinId(@RequestBody StockinD_getByStockinId_request entity,HttpServletRequest request ) {
		StockinD_getByStockinId_response response = new StockinD_getByStockinId_response();
		try {
			List<StockInD> result = stockInDService.getByStockinId(entity.stockinid_link);
			for(StockInD stockInD : result) {
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
}
