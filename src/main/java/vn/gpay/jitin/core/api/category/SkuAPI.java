package vn.gpay.jitin.core.api.category;

import java.util.ArrayList;
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

import vn.gpay.jitin.core.security.GpayAuthentication;
import vn.gpay.jitin.core.sku.ISKU_Service;
import vn.gpay.jitin.core.sku.SKU;
import vn.gpay.jitin.core.utils.ResponseMessage;

@RestController
@RequestMapping("/api/v1/category")
public class SkuAPI {

	@Autowired ISKU_Service skuService;
	
	@RequestMapping(value = "/getSkuByCode",method = RequestMethod.POST)
	public ResponseEntity<SkuResponse> GetSkuByCode( @RequestBody GetSkuByCodeRequest entity,HttpServletRequest request ) {
		SkuResponse response = new SkuResponse();
		try {
			List<SKU> list = new ArrayList<SKU>();
			list.add(skuService.get_bySkucode(entity.skucode));
			response.data = list;
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<SkuResponse>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_EXCEPTION));
		    return new ResponseEntity<SkuResponse>(response,HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/getSkuByType",method = RequestMethod.POST)
	public ResponseEntity<SkuResponse> GetSkuByCode(@RequestBody GetSkuByTypeRequest entity, HttpServletRequest request ) {
		SkuResponse response = new SkuResponse();
		try {
			GpayAuthentication user = (GpayAuthentication) SecurityContextHolder.getContext().getAuthentication();
			long orgrootid_link = user.getRootorgid_link();
			
			response.data = skuService.findSkuByType(entity.type, orgrootid_link);
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<SkuResponse>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_EXCEPTION));
		    return new ResponseEntity<SkuResponse>(response,HttpStatus.OK);
		}
	}
}
