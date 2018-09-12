package agency.akcom.mmg.sherlock.collect.domain;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.UUID;

import javax.inject.Inject;

import org.json.JSONException;
import org.json.JSONObject;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Entity
@Data
@Slf4j
public class AudUser extends DatastoreObject implements Serializable {
//	http://localhost:8080/collect?uid=1&cd11=2&cd30=testCity&cd69=testRegion&cd33=testCountry&ua=testOS&__bv=testosVersion&__dc=testDeviceType&__ul=testLang&pa=add

	// =====ID=====
	@IDField
	@JsonField(name = "uid")
	@Index
	private String uid;

	@IDField
	@JsonField(name = "cd11")
	@Index
	private String ga_clientid;

	@IDField
	@JsonField(name = "cd107")
	@Index
	private String mparticleuserid;

	@IDField
	@JsonField(name = "cd108")
	@Index
	private String customerid;

	@IDField
	@JsonField(name = "cd109")
	@Index
	private String facebookid;

	@IDField
	@JsonField(name = "cd110")
	@Index
	private String twitterid;

	@IDField
	@JsonField(name = "cd111")
	@Index
	private String googleuserid;

	@IDField
	@JsonField(name = "cd112")
	@Index
	private String microsoftuserid;

	@IDField
	@JsonField(name = "cd113")
	@Index
	private String yahoouserid;

	@IDField
	@JsonField(name = "cd114")
	@Index
	private String email;

	@IDField
	@JsonField(name = "cd115")
	@Index
	private String otheruserid;

	@IDField
	@JsonField(name = "cd116")
	@Index
	private String msisdn;

	@IDField
	@JsonField(name = "cd74")
	@Index
	private String iOSifaRaw;
	
	@IDField
	@JsonField(name = "cd68")
	@Index
	private String androidAidRaw;

	@IDField
	@JsonField(name = "cd102")
	@Index
	private String iOSifaSHA1;
	
	@IDField
	@JsonField(name = "cd103")
	@Index
	private String androidAidSHA1;
	
	@IDField
	@JsonField(name = "cd117")
	@Index
	private String amazonfireAID;
	
	@IDField
	@JsonField(name = "cd118")
	@Index
	private String windowsphoneAID;
	
	//TODO review this fields =====
//	@JsonField(name = "ios_ifa")
//	@Index
//	private String iOSifaTune;
//
//	@JsonField(name = "google_aid")
//	@Index
//	private String android;
//
//	@JsonField(name = "platform_aid")
//	@Index
//	private String amazonfire;
//
//	@JsonField(name = "windows_aid")
//	@Index
//	private String windowsphone;
	//=============================

	@IDField
	@JsonField(name = "cd8")
	@Index
	private String openUDIDSHA1;

	@IDField
	@JsonField(name = "cd40")
	@Index
	private String openUdidMD5;
	
	@IDField
	@JsonField(name = "cd119")
	@Index
	private String androidID;

	@IDField
	@JsonField(name = "cd7")
	@Index
	private String deviceIdSHA1;
	
	@IDField
	@JsonField(name = "cd38")
	@Index
	private String deviceIdMD5;

	@IDField
	@JsonField(name = "cd120")
	@Index
	private String macaddress;
	
	@IDField
	@JsonField(name = "cd42")
	@Index
	private String exchangeUID;
	// ==========

	@Index
	private Long frequency;
	
	private String engagementType;
	
	private Date latestHitTime;

	private Geography geography;

	private Demography demography;

	private Behavior behavior;

	private Category category;

	private Device device;

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
		latestHitTime = getHitTime(reqJson);

		// avoid empty or null values
		// TODO consider the sense
		if (uid == null || uid.isEmpty()) {
			uid = "empty_original_uid_" + UUID.randomUUID().toString();
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
	private static void setFieldsWithAnnotation(Object clazz, JSONObject reqJson) {
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

	// Checking JsonField annotation is exist in field
	private static boolean checkExistenceAnnotation(Field f) {
		Annotation[] annotations = f.getDeclaredAnnotations();
		for (int i = 0; i < annotations.length; i++) {
			if (annotations[i] instanceof JsonField) {
				return true;
			}
		}
		return false;
	}
	
	private static String getJsonValue(String[] jsonFieldName, JSONObject reqJson) {
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

	public static class Geography implements Serializable  {

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

		@Inject
		public Geography(JSONObject reqJson) {
			setFieldsWithAnnotation(this, reqJson);
		}
	}

	private static class Demography implements Serializable {
		
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

		@Inject
		Demography(JSONObject reqJson) {
			setFieldsWithAnnotation(this, reqJson);
		}
	}

	private static class Behavior implements Serializable  {

		// is describe new user or returning user.
		// new user - true, returning user - false
		Boolean entrance;
		// Number of transactions (0, 1, 2, etc)
		Long transactions;
		Long totalAmountSpent;
		// Number of iterations before purchase
		Long iterations;

		@Inject
		Behavior(JSONObject reqJson) {
			// TODO set fields
		}
	}

	private static class Category implements Serializable {
		String appOnDevice;

		@Inject
		Category(JSONObject reqJson) {
			// TODO set field
		}
	}

	private static class Device implements Serializable {
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

		@Inject
		Device(JSONObject reqJson) {
			setFieldsWithAnnotation(this, reqJson);
		}
	}

	private static class CRM implements Serializable {
		@JsonField(name = "cd123")
		String ispostpaid;
		String subscribedto;

		@Inject
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
		if(action == null) {
			engagementType = null;
		} else {
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
	
	private Date getHitTime(JSONObject reqJson) {
		Date date = new Date();
		date.setTime(reqJson.getLong("time"));
		return date;
	}

}