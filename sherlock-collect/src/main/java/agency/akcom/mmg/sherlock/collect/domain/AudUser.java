package agency.akcom.mmg.sherlock.collect.domain;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.inject.internal.Annotations;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Entity
@Data
@Slf4j
public class AudUser extends DatastoreObject {
//	http://localhost:8080/collect?uid=1&cd11=2&cd30=testCity&cd69=testRegion&cd33=testCountry&ua=testOS&__bv=testosVersion&__dc=testDeviceType&__ul=testLang&pa=add

	// =====ID=====
	@JsonField(name = "uid")
	@Index
	private String uid;

	@JsonField(name = "cd11")
	@Index
	private String ga_clientid;

	@Index
	private String mparticleuserid;

	@Index
	private String customerid;

	@Index
	private String facebookid;

	@Index
	private String twitterid;

	@Index
	private String googleuserid;

	@Index
	private String microsoftuserid;

	@Index
	private String yahoouserid;

	@Index
	private String email;

	@Index
	private String otheruserid;

	@Index
	private String msisdn;

	@JsonField(name = "ios_ifa")
	@Index
	private String ios;

	@JsonField(name = "google_aid")
	@Index
	private String android;

	@JsonField(name = "platform_aid")
	@Index
	private String amazonfire;

	@JsonField(name = "windows_aid")
	@Index
	private String windowsphone;

	@Index
	private String openudid;

	@Index
	private String androidid;

	@Index
	private String deviceid;

	@Index
	private String macaddress;
	// ==========

	@Index
	private Long frequency;

	private Geography geography;

	private Demography demography;

	private Behavior behavior;

	private Category category;

	private Device device;

	String engagementType;
	
	private CRM crm;

	// -• Uid
	// -• GA_ClientID
	// - mparticleuserid
	// • customerid
	// • facebookid
	// • twitterid
	// • googleuserid
	// • microsoftuserid
	// • yahoouserid
	// • email
	// • otheruserid
	// • MSISDN
	// • iOS: iOS IFA (Apple Identifier for Advertiser)
	// • Android: Google AID (Google Advertising Identifier)
	// • Amazon Fire: Fire AID (Amazon Fire Advertising Identifier)
	// • Windows Phone: Windows AID (Windows Advertising Identifier)
	// • Open UDID
	// • Android ID
	// • Device ID
	// • MAC Address

	// We would need to build a database with several fields. Please note that we
	// may need to add additional fields, should we need to integrate/ingest
	// additional data-points in the future. Please note that audience s from this
	// database should be filtered by Recency and frequency, hence we need to store
	// the date in which the uid was last seen, and how many times
	// Recency is measureas as follows:
	// • Recency
	// o 7 Days
	// o 14 Days
	// o 30 Days
	// o 60 Days
	// o 90 Days
	// o 180 Days
	// o 365 Days
	// o All Time
	//
	// The main fields within the database are:
	//
	// -• Geography
	// -o Continent
	// o Country
	// o Carrier
	// -o Region
	// o State
	// -o City
	//
	// -• Demography
	// o Gender
	// o Age
	// o Language
	// o Education_majority_ZIP
	// o Ethnicity_majority_ZIP
	// o Income_average_ZIP
	// o Unemployment_average_ZIP
	// o Crimes_average_ZIP

	// • Interests (refer to IAB_Tech_Lab_Content_Taxonomy_V2_Final_2017-11)

	// -• Behaviors
	// o New Users vs Returning Users
	// o Number of transactions (0, 1, 2, etc)
	// o Total amount spent (0, between 0 and 3, between 3 and 5, bewteen 5 and 10,
	// more than 10, etc)
	// o Number of iterations before purchase

	// • Points of Interest (Seen on product lp)

	// -• Web/App Category
	// o Apps on device

	// -• Device
	// o OS
	// o OS Version
	// o Device Type
	// o Language
	// o Manufacturer
	// o Model

	// -• Engagement type
	// o Session
	// o Impression (View-through)
	// o Click (Click-through)
	// o Engagement
	// o Purchase Intent
	// o Purchase
	// o Install

	// -• CRM
	// o Ispostpaid
	// o Subscribedto
	// o …..others fields that will be defined later.
	//

	public AudUser(JSONObject reqJson) {
		setFieldsWithAnnotation(this, reqJson);
		geography = new Geography(reqJson);
		demography = new Demography(reqJson);
		behavior = new Behavior(reqJson);
		category = new Category(reqJson);
		device = new Device(reqJson);
		setEngagementType(reqJson);
		crm = new CRM(reqJson);

		// avoid empty or null values
		// TODO consider the sense
		if (uid == null || uid.isEmpty()) {
			uid = "emty_original_uid_" + UUID.randomUUID().toString();
		}

		increaseFrequency();
	}

	public Long increaseFrequency() {
		frequency = (frequency == null) ? 1 : frequency + 1;
		return frequency;
	}

	public AudUser() {

	}

	//Set fields in class with "JsonField" annotation
	private void setFieldsWithAnnotation(Object clazz, JSONObject reqJson) {
		Field[] fieldsArr = clazz.getClass().getDeclaredFields();
		for (int i = 0; i < fieldsArr.length; i++) {
			boolean annotation = checkExistenceAnnotation(fieldsArr[i]);
			if (annotation) {
				String[] jsonFieldName = fieldsArr[i].getAnnotation(JsonField.class).name().split(",");
				String jsonValue = getJsonValue(jsonFieldName, reqJson);
				fieldsArr[i].setAccessible(true);
				try {
					fieldsArr[i].set(clazz, jsonValue);
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}
	}

	// Checking that JsonField annotation is exist in field
	private boolean checkExistenceAnnotation(Field f) {
		Annotation[] annotations = f.getDeclaredAnnotations();
		for (int i = 0; i < annotations.length; i++) {
			if (annotations[i] instanceof JsonField) {
				return true;
			}
		}
		return false;
	}
	
	private String getJsonValue(String[] jsonFieldName, JSONObject reqJson) {
		String value = null;
		for (int i = 0; i < jsonFieldName.length; i++) {
			try {
				value = reqJson.getString(jsonFieldName[i].trim());
				if (!value.isEmpty()) {
					return value;
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
			}
		}
		return value;
	}

	private class Geography {

		String continent;

		@JsonField(name = "cd33")
		String country;

		@JsonField(name = "cd57")
		String carrier;

		@JsonField(name = "cd69")
		String region;

		String state;

		@JsonField(name = "cd30")
		String city;

		Geography(JSONObject reqJson) {
			setFieldsWithAnnotation(this, reqJson);
		}
	}

	private class Demography {
		@JsonField(name = "cd43")
		String gender;
		String age;
		String language;
		String education_majority_ZIP;
		String ethnicity_majority_ZIP;
		String income_average_ZIP;
		String unemployment_average_ZIP;
		String crimes_average_ZIP;

		Demography(JSONObject reqJson) {
			setFieldsWithAnnotation(this, reqJson);
		}
	}

	private class Behavior {

		// is describe new user or returning user.
		// new user - true, returning user - false
		boolean entrance;
		// Number of transactions (0, 1, 2, etc)
		long transactions;
		long totalAmountSpent;
		// Number of iterations before purchase
		long iterations;

		Behavior(JSONObject reqJson) {
			// TODO set fields
		}
	}

	private class Category {
		String category;

		Category(JSONObject reqJson) {
			// TODO set field
		}
	}

	private class Device {
		// TODO review this, I set field from device.* from hits data
		@JsonField(name = "ua")
		String os;
		@JsonField(name = "__bv")
		String osVersion;
		@JsonField(name = "__dc")
		String deviceType;
		@JsonField(name = "ul, __ul")
		String language;
		// TODO set this field
		String manufacturer;
		@JsonField(name = "__dm")
		String model;

		Device(JSONObject reqJson) {
			setFieldsWithAnnotation(this, reqJson);
		}
	}

	private class CRM {
		String ispostpaid;
		String subscribedto;

		CRM(JSONObject reqJson) {
			setFieldsWithAnnotation(this, reqJson);
		}
	}

	private void setEngagementType(JSONObject reqJson) {
		// define by eCommerceAction.action_type
		String action = null;
		try {
			action = reqJson.getString("pa");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
		}
		// • Engagement type
		// o Session -?
		// o Impression (View-through) - detail (просмотр информации),
		// o Click (Click-through) - click (клик),
		// o Engagement -?
		// o Purchase Intent - add (добавление в корзину), checkout (оформление покупки)
		// o Purchase -purchase (покупка)
		// o Install - ?
		switch (action) {
		case "detail":
			engagementType = "Impression";
		case "click":
			engagementType = "Click";
		case "add":
			engagementType = "Purchase Intent";
		case "checkout":
			engagementType = "Purchase Intent";
		case "purchase":
			engagementType = "Purchaset";
		default:
			engagementType = null;
		}
	}

}