package agency.akcom.mmg.sherlock.ui.server.avazu.client;

import agency.akcom.mmg.sherlock.ui.server.avazu.model.Auth;
import agency.akcom.mmg.sherlock.ui.server.avazu.model.AuthRequest;
import agency.akcom.mmg.sherlock.ui.server.avazu.model.Report;
import agency.akcom.mmg.sherlock.ui.server.avazu.model.ReportRequest;
import feign.Headers;
import feign.RequestLine;

public interface AvazuClient {

	@RequestLine("POST /auth/access_token")
	@Headers("Content-Type: application/json")
	Auth getAuth(AuthRequest authRequest);

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
