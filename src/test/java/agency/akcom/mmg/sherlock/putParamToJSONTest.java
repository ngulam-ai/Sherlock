package agency.akcom.mmg.sherlock;

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
	public void init() {
		COLLECT_SERVLET.createMapTimeZone();
	}
	
	@Test
	public void customParamIDTimeZoneTest() throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest();
		JSONObject json = COLLECT_SERVLET.putParamToJSON(request);
		Assert.assertEquals("Europe/Madrid", json.get("CD91"));
	}
	
	@Test
	public void setCountryCDidTimeZoneTest() throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.addParameter("cd33", "Egypt");
		request.addParameter("cd30", "Alexandria");
		JSONObject json = COLLECT_SERVLET.putParamToJSON(request);
		Assert.assertEquals("Egypt", json.get("CD91"));
	}
	
	@Test
	public void setSityIDTimeZoneTest() throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.addParameter("cd33", "United Kingdom");
		request.addParameter("cd30", "London");
		JSONObject json = COLLECT_SERVLET.putParamToJSON(request);
		Assert.assertEquals("Europe/London", json.get("CD91"));
	}
}
