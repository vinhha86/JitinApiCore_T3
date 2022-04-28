package vn.gpay.jitin.core.category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.jitin.core.base.AbstractService;

@Service
public class ColorServiceImpl extends AbstractService<Color> implements IColorService{

	@Autowired
	ColorRepository repositoty;

	@Override
	protected JpaRepository<Color, Long> getRepository() {
		// TODO Auto-generated method stub
		return repositoty;
	}

}
