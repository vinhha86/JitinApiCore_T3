package vn.gpay.jitin.core.api.org;

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

import vn.gpay.jitin.core.base.ResponseBase;
import vn.gpay.jitin.core.base.ResponseError;
import vn.gpay.jitin.core.org.IOrgService;
import vn.gpay.jitin.core.org.IOrgTypeService;
import vn.gpay.jitin.core.org.Org;
import vn.gpay.jitin.core.org.OrgTree;
import vn.gpay.jitin.core.org.OrgType;
import vn.gpay.jitin.core.security.GpayAuthentication;
import vn.gpay.jitin.core.security.GpayUser;
import vn.gpay.jitin.core.utils.ResponseMessage;

@RestController
@RequestMapping("/api/v1/org")
public class OrgAPI {

	@Autowired IOrgService orgService;
	@Autowired IOrgTypeService orgTypeService;
	
	@RequestMapping(value = "/getOrgByType",method = RequestMethod.POST)
	public ResponseEntity<?> GetOrgByType(@RequestBody OrgByTypeRequest entity, HttpServletRequest request ) {//@RequestParam("type") 
		OrgResponse response = new OrgResponse();
		try {
			GpayUser user = (GpayUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			response.data = orgService.findOrgByType(user.getRootorgid_link(),user.getOrgid_link(),entity.type);
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<OrgResponse>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@RequestMapping(value = "/create",method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> CreateOrgByType(@RequestBody Org_create_Request entity, HttpServletRequest request ) {//@RequestParam("type") 
		ResponseBase response = new ResponseBase();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Org org = entity.data;
			if(org.getId() == 0 || org.getId() == null) {
				org.setOrgrootid_link(user.getRootorgid_link());
				org.setParentid_link(user.getRootorgid_link());
			}
			else {
				Org org_old = orgService.findOne(org.getId());
				org.setOrgrootid_link(org_old.getOrgrootid_link());
			}
			
			orgService.save(org);
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_SERVER_ERROR);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/delete",method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> DeleteOrg(@RequestBody Org_delete_request entity, HttpServletRequest request ) {//@RequestParam("type") 
		ResponseBase response = new ResponseBase();
		try {
			Org org = orgService.findOne(entity.id);
			org.setStatus(-1);
			
			orgService.save(org);
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_SERVER_ERROR);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/getorgdest",method = RequestMethod.POST)
	public ResponseEntity<?> GetOrgDest(@RequestBody GetOrgdestRequest entity ,  HttpServletRequest request ) {//
		OrgResponse response = new OrgResponse();
		try {
			String menuid = entity.menuid;
			GpayAuthentication user = (GpayAuthentication)SecurityContextHolder.getContext().getAuthentication();
			int type =user.getOrg_type();
			if("lsstockinproduct".equals(menuid)) {
				 List<Org> list1 =orgService.findOrgByType(user.getRootorgid_link(),user.getOrgId(),7);
				 List<Org> list2 =orgService.findOrgByType(user.getRootorgid_link(),user.getOrgId(),9);
				 list1.addAll(list2);
				 response.data=list1;
			}
			if("lsxuatdieuchuyen".equals(menuid)) {
				switch (type) {
				case 3:
					 List<Org> list1 =orgService.findOrgByType(user.getRootorgid_link(),user.getOrgId(),3);
					// List<Org> list2 =orgService.findOrgByType(user.getAppuser().getRootorgid_link(),7);
					// list1.addAll(list2);
					response.data=list1;
					break;
				case 4:
					 List<Org> list3 =orgService.findOrgByType(user.getRootorgid_link(),user.getOrgId(),4);
					 List<Org> list4 =orgService.findOrgByType(user.getRootorgid_link(),user.getOrgId(),8);
					 list3.addAll(list4);
					response.data=list3;
					break;
				case 8:
					 List<Org> list5 =orgService.findOrgByType(user.getRootorgid_link(),user.getOrgId(),4);
					 List<Org> list6 =orgService.findOrgByType(user.getRootorgid_link(),user.getOrgId(),8);
					 list5.addAll(list6);
					response.data=list5;
					break;
				default:
					response.data=orgService.findOrgByType(user.getRootorgid_link(),user.getOrgId(),1);
					break;
				}
			}
			
			if("lsnhapdieuchuyen".equals(menuid)) {
				switch (type) {
				case 3:
					 List<Org> list1 =orgService.findOrgByType(user.getRootorgid_link(),user.getOrgId(),3);
					// List<Org> list2 =orgService.findOrgByType(user.getAppuser().getRootorgid_link(),7);
					// list1.addAll(list2);
					response.data=list1;
					break;
				case 4:
					 List<Org> list3 =orgService.findOrgByType(user.getRootorgid_link(),user.getOrgId(),4);
					 List<Org> list4 =orgService.findOrgByType(user.getRootorgid_link(),user.getOrgId(),8);
					 list3.addAll(list4);
					response.data=list3;
					break;
				case 8:
					 List<Org> list5 =orgService.findOrgByType(user.getRootorgid_link(),user.getOrgId(),4);
					 List<Org> list6 =orgService.findOrgByType(user.getRootorgid_link(),user.getOrgId(),8);
					 list5.addAll(list6);
					response.data=list5;
					break;
				default:
					response.data=orgService.findOrgByType(user.getRootorgid_link(),user.getOrgId(),1);
				}
			}
			if("lsinvcheck".equals(menuid)) {
				switch (type) {
				case 1:
					 List<Org> list1 =orgService.findOrgByType(user.getRootorgid_link(),user.getOrgId(),3);
					 List<Org> list3 =orgService.findOrgByType(user.getRootorgid_link(),user.getOrgId(),4);
					 List<Org> list4 =orgService.findOrgByType(user.getRootorgid_link(),user.getOrgId(),8);
					 list1.addAll(list3);
					 list1.addAll(list4);
					response.data=list1;
					break;
				default:
					response.data =orgService.findStoreByType(user.getRootorgid_link(),user.getOrgId(),user.getOrg_type());
				}
			}
			if("lssalebill".equals(menuid)) {
				switch (type) {
				case 1:
					 List<Org> list1 =orgService.findStoreByType(user.getRootorgid_link(),null,4);
					response.data=list1;
					break;
				default:
					response.data =orgService.findStoreByType(user.getRootorgid_link(),user.getOrgId(),4);
				}
			}
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<OrgResponse>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	@RequestMapping(value = "/getOrgInvCheckByType",method = RequestMethod.POST)
	public ResponseEntity<?> GetOrgInvCheckByType(@RequestBody OrgByTypeRequest entity, HttpServletRequest request ) {//@RequestParam("type") 
		OrgResponse response = new OrgResponse();
		try {
			GpayUser user = (GpayUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if(user.getRootorgid_link() ==user.getOrgid_link()) {
				//là root
				response.data = orgService.findRootOrgInvCheckByType(user.getOrgid_link());
			}else {
				response.data = orgService.findOrgInvCheckByType(user.getOrgid_link());
			}
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<OrgResponse>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/getallbyroot_notuser",method = RequestMethod.POST)
	public ResponseEntity<?> GetAllOrgByRoot_notUser( HttpServletRequest request, @RequestBody Org_getbyroot_andtypeid_request entity ) {//@RequestParam("type") 
		OrgResponse response = new OrgResponse();
		try {
			GpayAuthentication user = (GpayAuthentication)SecurityContextHolder.getContext().getAuthentication();
			String[] listtype = entity.listid.split(",");
			List<String> list = new ArrayList<String>();
			for (String string : listtype) {
				list.add(string);
			}
			
			response.data = orgService.findOrgAllByRoot(user.getRootorgid_link(), user.getOrgId(), list, false);
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<OrgResponse>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/getallchildrenbyorg",method = RequestMethod.POST)
	public ResponseEntity<?> GetAllOrgByOrg( HttpServletRequest request, @RequestBody Org_getbyroot_andtypeid_request entity ) {//@RequestParam("type") 
		OrgResponse response = new OrgResponse();
		try {
			GpayAuthentication user = (GpayAuthentication)SecurityContextHolder.getContext().getAuthentication();
			String[] listtype = entity.listid.split(",");
			List<String> list = new ArrayList<String>();
			for (String string : listtype) {
				list.add(string);
			}
			List<Org> listreturn = orgService.findOrgAllByRoot(user.getRootorgid_link(), user.getOrgId(), list, true);
//			List<Org> listreturn = getOrgChildrenbyId(user.getOrgId(), listorg);
			
			response.data = listreturn;
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<OrgResponse>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/gettree_by_type",method = RequestMethod.POST)
	public ResponseEntity<?> GetTree( HttpServletRequest request, @RequestBody Org_gettree_bylistType_request entity ) {//@RequestParam("type") 
		Org_getTree_response response = new Org_getTree_response();
		try {
			GpayAuthentication user = (GpayAuthentication)SecurityContextHolder.getContext().getAuthentication();
			String[] listtype = entity.listid_type.split(",");
			List<String> list = new ArrayList<String>();
			for (String string : listtype) {
				list.add(string);
			}
			List<Org> listreturn = orgService.findOrgAllByRoot(user.getRootorgid_link(), user.getOrgId(), list, true);
			List<OrgTree> listtree = orgService.getTreeOrg_byType(listreturn);
//			List<Org> listreturn = getOrgChildrenbyId(user.getOrgId(), listorg);
			
			response.children = listtree;
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<Org_getTree_response>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/getOrgByTypeId",method = RequestMethod.POST)
	public ResponseEntity<OrgResponse> GetOrgByTypeid(@RequestBody Org_getbyType_request entity, HttpServletRequest request ) {//@RequestParam("type") 
		OrgResponse response = new OrgResponse();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			response.data = orgService.findAllorgByTypeId(entity.orgtypeid_link,(long)user.getRootorgid_link());
			if(entity.isAll) {
				Org org = new Org();
				org.setId((long)0);
				org.setName("Tất cả");
				response.data.add(0, org);
			}
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<OrgResponse>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_SERVER_ERROR);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<OrgResponse>(response,HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/getOrgById",method = RequestMethod.POST)
	public ResponseEntity<GetOrgById_response> GetOrgByid(@RequestBody GetOrgById_request entity, HttpServletRequest request ) {//@RequestParam("type") 
		GetOrgById_response response = new GetOrgById_response();
		try {
			response.data = orgService.findOne(entity.id);
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<GetOrgById_response>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_SERVER_ERROR);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<GetOrgById_response>(response,HttpStatus.OK);
		}
	}
	
	@RequestMapping("/getAllOrgType")
	public ResponseEntity<?> getAllOrgType(HttpServletRequest request ) {
		try {
			OrgTypeResponse response = new OrgTypeResponse();
			List<OrgType> all = orgTypeService.findOrgTypeForMenuOrg();
			response.data=all;
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<OrgTypeResponse>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
