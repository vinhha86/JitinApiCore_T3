package vn.gpay.jitin.core.api.porder;

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

import vn.gpay.jitin.core.api.encode.Encode_porder_getlistsku_request;
import vn.gpay.jitin.core.api.encode.Encode_porder_getlistsku_response;
import vn.gpay.jitin.core.attributevalue.Attributevalue;
import vn.gpay.jitin.core.attributevalue.IAttributeValueService;
import vn.gpay.jitin.core.category.IUnitService;
import vn.gpay.jitin.core.category.Unit;
import vn.gpay.jitin.core.encode.Encode;
import vn.gpay.jitin.core.encode.IEncode_Service;
import vn.gpay.jitin.core.porder.IPOrderService;
import vn.gpay.jitin.core.porder.POrder;
import vn.gpay.jitin.core.porder_product.IPOrder_Product_Service;
import vn.gpay.jitin.core.porder_product.porder_product;
import vn.gpay.jitin.core.porder_product_sku.IPOrder_Product_Sku_Service;
import vn.gpay.jitin.core.porder_product_sku.POrder_Product_SKU;
import vn.gpay.jitin.core.porder_product_sku.POrder_Product_SKU_Encode;
import vn.gpay.jitin.core.security.GpayUser;
import vn.gpay.jitin.core.sku.ISKU_Service;
import vn.gpay.jitin.core.sku.SKU;
import vn.gpay.jitin.core.stockin.StockInD;
import vn.gpay.jitin.core.utils.ResponseMessage;

@RestController
@RequestMapping("/api/v1/porder")
public class porderAPI {
	@Autowired
	IPOrderService porderService;
	@Autowired
	IPOrder_Product_Sku_Service porderskuService;
	@Autowired
	IPOrder_Product_Service porderproductService;
	@Autowired
	ISKU_Service skuService;
	@Autowired
	IUnitService unitService;
	@Autowired
	IAttributeValueService attValService;
	@Autowired
	IEncode_Service encodeService;

	@RequestMapping(value = "/get_list", method = RequestMethod.POST)
	public ResponseEntity<porder_getlist_response> Get_List(HttpServletRequest request,
			@RequestBody porder_getlist_request entity) {
		porder_getlist_response response = new porder_getlist_response();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			long orgrootid_link = user.getRootorgid_link();

			response.data = porderService.get_list(entity.ordercode, orgrootid_link);
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		}

		return new ResponseEntity<porder_getlist_response>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/get_list_sku", method = RequestMethod.POST)
	public ResponseEntity<Encode_porder_getlistsku_response> Get_List_Sku(HttpServletRequest request,
			@RequestBody Encode_porder_getlistsku_request entity) {
		Encode_porder_getlistsku_response response = new Encode_porder_getlistsku_response();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			long orgrootid_link = user.getRootorgid_link();

			response.data = porderService.get_list_sku(entity.ordercode, orgrootid_link, entity.skucode);
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		}

		return new ResponseEntity<Encode_porder_getlistsku_response>(response, HttpStatus.OK);
	}

	@RequestMapping(value = "/get_porder_stockin", method = RequestMethod.POST)
	public ResponseEntity<porder_get_stockin_response> Get_Porder_Stockin(HttpServletRequest request,
			@RequestBody porder_get_stockin_request entity) {
		porder_get_stockin_response response = new porder_get_stockin_response();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			long orgrootid_link = user.getRootorgid_link();
			String ordercode = entity.ordercode;
			List<StockInD> list = new ArrayList<StockInD>();

			POrder porder = porderService.get_by_code(ordercode, orgrootid_link).get(0);
			Long porderid_link = porder.getId();

			// get SKU within porder
			List<POrder_Product_SKU> list_sku = porderskuService.getlist_sku_in_porder(orgrootid_link, porderid_link);
			// get product within porder
			List<porder_product> list_product = porderproductService.get_product_inporder(orgrootid_link,
					porderid_link);

			for (POrder_Product_SKU porder_sku : list_sku) {
				Float unitprice = (float) 0;
				SKU sku = skuService.findOne(porder_sku.getSkuid_link());
				Unit unit = unitService.findOne(porder_sku.getUnitid_link());
				Attributevalue attMau = attValService.findOne(porder_sku.getColorid_link());
				Attributevalue attCo = attValService.findOne(porder_sku.getSizeid_link());

				for (porder_product obj : list_product) {
					if (obj.getProductid_link().equals(porder_sku.getProductid_link())) {
						unitprice = obj.getPrice();
					}
				}

				StockInD stockind = new StockInD();
				stockind.setColorid_link(porder_sku.getColorid_link());
				stockind.setId(null);
				stockind.setOrgrootid_link(orgrootid_link);
				stockind.setSizeid_link(porder_sku.getSizeid_link());
				stockind.setSkucode(porder_sku.getSkucode());
				stockind.setSkuid_link(porder_sku.getSkuid_link());
				stockind.setSkutypeid_link(porder_sku.getSkutypeid_link());
				stockind.setStockinid_link(null);
				stockind.setTimecreate(new Date());
				stockind.setTotalpackage(0);
				stockind.setTotalpackage_order(porder_sku.getPquantity_total());
				stockind.setUnitid_link(porder_sku.getUnitid_link());
				stockind.setUsercreateid_link(user.getId());
				stockind.setUnitprice(unitprice);
				stockind.setSku(sku);
				stockind.setUnit(unit);
				stockind.setPorder_year(porder_sku.getPorder_year());
				stockind.setAttMau(attMau);
				stockind.setAttCo(attCo);

				list.add(stockind);
			}

			response.data = list;
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		}

		return new ResponseEntity<porder_get_stockin_response>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/get_porder_sku_stockin", method = RequestMethod.POST)
	public ResponseEntity<get_sku_by_porder_response> Get_PorderSKU_Stockin(HttpServletRequest request,
			@RequestBody get_sku_by_porder_request entity) {
		get_sku_by_porder_response response = new get_sku_by_porder_response();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			long orgrootid_link = user.getRootorgid_link();
			Long porderid_link = entity.porderid_link;
			List<StockInD> list = new ArrayList<StockInD>();

//			porder porder = porderService.get_by_code(ordercode, orgrootid_link).get(0);

			// get SKU within porder
			List<POrder_Product_SKU> list_sku = porderskuService.getlist_sku_in_porder(orgrootid_link, porderid_link);
			// get product within porder
			List<porder_product> list_product = porderproductService.get_product_inporder(orgrootid_link,
					porderid_link);

			for (POrder_Product_SKU porder_sku : list_sku) {
				Float unitprice = (float) 0;
				SKU sku = skuService.findOne(porder_sku.getSkuid_link());
				Unit unit = null;
				if(porder_sku.getUnitid_link() != 0)
					unitService.findOne(porder_sku.getUnitid_link());
				
				Attributevalue attMau = attValService.findOne(porder_sku.getColorid_link());
				Attributevalue attCo = attValService.findOne(porder_sku.getSizeid_link());

				for (porder_product obj : list_product) {
					if (obj.getProductid_link().equals(porder_sku.getProductid_link())) {
						unitprice = obj.getPrice();
					}
				}

				StockInD stockind = new StockInD();
				stockind.setColorid_link(porder_sku.getColorid_link());
				stockind.setId(null);
				stockind.setOrgrootid_link(orgrootid_link);
				stockind.setSizeid_link(porder_sku.getSizeid_link());
				stockind.setSkucode(porder_sku.getSkucode());
				stockind.setSkuid_link(porder_sku.getSkuid_link());
				stockind.setSkutypeid_link(porder_sku.getSkutypeid_link());
				stockind.setStockinid_link(null);
				stockind.setTimecreate(new Date());
				stockind.setTotalpackage(0);
				stockind.setTotalpackage_order(porder_sku.getPquantity_total());
				stockind.setUnitid_link(porder_sku.getUnitid_link());
				stockind.setUsercreateid_link(user.getId());
				stockind.setUnitprice(unitprice);
				stockind.setSku(sku);
				stockind.setUnit(unit);
				stockind.setPorder_year(porder_sku.getPorder_year());
				stockind.setAttMau(attMau);
				stockind.setAttCo(attCo);

				list.add(stockind);
			}

			response.data = list;
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		}

		return new ResponseEntity<get_sku_by_porder_response>(response, HttpStatus.OK);
	}

	@RequestMapping(value = "/get_porder_sku_encode", method = RequestMethod.POST)
	public ResponseEntity<porder_get_sku_porder_response> Get_Porder_SKU_Encode(HttpServletRequest request,
			@RequestBody porder_get_sku_porder_request entity) {
		porder_get_sku_porder_response response = new porder_get_sku_porder_response();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			long orgrootid_link = user.getRootorgid_link();
			String ordercode = entity.ordercode;
			String skucode = entity.skucode;
			
			List<POrder_Product_SKU_Encode> list = new ArrayList<POrder_Product_SKU_Encode>();

			
			// get SKU within porder
			List<POrder_Product_SKU> list_sku = porderService.get_by_code_and_sku(ordercode, orgrootid_link, skucode);
			Long porderid_link = list_sku.get(0).getPorderid_link();

			for (POrder_Product_SKU porder_sku : list_sku) {
				long skuid_link = porder_sku.getSkuid_link();
				int pquantity_encode = 0;
				List<Encode> list_encode = encodeService.get_encode_by_porder_and_sku(orgrootid_link, porderid_link, skuid_link);
				for(Encode encode : list_encode) {
					pquantity_encode += encode.getAmount_encoded();
				}
				
				POrder_Product_SKU_Encode sku_encode = new POrder_Product_SKU_Encode();
				sku_encode.setId(porder_sku.getId());
				sku_encode.setOrgrootid_link(porder_sku.getOrgrootid_link());
				sku_encode.setPorderid_link(porder_sku.getPorderid_link());
				sku_encode.setPquantity_encode(pquantity_encode);
				sku_encode.setPquantity_total(porder_sku.getPquantity_total());
				sku_encode.setProductid_link(porder_sku.getProductid_link());
				sku_encode.setSkuid_link(porder_sku.getSkuid_link());
				sku_encode.setColor_name(porder_sku.getColor_name());
				sku_encode.setSize_name(porder_sku.getSize_name());
				sku_encode.setSkucode(porder_sku.getSkucode());
				sku_encode.setSkuname(porder_sku.getSkuname());
				sku_encode.setProduct_code(porder_sku.getSku_product_code());
				sku_encode.setPorderyear(porder_sku.getPorder_year());
				sku_encode.setPordercode(porder_sku.getOrdercode());
				
				list.add(sku_encode);
			}
			
			response.data = list;
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		}

		return new ResponseEntity<porder_get_sku_porder_response>(response, HttpStatus.OK);
	}
}
