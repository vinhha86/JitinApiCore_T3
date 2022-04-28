package vn.gpay.jitin.core.sku;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.jitin.core.base.AbstractService;

@Service
public class SKU_Service extends AbstractService<SKU> implements ISKU_Service {
	
	@Autowired ISKU_Repository repo;
	
	@Override
	protected JpaRepository<SKU, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}

	@Override
	public List<SKU> getlist_byProduct(Long productid_link) {
		// TODO Auto-generated method stub
		return repo.getlist_byproduct(productid_link);
	}

	public SKU get_bySkucode(String skucode) {
		// TODO Auto-generated method stub
		List<SKU> lstSKU = repo.getlist_byskucode(skucode);
		if (lstSKU.size() > 0)
			return repo.getlist_byskucode(skucode).get(0);
		else
			return null;
	}

	@Override
	public List<SKU> getlist_bycode(List<String> listcode) {
		// TODO Auto-generated method stub
		List<SKU> list = new ArrayList<SKU>();
		for(String code : listcode) {
			List<SKU> listbycode = repo.getlist_byskucode(code);
			if(listbycode.size() > 0) {
				SKU sku = listbycode.get(0);
				list.add(sku);
			}
		}
		return list;
	}

	@Override
	public List<SKU> findSkuByType(int type, long orgrootid_link) {
		// TODO Auto-generated method stub
		return repo.findSkuByType(type, orgrootid_link);
	}
}
