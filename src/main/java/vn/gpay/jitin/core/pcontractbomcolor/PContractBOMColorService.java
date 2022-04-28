package vn.gpay.jitin.core.pcontractbomcolor;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.jitin.core.base.AbstractService;


@Service
public class PContractBOMColorService extends AbstractService<PContractBOMColor> implements IPContractBOMColorService {
	@Autowired IPContractBOMColorRepository repo;
	@Override
	protected JpaRepository<PContractBOMColor, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}
	@Override
	public List<PContractBOMColor> getall_material_in_productBOMColor(long pcontractid_link, long productid_link,
			long colorid_link, long materialid_link) {
		// TODO Auto-generated method stub
		return repo.getall_material_in_productBOMColor(productid_link, pcontractid_link, colorid_link, materialid_link);
	}
	@Override
	public List<PContractBOMColor> getcolor_bymaterial_in_productBOMColor(long pcontractid_link, long productid_link,
			long materialid_link) {
		// TODO Auto-generated method stub
		return repo.getcolor_bymaterial_in_productBOMColor(productid_link, pcontractid_link, materialid_link);
	}

}
