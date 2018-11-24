package agency.akcom.mmg.sherlock.ui.shared.dto;

public class TokenConnectionDto extends ConfigConnectionDto{
    String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public TokenConnectionDto(String name, String token) {
        this.name = name;
        this.token = token;
    }

    public TokenConnectionDto() {
    }

}
