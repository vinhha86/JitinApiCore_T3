package vn.gpay.jitin.core.api.rfprint;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import vn.gpay.jitin.core.base.ResponseBase;
import vn.gpay.jitin.core.base.ResponseError;
import vn.gpay.jitin.core.rfprint_stockin.IRFPrint_StockinService;
import vn.gpay.jitin.core.rfprint_stockin.RFPrint_Stockin;
import vn.gpay.jitin.core.utils.RFPrintStockinStatus;
import vn.gpay.jitin.core.utils.ResponseMessage;

@RestController
@RequestMapping("/api/v1/rfprint")
public class RFPrint_API {
	@Autowired IRFPrint_StockinService rfPrintService;
	
	@RequestMapping(value = "/getbyid",method = RequestMethod.POST)
	@Transactional(rollbackFor = RuntimeException.class)
	public ResponseEntity<?> GetByID(@RequestBody RFPrint_request entity, HttpServletRequest request ) {
		RFPrint_response response = new RFPrint_response();
		try {
			RFPrint_Stockin theRFPrint = rfPrintService.findOne(entity.rfprintid_link);
			if (null != theRFPrint){
				response.status = theRFPrint.getStatus();
				response.err_code = theRFPrint.getErr_code();
				switch (response.err_code){
					case "21":
						response.err_msg = RFPrintStockinStatus.ZB_MSG_OUTPAPER_VN;
						break;
					case "22":
						response.err_msg = RFPrintStockinStatus.ZB_MSG_OUTRIBBON_VN;
						break;
					case "99":
						response.err_msg = RFPrintStockinStatus.ZB_MSG_UNKNOWN_VN;
						break;
				}
				response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
				response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
				return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);	
			} else {
				//Bao loi
				ResponseError errorBase = new ResponseError();
				errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
				errorBase.setMessage("Phiên in ấn không tồn tại! Liên hệ IT để được hỗ trợ");
			    return new ResponseEntity<>(errorBase, HttpStatus.BAD_REQUEST);
			}

		}catch (RuntimeException e) {
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(null==e.getMessage()?"Lỗi hệ thống! Liên hệ IT để được hỗ trợ":e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.BAD_REQUEST);
		}
	}
}