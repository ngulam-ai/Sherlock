package agency.akcom.mmg.sherlock.ui.server.avazu.clients;

import agency.akcom.mmg.sherlock.ui.server.avazu.models.Auth;
import agency.akcom.mmg.sherlock.ui.server.avazu.models.AuthRequest;
import agency.akcom.mmg.sherlock.ui.server.avazu.models.Report;
import agency.akcom.mmg.sherlock.ui.server.avazu.models.ReportRequest;
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
