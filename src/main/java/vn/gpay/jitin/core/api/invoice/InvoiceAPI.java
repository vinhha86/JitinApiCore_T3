package vn.gpay.jitin.core.api.invoice;

import java.util.ArrayList;
//import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import vn.gpay.jitin.core.api.invoice.invoice_getpkl_bylotnumber_request;
import vn.gpay.jitin.core.api.invoice.invoice_getpkl_bylotnumber_response;
import vn.gpay.jitin.core.api.invoice.invoice_insertpkl_request;
import vn.gpay.jitin.core.api.invoice.invoice_insertsku_response;
import vn.gpay.jitin.core.security.GpayUser;
import vn.gpay.jitin.core.api.invoice.invoice_getlotnumber_request;
import vn.gpay.jitin.core.api.invoice.invoice_getlotnumber_response;
import vn.gpay.jitin.core.base.ResponseBase;
import vn.gpay.jitin.core.base.ResponseError;
//import vn.gpay.jitin.core.category.Sku;
import vn.gpay.jitin.core.invoice.IInvoiceDService;
import vn.gpay.jitin.core.invoice.IInvoiceService;
import vn.gpay.jitin.core.invoice.Invoice;
import vn.gpay.jitin.core.invoice.InvoiceD;
import vn.gpay.jitin.core.org.IOrgService;
import vn.gpay.jitin.core.org.Org;
//import vn.gpay.jitin.core.invoice.InvoiceD;
import vn.gpay.jitin.core.packinglist.IPackingListService;
import vn.gpay.jitin.core.packinglist.PackingList;
import vn.gpay.jitin.core.security.GpayAuthentication;
import vn.gpay.jitin.core.sku.ISKU_Service;
import vn.gpay.jitin.core.sku.SKU;
import vn.gpay.jitin.core.utils.DateFormat;
import vn.gpay.jitin.core.utils.ResponseMessage;

@RestController
@RequestMapping("/api/v1/invoice")
public class InvoiceAPI {

	@Autowired IInvoiceService invoiceService;
	@Autowired IInvoiceDService invoiceDService;
	@Autowired IPackingListService packingListService;
	@Autowired ISKU_Service skuService;
	@Autowired IOrgService orgService;
	
	
	@RequestMapping(value = "/invoice_create",method = RequestMethod.POST)
	public ResponseEntity<?> InvoiceCreate(@RequestBody InvoiceCreateRequest entity,HttpServletRequest request ) {
		InvoiceCreateResponse response = new InvoiceCreateResponse();
		try {
			GpayAuthentication user = (GpayAuthentication)SecurityContextHolder.getContext().getAuthentication();
			if(entity.data.size()>0) {
				Invoice invoice = entity.data.get(0);
//				System.out.println(invoice.getId());
				 if(invoice.getId()==null || invoice.getId()==0) {
					invoice.setUsercreateid_link(user.getUserId());
					invoice.setTimecreate(new Date());
					invoice.setOrgid_link(user.getRootorgid_link());
					invoice.setStatus(0);
				 } else {
					invoice.setLastuserupdateid_link(user.getUserId());
					invoice.setLasttimeupdate(new Date());
				 }
//				 System.out.println(invoice.getInvoice_d().size());
				 for(InvoiceD invoiced: invoice.getInvoice_d()){
					 SKU sku = skuService.findOne(invoiced.getSkuid_link());
					 
					 if(invoiced.getId()==null || invoiced.getId()==0) {
						 invoiced.setUsercreateid_link(user.getUserId());
						 invoiced.setTimecreate(new Date());
						 invoiced.setOrgid_link(user.getRootorgid_link());
						 invoiced.setP_skuid_link(invoice.getP_skuid_link());
					 }else {
						 invoiced.setOrgid_link(user.getRootorgid_link());
						 invoiced.setLastuserupdateid_link(user.getUserId());
					     invoiced.setLasttimeupdate(new Date());
					 }
					 
					 float netweight = 0 ,grossweight = 0  ,yds = 0, met = 0, m3 = 0;
					 for(PackingList pklist: invoiced.getPackinglist()){
						 grossweight += (float)pklist.getGrossweight();
						 netweight += (float)pklist.getNetweight();
						 yds += (float)pklist.getYdsorigin();
						 met += (float)pklist.getMet_origin();
						 m3 += (float)pklist.getM3();
						 
						 if(pklist.getId()==null || pklist.getId()==0) {
							 pklist.setUsercreateid_link(user.getUserId());
							 pklist.setTimecreate(new Date());
							 pklist.setOrgrootid_link(user.getRootorgid_link());
							 pklist.setSkuid_link(invoiced.getSkuid_link());
							 pklist.setInvoiceid_link(invoice.getId());
							 if(sku != null) {
								 pklist.setColorid_link(sku.getColorid_link());
							 }
						 }else {
							 pklist.setLastuserupdateid_link(user.getUserId());
							 pklist.setLasttimeupdate(new Date());
						 }
						 invoiced.setGrossweight(grossweight);
						 invoiced.setNetweight(netweight);
						 invoiced.setYds(yds);
						 invoiced.setMet(met);
						 invoiced.setM3(m3);
						 
					 }			
					 if(invoiced.getPackinglist().size() != 0) {
						 invoiced.setTotalpackage(invoiced.getPackinglist().size());
					 }
					 invoiced.setTotalamount(invoiced.getMet() * invoiced.getUnitprice());
				 }
				invoiceService.save(invoice);
				response.id = invoice.getId();
			}
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			e.printStackTrace();
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
//	@RequestMapping(value = "/invoice_create",method = RequestMethod.POST)
//	public ResponseEntity<?> InvoiceCreate(@RequestBody InvoiceCreateRequest entity,HttpServletRequest request ) {
//		InvoiceCreateResponse response = new InvoiceCreateResponse();
//		try {
//			
//			GpayAuthentication user = (GpayAuthentication)SecurityContextHolder.getContext().getAuthentication();
//			if(entity.data.size()>0) {
//				Invoice invoice = entity.data.get(0);
//				List<InvoiceD> invoiceDs = invoice.getInvoice_d();
//				// invoice
//				if(invoice.getId()==null || invoice.getId().equals(0L)) {
//					invoice.setId(0L);
//					invoice.setUsercreateid_link(user.getUserId());
//					invoice.setTimecreate(new Date());
//					invoice.setOrgid_link(user.getOrgId());
//					invoice.setOrgrootid_link(user.getRootorgid_link());
//					invoice.setStatus(0);
//				}else {
//					invoice.setLastuserupdateid_link(user.getUserId());
//				   	invoice.setLasttimeupdate(new Date());
//				}
//				invoice = invoiceService.save(invoice);
//				// invoiceDs
//				for(InvoiceD invoiceD : invoiceDs) {
//					List<PackingList> packingLists = invoiceD.getPackinglist();
//					
//					if(invoiceD.getId()==null || invoiceD.getId().equals(0L)) {
//						invoiceD.setId(0L);
//						invoiceD.setUsercreateid_link(user.getUserId());
//						invoiceD.setTimecreate(new Date());
//						invoiceD.setOrgid_link(user.getOrgId());
//						invoiceD.setOrgrootid_link(user.getRootorgid_link());;
//						invoiceD.setP_skuid_link(invoice.getP_skuid_link());
//						invoiceD.setInvoiceid_link(invoice.getId());
//					}else {
//						invoiceD.setOrgid_link(user.getRootorgid_link());
//						invoiceD.setLastuserupdateid_link(user.getUserId());
//					 	invoiceD.setLasttimeupdate(new Date());
//					}
//					invoiceD = invoiceDService.save(invoiceD);
//					
//					// packingLists
//					for(PackingList packingList : packingLists) {
//						float netweight = 0 ,grossweight = 0  ,yds = 0  ;
//						 grossweight += (float)packingList.getGrossweight();
//						 netweight += packingList.getNetweight();
//						 yds += packingList.getYdsorigin();
//						 
//						 if(packingList.getId()==null || packingList.getId().equals(0L)) {
//							 packingList.setId(0L);
//							 packingList.setUsercreateid_link(user.getUserId());
//							 packingList.setTimecreate(new Date());
//							 packingList.setOrgrootid_link(user.getRootorgid_link());
//							 packingList.setSkuid_link(invoiceD.getSkuid_link());
//							 packingList.setInvoiceid_link(invoice.getId());
//							 packingList.setInvoicedid_link(invoiceD.getId());
//						 }else {
//							 packingList.setLastuserupdateid_link(user.getUserId());
//							 packingList.setLasttimeupdate(new Date());
//						 }
//						 packingList = packingListService.save(packingList);
//						 
//						 invoiceD.setGrossweight(grossweight);
//						 invoiceD.setNetweight(netweight);
//						 invoiceD.setYds(yds);
//					}
//					
//					invoiceD = invoiceDService.save(invoiceD);
//				}
//				
//				invoice = invoiceService.save(invoice);
//				response.id = invoice.getId();
//			}
//			
//			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
//			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
//			return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
//		}catch (RuntimeException e) {
////			e.printStackTrace();
//			ResponseError errorBase = new ResponseError();
//			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
//			errorBase.setMessage(e.getMessage());
//		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
//		}
//	}
	
	@RequestMapping(value = "/getlotnumber",method = RequestMethod.POST)
	public ResponseEntity<?> InvoiceGetLotNumber(@RequestBody invoice_getlotnumber_request entity,HttpServletRequest request ) {
		invoice_getlotnumber_response response = new invoice_getlotnumber_response();
		try {
			long invoicedid_link = entity.invoicedid_link;
			response.data = packingListService.getLotNumber_byinvoiced(invoicedid_link);
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<invoice_getlotnumber_response>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/getpkl_bylotnumber",method = RequestMethod.POST)
	public ResponseEntity<?> InvoiceGetPKL_byLotNumber(@RequestBody invoice_getpkl_bylotnumber_request entity,HttpServletRequest request ) {
		invoice_getpkl_bylotnumber_response response = new invoice_getpkl_bylotnumber_response();
		try {
			long invoicedid_link = entity.invoicedid_link;
			String lotnumber = entity.lotnumber;
			
			response.data = packingListService.getbylotnumber(lotnumber, invoicedid_link);
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<invoice_getpkl_bylotnumber_response>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/invoice_getone",method = RequestMethod.POST)
	public ResponseEntity<?> InvoiceGetone(@RequestBody InvoiceGetoneRequest entity,HttpServletRequest request ) {
		InvoiceResponse response = new InvoiceResponse();
		try {
			GpayAuthentication user = (GpayAuthentication)SecurityContextHolder.getContext().getAuthentication();
			response.data = invoiceService.findInvoiceBySkuCode(user.getOrgId(),entity.invoicenumber, entity.stockcode);
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<InvoiceResponse>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@RequestMapping(value = "/invoice_list_comming",method = RequestMethod.POST)
	public ResponseEntity<?> InvoiceListComming(@RequestBody InvoiceListCommingRequest entity,HttpServletRequest request ) {
		InvoiceResponse response = new InvoiceResponse();
		try {
			GpayAuthentication user = (GpayAuthentication)SecurityContextHolder.getContext().getAuthentication();
			response.data = invoiceService.InvoiceListComming(user.getRootorgid_link(),entity.stockcode, entity.orgfrom_code, entity.invoicenumber, entity.shipdateto_from, entity.shipdateto_to,entity.status);
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<InvoiceResponse>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@RequestMapping(value = "/invoiced_bycontract_and_sku",method = RequestMethod.POST)
	public ResponseEntity<Balance_Invoice_Response> InvoiceD_bycontract_and_sku(@RequestBody InvoiceList_ByContractAndSku_Request entity,HttpServletRequest request ) {
		Balance_Invoice_Response response = new Balance_Invoice_Response();
		try {
			List<InvoiceD> ls_invoiced = invoiceDService.findBy_ContractAndSku(entity.pcontractid_link,entity.skuid_link);
			List<Balance_Invoice_Data> lsbalance_invoice = new ArrayList<Balance_Invoice_Data>();
			Date Invoice_Date =  null;
			for(InvoiceD invoiced:ls_invoiced){
				Balance_Invoice_Data balance_invoice =  new Balance_Invoice_Data();
				
				//Lay invoice date som nhat
				Invoice theInvoice = invoiceService.findOne(invoiced.getInvoiceid_link());
				if (null!=theInvoice){
					if (null != Invoice_Date){
						if (theInvoice.getShipdateto().before(Invoice_Date)) Invoice_Date = theInvoice.getShipdateto();
					} else {
						Invoice_Date = theInvoice.getShipdateto();
					}
				}
				balance_invoice.setInvoice_shipdateto(Invoice_Date);
				balance_invoice.setInvoiceid_link(invoiced.getInvoiceid_link());
				balance_invoice.setSkuid_link(invoiced.getSkuid_link());
				balance_invoice.setUnitid_link(invoiced.getUnitid_link());
				balance_invoice.setTotalpackage(invoiced.getTotalpackage());
				balance_invoice.setNetweight(invoiced.getNetweight());
				balance_invoice.setGrossweight(invoiced.getGrossweight());
				balance_invoice.setFoc(invoiced.getFoc());
				balance_invoice.setM3(invoiced.getM3());
				balance_invoice.setMet(invoiced.getMet());
				balance_invoice.setYds(invoiced.getYds());
				balance_invoice.setUnitprice(invoiced.getUnitprice());
				balance_invoice.setTotalamount(invoiced.getTotalamount());
				balance_invoice.setSkucode(invoiced.getSkucode());
				balance_invoice.setSize_name(invoiced.getSize_name());
				balance_invoice.setSkuname(invoiced.getSkuname());
				balance_invoice.setColor_name(invoiced.getColor_name());
				balance_invoice.setHscode(invoiced.getHscode());
				balance_invoice.setUnitname(invoiced.getUnitname());
				balance_invoice.setPackinglist(invoiced.getPackinglist());
				
				lsbalance_invoice.add(balance_invoice);
			}
			response.data = lsbalance_invoice;
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<Balance_Invoice_Response>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<Balance_Invoice_Response>(response, HttpStatus.BAD_REQUEST);
		}
	}
	@RequestMapping(value = "/invoice_activate",method = RequestMethod.POST)
	public ResponseEntity<?> InvoiceActivate(HttpServletRequest request ) {
		InvoiceResponse response = new InvoiceResponse();
		try {
			GpayAuthentication user = (GpayAuthentication)SecurityContextHolder.getContext().getAuthentication();
			response.data = invoiceService.findInvoiceActivate(user.getOrgId());
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<InvoiceResponse>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@RequestMapping(value = "/invoice_deletebyid",method = RequestMethod.POST)
	@Transactional(rollbackFor = RuntimeException.class)
	public ResponseEntity<?> DeleteInvoiceById(@RequestBody GetInvoiceByID entity, HttpServletRequest request ) {
		InvoiceByIDResponse response = new InvoiceByIDResponse();
		try {
			Long invoiceid = entity.invoiceid;
			
			Invoice invoice = invoiceService.findOne(invoiceid);
			List<InvoiceD> invoice_d = invoice.getInvoice_d();
			
			for(InvoiceD invoiceD : invoice_d) {
				List<PackingList> pklist = invoiceD.getPackinglist();
				
				for(PackingList pkl : pklist) {
					packingListService.deleteById(pkl.getId());
				}
				
				invoiceDService.deleteById(invoiceD.getId());
			}
			invoiceService.deleteById(invoiceid);
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@RequestMapping(value = "/invoiced_deletebyid",method = RequestMethod.POST)
	public ResponseEntity<?> DeleteInvoiceDById(@RequestBody GetInvoiceByID entity, HttpServletRequest request ) {
		InvoiceByIDResponse response = new InvoiceByIDResponse();
		try {
			invoiceDService.deleteById(entity.invoiceid);
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	
	
	//cũ
	@RequestMapping(value = "/getInvoice",method = RequestMethod.POST)
	public ResponseEntity<?> getInvoice(@RequestBody InvoiceListPageRequest entity,HttpServletRequest request ) {
		InvoiceResponse output = new InvoiceResponse();
		try {
//			GpayAuthentication user = (GpayAuthentication)SecurityContextHolder.getContext().getAuthentication();
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Long orgid_link = user.getOrgid_link();
			Org userOrg = orgService.findOne(orgid_link);
			Long userOrgId = userOrg.getId();
			Integer userOrgType = userOrg.getOrgtypeid_link();
			
			String custom_declaration = entity.custom_declaration;
			Date invoicedate_from = entity.invoicedate_from;
			Date invoicedate_to = entity.invoicedate_to;
			String invoicenumber = entity.invoicenumber;
			Long org_prodviderid_link = entity.org_prodviderid_link;
			Integer status = entity.status;
//			Integer limit = entity.limit;
//			Integer page = entity.page;
			
//			System.out.println(custom_declaration);
//			System.out.println(invoicedate_from);
//			System.out.println(invoicedate_to);
//			System.out.println(invoicenumber);
//			System.out.println(org_prodviderid_link);
//			System.out.println(status);
			
			List<Invoice> invoiceList = invoiceService.getInvoiceBySearch(
					custom_declaration, DateFormat.atStartOfDay(invoicedate_from), DateFormat.atEndOfDay(invoicedate_to),
					invoicenumber, org_prodviderid_link, status
					);
			
			List<Invoice> temp = new ArrayList<Invoice>();
			
			for(Invoice invoice : invoiceList) {
				switch(userOrgType) {
					case 1:
						break;
					case 13:
						if(invoice.getOrgid_to_link() != null) {
							if(invoice.getOrgid_to_link().equals(userOrgId)) {
								break;
							}else {
								continue;
							}
						}
					default: 
						break;
					
				}
				temp.add(invoice);
			}
			
			output.totalCount = temp.size();
			
			PageRequest pageRequest = PageRequest.of(entity.page - 1, entity.limit);
			int start = (int) pageRequest.getOffset();
			int end = (start + pageRequest.getPageSize()) > temp.size() ? temp.size() : (start + pageRequest.getPageSize());
			Page<Invoice> pageToReturn = new PageImpl<Invoice>(temp.subList(start, end), pageRequest, temp.size()); 
			
			output.data = pageToReturn.getContent();
			
//			output.data = invoiceList;
			output.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			output.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<InvoiceResponse>(output,HttpStatus.OK);
		}catch (RuntimeException e) {
			e.printStackTrace();
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
//	@RequestMapping(value = "/getInvoice",method = RequestMethod.POST)
//	public ResponseEntity<?> getInvoice(@RequestBody InvoiceListPageRequest entity,HttpServletRequest request ) {
//		InvoiceResponse output = new InvoiceResponse();
//		try {
//			
//			
//			output.data = invoiceService.findAll();
//			output.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
//			output.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
//			return new ResponseEntity<InvoiceResponse>(output,HttpStatus.OK);
//		}catch (RuntimeException e) {
//			ResponseError errorBase = new ResponseError();
//			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
//			errorBase.setMessage(e.getMessage());
//		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
//		}
//	}
	@RequestMapping(value = "/getInvoiceByID",method = RequestMethod.POST)
	public ResponseEntity<?> getInvoiceById(@RequestBody GetInvoiceByID entity, HttpServletRequest request ) {
		InvoiceByIDResponse output = new InvoiceByIDResponse();
		try {
			output.data = invoiceService.findOne(entity.invoiceid);
			output.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			output.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<InvoiceByIDResponse>(output,HttpStatus.OK);
		}catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/getInvoiceByCode",method = RequestMethod.POST)
	public ResponseEntity<?> GetStockinByCode( @RequestBody GetInvoiceByCode entity,HttpServletRequest request ) {
		InvoiceResponse output = new InvoiceResponse();
		try {
			List<Invoice> listinv = invoiceService.findByInvoicenumber(entity.invoicecode);
			output.data = listinv;
			if(listinv!=null && listinv.size()>0) {
				packingListService.inv_getbyid(listinv.get(0).getId());
			}
			output.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			output.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<InvoiceResponse>(output,HttpStatus.OK);
		}catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/getInvoiceDBySkuCode",method = RequestMethod.POST)
	public ResponseEntity<?> getInvoiceDBySkuCode( @RequestBody GetInvoiceDBySkuCodeRequest entity,HttpServletRequest request ) {
		InvoiceDResponse response = new InvoiceDResponse();
		try {
			if(entity.invoicenumber!=null) {
				response.data = invoiceService.findInvoiceDBySkuCode(entity.invoicenumber, entity.skucode);
			}else {
				response.data = null;
//				List<Sku>  listsku=  skuService.findSkuByCode(entity.skucode);
//				List<InvoiceD> listdata = new ArrayList<>();
//				for(Sku entry : listsku) {
//					InvoiceD en = new InvoiceD();
//					//en.s
//					//listdata.add(en);
//				}
				
			}
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<InvoiceDResponse>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/InvoiceInsert",method = RequestMethod.POST)
	public ResponseEntity<?> InvoiceInsert( @RequestBody InvoiceRequest entity,HttpServletRequest request ) {
		ResponseBase response = new ResponseBase();
		try {
			
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/invoice_insertpkl",method = RequestMethod.POST)
	public ResponseEntity<?> InsertPKL(@RequestBody invoice_insertpkl_request entity,HttpServletRequest request ) {
		invoice_insertsku_response response = new invoice_insertsku_response();
		try {
			GpayAuthentication user = (GpayAuthentication)SecurityContextHolder.getContext().getAuthentication();
			long orgrootid_link = user.getRootorgid_link();
			long invoiceid_link = entity.data.getInvoiceid_link();
			Invoice invoice = invoiceService.findOne(invoiceid_link);
			
			//Cập nhật lại invoice
			invoice.setLasttimeupdate(new Date());
			invoice.setLastuserupdateid_link(user.getUserId());

			invoiceService.save(invoice);
			
			//Thêm vào Packinglist
//			SKU sku = skuService.findOne(entity.data.getSkuid_link());
			
			PackingList pkl = entity.data;
			
			
			if(pkl.getId() == null) {
//				pkl.setColorid_link(sku.getColor_id());
				pkl.setTimecreate(new Date());
				pkl.setUsercreateid_link(user.getUserId());
				pkl.setOrgrootid_link(orgrootid_link);
			}
			else {
				pkl.setLasttimeupdate(new Date());
				pkl.setLastuserupdateid_link(user.getUserId());
			}
			
			packingListService.save(pkl);
			
			//Cap nhat lai invoiced
			
			InvoiceD invoiced = invoiceDService.findOne(pkl.getInvoicedid_link());
			Float m3 = (float)0;
			Float gw = (float)0;
			Float nw = (float)0;
			Float yds = (float)0;
			Integer amount = 0;
			
			List<PackingList> list_pkl = packingListService.getPKL_by_invoiced(pkl.getInvoicedid_link());
			for(PackingList packinglist : list_pkl) {
				m3 += packinglist.getM3();
				gw += packinglist.getGrossweight();
				nw += packinglist.getNetweight();
				yds += packinglist.getYdsorigin();
				amount ++;
			}
			
			invoiced.setM3(m3);
			invoiced.setGrossweight(gw);
			invoiced.setNetweight(nw);
			invoiced.setYds(yds);
			invoiced.setTotalpackage(amount);
			
			Float unitprice = invoiced.getUnitprice().equals(null) ? 0 : invoiced.getUnitprice();
			invoiced.setTotalamount(amount*unitprice);
			
			invoiceDService.save(invoiced);
			
			
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<invoice_insertsku_response>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			e.printStackTrace();
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/deleteInvoiceDById",method = RequestMethod.POST)
	@Transactional(rollbackFor = RuntimeException.class)
	public ResponseEntity<?> deleteInvoiceDById(@RequestBody InvoiceDDeleteRequest entity,HttpServletRequest request ) {
		ResponseBase response = new ResponseBase();
		try {
			Long invoicedid_link = entity.invoicedid_link;
			InvoiceD invoiced = invoiceDService.findOne(invoicedid_link);
			List<PackingList> pklist = invoiced.getPackinglist();
			
			for(PackingList pkl : pklist) {
				packingListService.deleteById(pkl.getId());
			}
			invoiceDService.deleteById(invoicedid_link);
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
//			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/deletePackingListById",method = RequestMethod.POST)
	public ResponseEntity<?> deletePackingListById(@RequestBody PackingListDeleteRequest entity,HttpServletRequest request ) {
		ResponseBase response = new ResponseBase();
		try {
			GpayAuthentication user = (GpayAuthentication)SecurityContextHolder.getContext().getAuthentication();
			Long packinglistid_link = entity.packinglistid_link;
			PackingList pkl = packingListService.findOne(packinglistid_link);
			
			Long invoiceid_link = pkl.getInvoiceid_link();
			Invoice invoice = invoiceService.findOne(invoiceid_link);
			
			Long invoicedid_link = pkl.getInvoicedid_link();
			InvoiceD invoiced = invoiceDService.findOne(invoicedid_link);
			
			//Cập nhật lại invoice
			invoice.setLasttimeupdate(new Date());
			invoice.setLastuserupdateid_link(user.getUserId());

			invoiceService.save(invoice);
			
			//Xoá Packinglist
			
			packingListService.deleteById(packinglistid_link);
			
			//Cap nhat lai invoiced
			Float m3 = (float)0;
			Float gw = (float)0;
			Float nw = (float)0;
			Float yds = (float)0;
			Integer amount = 0;
			
			List<PackingList> list_pkl = packingListService.getPKL_by_invoiced(invoiced.getId());
			System.out.println(list_pkl.size());
			for(PackingList packinglist : list_pkl) {
				m3 += packinglist.getM3();
				gw += packinglist.getGrossweight();
				nw += packinglist.getNetweight();
				yds += packinglist.getYdsorigin();
				amount ++;
			}
			
			invoiced.setM3(m3);
			invoiced.setGrossweight(gw);
			invoiced.setNetweight(nw);
			invoiced.setYds(yds);
			invoiced.setTotalpackage(amount);
			
			Float unitprice = invoiced.getUnitprice().equals(null) ? 0 : invoiced.getUnitprice();
			invoiced.setTotalamount(amount*unitprice);
			
			invoiceDService.save(invoiced);
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
//			e.printStackTrace();
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/updatePackingListById",method = RequestMethod.POST)
	public ResponseEntity<?> updatePackingListById(@RequestBody PackingListUpdateRequest entity,HttpServletRequest request ) {
		ResponseBase response = new ResponseBase();
		try {
			GpayAuthentication user = (GpayAuthentication)SecurityContextHolder.getContext().getAuthentication();
			Long packinglistid_link = entity.packinglistid_link;
			PackingList pkl = packingListService.findOne(packinglistid_link);
			
			Long invoiceid_link = pkl.getInvoiceid_link();
			Invoice invoice = invoiceService.findOne(invoiceid_link);
			
			Long invoicedid_link = pkl.getInvoicedid_link();
			InvoiceD invoiced = invoiceDService.findOne(invoicedid_link);
			
			//Cập nhật lại invoice
			invoice.setLasttimeupdate(new Date());
			invoice.setLastuserupdateid_link(user.getUserId());

			invoiceService.save(invoice);
			
			//Cap nhat Packinglist
			String contextField = entity.contextField;
			Float value = entity.value;
			switch(contextField) {
				case "netweight":
					pkl.setNetweight(value);
					break;
				case "grossweight":
					pkl.setGrossweight(value);
					break;
				case "ydsorigin":
					pkl.setYdsorigin(value);
					break;
				case "m3":
					pkl.setM3(value);
					break;
				case "width":
					pkl.setWidth(value);
					break;
				default:
					break;
			}
			packingListService.save(pkl);
			
			//Cap nhat lai invoiced
			Float m3 = (float)0;
			Float gw = (float)0;
			Float nw = (float)0;
			Float yds = (float)0;
			Integer amount = 0;
			
			List<PackingList> list_pkl = packingListService.getPKL_by_invoiced(invoiced.getId());
			System.out.println(list_pkl.size());
			for(PackingList packinglist : list_pkl) {
				m3 += packinglist.getM3();
				gw += packinglist.getGrossweight();
				nw += packinglist.getNetweight();
				yds += packinglist.getYdsorigin();
				amount ++;
			}
			
			invoiced.setM3(m3);
			invoiced.setGrossweight(gw);
			invoiced.setNetweight(nw);
			invoiced.setYds(yds);
			invoiced.setTotalpackage(amount);
			
			Float unitprice = invoiced.getUnitprice().equals(null) ? 0 : invoiced.getUnitprice();
			invoiced.setTotalamount(amount*unitprice);
			
			invoiceDService.save(invoiced);
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
//			e.printStackTrace();
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
