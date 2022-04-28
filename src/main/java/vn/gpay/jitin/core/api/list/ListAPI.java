package vn.gpay.jitin.core.api.list;

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
import vn.gpay.jitin.core.category.IColorService;
import vn.gpay.jitin.core.security.GpayAuthentication;
import vn.gpay.jitin.core.security.GpayUser;
import vn.gpay.jitin.core.security.IGpayUserService;
import vn.gpay.jitin.core.sku.ISKU_Service;
import vn.gpay.jitin.core.stock.IStockrowService;
import vn.gpay.jitin.core.stock.IStockspaceService;
import vn.gpay.jitin.core.stock.Stockrow;
import vn.gpay.jitin.core.stock.Stockspace;
import vn.gpay.jitin.core.utils.ResponseMessage;

@RestController
@RequestMapping("/api/v1/list")
public class ListAPI {

	@Autowired ISKU_Service skuService;
	@Autowired IColorService colorService;
	@Autowired IStockrowService stockrowService;
	@Autowired IStockspaceService stockspaceService;
	@Autowired IGpayUserService userDetailsService;
	
	@RequestMapping(value = "/getAllProducts",method = RequestMethod.POST)
	public ResponseEntity<?> GetAllProducts( @RequestBody ListRequest entity,HttpServletRequest request ) {
		SkuResponse response = new SkuResponse();
		try {
			GpayAuthentication user = (GpayAuthentication) SecurityContextHolder.getContext().getAuthentication();
			long orgrootid_link = user.getRootorgid_link();
			
			response.data = skuService.findSkuByType(1, orgrootid_link);
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<SkuResponse>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/getAllMainMaterials",method = RequestMethod.POST)
	public ResponseEntity<?> GetAllMainMaterials( @RequestBody ListRequest entity,HttpServletRequest request ) {
		SkuResponse response = new SkuResponse();
		try {
			GpayAuthentication user = (GpayAuthentication) SecurityContextHolder.getContext().getAuthentication();
			long orgrootid_link = user.getRootorgid_link();
			response.data = skuService.findSkuByType(2, orgrootid_link);
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<SkuResponse>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/getAllColors",method = RequestMethod.POST)
	public ResponseEntity<?> GetAllColors( @RequestBody ListRequest entity,HttpServletRequest request ) {
		ColorResponse response =new ColorResponse();
		try {
			response.data = colorService.findAll();
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ColorResponse>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@RequestMapping(value = "/getAllStockSpaces",method = RequestMethod.POST)
	public ResponseEntity<?> GetAllStockSpaces( @RequestBody ListRequest entity,HttpServletRequest request ) {
		StockRowResponse response =new StockRowResponse();
		try {
			response.data = stockrowService.findStockrowByOrgID(3);
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<StockRowResponse>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/addStockRow",method = RequestMethod.POST)
	public ResponseEntity<?> AddStockRow( @RequestBody Stockrow entity,HttpServletRequest request ) {
		GpayAuthentication user = (GpayAuthentication)SecurityContextHolder.getContext().getAuthentication();
		ResponseBase response =new ResponseBase();
		if (user != null){
			try {
				entity.setOrgid_link(user.getOrgId());
				stockrowService.create(entity);
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
		else
			return new ResponseEntity<ResponseBase>(response,HttpStatus.BAD_REQUEST);		
	}
	@RequestMapping(value = "/addStockSpaceToRow",method = RequestMethod.POST)
	public ResponseEntity<?> AddStockSpaceToRow( @RequestBody Stockspace entity,HttpServletRequest request ) {
		ResponseBase response =new ResponseBase();
		try {
			stockspaceService.create(entity);
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
	@RequestMapping(value = "/getOneStockSpace",method = RequestMethod.POST)
	public ResponseEntity<?> GetOneStockSpace( @RequestBody StockSpaceRequest entity,HttpServletRequest request ) {
		GpayAuthentication user = (GpayAuthentication)SecurityContextHolder.getContext().getAuthentication();
		GpayUser user_info = userDetailsService.findById(user.getUserId());
		Long stockid_link = null!= user_info.getOrg_grant_id_link()?user_info.getOrg_grant_id_link():user_info.getOrgid_link();
		
		StockSpaceResponse response =new StockSpaceResponse();
		if (user != null){
			try {
				response.data = stockspaceService.findStockspaceByEpc(stockid_link, entity.spaceepc);
				response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
				response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
				return new ResponseEntity<StockSpaceResponse>(response,HttpStatus.OK);
			}catch (RuntimeException e) {
				ResponseError errorBase = new ResponseError();
				errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
				errorBase.setMessage(e.getMessage());
			    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		else
			return new ResponseEntity<StockSpaceResponse>(response,HttpStatus.BAD_REQUEST);
	}
}
