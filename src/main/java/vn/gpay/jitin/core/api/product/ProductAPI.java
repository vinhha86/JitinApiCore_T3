package vn.gpay.jitin.core.api.product;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import vn.gpay.jitin.core.attribute.Attribute;
import vn.gpay.jitin.core.attribute.IAttributeService;
import vn.gpay.jitin.core.base.ResponseBase;
import vn.gpay.jitin.core.product.IProductBomService;
import vn.gpay.jitin.core.product.IProductService;
import vn.gpay.jitin.core.product.Product;
import vn.gpay.jitin.core.product.ProductBOM;
import vn.gpay.jitin.core.product.ProductBinding;
import vn.gpay.jitin.core.product.ProductImg;
import vn.gpay.jitin.core.productattributevalue.IProductAttributeService;
import vn.gpay.jitin.core.productattributevalue.ProductAttributeValue;
import vn.gpay.jitin.core.productattributevalue.ProductAttributeValueBinding;
import vn.gpay.jitin.core.security.GpayUser;
import vn.gpay.jitin.core.sku.ISKU_AttributeValue_Service;
import vn.gpay.jitin.core.sku.ISKU_Service;
import vn.gpay.jitin.core.utils.ResponseMessage;


@RestController
@RequestMapping("/api/v1/product")
public class ProductAPI {
	@Autowired
	IProductService productService;
	@Autowired
	IAttributeService attrService;
	@Autowired
	IProductAttributeService pavService;
	@Autowired
	ISKU_AttributeValue_Service skuattService;
	@Autowired
	ISKU_Service skuService;
	@Autowired
	IProductBomService productbomservice;

	@RequestMapping(value = "/getall", method = RequestMethod.POST)
	public ResponseEntity<Product_getall_response> Product_GetAll(HttpServletRequest request,
			@RequestBody Product_getall_request entity) {
		Product_getall_response response = new Product_getall_response();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			Page<Product> pProduct = productService.getall_by_orgrootid_paging(user.getRootorgid_link(), entity);
			List<Product> lstproduct = pProduct.getContent();
			List<ProductBinding> lstdata = new ArrayList<>();
			String FolderPath = "";
			
			for (Product product : lstproduct) {
				ProductBinding pb = new ProductBinding();
				
				pb.setId(product.getId());
				pb.setCode(product.getCode());
				pb.setName(product.getName());
				pb.setProduct_type(product.getProduct_type());
				pb.setProduct_typeName(product.getProduct_typeName());
				pb.setCoKho(product.getCoKho());
				pb.setThanhPhanVai(product.getThanhPhanVai());
				pb.setTenMauNPL(product.getTenMauNPL());
				pb.setDesignerName(product.getDesignerName());
				
				switch ((int)product.getProduct_type()) {
				case 1:
					FolderPath = "upload/product";
					break;
				case 2:
					FolderPath = "upload/material";
					break;
				case 3:
					FolderPath = "upload/sewingtrim";
					break;
				case 4:
					FolderPath = "upload/packingtrim";
					break;
				default:
					break;
				}
				String uploadRootPath = request.getServletContext().getRealPath(FolderPath);
				
				pb.setUrlimage(getimg(product.getImgurl1(),uploadRootPath));
				lstdata.add(pb);
			}
			
			response.pagedata = lstdata;
			response.totalCount = pProduct.getTotalElements();
			response.data = productService.getall_by_orgrootid(user.getRootorgid_link(),
					entity.product_type);
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<Product_getall_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<Product_getall_response>(response, HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/getproductbom", method = RequestMethod.POST)
	public ResponseEntity<ProductBOM_getbyid_response> GetProductBom(HttpServletRequest request,
			@RequestBody ProductBOM_getbyid_request entity) {
		ProductBOM_getbyid_response response = new ProductBOM_getbyid_response();
		try {
			response.data = productbomservice.getproductBOMbyid(entity.productid_link);
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ProductBOM_getbyid_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<ProductBOM_getbyid_response>(response, HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/createproductbom", method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> CreateProductBom(HttpServletRequest request,
			@RequestBody ProductBOM_create_request entity) {
		ResponseBase response = new ResponseBase();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			for (Long npl : entity.listnpl) {
				ProductBOM productbom = new ProductBOM(); 
				productbom.setProductid_link(entity.productid_link);
				productbom.setMaterialid_link(npl);
				productbom.setUnitid_link((long)0);
				productbom.setAmount((float)0);
				productbom.setLost_ratio((float)0);
				productbom.setDescription("");
				productbom.setCreateduserid_link(user.getId());
				productbom.setCreateddate(new Date());
				productbom.setOrgrootid_link(user.getRootorgid_link());
				
				productbomservice.save(productbom);
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
	
	@RequestMapping(value = "/updateproductbom", method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> UpdateProductBom(HttpServletRequest request,
			@RequestBody ProductBOM_update_request entity) {
		ResponseBase response = new ResponseBase();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			ProductBOM productbom = entity.data;
			if(productbom.getId() == null || productbom.getId() == 0) {
				productbom.setOrgrootid_link(user.getRootorgid_link());
				productbom.setCreateduserid_link(user.getId());
				productbom.setCreateddate(new Date());
			}
			else {
				ProductBOM productbom_old = productbomservice.findOne(productbom.getId());
				productbom.setOrgrootid_link(productbom_old.getOrgrootid_link());
				productbom.setCreateduserid_link(productbom_old.getCreateduserid_link());
				productbom.setCreateddate(productbom_old.getCreateddate());
			}
			productbomservice.save(productbom);
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ResponseBase>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<ResponseBase>(response, HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/deleteproductbom", method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> DeleteProductBom(HttpServletRequest request,
			@RequestBody Product_deletebom_request entity) {
		ResponseBase response = new ResponseBase();
		try {
			productbomservice.deleteById(entity.id);
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ResponseBase>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<ResponseBase>(response, HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/getmaterial_notinbom", method = RequestMethod.POST)
	public ResponseEntity<Product_getmaterial_notinbom_response> Product_Get_Material_NotinBom(HttpServletRequest request,
			@RequestBody Product_getmaterial_notinbom_request entity) {
		Product_getmaterial_notinbom_response response = new Product_getmaterial_notinbom_response();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			Long orgrootid_link = user.getRootorgid_link();
			
			List<Product> data = productService.getList_material_notin_PContractProductBOM(orgrootid_link, entity.code, entity.name,
					entity.tenmaunpl, entity.productid_link, entity.product_type, entity.pcontractid_link);
			List<ProductBinding> lstdata = new ArrayList<ProductBinding>();
			String FolderPath ="";
			
			for (Product product : data) {
				ProductBinding pb = new ProductBinding();
				pb.setId(product.getId());
				pb.setCode(product.getCode());
				pb.setName(product.getName());
				pb.setProduct_type(product.getProduct_type());
				pb.setProduct_typeName(product.getProduct_typeName());
				pb.setCoKho(product.getCoKho());
				pb.setThanhPhanVai(product.getThanhPhanVai());
				pb.setTenMauNPL(product.getTenMauNPL());
				
				switch ((int)product.getProduct_type()) {
				case 1:
					FolderPath = "upload/product";
					break;
				case 2:
					FolderPath = "upload/material";
					break;
				case 3:
					FolderPath = "upload/sewingtrim";
					break;
				case 4:
					FolderPath = "upload/packingtrim";
					break;
				default:
					break;
				}
				String uploadRootPath = request.getServletContext().getRealPath(FolderPath);
				
				pb.setUrlimage(getimg(product.getImgurl1(),uploadRootPath));
				lstdata.add(pb);
			}
			response.data = lstdata;
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<Product_getmaterial_notinbom_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<Product_getmaterial_notinbom_response>(response, HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/getmaterial_notinpcontractproductbom", method = RequestMethod.POST)
	public ResponseEntity<Product_getmaterial_notinbom_response> Product_Get_Material_NotinPContractProductBom(HttpServletRequest request,
			@RequestBody Product_getall_npl_notin_pcontractproductbom_request entity) {
		Product_getmaterial_notinbom_response response = new Product_getmaterial_notinbom_response();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			Long orgrootid_link = user.getRootorgid_link();
			
			List<Product> data = productService.getList_material_notin_PContractProductBOM(orgrootid_link, entity.code, entity.name, 
					entity.tenmaunpl, entity.productid_link, entity.product_type, entity.pcontractid_link);
			List<ProductBinding> lstdata = new ArrayList<ProductBinding>();
			String FolderPath ="";
			
			for (Product product : data) {
				ProductBinding pb = new ProductBinding();
				pb.setId(product.getId());
				pb.setCode(product.getCode());
				pb.setName(product.getName());
				pb.setProduct_type(product.getProduct_type());
				pb.setProduct_typeName(product.getProduct_typeName());
				pb.setCoKho(product.getCoKho());
				pb.setThanhPhanVai(product.getThanhPhanVai());
				pb.setTenMauNPL(product.getTenMauNPL());
				
				switch ((int)product.getProduct_type()) {
				case 1:
					FolderPath = "upload/product";
					break;
				case 2:
					FolderPath = "upload/material";
					break;
				case 3:
					FolderPath = "upload/sewingtrim";
					break;
				case 4:
					FolderPath = "upload/packingtrim";
					break;
				default:
					break;
				}
				String uploadRootPath = request.getServletContext().getRealPath(FolderPath);
				
				pb.setUrlimage(getimg(product.getImgurl1(),uploadRootPath));
				lstdata.add(pb);
			}
			response.data = lstdata;
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<Product_getmaterial_notinbom_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<Product_getmaterial_notinbom_response>(response, HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/getone", method = RequestMethod.POST)
	public ResponseEntity<Product_getOne_Response> Product_GetOne(HttpServletRequest request,
			@RequestBody Product_getOne_request entity) {
		Product_getOne_Response response = new Product_getOne_Response();
		try {
			Product product = productService.findOne(entity.id);
			response.data = product;
			
			ProductImg pimg = new ProductImg();
			String FolderPath ="";
			switch ((int)product.getProduct_type()) {
			case 1:
				FolderPath = "upload/product";
				break;
			case 2:
				FolderPath = "upload/material";
				break;
			case 3:
				FolderPath = "upload/sewingtrim";
				break;
			case 4:
				FolderPath = "upload/packingtrim";
				break;
			default:
				break;
			}
			String uploadRootPath = request.getServletContext().getRealPath(FolderPath);
			pimg.img1 = getimg(response.data.getImgurl1(),uploadRootPath);
			pimg.img2 = getimg(response.data.getImgurl2(),uploadRootPath);
			pimg.img3 = getimg(response.data.getImgurl3(),uploadRootPath);
			pimg.img4 = getimg(response.data.getImgurl4(),uploadRootPath);
			pimg.img5 = getimg(response.data.getImgurl5(),uploadRootPath);
			
			response.img = pimg;
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<Product_getOne_Response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<Product_getOne_Response>(response, HttpStatus.OK);
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

	@RequestMapping(value = "/getattrvalue", method = RequestMethod.POST)
	public ResponseEntity<Product_getattvalue_response> Product_GetAttributeValue(HttpServletRequest request,
			@RequestBody Product_getattvalue_request entity) {
		Product_getattvalue_response response = new Product_getattvalue_response();
		
		response.data = new ArrayList<ProductAttributeValueBinding>();
		
		try {
			List<ProductAttributeValue> lst = pavService.getall_byProductId(entity.id);
			
			for (ProductAttributeValue productAttributeValue : lst) {
				ProductAttributeValueBinding obj = new ProductAttributeValueBinding();
				
				obj.setAttributeName(productAttributeValue.getAttributeName());
				obj.setAttributeid_link(productAttributeValue.getAttributeid_link());
				obj.setAttributeValueName("");
				
				boolean isExist = false;
				
				for (ProductAttributeValueBinding binding : response.data) {
					if (binding.getAttributeid_link() == obj.getAttributeid_link()){
						isExist = true;
						break;
					}
				}
				
				if(!isExist) {
					response.data.add(obj);
				}
			}

			for (ProductAttributeValueBinding binding : response.data) {
				String name = "";
				for (ProductAttributeValue productAttributeValue : lst) {
					if (productAttributeValue.getAttributeName() == binding.getAttributeName()) {
						String attName = productAttributeValue.getAttributeValueName();
						
						if (attName != "") {
							if (name == "") {
								name += productAttributeValue.getAttributeValueName();
							} else {
								name += ", " + productAttributeValue.getAttributeValueName();
							}
						}
					}
				}
				binding.setAttributeValueName(name);
			}

			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<Product_getattvalue_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<Product_getattvalue_response>(response, HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/createproduct", method = RequestMethod.POST)
	public ResponseEntity<Product_create_response> Product_Create(@RequestBody Product_create_request entity,
			HttpServletRequest request) {
		Product_create_response response = new Product_create_response();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			Product product = entity.data;
			long orgrootid_link = user.getRootorgid_link();
			
			if (product.getId() == null || product.getId() == 0) {
				product.setOrgrootid_link(user.getRootorgid_link());
				product.setUsercreateid_link(user.getId());
				product.setTimecreate(new Date());
			} else {
				Product product_old = productService.findOne(product.getId());
				product.setOrgrootid_link(product_old.getOrgrootid_link());
				product.setUsercreateid_link(product_old.getUsercreateid_link());
				product.setTimecreate(product_old.getTimecreate());
			}

			List<Product> pcheck = productService.getone_by_code(orgrootid_link,product.getCode(),
					product.getId(), product.getProduct_type());

			if (pcheck.size() > 0) {
				response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
				response.setMessage("Mã đã tồn tại trong hệ thống!");
			} else {
				product = productService.save(product);

				// sau khi lưu sản phẩm xong thì tự động insert các thuộc tính của sản phẩm vào
				List<Attribute> lstAttr = attrService.getList_attribute_forproduct(product.getProduct_type(),
						user.getRootorgid_link());
				for (Attribute attribute : lstAttr) {
					ProductAttributeValue pav = new ProductAttributeValue();
					pav.setId((long) 0);
					pav.setProductid_link(product.getId());
					pav.setAttributeid_link(attribute.getId());
					pav.setAttributevalueid_link((long) 0);
					pav.setOrgrootid_link(user.getRootorgid_link());
					pavService.save(pav);
				}

				response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
				response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			}

			response.id = product.getId();
			return new ResponseEntity<Product_create_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<Product_create_response>(response, HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/update_productpair", method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> ProductPair_Update(@RequestBody Product_update_productpair_request entity,
			HttpServletRequest request) {
		ResponseBase response = new ResponseBase();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			Product product = productService.findOne(entity.id);
			
			product.setCode(entity.code);
			product.setName(entity.name);

			List<Product> pcheck = productService.getone_by_code(user.getRootorgid_link(),
					product.getCode(), product.getId(), product.getProduct_type());

			if (pcheck.size() > 0) {
				response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
				response.setMessage("Mã đã tồn tại trong hệ thống!");
			} else {
				product = productService.save(product);

				response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
				response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			}
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		}
		return new ResponseEntity<ResponseBase>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/getimg", method = RequestMethod.POST)
	public ResponseEntity<Product_viewimg_response> GetImage(HttpServletRequest request, @RequestBody Product_viewimg_request entity) {
		Product_viewimg_response response = new Product_viewimg_response();
		try {
			String FolderPath ="";
			switch (entity.product_type) {
			case 1:
				FolderPath = "upload/product";
				break;
			case 2:
				FolderPath = "upload/material";
				break;
			case 3:
				FolderPath = "upload/sewingtrim";
				break;
			case 4:
				FolderPath = "upload/packingtrim";
				break;
			default:
				break;
			}
			Product product = productService.findOne(entity.id);
			String filename = product.getCode()+"_"+entity.img+"."+entity.ext;
			String uploadRootPath = request.getServletContext().getRealPath(FolderPath);
			String filePath = uploadRootPath+"\\"+ filename;
			Path path = Paths.get(filePath);
			byte[] data = Files.readAllBytes(path);
			response.data = data;
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<Product_viewimg_response>(response, HttpStatus.OK);
		}
		catch(Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<Product_viewimg_response>(response, HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/viewimg", method = RequestMethod.POST)
	public ResponseEntity<Product_viewimg_response> ViewImage(HttpServletRequest request, 
			@RequestBody Product_viewimg_request entity) {
		Product_viewimg_response response = new Product_viewimg_response();
		try {
			String FolderPath ="";
			switch (entity.product_type) {
			case 1:
				FolderPath = "upload/product";
				break;
			case 2:
				FolderPath = "upload/material";
				break;
			case 3:
				FolderPath = "upload/sewingtrim";
				break;
			case 4:
				FolderPath = "upload/packingtrim";
				break;
			default:
				break;
			}
			Product product = productService.findOne(entity.id);
			String filename = product.getCode()+"_"+entity.img+"."+entity.ext;
			String uploadRootPath = request.getServletContext().getRealPath(FolderPath);
			String filePath = uploadRootPath+"\\"+ filename;
			Path path = Paths.get(filePath);
			byte[] data = Files.readAllBytes(path);
			response.data = data;
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<Product_viewimg_response>(response, HttpStatus.OK);
		}
		catch(Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<Product_viewimg_response>(response, HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/updateimg", method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> Product_updteImg(HttpServletRequest request,
			@RequestParam("file") MultipartFile file, @RequestParam("id") int id, @RequestParam("img") int img) {
		ResponseBase response = new ResponseBase();

		try {
			Product product = productService.findOne(id);
			String FolderPath ="";
			// Thư mục gốc upload file.
			switch (product.getProduct_type()) {
			case 1:
				FolderPath = "upload/product";
				break;
			case 2:
				FolderPath = "upload/material";
				break;
			case 3:
				FolderPath = "upload/sewingtrim";
				break;
			case 4:
				FolderPath = "upload/packingtrim";
				break;

			default:
				break;
			}
			String uploadRootPath = request.getServletContext().getRealPath(FolderPath);

			File uploadRootDir = new File(uploadRootPath);
			// Tạo thư mục gốc upload nếu nó không tồn tại.
			if (!uploadRootDir.exists()) {
				uploadRootDir.mkdirs();
			}

			String name = file.getOriginalFilename();		
			if (name != null && name.length() > 0) {
				String[] str = name.toString().split("\\.");
				String extend = str[str.length -1];	
				name = product.getCode()+"_"+img+"."+extend;
				File serverFile = new File(uploadRootDir.getAbsolutePath() + File.separator + name);

				BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
				stream.write(file.getBytes());
				stream.close();
			}
			
			switch (img) {
			case 1:
				product.setImgurl1(name);
				break;
			case 2:
				product.setImgurl2(name);
				break;
			case 3:
				product.setImgurl3(name);
				break;
			case 4:
				product.setImgurl4(name);
				break;
			case 5:
				product.setImgurl5(name);
				break;

			default:
				break;
			}
			productService.save(product);
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
	public ResponseEntity<ResponseBase> Prduct_Delete(@RequestBody Product_delete_request entity,
			HttpServletRequest request) {
		ResponseBase response = new ResponseBase();

		try {
			// Xóa bảng product : update status = -1
			Product p = productService.findOne(entity.id);
			p.setStatus(-1);
			productService.save(p);

			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ResponseBase>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<ResponseBase>(response, HttpStatus.OK);
		}
	}
}
