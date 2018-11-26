package agency.akcom.mmg.sherlock.ui.shared.dto;

import java.util.Objects;

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
        return Objects.equals(token, that.token);
    }

    @Override
    public int hashCode() {
        return Objects.hash(token);
    }
}
