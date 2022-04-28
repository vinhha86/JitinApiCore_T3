package vn.gpay.jitin.core.api.stockin;

import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import vn.gpay.jitin.core.base.ResponseBase;
import vn.gpay.jitin.core.base.ResponseError;
import vn.gpay.jitin.core.security.GpayUser;
import vn.gpay.jitin.core.stock.IStockspaceService;
import vn.gpay.jitin.core.stock.Stockspace;
import vn.gpay.jitin.core.stockin.IStockInPklistService;
import vn.gpay.jitin.core.stockin.IStockInService;
import vn.gpay.jitin.core.stockin.IStockinLotService;
import vn.gpay.jitin.core.stockin.IStockinLotSpaceService;
import vn.gpay.jitin.core.stockin.StockIn;
import vn.gpay.jitin.core.stockin.StockInPklist;
import vn.gpay.jitin.core.stockin.StockinLot;
import vn.gpay.jitin.core.stockin.StockinLotSpace;
import vn.gpay.jitin.core.utils.ResponseMessage;
import vn.gpay.jitin.core.utils.StockinStatus;

@RestController
@RequestMapping("/api/v1/stockin_lot_space")
public class StockinLotSpaceAPI {
	@Autowired IStockinLotSpaceService stockinLotSpaceService;
	@Autowired IStockinLotService stockinLotService;
	@Autowired IStockspaceService stockspaceService;
	@Autowired IStockInService stockinService;
	@Autowired IStockInPklistService stockInPklistService;
	
	@RequestMapping(value = "/stockin_lot_space_create",method = RequestMethod.POST)
	@Transactional(rollbackFor = RuntimeException.class)
	public ResponseEntity<?> stockin_lot_space_create(@RequestBody StockinLotSpace_create_request entity,HttpServletRequest request ) {
		StockinLotSpace_create_response response = new StockinLotSpace_create_response();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			
			Long stockinid_link = entity.stockinid_link;
			Long stockinlotid_link = entity.stockinlotid_link;
		    Integer totalpackage = entity.totalpackage;
		    String spaceepcid_link = entity.spaceepcid_link.toUpperCase();
		    boolean isCreateNew = false;
		    StockIn stockin = stockinService.findOne(stockinid_link);
		    Long orgid_to_link = stockin.getOrgid_to_link();
		    if(orgid_to_link == null) {
		    	ResponseError errorBase = new ResponseError();
	 			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
	 			errorBase.setMessage("Phiếu nhập không có đơn vị nhận");
	 			return new ResponseEntity<>(errorBase, HttpStatus.BAD_REQUEST);
		    }
		    
			 // kiểm tra xem khoang có tồn tại trong db hay không, nếu không tồn tại ko lưu và thông báo
			 // row -> space -> floor (dãy -> tầng -> khoang)
			 // D-1|H-2|T-3|(10)(123);  or DxHxTx
		     // D-1c|H-1|T-1|
//		    spaceepcid_link = spaceepcid_link.replace("|", "/"); System.out.println(spaceepcid_link);
		    String[] spaceepc_linkArr = spaceepcid_link.split(Pattern.quote("|"));
		    String row = spaceepc_linkArr[0].substring(2);
		    String space = spaceepc_linkArr[1].substring(2);
		    String floor = spaceepc_linkArr[2].substring(2);
		    
		    Stockspace stockspace = new Stockspace();
		 	if(
		 			(!row.equals("X") && !space.equals("X") && !floor.equals("X")) &&
		 			(!row.equals("x") && !space.equals("x") && !floor.equals("x"))
		 	) {
		 		// trường hợp user nhập thông tin dãy, hàng và tầng -> kiểm tra bảng stockrow và stockspace
		 		Integer floorInt = Integer.parseInt(floor);
		 		Long orgid_link = user.getOrg_grant_id_link();
//		 		List<Stockspace> listStockspace = stockspaceService.findStockspaceByRowSpaceFloor(row, space, floorInt, orgid_link);
		 		List<Stockspace> listStockspace = stockspaceService.findStockspaceByRowSpaceFloor(row, space, floorInt, orgid_to_link);
//		 		System.out.println(orgid_link);
//		 		System.out.println(orgid_to_link);
		 		
		 		if(listStockspace.size() == 0) { // ko có space này
		 			ResponseError errorBase = new ResponseError();
		 			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
		 			errorBase.setMessage("Khoang này không tồn tại");
		 			return new ResponseEntity<>(errorBase, HttpStatus.BAD_REQUEST);
		 		}else {
		 			stockspace = listStockspace.get(0);
		 		}
			}
		    
		    // Tìm khoang của lot đã tồn tại hay chưa
		    List<StockinLotSpace> listStockinLotSpace = stockinLotSpaceService.getByLotAndSpace(stockinlotid_link, stockspace.getSpaceepc());
		    
		    if(listStockinLotSpace.size() == 0) {
		    	isCreateNew = true;
		    	StockinLotSpace stockinLotSpace = new StockinLotSpace();
		    	stockinLotSpace.setId(null);
		    	stockinLotSpace.setStockinlotid_link(stockinlotid_link);
		    	if(stockspace != null)stockinLotSpace.setSpaceepcid_link(stockspace.getSpaceepc());
		    	stockinLotSpace.setTotalpackage(totalpackage);
		    	stockinLotSpace = stockinLotSpaceService.save(stockinLotSpace);
		    	
		    	// update dãy tầng khoang các cây vải trong lot theo (nếu có)
		    	String lot_spaceepcid_link = stockinLotSpace.getSpaceepcid_link();
		    	StockinLot stockinLot = stockinLotService.findOne(stockinLotSpace.getStockinlotid_link());
		    	Long stockindid_link = stockinLot.getStockindid_link();
		    	List<StockInPklist> stockInPklistList = stockInPklistService.getBy_Lot_StockinD(stockindid_link, stockinLot.getLot_number());
		    	for(StockInPklist stockInPklist : stockInPklistList) {
		    		stockInPklist.setSpaceepc_link(lot_spaceepcid_link);
		    		stockInPklistService.save(stockInPklist);
		    	}
		    }else {
		    	StockinLotSpace stockinLotSpace = listStockinLotSpace.get(0);
		    	totalpackage += stockinLotSpace.getTotalpackage();
		    	stockinLotSpace.setTotalpackage(totalpackage);
		    	stockinLotSpace = stockinLotSpaceService.save(stockinLotSpace);
		    	
		    	// update dãy tầng khoang các cây vải trong lot theo (nếu có)
		    	String lot_spaceepcid_link = stockinLotSpace.getSpaceepcid_link();
		    	StockinLot stockinLot = stockinLotService.findOne(stockinLotSpace.getStockinlotid_link());
		    	Long stockindid_link = stockinLot.getStockindid_link();
		    	List<StockInPklist> stockInPklistList = stockInPklistService.getBy_Lot_StockinD(stockindid_link, stockinLot.getLot_number());
		    	for(StockInPklist stockInPklist : stockInPklistList) {
		    		stockInPklist.setSpaceepc_link(lot_spaceepcid_link);
		    		stockInPklistService.save(stockInPklist);
		    	}
		    }
		    
	    	// update totalpackagecheck của lot
	    	StockinLot stockinLot = stockinLotService.findOne(stockinlotid_link);
	    	List<StockinLotSpace> list = stockinLotSpaceService.getByLot(stockinlotid_link);
	    	
	    	Integer newTotalpackagecheck = 0;
	    	if(isCreateNew) { // nếu là tạo một lotspace mới thì cần điều kiện này vì trong db chưa có lotspace mới
	    		newTotalpackagecheck += totalpackage;
	    	}
	    	for(StockinLotSpace stockinLotSpace : list) {
	    		Integer total = stockinLotSpace.getTotalpackage() == null ? 0 : stockinLotSpace.getTotalpackage();
	    		newTotalpackagecheck += total;
	    	}
	    	stockinLot.setTotalpackagecheck(newTotalpackagecheck);
	    	stockinLot.setStatus(StockinStatus.STOCKIN_LOT_CHECKED);
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
}
