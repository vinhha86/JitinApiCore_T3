package vn.gpay.jitin.core.api.cutplan_processing;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import vn.gpay.jitin.core.base.ResponseBase;
import vn.gpay.jitin.core.base.ResponseError;
import vn.gpay.jitin.core.cutplan_processing.ICutplanProcessingDService;
import vn.gpay.jitin.core.cutplan_processing.ICutplanProcessingService;
import vn.gpay.jitin.core.org.IOrgService;
import vn.gpay.jitin.core.org.Org;
import vn.gpay.jitin.core.security.GpayUser;
import vn.gpay.jitin.core.utils.ResponseMessage;

@RestController
@RequestMapping("/api/v1/cutplan_processing")
public class CutplanProcessingAPI {
	@Autowired ICutplanProcessingService cutplanProcessingService;
	@Autowired ICutplanProcessingDService cutplanProcessingDService;
	@Autowired IOrgService orgService;
	
	@RequestMapping(value = "/cutplan_processing_create",method = RequestMethod.POST)
	@Transactional(rollbackFor = RuntimeException.class)
	public ResponseEntity<?> CutplanProcessingCreate(@RequestBody CutplanProcessingCreateRequest entity, HttpServletRequest request ) {
		CutplanProcessingResponse response = new CutplanProcessingResponse();
		
		try {
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value = "/cutplan_processing_list",method = RequestMethod.POST)
	public ResponseEntity<?> CutplanProcessingList(@RequestBody CutplanProcessingListRequest entity, HttpServletRequest request ) {
		CutplanProcessingListResponse response = new CutplanProcessingListResponse();
		try {
//			GpayAuthentication user = (GpayAuthentication)SecurityContextHolder.getContext().getAuthentication();
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Long orgid_link = user.getOrgid_link();
			Org userOrg = orgService.findOne(orgid_link);
			Long userOrgId = userOrg.getId();
			Integer userOrgType = userOrg.getOrgtypeid_link();
			
			if (entity.page == 0) entity.page = 1;
			if (entity.limit == 0) entity.limit = 100;
			
			// 
			
			response.data = cutplanProcessingService.findAll();
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<CutplanProcessingListResponse>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			e.printStackTrace();
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
