package vn.gpay.jitin.core.api.pcontractproduct;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
import vn.gpay.jitin.core.pcontractattributevalue.IPContractProductAtrributeValueService;
import vn.gpay.jitin.core.pcontractattributevalue.PContractAttributeValue;
import vn.gpay.jitin.core.pcontractproduct.IPContractProductService;
import vn.gpay.jitin.core.pcontractproduct.PContractProduct;
import vn.gpay.jitin.core.pcontractproduct.PContractProductBinding;
import vn.gpay.jitin.core.product.IProductService;
import vn.gpay.jitin.core.product.Product;
import vn.gpay.jitin.core.security.GpayUser;
import vn.gpay.jitin.core.utils.ResponseMessage;


@RestController
@RequestMapping("/api/v1/pcontractproduct")
public class PContractProductAPI {
	@Autowired IPContractProductService pcpservice;
	@Autowired IPContractProductAtrributeValueService pcpavservice;
	@Autowired IProductService pservice;
	
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> Create(HttpServletRequest request,
			@RequestBody PContractProduct_create_request entity) {
		ResponseBase response = new ResponseBase();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			long orgrootid_link = user.getRootorgid_link();
			long pcontractid_link = entity.pcontractid_link;
			for (long productid_link : entity.listIdProduct) {
				List<PContractProduct> lst = pcpservice.get_by_product_and_pcontract(orgrootid_link, productid_link, pcontractid_link);
				if(lst.size() == 0) {
					PContractProduct pcontractp = new PContractProduct();
					pcontractp.setId((long)0);
					pcontractp.setPcontractid_link(pcontractid_link);
					pcontractp.setProductid_link(productid_link);
					pcontractp.setOrgrootid_link(orgrootid_link);
					pcontractp.setPquantity(0);
					
					pcpservice.save(pcontractp);
					
					//insert cac thuoc tinh cua san pham sang
					Product p = pservice.findOne(productid_link);
					for (Long productattributeid_link : p.getProductAttribute()) {
						PContractAttributeValue cav = new PContractAttributeValue();
						cav.setId((long)0);
						cav.setOrgrootid_link(orgrootid_link);
						cav.setAttributeid_link(productattributeid_link);
						cav.setProductid_link(productid_link);
						cav.setPcontractid_link(pcontractid_link);
						cav.setAttributevalueid_link((long)0);
						
						pcpavservice.save(cav);
					}
				}
			}
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ResponseBase>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<ResponseBase>(response, HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> Update(HttpServletRequest request,
			@RequestBody PContractProduct_update_request entity) {
		ResponseBase response = new ResponseBase();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			long orgrootid_link = user.getRootorgid_link();
			PContractProduct pcontractproduct = entity.data;
			
			pcontractproduct.setOrgrootid_link(orgrootid_link);
			pcpservice.save(pcontractproduct);
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ResponseBase>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<ResponseBase>(response, HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> Update(HttpServletRequest request,
			@RequestBody PContractProduct_delete_product_request entity) {
		ResponseBase response = new ResponseBase();
		try {
			pcpservice.deleteById(entity.id);
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ResponseBase>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<ResponseBase>(response, HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/getbypcontract", method = RequestMethod.POST)
	public ResponseEntity<PContractProduct_getall_response> GetByPContract(HttpServletRequest request,
			@RequestBody PContractProduct_getbycontract_request entity) {
		PContractProduct_getall_response response = new PContractProduct_getall_response();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			long orgrootid_link = user.getRootorgid_link();
			long pcontractid_link = entity.pcontractid_link;
			List<PContractProduct> lst = pcpservice.get_by_product_and_pcontract(orgrootid_link, 0, pcontractid_link);
			List<PContractProductBinding> data = new ArrayList<PContractProductBinding>();
			String FolderPath = "upload/product";
			
			for (PContractProduct pContractProduct : lst) {
				PContractProductBinding binding = new PContractProductBinding();
				binding.setId(pContractProduct.getId());
				binding.setOrgrootid_link(orgrootid_link);
				binding.setPcontractid_link(pContractProduct.getPcontractid_link());
				binding.setProductid_link(pContractProduct.getProductid_link());
				binding.setProductCode(pContractProduct.getProductCode());
				binding.setProductName(pContractProduct.getProductName());
				binding.setPquantity(pContractProduct.getPquantity());
				
				String uploadRootPath = request.getServletContext().getRealPath(FolderPath);
				
				binding.setImgproduct(getimg(pContractProduct.getImgurl1(),uploadRootPath));
				
				data.add(binding);
			}
			
			response.data = data;
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<PContractProduct_getall_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<PContractProduct_getall_response>(response, HttpStatus.OK);
		}
	}
	
	
	
	private byte[] getimg(String filename, String uploadRootPath) {
		String filePath = uploadRootPath+"\\"+ filename;
		Path path = Paths.get(filePath);
		byte[] data;
		try {
			data = Files.readAllBytes(path);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			data = null;
		}
		return data;
	}
}
