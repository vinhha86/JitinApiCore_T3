package vn.gpay.jitin.core.api.category;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import vn.gpay.jitin.core.base.ResponseError;
import vn.gpay.jitin.core.category.IPortService;
import vn.gpay.jitin.core.utils.ResponseMessage;

@RestController
@RequestMapping("/api/v1/category")
public class PortAPI {

	@Autowired IPortService portService;
	
	@RequestMapping(value = "/getPortAll",method = RequestMethod.POST)
	public ResponseEntity<?> GetSkuByCode(HttpServletRequest request ) {
		PortResponse response = new PortResponse();
		try {
			response.data = portService.findAll();
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<PortResponse>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
