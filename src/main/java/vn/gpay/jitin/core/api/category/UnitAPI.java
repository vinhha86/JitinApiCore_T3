package vn.gpay.jitin.core.api.category;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import vn.gpay.jitin.core.category.IUnitService;
import vn.gpay.jitin.core.utils.ResponseMessage;


@RestController
@RequestMapping("/api/v1/unit")
public class UnitAPI {
	@Autowired IUnitService unitservice;
	@RequestMapping(value = "/getall",method = RequestMethod.POST)
	public ResponseEntity<UnitGetallResponse> GetAllUnit(HttpServletRequest request ) {
		UnitGetallResponse response = new UnitGetallResponse();
		try {
			response.data = unitservice.findAll();
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<UnitGetallResponse>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<UnitGetallResponse>(response,HttpStatus.OK);
		}
	}
}
