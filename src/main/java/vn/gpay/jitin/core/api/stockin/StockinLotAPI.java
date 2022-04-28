package vn.gpay.jitin.core.api.stockin;

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
import vn.gpay.jitin.core.security.GpayUser;
import vn.gpay.jitin.core.stockin.IStockInDService;
import vn.gpay.jitin.core.stockin.IStockInPklistService;
import vn.gpay.jitin.core.stockin.IStockInService;
import vn.gpay.jitin.core.stockin.IStockinLotService;
import vn.gpay.jitin.core.stockin.StockIn;
import vn.gpay.jitin.core.stockin.StockInD;
import vn.gpay.jitin.core.stockin.StockInPklist;
//import vn.gpay.jitin.core.stockin.StockInServiceImpl;
import vn.gpay.jitin.core.stockin.StockinLot;
import vn.gpay.jitin.core.utils.ResponseMessage;
import vn.gpay.jitin.core.utils.StockinStatus;

@RestController
@RequestMapping("/api/v1/stockin_lot")
public class StockinLotAPI {
	@Autowired IStockinLotService stockinLotService;
	@Autowired IStockInPklistService stockInPklistService;
	@Autowired IStockInService stockinService;
	@Autowired IStockInDService stockinDService;
	
	@RequestMapping(value = "/stockin_lot_create",method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> stockin_lot_create(@RequestBody StockinLot_create_request entity,HttpServletRequest request ) {
		StockinLot_create_response response = new StockinLot_create_response();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			StockinLot stockinLot = entity.data;
			
			// kiểm tra xem lot đã tồn tại hay chưa, tìm lot theo lot_number và stockindid_link
			if(stockinLot.getId() == null) {
				stockinLot.setOrgrootid_link(user.getRootorgid_link());
				String lot_number = stockinLot.getLot_number();
				Long stockindid_link = stockinLot.getStockindid_link();
				
				List<StockinLot> listStockinLot = stockinLotService.getByStockinD_LOT(stockindid_link, lot_number);
				
				// nếu list > 0 return thông báo
				if(listStockinLot.size() > 0) {
					// return
					response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
					response.setMessage("Số lot đã tồn tại");
					return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
				}
			}
			
			StockinLot responseObj = stockinLotService.save(stockinLot);
			response.data = responseObj;
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<ResponseBase>(response,HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value = "/getById",method = RequestMethod.POST)
//	@Transactional(rollbackFor = RuntimeException.class)
	public ResponseEntity<ResponseBase> getById(@RequestBody StockinLot_getById_request entity,HttpServletRequest request ) {
		StockinLot_getById_response response = new StockinLot_getById_response();
		try {
			Long stockinlotid_link = entity.stockinlotid_link;
			response.data = stockinLotService.findOne(stockinlotid_link);
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
		}catch (Exception e) {
			e.printStackTrace();
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<ResponseBase>(response,HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value = "/delete",method = RequestMethod.POST)
//	@Transactional(rollbackFor = RuntimeException.class)
	public ResponseEntity<ResponseBase> delete(@RequestBody StockinLot_getById_request entity,HttpServletRequest request ) {
		ResponseBase response = new ResponseBase();
		try {
			Long stockinlotid_link = entity.stockinlotid_link;
			StockinLot stockinLot = stockinLotService.findOne(stockinlotid_link);
			Long stockindid_link = stockinLot.getStockindid_link();
			String lot_number = stockinLot.getLot_number();
			
			StockInD stockInD = stockinDService.findOne(stockindid_link);
			Long stockinId = stockInD.getStockinid_link();
			StockIn stockIn = stockinService.findOne(stockinId);
			
			if(stockIn.getStatus() >= StockinStatus.STOCKIN_STATUS_APPROVED) {
				response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
				response.setMessage("Lỗi: Phiếu nhập đã được duyệt");
				return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
			}
			
			// check lot có cây vải chưa, nếu có ko xoá lot và thông báo
			List<StockInPklist> stockInPklist_list = stockInPklistService.getBy_Lot_StockinD(stockindid_link, lot_number);
			if(stockInPklist_list.size() == 0) {
				stockinLotService.delete(stockinLot);
				response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
				response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
				return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
			}else {
				response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
				response.setMessage("Lot này đã tồn tại cây vải");
				return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
			}
			
			
		}catch (Exception e) {
			e.printStackTrace();
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<ResponseBase>(response,HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value = "/update",method = RequestMethod.POST)
//	@Transactional(rollbackFor = RuntimeException.class)
	public ResponseEntity<ResponseBase> update(@RequestBody StockinLot_update_request entity,HttpServletRequest request ) {
		StockinLot_getById_response response = new StockinLot_getById_response();
		try {
			StockinLot stockinLot = entity.stockinLot;
			
			Integer totalpackagecheck = stockinLot.getStockin_lot_space().stream().mapToInt(
					x->null==x.getTotalpackage()?0:x.getTotalpackage()
			).sum();
			stockinLot.setTotalpackagecheck(totalpackagecheck);
			stockinLot = stockinLotService.save(stockinLot);
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
		}catch (Exception e) {
			e.printStackTrace();
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<ResponseBase>(response,HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value = "/getByStockinId",method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> getByStockinId(@RequestBody StockinD_getByStockinId_request entity,HttpServletRequest request ) {
		StockinLot_getByStockinId_response response = new StockinLot_getByStockinId_response();
		try {
			
			response.data = stockinLotService.getByStockinId(entity.stockinid_link);
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<ResponseBase>(response,HttpStatus.BAD_REQUEST);
		}
	}
	@RequestMapping(value = "/getByStockinDId",method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> getByStockinDId(@RequestBody StockinD_getByStockinId_request entity,HttpServletRequest request ) {
		StockinLot_getByStockinId_response response = new StockinLot_getByStockinId_response();
		try {
			
			response.data = stockinLotService.getByStockinDId(entity.stockinid_link);
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<ResponseBase>(response,HttpStatus.BAD_REQUEST);
		}
	}
	@RequestMapping(value = "/stockin_lot_changeSku",method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> stockin_lot_changeSku(@RequestBody StockinLot_create_request entity,HttpServletRequest request ) {
		StockinLot_create_response response = new StockinLot_create_response();
		try {
//			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			StockinLot stockinLot = entity.data;
			Long stockinid_link = stockinLot.getStockinid_link();
			Long stockindid_link_old = stockinLot.getStockindid_link();
			String lot_number = stockinLot.getLot_number();
			Long stockindid_link = entity.stockindid_link;
			
			StockInD stockInD = stockinDService.findOne(stockindid_link);
			
			StockIn stockIn = stockinService.findOne(stockinid_link);
			if(stockIn.getStatus() >= StockinStatus.STOCKIN_STATUS_APPROVED) {
				response.setRespcode(ResponseMessage.KEY_RC_BAD_REQUEST);
				response.setMessage("Phiếu đã được duyệt");
				return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
			}
			
			if(!stockinid_link.equals(stockindid_link_old)) {
				List<StockinLot> listStockinLot = stockinLotService.getByStockinD_LOT(stockindid_link, lot_number);
				if(listStockinLot.size() > 0) {
					response.setRespcode(ResponseMessage.KEY_RC_BAD_REQUEST);
					response.setMessage("Số lot đã tồn tại cho nguyên phụ liệu");
					return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
				}
			}
			
			// update stockin_pkl
			// skuid_link, stockindid_link
			List<StockInPklist> stockInPklist_list = 
					stockInPklistService.getBy_Lot_StockinD(stockindid_link_old, lot_number);
			for(StockInPklist stockInPklist : stockInPklist_list) {
				stockInPklist.setSkuid_link(stockInD.getSkuid_link());
				stockInPklist.setStockindid_link(stockInD.getId());
				stockInPklist = stockInPklistService.save(stockInPklist);
			}
			
			// update stockin_d
			// Update lại số tổng Check trong Stockin_d
			stockinDService.recal_Totalcheck(stockindid_link);
			stockinDService.recal_Totalcheck(stockindid_link_old);

			// update stockin_lot:
			// materialid_link, stockindid_link
			stockinLot.setMaterialid_link(stockInD.getSkuid_link());
			stockinLot.setStockindid_link(stockInD.getId());
			stockinLot = stockinLotService.save(stockinLot);
			
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
