package agency.akcom.mmg.sherlock.collect.domain;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.UUID;

import javax.inject.Inject;

import org.json.JSONException;
import org.json.JSONObject;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Ignore;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Load;

import agency.akcom.mmg.sherlock.collect.AudienceService;
import agency.akcom.mmg.sherlock.collect.audience.AudUserAttribute;
import agency.akcom.mmg.sherlock.collect.audience.AudienceProcessing;
import agency.akcom.mmg.sherlock.collect.audience.Demography;
import agency.akcom.mmg.sherlock.collect.audience.Geography;
import agency.akcom.mmg.sherlock.collect.dao.AudUserAttributeDao;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Entity
@Data
@Slf4j
public class AudUser extends DatastoreObject implements Serializable {
//	http://localhost:8080/collect?uid=1&cd11=2&cd30=testCity&cd69=testRegion&cd33=testCountry&ua=testOS&__bv=testosVersion&__dc=testDeviceType&__ul=testLang&pa=add

	/**
	 * Criterias
OK•	Source: Could be any of the UTM parameters: Source, Medium, DSP, bid type, campaign name, creative, keyword, 
OK•	Event: could be AdView, AdClick, Conversion, LPView, etc
•	Membership duration
ok•	Recency
ok•	Campaign Activity: Create an audience based on the number of clicks, conversions,
and impressions (which are based on first-party remarketing lists or lead to conversions).
o	Audience Definition: 
	Campaign dimensions: Campaign name
	Campaign Metrics: AdViews, AdClicks, Conversions or any other event is below, above, equal to a certain value
ok•	Audience frequency cap: Create an audience excluding users based on the number of impressions they were served (across media, channels, and identity spaces).
ok•	Interest and Affinity
OK•	Behaviour
OK•	Geography
OK•	Demography
OK•	Point of interest
•	Apps on Device
•	App Usage
OK•	Device 
?•	Suppress 

	 */
	
	private String adserver_uid;
	
	@JsonField(name = "last_uid")
	private String last_uid;
	
	// =====ID=====
	@IDField
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
	
	@Scope
	@Load
	Ref<Geography> geography;
	@Scope
	@Load 
	Ref<Demography> demography;
	
	@Ignore
	Geography geo;
	
	private String engagementType;
	
	@Index
	private Date latestHitTime;

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
		createUid(reqJson);
		AudienceProcessing.setFieldsWithAnnotation(this, reqJson);
		latestHitTime = AudienceProcessing.getHitTime(reqJson);
		increaseFrequency();
		geo = new Geography(reqJson); //for test, will be delete after
	}
	
	//Creating additional entity for this class
	public void createFullAudUser(JSONObject reqJson) {
		Geography geo = new Geography(reqJson);
		this.geography = Ref.create(null);
	}

	public Long increaseFrequency() {
		frequency = (frequency == null) ? 1 : frequency + 1;
		return frequency;
	}

	public AudUser() {

	}
	
	public void createUid(JSONObject reqJson) {
		String uid = AudienceProcessing.getJsonValue(new String[] { "uid" }, reqJson);
		
		adserver_uid = AudienceProcessing.getJsonValue(new String[] { "adserver_uid" }, reqJson);
		if (adserver_uid == null) {
			adserver_uid = uid;
		}

		if (uid == null || uid.isEmpty()) {
			uid = UUID.randomUUID().toString();
		} else {
			try {
				this.uid = UUID.fromString(uid).toString();
			} catch (IllegalArgumentException e) {
				this.uid = UUID.nameUUIDFromBytes(uid.getBytes()).toString(); // If uid is provided wrong, like "Africa;Saldanha;unknown;Android...etc"
			}
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

//	private static class Device implements Serializable {
//		// TODO review this, I set field from device.* from hits data
//		@JsonField(name = "ua")
//		String os;
//		@JsonField(name = "__bv")
//		String osVersion;
//		@JsonField(name = "__dc")
//		String deviceType;
//		@JsonField(name = "ul, __ul")
//		String language;
//		// TODO set this field
//		String manufacturer;
//		@JsonField(name = "__dm")
//		String model;
//
//		@Inject
//		Device(JSONObject reqJson) {
//			setFieldsWithAnnotation(this, reqJson);
//		}
//	}

//	private static class CRM implements Serializable {
//		@JsonField(name = "cd123")
//		String ispostpaid;
//		String subscribedto;
//
//		@Inject
//		CRM(JSONObject reqJson) {
//			setFieldsWithAnnotation(this, reqJson);
//		}
//	}

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
	
	

}