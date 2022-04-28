package vn.gpay.jitin.core.api.pcontractattributevalue;

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

import vn.gpay.jitin.core.base.ResponseBase;
import vn.gpay.jitin.core.pcontract.IPContractService;
import vn.gpay.jitin.core.pcontractattributevalue.IPContractProductAtrributeValueService;
import vn.gpay.jitin.core.pcontractattributevalue.PContractAttributeValue;
import vn.gpay.jitin.core.pcontractattributevalue.PContractAttributeValueBinding;
import vn.gpay.jitin.core.pcontractproductcolor.IPContractProductColorService;
import vn.gpay.jitin.core.pcontractproductcolor.PContractProductColor;
import vn.gpay.jitin.core.pcontratproductsku.IPContractProductSKUService;
import vn.gpay.jitin.core.pcontratproductsku.PContractProductSKU;
import vn.gpay.jitin.core.product.IProductService;
import vn.gpay.jitin.core.product.Product;
import vn.gpay.jitin.core.productattributevalue.IProductAttributeService;
import vn.gpay.jitin.core.productattributevalue.ProductAttributeValue;
import vn.gpay.jitin.core.security.GpayUser;
import vn.gpay.jitin.core.sku.ISKU_AttributeValue_Service;
import vn.gpay.jitin.core.sku.ISKU_Service;
import vn.gpay.jitin.core.sku.SKU;
import vn.gpay.jitin.core.sku.SKU_Attribute_Value;
import vn.gpay.jitin.core.utils.ResponseMessage;


@RestController
@RequestMapping("/api/v1/pcontractattvalue")
public class PContractAttrValueAPI {
	@Autowired IPContractProductAtrributeValueService ppavservice;
	@Autowired IProductService productService;
	@Autowired IProductAttributeService productavService;
	@Autowired IPContractService pcontractService;
	@Autowired IPContractProductColorService ppcolorService;
	@Autowired ISKU_AttributeValue_Service skuavService;
	@Autowired ISKU_Service skuService;
	@Autowired IPContractProductSKUService ppskuService;
	@Autowired IProductAttributeService pavService;
	
	@RequestMapping(value = "/getattributebyproduct",method = RequestMethod.POST)
	public ResponseEntity<PContractProduct_getbyproduct_response> Attribute_GetbyProduct(HttpServletRequest request, @RequestBody PContractProductAttValue_getbyproduct_request entity ) {
		PContractProduct_getbyproduct_response response = new PContractProduct_getbyproduct_response();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			long orgrootid_link = user.getRootorgid_link();
			long pcontractid_link = entity.pcontractid_link;
			long productid_link = entity.productid_link;
			
			List<PContractAttributeValue> lstpav = ppavservice.getattribute_by_product_and_pcontract(orgrootid_link, pcontractid_link, productid_link);
			response.data = new ArrayList<PContractAttributeValueBinding>();
			for (PContractAttributeValue pContractAttributeValue : lstpav) {
				PContractAttributeValueBinding binding = new PContractAttributeValueBinding();
				
				binding.setAttributeid_link(pContractAttributeValue.getAttributeid_link());
				binding.setAttributeName(pContractAttributeValue.getAttributeName());
				if (!response.data.contains(binding)) 
					response.data.add(0, binding);
			}
			
			for (PContractAttributeValueBinding binding : response.data) {
				String name = "";
				String id = "";
				for (PContractAttributeValue pContractAttributeValue : lstpav) {
					if (binding.getAttributeid_link().equals(pContractAttributeValue.getAttributeid_link())) {
						if (name.equals("")) {
							name += pContractAttributeValue.getAttributevalueName();
							id += pContractAttributeValue.getAttributevalueid_link() == 0 ? "" : pContractAttributeValue.getAttributevalueid_link();
						} else {
							name += ", " + pContractAttributeValue.getAttributevalueName();
							id += "," + pContractAttributeValue.getAttributevalueid_link();
						}
					}
				}
				binding.setAttributeValueName(name);
				binding.setList_attributevalueid(id);
			}
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<PContractProduct_getbyproduct_response>(response,HttpStatus.OK);
		}
		catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_EXCEPTION));
		    return new ResponseEntity<PContractProduct_getbyproduct_response>(HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/getvaluebyproduct",method = RequestMethod.POST)
	public ResponseEntity<PContractAttributeValue_getbyproduct_response> Attributevalue_GetbyProduct(HttpServletRequest request, @RequestBody PContractAttributeValue_getvalue_byprouct_request entity ) {
		PContractAttributeValue_getbyproduct_response response = new PContractAttributeValue_getbyproduct_response();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			long orgrootid_link = user.getRootorgid_link();
			long pcontractid_link = entity.pcontractid_link;
			long productid_link = entity.productid_link;
			long attributeid_link = entity.attributeid_link;
			
			response.data = ppavservice.getvalue_by_product_and_pcontract_and_attribute(orgrootid_link, pcontractid_link, productid_link, attributeid_link);
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<PContractAttributeValue_getbyproduct_response>(response,HttpStatus.OK);
		}
		catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_EXCEPTION));
		    return new ResponseEntity<PContractAttributeValue_getbyproduct_response>(HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/createattribute",method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> Attribute_Create(HttpServletRequest request, @RequestBody PContractAttrbuteValue_insertattribute_request entity ) {
		ResponseBase response = new ResponseBase();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			long orgrootid_link = user.getRootorgid_link();
			long pcontractid_link = entity.pcontractid_link;
			long productid_link = entity.productid_link;
			
			for (long attributeid_link : entity.listId) {
				PContractAttributeValue pcav = new PContractAttributeValue();
				pcav.setId((long)0);
				pcav.setOrgrootid_link(orgrootid_link);
				pcav.setAttributeid_link(attributeid_link);
				pcav.setPcontractid_link(pcontractid_link);
				pcav.setProductid_link(productid_link);
				pcav.setAttributevalueid_link((long)0);
				ppavservice.save(pcav);
			}
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
		}
		catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_EXCEPTION));
		    return new ResponseEntity<ResponseBase>(response, HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/deleteAtribute", method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> Attribute_delete(HttpServletRequest request,
			@RequestBody PContractAttributeValue_deleteatt_request entity) {
		ResponseBase response = new ResponseBase();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			long orgrootid_link = user.getRootorgid_link();
			long pcontractid_link = entity.pcontractid_link;
			long productid_link = entity.productid_link;
			long attributeid_link = entity.attributeid_link;
			
			List<PContractAttributeValue> lstValue = ppavservice.getvalue_by_product_and_pcontract_and_attribute(orgrootid_link, pcontractid_link, productid_link, attributeid_link);
			for (PContractAttributeValue pContractAttributeValue : lstValue) {
				ppavservice.delete(pContractAttributeValue);
			}
			
			if(attributeid_link==4 || attributeid_link == 30) {
				//Nếu xóa thuộc tính màu thì xóa cả trong bảng pcontract_product_color
				if(attributeid_link == 4) {
					List<PContractProductColor> lstcolor = ppcolorService.getcolor_by_pcontract_and_product(orgrootid_link, pcontractid_link, productid_link);
					for (PContractProductColor pContractProductColor : lstcolor) {
						ppcolorService.delete(pContractProductColor);
					}
				}
				//Xóa trong bảng sku
				//Lấy danh sách sku của sản phẩm trong đơn hàng
				List<PContractProductSKU> listsku = ppskuService.getlistsku_byproduct_and_pcontract(orgrootid_link, productid_link, pcontractid_link);
				for (PContractProductSKU pContractProductSKU : listsku) {
					ppskuService.delete(pContractProductSKU);
				}
			}
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
		}
		catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		}
		return new ResponseEntity<ResponseBase>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/createattributevalue",method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> AttributeValue_Create(HttpServletRequest request, @RequestBody PConrtactAttributeValue_createvalue_request entity ) {
		ResponseBase response = new ResponseBase();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			long orgrootid_link = user.getRootorgid_link();
			long pcontractid_link = entity.pcontractid_link;
			long productid_link = entity.productid_link;
			long attributeid_link = entity.attributeid_link;
			
			List<PContractAttributeValue> lst = ppavservice.getvalue_by_product_and_pcontract_and_attribute(orgrootid_link, pcontractid_link, productid_link, attributeid_link);
			for (PContractAttributeValue pcontractattributevalue : lst) {
				if(pcontractattributevalue.getAttributevalueid_link() > 0)
				ppavservice.deleteById(pcontractattributevalue.getId());
			}

			for (Long id : entity.listAdd) {
				PContractAttributeValue pcontractattributevalue = new PContractAttributeValue();
				
				pcontractattributevalue.setId((long)0);
				pcontractattributevalue.setAttributeid_link(attributeid_link);
				pcontractattributevalue.setAttributevalueid_link(id);
				pcontractattributevalue.setOrgrootid_link(orgrootid_link);
				pcontractattributevalue.setPcontractid_link(pcontractid_link);
				pcontractattributevalue.setProductid_link(productid_link);
				
				ppavservice.save(pcontractattributevalue);
			}
			
			Long ID_Mau = (long) 4, ID_Co = (long) 30;
			Product product = productService.findOne(entity.productid_link);
//			PContract pcontract = pcontractService.findOne(entity.pcontractid_link);
			
			if(attributeid_link == ID_Mau || attributeid_link == ID_Co) {
				//nếu thay đổi thuộc tính của màu thì sinh thêm vào bảng pcontract_product_color
				if(attributeid_link == ID_Mau) {
					List<PContractProductColor> lstcolor = ppcolorService.getcolor_by_pcontract_and_product(orgrootid_link, pcontractid_link, productid_link);
					for (PContractProductColor pContractProductColor : lstcolor) {
						
						//Xóa những màu trong bảng pconntract_product_color mà ko có trong thuộc tính đơn hàng
						if(!entity.listAdd.contains(pContractProductColor.getColorid_link())) {
							ppcolorService.delete(pContractProductColor);
						}
					}
					
					//Lấy những màu trong thuộc tính mà chưa có trong bảng pconntract_product_color để thêm vào pconntract_product_color
					List<Long> list_colorid = ppcolorService.getcolorid_by_pcontract_and_product(orgrootid_link, pcontractid_link, productid_link);
					entity.listAdd.removeIf(c -> list_colorid.contains(c));
					
					for (Long colorid_link : entity.listAdd) {
						PContractProductColor pColor = new PContractProductColor();
						pColor.setId((long)0);
						pColor.setColorid_link(colorid_link);
						pColor.setOrgrootid_link(orgrootid_link);
						pColor.setPcontractid_link(pcontractid_link);
						pColor.setPquantity(0);
						pColor.setProductid_link(productid_link);
						
						ppcolorService.save(pColor);
					}
				}
				//Lấy danh sách sku của sản phẩm trong đơn hàng

				List<PContractProductSKU> listsku = ppskuService.getlistsku_byproduct_and_pcontract(orgrootid_link, productid_link, pcontractid_link);
				
				//Thêm những màu, cỡ chưa có vào bảng pcontract_product_sku
				List<PContractAttributeValue> lstValueMau = ppavservice.getvalue_by_product_and_pcontract_and_attribute(orgrootid_link, pcontractid_link, productid_link, ID_Mau);
				List<PContractAttributeValue> lstValueCo = ppavservice.getvalue_by_product_and_pcontract_and_attribute(orgrootid_link, pcontractid_link, productid_link, ID_Co);
				//Tạo list để lưu những skuid_link mới nhất
				List<Long> listidskuNew = new ArrayList<Long>();
				
				for (PContractAttributeValue valueCo : lstValueCo) {
					for (PContractAttributeValue valueMau : lstValueMau) {
						long valuemau = valueMau.getAttributevalueid_link();
						long valueco = valueCo.getAttributevalueid_link();

						long skuid_link = skuavService.getsku_byproduct_and_valuemau_valueco(productid_link, valuemau, valueco);
						
						//Nếu trong bảng product_sku chưa có sku cho màu và cỡ thì thêm vào
						if(skuid_link == 0)  {
							//Thêm vào bảng product_attribute_value
							ProductAttributeValue pavMau = new ProductAttributeValue();
							List<ProductAttributeValue> lstpavmau = pavService.getOne_byproduct_and_value(productid_link, 4, valuemau);
							
							if(lstpavmau.size() == 0) {
								pavMau.setAttributeid_link(ID_Mau);
								pavMau.setAttributevalueid_link(valuemau);
								pavMau.setId((long)0);
								pavMau.setOrgrootid_link(orgrootid_link);
								pavMau.setProductid_link(productid_link);
								pavService.save(pavMau);
							}
							
							ProductAttributeValue pavCo = new ProductAttributeValue();
							List<ProductAttributeValue> lstpavco = pavService.getOne_byproduct_and_value(productid_link, 30, valueco);
							if(lstpavco.size() == 0) {
								pavCo.setAttributeid_link(ID_Co);
								pavCo.setAttributevalueid_link(valueco);
								pavCo.setId((long)0);
								pavCo.setOrgrootid_link(orgrootid_link);
								pavCo.setProductid_link(productid_link);
								pavService.save(pavCo);
							}
							
							
							//Thêm vào bảng product_sku
							if (skuid_link == 0) {
								SKU sku = new SKU();
								
								sku.setId(skuid_link);
								sku.setName(getNameSKU(product));
								sku.setProductid_link(product.getId());
								sku.setOrgrootid_link(orgrootid_link);
								sku.setSkutypeid_link(product.getProduct_type());

								sku = skuService.save(sku);
								skuid_link = sku.getId();
								
								//Thêm vào bảng sku_attribute_value
								
								SKU_Attribute_Value savMau = new SKU_Attribute_Value();
								savMau.setId((long) 0);
								savMau.setAttributevalueid_link(valueMau.getAttributevalueid_link());
								savMau.setAttributeid_link(ID_Mau);
								savMau.setOrgrootid_link(user.getRootorgid_link());
								savMau.setSkuid_link(skuid_link);
								savMau.setUsercreateid_link(user.getId());
								savMau.setTimecreate(new Date());

								skuavService.save(savMau);

								SKU_Attribute_Value savCo = new SKU_Attribute_Value();
								savCo.setId((long) 0);
								savCo.setAttributevalueid_link(valueCo.getAttributevalueid_link());
								savCo.setAttributeid_link(ID_Co);
								savCo.setOrgrootid_link(user.getRootorgid_link());
								savCo.setSkuid_link(skuid_link);
								savCo.setUsercreateid_link(user.getId());
								savCo.setTimecreate(new Date());

								skuavService.save(savCo);
							}
							
						}
						listidskuNew.add(skuid_link);
						
						List<PContractProductSKU> lstppSku = new ArrayList<PContractProductSKU>(listsku);
						for (PContractProductSKU pContractProductSKU : lstppSku) {
							if((long)pContractProductSKU.getPcontractid_link() == pcontractid_link && 
								(long)pContractProductSKU.getOrgrootid_link() == orgrootid_link && 
								(long)pContractProductSKU.getProductid_link() == productid_link &&
								(long)pContractProductSKU.getSkuid_link() == skuid_link) {
								lstppSku.remove(pContractProductSKU);
								break;
							}
						}
						
						//Nếu chưa có sku thì insert vào bảng pcontract_product_sku
						if(lstppSku.size() == listsku.size()) {
							PContractProductSKU ppsku = new PContractProductSKU();
							ppsku.setId((long)0);
							ppsku.setOrgrootid_link(orgrootid_link);
							ppsku.setPcontractid_link(pcontractid_link);
							ppsku.setPquantity_porder(0);
							ppsku.setPquantity_sample(0);
							ppsku.setPquantity_total(0);
							ppsku.setProductid_link(productid_link);
							ppsku.setSkuid_link(skuid_link);
							
							ppskuService.save(ppsku);
						}
						
					}
				}
				
				//Xóa những sku không có trong pcontract_product_attribute_value
				for (PContractProductSKU ppsku : listsku) {
					if(!listidskuNew.contains((long)ppsku.getSkuid_link())) {
						ppskuService.delete(ppsku);
					}
				}
			}

			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
		}
		catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		}
		return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
	}
	
	private String getNameSKU(Product product) {
		List<SKU> lstSKU = skuService.getlist_byProduct(product.getId());
		if (lstSKU.size() == 0) {
			return product.getCode() + "_" + "1";
		}
		String old_code = lstSKU.get(0).getName();
		String[] obj = old_code.split("_");
		int a = Integer.parseInt(obj[1]);
		return product.getCode() + "_" + (a + 1);
	}
}
