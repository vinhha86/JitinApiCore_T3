package vn.gpay.jitin.core.api.sku;


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
import vn.gpay.jitin.core.sku.ISKU_AttributeValue_Service;
import vn.gpay.jitin.core.sku.ISKU_Service;
import vn.gpay.jitin.core.sku.SKU;
import vn.gpay.jitin.core.utils.ResponseMessage;


@RestController
@RequestMapping("/api/v1/sku")
public class SKU_API {
	@Autowired ISKU_AttributeValue_Service savService;
	@Autowired ISKU_Service skuService;
	
	@RequestMapping(value = "/getall_byproduct",method = RequestMethod.POST)
	public ResponseEntity<SKU_getbyproduct_response> Product_GetAll(HttpServletRequest request, @RequestBody SKU_getbyproduct_request entity ) {
		SKU_getbyproduct_response response = new SKU_getbyproduct_response();
		try {
			response.data = skuService.getlist_byProduct(entity.productid_link);
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<SKU_getbyproduct_response>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<SKU_getbyproduct_response>(response, HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/createcode",method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> Product_GetAll(HttpServletRequest request, @RequestBody SKU_Createcode_Request entity ) {
		ResponseBase response = new ResponseBase();
		
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			SKU sku = entity.data;
			if(sku.getId()==null || sku.getId()==0) {
				sku.setOrgrootid_link(user.getRootorgid_link());
			}else {
				SKU sku_old =  skuService.findOne(sku.getId());
				sku.setOrgrootid_link(sku_old.getOrgrootid_link());
			}
			
			skuService.save(sku);
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<ResponseBase>(response, HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/getinfo_bycode",method = RequestMethod.POST)
	public ResponseEntity<SKU_getinfobycode_response> GetInfo(HttpServletRequest request, @RequestBody SKU_getinfobycode_request entity ) {
		SKU_getinfobycode_response response = new SKU_getinfobycode_response();
		
		try {
			String skucode = entity.skucode;
			response.data = skuService.get_bySkucode(skucode);
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<SKU_getinfobycode_response>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<SKU_getinfobycode_response>(response, HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/getinfolist_bycode",method = RequestMethod.POST)
	public ResponseEntity<SKU_getinfoListsku_response> GetInfo(HttpServletRequest request, @RequestBody SKU_getinfoListsku_request entity ) {
		SKU_getinfoListsku_response response = new SKU_getinfoListsku_response();
		
		try {
			response.data = skuService.getlist_bycode(entity.listcode);
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<SKU_getinfoListsku_response>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<SKU_getinfoListsku_response>(response, HttpStatus.OK);
		}
	}
}
