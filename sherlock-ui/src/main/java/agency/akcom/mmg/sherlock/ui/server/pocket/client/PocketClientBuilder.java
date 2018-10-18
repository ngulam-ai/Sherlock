package agency.akcom.mmg.sherlock.ui.server.pocket.client;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;

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
public class PocketClientBuilder {
	
	PocketClient pocketClient = createClient(PocketClient.class, "https://api.pocketmath.com");
	@Getter
	private static Map<String, Collection<String>> headers;
	@Getter
	private static int status;

	static Gson gson = new GsonBuilder().setLenient().create();
			
	private <T> T createClient(Class<T> type, String uri) {
        return Feign.builder()
            .client(new OkHttpClient())
            .encoder(new GsonEncoder())
            .mapAndDecode((response, t) -> logThenDecode(response, t), new GsonDecoder(gson))
            .logger(new Slf4jLogger(type))
            .logLevel(Logger.Level.FULL)
            .target(type, uri);
	}

	public Response logThenDecode(Response response, Type type) {
		try {
			String responseString = Util.toString(response.body().asReader());
			log.info(responseString);
			headers = response.headers();
			status = response.status();
			return response.toBuilder().body(responseString.getBytes()).build();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
