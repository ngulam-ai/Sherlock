package agency.akcom.mmg.sherlock.collect.domain;

import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Entity
@Data
@Slf4j
public class AudUser extends DatastoreObject {

	@Index
	private String uid;

	@Index
	private Long frequency;

	// • Uid
	// • GA_ClientID
	// • mparticleuserid
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
	// • Geography
	// o Continent
	// o Country
	// o Carrier
	// o Region
	// o State
	// o City
	//
	// • Demography
	// o Gender
	// o Age
	// o Language
	// o Education_majority_ZIP
	// o Ethnicity_majority_ZIP
	// o Income_average_ZIP
	// o Unemployment_average_ZIP
	// o Crimes_average_ZIP
	// • Interests (refer to IAB_Tech_Lab_Content_Taxonomy_V2_Final_2017-11)
	// • Behaviors
	// o New Users vs Returning Users
	// o Number of transactions (0, 1, 2, etc)
	// o Total amount spent (0, between 0 and 3, between 3 and 5, bewteen 5 and 10,
	// more than 10, etc)
	// o Number of iterations before purchase
	// • Points of Interest (Seen on product lp)
	// • Web/App Category
	// o Apps on device
	// • Device
	// o OS
	// o OS Version
	// o Device Type
	// o Language
	// o Manufacturer
	// o Model
	//
	//
	// • Engagement type
	// o Session
	// o Impression (View-through)
	// o Click (Click-through)
	// o Engagement
	// o Purchase Intent
	// o Purchase
	// o Install
	// • CRM
	// o Ispostpaid
	// o Subscribedto
	// o …..others fields that will be defined later.
	//

	public AudUser(JSONObject reqJson) {
		try {
			uid = reqJson.getString("uid");
			// TODO add all other uids
			// ...
		} catch (JSONException e) {
			log.warn(e.getMessage());
		}

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

}
