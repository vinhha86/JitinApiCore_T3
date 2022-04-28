package vn.gpay.jitin.core.security;

import java.util.List;

import vn.gpay.jitin.core.base.Operations;
import vn.gpay.jitin.core.org.Org;

public interface IGpayUserService extends Operations<GpayUser>{

	public GpayUser findByUsername(String username);

	public GpayUser findByEmail(String email);
	
	public GpayUser findById(long userid);
	
    public List<GpayUser> getUserList(long orgid_link,String textsearch,int status);
    
    public List<GpayUser> getUserList_page(String firstname, String middlename, String lastname,
    		String username, Long groupuserid_link);
    
    public List<GpayUser> getUserinOrgid(List<Org> listorg);
}
