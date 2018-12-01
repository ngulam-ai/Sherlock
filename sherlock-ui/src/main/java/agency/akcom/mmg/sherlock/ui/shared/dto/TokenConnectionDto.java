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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TokenConnectionDto that = (TokenConnectionDto) o;

        return token != null ? token.equals(that.token) : that.token == null;
    }

    @Override
    public int hashCode() {
        return token != null ? token.hashCode() : 0;
    }
}
