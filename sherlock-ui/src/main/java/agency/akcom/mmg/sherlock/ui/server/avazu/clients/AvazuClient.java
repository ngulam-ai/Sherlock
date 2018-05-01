package agency.akcom.mmg.sherlock.ui.server.avazu.clients;

import agency.akcom.mmg.sherlock.ui.server.avazu.models.Auth;
import agency.akcom.mmg.sherlock.ui.server.avazu.models.Report;
import agency.akcom.mmg.sherlock.ui.server.avazu.models.ReportRequest;
import feign.Headers;
import feign.Param;
import feign.RequestLine;

public interface AvazuClient {
	@RequestLine("POST /auth/access_token?client_id={client_id}&client_secret={client_secret}&grant_type={grant_type}")
	Auth getAuth(@Param("client_id") String client_id, @Param("client_secret") String client_secret,
			@Param("grant_type") String grant_type);

	@RequestLine("POST /reporting")
	@Headers("Content-Type: application/json")
	Report getReport(ReportRequest reportRequest);

	//
	//
	// @RequestLine("GET /{isbn}")
	// BookResource findByIsbn(@Param("isbn") String isbn);
	//
	// @RequestLine("GET")
	// List<BookResource> findAll();
	//
	// @RequestLine("POST")
	// @Headers("Content-Type: application/json")
	// void create(Book book);
}
