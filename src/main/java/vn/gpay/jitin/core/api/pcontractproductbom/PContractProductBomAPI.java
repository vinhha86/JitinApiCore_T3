package vn.gpay.jitin.core.api.pcontractproductbom;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import vn.gpay.jitin.core.pcontractbomcolor.IPContractBOMColorService;
import vn.gpay.jitin.core.pcontractbomcolor.PContractBOMColor;
import vn.gpay.jitin.core.pcontractbomsku.IPContractBOMSKUService;
import vn.gpay.jitin.core.pcontractbomsku.PContractBOMSKU;
import vn.gpay.jitin.core.pcontractproductbom.IPContractProductBomService;
import vn.gpay.jitin.core.pcontractproductbom.PContractProductBom;
import vn.gpay.jitin.core.security.GpayUser;
import vn.gpay.jitin.core.utils.ResponseMessage;


@RestController
@RequestMapping("/api/v1/pcontractproductbom")
public class PContractProductBomAPI {
	@Autowired IPContractProductBomService ppbomservice;
	@Autowired IPContractBOMColorService ppbomcolorservice;
	@Autowired IPContractBOMSKUService ppbomskuservice;
	@Autowired IPContractProductAtrributeValueService ppatt_service;
	
	@RequestMapping(value = "/create_pcontract_productbom", method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> CreateProductBom(HttpServletRequest request,
			@RequestBody PContractProductBom_create_request entity) {
		ResponseBase response = new ResponseBase();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			for (Long npl : entity.listnpl) {
				//them vao bang product_bom
				PContractProductBom productbom = new PContractProductBom(); 
				productbom.setProductid_link(entity.productid_link);
				productbom.setMaterialid_link(npl);
				productbom.setAmount((float)0);
				productbom.setLost_ratio((float)0);
				productbom.setDescription("");
				productbom.setCreateduserid_link(user.getId());
				productbom.setCreateddate(new Date());
				productbom.setOrgrootid_link(user.getRootorgid_link());
				productbom.setPcontractid_link(entity.pcontractid_link);
				
				ppbomservice.save(productbom);
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
	
	@RequestMapping(value = "/getlist_pcontract_productbom", method = RequestMethod.POST)
	public ResponseEntity<PContractProductBOM_getbyproduct_response> GetListProductBom(HttpServletRequest request,
			@RequestBody PContractProductBOM_getbyproduct_request entity) {
		PContractProductBOM_getbyproduct_response response = new PContractProductBOM_getbyproduct_response();
		try {
			long pcontractid_link = entity.pcontractid_link;
			long productid_link = entity.productid_link;
			
			response.data = ppbomservice.get_pcontract_productBOMbyid(productid_link, pcontractid_link);
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		}
		return new ResponseEntity<PContractProductBOM_getbyproduct_response>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/update_pcontract_productbom", method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> UpdateProductBom(HttpServletRequest request,
			@RequestBody PContractProductBom_update_request entity) {
		ResponseBase response = new ResponseBase();
		try {
			
			ppbomservice.save(entity.data);
			//Xóa trong bom_color và bom_sku
			long pcontractid_link = entity.data.getPcontractid_link();
			long productid_link = entity.data.getProductid_link();
			long materialid_link = entity.data.getMaterialid_link();
			
			if(entity.isUpdateBOM) {
				List<PContractBOMColor> listcolor = ppbomcolorservice.getcolor_bymaterial_in_productBOMColor
						(pcontractid_link, productid_link, materialid_link);
				for (PContractBOMColor pContractBOMColor : listcolor) {
					ppbomcolorservice.delete(pContractBOMColor);
				}
				
				List<PContractBOMSKU> listsku = ppbomskuservice.getcolor_bymaterial_in_productBOMSKU(pcontractid_link, productid_link, materialid_link);
				for (PContractBOMSKU pContractBOMSKU : listsku) {
					ppbomskuservice.delete(pContractBOMSKU);
				}
			}
			
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		}
		return new ResponseEntity<ResponseBase>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/update_pcontract_productbomcolor", method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> UpdateProductBomColor(HttpServletRequest request,
			@RequestBody PContractBOMColor_update_request entity) {
		ResponseBase response = new ResponseBase();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			long pcontractid_link = entity.data.getPcontractid_link();
			long productid_link = entity.data.getProductid_link();
			long materialid_link = entity.data.getMaterialid_link();
			long colorid_link = entity.colorid_link;
			
			List<PContractBOMColor> listcolor = ppbomcolorservice.getall_material_in_productBOMColor
					(pcontractid_link, productid_link, colorid_link, materialid_link);

			PContractBOMColor pContractBOMColor = new PContractBOMColor();
			if(listcolor.size() > 0) {
				pContractBOMColor = listcolor.get(0);
				pContractBOMColor.setAmount(entity.data.getAmount_color());
			}
			else {
				pContractBOMColor.setAmount(entity.data.getAmount_color());
				pContractBOMColor.setColorid_link(colorid_link);
				pContractBOMColor.setCreateddate(new Date());
				pContractBOMColor.setCreateduserid_link(user.getId());
				pContractBOMColor.setDescription(entity.data.getDescription());
				pContractBOMColor.setId((long) 0);
				pContractBOMColor.setMaterialid_link(materialid_link);
				pContractBOMColor.setOrgrootid_link(user.getRootorgid_link());
				pContractBOMColor.setPcontractid_link(pcontractid_link);
				pContractBOMColor.setProductid_link(productid_link);
			}
			ppbomcolorservice.save(pContractBOMColor);
			
			//update lai bang bom amount = 0
			PContractProductBom pContractProductBom = ppbomservice.findOne(entity.data.getId());
			pContractProductBom.setAmount((float) 0);
			ppbomservice.update(pContractProductBom);
			
			//update lai bang sku bom
			List<PContractBOMSKU> listsku = ppbomskuservice.getmaterial_bycolorid_link(pcontractid_link, productid_link, colorid_link, materialid_link);
			for (PContractBOMSKU pContractBOMSKU : listsku) {
				ppbomskuservice.delete(pContractBOMSKU);
			}			
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		}
		return new ResponseEntity<ResponseBase>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/update_pcontract_productbomsku", method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> UpdateProductBomSKU(HttpServletRequest request,
			@RequestBody PContractBOMSKU_update_request entity) {
		ResponseBase response = new ResponseBase();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			long pcontractid_link = entity.data.getPcontractid_link();
			long productid_link = entity.data.getProductid_link();
			long materialid_link = entity.data.getMaterialid_link();
			long sizeid_link = entity.sizeid_link;
			long colorid_link = entity.colorid_link;
			long skuid_link = ppbomskuservice.getskuid_link_by_color_and_size(colorid_link, sizeid_link, productid_link);
			
			List<PContractBOMSKU> list_sku = ppbomskuservice.getall_material_in_productBOMSKU(pcontractid_link, productid_link, sizeid_link, colorid_link, materialid_link);
			

			PContractBOMSKU pContractBOMSKU = new PContractBOMSKU();
			if(list_sku.size() > 0) {
				pContractBOMSKU = list_sku.get(0);
				pContractBOMSKU.setAmount(entity.value);
			}
			else {
				pContractBOMSKU.setAmount(entity.value);
				pContractBOMSKU.setCreateddate(new Date());
				pContractBOMSKU.setCreateduserid_link(user.getId());
				pContractBOMSKU.setDescription(entity.data.getDescription());
				pContractBOMSKU.setId((long) 0);
				pContractBOMSKU.setMaterialid_link(materialid_link);
				pContractBOMSKU.setOrgrootid_link(user.getRootorgid_link());
				pContractBOMSKU.setPcontractid_link(pcontractid_link);
				pContractBOMSKU.setProductid_link(productid_link);
				pContractBOMSKU.setLost_ratio(entity.data.getLost_ratio());
				pContractBOMSKU.setSkuid_link(skuid_link);
			}
			ppbomskuservice.save(pContractBOMSKU);
			
			//update lai bang bom amount = 0
			PContractProductBom pContractProductBom = ppbomservice.findOne(entity.data.getId());
			pContractProductBom.setAmount((float) 0);
			ppbomservice.update(pContractProductBom);
			
			//update lai bang sku bom
			List<PContractBOMColor> listcolor = ppbomcolorservice.getall_material_in_productBOMColor(
					pcontractid_link, productid_link, colorid_link, materialid_link);
			
			for (PContractBOMColor pColor : listcolor) {
				ppbomcolorservice.delete(pColor);
			}			
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		}
		return new ResponseEntity<ResponseBase>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/getlist_pcontract_productbomcolor", method = RequestMethod.POST)
	public ResponseEntity<PContractProductBOM_getbomcolor_response> GetListProductBomColor(HttpServletRequest request,
			@RequestBody PContractProductBOM_getbomcolor_request entity) {
		PContractProductBOM_getbomcolor_response response = new PContractProductBOM_getbomcolor_response();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			long orgrootid_link = user.getRootorgid_link();
			long pcontractid_link = entity.pcontractid_link;
			long productid_link = entity.productid_link;
			long colorid_link = entity.colorid_link;
			List<Map<String, String>> listdata = new ArrayList<Map<String, String>>();			
			
			List<PContractProductBom> listbom = ppbomservice.get_pcontract_productBOMbyid(productid_link, pcontractid_link);
			List<PContractBOMColor> listbomcolor = ppbomcolorservice.getall_material_in_productBOMColor(pcontractid_link, productid_link, colorid_link, (long)0);
			List<PContractBOMSKU> listbomsku = ppbomskuservice.getmaterial_bycolorid_link(pcontractid_link, productid_link, colorid_link, 0);
			
			List<Long> List_size = ppatt_service.getvalueid_by_product_and_pcontract_and_attribute(orgrootid_link, pcontractid_link, productid_link, (long) 30);
					//ppbomskuservice.getsize_bycolor(pcontractid_link, productid_link, colorid_link);
			
			for (PContractProductBom pContractProductBom : listbom) {
				Map<String, String> map = new HashMap();
				List<PContractBOMColor> listbomcolorclone = new ArrayList<PContractBOMColor>(listbomcolor);
				listbomcolorclone.removeIf(c -> !c.getMaterialid_link().equals(pContractProductBom.getMaterialid_link()));
				
				Float amount_color = (float) 0;
				if(listbomcolorclone.size() > 0)
					amount_color = listbomcolorclone.get(0).getAmount();
				
				map.put("amount", pContractProductBom.getAmount().toString());
				
				map.put("amount_color", amount_color.toString());
				
				map.put("coKho", pContractProductBom.getCoKho().toString());
				
				map.put("createddate", pContractProductBom.getCreateddate().toString());
				
				map.put("createduserid_link", pContractProductBom.getCreateduserid_link().toString());
				
				map.put("description", pContractProductBom.getDescription().toString());
				
				map.put("id", pContractProductBom.getId().toString());
				
				map.put("lost_ratio", pContractProductBom.getLost_ratio().toString());
				
				map.put("materialid_link", pContractProductBom.getMaterialid_link().toString());
				
				map.put("materialName", pContractProductBom.getMaterialName().toString());
				
				map.put("orgrootid_link", pContractProductBom.getOrgrootid_link().toString());
				
				map.put("pcontractid_link", pContractProductBom.getPcontractid_link().toString());
				
				map.put("product_type", pContractProductBom.getProduct_type()+"");
				
				map.put("product_typename", pContractProductBom.getProduct_typeName().toString());
				
				map.put("productid_link", pContractProductBom.getProductid_link().toString());
				
				map.put("tenMauNPL", pContractProductBom.getTenMauNPL().toString());
				
				map.put("thanhPhanVai", pContractProductBom.getThanhPhanVai().toString());
				
				map.put("unitName", pContractProductBom.getUnitName().toString());
				
				for(Long size : List_size) {
					List<PContractBOMSKU> listbomsku_clone = new ArrayList<PContractBOMSKU>(listbomsku);
					long skuid_link = ppbomskuservice.getskuid_link_by_color_and_size(colorid_link, size, productid_link);
					listbomsku_clone.removeIf(c -> !c.getMaterialid_link().equals(pContractProductBom.getMaterialid_link()) || 
							!c.getSkuid_link().equals(skuid_link));
					Float amount_size = (float) 0;
					if(listbomsku_clone.size() > 0)
						amount_size = listbomsku_clone.get(0).getAmount();
					map.put(""+size, amount_size+"");
				}
				
				listdata.add(map);
			}
			
			response.data = listdata;
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		}
		return new ResponseEntity<PContractProductBOM_getbomcolor_response>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/deletematerial", method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> DeleteMaterial(HttpServletRequest request,
			@RequestBody PContractProductBom_delete_material_request entity) {
		ResponseBase response = new ResponseBase();
		try {
			long pcontractid_link = entity.pcontractid_link;
			long productid_link = entity.productid_link;
			long materialid_link = entity.materialid_link;
			
			//xoa trong bang pcontract_product_bom
			List<PContractProductBom> list_bom = ppbomservice.getby_pcontract_product_material(productid_link, pcontractid_link, materialid_link);
			for(PContractProductBom bom : list_bom) {
				ppbomservice.delete(bom);
			}
			
			//Xoa trong bang pcontract_product_color_bom
			List<PContractBOMColor> list_bom_color = ppbomcolorservice.getcolor_bymaterial_in_productBOMColor(pcontractid_link, productid_link, materialid_link);
			for(PContractBOMColor color : list_bom_color) {
				ppbomcolorservice.delete(color);
			}
			
			//Xoa trong bang pcontract_sku_bom
			List<PContractBOMSKU> list_bom_sku = ppbomskuservice.getcolor_bymaterial_in_productBOMSKU(pcontractid_link, productid_link, materialid_link);
			for(PContractBOMSKU sku : list_bom_sku) {
				ppbomskuservice.delete(sku);
			}
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		}
		return new ResponseEntity<ResponseBase>(response, HttpStatus.OK);
	}
}
