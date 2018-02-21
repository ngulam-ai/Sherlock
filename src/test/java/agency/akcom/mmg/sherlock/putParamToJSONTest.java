package agency.akcom.mmg.sherlock;

import javax.servlet.ServletException;

import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.mock.web.MockHttpServletRequest;

@RunWith(JUnit4.class)
public class putParamToJSONTest {
	static final CollectServlet COLLECT_SERVLET = new CollectServlet();
	
	@Before
	public void init() throws ServletException {
		COLLECT_SERVLET.init();
	}
	
	@Test
	public void defaultParamIDTimeZoneTest() throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest();
		JSONObject json = COLLECT_SERVLET.putParamToJSON(request);
		Assert.assertEquals("Europe/Madrid", json.get("CD91"));
	}
	
	@Test
	public void setBadParamCDidTimeZoneTest() throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.addParameter("cd33", "SDF ggs");
		request.addParameter("cd30", "hdfwf");
		JSONObject json = COLLECT_SERVLET.putParamToJSON(request);
		Assert.assertEquals("Europe/Madrid", json.get("CD91"));
	}
	
	@Test
	public void setGoodParamCDidTimeZoneTest() throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.addParameter("cd33", "Russia");
		request.addParameter("cd30", "Yaroslavl");
		JSONObject json = COLLECT_SERVLET.putParamToJSON(request);
		Assert.assertEquals("Europe/Moscow", json.get("CD91"));
	}
	
	@Test
	public void setGoodParamCDidTimeZoneTest2() throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.addParameter("cd33", "United Kingdom");
		request.addParameter("cd30", "London");
		JSONObject json = COLLECT_SERVLET.putParamToJSON(request);
		Assert.assertEquals("Europe/London", json.get("CD91"));
	}
	
	@Test
	public void setOneCountryCDidTimeZoneTest() throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.addParameter("cd33", "Japan");
		JSONObject json = COLLECT_SERVLET.putParamToJSON(request);
		Assert.assertEquals("Asia/Tokyo", json.get("CD91"));
	}
}
