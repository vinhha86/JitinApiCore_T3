package vn.gpay.jitin.core.api.pcontractsku;

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
import vn.gpay.jitin.core.pcontratproductsku.IPContractProductSKUService;
import vn.gpay.jitin.core.security.GpayUser;
import vn.gpay.jitin.core.utils.ResponseMessage;


@RestController
@RequestMapping("/api/v1/pcontractsku")
public class PContractskuAPI {
	@Autowired IPContractProductSKUService pskuservice;
	
	@RequestMapping(value = "/getbypcontract_product",method = RequestMethod.POST)
	public ResponseEntity<PContractSKU_getbyproduct_response> SKU_GetbyProduct
	(HttpServletRequest request, @RequestBody PContractSKU_getbyproduct_request entity ) {
		PContractSKU_getbyproduct_response response = new PContractSKU_getbyproduct_response();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			long orgrootid_link = user.getRootorgid_link();
			long pcontractid_link = entity.pcontractid_link;
			long productid_link = entity.productid_link;
			
			response.data = pskuservice.getlistsku_byproduct_and_pcontract(orgrootid_link, productid_link, pcontractid_link);
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
		}
		catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		}
		
		return new ResponseEntity<PContractSKU_getbyproduct_response>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/update",method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> SKU_Update
	(HttpServletRequest request, @RequestBody PContractSKYU_update_request entity ) {
		ResponseBase response = new ResponseBase();
		try {
			pskuservice.save(entity.data);
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
		}
		catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		}
		
		return new ResponseEntity<ResponseBase>(response, HttpStatus.OK);
	}
}
