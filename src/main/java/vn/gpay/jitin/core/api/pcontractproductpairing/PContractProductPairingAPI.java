package vn.gpay.jitin.core.api.pcontractproductpairing;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
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

import vn.gpay.jitin.core.api.pcontractproduct.PContractProduct_getall_response;
import vn.gpay.jitin.core.base.ResponseBase;
import vn.gpay.jitin.core.pcontractproduct.IPContractProductService;
import vn.gpay.jitin.core.pcontractproduct.PContractProduct;
import vn.gpay.jitin.core.pcontractproduct.PContractProductBinding;
import vn.gpay.jitin.core.pcontractproductpairing.IPContractProductPairingService;
import vn.gpay.jitin.core.pcontractproductpairing.PContractProductPairing;
import vn.gpay.jitin.core.product.IProductService;
import vn.gpay.jitin.core.product.Product;
import vn.gpay.jitin.core.productpairing.IProductPairingService;
import vn.gpay.jitin.core.productpairing.ProductPairing;
import vn.gpay.jitin.core.security.GpayUser;
import vn.gpay.jitin.core.utils.ResponseMessage;


@RestController
@RequestMapping("/api/v1/pcontractproductpairing")
public class PContractProductPairingAPI {
	@Autowired IPContractProductService ppservice;
	@Autowired IProductPairingService prodctpairservice;
	@Autowired IProductService productService;
	@Autowired IPContractProductPairingService ppPairingservice;
	
	@RequestMapping(value = "/getproductnotpair", method = RequestMethod.POST)
	public ResponseEntity<PContractProduct_getall_response> GetProductNotPair(HttpServletRequest request,
			@RequestBody PContractProductPair_getnotpair_request entity) {
		PContractProduct_getall_response response = new PContractProduct_getall_response();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			long orgrootid_link = user.getRootorgid_link();
			long pcontractid_link = entity.pcontractid_link;
			
			List<PContractProduct> listproduct = ppservice.get_by_product_and_pcontract(orgrootid_link, (long)0, pcontractid_link);
			List<Long> listpair = prodctpairservice.getproductid_pairing_bycontract(orgrootid_link, pcontractid_link);
			
			listproduct.removeIf(c-> listpair.contains(c.getProductid_link()));
			
			List<PContractProductBinding> data = new ArrayList<PContractProductBinding>();
			String FolderPath = "upload/product";
			
			for (PContractProduct pContractProduct : listproduct) {
				PContractProductBinding binding = new PContractProductBinding();
				binding.setId(pContractProduct.getId());
				binding.setOrgrootid_link(orgrootid_link);
				binding.setPcontractid_link(pContractProduct.getPcontractid_link());
				binding.setProductid_link(pContractProduct.getProductid_link());
				binding.setProductCode(pContractProduct.getProductCode());
				binding.setProductName(pContractProduct.getProductName());
				binding.setPquantity(0);
				
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
	
	@RequestMapping(value = "/getproductpairdetail", method = RequestMethod.POST)
	public ResponseEntity<PContractProduct_getall_response> GetProductPair(HttpServletRequest request,
			@RequestBody PContractProductPair_getnotpair_request entity) {
		PContractProduct_getall_response response = new PContractProduct_getall_response();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			long orgrootid_link = user.getRootorgid_link();
			long pcontractid_link = entity.pcontractid_link;
			long productpairid_link = entity.productpairid_link;
			
			List<ProductPairing> listpair = prodctpairservice.getproduct_pairing_detail_bycontract(orgrootid_link, pcontractid_link, productpairid_link);
			
			List<PContractProductBinding> data = new ArrayList<PContractProductBinding>();
			String FolderPath = "upload/product";
			
			for (ProductPairing productPairing : listpair) {
				PContractProductBinding binding = new PContractProductBinding();
				binding.setId(productPairing.getId());
				binding.setOrgrootid_link(orgrootid_link);
				binding.setPcontractid_link(pcontractid_link);
				binding.setProductid_link(productPairing.getProductid_link());
				binding.setProductCode(productPairing.getProductCode());
				binding.setProductName(productPairing.getProductName());
				binding.setAmount(productPairing.getAmount());
				
				String uploadRootPath = request.getServletContext().getRealPath(FolderPath);
				
				binding.setImgproduct(getimg(productPairing.getImgurl1(),uploadRootPath));
				
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
	
	@RequestMapping(value = "/getbypcontract", method = RequestMethod.POST)
	public ResponseEntity<PContractProductPair_getbypcontract_response> GetByPContract(HttpServletRequest request,
			@RequestBody PContractProductPair_getbypcontract_request entity) {
		PContractProductPair_getbypcontract_response response = new PContractProductPair_getbypcontract_response();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			long orgrootid_link = user.getRootorgid_link();
			long pcontractid_link = entity.pcontractid_link;
			
			response.data = ppPairingservice.getall_bypcontract(orgrootid_link, pcontractid_link);
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		}
		return new ResponseEntity<PContractProductPair_getbypcontract_response>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/deleteproductpair", method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> delete_productpair(HttpServletRequest request,
			@RequestBody PContractProductPair_delete_request entity) {
		ResponseBase response = new ResponseBase();
		try {
//			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication()
//					.getPrincipal();
			long id = entity.id;
			ppPairingservice.deleteById(id);
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		}
		return new ResponseEntity<ResponseBase>(response, HttpStatus.OK);
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
	
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> Create(HttpServletRequest request,
			@RequestBody PContractProductPair_create_request entity) {
		ResponseBase response = new ResponseBase();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			long orgrootid_link = user.getRootorgid_link();
			long pcontractid_link = entity.pcontractid_link;
			long productpairid_link = entity.productpairid_link;

			Product product = new Product();
			if(productpairid_link == 0) {
				//Sinh sản phẩm bộ
				product.setId(productpairid_link);				
				product.setProduct_type(5);
				product.setOrgrootid_link(orgrootid_link);
				product.setStatus(1);
				product.setUsercreateid_link(user.getId());
				product.setTimecreate(new Date());
			}
			else {
				product = productService.findOne(productpairid_link);
			}
			
			product.setName(getNameproductPair(entity.listpair));
			
			product = productService.save(product);
			
			//Update vào bảng ProductPairing
			for (ProductPairing productPairing : entity.listpair) {
				productPairing.setProductpairid_link(product.getId());
				productPairing.setOrgrootid_link(orgrootid_link);
				prodctpairservice.save(productPairing);
			}
			
			//Cập nhật lại vào bảng PContract_Product_Pair
			if(productpairid_link == 0) {
				PContractProductPairing ppPair = new PContractProductPairing();
				ppPair.setId((long)0);
				ppPair.setOrgrootid_link(orgrootid_link);
				ppPair.setPcontractid_link(pcontractid_link);
				ppPair.setProductpairid_link(product.getId());
				ppPairingservice.save(ppPair);
			}
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		}
		return new ResponseEntity<ResponseBase>(response, HttpStatus.OK);
	}
	
	public String getNameproductPair(List<ProductPairing> lst) {
		String name = "";
		for (ProductPairing productPairing : lst) {
			long productid_link = productPairing.getProductid_link();
			Product product = productService.findOne(productid_link);
			name += productPairing.getAmount()+"-"+product.getName()+"; ";
		}
		return name;
	}
}
