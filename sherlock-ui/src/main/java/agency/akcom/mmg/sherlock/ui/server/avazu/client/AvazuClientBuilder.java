package agency.akcom.mmg.sherlock.ui.server.avazu.client;

import java.io.IOException;
import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import feign.Feign;
import feign.Logger;
import feign.Response;
import feign.Util;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.okhttp.OkHttpClient;
import feign.slf4j.Slf4jLogger;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public class AvazuClientBuilder {
	private AvazuClient avazuClient = createClient(AvazuClient.class, "http://api.mdsp.avazutracking.net");
	
	static Gson gson = new GsonBuilder().setLenient().create();
	

    private static <T> T createClient(Class<T> type, String uri) {
        return Feign.builder()
            .client(new OkHttpClient())
            .encoder(new GsonEncoder())
          //  .decoder(new GsonDecoder(gson))
            .mapAndDecode((response, t) -> logThenDecode(response, t), new GsonDecoder(gson))
            .logger(new Slf4jLogger(type))
            .logLevel(Logger.Level.FULL)
            .target(type, uri);
    }

	private static Response logThenDecode(Response response, Type t) {
		try {
			String responseString = Util.toString(response.body().asReader());
			log.info(responseString);

			return response.toBuilder().body(responseString.getBytes()).build();

		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}

}
