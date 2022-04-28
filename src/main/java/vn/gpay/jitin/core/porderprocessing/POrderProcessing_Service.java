package vn.gpay.jitin.core.porderprocessing;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.jitin.core.base.AbstractService;
import vn.gpay.jitin.core.porder_grant.POrder_Grant;

@Service
public class POrderProcessing_Service extends AbstractService<POrderProcessing> implements IPOrderProcessing_Service {
	@Autowired IPOrderProcessing_Repository repo;
	@Override
	protected JpaRepository<POrderProcessing, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}
	
	@Override
	public List<POrderProcessing>getLatest_All(){
		return repo.getLatest_All();
	}

	@Override
	public List<POrderProcessing>getByDate(Date processingdate_to){
		return repo.getByDate(processingdate_to);
	}
	
//	@Override
//	public List<POrderProcessing>getByDateAndFactory(Date processingdate_to, Long factoryid){
//		List<POrderProcessing> a = repo.getByDateAndFactory(processingdate_to, factoryid);
//		return a;
//	}
	
	@Override
	public List<POrderProcessing>getBySalaryMonth(Integer salaryyear, Integer salarymonth){
		return repo.getBySalaryMonth(salaryyear, salarymonth);
	}
	
	
	@Override
	public List<POrderProcessing>getByDate_Cutting(Date processingdate_to){
		return repo.getByDate_Cutting(processingdate_to);
	}
	
	@Override
	public List<POrderProcessing>getPProcess_Cutting(Date processingdate_to, Long porderid_link){
		return repo.getPProcess_Cutting(processingdate_to, porderid_link);
	}

	@Override
	public List<POrderProcessing>getByDateAndOrderCode(String ordercode, Date processingdate_to){
		return repo.getByDateAndOrderCode(ordercode, processingdate_to);
	}

	@Override
	public List<POrderProcessing> getProductionStartDate(String ordercode){
		return repo.getProductionStartDate(ordercode);
	}

	@Override
	public List<POrderProcessing>getByBeforeDateAndOrderCode(String ordercode, Date processingdate_to){
		return repo.getByBeforeDateAndOrderCode(ordercode, processingdate_to);
	}
	
	@Override
	public List<POrderProcessing>getByBeforeDateAndOrderGrantID(Long pordergrantid_link, Date processingdate_to){
		try {
			return repo.getByBeforeDateAndOrderGrantID(pordergrantid_link, processingdate_to);
		} catch(Exception e){
			e.printStackTrace();
			return null;
		}

//		return null;
	}

	@Override
	public List<POrderProcessing>getByBeforeDateAndOrderID(Long porderid_link, Date processingdate_to){
		return repo.getByBeforeDateAndOrderID(porderid_link, processingdate_to);
	}

	@Override
	public List<POrderProcessing>getByDateAndStatus(Date processingdate_to, Integer status){
		return repo.getByDateAndStatus(processingdate_to, status);
	}

	@Override
	public List<POrderProcessing>findByIdAndPDate(Long porderid_link, Long pordergrantid_link, Date processingdate){
		return repo.findByIdAndPDate(porderid_link, pordergrantid_link, processingdate);
	}
	
	@Override
	public List<POrderProcessing>findByCodeAndPDate(String ordercode, Date processingdate){
		return repo.findByCodeAndPDate(ordercode, processingdate);
	}

	@Override
	public List<POrderProcessing>getAfterDate(Long porderid_link, Long pordergrantid_link, Date processingdate_to){
		return repo.getAfterDate(porderid_link, pordergrantid_link, processingdate_to);
	}
	
	@Override
	public List<POrderProcessing>getBeforeDate(Long porderid_link, Long pordergrantid_link, Date processingdate){
		return repo.getBeforeDate(porderid_link, pordergrantid_link, processingdate);
	}
	
	@Override
	public void deleteByOrderID(final Long porderid_link){
		for(POrderProcessing porder: repo.getByOrderId(porderid_link)){
			repo.delete(porder);
		}
	}
	
	@Override
	public List<POrderProcessing>getByOrderId(Long porderid_link){
		return repo.getByOrderId(porderid_link);
	}

	@Override
	public List<POrderProcessing> getByOrderId_and_GrantId(Long porderid_link, Long pordergrantid_link) {
		// TODO Auto-generated method stub
		return repo.getByOrderId_And_GrantId(porderid_link, pordergrantid_link);
	}
	
	@Override
	public POrder_Grant get_processing_bygolivedate(Long porderid_link, Long pordergrantid_link){
		POrder_Grant thePorderGrant = new POrder_Grant();
		List<POrderProcessing> aMIN = repo.getMINRunningByOrderId_And_GrantId(porderid_link, pordergrantid_link);
		List<POrderProcessing> aMAX = repo.getMAXRunningByOrderId_And_GrantId(porderid_link, pordergrantid_link);
		if (aMIN.size() > 0 && aMAX.size() > 0){
			thePorderGrant.setStart_date_plan(aMIN.get(0).getProcessingdate());
			thePorderGrant.setFinish_date_plan(aMAX.get(0).getProcessingdate());
			thePorderGrant.setGrantamount(null==aMAX.get(0).getAmountoutputsum()?0:aMAX.get(0).getAmountoutputsum());
			thePorderGrant.setAmountcutsum(null==aMAX.get(0).getAmountoutput()?0:aMAX.get(0).getAmountoutput());

			return thePorderGrant;
		} else {
			return null;
		}
	}

	@Override
	public List<POrderProcessing> getByOrgId(Long granttoorgid_link) {
		// TODO Auto-generated method stub
		return repo.getByOrgId(granttoorgid_link);
	}

	@Override
	public List<POrderProcessing> getByPOrderAndPOrderGrant(Long porderid_link, Long pordergrantid_link) {
		// TODO Auto-generated method stub
		return repo.getByPOrderAndPOrderGrant(porderid_link, pordergrantid_link);
	}

//	@Override
//	public List<POrderProcessing> getby_pcontratpo(Long pcontract_poid_link) {
//		// TODO Auto-generated method stub
//		return repo.getby_pcontractpo(pcontract_poid_link);
//	}

	@Override
	public List<POrderProcessingBinding> getAmountOutputForChart(Date dateFrom, Date dateTo, Long dayDifference) {
		// TODO Auto-generated method stub
		List<POrderProcessingBinding> data = new ArrayList<POrderProcessingBinding>();
		List<Object[]> objects = repo.getAmountOutputForChart(dateFrom, dateTo, dayDifference);
		
		for(Object[] row : objects) {
			POrderProcessingBinding temp = new POrderProcessingBinding();
			temp.setSumOutput((Long) row[0]);
			temp.setSumError((Long) row[1]);
			temp.setSumStocked((Long) row[2]);
			temp.setParentid_link((Long) row[3]);
			temp.setName((String) row[4]);
			data.add(temp);
		}
		
		return data;
	}

	@Override
	public List<POrderProcessingBinding> getAmountPackStockedForChart(Date tenDaysAgo, Date today) {
		
		List<POrderProcessingBinding> data = new ArrayList<POrderProcessingBinding>();
		Map<Date, POrderProcessingBinding> mapTmp = new HashMap<>();
		List<Object[]> objects = repo.getAmountPackStockedForChart(tenDaysAgo, today);
		
		LocalDate start = tenDaysAgo.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		LocalDate end = today.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

		for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {
		    // Do your job here with `date`.
			Date dateObj = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
			
			Calendar cal = Calendar.getInstance();
			cal.setTime(dateObj);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			dateObj = cal.getTime();
			
			POrderProcessingBinding temp = new POrderProcessingBinding();
			temp.setProcessingDate(dateObj);
			temp.setDataDHA(0L);
			temp.setDataNV(0L);
			temp.setDataBN1(0L);
			temp.setDataBN2(0L);
			temp.setDataBN3(0L);
			mapTmp.put(dateObj, temp);
		}
		
		for(Object[] row : objects) {
			Long sum = (Long) row[0];
			String name = (String) row[1];
			Date date = (Date) row[2];
			
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			date = cal.getTime();
			
			if(mapTmp.containsKey(date)) {
				POrderProcessingBinding temp = mapTmp.get(date);
				switch(name) {
					case "DHA":
						temp.setDataDHA(sum);
						break;
					case "NV":
						temp.setDataNV(sum);
						break;
					case "Bắc Ninh 1":
						temp.setDataBN1(sum);
						break;
					case "Bắc Ninh 2":
						temp.setDataBN2(sum);
						break;
					case "Bắc Ninh 3":
						temp.setDataBN3(sum);
						break;
				}
				mapTmp.put(date, temp);
			}
//			else {
//				POrderProcessingBinding temp = new POrderProcessingBinding();
//				temp.setProcessingDate(date);
//				temp.setDataDHA(0L);
//				temp.setDataNV(0L);
//				temp.setDataBN1(0L);
//				temp.setDataBN2(0L);
//				temp.setDataBN3(0L);
//				switch(name) {
//					case "DHA":
//						temp.setDataDHA(sum);
//						break;
//					case "NV":
//						temp.setDataNV(sum);
//						break;
//					case "Bắc Ninh 1":
//						temp.setDataBN1(sum);
//						break;
//					case "Bắc Ninh 2":
//						temp.setDataBN2(sum);
//						break;
//					case "Bắc Ninh 3":
//						temp.setDataBN3(sum);
//						break;
//				}
//				mapTmp.put(date, temp);
//			}
		}
		data = new ArrayList<POrderProcessingBinding>(mapTmp.values());
		Collections.sort(data, new Comparator<POrderProcessingBinding>() {
			  public int compare(POrderProcessingBinding o1, POrderProcessingBinding o2) {
			      return o1.getProcessingDate().compareTo(o2.getProcessingDate());
			  }
			});
		return data;
	}
	
	@Override
	public List<POrderProcessing> getByPOrderAndLineAndDate(Long porderid_link, Long orgid_to_link, Date receive_date) {
		return repo.getByPOrderAndLineAndDate(porderid_link, orgid_to_link, receive_date);
	}
}
