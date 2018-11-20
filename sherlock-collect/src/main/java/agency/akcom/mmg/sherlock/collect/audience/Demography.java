package agency.akcom.mmg.sherlock.collect.audience;

import java.util.Date;

import javax.inject.Inject;

import org.json.JSONObject;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Subclass;

import agency.akcom.mmg.sherlock.collect.domain.AudUser;
import agency.akcom.mmg.sherlock.collect.domain.JsonField;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Subclass(index = true)
@Data
@Slf4j
public class Demography extends AudUserChild {
	
	@Index
	Date date;
	
	@JsonField(name = "cd43")
	String gender;
	
	@JsonField(name = "cd84")
	String age;
	
	@JsonField(name = "cd51")
	String language;
	
	@JsonField(name = "cd85")
	String education_majority_ZIP;
	
	@JsonField(name = "cd86")
	String ethnicity_majority_ZIP;
	
	@JsonField(name = "cd87")
	String income_average_ZIP;
	
	@JsonField(name = "cd88")
	String unemployment_average_ZIP;
	
	@JsonField(name = "cd89")
	String crimes_average_ZIP;
	
	String parentUid;

	@Inject
	public Demography(JSONObject reqJson) {
		AudienceProcessing.setFieldsWithAnnotation(this, reqJson);
	}
}
