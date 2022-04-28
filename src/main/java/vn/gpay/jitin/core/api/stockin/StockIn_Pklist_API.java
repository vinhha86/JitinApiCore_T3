package vn.gpay.jitin.core.api.stockin;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;
//import java.util.concurrent.ThreadLocalRandom;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import vn.gpay.jitin.core.utils.ColumnStockinPackinglist;
import vn.gpay.jitin.core.utils.Common;
import vn.gpay.jitin.core.base.ResponseBase;
import vn.gpay.jitin.core.base.ResponseError;
import vn.gpay.jitin.core.product.IProductService;
import vn.gpay.jitin.core.product.Product;
import vn.gpay.jitin.core.rfprint_stockin.IRFPrint_StockinService;
import vn.gpay.jitin.core.rfprint_stockin.RFPrint_Stockin;
import vn.gpay.jitin.core.rfprint_stockin.RFPrint_Stockin_D;
import vn.gpay.jitin.core.security.GpayAuthentication;
import vn.gpay.jitin.core.security.GpayUser;
import vn.gpay.jitin.core.sku.ISKU_Service;
import vn.gpay.jitin.core.sku.SKU;
import vn.gpay.jitin.core.stock.IStockrowService;
import vn.gpay.jitin.core.stock.IStockspaceService;
import vn.gpay.jitin.core.stock.Stockrow;
import vn.gpay.jitin.core.stock.Stockspace;
import vn.gpay.jitin.core.stockin.IStockInDService;
import vn.gpay.jitin.core.stockin.IStockInPklistService;
import vn.gpay.jitin.core.stockin.IStockInService;
import vn.gpay.jitin.core.stockin.IStockinLotService;
import vn.gpay.jitin.core.stockin.StockIn;
import vn.gpay.jitin.core.stockin.StockInD;
import vn.gpay.jitin.core.stockin.StockInPklist;
import vn.gpay.jitin.core.stockin.StockinLot;
import vn.gpay.jitin.core.stockin.StockinProduct;
import vn.gpay.jitin.core.utils.RFPrintStockinStatus;
import vn.gpay.jitin.core.utils.ResponseMessage;
import vn.gpay.jitin.core.utils.StockinStatus;
//import vn.gpay.epc.TexRoCoding;
import vn.gpay.jitin.core.utils.commonUnit;

@RestController
@RequestMapping("/api/v1/stockin_pklist")
public class StockIn_Pklist_API {
	@Autowired
	Common commonService;
	@Autowired IStockInService stockInService;
	@Autowired IStockInDService stockInDService;
	@Autowired IStockinLotService stockInLOTService;
	@Autowired IStockInPklistService stockInPklistService;
	@Autowired ISKU_Service skuService;
	@Autowired IRFPrint_StockinService rfprintService;
	@Autowired IProductService productService;
	@Autowired IStockspaceService stockspaceService;
	@Autowired IStockrowService stockrowService;
	
	@RequestMapping(value = "/pklist_create",method = RequestMethod.POST)
//	@Transactional(rollbackFor = RuntimeException.class)
	public ResponseEntity<?> Pklist_Create(@RequestBody Stockin_PklistCreateRequest entity, HttpServletRequest request ) {
		Stockin_Pklist_create_response response = new Stockin_Pklist_create_response();
		try {
//			GpayAuthentication user = (GpayAuthentication)SecurityContextHolder.getContext().getAuthentication();
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			StockInPklist pklist = entity.data;
			pklist.setOrgrootid_link(user.getRootorgid_link());
			Long stockinId = pklist.getStockinid_link();
			StockIn stockin = stockInService.findOne(stockinId);
			
			// Kiểm tra số lot có tồn tại hay không
			String lotnumber = pklist.getLotnumber();
			Long stockindid_link = pklist.getStockindid_link();
			List<StockinLot> stockinLot_list = stockInLOTService.getByStockinD_LOT(stockindid_link, lotnumber);
			if(stockinLot_list.size() == 0) {
				//Bao loi
				ResponseError errorBase = new ResponseError();
				errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
				errorBase.setMessage("Số LOT không tồn tại");
			    return new ResponseEntity<>(errorBase, HttpStatus.BAD_REQUEST);
			}
			
			// kiểm tra xem khoang có tồn tại trong db hay không, nếu không tồn tại ko lưu và thông báo
			// row -> space -> floor (dãy -> hàng -> tầng)
			// D-1|H-2|T-3| or or DxHxTx
			
			if(
				pklist.getRow() != null && pklist.getSpace() != null && pklist.getFloor() != null
			) {
				// trường hợp user nhập thông tin dãy, hàng và tầng -> kiểm tra bảng stockrow và stockspace
				String row = pklist.getRow();
				String space = pklist.getSpace();
				Integer floor = pklist.getFloor();
//				Long orgid_link = user.getOrg_grant_id_link();
				Long orgid_to_link = stockin.getOrgid_to_link();
				
				if(orgid_to_link == null) {
			    	ResponseError errorBase = new ResponseError();
			    	errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
		 			errorBase.setMessage("Phiếu nhập không có đơn vị nhận");
		 			return new ResponseEntity<>(errorBase, HttpStatus.BAD_REQUEST);
			    }
				
				List<Stockspace> listStockspace = stockspaceService.findStockspaceByRowSpaceFloor(row, space, floor, orgid_to_link);
				if(listStockspace.size() == 0) { // ko có space này
					ResponseError errorBase = new ResponseError();
					errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
					errorBase.setMessage("Khoang này không tồn tại");
				    return new ResponseEntity<>(errorBase, HttpStatus.BAD_REQUEST);
				}else {
					// có space này
					Stockspace stockspace = listStockspace.get(0);
					pklist.setSpaceepc_link(stockspace.getSpaceepc());
				}
			}else if(pklist.getRow() == null && pklist.getSpace() == null && pklist.getFloor() == null){
				pklist.setSpaceepc_link(null);
			}
			
			String oldLotnumber = null;
			if(null == pklist.getId()) {//Item mới trong Stockin
				//Kiểm tra xem có trùng Lot, Packageid trong 1 Mã vải ko
				if (stockInPklistService.checkFabricRollExisted(pklist.getStockindid_link(), pklist.getLotnumber(), pklist.getPackageid())){
					//Bao loi
					ResponseError errorBase = new ResponseError();
					errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
					errorBase.setMessage("Số LOT + Số cây đã tồn tại");
				    return new ResponseEntity<>(errorBase, HttpStatus.BAD_REQUEST);
				}
				
    			pklist.setId(null);
    			pklist.setUsercreateid_link(user.getId());
    			pklist.setTimecreate(new Date());
    			pklist.setLasttimeupdate(new Date());
    			
    			//Neu ko co EPC --> Sinh EPC
//    			String skuToGen = pklist.getStockinid_link().toString() + "-" + pklist.getLotnumber().replace("-", "") + "-" + pklist.getPackageid().toString();
//    			String skuToGen = pklist.getStockinid_link().toString() + "-" + String.valueOf(ThreadLocalRandom.current().nextLong(10000)) + "-" + pklist.getPackageid().toString();
//    			pklist.setEpc(genEPC(skuToGen));
    			pklist.setEpc(genUUID());
			}else { // update
				//Kiểm tra xem có trùng Lot, Packageid trong 1 Mã vải ko
				if (stockInPklistService.checkFabricRollExisted_other(pklist.getStockindid_link(), pklist.getLotnumber(), pklist.getPackageid(), pklist.getId())){
					//Bao loi
					ResponseError errorBase = new ResponseError();
					errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
					errorBase.setMessage("Số LOT + Số cây đã tồn tại");
				    return new ResponseEntity<>(errorBase, HttpStatus.BAD_REQUEST);
				}
				
				StockInPklist oldStockInPklist = stockInPklistService.findOne(pklist.getId());
				Integer oldStatus = oldStockInPklist.getStatus();
				if(oldStatus.equals(StockinStatus.STOCKIN_EPC_STATUS_ERR)) {
					oldStatus = StockinStatus.STOCKIN_EPC_STATUS_CHECK_LABEL;
				}
				
				pklist.setEpc(oldStockInPklist.getEpc());
				pklist.setUsercreateid_link(oldStockInPklist.getUsercreateid_link());
				pklist.setTimecreate(oldStockInPklist.getTimecreate());
				pklist.setStatus(oldStatus);
				pklist.setLasttimeupdate(new Date());
				pklist.setLastuserupdateid_link(user.getId());
				
				// nếu thay đổi số lotnumber thì lưu số lot number cũ để tính lại totalpackage stockin_lot
				if(!oldStockInPklist.getLotnumber().equals(pklist.getLotnumber())) {
					oldLotnumber = oldStockInPklist.getLotnumber();
				}
			}
			
			//Lưu thông tin Pklist
			if(pklist.getStatus() == null) pklist.setStatus(StockinStatus.STOCKIN_EPC_STATUS_CHECK_LABEL);
			pklist = stockInPklistService.save(pklist);
			
			//Update lại số tổng Check trong Stockin_d
			stockInDService.recal_Totalcheck(pklist.getStockindid_link());
			
			//Update lại số tổng Chẹck của Stockin_LOT
			stockInLOTService.recal_Totalcheck(pklist.getStockindid_link(), pklist.getLotnumber());
			if(oldLotnumber != null) { // tính lại totalpackage stockin_lot
				stockInLOTService.recal_Totalcheck(pklist.getStockindid_link(), oldLotnumber);
			}
			
			//Nếu có yêu cầu in Tem
			if (entity.isprintlabel){
				if (null != pklist.getEpc() && pklist.getEpc().length() >= 0){
					//Lấy thông tin của Material từ SKU
					SKU theSKU_Mat = skuService.findOne(pklist.getSkuid_link());
					StockIn theStockin = stockInService.findOne(pklist.getStockinid_link());
					if (null!=theSKU_Mat){
						
					//Tạo phiên in ấn
						RFPrint_Stockin thePrintSession = new RFPrint_Stockin();
						thePrintSession.setRfid_enable(entity.rfid_enable);
						thePrintSession.setStockinid_link(pklist.getStockinid_link());
						thePrintSession.setTotal_req(1);
						thePrintSession.setStatus(RFPrintStockinStatus.RFPRINT_STOCKIN_STATUS_NEW);
						thePrintSession.setUsercreatedid_link(user.getId());
						thePrintSession.setTimecreated(new Date());
						
						RFPrint_Stockin_D thePrint_D = new RFPrint_Stockin_D();
						thePrint_D.setInv_code(theStockin.getInvoice_number().length() > 11?theStockin.getInvoice_number().substring(0,10):theStockin.getInvoice_number());
						
						//Lấy mã sản phẩm đầu tiên trong danh sách sản phẩm của Phiếu nhập nguyên liệu
						if (theStockin.getStockin_product().size() > 0){
							StockinProduct theStockinproduct = theStockin.getStockin_product().get(0);
							Product theProduct  = productService.findOne(theStockinproduct.getProductid_link());
							if (null!=theProduct){
								thePrint_D.setProductid_link(theProduct.getId());
								thePrint_D.setProduct_code(theProduct.getBuyercode().length() >9?theProduct.getBuyercode().substring(0,9):theProduct.getBuyercode());
							}
						}
						thePrint_D.setMaterial_id_link(theSKU_Mat.getId());
						thePrint_D.setMaterial_code(theSKU_Mat.getProduct_managecode().length()>10?theSKU_Mat.getProduct_managecode().substring(0,10):theSKU_Mat.getProduct_managecode());
						thePrint_D.setMaterial_provider_code(theSKU_Mat.getProduct_code().length()>12?theSKU_Mat.getProduct_code().substring(0,12):theSKU_Mat.getProduct_code());
						//Màu của nguyên liệu, nguyên liệu không chia SKU theo màu và size, chỉ có 1 SKU ALL và màu là thuộc tính của product
						thePrint_D.setColor_name(theSKU_Mat.getProduct_color().length()>25?theSKU_Mat.getProduct_color().substring(0,25):theSKU_Mat.getProduct_color());
						
						thePrint_D.setEpc(pklist.getEpc());
						
						//Cắt Desc
						if (null!=theSKU_Mat.getProduct_desc() && theSKU_Mat.getProduct_desc().length() > 22){
							thePrint_D.setDesc1(theSKU_Mat.getProduct_desc().substring(0,21));
							thePrint_D.setDesc2(theSKU_Mat.getProduct_desc().substring(22,theSKU_Mat.getProduct_desc().length()));
						} else {
							thePrint_D.setDesc1(theSKU_Mat.getProduct_desc());
						}
						
						thePrint_D.setLotnumber(pklist.getLotnumber());
						thePrint_D.setPackageid(pklist.getPackageid().toString());
						thePrint_D.setMet(pklist.getMet_check().toString());
						thePrint_D.setWidth(pklist.getWidth_met_check().toString());
						thePrint_D.setPrinttime(null);
						
						
						thePrintSession.getRfprint_d().add(thePrint_D);
						
						thePrintSession = rfprintService.save(thePrintSession);
						response.rfprintid_link = thePrintSession.getId();
					//Trả về mã phiên
					} else {
						//Bao loi
						ResponseError errorBase = new ResponseError();
						errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
						errorBase.setMessage("SKU không tồn tại! Liên hệ IT để được hỗ trợ");
					    return new ResponseEntity<>(errorBase, HttpStatus.BAD_REQUEST);
					}
				}
			} 

			response.id = pklist.getId();
			
			//Update lại trạng thái của Stockin
			stockInService.stockin_update_status(pklist.getStockinid_link(), StockinStatus.STOCKIN_STATUS_OK);
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);	

		}catch (RuntimeException e) {
			e.printStackTrace();
//			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(null==e.getMessage()?"Lỗi hệ thống! Liên hệ IT để được hỗ trợ":e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value = "/pklist_print",method = RequestMethod.POST)
//	@Transactional(rollbackFor = RuntimeException.class)
	public ResponseEntity<?> Pklist_Print(@RequestBody Stockin_PklistCreateRequest entity, HttpServletRequest request ) {
		Stockin_Pklist_create_response response = new Stockin_Pklist_create_response();
		try {
			GpayAuthentication user = (GpayAuthentication)SecurityContextHolder.getContext().getAuthentication();
			StockInPklist pklist = entity.data;
			if(null != pklist.getId()) {//Item mới trong Stockin
				if (null == stockInPklistService.findOne(pklist.getId())){
					//Bao loi
					ResponseError errorBase = new ResponseError();
					errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
					errorBase.setMessage("Dòng hàng không tồn tại! Liên hệ IT để được hỗ trợ");
				    return new ResponseEntity<>(errorBase, HttpStatus.BAD_REQUEST);
				}
			}
			
			//Nếu có yêu cầu in Tem
			if (entity.isprintlabel){
				if (null != pklist.getEpc() && pklist.getEpc().length() >= 0){
					//Lấy thông tin của Material từ SKU
					SKU theSKU_Mat = skuService.findOne(pklist.getSkuid_link());
					StockIn theStockin = stockInService.findOne(pklist.getStockinid_link());
					if (null!=theSKU_Mat){
						
					//Tạo phiên in ấn
						RFPrint_Stockin thePrintSession = new RFPrint_Stockin();
						thePrintSession.setStockinid_link(pklist.getStockinid_link());
						thePrintSession.setTotal_req(1);
						thePrintSession.setStatus(RFPrintStockinStatus.RFPRINT_STOCKIN_STATUS_NEW);
						thePrintSession.setUsercreatedid_link(user.getUserId());
						thePrintSession.setTimecreated(new Date());
						
						RFPrint_Stockin_D thePrint_D = new RFPrint_Stockin_D();
						thePrint_D.setInv_code(theStockin.getInvoice_number().length() > 11?theStockin.getInvoice_number().substring(0,10):theStockin.getInvoice_number());
						
						//Lấy mã sản phẩm đầu tiên trong danh sách sản phẩm của Phiếu nhập nguyên liệu
						if (theStockin.getStockin_product().size() > 0){
							StockinProduct theStockinproduct = theStockin.getStockin_product().get(0);
							Product theProduct  = productService.findOne(theStockinproduct.getProductid_link());
							if (null!=theProduct){
								thePrint_D.setProductid_link(theProduct.getId());
								thePrint_D.setProduct_code(theProduct.getBuyercode());
							}
						}
						thePrint_D.setMaterial_id_link(theSKU_Mat.getId());
						thePrint_D.setMaterial_code(theSKU_Mat.getProduct_managecode().length()>10?theSKU_Mat.getProduct_managecode().substring(0,10):theSKU_Mat.getProduct_managecode());
						thePrint_D.setMaterial_provider_code(theSKU_Mat.getProduct_code().length()>12?theSKU_Mat.getProduct_code().substring(0,12):theSKU_Mat.getProduct_code());
						//Màu của nguyên liệu, nguyên liệu không chia SKU theo màu và size, chỉ có 1 SKU ALL và màu là thuộc tính của product
						thePrint_D.setColor_name(theSKU_Mat.getProduct_color());
						
						thePrint_D.setEpc(pklist.getEpc());
						
						//Cắt Desc
						if (null!=theSKU_Mat.getProduct_desc() && theSKU_Mat.getProduct_desc().length() > 22){
							thePrint_D.setDesc1(theSKU_Mat.getProduct_desc().substring(0,21));
							thePrint_D.setDesc2(theSKU_Mat.getProduct_desc().substring(22,theSKU_Mat.getProduct_desc().length()));
						} else {
							thePrint_D.setDesc1(theSKU_Mat.getProduct_desc());
						}
						
						thePrint_D.setLotnumber(pklist.getLotnumber());
						thePrint_D.setPackageid(pklist.getPackageid().toString());
						thePrint_D.setMet(pklist.getMet_check().toString());
						thePrint_D.setWidth(pklist.getWidth_met_check().toString());
						thePrint_D.setPrinttime(null);
						
						
						thePrintSession.getRfprint_d().add(thePrint_D);
						
						thePrintSession = rfprintService.save(thePrintSession);
						response.rfprintid_link = thePrintSession.getId();
					//Trả về mã phiên
					} else {
						//Bao loi
						ResponseError errorBase = new ResponseError();
						errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
						errorBase.setMessage("SKU không tồn tại! Liên hệ IT để được hỗ trợ");
					    return new ResponseEntity<>(errorBase, HttpStatus.BAD_REQUEST);
					}
				}
			} 

			response.id = pklist.getId();
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);	

		}catch (RuntimeException e) {
			e.printStackTrace();
//			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(null==e.getMessage()?"Lỗi hệ thống! Liên hệ IT để được hỗ trợ":e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.BAD_REQUEST);
		}
	}	
//	private String genEPC(String skuToGen){
//		String theEPC = "";
//		theEPC = TexRoCoding.encode(skuToGen);
//		return theEPC;
//	}
	private String genUUID(){
		UUID uuid = UUID.randomUUID();
        return uuid.toString();
	}
	
	@RequestMapping(value = "/getByStockinDIdAndGreaterThanStatus",method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> getByStockinDIdAndGreaterThanStatus(@RequestBody StockinPkl_getByStockinDIdAndStatus_request entity,HttpServletRequest request ) {
		StockinPkl_response response = new StockinPkl_response();
		try {
			List<StockInPklist> result = stockInPklistService.getStockinPklByStockinDIdAndGreaterThanStatus(
					entity.stockindid_link, entity.lotnumber, entity.status)
					;
			
			for(StockInPklist item : result) {
				Long stockindid_link = item.getStockindid_link();
				StockInD stockind = stockInDService.findOne(stockindid_link);
				Long stockinid_link = stockind.getStockinid_link();
				StockIn stockin = stockInService.findOne(stockinid_link);
				Long orgid = stockin.getOrgid_to_link();
				String spaceepc = item.getSpaceepc_link();
				List<Stockspace> stockspace_list = stockspaceService.findStockspaceByEpc(orgid, spaceepc);
				if(stockspace_list.size() > 0) {
					Stockspace stockspace = stockspace_list.get(0);
					Stockrow stockrow = stockrowService.findOne(stockspace.getRowid_link());
					item.setRow(stockrow.getCode());
					item.setSpace(stockspace.getSpacename());
					item.setFloor(stockspace.getFloorid());
				}
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
	
	@RequestMapping(value = "/getByStockinDIdAndEqualStatus",method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> getByStockinDIdAndEqualStatus(@RequestBody StockinPkl_getByStockinDIdAndStatus_request entity,HttpServletRequest request ) {
		StockinPkl_response response = new StockinPkl_response();
		try {
			List<StockInPklist> result = stockInPklistService.getStockinPklByStockinDIdAndEqualStatus(
					entity.stockindid_link, entity.lotnumber, entity.status
					);
			
			for(StockInPklist item : result) {
				Long stockindid_link = item.getStockindid_link();
				StockInD stockind = stockInDService.findOne(stockindid_link);
				Long stockinid_link = stockind.getStockinid_link();
				StockIn stockin = stockInService.findOne(stockinid_link);
				Long orgid = stockin.getOrgid_to_link();
				String spaceepc = item.getSpaceepc_link();
				List<Stockspace> stockspace_list = stockspaceService.findStockspaceByEpc(orgid, spaceepc);
				if(stockspace_list.size() > 0) {
					Stockspace stockspace = stockspace_list.get(0);
					Stockrow stockrow = stockrowService.findOne(stockspace.getRowid_link());
					item.setRow(stockrow.getCode());
					item.setSpace(stockspace.getSpacename());
					item.setFloor(stockspace.getFloorid());
				}
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
	
	@RequestMapping(value = "/getByStockinDLotAndPackageId",method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> getByStockinDLotAndPackageId(@RequestBody StockinPkl_getByStockinDLotAndPackageId_request entity,HttpServletRequest request ) {
		StockinPkl_response response = new StockinPkl_response();
		try {
			
//			response.data = stockInPklistService.getStockinPklByStockinDLotAndPackageId(entity.stockindid_link, entity.lotnumber, entity.packageid);
			response.data = stockInPklistService.getStockinPklByStockinDLotAndPackageIdNotCheck10(entity.stockindid_link, entity.lotnumber, entity.packageid);
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<ResponseBase>(response,HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value = "/update",method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> update(@RequestBody Stockin_PklistCreateRequest entity,HttpServletRequest request ) {
		Stockin_Pklist_create_response response = new Stockin_Pklist_create_response();
		try {
			GpayAuthentication user = (GpayAuthentication)SecurityContextHolder.getContext().getAuthentication();
			StockInPklist stockInPklist = entity.data;
			Long id = stockInPklist.getId();
			Long stockindid_link = stockInPklist.getStockindid_link();
			String lotnumber = stockInPklist.getLotnumber();
			Integer packageid = stockInPklist.getPackageid();
			
			// Tìm trong db xem đã có cây vải khác có thông tin lot và packageid này hay chưa
			List<StockInPklist> stockInPklist_list = stockInPklistService.getBy_Lot_Packageid_and_notId(
					stockindid_link, lotnumber, packageid, id
					);
			
			if(stockInPklist_list.size() == 0) { // nhảy vào đây
				StockInPklist oldStockInPklist = stockInPklistService.findOne(id);
				
				if(stockInPklist.getStatus() < StockinStatus.STOCKIN_EPC_STATUS_CHECK_10PERCENT) {
					stockInPklist.setYds_beforecheck(oldStockInPklist.getYdscheck());
					stockInPklist.setMet_beforecheck(oldStockInPklist.getMet_check());
					stockInPklist.setWidth_yds_beforecheck(oldStockInPklist.getWidth_yds_check());
					stockInPklist.setWidth_met_beforecheck(oldStockInPklist.getWidth_met_check());
					stockInPklist.setGrossweight_beforecheck(oldStockInPklist.getGrossweight_check());
				}
				
				stockInPklist.setLasttimeupdate(new Date());
				stockInPklist.setLastuserupdateid_link(user.getUserId());
				stockInPklist.setStatus(StockinStatus.STOCKIN_EPC_STATUS_CHECK_10PERCENT);
				stockInPklistService.save(stockInPklist);
				
				//Update lại số tổng Check trong Stockin_d
				stockInDService.recal_Totalcheck(stockInPklist.getStockindid_link());
				
				//Update lại số tổng Chẹck của Stockin_LOT
				stockInLOTService.recal_Totalcheck(stockInPklist.getStockindid_link(), stockInPklist.getLotnumber());
			}else { // ko nhảy vào đây
				// đã có cây vải khác có lotnumber, packageid này của cùng StockinD
				response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
				response.setMessage("Đã tồn tại cây vải khác có lot và packageid này");
				return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
			}
			
			response.data = stockInPklist;
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
	public ResponseEntity<?> getByStockinDId(@RequestBody StockinByIDRequest entity,HttpServletRequest request ) {
		StockinPkl_response response = new StockinPkl_response();
		try {
			response.data = stockInPklistService.getByStockinDId(entity.id);
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<StockinPkl_response>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<StockinPkl_response>(response,HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value = "/pklist_delete",method = RequestMethod.POST)
//	@Transactional(rollbackFor = RuntimeException.class)
	public ResponseEntity<?> Pklist_Delete(@RequestBody StockinByIDRequest entity, HttpServletRequest request ) {
		Stockin_Pklist_create_response response = new Stockin_Pklist_create_response();
		try {
//			GpayAuthentication user = (GpayAuthentication)SecurityContextHolder.getContext().getAuthentication();
			Long id = entity.id;
			StockInPklist stockInPklist = stockInPklistService.findOne(id);
			Long stockInDId = stockInPklist.getStockindid_link();
			String lotnumber = stockInPklist.getLotnumber();
			
			// delete pkl
			stockInPklistService.delete(stockInPklist);
			
			//Update lại số tổng Check trong Stockin_d
			stockInDService.recal_Totalcheck(stockInDId);
			
			//Update lại số tổng Chẹck của Stockin_LOT
			stockInLOTService.recal_Totalcheck(stockInDId, lotnumber);
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			e.printStackTrace();
//			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(null==e.getMessage()?"Lỗi hệ thống! Liên hệ IT để được hỗ trợ":e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value = "/pklist_delete_check10",method = RequestMethod.POST)
//	@Transactional(rollbackFor = RuntimeException.class)
	public ResponseEntity<?> pklist_delete_check10(@RequestBody StockinByIDRequest entity, HttpServletRequest request ) {
		Stockin_Pklist_create_response response = new Stockin_Pklist_create_response();
		try {
//			GpayAuthentication user = (GpayAuthentication)SecurityContextHolder.getContext().getAuthentication();
			Long id = entity.id;
			StockInPklist stockInPklist = stockInPklistService.findOne(id);
			Long stockInDId = stockInPklist.getStockindid_link();
			String lotnumber = stockInPklist.getLotnumber();
			
			// set pkl
//			stockInPklistService.delete(stockInPklist);
			stockInPklist.setStatus(StockinStatus.STOCKIN_EPC_STATUS_CHECK_LABEL);
			stockInPklist.setMet_check(stockInPklist.getMet_beforecheck());
			stockInPklist.setYdscheck(stockInPklist.getYds_beforecheck());
			stockInPklist.setGrossweight_check(stockInPklist.getGrossweight_beforecheck());
			stockInPklist.setGrossweight_lbs_check(stockInPklist.getGrossweight_beforecheck() * (float)2.20462);
			stockInPklist.setWidth_met_check(stockInPklist.getWidth_met_beforecheck());
			stockInPklist.setWidth_yds_check(stockInPklist.getWidth_yds_beforecheck());
			
			stockInPklistService.save(stockInPklist);
			
			//Update lại số tổng Check trong Stockin_d
			stockInDService.recal_Totalcheck(stockInDId);
			
			//Update lại số tổng Chẹck của Stockin_LOT
			stockInLOTService.recal_Totalcheck(stockInDId, lotnumber);
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			e.printStackTrace();
//			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(null==e.getMessage()?"Lỗi hệ thống! Liên hệ IT để được hỗ trợ":e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value = "/pklist_delete_stockin_packinglist_detail",method = RequestMethod.POST)
//	@Transactional(rollbackFor = RuntimeException.class)
	public ResponseEntity<?> pklist_delete_stockin_packinglist_detail(@RequestBody StockinByIDRequest entity, HttpServletRequest request ) {
		Stockin_Pklist_create_response response = new Stockin_Pklist_create_response();
		try {
//			GpayAuthentication user = (GpayAuthentication)SecurityContextHolder.getContext().getAuthentication();
			Long id = entity.id;
			StockInPklist stockInPklist = stockInPklistService.findOne(id);
			Long stockInDId = stockInPklist.getStockindid_link();
			String lotnumber = stockInPklist.getLotnumber();
			
			StockInD stockInD = stockInDService.findOne(stockInDId);
			Long stockinid = stockInD.getStockinid_link();
			StockIn stockIn = stockInService.findOne(stockinid);
			
			if(stockIn.getStatus() >= StockinStatus.STOCKIN_STATUS_APPROVED) {
				response.setRespcode(ResponseMessage.KEY_RC_BAD_REQUEST);
				response.setMessage("Lỗi: Phiếu nhập đã được duyệt.");
				return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
			}
			
			// delete pkl
			stockInPklistService.delete(stockInPklist);
			
			//Update lại số tổng Check trong Stockin_d
			stockInDService.recal_Totalcheck(stockInDId);
			
			//Update lại số tổng Chẹck của Stockin_LOT
			stockInLOTService.recal_Totalcheck(stockInDId, lotnumber);
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			e.printStackTrace();
//			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(null==e.getMessage()?"Lỗi hệ thống! Liên hệ IT để được hỗ trợ":e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value = "/lot_rollnotcheck_info",method = RequestMethod.POST)
//	@Transactional(rollbackFor = RuntimeException.class)
	public ResponseEntity<?> lot_rollnotcheck_info(@RequestBody StockinPkl_getByStockinDLotAndPackageId_request entity, HttpServletRequest request ) {
		stockin_lot_rollnotcheck_info_response response = new stockin_lot_rollnotcheck_info_response();
		try {
//			Long stockinid_link = entity.stockinid_link;
			Long stockindid_link = entity.stockindid_link;
			String lotnumber = entity.lotnumber;
			
			Integer totalpackage = 0;
			String list_not_check = "";
			
			List<StockinLot> stockinLot_list = stockInLOTService.getByStockinD_LOT(stockindid_link, lotnumber);
			if(stockinLot_list.size() > 0) {
				StockinLot stockinLot = stockinLot_list.get(0);
				totalpackage = stockinLot.getTotalpackage();
				
				List<StockInPklist> pkl_list = stockInPklistService.getBy_Lot_StockinD(stockindid_link, lotnumber);
				List<Integer> packageid_list = new ArrayList<Integer>();
				List<Integer> packageid_list_notcheck = new ArrayList<Integer>();
				
				for(StockInPklist pkl : pkl_list) {
					packageid_list.add(pkl.getPackageid());
					if(pkl.getStatus().equals(StockinStatus.STOCKIN_EPC_STATUS_ERR)) {
						packageid_list_notcheck.add(pkl.getPackageid());
					}
				}
				Collections.sort(packageid_list);
				
				if(stockinLot.getIs_upload() == null || !stockinLot.getIs_upload()) {
					for(Integer n = 1; n <= totalpackage; n++) {
						if(!packageid_list.contains(n)) {
							packageid_list_notcheck.add(n);
						}
					}
				}
				
				Collections.sort(packageid_list_notcheck);
				for(Integer n : packageid_list_notcheck) {
					if(list_not_check.equals("")) {
						list_not_check+=n;
					}else {
						list_not_check+="; " + n;
					}
				}

//				for(Integer n = 1; n <= totalpackage; n++) {
//					if(!packageid_list.contains(n)) {
//						if(list_not_check.equals("")) {
//							list_not_check+=n;
//						}else {
//							list_not_check+="; " + n;
//						}
//					}
//				}
				
				
			}
			response.total = totalpackage;
			response.list_not_check = list_not_check;
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			e.printStackTrace();
//			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(null==e.getMessage()?"Lỗi hệ thống! Liên hệ IT để được hỗ trợ":e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.BAD_REQUEST);
		}
	}@RequestMapping(value = "/getbyStockinID_StockinDID",method = RequestMethod.POST)
//	@Transactional(rollbackFor = RuntimeException.class)
	public ResponseEntity<?> getbyStockinID_StockinDID(@RequestBody StockinPkl_getByStockinDLotAndPackageId_request entity, HttpServletRequest request ) {
		StockinD_getByStockinId_response response = new StockinD_getByStockinId_response();
		try {
			Long stockinid_link = entity.stockinid_link;
			Long stockindid_link = entity.stockindid_link;
			
			List<StockInD> stockinD_list = stockInDService.getbyStockinID_StockinDID(stockinid_link, stockindid_link);
			
			for(StockInD stockInD : stockinD_list) {
				List<StockinLot> lot_list = stockInD.getStockin_lot();
				for(StockinLot stockinLot : lot_list) {
					Integer totalpackage = stockinLot.getTotalpackage();
					String list_not_check = "";
					String lotnumber = stockinLot.getLot_number();
					
					List<StockInPklist> pkl_list = stockInPklistService.getBy_Lot_StockinD(stockInD.getId(), lotnumber);
					List<Integer> packageid_list = new ArrayList<Integer>();
					List<Integer> packageid_list_notcheck = new ArrayList<Integer>();
					
					for(StockInPklist pkl : pkl_list) {
						packageid_list.add(pkl.getPackageid());
						if(pkl.getStatus().equals(StockinStatus.STOCKIN_EPC_STATUS_ERR)) {
							packageid_list_notcheck.add(pkl.getPackageid());
						}
						
						StockInD stockind = stockInDService.findOne(pkl.getStockindid_link());
						StockIn stockin = stockInService.findOne(stockind.getStockinid_link());
						Long orgid = stockin.getOrgid_to_link();
						String spaceepc = pkl.getSpaceepc_link();
						List<Stockspace> stockspace_list = stockspaceService.findStockspaceByEpc(orgid, spaceepc);
						if(stockspace_list.size() > 0) {
							Stockspace stockspace = stockspace_list.get(0);
							Stockrow stockrow = stockrowService.findOne(stockspace.getRowid_link());
							pkl.setRow(stockrow.getCode());
							pkl.setSpace(stockspace.getSpacename());
							pkl.setFloor(stockspace.getFloorid());
						}
					}
					Collections.sort(packageid_list);

					if(stockinLot.getIs_upload() == null || !stockinLot.getIs_upload()) {
						for(Integer n = 1; n <= totalpackage; n++) {
							if(!packageid_list.contains(n)) {
								packageid_list_notcheck.add(n);
							}
						}
					}
					
					Collections.sort(packageid_list_notcheck);
					for(Integer n : packageid_list_notcheck) {
						if(list_not_check.equals("")) {
							list_not_check+=n;
						}else {
							list_not_check+="; " + n;
						}
					}
					stockinLot.setList_not_check(list_not_check);
				}
			}
			
			response.data = stockinD_list;
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			e.printStackTrace();
//			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(null==e.getMessage()?"Lỗi hệ thống! Liên hệ IT để được hỗ trợ":e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value = "/getByLotAndPackageId",method = RequestMethod.POST)
	public ResponseEntity<StockinPkl_response> getByLotAndPackageId( @RequestBody StockInRequest entity,HttpServletRequest request ) {
		StockinPkl_response  response= new StockinPkl_response();
		try {
//			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//			Long stockid_link = user.getOrg_grant_id_link();
//			System.out.println(stockid_link);
			List<StockInPklist> stockInPklist_list = 
					stockInPklistService.getStockinPklByStockinDLotAndPackageId(entity.stockindid_link, entity.lotnumber, entity.packageid);
			
			for(StockInPklist item : stockInPklist_list) {
				Long stockindid_link = item.getStockindid_link();
				StockInD stockind = stockInDService.findOne(stockindid_link);
				Long stockinid_link = stockind.getStockinid_link();
				StockIn stockin = stockInService.findOne(stockinid_link);
				Long orgid = stockin.getOrgid_to_link();
				String spaceepc = item.getSpaceepc_link();
				List<Stockspace> stockspace_list = stockspaceService.findStockspaceByEpc(orgid, spaceepc);
				if(stockspace_list.size() > 0) {
					Stockspace stockspace = stockspace_list.get(0);
					Stockrow stockrow = stockrowService.findOne(stockspace.getRowid_link());
					item.setRow(stockrow.getCode());
					item.setSpace(stockspace.getSpacename());
					item.setFloor(stockspace.getFloorid());
				}
			}
			
			response.data = stockInPklist_list;
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<StockinPkl_response>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			response.setRespcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<StockinPkl_response>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/upload_stockin_pklist", method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> upload_stockin_pklist(HttpServletRequest request, @RequestParam("file") MultipartFile file,
			@RequestParam("stockindid_link") Long stockindid_link, @RequestParam("isKiem") Boolean isKiem
			) {
		ResponseBase response = new ResponseBase();

		Date current_time = new Date();
		String name = "";
		String mes_err = "";
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Long orgrootid_link = user.getRootorgid_link();
			String FolderPath = "upload/stockin_pklist";
			String uploadRootPath = request.getServletContext().getRealPath(FolderPath);
			File uploadRootDir = new File(uploadRootPath);
			// Tạo thư mục gốc upload nếu nó không tồn tại.
			if (!uploadRootDir.exists()) {
				uploadRootDir.mkdirs();
			}

			name = file.getOriginalFilename();
			if (name != null && name.length() > 0) {
				String[] str = name.split("\\.");
				String extend = str[str.length - 1];
				name = current_time.getTime() + "." + extend;
				File serverFile = new File(uploadRootDir.getAbsolutePath() + File.separator + name);

				BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
				stream.write(file.getBytes());
				stream.close();

				// doc file upload
				XSSFWorkbook workbook = new XSSFWorkbook(serverFile);
				XSSFSheet sheet = workbook.getSheetAt(0);
				
				
				StockInD stockInD = stockInDService.findOne(stockindid_link);
				StockIn stockIn = stockInService.findOne(stockInD.getStockinid_link());

				// kiem tra xem co upload nham loai file hay khong
				Row row0 = sheet.getRow(0);
				String file_type = commonService.getStringValue(row0.getCell(ColumnStockinPackinglist.STT));
				if (!file_type.equals("STT (Packinglist)")) {
					mes_err = "Bạn upload nhầm loại file! Bạn phải tải file mẫu trước khi upload!";
				} else {
					// Kiem tra header
					Integer rowNum = 1; // index bắt đầu từ 0 (header)
					Integer colNum = 1;

					Row row = sheet.getRow(rowNum);
//					Row rowheader = sheet.getRow(0);
					
					try {
						String STT = "";
						STT = commonService.getStringValue(row.getCell(ColumnStockinPackinglist.STT));
						STT = STT.equals("0") ? "" : STT;
						
//						Long productid_link = product.getId();
						
						// kiểm tra các dòng có lỗi hay không (loop qua toàn bộ file excel)
						while(!STT.equals("")) {
//							System.out.println("- TenCongDoan");
							// SoLot
							colNum = ColumnStockinPackinglist.SoLot;
							String soLot = commonService.getStringValue(row.getCell(ColumnStockinPackinglist.SoLot));
							soLot = soLot.toLowerCase().trim();
							if(soLot.equals("")) {
								mes_err = "Số Lot không được để trống. Ở dòng " + (rowNum + 1) + " và cột "
										+ (colNum + 1);
								break;
							}
							
							// SoCay
							colNum = ColumnStockinPackinglist.SoCay;
							String soCay = commonService.getStringValue(row.getCell(ColumnStockinPackinglist.SoCay));
							soCay = soCay.toLowerCase().trim();
							if(soCay.equals("")) {
								mes_err = "Số cây không được để trống. Ở dòng " + (rowNum + 1) + " và cột "
										+ (colNum + 1);
								break;
							}
							if(!soCay.equals("")) {
								try {
								    Integer.parseInt(soCay);
								} catch (NumberFormatException e) {
									mes_err = "Số cây phải là số nguyên. Ở dòng " + (rowNum + 1) + " và cột "
											+ (colNum + 1);
									break;
								}
							}
							
							// SoYds
							colNum = ColumnStockinPackinglist.SoYds;
							String soYds = commonService.getStringValue(row.getCell(ColumnStockinPackinglist.SoYds));
							soYds = soYds.toLowerCase().trim();
							if(!soYds.equals("")) {
								try {
								    Float.parseFloat(soYds);
								} catch (NumberFormatException e) {
									mes_err = "Số Yds phải là số. Ở dòng " + (rowNum + 1) + " và cột "
											+ (colNum + 1);
									break;
								}
							}
							
							// SoKg
							colNum = ColumnStockinPackinglist.SoKg;
							String soKg = commonService.getStringValue(row.getCell(ColumnStockinPackinglist.SoKg));
							soKg = soKg.toLowerCase().trim();
							if(!soKg.equals("")) {
								try {
								    Float.parseFloat(soKg);
								} catch (NumberFormatException e) {
									mes_err = "Số Kg phải là số. Ở dòng " + (rowNum + 1) + " và cột "
											+ (colNum + 1);
									break;
								}
							}
							
							// SoKhoCm
							colNum = ColumnStockinPackinglist.SoKhoCm;
							String soKhoCm = commonService.getStringValue(row.getCell(ColumnStockinPackinglist.SoKhoCm));
							soKhoCm = soKhoCm.toLowerCase().trim();
							if(!soKhoCm.equals("")) {
								try {
								    Float.parseFloat(soKhoCm);
								} catch (NumberFormatException e) {
									mes_err = "Số khổ (cm) phải là số. Ở dòng " + (rowNum + 1) + " và cột "
											+ (colNum + 1);
									break;
								}
							}
							
							// Chuyen sang row tiep theo neu con du lieu thi xu ly tiep khong thi dung lai
							rowNum++;
							row = sheet.getRow(rowNum);
							if (row == null) {
								break;
							}
							STT = commonService.getStringValue(row.getCell(ColumnStockinPackinglist.STT));
							STT = STT.equals("0") ? "" : STT;
						}
						
						// nếu không có lỗi -> xử lý db
						if(mes_err.equals("")) {
							rowNum = 1;
							colNum = 1;
							row = sheet.getRow(rowNum);
							
							STT = commonService.getStringValue(row.getCell(ColumnStockinPackinglist.STT));
							STT = STT.equals("0") ? "" : STT;
							
							while (!STT.equals("")) {
								colNum = ColumnStockinPackinglist.SoLot;
								String soLot = commonService.getStringValue(row.getCell(ColumnStockinPackinglist.SoLot)).toUpperCase().trim();
								colNum = ColumnStockinPackinglist.SoCay;
								String soCay = commonService.getStringValue(row.getCell(ColumnStockinPackinglist.SoCay)).toLowerCase().trim();
								colNum = ColumnStockinPackinglist.SoYds;
								String soYds = commonService.getStringValue(row.getCell(ColumnStockinPackinglist.SoYds)).toLowerCase().trim();
								colNum = ColumnStockinPackinglist.SoKg;
								String soKg = commonService.getStringValue(row.getCell(ColumnStockinPackinglist.SoKg)).toLowerCase().trim();
								colNum = ColumnStockinPackinglist.SoKhoCm;
								String soKhoCm = commonService.getStringValue(row.getCell(ColumnStockinPackinglist.SoKhoCm)).toLowerCase().trim();
								
								Integer intValueSoCay = 0;
								Float floatValueSoYds = (float) 0;
								Float floatValueSoKg = (float) 0;
								Float floatValueSoKhoCm = (float) 0;
								
								// SoYds
								if(!soCay.equals("")) {
									intValueSoCay = Integer.parseInt(soCay);
								}
								// SoYds
								if(!soYds.equals("")) {
									floatValueSoYds = Float.parseFloat(soYds);
								}
								// SoKg
								if(!soKg.equals("")) {
									floatValueSoKg = Float.parseFloat(soKg);
								}
								// SoKhoCm
								if(!soKhoCm.equals("")) {
									floatValueSoKhoCm = Float.parseFloat(soKhoCm);
								}
								
								// Stockinlot
								StockinLot stockinLot= new StockinLot();
								List<StockinLot> stockinLot_list = 
										stockInLOTService.getByStockinD_LOT(stockindid_link, soLot);
								if(stockinLot_list.size() > 0) { // số lot đã tồn tại
									stockinLot = stockinLot_list.get(0);
								}else { // số lot chưa tồn tại -> create
									stockinLot.setOrgrootid_link(orgrootid_link);
									stockinLot.setStockinid_link(stockIn.getId());
									stockinLot.setStockindid_link(stockindid_link);
									stockinLot.setMaterialid_link(stockInD.getSkuid_link());
									stockinLot.setLot_number(soLot);
									stockinLot.setTotalpackage(0);
									stockinLot.setTotalpackagecheck(0);
									stockinLot.setTotalpackagepklist(0);
									stockinLot.setStatus(0);
									stockinLot.setTotalyds((float) 0);
									stockinLot.setTotalydscheck((float) 0);
									stockinLot.setTotalmet((float) 0);
									stockinLot.setTotalmetcheck((float) 0);
									stockinLot.setGrossweight((float) 0);
									stockinLot.setGrossweight_check((float) 0);
									stockinLot.setGrossweight_lbs((float) 0);
									stockinLot.setGrossweight_lbs_check((float) 0);
									stockinLot.setIs_upload(true);
									stockinLot = stockInLOTService.save(stockinLot);
								}
								
								// StockinPklist
								StockInPklist stockInPklist = new StockInPklist();
								List<StockInPklist> stockInPklist_list = 
										stockInPklistService.getStockinPklByStockinDLotAndPackageId(stockindid_link, soLot, intValueSoCay);
								
								if(stockInPklist_list.size() > 0) {
									stockInPklist = stockInPklist_list.get(0);
									
									Float pklist_met_origin_old = stockInPklist.getMet_origin() == null ? (float)0 : stockInPklist.getMet_origin();
									Float pklist_ydsorigin_old = stockInPklist.getYdsorigin() == null ? (float)0 : stockInPklist.getYdsorigin();
									Float pklist_grossweight_old = stockInPklist.getGrossweight() == null ? (float)0 : stockInPklist.getGrossweight();
									Float pklist_grossweight_lbs_old = stockInPklist.getGrossweight_lbs() == null ? (float)0 : stockInPklist.getGrossweight_lbs();
									
									Float pklist_met_check_old = stockInPklist.getMet_check() == null ? (float)0 : stockInPklist.getMet_check();
									Float pklist_ydscheck_old = stockInPklist.getYdscheck() == null ? (float)0 : stockInPklist.getYdscheck();
									Float pklist_grossweight_check_old = stockInPklist.getGrossweight_check() == null ? (float)0 : stockInPklist.getGrossweight_check();
									Float pklist_grossweight_lbs_check_old = stockInPklist.getGrossweight_lbs_check() == null ? (float)0 : stockInPklist.getGrossweight_lbs_check();
									
									// set giá trị cho các trường phiếu
									stockInPklist.setYdsorigin(floatValueSoYds);
									stockInPklist.setMet_origin((float) (floatValueSoYds * commonUnit.yardTomet));
									stockInPklist.setGrossweight(floatValueSoKg);
									stockInPklist.setGrossweight_lbs((float) (floatValueSoKg * commonUnit.kgTolbs));
									stockInPklist.setWidth_met(floatValueSoKhoCm / 100);
									stockInPklist.setWidth_yds((float) (floatValueSoKhoCm / 100 * commonUnit.metToyard));
									
									// set giá trị cho các trường kiểm
									stockInPklist.setYdscheck(floatValueSoYds);
									stockInPklist.setMet_check((float) (floatValueSoYds * commonUnit.yardTomet));
									stockInPklist.setGrossweight_check(floatValueSoKg);
									stockInPklist.setGrossweight_lbs_check((float) (floatValueSoKg * commonUnit.kgTolbs));
									stockInPklist.setWidth_met_check(floatValueSoKhoCm / 100);
									stockInPklist.setWidth_yds_check((float) (floatValueSoKhoCm / 100 * commonUnit.metToyard));
									// set status
									if(stockInPklist.getStatus() != null && stockInPklist.getStatus() < StockinStatus.STOCKIN_EPC_STATUS_CHECK_LABEL) {
										if(isKiem)
//											stockInPklist.setStatus(StockinStatus.STOCKIN_EPC_STATUS_CHECK_LABEL);
											stockInPklist.setStatus(StockinStatus.STOCKIN_EPC_STATUS_ERR);
										else
											stockInPklist.setStatus(StockinStatus.STOCKIN_EPC_STATUS_ERR);
									}
									// set mobile checked?
									if(stockInPklist.getStatus() <= StockinStatus.STOCKIN_EPC_STATUS_CHECK_LABEL && stockInPklist.getIs_mobile_checked() != null) {
										stockInPklist.setIs_mobile_checked(null);
									}
									
									stockInPklist = stockInPklistService.save(stockInPklist);
									
									Float pklist_met_origin_new = stockInPklist.getMet_origin() == null ? (float)0 : stockInPklist.getMet_origin();
									Float pklist_ydsorigin_new = stockInPklist.getYdsorigin() == null ? (float)0 : stockInPklist.getYdsorigin();
									Float pklist_grossweight_new = stockInPklist.getGrossweight() == null ? (float)0 : stockInPklist.getGrossweight();
									Float pklist_grossweight_lbs_new = stockInPklist.getGrossweight_lbs() == null ? (float)0 : stockInPklist.getGrossweight_lbs();
									
									Float pklist_met_check_new = stockInPklist.getMet_check() == null ? (float)0 : stockInPklist.getMet_check();
									Float pklist_ydscheck_new = stockInPklist.getYdscheck() == null ? (float)0 : stockInPklist.getYdscheck();
									Float pklist_grossweight_check_new = stockInPklist.getGrossweight_check() == null ? (float)0 : stockInPklist.getGrossweight_check();
									Float pklist_grossweight_lbs_check_new = stockInPklist.getGrossweight_lbs_check() == null ? (float)0 : stockInPklist.getGrossweight_lbs_check();
									
									Float lot_totalmet = stockinLot.getTotalmet() == null ? (float)0 : stockinLot.getTotalmet();
									Float lot_totalyds = stockinLot.getTotalyds() == null ? (float)0 : stockinLot.getTotalyds();
									Float lot_grossweight = stockinLot.getGrossweight() == null ? (float)0 : stockinLot.getGrossweight();
									Float lot_grossweight_lbs = stockinLot.getGrossweight_lbs() == null ? (float)0 : stockinLot.getGrossweight_lbs();
									
									Float lot_totalmet_check = stockinLot.getTotalmetcheck() == null ? (float)0 : stockinLot.getTotalmetcheck();
									Float lot_totalyds_check = stockinLot.getTotalydscheck() == null ? (float)0 : stockinLot.getTotalydscheck();
									Float lot_grossweight_check = stockinLot.getGrossweight_check() == null ? (float)0 : stockinLot.getGrossweight_check();
									Float lot_grossweight_lbs_check = stockinLot.getGrossweight_lbs_check() == null ? (float)0 : stockinLot.getGrossweight_lbs_check();
									
									lot_totalmet = lot_totalmet - pklist_met_origin_old + pklist_met_origin_new;
									lot_totalyds = lot_totalyds - pklist_ydsorigin_old + pklist_ydsorigin_new;
									lot_grossweight = lot_grossweight - pklist_grossweight_old + pklist_grossweight_new;
									lot_grossweight_lbs = lot_grossweight_lbs - pklist_grossweight_lbs_old + pklist_grossweight_lbs_new;
									
									lot_totalmet_check = lot_totalmet_check - pklist_met_check_old + pklist_met_check_new;
									lot_totalyds_check = lot_totalyds_check - pklist_ydscheck_old + pklist_ydscheck_new;
									lot_grossweight_check = lot_grossweight_check - pklist_grossweight_check_old + pklist_grossweight_check_new;
									lot_grossweight_lbs_check = lot_grossweight_lbs_check - pklist_grossweight_lbs_check_old + pklist_grossweight_lbs_check_new;
									
									stockinLot.setTotalmet(lot_totalmet);
									stockinLot.setTotalyds(lot_totalyds);
									stockinLot.setGrossweight(lot_grossweight);
									stockinLot.setGrossweight_lbs(lot_grossweight_lbs);
									stockinLot.setTotalmetcheck(lot_totalmet_check);
									stockinLot.setTotalydscheck(lot_totalyds_check);
									stockinLot.setGrossweight_check(lot_grossweight_check);
									stockinLot.setGrossweight_lbs_check(lot_grossweight_lbs_check);
									stockinLot = stockInLOTService.save(stockinLot);
								}else {
									stockInPklist.setOrgrootid_link(orgrootid_link);								
									stockInPklist.setStockinid_link(stockInD.getStockinid_link());
									stockInPklist.setStockindid_link(stockindid_link);
									stockInPklist.setSkuid_link(stockInD.getSkuid_link());
									stockInPklist.setColorid_link(stockInD.getColorid_link());
									stockInPklist.setLotnumber(soLot);
									stockInPklist.setPackageid(intValueSoCay);
									stockInPklist.setYdsorigin(floatValueSoYds);
									stockInPklist.setMet_origin((float) (floatValueSoYds * commonUnit.yardTomet));
									stockInPklist.setGrossweight(floatValueSoKg);
									stockInPklist.setGrossweight_lbs((float) (floatValueSoKg * commonUnit.kgTolbs));
									stockInPklist.setWidth_met(floatValueSoKhoCm / 100);
									stockInPklist.setWidth_yds((float) (floatValueSoKhoCm / 100 * commonUnit.metToyard));
									stockInPklist.setEpc(genUUID());
									stockInPklist.setUsercreateid_link(user.getId());
									stockInPklist.setTimecreate(current_time);
									stockInPklist.setUnitid_link(stockInD.getUnitid_link());
//									stockInPklist.setStatus(StockinStatus.STOCKIN_EPC_STATUS_CHECK_LABEL);
									
									if(isKiem) {
										stockInPklist.setYdscheck(floatValueSoYds);
										stockInPklist.setMet_check((float) (floatValueSoYds * commonUnit.yardTomet));
										stockInPklist.setGrossweight_check(floatValueSoKg);
										stockInPklist.setGrossweight_lbs_check((float) (floatValueSoKg * commonUnit.kgTolbs));
										stockInPklist.setWidth_met_check(floatValueSoKhoCm / 100);
										stockInPklist.setWidth_yds_check((float) (floatValueSoKhoCm / 100 * commonUnit.metToyard));
										stockInPklist.setStatus(StockinStatus.STOCKIN_EPC_STATUS_ERR);
									}
									else {
										stockInPklist.setYdscheck((float) 0);
										stockInPklist.setMet_check((float) 0);
										stockInPklist.setGrossweight_check((float) 0);
										stockInPklist.setGrossweight_lbs_check((float) 0);
										stockInPklist.setWidth_met_check((float) 0);
										stockInPklist.setWidth_yds_check((float) 0);
										stockInPklist.setStatus(StockinStatus.STOCKIN_EPC_STATUS_ERR);
									}
									stockInPklist.setIs_mobile_checked(null);
									
									stockInPklist.setRssi(0);
									stockInPklist = stockInPklistService.save(stockInPklist);
									
									// update lot
									Integer lot_totalpackage = stockinLot.getTotalpackage() == null ? 0 : stockinLot.getTotalpackage();
									Float lot_totalmet = stockinLot.getTotalmet() == null ? (float)0 : stockinLot.getTotalmet();
									Float lot_totalyds = stockinLot.getTotalyds() == null ? (float)0 : stockinLot.getTotalyds();
									Float lot_grossweight = stockinLot.getGrossweight() == null ? (float)0 : stockinLot.getGrossweight();
									Float lot_grossweight_lbs = stockinLot.getGrossweight_lbs() == null ? (float)0 : stockinLot.getGrossweight_lbs();
									
									lot_totalpackage = lot_totalpackage + 1;
									lot_totalmet = lot_totalmet + stockInPklist.getMet_origin();
									lot_totalyds = lot_totalyds + stockInPklist.getYdsorigin();
									lot_grossweight = lot_grossweight + stockInPklist.getGrossweight();
									lot_grossweight_lbs = lot_grossweight_lbs + stockInPklist.getGrossweight_lbs();
									
									stockinLot.setTotalpackage(lot_totalpackage);
									stockinLot.setTotalmet(lot_totalmet);
									stockinLot.setTotalyds(lot_totalyds);
									stockinLot.setGrossweight(lot_grossweight);
									stockinLot.setGrossweight_lbs(lot_grossweight_lbs);
									stockinLot.setTotalmetcheck(lot_totalmet);
									stockinLot.setTotalydscheck(lot_totalyds);
									stockinLot.setGrossweight_check(lot_grossweight);
									stockinLot.setGrossweight_lbs_check(lot_grossweight_lbs);
									
									stockinLot = stockInLOTService.save(stockinLot);
								}
								
								// Chuyen sang row tiep theo neu con du lieu thi xu ly tiep khong thi dung lai
								rowNum++;
								row = sheet.getRow(rowNum);
								if (row == null) {
									break;
								}
								STT = commonService.getStringValue(row.getCell(ColumnStockinPackinglist.STT));
								STT = STT.equals("0") ? "" : STT;
							}
							
							// Tính toán lại lot và stockind
							//Update lại số tổng Check trong Stockin_d
							stockInDService.recal_Totalcheck(stockindid_link);
							
							//Update lại số tổng Chẹck của Stockin_LOT
							StockInD s = stockInDService.findOne(stockindid_link);
							List<StockinLot> lot_list = s.getStockin_lot(); // System.out.println(lot_list.size());
							for(StockinLot item : lot_list) {
								stockInLOTService.recal_Totalcheck(stockindid_link, item.getLot_number());
							}
						}
						
					} catch (Exception e) {
//						System.out.println("- last catch");
						mes_err = "Có lỗi ở dòng " + (rowNum + 1) + " và cột " + (colNum + 1) + ": " + mes_err;
					} finally {
//						System.out.println("- finally");
						if(stockIn.getStatus().equals(StockinStatus.STOCKIN_STATUS_ERR)) {
							stockIn.setStatus(StockinStatus.STOCKIN_STATUS_OK);
						}
						workbook.close();
						serverFile.delete();
					}
				}
//				System.out.println("- outside");
				if (mes_err == "") {
//					System.out.println("- no error");
					response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
					response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
				} else {
//					System.out.println("- error");
					response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
					response.setMessage(mes_err);
				}
			} else {
				response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
				response.setMessage("Có lỗi trong quá trình upload! Bạn hãy thử lại");
			}
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		}
		return new ResponseEntity<ResponseBase>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/download_temp_stockinpackinglist", method = RequestMethod.POST)
	public ResponseEntity<download_temp_stockinpackinglist_response> download_temp_stockinpackinglist(HttpServletRequest request) {

		download_temp_stockinpackinglist_response response = new download_temp_stockinpackinglist_response();
		try {
			String FolderPath = "TemplateUpload";

			// Thư mục gốc upload file.
			String uploadRootPath = request.getServletContext().getRealPath(FolderPath);

			String filePath = uploadRootPath + "/" + "Template_StockinPackingList_New.xlsx";
			Path path = Paths.get(filePath);
			byte[] data = Files.readAllBytes(path);
			response.data = data;
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<download_temp_stockinpackinglist_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<download_temp_stockinpackinglist_response>(response, HttpStatus.OK);
		}
	}
}