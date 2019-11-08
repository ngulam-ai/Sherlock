package agency.akcom.mmg.sherlock.collect.audience;

import javax.inject.Inject;

import org.json.JSONObject;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Subclass;

import agency.akcom.mmg.sherlock.collect.domain.AudUser;
import agency.akcom.mmg.sherlock.collect.domain.JsonField;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.Date;

@Subclass(index = true)
@Data
@Slf4j
public class Geography extends AudUserAttribute {
	
	@Index
	Date date;

	@JsonField(name = "cd121")
	String continent;

	@JsonField(name = "cd33")
	String country;

	@JsonField(name = "cd57")
	String carrier;

	@JsonField(name = "cd69")
	String region;

	@JsonField(name = "cd122")
	String state;

	@JsonField(name = "cd30")
	String city;

	String parentUid;

	@Inject
	public Geography(JSONObject reqJson) {
		AudienceProcessing.setFieldsWithAnnotation(this, reqJson);
	}
}
